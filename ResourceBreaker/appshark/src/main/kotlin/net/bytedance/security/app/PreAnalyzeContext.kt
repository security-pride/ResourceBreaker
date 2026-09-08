/*
* Copyright 2022 Beijing Zitiao Network Technology Co., Ltd.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/


package net.bytedance.security.app

import net.bytedance.security.app.preprocess.*
import net.bytedance.security.app.rules.IRulesForContext
import net.bytedance.security.app.util.profiler
import soot.RefType
import soot.Scene
import soot.SootClass
import soot.SootField
import soot.SootMethod
import soot.SootMethodRef
import soot.VoidType
import soot.jimple.AssignStmt
import soot.jimple.InvokeExpr
import soot.jimple.Jimple
import soot.jimple.Stmt
import java.util.concurrent.atomic.AtomicInteger

/**
 * for jsb methods
 */
interface ContextWithJSBMethods {
    fun getJSBMethods(): List<SootMethod>
}

/**
 * The context before the pointer analysis ,it contains all the preprocessing information for the Java program.
 */

open class PreAnalyzeContext {

    /**
     * key is the function that is callee,value is the caller functions and the statement that occurs
     * Direct is the function that is called directly without considering CHA relationship
     * Heir is after considering CHA
     */
    val methodDirectRefs: MutableMap<SootMethod, MutableSet<CallSite>> = HashMap()

    /**
     * Key is the field to be loaded, and value is the callsite
     * for example a=b.c;
     */
    val loadFieldRefs: MutableMap<SootField, MutableSet<CallSite>> = HashMap()

    /**
     *Key is the field to be stored, and value is the callSite
     * for example a.b=c;
     */
    val storeFieldRefs: MutableMap<SootField, MutableSet<CallSite>> = HashMap()

    /**
    key is the pattern in the rule, and value is the place where possible matching constant strings appear
     */
    var constStringPatternMap: MutableMap<String, MutableSet<CallSite>> = HashMap()


    val newInstanceRefs: MutableMap<SootClass, MutableSet<CallSite>> = HashMap()


    val callGraph = CallGraph()


    private var classCounter: AtomicInteger = AtomicInteger(0)
    private var methodsCounter: AtomicInteger = AtomicInteger(0)


    fun addMethodCounter(): Int {
        return methodsCounter.incrementAndGet()
    }

    fun addClassCounter(): Int {
        return classCounter.incrementAndGet()
    }

    fun getMethodCounter(): Int {
        return methodsCounter.get()
    }

    fun getClassCounter(): Int {
        return classCounter.get()
    }

    suspend fun createContext(
        rules: IRulesForContext,
        callBackEnhance: Boolean
    ) {
        patchCallbackPassthroughsInJimple()
        val cam = createClassAndMethodHandler(this)
        addClassAndMethodVisitor(cam, rules, callBackEnhance)
        cam.run()
        profiler.initProcessMethodStatistics(getMethodCounter(), getClassCounter(), this)
    }

    fun buildCustomClassCallGraph(rules: IRulesForContext) {
        val cam = createClassAndMethodHandler(this)
        addClassAndMethodVisitor(cam, rules, false)
        cam.buildCustomClassCallGraph()
    }

    private fun createClassAndMethodHandler(ctx: PreAnalyzeContext): AnalyzePreProcessor {
        return AnalyzePreProcessor(getConfig().getMaxPreprocessorThread(), ctx)
    }

    protected open fun addClassAndMethodVisitor(
        cam: AnalyzePreProcessor, rules: IRulesForContext,
        callBackEnhance: Boolean
    ) {

//        val constStrPatternInRules = MethodFieldConstCacheVisitor.parseAllConstStrPatternInRules(ruleDir, ruleList)
        cam.addMethodVisitor {
            //1. ssa Make sure SSA is at the first
            MethodVisitorStatistics(MethodSSAVisitor())
        }.addMethodVisitor {
            //2. The callback must be handled after the SSA, otherwise the function doesn't have body
            MethodVisitorStatistics(MethodCallbackVisitor(callBackEnhance))
        }.addMethodVisitor {
            //3.  MethodFieldConstCacheVisitor must be handled after ssa, because there are dependencies
            MethodFieldConstCacheVisitor(
                this,
                MethodStmtFieldCache(),
                rules.constStringPatterns(),
                rules.fields(),
                rules.newInstances()
            )
        }.addMethodVisitor {
            MethodCounter(this)
        }
        cam.addClassVisitor { ClassCounter(this) }
    }

    @Suppress("unused", "unused")
    fun queryAMethod(method: SootMethod, result: MutableMap<SootMethod, Set<SootMethod>>, depth: Int) {
        if (depth <= 0) {
            return
        }
        if (result.containsKey(method)) {
            return
        }
        if (callGraph.heirReverseCallGraph.containsKey(method)) {
            result[method] = callGraph.heirReverseCallGraph[method]!!
            for (m in callGraph.heirReverseCallGraph[method]!!) {
                queryAMethod(m, result, depth - 1)
            }
        }
    }


    @Suppress("unused")
    fun findInvokeCallSite(methodSig: String): Set<CallSite> {
        val m = Scene.v().getMethod(methodSig)
        return findInvokeCallSite(m)
    }

    fun findInvokeCallSite(method: SootMethod): Set<CallSite> {
        return this.methodDirectRefs[method] ?: setOf()
    }

    fun findConstStringPatternCallSite(patternStr: String): Set<CallSite> {
        return this.constStringPatternMap[patternStr] ?: setOf()
    }

    fun findInstantCallSite(className: String): Set<CallSite> {
        val clz = Scene.v().getSootClassUnsafe(className) ?: return emptySet()
        return findInstantCallSite(clz)
    }

    fun findInstantCallSite(clz: SootClass): Set<CallSite> {
        return this.newInstanceRefs[clz] ?: setOf()
    }

    fun findInstantCallSiteWithSubclass(className: String): Set<CallSite> {
        val s = HashSet<CallSite>()
        for (sc in PLUtils.classes) {
            if (sc.name == className || sc.hasSuperclass() && className == sc.superclass.name) {
                s.addAll(findInstantCallSite(sc))
            }
        }
        return s
    }


    /**
     * field load callsites
     */
    fun findFieldCallSite(field: String): Set<CallSite> {
        val fields = MethodFinder.checkAndParseFieldSignature(field)
        val results = HashSet<CallSite>()
        for (f in fields) {
            results.addAll(findFieldCallSite(f))
        }
        return results
    }

    /**
     * field load callsites
     */
    fun findFieldCallSite(field: SootField): Set<CallSite> {
        return this.loadFieldRefs[field] ?: setOf()
    }


    /**
     * 直接 patch Jimple 方法体：
     * 把  $r6 = virtualinvoke $r3.binderWithCleanCallingIdentity($r5)
     * 替换为  $r6 = interfaceinvoke/virtualinvoke $r5.getOrThrow()
     *
     * 这样后续的 call graph 构建和污点分析就能直接看到对 SAM 方法的调用，
     * 进而穿透进 lambda 体（ExternalSyntheticLambdaXX → lambda$findAdmin$5）。
     */
    fun patchCallbackPassthroughsInJimple() {
        val tag = "[PATCH-CB]"
        Log.logInfo("$tag === start === classes=${PLUtils.classes.size}")

        // ========== 诊断：先找 findAdmin，dump 它的 invoke 语句 ==========
        for (cls in PLUtils.classes) {
            if (cls.name.contains("\$") || !cls.name.endsWith("DevicePolicyManagerService")) continue
            for (m in cls.methods) {
                if (m.name != "findAdmin") continue
                Log.logInfo("$tag DIAG findAdmin: ${m.signature}")
                Log.logInfo("$tag   concrete=${m.isConcrete} hasActive=${m.hasActiveBody()}")
                try {
                    val b = m.retrieveActiveBody()
                    for (u in b.units) {
                        val s = u as? soot.jimple.Stmt ?: continue
                        if (s.containsInvokeExpr()) {
                            Log.logInfo("$tag   INVOKE: ${s.invokeExpr}")
                        }
                    }
                } catch (e: Exception) {
                    Log.logInfo("$tag   body error: ${e.javaClass.simpleName}: ${e.message}")
                }
            }
        }

        // ========== 正式 patch ==========
        val wrapperNames = setOf("binderWithCleanCallingIdentity", "withCleanCallingIdentity")
        var scanned = 0
        var patched = 0

        for (cls in PLUtils.classes) {
            val methods = try { ArrayList(cls.methods) } catch (_: Exception) { continue }
            for (method in methods) {
                if (!method.isConcrete) continue
                val body = try {
                    method.retrieveActiveBody()
                } catch (_: Exception) { continue }

                scanned++
                val snapshot = ArrayList(body.units)

                for (unit in snapshot) {
                    val stmt = unit as? soot.jimple.Stmt ?: continue
                    if (!stmt.containsInvokeExpr()) continue

                    val invoke = stmt.invokeExpr
                    val callee = try { invoke.method } catch (_: Exception) { continue }
                    if (callee.name !in wrapperNames) continue
                    if (invoke.argCount < 1) continue

                    val funcArg = invoke.getArg(0)
                    if (funcArg !is soot.Local) {
                        Log.logInfo("$tag skip: arg0 not Local (${funcArg.javaClass.simpleName}) in ${method.signature}")
                        continue
                    }
                    val argRefType = funcArg.type as? RefType
                    if (argRefType == null) {
                        Log.logInfo("$tag skip: arg type not RefType (${funcArg.type}) in ${method.signature}")
                        continue
                    }
                    val argClass = argRefType.sootClass

                    val isVoid = callee.returnType == VoidType.v()
                    val samName = if (isVoid) "runOrThrow" else "getOrThrow"

                    Log.logInfo("$tag HIT in ${method.signature}")
                    Log.logInfo("$tag   callee=${callee.signature}")
                    Log.logInfo("$tag   funcArg=$funcArg type=${argClass.name} isInterface=${argClass.isInterface}")
                    Log.logInfo("$tag   samName=$samName")

                    // 在具体类上找 SAM
                    var sam: SootMethod? = null
                    try {
                        sam = argClass.methods.firstOrNull { it.name == samName }
                    } catch (_: Exception) {}

                    // fallback: 接口上找
                    if (sam == null) {
                        try {
                            for (iface in argClass.interfaces) {
                                sam = iface.methods.firstOrNull { it.name == samName }
                                if (sam != null) break
                            }
                        } catch (_: Exception) {}
                    }

                    if (sam == null) {
                        Log.logInfo("$tag   FAIL: SAM '$samName' not found!")
                        try {
                            Log.logInfo("$tag   argClass methods: ${argClass.methods.map { it.subSignature }}")
                            Log.logInfo("$tag   argClass interfaces: ${argClass.interfaces.map { it.name }}")
                        } catch (_: Exception) {}
                        continue
                    }

                    Log.logInfo("$tag   SAM resolved: ${sam.signature}")

                    try {
                        val newInvoke = if (argClass.isInterface) {
                            Jimple.v().newInterfaceInvokeExpr(funcArg, sam.makeRef())
                        } else {
                            Jimple.v().newVirtualInvokeExpr(funcArg, sam.makeRef())
                        }

                        val newStmt: soot.Unit = if (stmt is soot.jimple.AssignStmt) {
                            Jimple.v().newAssignStmt(stmt.leftOp, newInvoke)
                        } else {
                            Jimple.v().newInvokeStmt(newInvoke)
                        }

                        body.units.insertBefore(newStmt, unit)
                        unit.redirectJumpsToThisTo(newStmt)
                        body.units.remove(unit)
                        patched++
                        Log.logInfo("$tag   PATCHED!")
                    } catch (e: Exception) {
                        Log.logInfo("$tag   PATCH ERROR: ${e.javaClass.simpleName}: ${e.message}")
                    }
                }
            }
        }

        // ========== 验证：再 dump 一次 findAdmin ==========
        for (cls in PLUtils.classes) {
            if (cls.name.contains("\$") || !cls.name.endsWith("DevicePolicyManagerService")) continue
            for (m in cls.methods) {
                if (m.name != "findAdmin" || !m.hasActiveBody()) continue
                Log.logInfo("$tag VERIFY findAdmin after patch:")
                for (u in m.activeBody.units) {
                    val s = u as? soot.jimple.Stmt ?: continue
                    if (s.containsInvokeExpr()) {
                        Log.logInfo("$tag   INVOKE: ${s.invokeExpr}")
                    }
                }
            }
        }

        Log.logInfo("$tag === done: scanned=$scanned patched=$patched ===")
    }

    /**
     * 为 funcArg.samMethod() 构造最精确的 InvokeExpr：
     * - funcArg 声明类型是具体类 → virtualinvoke（直接命中实现，无需 CHA）
     * - funcArg 声明类型是接口   → interfaceinvoke（需 CHA 解析）
     */
    private fun buildSamInvoke(funcArg: soot.Local, interfaceSamRef: SootMethodRef): InvokeExpr {
        try {
            val argType = funcArg.type
            if (argType is RefType && !argType.sootClass.isInterface) {
                // funcArg 的声明类型就是具体的 lambda 类（如 ExternalSyntheticLambda64）
                val cls = argType.sootClass
                val samName = interfaceSamRef.name()
                val paramCount = interfaceSamRef.parameterTypes().size
                val impl = cls.methods.firstOrNull {
                    it.name == samName && it.parameterCount == paramCount && it.isConcrete
                }
                if (impl != null) {
                    // virtualinvoke $r5.<ExternalSyntheticLambda64: Object getOrThrow()>()
                    return Jimple.v().newVirtualInvokeExpr(funcArg, impl.makeRef())
                }
            }
        } catch (_: Exception) { /* fall through */ }
        // fallback: interfaceinvoke $r5.<ThrowingSupplier: Object getOrThrow()>()
        return Jimple.v().newInterfaceInvokeExpr(funcArg, interfaceSamRef)
    }
}