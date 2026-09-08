package com.userxxx.cfg;

import com.userxxx.cfg.CallGraph;
import com.userxxx.parser.CPP14Lexer;
import com.userxxx.parser.CPP14Parser;
import com.userxxx.parser.CPP14ParserBaseListener;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import java.util.ArrayDeque;
import java.util.Deque;

public class CallGraphHandler extends CPP14ParserBaseListener {
    private final CallGraph callGraph = new CallGraph();
    private final Deque<String> funcStack = new ArrayDeque<>();
    public CallGraph getCallGraph() { return callGraph; }
    @Override
    public void enterFunctionDefinition(CPP14Parser.FunctionDefinitionContext ctx) {
        String name = extractFuncName(ctx.declarator());
        if (name == null || name.isEmpty()) {
            name = "<anon@L" + ctx.getStart().getLine() + ">";
        }
        callGraph.addFunction(name);
        funcStack.push(name);
    }
    @Override
    public void exitFunctionDefinition(CPP14Parser.FunctionDefinitionContext ctx) {
        if (!funcStack.isEmpty()) funcStack.pop();
    }
    @Override
    public void enterPostfixExpression(CPP14Parser.PostfixExpressionContext ctx) {
        if (funcStack.isEmpty()) return;
        String caller = funcStack.peek();
        for (int i = 1; i < ctx.getChildCount(); i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof TerminalNode
                    && ((TerminalNode) child).getSymbol().getType() == CPP14Lexer.LeftParen) {
                ParseTree prev = ctx.getChild(i - 1);
                if (prev instanceof CPP14Parser.SimpleTypeSpecifierContext
                        || prev instanceof CPP14Parser.TypeNameSpecifierContext) {
                    continue;
                }
                String callee = extractCallee(prev);
                if (callee != null && !callee.isEmpty()) {
                    callGraph.addCall(caller, callee);
                }
            }
        }
    }
    private String extractFuncName(CPP14Parser.DeclaratorContext ctx) {
        if (ctx == null) return null;
        if (ctx.pointerDeclarator() != null)
            return extractFromNoPtrDecl(ctx.pointerDeclarator().noPointerDeclarator());
        if (ctx.noPointerDeclarator() != null)
            return extractFromNoPtrDecl(ctx.noPointerDeclarator());
        return null;
    }
    private String extractFromNoPtrDecl(CPP14Parser.NoPointerDeclaratorContext ctx) {
        if (ctx == null) return null;
        if (ctx.declaratorId() != null) return ctx.declaratorId().getText();
        if (ctx.noPointerDeclarator() != null) return extractFromNoPtrDecl(ctx.noPointerDeclarator());
        if (ctx.pointerDeclarator() != null)
            return extractFromNoPtrDecl(ctx.pointerDeclarator().noPointerDeclarator());
        return null;
    }
    private String extractCallee(ParseTree node) {
        if (node == null) return null;
        String text = node.getText();
        if (text == null || text.isEmpty()) return null;
        int lt = text.indexOf('<');
        if (lt > 0) text = text.substring(0, lt);
        return text;
    }
}