package com.userxxx.core;

import soot.*;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleCFGBuilder {

    private final int threadCount;

    // 正向: 方法调用的方法集合
    private final Map<SootMethod, Set<SootMethod>> cfgEdges = Collections.synchronizedMap(new HashMap<>());
    // 逆向: 被谁调用的方法集合
    private final Map<SootMethod, Set<SootMethod>> reverseCfgEdges = Collections.synchronizedMap(new HashMap<>());

    // 字符串图（无须依赖 Soot 初始化即可读取/保存）
    private final Map<String, Set<String>> cfgEdgesStr = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, Set<String>> reverseCfgEdgesStr = Collections.synchronizedMap(new HashMap<>());

    private static final Object sootResolveLock = new Object();

    private static final String[] blacklistPrefixes = {
            "java.",
            "javax.",
            "sun.",
            "jdk.",
    };

    private boolean isBlacklistedPackage(SootClass sc) {
        String pkg = sc.getPackageName();
        if (pkg == null) return false;
        for (String prefix : blacklistPrefixes) {
            if (pkg.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    public SimpleCFGBuilder(int threadCount) {
        this.threadCount = threadCount;
    }

    private List<SootMethod> findConcreteImplementations(SootMethod absMethod) {
        List<SootMethod> impls = new ArrayList<>();
        String subSignature = absMethod.getSubSignature();
        SootClass base = absMethod.getDeclaringClass();

        Hierarchy hierarchy = Scene.v().getActiveHierarchy();
        Collection<SootClass> candidates;

        if (base.isInterface()) {
            candidates = hierarchy.getImplementersOf(base);
        } else {
            candidates = hierarchy.getSubclassesOf(base);
        }

        for (SootClass cls : candidates) {
            if (cls.isInterface() || cls.isPhantom() || cls.isAbstract()) continue;
            if (isBlacklistedPackage(cls)) continue;

            if (cls.declaresMethod(subSignature)) {
                SootMethod m = cls.getMethod(subSignature);
                if (m.isConcrete()) {
                    impls.add(m);
                }
            }
        }
        return impls;
    }

    public void buildMethodCallGraph() {
        ExecutorService executorService = Executors.newFixedThreadPool(this.threadCount);

        List<SootMethod> allMethods = new ArrayList<>();

        for (SootClass sootClass : Scene.v().getApplicationClasses()) {
            if (isBlacklistedPackage(sootClass)) continue;
            if (sootClass.isPhantom()) continue;

            for (SootMethod method : sootClass.getMethods()) {
                if (method.isConcrete()) {
                    allMethods.add(method);
                }
            }
        }

        for (SootMethod method : allMethods) {
            executorService.execute(() -> analyzeMethodCalls(method));
        }

        executorService.shutdown();
        while (!executorService.isTerminated()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("🎉 All Method Call CFG edges collected.");
    }

    private void analyzeMethodCalls(SootMethod caller) {
        try {
            cfgEdges.putIfAbsent(caller, Collections.synchronizedSet(new HashSet<>()));

            for (Unit unit : caller.retrieveActiveBody().getUnits()) {
                if (unit instanceof Stmt && ((Stmt) unit).containsInvokeExpr()) {
                    InvokeExpr invokeExpr = ((Stmt) unit).getInvokeExpr();
                    SootMethod callee;
                    synchronized (sootResolveLock) {
                        callee = invokeExpr.getMethod();
                    }
                    // 忽略掉 java 的方法
                    if (isBlacklistedPackage(callee.getDeclaringClass()))
                        continue;

                    List<SootMethod> tgts = new ArrayList<>();
                    if (callee.isAbstract()) {
                        tgts = findConcreteImplementations(callee);
                    } else {
                        tgts.add(callee);
                    }
                    for (SootMethod tgt : tgts) {
                        cfgEdges.get(caller).add(tgt);
                        reverseCfgEdges.putIfAbsent(tgt, Collections.synchronizedSet(new HashSet<>()));
                        reverseCfgEdges.get(tgt).add(caller);

                        // 同步维护字符串图，便于直接保存/读取
                        String callerSig = caller.getSignature();
                        String calleeSig = tgt.getSignature();
                        cfgEdgesStr.computeIfAbsent(callerSig, k -> Collections.synchronizedSet(new HashSet<>())).add(calleeSig);
                        reverseCfgEdgesStr.computeIfAbsent(calleeSig, k -> Collections.synchronizedSet(new HashSet<>())).add(callerSig);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error analyzing method: " + caller.getSignature());
            e.printStackTrace();
        }
    }

    // 保存 CFG（SootMethod 版本）到文件
    public void safeAllEdges() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("cfg.txt"))) {
            for (Map.Entry<SootMethod, Set<SootMethod>> entry : cfgEdges.entrySet()) {
                SootMethod caller = entry.getKey();
                for (SootMethod callee : entry.getValue()) {
                    writer.write(caller.getSignature() + " -> " + callee.getSignature() + "\n");
                }
            }
            writer.flush();
        } catch (IOException e) {
            System.err.println("⚠️ Error writing CFG edges to file.");
            e.printStackTrace();
        }
    }

    // 保存字符串图到文件（可与 safeAllEdges 等价结果）
    public void saveAllEdgesAsStrings(String path) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (Map.Entry<String, Set<String>> entry : cfgEdgesStr.entrySet()) {
                String caller = entry.getKey();
                for (String callee : entry.getValue()) {
                    writer.write(caller + " -> " + callee + "\n");
                }
            }
            writer.flush();
        } catch (IOException e) {
            System.err.println("⚠️ Error writing string CFG edges to file.");
            e.printStackTrace();
        }
    }

    // 从文件读取为字符串图（不依赖 Soot）
    public void loadEdgesFromFileAsStrings(String path) throws IOException {
        cfgEdgesStr.clear();
        reverseCfgEdgesStr.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                int arrow = line.indexOf("->");
                if (arrow < 0) continue;
                String caller = line.substring(0, arrow).trim();
                String callee = line.substring(arrow + 2).trim();
                cfgEdgesStr.computeIfAbsent(caller, k -> Collections.synchronizedSet(new HashSet<>())).add(callee);
                reverseCfgEdgesStr.computeIfAbsent(callee, k -> Collections.synchronizedSet(new HashSet<>())).add(caller);
            }
        }
    }

    // 工具：尝试用签名解析为 SootMethod（要求 Soot 场景已加载）
    private SootMethod tryResolveMethodBySignature(String sig) {
        try {
            // grabMethod: 若找不到返回 null，不会抛异常
            SootMethod m = Scene.v().grabMethod(sig);
            if (m != null) return m;

            // 退一步：解析出类名并强制加载，再次尝试
            // 签名格式: <pkg.Clazz: ret name(params)>
            int lt = sig.indexOf('<');
            int colon = sig.indexOf(':');
            int gt = sig.lastIndexOf('>');
            if (lt == -1 || colon == -1 || gt == -1) return null;
            String className = sig.substring(lt + 1, colon).trim();
            // 尝试加载类
            SootClass sc = null;
            try {
                sc = Scene.v().getSootClass(className);
            } catch (RuntimeException ignored) {
                // not loaded
            }
            if (sc == null) {
                sc = Scene.v().loadClassAndSupport(className);
                sc.setApplicationClass();
            }
            // 再次抓取
            return Scene.v().grabMethod(sig);
        } catch (Exception e) {
            return null;
        }
    }

    // 从文件读取并直接解析为 SootMethod 图（要求 Soot 场景已正确初始化）
    public void loadEdgesFromFile(String path) throws IOException {
        cfgEdges.clear();
        reverseCfgEdges.clear();

        // 同步维护字符串图
        cfgEdgesStr.clear();
        reverseCfgEdgesStr.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                int arrow = line.indexOf("->");
                if (arrow < 0) continue;
                String callerSig = line.substring(0, arrow).trim();
                String calleeSig = line.substring(arrow + 2).trim();

                cfgEdgesStr.computeIfAbsent(callerSig, k -> Collections.synchronizedSet(new HashSet<>())).add(calleeSig);
                reverseCfgEdgesStr.computeIfAbsent(calleeSig, k -> Collections.synchronizedSet(new HashSet<>())).add(callerSig);

                SootMethod caller = tryResolveMethodBySignature(callerSig);
                SootMethod callee = tryResolveMethodBySignature(calleeSig);
                if (caller == null || callee == null) {
                    // 跳过无法解析的条目，仍保留在字符串图中
                    continue;
                }
                cfgEdges.computeIfAbsent(caller, k -> Collections.synchronizedSet(new HashSet<>())).add(callee);
                reverseCfgEdges.computeIfAbsent(callee, k -> Collections.synchronizedSet(new HashSet<>())).add(caller);
            }
        }
    }

    // 在 Soot 场景已加载时，可用字符串图重建 SootMethod 图
    public void rebuildMethodGraphsFromStringMaps() {
        cfgEdges.clear();
        reverseCfgEdges.clear();
        for (Map.Entry<String, Set<String>> e : cfgEdgesStr.entrySet()) {
            String callerSig = e.getKey();
            SootMethod caller = tryResolveMethodBySignature(callerSig);
            if (caller == null) continue;
            for (String calleeSig : e.getValue()) {
                SootMethod callee = tryResolveMethodBySignature(calleeSig);
                if (callee == null) continue;
                cfgEdges.computeIfAbsent(caller, k -> Collections.synchronizedSet(new HashSet<>())).add(callee);
                reverseCfgEdges.computeIfAbsent(callee, k -> Collections.synchronizedSet(new HashSet<>())).add(caller);
            }
        }
    }

    public Map<SootMethod, Set<SootMethod>> getCfgEdges() {
        return cfgEdges;
    }

    public Map<SootMethod, Set<SootMethod>> getReverseCfgEdges() {
        return reverseCfgEdges;
    }

    public Map<String, Set<String>> getCfgEdgesStrings() {
        return cfgEdgesStr;
    }

    public Map<String, Set<String>> getReverseCfgEdgesStrings() {
        return reverseCfgEdgesStr;
    }
}