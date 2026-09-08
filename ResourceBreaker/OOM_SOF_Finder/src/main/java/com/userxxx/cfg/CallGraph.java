package com.userxxx.cfg;

import java.util.*;

public class CallGraph {

    private final Map<String, Set<String>> graph = new LinkedHashMap<>();

    public void addFunction(String name) {
        graph.putIfAbsent(name, new LinkedHashSet<>());
    }

    public void addCall(String caller, String callee) {
        graph.computeIfAbsent(caller, k -> new LinkedHashSet<>()).add(callee);
    }

    public List<List<String>> detectCycles() {
        List<List<String>> cycles = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Set<String> onStack = new HashSet<>();
        Deque<String> path = new ArrayDeque<>();
        for (String node : graph.keySet()) {
            if (!visited.contains(node)) {
                dfs(node, visited, onStack, path, cycles);
            }
        }
        return cycles;
    }

    private void dfs(String node, Set<String> visited, Set<String> onStack,
                     Deque<String> path, List<List<String>> cycles) {
        visited.add(node);
        onStack.add(node);
        path.push(node);
        for (String callee : graph.getOrDefault(node, Collections.emptySet())) {
            if (onStack.contains(callee)) {
                List<String> stack = new ArrayList<>(path);
                Collections.reverse(stack);
                List<String> cycle = new ArrayList<>();
                boolean recording = false;
                for (String s : stack) {
                    if (s.equals(callee)) recording = true;
                    if (recording) cycle.add(s);
                }
                cycle.add(callee);
                cycles.add(cycle);
            } else if (!visited.contains(callee)) {
                dfs(callee, visited, onStack, path, cycles);
            }
        }
        path.pop();
        onStack.remove(node);
    }

    public List<String> detectVirtualRecursions() {
        List<String> results = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : graph.entrySet()) {
            String caller = entry.getKey();
            String callerShort = getShortName(caller);
            for (String callee : entry.getValue()) {
                String calleeShort = getShortName(callee);
                if (callerShort.equals(calleeShort) && callee.contains("->")) {
                    results.add(caller + " -> " + callee);
                }
            }
        }
        return results;
    }

    private String getShortName(String name) {
        int i = name.lastIndexOf("::");
        if (i >= 0 && i + 2 < name.length()) return name.substring(i + 2);
        i = name.lastIndexOf("->");
        if (i >= 0 && i + 2 < name.length()) return name.substring(i + 2);
        i = name.lastIndexOf('.');
        if (i >= 0 && i + 1 < name.length()) return name.substring(i + 1);
        return name;
    }

    public void printAllLoops() {
        boolean found = false;

        List<List<String>> cycles = detectCycles();
        if (!cycles.isEmpty()) {
            found = true;
            System.out.println("[!] Call graph cycles detected:");
            for (List<String> c : cycles) {
                System.out.println("  " + String.join(" -> ", c));
            }
            System.out.println();
        }

        List<String> vrecs = detectVirtualRecursions();
        if (!vrecs.isEmpty()) {
            found = true;
            System.out.println("[!] Virtual/polymorphic pointer recursion (stack overflow risk):");
            for (String s : vrecs) {
                System.out.println("  " + s);
            }
            System.out.println();
        }

        if (!found) {
            System.out.println("[OK] No recursive call risks detected.");
        }
    }
}