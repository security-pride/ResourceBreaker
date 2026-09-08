package com.userxxx.symbolic.parser;

import com.userxxx.core.Pair;
import com.userxxx.parser.AndroidResourceParser;

import java.util.ArrayList;
import java.util.List;

public class FunctionInfo {
    public String name;
    public String returnType;
    public List<Pair<String, String>> params = new ArrayList<>();
    public int startLine;
    public int endLine;
    public AndroidResourceParser.BlockContext blockContext;

}