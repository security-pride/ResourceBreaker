package com.userxxx.symbolic.parser;


import com.userxxx.core.Pair;
import com.userxxx.parser.AndroidResourceParser;
import com.userxxx.parser.AndroidResourceParserBaseListener;

import java.util.List;


public class MainHandler extends AndroidResourceParserBaseListener {

    final private Scope root = new Scope();
    private Scope currentScope = root;

    private boolean isInit = false;

    private void enterScope(int startLine){
        Scope newScope = new Scope();
        newScope.parent = currentScope;
        newScope.start = startLine;
        if (currentScope != null) {
            currentScope.children.add(newScope);
        }
        currentScope = newScope;
    }

    private void exitScope(int endLine){
        currentScope.end = endLine;
        currentScope = currentScope.parent;
    }


    @Override
    public void enterEnumDecl(AndroidResourceParser.EnumDeclContext ctx) {
        String typeName = ctx.type() != null ? ctx.type().getText() : "int";

        List<AndroidResourceParser.EnumItemContext> enumItems = ctx.enumItemList().enumItem();

        int autoValue = 0;

        for (AndroidResourceParser.EnumItemContext itemCtx : enumItems) {
            String key = null;
            String valueStr = null;

            if (itemCtx.assignStmt() != null) {
                // assignStmt: IDENTIFIER assign_op expr
                key = itemCtx.assignStmt().IDENTIFIER().getText();
                valueStr = itemCtx.assignStmt().expr().getText();
            } else if (itemCtx.IDENTIFIER() != null) {
                key = itemCtx.IDENTIFIER().getText();
                valueStr = String.valueOf(autoValue);
            }

            Value v = new Value();
            v.Type = typeName;
            v.isArray = false;
            v.data = valueStr;

            currentScope.variables.put(key, v);

            autoValue++;
        }
    }


    private String typeDefIdentifiers = null;

    @Override
    public void enterTypedefDecl(AndroidResourceParser.TypedefDeclContext ctx) {
        if(ctx.structDecl() != null)
            typeDefIdentifiers = ctx.IDENTIFIER().getText();
    }

    @Override
    public void exitTypedefDecl(AndroidResourceParser.TypedefDeclContext ctx) {
    }

    @Override
    public void enterStructDecl(AndroidResourceParser.StructDeclContext ctx) {

        StructInfo structInfo = new StructInfo();

        if (ctx.block() != null) {
            for (AndroidResourceParser.StatementContext stmtCtx : ctx.block().statement()) {

                if (stmtCtx.varDecl() != null) {
                    if (stmtCtx.varDecl().LOCAL() != null){

                    }else {
                        System.out.println(stmtCtx.varDecl().getText());
                        String fieldName = stmtCtx.varDecl().IDENTIFIER().getText();
                        structInfo.fields.add(fieldName);
                    }
                }
            }
        }


        if (typeDefIdentifiers != null) {
            currentScope.typeDefVariables.put(typeDefIdentifiers, structInfo.toValue());
        }

        if (ctx.IDENTIFIER() != null){
            currentScope.structDefVariables.put(ctx.IDENTIFIER().getText(), structInfo);
        }

    }


    @Override
    public void enterFuncDecl(AndroidResourceParser.FuncDeclContext ctx) {
        FunctionInfo funcInfo = new FunctionInfo();

        funcInfo.returnType = ctx.type().getText();

        funcInfo.name = ctx.IDENTIFIER().getText();

        funcInfo.startLine = ctx.getStart().getLine();
        funcInfo.endLine = ctx.getStop().getLine();

        if (ctx.paramList() != null) {
            for (AndroidResourceParser.ParamContext paramCtx : ctx.paramList().param()) {


                String paramType = paramCtx.IDENTIFIER().getText();
                // 支持指针/引用
                String pointerOrRef = "";
                if (paramCtx.AND() != null) pointerOrRef = "&";
                if (paramCtx.STAR() != null) pointerOrRef = "*";
                paramType += pointerOrRef;

                String paramName = paramCtx.IDENTIFIER().getText();
                Pair<String, String> paramInfo = new Pair<>(paramType, paramName);
                funcInfo.params.add(paramInfo);
            }
        }

        // 存入当前作用域
        currentScope.functionDefVariables.put(funcInfo.name, funcInfo);

        System.out.println("Function found: " + funcInfo.name + " return type: " + funcInfo.returnType);


    }

    @Override
    public void enterStatementOrBlock(AndroidResourceParser.StatementOrBlockContext ctx) {
        enterScope(ctx.getStart().getLine());
    }

    @Override
    public void exitStatementOrBlock(AndroidResourceParser.StatementOrBlockContext ctx) {
        exitScope(ctx.getStop().getLine());
    }

    @Override
    public void enterBlock(AndroidResourceParser.BlockContext ctx) {
        enterScope(ctx.getStart().getLine());
    }

    @Override
    public void exitBlock(AndroidResourceParser.BlockContext ctx) {
        exitScope(ctx.getStop().getLine());
    }


    @Override
    public void exitFile(AndroidResourceParser.FileContext ctx) {
        isInit = true;
    }

    public Scope getRootScope() {
        if (isInit)
            return currentScope;
        else
            throw new RuntimeException("Must init MainHandler");
    }




}
