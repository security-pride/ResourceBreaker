package com.userxxx.symbolic.parser;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Scope {


//    public List<Pair<String, Value>> variableTable = new ArrayList<>();

    public HashMap<String, Value> variables = new HashMap<>();

    public Scope parent;
    public List<Scope> children = new ArrayList<>();

    public HashMap<String, Value> typeDefVariables = new HashMap<>();

    public HashMap<String, StructInfo> structDefVariables = new HashMap<>();

    public HashMap<String, FunctionInfo> functionDefVariables = new HashMap<>();

    public int start;
    public int end;

    @Override
    public String toString() {
        return "Scope@" + hashCode() +
                " [start=" + start + ", end=" + end + "] " +
                variables.toString();
    }


    public String toTreeString()
    {
        return toTreeString("");
    }

    private String toTreeString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent)
                .append("Scope@").append(hashCode())
                .append(" [start=").append(start)
                .append(", end=").append(end)
                .append("] ").append(variables)
                .append("\n");
        for (Scope child : children) {
            sb.append(child.toTreeString(indent + "  "));
        }
        return sb.toString();
    }


}