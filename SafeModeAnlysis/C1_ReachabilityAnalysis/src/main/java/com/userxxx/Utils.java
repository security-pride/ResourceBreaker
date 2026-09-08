package com.userxxx;


import soot.SootMethod;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Utils {

    public static void writeCallHierarchyDot(SootMethod target,
                                             Map<Integer, Set<SootMethod>> levelToCallers,
                                             Map<SootMethod, Set<SootMethod>> reverseCfgEdges,
                                             String outputPath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.write("digraph CallHierarchy {\n");
            Set<String> writtenEdges = new HashSet<>();

            // 层级遍历中，从 level=1 开始（0 是 target）
            for (Map.Entry<Integer, Set<SootMethod>> entry : levelToCallers.entrySet()) {
                for (SootMethod method : entry.getValue()) {
                    Set<SootMethod> callees = reverseCfgEdges.getOrDefault(method, Collections.emptySet());
                    for (SootMethod callee : callees) {
                        // 只画到调用链中的方法（过滤无关节点）
                        if (levelToCallers.values().stream().anyMatch(set -> set.contains(callee)) || callee.equals(target)) {
                            String edge = "\"" + method.getSignature() + "\" -> \"" + callee.getSignature() + "\";";
                            if (writtenEdges.add(edge)) {
                                writer.write("    " + edge + "\n");
                            }
                        }
                    }
                }
            }

            writer.write("}\n");
            System.out.println("✅ DOT file written to: " + outputPath);
        } catch (IOException e) {
            System.err.println("❌ Failed to write DOT file.");
            e.printStackTrace();
        }
    }

}
