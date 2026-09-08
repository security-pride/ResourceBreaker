package com.userxxx.core;


import soot.SootMethod;
import java.util.*;

public class MethodPathFinder {

    public interface InvokeTargetResolver {
        Collection<SootMethod> resolveImplementations(SootMethod declaredTarget);
    }

    public static class NoOpResolver implements InvokeTargetResolver {
        public Collection<SootMethod> resolveImplementations(SootMethod m){ return Collections.emptyList(); }
    }

    public static class PathResult {
        public final List<SootMethod> path;
        public final boolean found;
        public PathResult(List<SootMethod> path, boolean found){ this.path = path; this.found = found; }
    }

    private final Map<SootMethod, Set<SootMethod>> graph;
    private final InvokeTargetResolver resolver;
    private int maxDepth = 10;

    private static final Set<String> BLACKLIST = new HashSet<>(Arrays.asList(
            "<com.android.server.SystemService: void publishBinderService(java.lang.String,android.os.IBinder,boolean,int)>",
            "<android.view.textclassifier.TextClassification$1: java.lang.Object createFromParcel(android.os.Parcel)>",
            "<android.content.res.AssetManager: java.lang.String[] getLocales()>",
            "<android.content.Context: java.lang.Object getSystemService(java.lang.Class)>",
            "<android.app.ContextImpl: java.lang.Object getSystemService(java.lang.String)>",
            "<android.content.res.Resources: android.content.res.Resources getSystem()>",
            "<android.os.Binder: boolean transact(int,android.os.Parcel,android.os.Parcel,int)>",
            "<android.os.Binder: boolean onTransact(int,android.os.Parcel,android.os.Parcel,int)>",
            "<android.app.ContextImpl: java.lang.ClassLoader getClassLoader()>"
    ));

    public MethodPathFinder(Map<SootMethod, Set<SootMethod>> graph, InvokeTargetResolver resolver){
        this.graph = graph;
        this.resolver = (resolver == null) ? new NoOpResolver() : resolver;
    }
    public MethodPathFinder withMaxDepth(int d){ this.maxDepth = d; return this; }


    private static boolean isBlacklisted(SootMethod m) {
        if (m == null) return false;
        try {
            return BLACKLIST.contains(m.getSignature());
        } catch (Throwable ignored) {
            return false;
        }
    }

    public PathResult findPath(SootMethod src, SootMethod dst){
        if (src == null || dst == null) return new PathResult(Collections.emptyList(), false);

        // 如果不希望起点或终点是黑名单，直接失败或按需处理
        if (isBlacklisted(src) || isBlacklisted(dst)) {
            // 选择其一：
            // return new PathResult(Collections.emptyList(), false);
            // 或者继续但防止扩展黑名单节点
        }

        if (src.equals(dst)) {
            if (isBlacklisted(src)) return new PathResult(Collections.emptyList(), false);
            return new PathResult(Collections.singletonList(src), true);
        }

        Deque<SootMethod> q = new ArrayDeque<>();
        Map<SootMethod,SootMethod> prev = new HashMap<>();
        Set<SootMethod> vis = new HashSet<>();

        // 起点如果在黑名单，按政策：这里选择不入队
        if (!isBlacklisted(src)) {
            q.add(src); vis.add(src); prev.put(src, null);
        } else {
            return new PathResult(Collections.emptyList(), false);
        }

        int depth = 0;
        while(!q.isEmpty() && depth++ <= maxDepth){
            int sz = q.size();
            for(int i=0;i<sz;i++){
                SootMethod cur = q.poll();
                if (cur == null) continue;

                Set<SootMethod> ns = graph.getOrDefault(cur, Collections.emptySet());
                Set<SootMethod> exp = new LinkedHashSet<>(ns);
                for (SootMethod t : ns) {
                    if (t != null && (t.isAbstract() || t.getDeclaringClass().isInterface())) {
                        exp.addAll(safeResolve(t));
                    }
                }

                for (SootMethod nxt : exp) {
                    if (nxt == null) continue;

                    // 黑名单策略：
                    // 1) 完全跳过黑名单方法（既不作为中间节点，也不作为终点）
                    if (isBlacklisted(nxt)) continue;

                    if (vis.contains(nxt)) continue;

                    // 如果终点本身可能在黑名单，这里已经被 continue 掉了
                    // 如果你希望“允许终点是黑名单，但不继续扩展它”，可以改成：
                    // boolean isDst = nxt.equals(dst);
                    // if (!isDst && isBlacklisted(nxt)) continue;

                    vis.add(nxt);
                    prev.put(nxt, cur);

                    if (nxt.equals(dst)) {
                        return new PathResult(reconstruct(prev, dst), true);
                    }
                    q.add(nxt);
                }
            }
        }
        return new PathResult(Collections.emptyList(), false);
    }

    private Collection<SootMethod> safeResolve(SootMethod m){
        try {
            Collection<SootMethod> r = resolver.resolveImplementations(m);
            return (r==null)?Collections.emptyList():r;
        } catch (Throwable ignored){ return Collections.emptyList(); }
    }

    private List<SootMethod> reconstruct(Map<SootMethod,SootMethod> prev, SootMethod t){
        LinkedList<SootMethod> path = new LinkedList<>();
        for(SootMethod c=t; c!=null; c=prev.get(c)) path.addFirst(c);
        return path;
    }
}