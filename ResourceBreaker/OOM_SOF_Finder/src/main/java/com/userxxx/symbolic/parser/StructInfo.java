package com.userxxx.symbolic.parser;

import java.util.ArrayList;
import java.util.List;

public class StructInfo {
    public String name;
//    public String typedefName;
    public List<String> fields = new ArrayList<>();

    public Value toValue(){
        Value value = new Value();
        value.Type = "struct";
        value.data = this;
        return value;
    }
}
