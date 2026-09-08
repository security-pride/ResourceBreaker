package com.userxxx;

import com.userxxx.core.MethodPathFinder;
import com.userxxx.core.SimpleCFGBuilder;
import soot.*;
import soot.options.Options;


import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {



    private static void initializeSoot(Config config) {
        G.reset();

        // 设置基本分析选项
        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_android_jars(config.getAndroidJarPath());
        Options.v().set_process_dir(Arrays.asList(config.getTarget()));
        Options.v().set_force_android_jar(config.getAndroidJarPath());
        Options.v().set_process_multiple_dex(true);
        Options.v().set_output_format(Options.output_format_none);

        Options.v().set_allow_phantom_refs(true);
        Options.v().set_whole_program(true);


        Options.v().set_keep_line_number(false);
        Options.v().set_wrong_staticness(Options.wrong_staticness_ignore);
        Options.v().set_debug(false);
        Options.v().set_verbose(false);
        Options.v().set_validate(false);

        Scene.v().loadNecessaryClasses();

    }

    private static boolean isAndroidComponent(SootClass sootClass) {
        return Scene.v().getActiveHierarchy().isClassSubclassOfIncluding(sootClass, Scene.v().getSootClass("android.app.Activity"))
                || Scene.v().getActiveHierarchy().isClassSubclassOfIncluding(sootClass, Scene.v().getSootClass("android.app.Service"))
                || Scene.v().getActiveHierarchy().isClassSubclassOfIncluding(sootClass, Scene.v().getSootClass("android.content.BroadcastReceiver"))
                || Scene.v().getActiveHierarchy().isClassSubclassOfIncluding(sootClass, Scene.v().getSootClass("android.content.ContentProvider"));
    }


    public static List<SootMethod> getAllRuntimeMethods(SootClass sootClass, int depth) {
        List<SootMethod> allMethods = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        SootClass current = sootClass;
        int currentDepth = 0;

        while (current != null && currentDepth <= depth) {
            for (SootMethod method : current.getMethods()) {
                if (seen.add(method.getSubSignature())) {
                    allMethods.add(method);
                }
            }

            if (current.hasSuperclass()) {
                current = current.getSuperclass();
                currentDepth++;
            } else {
                break;
            }
        }

        return allMethods;
    }

    public static Map<SootMethod, List<List<SootMethod>>> getCallPathsToTarget(
            String className,
            SootMethod targetMethod,
            Map<SootMethod, Set<SootMethod>> reverseCfgEdges,
            int maxDepth) {

        Map<SootMethod, List<List<SootMethod>>> methodToPaths = new HashMap<>();

        try {
            SootClass sootClass = Scene.v().getSootClass(className);

            for (SootMethod method : getAllRuntimeMethods(sootClass,1 )) {
                // 不需要lambda方法
                if (method.getSignature().contains("lambda$")){
                    continue;
                }
                System.out.println("Analyzing method: " + method.getSignature());

                List<List<SootMethod>> paths = findReverseCallPaths(method, targetMethod, reverseCfgEdges, maxDepth);
                methodToPaths.put(method, paths);

                if (!paths.isEmpty()) {
                    System.out.println("✅ " + method.getSignature()
                            + " has " + paths.size() + " call path(s) to target:");
                    for (int i = 0; i < Math.min(paths.size(), 3); i++) { // 只显示前3条路径
                        System.out.println("   Path " + (i + 1) + ": " + formatCallPath(paths.get(i)));
                    }
                } else {
                    // 可选：没有路径时也给出说明
                    System.out.println("   No call path to target.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error analyzing class " + className + ": " + e.getMessage());
        }

        return methodToPaths;
    }

    private static List<List<SootMethod>> findReverseCallPaths(SootMethod sourceMethod,
                                                               SootMethod targetMethod,
                                                               Map<SootMethod, Set<SootMethod>> reverseCfgEdges,
                                                               int maxDepth) {
        List<List<SootMethod>> allPaths = new ArrayList<>();

        // 从目标方法开始反向BFS，寻找到源方法的路径
        Queue<List<SootMethod>> pathQueue = new LinkedList<>();

        List<SootMethod> initialPath = new ArrayList<>();
        initialPath.add(targetMethod);
        pathQueue.add(initialPath);

        while (!pathQueue.isEmpty()) {
            List<SootMethod> currentPath = pathQueue.poll();
            SootMethod currentMethod = currentPath.get(currentPath.size() - 1);

            if (currentPath.size() > maxDepth) {
                continue;
            }

            if (currentMethod.equals(sourceMethod)) {
                // 找到路径，需要反转因为我们是反向搜索的
                List<SootMethod> reversedPath = new ArrayList<>(currentPath);
                Collections.reverse(reversedPath);
                allPaths.add(reversedPath);
                continue;
            }

            Set<SootMethod> callers = reverseCfgEdges.getOrDefault(currentMethod, Collections.emptySet());
            for (SootMethod caller : callers) {
                if (!currentPath.contains(caller)) { // 避免循环
                    List<SootMethod> newPath = new ArrayList<>(currentPath);
                    newPath.add(caller);
                    pathQueue.add(newPath);
                }
            }
        }

        return allPaths;
    }

    private static String formatCallPath(List<SootMethod> path) {
        return path.stream()
                .map(method -> method.getDeclaringClass().getShortName() + "." + method.getName())
                .collect(Collectors.joining(" -> "));
    }


    public static Map<Integer, Set<SootMethod>> reverseCallHierarchy(SootMethod tgtMethod,
                                                                     Map<SootMethod, Set<SootMethod>> reverseCfgEdges,
                                                                     int maxDepth) {


        Map<Integer, Set<SootMethod>> levelToCallers = new HashMap<>();
        Set<SootMethod> visited = new HashSet<>();
        Queue<SootMethod> queue = new LinkedList<>();

        int currentDepth = 0;

        queue.add(tgtMethod);
        visited.add(tgtMethod);

        while (!queue.isEmpty() && currentDepth < maxDepth) {
            int levelSize = queue.size();
            Set<SootMethod> currentLevelMethods = new HashSet<>();

            for (int i = 0; i < levelSize; i++) {
                SootMethod currentMethod = queue.poll();

                // 使用你构建的 reverse edges 查找调用当前方法的方法（即reverse调用关系）
                Set<SootMethod> callers = reverseCfgEdges.getOrDefault(currentMethod, Collections.emptySet());

                for (SootMethod callerMethod : callers) {
                    if (visited.add(callerMethod)) {
                        currentLevelMethods.add(callerMethod);
                        queue.add(callerMethod);
                    }
                }
            }

            if (!currentLevelMethods.isEmpty()) {
                levelToCallers.put(++currentDepth, currentLevelMethods);
            } else {
                break;  // 若这一层找不到更多调用者，则遍历停止
            }
        }

        return levelToCallers;
    }


    // Only count methods declared in PMS classes, not inherited from Binder/Stub
    private static boolean isPmsMethod(SootMethod method) {
        String declClass = method.getDeclaringClass().getName();
        return declClass.equals("com.android.server.pm.IPackageManagerBase")
                || declClass.equals("com.android.server.pm.PackageManagerService$IPackageManagerImpl");
    }

    private static void testSafeMode(SimpleCFGBuilder cfgBuilder) {

        String methodSignature = "<com.android.server.pm.ComputerEngine: boolean safeMode()>";
        SootMethod tgtMethod = Scene.v().grabMethod(methodSignature);

        if (tgtMethod == null) {
            System.err.println("Method not found: " + methodSignature);
            return;
        }

        String[] classNames = {
                "com.android.server.pm.PackageManagerService$IPackageManagerImpl",
                "com.android.server.pm.IPackageManagerBase"
        };

        // Collect all methods, deduplicate by subSignature
        Map<String, SootMethod> uniqueMethods = new LinkedHashMap<>();
        for (String className : classNames) {
            SootClass sootClass = Scene.v().getSootClass(className);
            for (SootMethod method : getAllRuntimeMethods(sootClass, 1)) {
                if (method.getSignature().contains("lambda$")) continue;
                if (method.getName().equals("<init>")) continue;
                uniqueMethods.putIfAbsent(method.getSubSignature(), method);
            }
        }

        // Analyze each method
        Map<SootMethod, Boolean> reachesTarget = new LinkedHashMap<>();
        for (SootMethod method : uniqueMethods.values()) {
            List<List<SootMethod>> paths = findReverseCallPaths(
                    method, tgtMethod, cfgBuilder.getReverseCfgEdges(), 8);
            reachesTarget.put(method, !paths.isEmpty());
        }

        // Classify
        Map<String, List<Map.Entry<SootMethod, Boolean>>> categories = new LinkedHashMap<>();
        categories.put("Query / Resolve", new ArrayList<>());
        categories.put("Preferred Activity", new ArrayList<>());
        categories.put("Component Info Lookup", new ArrayList<>());
        categories.put("Other", new ArrayList<>());

        for (Map.Entry<SootMethod, Boolean> entry : reachesTarget.entrySet()) {
            if (!isPmsMethod(entry.getKey())) {
                continue;
            }
            String category = classifyMethod(entry.getKey());
            categories.get(category).add(entry);
        }

        // Output markdown table per category
        System.out.println("\n## Safe Mode Coverage Audit Results\n");

        int totalAll = 0, totalReaches = 0, totalViol = 0;

        for (Map.Entry<String, List<Map.Entry<SootMethod, Boolean>>> cat : categories.entrySet()) {
            String catName = cat.getKey();
            List<Map.Entry<SootMethod, Boolean>> methods = cat.getValue();

            int total = methods.size();
            int reaches = (int) methods.stream().filter(Map.Entry::getValue).count();
            int violations = catName.equals("Component Info Lookup") ? (total - reaches) : 0;

            totalAll += total;
            totalReaches += reaches;
            totalViol += violations;

            System.out.println("### " + catName + " (" + total + " methods, "
                    + reaches + " reach safeMode, " + violations + " C1 violations)\n");
            System.out.println("| # | Method | Return Type | Reaches safeMode() |");
            System.out.println("|---|--------|-------------|-------------------|");

            int idx = 1;
            for (Map.Entry<SootMethod, Boolean> e : methods) {
                SootMethod m = e.getKey();
                String retType = m.getReturnType().toString();
                // Shorten common types
                retType = retType.replace("android.content.pm.", "");
                retType = retType.replace("android.content.", "");
                retType = retType.replace("android.graphics.", "");
                retType = retType.replace("java.lang.", "");
                retType = retType.replace("java.util.", "");

                String reachStr = e.getValue() ? "✓" : "✗";
                System.out.println("| " + idx++ + " | `" + m.getName() + "` | `"
                        + retType + "` | " + reachStr + " |");
            }
            System.out.println();
        }

        // Summary table
        System.out.println("### Summary\n");
        System.out.println("| Category | Total | Reaches safeMode() | C1 viol. |");
        System.out.println("|----------|-------|--------------------|----------|");
        for (Map.Entry<String, List<Map.Entry<SootMethod, Boolean>>> cat : categories.entrySet()) {
            String catName = cat.getKey();
            List<Map.Entry<SootMethod, Boolean>> methods = cat.getValue();
            int total = methods.size();
            int reaches = (int) methods.stream().filter(Map.Entry::getValue).count();
            int violations = catName.equals("Component Info Lookup") ? (total - reaches) : 0;
            System.out.println("| " + catName + " | " + total + " | " + reaches
                    + " | " + (violations > 0 ? "**" + violations + "**" : "0") + " |");
        }
        System.out.println("| **Total** | **" + totalAll + "** | **" + totalReaches
                + "** | **" + totalViol + "** |");

        System.out.println("\nAnalysis finished!");
    }

    private static String classifyMethod(SootMethod method) {
        String name = method.getName();
        Type retType = method.getReturnType();
        String retName = retType.toString();

        // Component Info Lookup: returns XxxInfo directly by ComponentName/package name/authority
        if (isComponentInfoReturn(retName) && isDirectLookupName(name)) {
            return "Component Info Lookup";
        }

        // Query / Resolve
        if (name.startsWith("queryIntent")
                || name.equals("resolveIntent")
                || name.equals("resolveService")
                || name.equals("canForwardTo")
                || name.equals("getHomeActivities")
                || name.equals("getLaunchIntentSenderForPackage")
                || name.equals("getInstantAppResolverComponent")) {
            return "Query / Resolve";
        }

        // Preferred Activity
        if (name.contains("PreferredActivity")
                || name.contains("PersistentPreferred")
                || name.contains("HomeActivity")
                || name.equals("setHomeActivity")
                || name.contains("LastChosen")
                || name.equals("resetApplicationPreferences")) {
            return "Preferred Activity";
        }

        // Fallback: check if return type is component info but not caught above
        if (isComponentInfoReturn(retName)) {
            return "Component Info Lookup";
        }

        return "Other";
    }

    private static boolean isDirectLookupName(String name) {
        return name.equals("getActivityInfo")
                || name.equals("getServiceInfo")
                || name.equals("getReceiverInfo")
                || name.equals("getProviderInfo")
                || name.equals("getApplicationInfo")
                || name.equals("getPackageInfo")
                || name.equals("getPackageInfoVersioned")
                || name.equals("resolveContentProvider");
    }

    private static boolean isComponentInfoReturn(String retName) {
        return retName.contains("ActivityInfo")
                || retName.contains("ServiceInfo")
                || retName.contains("ProviderInfo")
                || retName.contains("ApplicationInfo")
                || retName.contains("PackageInfo");
    }



    private static void testXMLParsing(SimpleCFGBuilder cfgBuilder) {
        String targetPkgPrefix = "android.content.res";


        List<SootClass> targetClasses = Scene.v().getApplicationClasses()
                .stream()
                .filter(sc -> sc.getName().equals(targetPkgPrefix) || sc.getName().startsWith(targetPkgPrefix + "."))
                .collect(Collectors.toList());

        if (targetClasses.isEmpty()) {
            targetClasses = Scene.v().getClasses().stream()
                    .filter(sc -> sc.getName().equals(targetPkgPrefix) || sc.getName().startsWith(targetPkgPrefix + "."))
                    .collect(Collectors.toList());
        }

        System.out.println("# Native methods in package: " + targetPkgPrefix);

        int total = 0;
        List<String> all = new ArrayList<>();
        List<String> arrayRet = new ArrayList<>();
        Set<SootMethod> nativeTargets = new HashSet<>();

        for (SootClass sc : targetClasses) {
            if (sc.isPhantom()) continue;

            for (SootMethod m : sc.getMethods()) {
                if (!m.isNative()) continue;

                total++;
                String mods = Modifier.toString(m.getModifiers());
                String ret = m.getReturnType().toString();
                String name = m.getName();
                String params = m.getParameterTypes().stream()
                        .map(Type::toString)
                        .collect(Collectors.joining(", "));
                String line = String.format("%s %s %s.%s(%s)", mods, ret, sc.getName(), name, params);
                all.add(line);


                if (!isPrimitive(m.getReturnType())) {
                    arrayRet.add(line);
                    nativeTargets.add(m);
                }

            }
        }

        // 原始列表
        all.forEach(System.out::println);
        System.out.println("# Total native methods: " + total);

        // 可变返回值 返回清单
        System.out.println("\n# Native methods with Var-length return return type");
        arrayRet.forEach(System.out::println);
        System.out.println("# Total native methods: " + arrayRet.size());

        FastHierarchy fh = Scene.v().getOrMakeFastHierarchy();
        String systemServiceFqn = "com.android.server.SystemService";
        SootClass base = Scene.v().forceResolve(systemServiceFqn, SootClass.SIGNATURES);
        base.setApplicationClass();

        Set<SootClass> serviceClasses = new HashSet<>();

        for (SootClass sc : Scene.v().getClasses()) {
            try {
                if (!sc.isInterface() && fh.canStoreType(sc.getType(), base.getType())) {
                    // canStoreType(A, B): A 可以赋给 B，意味着 A 是 B 的子类或实现
                    if (!sc.getName().equals(systemServiceFqn)) {
                        serviceClasses.add(sc);
                    }
                }
            } catch (RuntimeException e) {
                // 某些 phantom 类可能触发异常，跳过
            }
        }


        System.out.println("Found SystemService subclasses: " + serviceClasses.size());
        for (SootClass sc : serviceClasses) {
            System.out.println(" - " + sc.getName());

        }

        for (SootClass sc : serviceClasses) {

            List<SootMethod> sourceMethods = collectSystemServiceLifecycleMethods(sc);
            if (sourceMethods.isEmpty()) {
//                System.out.println("No lifecycle methods found in " + sc);
//                return;
            }

            for (SootMethod sm : sourceMethods) {
                System.out.println("\""+sm+"\",");
            }

//            findPathsFromSystemServer(cfgBuilder.getCfgEdges(), nativeTargets, sc);
        }
    }


    public static class MyInvokeResolver implements MethodPathFinder.InvokeTargetResolver {
        @Override
        public Collection<SootMethod> resolveImplementations(SootMethod declaredTarget) {
            try {
                String subSig = declaredTarget.getSubSignature();
                SootClass base = declaredTarget.getDeclaringClass();
                Hierarchy h = Scene.v().getActiveHierarchy();

                Collection<SootClass> candidates = base.isInterface()
                        ? h.getImplementersOf(base)
                        : h.getSubclassesOf(base);

                for (SootClass c : candidates) {
                    if (c.isInterface() || c.isAbstract() || c.isPhantom()) continue;
                    if (!c.declaresMethod(subSig)) continue;
                    SootMethod m = c.getMethod(subSig);
                    if (m.isConcrete()) {
                        // 只返回一个实现，立刻短路
                        return Collections.singletonList(m);
                    }
                }
            } catch (Throwable ignore) {
            }
            return Collections.emptyList();
        }
    }

    private static final Set<String> LIFECYCLE_METHOD_NAMES = new HashSet<>(Arrays.asList(
            "<init>",
            "onStart",
            "onBootPhase",
            "onUserStarting",
            "onUserUnlocking",
            "onUserUnlocked",
            "onUserSwitching",
//            "onUserStopping",
//            "onUserStopped",
            "onUserCompletedEvent"
    ));



    private static List<SootMethod> collectSystemServiceLifecycleMethods(SootClass cls) {
        List<SootMethod> list = new ArrayList<>();
        for (SootMethod m : cls.getMethods()) {
            if (!m.isConcrete()) continue;
            if (LIFECYCLE_METHOD_NAMES.contains(m.getName())) {
                list.add(m);
            }
        }
        return list;
    }

    /**
     * 从 frameworks/base/services/voiceinteraction/java/com/android/server/voiceinteraction/VoiceInteractionManagerService
     * 类的所有方法作为起点，使用 BFS 查找是否能到 android.content.res 包内的 native 方法。
     */

    private static void findPathsFromSystemServer(Map<SootMethod, Set<SootMethod>> cfgEdges,
                                                      Set<SootMethod> nativeTargets, SootClass tgtCls) {


        // 仅系统服务生命周期方法作为起点
        List<SootMethod> sourceMethods = collectSystemServiceLifecycleMethods(tgtCls);
        if (sourceMethods.isEmpty()) {
            System.out.println("No lifecycle methods found in " + tgtCls);
            return;
        }

        for (SootMethod sm : sourceMethods) {
            System.out.println(sm+",");
        }
        // 目标（可根据你想要的少数 native 方法再精筛）
        Set<SootMethod> targetSet = new HashSet<>(nativeTargets);

        MethodPathFinder finder = new MethodPathFinder(cfgEdges, new MyInvokeResolver()).withMaxDepth(8000);

        System.out.println("\n# Search paths from " + tgtCls +" LIFECYCLE methods to android.content.res native methods");
        int foundCount = 0;

        for (SootMethod src : sourceMethods) {
            MethodPathFinder.PathResult best = null;
            SootMethod bestTgt = null;

            for (SootMethod tgt : targetSet) {
                MethodPathFinder.PathResult r = finder.findPath(src, tgt);
                if (r.found && (best == null || r.path.size() < best.path.size())) {
                    best = r; bestTgt = tgt;
                }
            }

            if (best != null) {
                foundCount++;
                System.out.println("== Path found ==");
                System.out.println("SRC: " + src.getSignature());
                System.out.println("TGT: " + bestTgt.getSignature());
                for (int i = 0; i < best.path.size(); i++) {
                    System.out.println("  " + i + ": " + best.path.get(i).getSignature());
                }
                System.out.println();
            }
        }

        if (foundCount == 0) {
            System.out.println("No paths found from lifecycle methods.");
        } else {
            System.out.println("Total lifecycle paths found: " + foundCount);
        }
    }


    private static boolean isPrimitive(Type t) {
        if (t instanceof ArrayType) return false;
        if (t instanceof VoidType) return true;
        return t instanceof PrimType;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java -jar Tool.jar config.json");
            return;
        }

        String configFilePath = args[0];
        Config config = Config.loadFromFile(configFilePath);

        initializeSoot(config);

        SimpleCFGBuilder cfgBuilder = new SimpleCFGBuilder(16);
        try {
            cfgBuilder.loadEdgesFromFile("cfg.txt");
        } catch (IOException e) {
            cfgBuilder.buildMethodCallGraph();
        }

//	    cfgBuilder.dumpAllEdges();

        testSafeMode(cfgBuilder);

//        testXMLParsing(cfgBuilder);


    }


}
