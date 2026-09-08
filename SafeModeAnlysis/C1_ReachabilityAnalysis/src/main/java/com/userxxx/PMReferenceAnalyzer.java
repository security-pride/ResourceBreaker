package com.userxxx;

import soot.*;
import soot.jimple.*;
import soot.options.Options;

import java.io.*;
import java.util.*;

/**
 * PM Resource Reference 分析器 - 完整版
 *
 * 功能：
 * 1. 追踪 TypedArray/XmlParser 资源提取
 * 2. 追踪 meta-data 解析（包括 XML 属性索引）
 * 3. 追踪 Bundle 内容
 * 4. 建立 ParsedXxx → XxxInfo 映射
 */
public class PMReferenceAnalyzer {

    // ==================== 资源提取方法配置 ====================

    private static final Map<String, Map<String, Boolean>> RESOURCE_EXTRACT_METHODS = new HashMap<>();
    static {
        // TypedArray 方法
        Map<String, Boolean> typedArrayMethods = new HashMap<>();
        typedArrayMethods.put("getResourceId", true);
        typedArrayMethods.put("getDrawable", true);
        typedArrayMethods.put("getColorStateList", true);
        typedArrayMethods.put("getString", false);
        typedArrayMethods.put("getNonResourceString", false);
        typedArrayMethods.put("getNonConfigurationString", false);
        typedArrayMethods.put("getBoolean", false);
        typedArrayMethods.put("getInt", false);
        typedArrayMethods.put("getInteger", false);
        typedArrayMethods.put("getFloat", false);
        typedArrayMethods.put("getText", false);
        typedArrayMethods.put("peekValue", false);  // 返回 TypedValue
        typedArrayMethods.put("getDimension", false);
        typedArrayMethods.put("getDimensionPixelSize", false);
        typedArrayMethods.put("getDimensionPixelOffset", false);
        typedArrayMethods.put("getColor", false);
        typedArrayMethods.put("getLayoutDimension", false);
        typedArrayMethods.put("getFraction", false);
        typedArrayMethods.put("hasValue", false);
        typedArrayMethods.put("hasValueOrEmpty", false);
        RESOURCE_EXTRACT_METHODS.put("android.content.res.TypedArray", typedArrayMethods);

        // XmlResourceParser 方法
        Map<String, Boolean> xmlParserMethods = new HashMap<>();
        xmlParserMethods.put("getAttributeResourceValue", true);
        xmlParserMethods.put("getAttributeValue", false);
        xmlParserMethods.put("getAttributeIntValue", false);
        xmlParserMethods.put("getAttributeBooleanValue", false);
        xmlParserMethods.put("getAttributeUnsignedIntValue", false);
        xmlParserMethods.put("getAttributeFloatValue", false);
        xmlParserMethods.put("getAttributeNameResource", true);
        xmlParserMethods.put("getIdAttributeResourceValue", true);
        RESOURCE_EXTRACT_METHODS.put("android.content.res.XmlResourceParser", xmlParserMethods);
        RESOURCE_EXTRACT_METHODS.put("org.xmlpull.v1.XmlPullParser", xmlParserMethods);

        // AttributeSet 方法
        Map<String, Boolean> attrSetMethods = new HashMap<>();
        attrSetMethods.put("getAttributeResourceValue", true);
        attrSetMethods.put("getAttributeValue", false);
        attrSetMethods.put("getAttributeIntValue", false);
        attrSetMethods.put("getAttributeBooleanValue", false);
        RESOURCE_EXTRACT_METHODS.put("android.util.AttributeSet", attrSetMethods);

        // Resources 方法
        Map<String, Boolean> resourcesMethods = new HashMap<>();
        resourcesMethods.put("getDrawable", true);
        resourcesMethods.put("getString", true);
        resourcesMethods.put("getText", true);
        resourcesMethods.put("getColor", true);
        resourcesMethods.put("getColorStateList", true);
        resourcesMethods.put("getInteger", false);
        resourcesMethods.put("getBoolean", false);
        resourcesMethods.put("getStringArray", false);
        resourcesMethods.put("getIntArray", false);
        RESOURCE_EXTRACT_METHODS.put("android.content.res.Resources", resourcesMethods);
    }

    // 资源相关字段访问
    private static final Map<String, Map<String, Boolean>> RESOURCE_FIELDS = new HashMap<>();
    static {
        Map<String, Boolean> typedValueFields = new HashMap<>();
        typedValueFields.put("resourceId", true);
        typedValueFields.put("data", false);
        typedValueFields.put("string", false);
        typedValueFields.put("type", false);
        RESOURCE_FIELDS.put("android.util.TypedValue", typedValueFields);
    }

    // Bundle 类
    private static final Set<String> BUNDLE_CLASSES = new HashSet<>(Arrays.asList(
            "android.os.Bundle",
            "android.os.BaseBundle",
            "android.os.PersistableBundle"
    ));

    // ==================== 数据结构 ====================

    /**
     * 值追踪信息
     */
    private static class ValueInfo {
        String sourceMethod;      // 提取方法名
        String sourceClass;       // 提取方法所属类
        String attrIndex;         // 属性索引 (如 index_0, R.styleable.xxx)
        boolean isReference;      // 是否资源引用
        boolean isContainer;      // 是否容器类型
        String containerType;     // 容器类型名
        String bundleKey;         // Bundle 中的 key
        String xmlAttrName;       // XML 属性名 (如 "name", "value", "resource")
        String xmlTagName;        // XML 标签名 (如 "meta-data")

        ValueInfo(String method, String clazz, String attr, boolean isRef) {
            this.sourceMethod = method;
            this.sourceClass = clazz;
            this.attrIndex = attr;
            this.isReference = isRef;
            this.isContainer = false;
        }

        static ValueInfo container(String type) {
            ValueInfo v = new ValueInfo("", "", "", false);
            v.isContainer = true;
            v.containerType = type;
            return v;
        }

        ValueInfo copy() {
            ValueInfo v = new ValueInfo(sourceMethod, sourceClass, attrIndex, isReference);
            v.isContainer = isContainer;
            v.containerType = containerType;
            v.bundleKey = bundleKey;
            v.xmlAttrName = xmlAttrName;
            v.xmlTagName = xmlTagName;
            return v;
        }
    }

    /**
     * meta-data 条目信息
     */
    public static class MetaDataEntry {
        public String name;              // android:name 的值
        public String nameAttrIndex;     // name 属性的 styleable 索引
        public String valueAttrIndex;    // value 属性的 styleable 索引
        public String resourceAttrIndex; // resource 属性的 styleable 索引
        public boolean hasValue;         // 是否有 value 属性
        public boolean hasResource;      // 是否有 resource 属性
        public String extractMethod;     // 提取方法 (getResourceId, peekValue, etc.)
        public boolean isResourceRef;    // 是否为资源引用
        public String sourceMethod;      // 解析方法签名

        @Override
        public String toString() {
            return String.format("MetaData[name=%s, value=%s, resource=%s, isRef=%s]",
                    name, valueAttrIndex, resourceAttrIndex, isResourceRef);
        }
    }

    /**
     * Phase 1 结果: ParsedXxx 字段信息
     */
    public static class ParsedFieldInfo {
        public String parsedClass;
        public String fieldName;
        public String setterMethod;
        public String extractMethod;
        public String extractClass;
        public boolean isReference;
        public String styleableAttr;
        public String sourceMethod;
        public boolean isFromBundle;
        public String bundleKey;
        public boolean isMetaData;
        public String xmlTagName;        // 新增：XML 标签名

        public String getKey() {
            return parsedClass + "." + fieldName;
        }
    }

    /**
     * Phase 2 结果: 完整映射
     */
    public static class FieldMapping {
        public String infoClass;
        public String infoField;
        public String parsedClass;
        public String parsedField;
        public String parsedGetter;
        public String extractMethod;
        public String extractClass;
        public boolean isReference;
        public String styleableAttr;
        public String parseSourceMethod;
        public boolean isFromBundle;
        public String bundleKey;
        public boolean isMetaData;
        public String generateMethod;

        public String toCsv() {
            return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,\"%s\",\"%s\"",
                    escapeCsv(infoClass),
                    escapeCsv(infoField),
                    escapeCsv(parsedClass),
                    escapeCsv(parsedField),
                    escapeCsv(parsedGetter),
                    escapeCsv(extractClass),
                    escapeCsv(extractMethod),
                    isReference,
                    escapeCsv(styleableAttr),
                    isFromBundle,
                    isMetaData,
                    escapeCsv(bundleKey),
                    escapeCsv(parseSourceMethod),
                    escapeCsv(generateMethod));
        }

        private String escapeCsv(String v) {
            if (v == null) return "";
            if (v.contains(",") || v.contains("\"") || v.contains("\n")) {
                return "\"" + v.replace("\"", "\"\"") + "\"";
            }
            return v;
        }
    }

    /**
     * Bundle 内容追踪
     */
    private static class BundleContent {
        Map<String, ValueInfo> entries = new LinkedHashMap<>();
        String assignedToField;
        String assignedToClass;
        boolean isMetaData;
        List<MetaDataEntry> metaDataEntries = new ArrayList<>();  // 新增
    }

    private static class CallInfo {
        String callerMethod;
        Map<Integer, String> paramValues = new HashMap<>();

        CallInfo(String caller) {
            this.callerMethod = caller;
        }
    }

    private static class GetterInfo {
        String parsedClass;
        String fieldName;
        String getterMethod;
    }

    // ==================== 成员变量 ====================

    private Config config;
    private Map<String, ParsedFieldInfo> parsedFieldInfos = new LinkedHashMap<>();
    private Map<String, BundleContent> bundleContents = new LinkedHashMap<>();
    private List<MetaDataEntry> allMetaDataEntries = new ArrayList<>();  // 新增：所有 meta-data 条目
    private List<FieldMapping> fieldMappings = new ArrayList<>();
    private Map<String, SootClass> classCache = new HashMap<>();
    private Map<String, List<CallInfo>> methodCallGraph = new HashMap<>();
    private Map<String, Set<String>> inheritanceCache = new HashMap<>();

    // ==================== 构造函数 ====================

    public PMReferenceAnalyzer(Config config) {
        this.config = config;
    }

    // ==================== 初始化 ====================

    public void initialize() {
        System.out.println("[*] Initializing Soot...");

        G.reset();

        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_android_jars(config.getAndroidJarPath());
        Options.v().set_process_dir(Arrays.asList(config.getTarget()));
        Options.v().set_force_android_jar(config.getAndroidJarPath());
        Options.v().set_process_multiple_dex(true);
        Options.v().set_output_format(Options.output_format_none);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_whole_program(true);
        Options.v().set_keep_line_number(true);

        Scene.v().loadNecessaryClasses();

        for (SootClass sc : Scene.v().getClasses()) {
            classCache.put(sc.getName(), sc);
        }

        buildInheritanceCache();

        System.out.println("[*] Loaded " + Scene.v().getApplicationClasses().size() + " classes");
    }

    private void buildInheritanceCache() {
        for (SootClass sc : Scene.v().getClasses()) {
            Set<String> supers = new HashSet<>();
            collectSuperTypes(sc, supers);
            inheritanceCache.put(sc.getName(), supers);
        }
    }

    private void collectSuperTypes(SootClass sc, Set<String> result) {
        if (sc == null) return;
        result.add(sc.getName());
        for (SootClass iface : sc.getInterfaces()) {
            if (!result.contains(iface.getName())) {
                collectSuperTypes(iface, result);
            }
        }
        if (sc.hasSuperclass()) {
            SootClass superClass = sc.getSuperclass();
            if (!result.contains(superClass.getName())) {
                collectSuperTypes(superClass, result);
            }
        }
    }

    private boolean isParsingClass(String name) {
        return name.contains("PackageParser") ||
                name.contains("pm.pkg.parsing") ||
                name.contains("pm.pkg.component");
    }

    private boolean isInfoUtilsClass(String name) {
        return name.contains("PackageInfoUtils") ||
                name.contains("pm.parsing.PackageInfoUtils");
    }

    // ==================== Phase 0: 构建调用图 ====================

    public void buildCallGraph() {
        System.out.println("\n[Phase 0] Building call graph...");

        for (SootClass sc : Scene.v().getApplicationClasses()) {
            if (!isParsingClass(sc.getName())) continue;

            for (SootMethod method : sc.getMethods()) {
                if (!method.isConcrete()) continue;

                try {
                    Body body = method.retrieveActiveBody();
                    collectCallSites(method, body);
                } catch (Exception e) {}
            }
        }

        System.out.println("[*] Call graph: " + methodCallGraph.size() + " methods");
    }

    private void collectCallSites(SootMethod caller, Body body) {
        for (Unit unit : body.getUnits()) {
            Stmt stmt = (Stmt) unit;
            if (!stmt.containsInvokeExpr()) continue;

            InvokeExpr invoke = stmt.getInvokeExpr();
            String calleeSig = invoke.getMethod().getSignature();

            CallInfo info = new CallInfo(caller.getSignature());
            for (int i = 0; i < invoke.getArgCount(); i++) {
                String val = resolveValue(invoke.getArg(i), body);
                if (val != null) info.paramValues.put(i, val);
            }

            if (!info.paramValues.isEmpty()) {
                methodCallGraph.computeIfAbsent(calleeSig, k -> new ArrayList<>()).add(info);
            }
        }
    }

    private String resolveValue(Value v, Body body) {
        if (v instanceof IntConstant) return "index_" + ((IntConstant) v).value;
        if (v instanceof StringConstant) return ((StringConstant) v).value;
        if (v instanceof StaticFieldRef) return ((StaticFieldRef) v).getField().getName();
        if (v instanceof Local) {
            for (Unit u : body.getUnits()) {
                if (u instanceof AssignStmt) {
                    AssignStmt a = (AssignStmt) u;
                    if (a.getLeftOp().equals(v)) {
                        if (a.getRightOp() instanceof IntConstant)
                            return "index_" + ((IntConstant) a.getRightOp()).value;
                        if (a.getRightOp() instanceof StringConstant)
                            return ((StringConstant) a.getRightOp()).value;
                        if (a.getRightOp() instanceof StaticFieldRef)
                            return ((StaticFieldRef) a.getRightOp()).getField().getName();
                    }
                }
            }
        }
        return null;
    }

    // ==================== Phase 1: 分析解析代码 ====================

    public void analyzeParsingCode() {
        System.out.println("\n[Phase 1] Analyzing parsing code...");

        int methodCount = 0;

        for (SootClass sc : Scene.v().getApplicationClasses()) {
            if (!isParsingClass(sc.getName())) continue;

            for (SootMethod method : sc.getMethods()) {
                if (!method.isConcrete()) continue;

                try {
                    Body body = method.retrieveActiveBody();
                    String methodName = method.getName().toLowerCase();

                    // 专门分析 meta-data 解析方法
                    if (methodName.contains("metadata") || methodName.contains("meta_data") ||
                            methodName.contains("parsebundle") || isMetaDataMethod(body)) {
                        analyzeMetaDataMethod(method, body);
                        methodCount++;
                    }
                    // 分析其他解析方法
                    else if (hasResourceUsage(body)) {
                        analyzeParseMethod(method, body);
                        methodCount++;
                    }
                } catch (Exception e) {}
            }
        }

        System.out.println("[*] Analyzed " + methodCount + " methods");
        System.out.println("[*] Found " + parsedFieldInfos.size() + " ParsedXxx fields");
        System.out.println("[*] Found " + allMetaDataEntries.size() + " meta-data entries");
    }

    /**
     * 检查是否为 meta-data 解析方法
     */
    private boolean isMetaDataMethod(Body body) {
        for (Unit unit : body.getUnits()) {
            Stmt stmt = (Stmt) unit;

            // 检查字符串常量
            if (stmt instanceof AssignStmt) {
                Value rhs = ((AssignStmt) stmt).getRightOp();
                if (rhs instanceof StringConstant) {
                    String val = ((StringConstant) rhs).value;
                    if (val.equals("meta-data") || val.equals("metadata")) {
                        return true;
                    }
                }
            }

            // 检查 equals("meta-data") 调用
            if (stmt.containsInvokeExpr()) {
                InvokeExpr invoke = stmt.getInvokeExpr();
                if (invoke.getMethod().getName().equals("equals") && invoke.getArgCount() > 0) {
                    Value arg = invoke.getArg(0);
                    if (arg instanceof StringConstant) {
                        String val = ((StringConstant) arg).value;
                        if (val.equals("meta-data") || val.equals("metadata")) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 分析 meta-data 解析方法 - 提取 XML 属性索引
     */
    private void analyzeMetaDataMethod(SootMethod method, Body body) {
        System.out.println("  [meta-data] Analyzing: " + method.getName());

        Map<Value, ValueInfo> trackedValues = new HashMap<>();
        Map<Value, BundleContent> trackedBundles = new HashMap<>();
        Map<Value, String> stringValues = new HashMap<>();

        // 追踪 TypedArray 变量
        Map<Value, String> typedArrayVars = new HashMap<>();

        // 当前 meta-data 条目
        MetaDataEntry currentEntry = null;

        for (Unit unit : body.getUnits()) {
            Stmt stmt = (Stmt) unit;

            if (stmt instanceof AssignStmt) {
                AssignStmt assign = (AssignStmt) stmt;
                Value lhs = assign.getLeftOp();
                Value rhs = assign.getRightOp();

                // 追踪字符串常量
                if (rhs instanceof StringConstant) {
                    stringValues.put(lhs, ((StringConstant) rhs).value);
                }

                // 追踪 obtainAttributes 返回的 TypedArray
                if (rhs instanceof InvokeExpr) {
                    InvokeExpr invoke = (InvokeExpr) rhs;
                    String calledName = invoke.getMethod().getName();

                    if (calledName.equals("obtainAttributes") || calledName.equals("obtainStyledAttributes")) {
                        typedArrayVars.put(lhs, "TypedArray");
                        // 开始新的 meta-data 条目
                        currentEntry = new MetaDataEntry();
                        currentEntry.sourceMethod = method.getSignature();
                    }

                    // 检查 TypedArray 方法调用
                    if (invoke instanceof InstanceInvokeExpr) {
                        Value base = ((InstanceInvokeExpr) invoke).getBase();
                        if (typedArrayVars.containsKey(base)) {
                            ValueInfo info = analyzeTypedArrayCall(invoke, currentEntry);
                            if (info != null) {
                                trackedValues.put(lhs, info);
                            }
                        }
                    }

                    // 检查其他资源提取
                    ValueInfo info = checkResourceExtraction(invoke, new HashMap<>());
                    if (info != null) {
                        trackedValues.put(lhs, info);
                    }
                }

                // 容器创建
                if (rhs instanceof NewExpr) {
                    String type = ((NewExpr) rhs).getType().toString();
                    if (isBundleClass(type)) {
                        BundleContent bc = new BundleContent();
                        bc.isMetaData = true;
                        trackedBundles.put(lhs, bc);
                    }
                }

                // 变量传递
                if (rhs instanceof Local) {
                    if (trackedValues.containsKey(rhs)) {
                        trackedValues.put(lhs, trackedValues.get(rhs).copy());
                    }
                    if (trackedBundles.containsKey(rhs)) {
                        trackedBundles.put(lhs, trackedBundles.get(rhs));
                    }
                    if (stringValues.containsKey(rhs)) {
                        stringValues.put(lhs, stringValues.get(rhs));
                    }
                    if (typedArrayVars.containsKey(rhs)) {
                        typedArrayVars.put(lhs, typedArrayVars.get(rhs));
                    }
                }

                // TypedValue.resourceId 访问
                if (rhs instanceof InstanceFieldRef) {
                    InstanceFieldRef ref = (InstanceFieldRef) rhs;
                    String fieldName = ref.getField().getName();
                    String className = ref.getField().getDeclaringClass().getName();

                    if (fieldName.equals("resourceId") && className.contains("TypedValue")) {
                        ValueInfo info = trackedValues.get(ref.getBase());
                        if (info != null) {
                            ValueInfo newInfo = info.copy();
                            newInfo.sourceMethod = "resourceId(field)";
                            newInfo.isReference = true;
                            trackedValues.put(lhs, newInfo);

                            // 更新 currentEntry
                            if (currentEntry != null) {
                                currentEntry.isResourceRef = true;
                                currentEntry.extractMethod = "TypedValue.resourceId";
                            }
                        }
                    }
                }

                // 字段赋值
                if (lhs instanceof InstanceFieldRef) {
                    InstanceFieldRef ref = (InstanceFieldRef) lhs;
                    String targetClass = ref.getField().getDeclaringClass().getName();
                    String fieldName = ref.getField().getName();

                    // Bundle 赋值到 metaData 字段
                    BundleContent bundle = trackedBundles.get(rhs);
                    if (bundle != null && isParsedClass(targetClass)) {
                        bundle.assignedToField = fieldName;
                        bundle.assignedToClass = shortName(targetClass);
                        recordMetaDataBundle(ref.getField(), bundle, method);
                    }
                }
            }

            // 方法调用
            if (stmt.containsInvokeExpr()) {
                InvokeExpr invoke = stmt.getInvokeExpr();

                // Bundle.putXxx 调用
                analyzeMetaDataBundlePut(invoke, trackedValues, trackedBundles,
                        body, stringValues, method, currentEntry);
            }
        }

        // 保存当前条目
        if (currentEntry != null && (currentEntry.nameAttrIndex != null ||
                currentEntry.valueAttrIndex != null || currentEntry.resourceAttrIndex != null)) {
            allMetaDataEntries.add(currentEntry);
        }
    }

    /**
     * 分析 TypedArray 方法调用，提取属性索引
     */
    private ValueInfo analyzeTypedArrayCall(InvokeExpr invoke, MetaDataEntry entry) {
        String methodName = invoke.getMethod().getName();

        // 获取属性索引
        String attrIndex = "";
        if (invoke.getArgCount() > 0) {
            Value arg = invoke.getArg(0);
            if (arg instanceof IntConstant) {
                attrIndex = "index_" + ((IntConstant) arg).value;
            } else if (arg instanceof StaticFieldRef) {
                attrIndex = ((StaticFieldRef) arg).getField().getName();
            }
        }

        boolean isRef = false;

        // 根据方法名判断属性类型
        if (methodName.equals("getNonConfigurationString") || methodName.equals("getString")) {
            // 可能是 name 属性
            if (attrIndex.toLowerCase().contains("name") || attrIndex.contains("index_0")) {
                if (entry != null) entry.nameAttrIndex = attrIndex;
            }
        } else if (methodName.equals("peekValue")) {
            // 可能是 value 属性
            if (entry != null) {
                entry.valueAttrIndex = attrIndex;
                entry.hasValue = true;
            }
        } else if (methodName.equals("getResourceId")) {
            // 可能是 resource 属性
            isRef = true;
            if (entry != null) {
                entry.resourceAttrIndex = attrIndex;
                entry.hasResource = true;
                entry.isResourceRef = true;
                entry.extractMethod = "getResourceId";
            }
        }

        Map<String, Boolean> methods = RESOURCE_EXTRACT_METHODS.get("android.content.res.TypedArray");
        if (methods != null && methods.containsKey(methodName)) {
            isRef = methods.get(methodName);
        }

        ValueInfo info = new ValueInfo(methodName, "TypedArray", attrIndex, isRef);
        info.xmlTagName = "meta-data";
        return info;
    }

    /**
     * 分析 meta-data 中的 Bundle.putXxx 调用
     */
    private void analyzeMetaDataBundlePut(InvokeExpr invoke, Map<Value, ValueInfo> tracked,
                                          Map<Value, BundleContent> bundles, Body body,
                                          Map<Value, String> stringValues, SootMethod source,
                                          MetaDataEntry currentEntry) {
        SootMethod called = invoke.getMethod();
        String className = called.getDeclaringClass().getName();
        String methodName = called.getName();

        if (!isBundleClassOrSubclass(className)) return;
        if (!methodName.startsWith("put") || invoke.getArgCount() < 2) return;

        if (!(invoke instanceof InstanceInvokeExpr)) return;
        Value bundleBase = ((InstanceInvokeExpr) invoke).getBase();
        BundleContent bundleContent = bundles.get(bundleBase);
        if (bundleContent == null) {
            bundleContent = new BundleContent();
            bundleContent.isMetaData = true;
            bundles.put(bundleBase, bundleContent);
        }

        Value keyArg = invoke.getArg(0);
        Value valueArg = invoke.getArg(1);

        // 解析 key
        String key = resolveStringArg(keyArg, body, stringValues);

        // 检查 value 来源
        ValueInfo valueInfo = tracked.get(valueArg);
        if (valueInfo == null) {
            valueInfo = checkValueSource(valueArg, body, tracked);
        }

        String putType = methodName.substring(3);  // putInt -> Int
        boolean isRef = false;
        String extractMethod = "put" + putType;
        String extractClass = "Bundle";
        String attrIndex = "";

        if (valueInfo != null) {
            isRef = valueInfo.isReference;
            extractMethod = valueInfo.sourceMethod;
            extractClass = valueInfo.sourceClass;
            attrIndex = valueInfo.attrIndex;
        } else {
            // 检查是否为资源 ID 上下文
            isRef = checkIfResourceIdContext(valueArg, body);
            if (isRef) {
                extractMethod = "getResourceId/resourceId";
            }
        }

        // 记录到 Bundle
        if (key != null || valueInfo != null) {
            String entryKey = key != null ? key : "(dynamic:" + putType + ")";
            ValueInfo entry = new ValueInfo(extractMethod, extractClass, attrIndex, isRef);
            entry.bundleKey = entryKey;
            entry.xmlTagName = "meta-data";
            bundleContent.entries.put(entryKey, entry);

            // 记录到 parsedFieldInfos
            ParsedFieldInfo pfi = new ParsedFieldInfo();
            pfi.parsedClass = "metaData";
            pfi.fieldName = entryKey;
            pfi.extractMethod = extractMethod;
            pfi.extractClass = extractClass;
            pfi.isReference = isRef;
            pfi.styleableAttr = attrIndex.isEmpty() ? "meta-data:" + entryKey : attrIndex;
            pfi.sourceMethod = source.getSignature();
            pfi.isFromBundle = true;
            pfi.bundleKey = entryKey;
            pfi.isMetaData = true;
            pfi.xmlTagName = "meta-data";

            parsedFieldInfos.put("metaData." + entryKey, pfi);

            System.out.println("    [meta-data] " + entryKey + " <- " + extractClass + "." +
                    extractMethod + "[" + attrIndex + "] (ref=" + isRef + ")");
        }

        // 更新 currentEntry
        if (currentEntry != null && valueInfo != null) {
            currentEntry.extractMethod = extractMethod;
            currentEntry.isResourceRef = isRef;
            if (key != null) currentEntry.name = key;
        }
    }

    /**
     * 检查值的来源
     */
    private ValueInfo checkValueSource(Value v, Body body, Map<Value, ValueInfo> tracked) {
        if (!(v instanceof Local)) return null;

        for (Unit u : body.getUnits()) {
            if (u instanceof AssignStmt) {
                AssignStmt a = (AssignStmt) u;
                if (a.getLeftOp().equals(v)) {
                    Value rhs = a.getRightOp();

                    // 来自 TypedArray 方法
                    if (rhs instanceof InvokeExpr) {
                        InvokeExpr inv = (InvokeExpr) rhs;
                        String name = inv.getMethod().getName();
                        String clazz = inv.getMethod().getDeclaringClass().getName();

                        if (clazz.contains("TypedArray")) {
                            String attr = "";
                            if (inv.getArgCount() > 0) {
                                Value arg = inv.getArg(0);
                                if (arg instanceof IntConstant) {
                                    attr = "index_" + ((IntConstant) arg).value;
                                } else if (arg instanceof StaticFieldRef) {
                                    attr = ((StaticFieldRef) arg).getField().getName();
                                }
                            }

                            Map<String, Boolean> methods = RESOURCE_EXTRACT_METHODS.get("android.content.res.TypedArray");
                            boolean isRef = methods != null && methods.getOrDefault(name, false);

                            return new ValueInfo(name, "TypedArray", attr, isRef);
                        }
                    }

                    // 来自 TypedValue.resourceId
                    if (rhs instanceof InstanceFieldRef) {
                        InstanceFieldRef ref = (InstanceFieldRef) rhs;
                        if (ref.getField().getName().equals("resourceId")) {
                            ValueInfo baseInfo = tracked.get(ref.getBase());
                            String attr = baseInfo != null ? baseInfo.attrIndex : "unknown";
                            return new ValueInfo("resourceId(field)", "TypedValue", attr, true);
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 检查值是否在资源 ID 上下文中
     */
    private boolean checkIfResourceIdContext(Value v, Body body) {
        if (!(v instanceof Local)) return false;

        for (Unit u : body.getUnits()) {
            if (u instanceof AssignStmt) {
                AssignStmt a = (AssignStmt) u;
                if (a.getLeftOp().equals(v)) {
                    Value rhs = a.getRightOp();

                    if (rhs instanceof InvokeExpr) {
                        String name = ((InvokeExpr) rhs).getMethod().getName();
                        if (name.contains("Resource") || name.equals("getResourceId") ||
                                name.equals("getAttributeResourceValue")) {
                            return true;
                        }
                    }

                    if (rhs instanceof InstanceFieldRef) {
                        String fieldName = ((InstanceFieldRef) rhs).getField().getName();
                        if (fieldName.equals("resourceId")) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private String resolveStringArg(Value v, Body body, Map<Value, String> stringValues) {
        if (v instanceof StringConstant) {
            return ((StringConstant) v).value;
        }
        if (v instanceof Local) {
            if (stringValues.containsKey(v)) {
                return stringValues.get(v);
            }
            for (Unit u : body.getUnits()) {
                if (u instanceof AssignStmt) {
                    AssignStmt a = (AssignStmt) u;
                    if (a.getLeftOp().equals(v) && a.getRightOp() instanceof StringConstant) {
                        return ((StringConstant) a.getRightOp()).value;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 记录 meta-data Bundle 字段
     */
    private void recordMetaDataBundle(SootField field, BundleContent bundle, SootMethod source) {
        String className = field.getDeclaringClass().getName();
        String fieldName = field.getName();

        // 记录 Bundle 字段本身
        ParsedFieldInfo bundleField = new ParsedFieldInfo();
        bundleField.parsedClass = shortName(className);
        bundleField.fieldName = fieldName;
        bundleField.extractMethod = "Bundle";
        bundleField.extractClass = "Bundle";
        bundleField.isReference = true;
        bundleField.styleableAttr = "meta-data";
        bundleField.sourceMethod = source.getSignature();
        bundleField.isFromBundle = true;
        bundleField.isMetaData = true;
        bundleField.xmlTagName = "meta-data";

        parsedFieldInfos.put(bundleField.getKey(), bundleField);

        // 记录 Bundle 内的每个条目
        for (Map.Entry<String, ValueInfo> entry : bundle.entries.entrySet()) {
            String key = entry.getKey();
            ValueInfo info = entry.getValue();

            ParsedFieldInfo pfi = new ParsedFieldInfo();
            pfi.parsedClass = shortName(className);
            pfi.fieldName = fieldName + "[" + key + "]";
            pfi.extractMethod = info.sourceMethod;
            pfi.extractClass = info.sourceClass;
            pfi.isReference = info.isReference;
            pfi.styleableAttr = info.attrIndex.isEmpty() ? "meta-data:" + key : info.attrIndex;
            pfi.sourceMethod = source.getSignature();
            pfi.isFromBundle = true;
            pfi.bundleKey = key;
            pfi.isMetaData = true;
            pfi.xmlTagName = "meta-data";

            parsedFieldInfos.put(pfi.getKey(), pfi);
        }

        // 保存 Bundle 内容
        String bundleId = shortName(className) + "." + fieldName;
        bundleContents.put(bundleId, bundle);

        System.out.println("    [meta-data] Bundle -> " + bundleId +
                " with " + bundle.entries.size() + " entries");
    }

    /**
     * 检查方法是否使用了资源提取相关的类
     */
    private boolean hasResourceUsage(Body body) {
        for (Unit unit : body.getUnits()) {
            Stmt stmt = (Stmt) unit;
            if (!stmt.containsInvokeExpr()) continue;

            InvokeExpr invoke = stmt.getInvokeExpr();
            String className = invoke.getMethod().getDeclaringClass().getName();
            String methodName = invoke.getMethod().getName();

            Map<String, Boolean> methods = getMethodsForClass(className);
            if (methods != null && methods.containsKey(methodName)) {
                return true;
            }
        }
        return false;
    }

    private Map<String, Boolean> getMethodsForClass(String className) {
        if (RESOURCE_EXTRACT_METHODS.containsKey(className)) {
            return RESOURCE_EXTRACT_METHODS.get(className);
        }

        Set<String> supers = inheritanceCache.get(className);
        if (supers != null) {
            for (String s : supers) {
                if (RESOURCE_EXTRACT_METHODS.containsKey(s)) {
                    return RESOURCE_EXTRACT_METHODS.get(s);
                }
            }
        }
        return null;
    }

    /**
     * 分析普通解析方法
     */
    private void analyzeParseMethod(SootMethod method, Body body) {
        Map<Local, String> paramMap = buildParamValueMap(method, body);
        Map<Value, ValueInfo> trackedValues = new HashMap<>();
        Map<Value, BundleContent> trackedBundles = new HashMap<>();

        for (Unit unit : body.getUnits()) {
            Stmt stmt = (Stmt) unit;

            if (stmt instanceof AssignStmt) {
                AssignStmt assign = (AssignStmt) stmt;
                Value lhs = assign.getLeftOp();
                Value rhs = assign.getRightOp();

                // 资源提取方法调用
                if (rhs instanceof InvokeExpr) {
                    ValueInfo info = checkResourceExtraction((InvokeExpr) rhs, paramMap);
                    if (info != null) {
                        trackedValues.put(lhs, info);
                    }
                }

                // 容器创建
                if (rhs instanceof NewExpr) {
                    String type = ((NewExpr) rhs).getType().toString();
                    if (isBundleClass(type)) {
                        trackedBundles.put(lhs, new BundleContent());
                    }
                }

                // 字段访问
                if (rhs instanceof InstanceFieldRef) {
                    ValueInfo info = checkResourceFieldAccess((InstanceFieldRef) rhs, trackedValues);
                    if (info != null) {
                        trackedValues.put(lhs, info);
                    }
                }

                // 变量传递
                if (rhs instanceof Local && trackedValues.containsKey(rhs)) {
                    trackedValues.put(lhs, trackedValues.get(rhs).copy());
                }
                if (rhs instanceof Local && trackedBundles.containsKey(rhs)) {
                    trackedBundles.put(lhs, trackedBundles.get(rhs));
                }

                // 字段赋值
                if (lhs instanceof InstanceFieldRef) {
                    InstanceFieldRef ref = (InstanceFieldRef) lhs;
                    String targetClass = ref.getField().getDeclaringClass().getName();

                    ValueInfo info = trackedValues.get(rhs);
                    if (info == null && rhs instanceof InvokeExpr) {
                        info = checkResourceExtraction((InvokeExpr) rhs, paramMap);
                    }
                    if (info != null && isParsedClass(targetClass)) {
                        recordParsedField(ref.getField(), info, method);
                    }

                    BundleContent bundle = trackedBundles.get(rhs);
                    if (bundle != null && isParsedClass(targetClass)) {
                        bundle.assignedToField = ref.getField().getName();
                        bundle.assignedToClass = shortName(targetClass);
                        recordBundleField(ref.getField(), bundle, method);
                    }
                }
            }

            // setter 调用
            if (stmt.containsInvokeExpr()) {
                InvokeExpr invoke = stmt.getInvokeExpr();
                analyzeSetterCall(invoke, trackedValues, method, paramMap);
                analyzeBundlePut(invoke, trackedValues, trackedBundles, body);
            }
        }
    }

    private ValueInfo checkResourceExtraction(InvokeExpr invoke, Map<Local, String> paramMap) {
        SootMethod called = invoke.getMethod();
        String className = called.getDeclaringClass().getName();
        String methodName = called.getName();

        Map<String, Boolean> methods = getMethodsForClass(className);
        if (methods != null && methods.containsKey(methodName)) {
            String attr = extractAttrIndex(invoke, paramMap);
            String actualClass = findActualResourceClass(className);
            return new ValueInfo(methodName, actualClass, attr, methods.get(methodName));
        }

        return null;
    }

    private String findActualResourceClass(String className) {
        if (RESOURCE_EXTRACT_METHODS.containsKey(className)) {
            return shortName(className);
        }

        Set<String> supers = inheritanceCache.get(className);
        if (supers != null) {
            for (String s : supers) {
                if (RESOURCE_EXTRACT_METHODS.containsKey(s)) {
                    return shortName(s);
                }
            }
        }
        return shortName(className);
    }

    private ValueInfo checkResourceFieldAccess(InstanceFieldRef ref, Map<Value, ValueInfo> tracked) {
        SootField field = ref.getField();
        String className = field.getDeclaringClass().getName();
        String fieldName = field.getName();

        Map<String, Boolean> fields = RESOURCE_FIELDS.get(className);
        if (fields == null) {
            Set<String> supers = inheritanceCache.get(className);
            if (supers != null) {
                for (String s : supers) {
                    if (RESOURCE_FIELDS.containsKey(s)) {
                        fields = RESOURCE_FIELDS.get(s);
                        break;
                    }
                }
            }
        }

        if (fields != null && fields.containsKey(fieldName)) {
            Value base = ref.getBase();
            ValueInfo baseInfo = tracked.get(base);
            String attr = (baseInfo != null) ? baseInfo.attrIndex : "unknown";
            return new ValueInfo(fieldName + "(field)", shortName(className), attr, fields.get(fieldName));
        }

        return null;
    }

    private void analyzeBundlePut(InvokeExpr invoke, Map<Value, ValueInfo> tracked,
                                  Map<Value, BundleContent> bundles, Body body) {
        SootMethod called = invoke.getMethod();
        String className = called.getDeclaringClass().getName();
        String methodName = called.getName();

        if (!isBundleClassOrSubclass(className)) return;
        if (!methodName.startsWith("put") || invoke.getArgCount() < 2) return;

        if (!(invoke instanceof InstanceInvokeExpr)) return;
        Value bundleBase = ((InstanceInvokeExpr) invoke).getBase();
        BundleContent bundleContent = bundles.get(bundleBase);
        if (bundleContent == null) {
            bundleContent = new BundleContent();
            bundles.put(bundleBase, bundleContent);
        }

        Value keyArg = invoke.getArg(0);
        Value valueArg = invoke.getArg(1);

        String key = resolveStringConstant(keyArg, body);

        ValueInfo valueInfo = tracked.get(valueArg);
        if (valueInfo == null && valueArg instanceof InvokeExpr) {
            valueInfo = checkResourceExtraction((InvokeExpr) valueArg, new HashMap<>());
        }

        if (key != null) {
            if (valueInfo != null) {
                valueInfo = valueInfo.copy();
                valueInfo.bundleKey = key;
                bundleContent.entries.put(key, valueInfo);
            } else {
                ValueInfo unknown = new ValueInfo("put" + methodName.substring(3), "Bundle", key, false);
                unknown.bundleKey = key;
                bundleContent.entries.put(key, unknown);
            }
        }
    }

    private boolean isBundleClassOrSubclass(String className) {
        if (BUNDLE_CLASSES.contains(className)) return true;

        Set<String> supers = inheritanceCache.get(className);
        if (supers != null) {
            for (String s : supers) {
                if (BUNDLE_CLASSES.contains(s)) return true;
            }
        }
        return false;
    }

    private void analyzeSetterCall(InvokeExpr invoke, Map<Value, ValueInfo> tracked,
                                   SootMethod source, Map<Local, String> paramMap) {
        String methodName = invoke.getMethod().getName();
        if (!methodName.startsWith("set") || methodName.length() <= 3) return;
        if (invoke.getArgCount() == 0) return;

        Value arg = invoke.getArg(0);
        ValueInfo info = tracked.get(arg);
        if (info == null && arg instanceof InvokeExpr) {
            info = checkResourceExtraction((InvokeExpr) arg, paramMap);
        }
        if (info == null) return;

        String receiver = getReceiverClass(invoke);
        if (receiver == null || !isParsedClass(receiver)) return;

        String fieldName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);

        ParsedFieldInfo pfi = new ParsedFieldInfo();
        pfi.parsedClass = shortName(receiver);
        pfi.fieldName = fieldName;
        pfi.setterMethod = methodName;
        pfi.extractMethod = info.sourceMethod;
        pfi.extractClass = info.sourceClass;
        pfi.isReference = info.isReference;
        pfi.styleableAttr = info.attrIndex;
        pfi.sourceMethod = source.getSignature();
        pfi.isFromBundle = info.bundleKey != null;
        pfi.bundleKey = info.bundleKey;

        parsedFieldInfos.put(pfi.getKey(), pfi);
    }

    private void recordParsedField(SootField field, ValueInfo info, SootMethod source) {
        String className = field.getDeclaringClass().getName();
        if (!isParsedClass(className)) return;

        ParsedFieldInfo pfi = new ParsedFieldInfo();
        pfi.parsedClass = shortName(className);
        pfi.fieldName = field.getName();
        pfi.extractMethod = info.sourceMethod;
        pfi.extractClass = info.sourceClass;
        pfi.isReference = info.isReference;
        pfi.styleableAttr = info.attrIndex;
        pfi.sourceMethod = source.getSignature();
        pfi.isFromBundle = info.bundleKey != null;
        pfi.bundleKey = info.bundleKey;

        parsedFieldInfos.put(pfi.getKey(), pfi);
    }

    private void recordBundleField(SootField field, BundleContent bundle, SootMethod source) {
        String className = field.getDeclaringClass().getName();
        String fieldName = field.getName();

        boolean isMetaData = fieldName.toLowerCase().contains("metadata") ||
                fieldName.toLowerCase().contains("meta_data");

        ParsedFieldInfo bundleField = new ParsedFieldInfo();
        bundleField.parsedClass = shortName(className);
        bundleField.fieldName = fieldName;
        bundleField.extractMethod = "Bundle";
        bundleField.extractClass = "Bundle";
        bundleField.isReference = isMetaData;
        bundleField.styleableAttr = isMetaData ? "meta-data" : "bundle";
        bundleField.sourceMethod = source.getSignature();
        bundleField.isFromBundle = true;
        bundleField.isMetaData = isMetaData;

        parsedFieldInfos.put(bundleField.getKey(), bundleField);

        for (Map.Entry<String, ValueInfo> entry : bundle.entries.entrySet()) {
            String key = entry.getKey();
            ValueInfo info = entry.getValue();

            ParsedFieldInfo pfi = new ParsedFieldInfo();
            pfi.parsedClass = shortName(className);
            pfi.fieldName = fieldName + "[" + key + "]";
            pfi.extractMethod = info.sourceMethod;
            pfi.extractClass = info.sourceClass;
            pfi.isReference = info.isReference;
            pfi.styleableAttr = info.attrIndex.isEmpty() ?
                    (isMetaData ? "meta-data:" + key : "bundle:" + key) : info.attrIndex;
            pfi.sourceMethod = source.getSignature();
            pfi.isFromBundle = true;
            pfi.bundleKey = key;
            pfi.isMetaData = isMetaData;

            parsedFieldInfos.put(pfi.getKey(), pfi);
        }

        String bundleId = shortName(className) + "." + fieldName;
        bundleContents.put(bundleId, bundle);
    }

    private boolean isParsedClass(String name) {
        return name.contains("Parsed") ||
                name.contains("ParsingPackage") ||
                name.contains("PackageParser$");
    }

    private boolean isBundleClass(String type) {
        for (String bundle : BUNDLE_CLASSES) {
            if (type.equals(bundle) || type.endsWith("." + bundle.substring(bundle.lastIndexOf('.') + 1))) {
                return true;
            }
        }
        return false;
    }

    // ==================== Phase 2: 分析 PackageInfoUtils ====================

    public void analyzeInfoGeneration() {
        System.out.println("\n[Phase 2] Analyzing PackageInfoUtils...");

        for (SootClass sc : Scene.v().getApplicationClasses()) {
            if (!isInfoUtilsClass(sc.getName())) continue;

            System.out.println("[*] Found: " + sc.getName());

            for (SootMethod method : sc.getMethods()) {
                if (!method.isConcrete()) continue;

                String name = method.getName();
                if (name.startsWith("generate") || name.startsWith("assignFields")) {
                    try {
                        Body body = method.retrieveActiveBody();
                        analyzeGenerateMethod(method, body);
                    } catch (Exception e) {}
                }
            }
        }

        System.out.println("[*] Found " + fieldMappings.size() + " field mappings");
    }

    private void analyzeGenerateMethod(SootMethod method, Body body) {
        Map<Value, GetterInfo> getterValues = new HashMap<>();

        for (Unit unit : body.getUnits()) {
            Stmt stmt = (Stmt) unit;

            if (stmt instanceof AssignStmt) {
                AssignStmt assign = (AssignStmt) stmt;
                Value lhs = assign.getLeftOp();
                Value rhs = assign.getRightOp();

                if (rhs instanceof InvokeExpr) {
                    GetterInfo gi = checkParsedGetter((InvokeExpr) rhs);
                    if (gi != null) getterValues.put(lhs, gi);
                }

                if (rhs instanceof Local && getterValues.containsKey(rhs)) {
                    getterValues.put(lhs, getterValues.get(rhs));
                }

                if (lhs instanceof InstanceFieldRef) {
                    InstanceFieldRef ref = (InstanceFieldRef) lhs;
                    String targetClass = ref.getField().getDeclaringClass().getName();

                    if (isInfoClass(targetClass)) {
                        GetterInfo gi = getterValues.get(rhs);
                        if (gi == null && rhs instanceof InvokeExpr) {
                            gi = checkParsedGetter((InvokeExpr) rhs);
                        }
                        if (gi != null) {
                            createMapping(ref.getField(), gi, method);
                        }
                    }
                }
            }

            if (stmt.containsInvokeExpr()) {
                analyzeInfoSetter(stmt.getInvokeExpr(), getterValues, method);
            }
        }
    }

    private boolean isInfoClass(String name) {
        return (name.endsWith("Info") || name.endsWith("Info$")) &&
                (name.contains("Activity") || name.contains("Service") ||
                        name.contains("Provider") || name.contains("Application") ||
                        name.contains("Permission") || name.contains("Instrumentation") ||
                        name.contains("Package") || name.contains("Component") ||
                        name.contains("Feature") || name.contains("Configuration") ||
                        name.contains("Receiver") || name.contains("Broadcast"));
    }

    private GetterInfo checkParsedGetter(InvokeExpr invoke) {
        SootMethod called = invoke.getMethod();
        String className = called.getDeclaringClass().getName();
        String methodName = called.getName();

        if (!isParsedClass(className)) return null;
        if (!methodName.startsWith("get") && !methodName.startsWith("is")) return null;

        String fieldName;
        if (methodName.startsWith("get") && methodName.length() > 3) {
            fieldName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
        } else if (methodName.startsWith("is") && methodName.length() > 2) {
            fieldName = Character.toLowerCase(methodName.charAt(2)) + methodName.substring(3);
        } else {
            return null;
        }

        GetterInfo gi = new GetterInfo();
        gi.parsedClass = shortName(className);
        gi.fieldName = fieldName;
        gi.getterMethod = methodName + "()";
        return gi;
    }

    private void analyzeInfoSetter(InvokeExpr invoke, Map<Value, GetterInfo> getterValues,
                                   SootMethod source) {
        String methodName = invoke.getMethod().getName();
        String className = invoke.getMethod().getDeclaringClass().getName();

        if (!isInfoClass(className)) return;
        if (!methodName.startsWith("set") || invoke.getArgCount() == 0) return;

        Value arg = invoke.getArg(0);
        GetterInfo gi = getterValues.get(arg);
        if (gi == null && arg instanceof InvokeExpr) {
            gi = checkParsedGetter((InvokeExpr) arg);
        }
        if (gi == null) return;

        String fieldName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
        String receiver = getReceiverClass(invoke);
        if (receiver == null) receiver = className;

        createMappingFromSetter(receiver, fieldName, gi, source);
    }

    private void createMapping(SootField field, GetterInfo gi, SootMethod source) {
        FieldMapping m = new FieldMapping();
        m.infoClass = shortName(field.getDeclaringClass().getName());
        m.infoField = field.getName();
        m.parsedClass = gi.parsedClass;
        m.parsedField = gi.fieldName;
        m.parsedGetter = gi.getterMethod;
        m.generateMethod = source.getSignature();

        linkParseInfo(m);

        fieldMappings.add(m);
    }

    private void createMappingFromSetter(String infoClass, String fieldName,
                                         GetterInfo gi, SootMethod source) {
        FieldMapping m = new FieldMapping();
        m.infoClass = shortName(infoClass);
        m.infoField = fieldName;
        m.parsedClass = gi.parsedClass;
        m.parsedField = gi.fieldName;
        m.parsedGetter = gi.getterMethod;
        m.generateMethod = source.getSignature();

        linkParseInfo(m);

        fieldMappings.add(m);
    }

    private void linkParseInfo(FieldMapping m) {
        // 精确匹配
        String key = m.parsedClass + "." + m.parsedField;
        ParsedFieldInfo pfi = parsedFieldInfos.get(key);

        // 模糊匹配
        if (pfi == null) {
            for (ParsedFieldInfo p : parsedFieldInfos.values()) {
                if (p.fieldName.equals(m.parsedField)) {
                    pfi = p;
                    break;
                }
            }
        }

        // 特殊处理 metaData
        if (pfi == null && (m.parsedField.equals("metaData") || m.parsedField.equals("meta_data"))) {
            m.extractMethod = "Bundle";
            m.extractClass = "Bundle";
            m.isReference = true;
            m.isFromBundle = true;
            m.isMetaData = true;
            m.styleableAttr = "meta-data";
            m.parseSourceMethod = "";
            return;
        }

        if (pfi != null) {
            m.extractMethod = pfi.extractMethod;
            m.extractClass = pfi.extractClass;
            m.isReference = pfi.isReference;
            m.styleableAttr = pfi.styleableAttr;
            m.parseSourceMethod = pfi.sourceMethod;
            m.isFromBundle = pfi.isFromBundle;
            m.bundleKey = pfi.bundleKey;
            m.isMetaData = pfi.isMetaData;
        } else {
            m.extractMethod = "?";
            m.extractClass = "?";
            m.isReference = false;
            m.styleableAttr = "";
            m.parseSourceMethod = "";
        }
    }

    // ==================== 辅助方法 ====================

    private Map<Local, String> buildParamValueMap(SootMethod method, Body body) {
        Map<Local, String> map = new HashMap<>();
        List<CallInfo> callers = methodCallGraph.get(method.getSignature());
        if (callers == null) return map;

        List<Local> params = body.getParameterLocals();
        for (CallInfo c : callers) {
            for (Map.Entry<Integer, String> e : c.paramValues.entrySet()) {
                if (e.getKey() < params.size()) {
                    Local p = params.get(e.getKey());
                    String existing = map.get(p);
                    if (existing == null) {
                        map.put(p, e.getValue());
                    } else if (!existing.contains(e.getValue())) {
                        map.put(p, existing + "|" + e.getValue());
                    }
                }
            }
        }
        return map;
    }

    private String extractAttrIndex(InvokeExpr invoke, Map<Local, String> paramMap) {
        if (invoke.getArgCount() == 0) return "";

        Value arg = invoke.getArg(0);
        if (arg instanceof StaticFieldRef) {
            return ((StaticFieldRef) arg).getField().getName();
        } else if (arg instanceof IntConstant) {
            return "index_" + ((IntConstant) arg).value;
        } else if (arg instanceof StringConstant) {
            return ((StringConstant) arg).value;
        } else if (arg instanceof Local) {
            String mapped = paramMap.get(arg);
            if (mapped != null) return mapped;
        }
        return "dynamic";
    }

    private String resolveStringConstant(Value v, Body body) {
        if (v instanceof StringConstant) {
            return ((StringConstant) v).value;
        }
        if (v instanceof Local) {
            for (Unit u : body.getUnits()) {
                if (u instanceof AssignStmt) {
                    AssignStmt a = (AssignStmt) u;
                    if (a.getLeftOp().equals(v) && a.getRightOp() instanceof StringConstant) {
                        return ((StringConstant) a.getRightOp()).value;
                    }
                }
            }
        }
        return null;
    }

    private String getReceiverClass(InvokeExpr invoke) {
        if (invoke instanceof InstanceInvokeExpr) {
            return ((InstanceInvokeExpr) invoke).getBase().getType().toString();
        }
        return null;
    }

    private String shortName(String full) {
        if (full == null) return "?";
        int dot = full.lastIndexOf('.');
        return dot >= 0 ? full.substring(dot + 1) : full;
    }

    // ==================== 输出 ====================

    public void writeCsv(String path) {
        System.out.println("\n[*] Writing to: " + path);

        try (PrintWriter w = new PrintWriter(new BufferedWriter(new FileWriter(path)))) {
            w.println("InfoClass,InfoField,ParsedClass,ParsedField,ParsedGetter,ExtractClass,ExtractMethod,IsReference,StyleableAttr,IsFromBundle,IsMetaData,BundleKey,ParseSourceMethod,GenerateMethod");

            Set<String> seen = new HashSet<>();
            int count = 0;

            for (FieldMapping m : fieldMappings) {
                String key = m.infoClass + "." + m.infoField + "." + m.parsedGetter;
                if (seen.contains(key)) continue;
                seen.add(key);

                w.println(m.toCsv());
                count++;
            }

            System.out.println("[*] Written " + count + " mappings");
        } catch (IOException e) {
            System.err.println("[!] Error: " + e.getMessage());
        }
    }

    public void writeParsedFieldsCsv(String path) {
        System.out.println("[*] Writing parsed fields to: " + path);

        try (PrintWriter w = new PrintWriter(new BufferedWriter(new FileWriter(path)))) {
            w.println("ParsedClass,FieldName,ExtractClass,ExtractMethod,IsReference,StyleableAttr,IsFromBundle,BundleKey,IsMetaData,SourceMethod");

            for (ParsedFieldInfo pfi : parsedFieldInfos.values()) {
                w.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s,\"%s\"\n",
                        pfi.parsedClass,
                        pfi.fieldName,
                        pfi.extractClass != null ? pfi.extractClass : "",
                        pfi.extractMethod != null ? pfi.extractMethod : "",
                        pfi.isReference,
                        pfi.styleableAttr != null ? pfi.styleableAttr : "",
                        pfi.isFromBundle,
                        pfi.bundleKey != null ? pfi.bundleKey : "",
                        pfi.isMetaData,
                        pfi.sourceMethod != null ? pfi.sourceMethod : "");
            }

            System.out.println("[*] Written " + parsedFieldInfos.size() + " parsed fields");
        } catch (IOException e) {
            System.err.println("[!] Error: " + e.getMessage());
        }
    }

    public void writeMetaDataCsv(String path) {
        System.out.println("[*] Writing meta-data entries to: " + path);

        try (PrintWriter w = new PrintWriter(new BufferedWriter(new FileWriter(path)))) {
            w.println("Name,NameAttrIndex,ValueAttrIndex,ResourceAttrIndex,HasValue,HasResource,ExtractMethod,IsResourceRef,SourceMethod");

            for (MetaDataEntry entry : allMetaDataEntries) {
                w.printf("%s,%s,%s,%s,%s,%s,%s,%s,\"%s\"\n",
                        entry.name != null ? entry.name : "",
                        entry.nameAttrIndex != null ? entry.nameAttrIndex : "",
                        entry.valueAttrIndex != null ? entry.valueAttrIndex : "",
                        entry.resourceAttrIndex != null ? entry.resourceAttrIndex : "",
                        entry.hasValue,
                        entry.hasResource,
                        entry.extractMethod != null ? entry.extractMethod : "",
                        entry.isResourceRef,
                        entry.sourceMethod != null ? entry.sourceMethod : "");
            }

            System.out.println("[*] Written " + allMetaDataEntries.size() + " meta-data entries");
        } catch (IOException e) {
            System.err.println("[!] Error: " + e.getMessage());
        }
    }

    public void printStats() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("Statistics");
        System.out.println("=".repeat(70));

        // Phase 1 统计
        System.out.println("\n--- Phase 1: ParsedXxx Fields ---");
        Map<String, Integer> parsedByClass = new TreeMap<>();
        Map<String, Integer> refByParsedClass = new TreeMap<>();
        int metaDataFields = 0;

        for (ParsedFieldInfo p : parsedFieldInfos.values()) {
            parsedByClass.merge(p.parsedClass, 1, Integer::sum);
            if (p.isReference) {
                refByParsedClass.merge(p.parsedClass, 1, Integer::sum);
            }
            if (p.isMetaData) metaDataFields++;
        }
        for (Map.Entry<String, Integer> e : parsedByClass.entrySet()) {
            int refs = refByParsedClass.getOrDefault(e.getKey(), 0);
            System.out.printf("  %-35s: %3d fields (%d refs)\n", e.getKey(), e.getValue(), refs);
        }

        // Phase 2 统计
        System.out.println("\n--- Phase 2: XxxInfo Mappings ---");
        Map<String, Integer> infoByClass = new TreeMap<>();
        Map<String, Integer> refByInfoClass = new TreeMap<>();
        Set<String> seen = new HashSet<>();

        for (FieldMapping m : fieldMappings) {
            String key = m.infoClass + "." + m.infoField;
            if (seen.contains(key)) continue;
            seen.add(key);

            infoByClass.merge(m.infoClass, 1, Integer::sum);
            if (m.isReference) {
                refByInfoClass.merge(m.infoClass, 1, Integer::sum);
            }
        }

        for (Map.Entry<String, Integer> e : infoByClass.entrySet()) {
            int refs = refByInfoClass.getOrDefault(e.getKey(), 0);
            System.out.printf("  %-35s: %3d fields (%d refs)\n", e.getKey(), e.getValue(), refs);
        }

        long unlinked = fieldMappings.stream()
                .filter(m -> "?".equals(m.extractMethod))
                .map(m -> m.infoClass + "." + m.infoField)
                .distinct()
                .count();

        long totalRefs = fieldMappings.stream()
                .filter(m -> m.isReference)
                .map(m -> m.infoClass + "." + m.infoField)
                .distinct()
                .count();

        System.out.println("\n--- Summary ---");
        System.out.println("  ParsedXxx fields: " + parsedFieldInfos.size());
        System.out.println("  Meta-data fields: " + metaDataFields);
        System.out.println("  Meta-data entries: " + allMetaDataEntries.size());
        System.out.println("  XxxInfo mappings: " + seen.size());
        System.out.println("  Resource refs: " + totalRefs);
        System.out.println("  Unlinked: " + unlinked);
    }

    public void printMetaDataInfo() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("Meta-Data Analysis");
        System.out.println("=".repeat(70));

        System.out.println("\n--- Meta-Data Entries ---");
        for (MetaDataEntry entry : allMetaDataEntries) {
            System.out.printf("  name=%s\n", entry.name != null ? entry.name : "(dynamic)");
            System.out.printf("    nameAttr: %s\n", entry.nameAttrIndex);
            System.out.printf("    valueAttr: %s (hasValue=%s)\n", entry.valueAttrIndex, entry.hasValue);
            System.out.printf("    resourceAttr: %s (hasResource=%s, isRef=%s)\n",
                    entry.resourceAttrIndex, entry.hasResource, entry.isResourceRef);
            System.out.printf("    extractMethod: %s\n", entry.extractMethod);
        }

        System.out.println("\n--- Meta-Data Bundle Contents ---");
        for (Map.Entry<String, BundleContent> e : bundleContents.entrySet()) {
            if (!e.getValue().isMetaData) continue;

            System.out.println("  " + e.getKey() + ":");
            for (Map.Entry<String, ValueInfo> entry : e.getValue().entries.entrySet()) {
                ValueInfo info = entry.getValue();
                System.out.printf("    [%s] <- %s.%s[%s] (ref=%s)\n",
                        entry.getKey(),
                        info.sourceClass,
                        info.sourceMethod,
                        info.attrIndex,
                        info.isReference);
            }
        }
    }

    // ==================== Main ====================

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java PMReferenceAnalyzer <config.json> [output.csv]");
            return;
        }

        String output = args.length >= 2 ? args[1] : "pm_field_mappings.csv";

        Config config = Config.loadFromFile(args[0]);
        PMReferenceAnalyzer analyzer = new PMReferenceAnalyzer(config);

        analyzer.initialize();
        analyzer.buildCallGraph();

        // Phase 1: 分析解析代码
        analyzer.analyzeParsingCode();

        // Phase 2: 分析 PackageInfoUtils
        analyzer.analyzeInfoGeneration();

        // 输出统计
        analyzer.printStats();
        analyzer.printMetaDataInfo();

        // 写入 CSV
        analyzer.writeCsv(output);
        analyzer.writeParsedFieldsCsv(output.replace(".csv", "_parsed.csv"));
        analyzer.writeMetaDataCsv(output.replace(".csv", "_metadata.csv"));

        System.out.println("\n[*] Done!");
    }
}