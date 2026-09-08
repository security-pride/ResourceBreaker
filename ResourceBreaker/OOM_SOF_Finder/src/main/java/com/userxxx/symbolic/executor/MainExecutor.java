package com.userxxx.symbolic.executor;

import com.userxxx.parser.AndroidResourceParser;
import com.userxxx.symbolic.parser.MainHandler;
import com.microsoft.z3.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainExecutor {

    private final ParseTree parseTree;
    private final MainHandler handler;

    private final Deque<Map<String, Integer>> scopeStack = new ArrayDeque<>();
    private final Deque<List<BoolExpr>> pcStack = new ArrayDeque<>();

    private final com.microsoft.z3.Context z3;

    private BigInteger utf8MaxLen = null;
    private BigInteger utf16MaxLen = null;

    public MainExecutor(ParseTree tree, MainHandler handler) {
        this.parseTree = tree;
        this.handler = handler;
        com.microsoft.z3.Global.setParameter("model", "true");
        this.z3 = new com.microsoft.z3.Context();
    }

    public void Exec() {
        if (!(parseTree instanceof AndroidResourceParser.FileContext)) {
            throw new IllegalStateException("ParseTree is not FileContext");
        }
        AndroidResourceParser.FileContext fileCtx = (AndroidResourceParser.FileContext) parseTree;

        scopeStack.push(new HashMap<>());
        pcStack.push(new ArrayList<>());

        for (AndroidResourceParser.StatementContext stmt : fileCtx.statement()) {
            handleStatement(stmt);
        }

        System.out.println("==== Prediction Results ====");
        if (utf16MaxLen != null) {
            System.out.printf("UTF-16 max string length: %s (0x%s)\n",
                    utf16MaxLen, utf16MaxLen.toString(16));
        } else {
            System.out.println("UTF-16 max string length: <no wchar_t array declaration observed>");
        }
        if (utf8MaxLen != null) {
            System.out.printf("UTF-8 max string length: %s (0x%s)\n",
                    utf8MaxLen, utf8MaxLen.toString(16));
        } else {
            System.out.println("UTF-8 max string length: <no char array declaration observed>");
        }

        scopeStack.pop();
        pcStack.pop();
        z3.close();
    }

    private void handleStatement(AndroidResourceParser.StatementContext ctx) {
        if (ctx.varDecl() != null) {
            handleVarDecl(ctx.varDecl());
        } else if (ctx.assignStmt() != null) {
            handleAssignStmt(ctx.assignStmt());
        } else if (ctx.exprStmt() != null) {
            handleExprStmt(ctx.exprStmt());
        } else if (ctx.ifStmt() != null) {
            handleIfStmt(ctx.ifStmt());
        } else if (ctx.forStmt() != null) {
            handleForStmt(ctx.forStmt());
        } else if (ctx.whileStmt() != null) {
            handleWhileStmt(ctx.whileStmt());
        } else if (ctx.switchStmt() != null) {
            handleSwitchStmt(ctx.switchStmt());
        } else if (ctx.returnStmt() != null) {
            handleReturnStmt(ctx.returnStmt());
        } else if (ctx.breakStmt() != null) {
            handleBreakStmt(ctx.breakStmt());
        } else if (ctx.structDecl() != null) {
            handleStructDecl(ctx.structDecl());
        } else if (ctx.typedefDecl() != null) {
            handleTypedefDecl(ctx.typedefDecl());
        } else if (ctx.enumDecl() != null) {
            handleEnumDecl(ctx.enumDecl());
        } else {
            System.out.println("Unhandled statement: " + ctx.getText());
        }
    }

    private void handleVarDecl(AndroidResourceParser.VarDeclContext ctx) {
        AndroidResourceParser.VarDeclContext decl = ctx;
        if (decl.LOCAL() != null) decl = decl.varDecl();

        String typeText = decl.type().getText();
        String name = decl.IDENTIFIER().getText();
        Integer width = inferBitWidthFromType(typeText);
        declareVar(name, width);

        System.out.printf("Variable declaration: type=%s, name=%s\n", typeText, name);

        AndroidResourceParser.ExprContext arrayLenExpr = extractArrayLengthExpr(decl);
        if (arrayLenExpr != null) {
            System.out.printf("    Array length expression detected: %s\n", arrayLenExpr.getText());

            BuildResult brCap = buildBV(arrayLenExpr, 32);
            if (brCap != null && brCap.bv != null) {
                System.out.println("    Capacity Z3 expression: " + brCap.bv);
            } else {
                System.out.println("    Capacity Z3 expression: <unsupported expression/insufficient type info>");
            }

            BuildResult brLen = (brCap != null && brCap.bv != null) ? capMinusOne(brCap) : null;

            if (brLen != null && brLen.bv != null) {
                System.out.println("    lenBV Z3 expression: " + brLen.bv);

                MaxResult mr = maximizeUnsignedBVFromBuild(brLen);
                if (mr != null && mr.modelValue != null) {
                    System.out.printf("    Max string length: %s (0x%s)\n",
                            mr.modelValue.toString(), mr.modelValue.toString(16));

                    String enc = classifyEncoding(typeText);
                    if ("UTF-16".equals(enc)) utf16MaxLen = max(utf16MaxLen, mr.modelValue);
                    else if ("UTF-8".equals(enc)) utf8MaxLen = max(utf8MaxLen, mr.modelValue);

                    if (brCap != null) {
                        System.out.printf("    Max capacity: %s (0x%s)\n",
                                mr.modelValue.add(BigInteger.ONE), mr.modelValue.add(BigInteger.ONE).toString(16));
                    }
                    printModelForUsedVars(mr.model, brLen.usedVars);
                } else {
                    System.out.println("    Prediction failed or no upper bound (insufficient constraints/unsupported expression)");
                }
            }
        }
    }

    private void handleAssignStmt(AndroidResourceParser.AssignStmtContext ctx) {
        String name = ctx.IDENTIFIER().getText();
        String value = ctx.expr().getText();
        System.out.printf("Assignment: %s = %s\n", name, value);
    }

    private void handleExprStmt(AndroidResourceParser.ExprStmtContext ctx) {
        System.out.println("Expression statement: " + ctx.expr().getText());
    }

    private void handleIfStmt(AndroidResourceParser.IfStmtContext ctx) {
        System.out.println("If statement, condition: " + ctx.expr().getText());

        BoolExpr cond = buildBoolExpr(ctx.expr());

        {
            List<BoolExpr> thenPC = new ArrayList<>(pcStack.peek());
            if (cond != null) thenPC.add(cond);
            pcStack.push(thenPC);
            handleStatementOrBlock(ctx.statementOrBlock(0));
            pcStack.pop();
        }

        if (ctx.ELSE() != null) {
            BoolExpr notCond = (cond == null) ? null : z3.mkNot(cond);
            List<BoolExpr> elsePC = new ArrayList<>(pcStack.peek());
            if (notCond != null) elsePC.add(notCond);
            pcStack.push(elsePC);
            handleStatementOrBlock(ctx.statementOrBlock(1));
            pcStack.pop();
        }
    }

    private void handleStatementOrBlock(AndroidResourceParser.StatementOrBlockContext ctx) {
        if (ctx.statement() != null && !ctx.statement().isEmpty()) {
            handleStatement(ctx.statement(0));
        } else {
            scopeStack.push(new HashMap<>(scopeStack.peek()));
            for (AndroidResourceParser.StatementContext st : ctx.statement()) {
                handleStatement(st);
            }
            scopeStack.pop();
        }
    }

    private void handleWhileStmt(AndroidResourceParser.WhileStmtContext ctx) {
        System.out.println("While loop: " + ctx.getText());
        BoolExpr cond = buildBoolExpr(ctx.expr());
        List<BoolExpr> loopPC = new ArrayList<>(pcStack.peek());
        if (cond != null) loopPC.add(cond);
        pcStack.push(loopPC);
        handleBlock(ctx.block());
        pcStack.pop();
    }

    private void handleForStmt(AndroidResourceParser.ForStmtContext ctx) {
        String raw = ctx.getText();
        System.out.println("For loop: " + raw);

        if (ctx.expr() != null && !ctx.expr().isEmpty()) {
            BoolExpr cond = buildBoolExpr(ctx.expr(0));
            List<BoolExpr> loopPC = new ArrayList<>(pcStack.peek());
            if (cond != null) loopPC.add(cond);
            pcStack.push(loopPC);
            handleStatementOrBlock(ctx.statementOrBlock());
            pcStack.pop();
        } else {
            handleStatementOrBlock(ctx.statementOrBlock());
        }

        scanAndSolveArraysInRawForBlock(raw);
    }

    private void handleBlock(AndroidResourceParser.BlockContext ctx) {
        scopeStack.push(new HashMap<>(scopeStack.peek()));
        for (AndroidResourceParser.StatementContext st : ctx.statement()) {
            handleStatement(st);
        }
        scopeStack.pop();
    }

    private void handleSwitchStmt(AndroidResourceParser.SwitchStmtContext ctx) {
        System.out.println("Switch statement (path branching not yet expanded): " + ctx.getText());
    }

    private void handleReturnStmt(AndroidResourceParser.ReturnStmtContext ctx) {
        String value = ctx.expr() != null ? ctx.expr().getText() : null;
        System.out.println("Return statement: " + value);
    }

    private void handleBreakStmt(AndroidResourceParser.BreakStmtContext ctx) {
        System.out.println("Break statement");
    }

    private void handleStructDecl(AndroidResourceParser.StructDeclContext ctx) {
        System.out.println("Struct declaration: " +
                (ctx.IDENTIFIER() != null ? ctx.IDENTIFIER().getText() : "<anonymous>"));
        if (ctx.block() != null) {
            handleBlock(ctx.block());
        }
    }

    private void handleTypedefDecl(AndroidResourceParser.TypedefDeclContext ctx) {
        System.out.println("Typedef declaration: " + ctx.getText());
        if (ctx.structDecl() != null) handleStructDecl(ctx.structDecl());
        if (ctx.unionDecl() != null) handleUnionDecl(ctx.unionDecl());
    }

    private void handleUnionDecl(AndroidResourceParser.UnionDeclContext ctx) {
        System.out.println("Union declaration: " + (ctx.IDENTIFIER() != null ? ctx.IDENTIFIER().getText() : "<anonymous>"));
        if (ctx.structItemList() != null) {
            scopeStack.push(new HashMap<>(scopeStack.peek()));
            for (AndroidResourceParser.StructItemContext it : ctx.structItemList().structItem()) {
                if (it.varDecl() != null) handleVarDecl(it.varDecl());
                else if (it.structDecl() != null) handleStructDecl(it.structDecl());
                else if (it.typedefDecl() != null) handleTypedefDecl(it.typedefDecl());
                else if (it.enumDecl() != null) handleEnumDecl(it.enumDecl());
                else if (it.assignStmt() != null) handleAssignStmt(it.assignStmt());
            }
            scopeStack.pop();
        }
    }

    private void handleEnumDecl(AndroidResourceParser.EnumDeclContext ctx) {
        System.out.println("Enum declaration: " + ctx.getText());
    }

    private AndroidResourceParser.ExprContext extractArrayLengthExpr(AndroidResourceParser.VarDeclContext decl) {
        for (int i = 0; i < decl.getChildCount(); i++) {
            if ("[".equals(decl.getChild(i).getText())) {
                for (int j = i + 1; j < decl.getChildCount(); j++) {
                    if (decl.getChild(j) instanceof AndroidResourceParser.ExprContext) {
                        return (AndroidResourceParser.ExprContext) decl.getChild(j);
                    }
                }
            }
        }
        return null;
    }

    // ========== Antlr ExprContext: BV/Bool ==========

    private BuildResult buildBV(AndroidResourceParser.ExprContext ctx, int defaultBitWidth) {
        if (ctx == null) return null;

        if (ctx.getChildCount() == 3
                && "(".equals(ctx.getChild(0).getText())
                && ")".equals(ctx.getChild(2).getText())) {
            return buildBV((AndroidResourceParser.ExprContext) ctx.getChild(1), defaultBitWidth);
        }

        if (ctx.getChildCount() == 1) {
            String t = ctx.getChild(0).getText();
            if (isNumberToken(t)) {
                int bw = defaultBitWidth;
                return BuildResult.constBV(z3, parseBVConst(t, bw), bw);
            }
            if (ctx.IDENTIFIER() != null) {
                String name = t;
                int bw = lookupVarBitWidth(name, defaultBitWidth);
                return BuildResult.varBV(z3, name, bw);
            }
        }

        if (ctx.getChildCount() == 2) {
            String op = ctx.getChild(0).getText();
            BuildResult r = buildBV((AndroidResourceParser.ExprContext) ctx.getChild(1), defaultBitWidth);
            if (r == null || r.bv == null) return null;
            if ("+".equals(op)) return r;
            if ("-".equals(op)) return BuildResult.wrap(z3.mkBVNeg(r.bv), r.usedVars, r.bitWidth);
        }

        if (ctx.getChildCount() == 3) {
            String op = ctx.getChild(1).getText();

            if (isRelOp(op) || "&&".equals(op) || "||".equals(op)) return null;

            BuildResult l = buildBV((AndroidResourceParser.ExprContext) ctx.getChild(0), defaultBitWidth);
            BuildResult r = buildBV((AndroidResourceParser.ExprContext) ctx.getChild(2), defaultBitWidth);
            if (l == null || r == null || l.bv == null || r.bv == null) return null;

            int bw = Math.max(l.bitWidth, r.bitWidth);
            BitVecExpr L = ensureWidth(l.bv, l.bitWidth, bw);
            BitVecExpr R = ensureWidth(r.bv, r.bitWidth, bw);
            Map<String, Integer> used = mergeUsed(l.usedVars, r.usedVars);

            switch (op) {
                case "+":  return BuildResult.wrap(z3.mkBVAdd(L, R), used, bw);
                case "-":  return BuildResult.wrap(z3.mkBVSub(L, R), used, bw);
                case "*":  return BuildResult.wrap(z3.mkBVMul(L, R), used, bw);
                case "/":  return BuildResult.wrap(z3.mkBVUDiv(L, R), used, bw);
                case "%":  return BuildResult.wrap(z3.mkBVURem(L, R), used, bw);
                case "<<": return BuildResult.wrap(z3.mkBVSHL(L, R), used, bw);
                case ">>": return BuildResult.wrap(z3.mkBVLSHR(L, R), used, bw);
                case "&":  return BuildResult.wrap(z3.mkBVAND(L, R), used, bw);
                case "|":  return BuildResult.wrap(z3.mkBVOR(L, R), used, bw);
                case ".":  return null;
                default:   return null;
            }
        }

        return null;
    }

    private BoolExpr buildBoolExpr(AndroidResourceParser.ExprContext ctx) {
        if (ctx == null) return null;

        if (ctx.getChildCount() == 3
                && "(".equals(ctx.getChild(0).getText())
                && ")".equals(ctx.getChild(2).getText())) {
            return buildBoolExpr((AndroidResourceParser.ExprContext) ctx.getChild(1));
        }

        if (ctx.getChildCount() == 2 && "!".equals(ctx.getChild(0).getText())) {
            BoolExpr b = buildBoolExpr((AndroidResourceParser.ExprContext) ctx.getChild(1));
            return (b == null) ? null : z3.mkNot(b);
        }

        if (ctx.getChildCount() == 3) {
            String op = ctx.getChild(1).getText();

            if ("&&".equals(op) || "||".equals(op)) {
                BoolExpr l = buildBoolExpr((AndroidResourceParser.ExprContext) ctx.getChild(0));
                BoolExpr r = buildBoolExpr((AndroidResourceParser.ExprContext) ctx.getChild(2));
                if (l == null || r == null) return null;
                return "&&".equals(op) ? z3.mkAnd(l, r) : z3.mkOr(l, r);
            }

            if (isRelOp(op)) {
                BuildResult lb = buildBV((AndroidResourceParser.ExprContext) ctx.getChild(0), 32);
                BuildResult rb = buildBV((AndroidResourceParser.ExprContext) ctx.getChild(2), 32);
                if (lb == null || rb == null || lb.bv == null || rb.bv == null) return null;

                int bw = Math.max(lb.bitWidth, rb.bitWidth);
                BitVecExpr L = ensureWidth(lb.bv, lb.bitWidth, bw);
                BitVecExpr R = ensureWidth(rb.bv, rb.bitWidth, bw);

                switch (op) {
                    case "==": return z3.mkEq(L, R);
                    case "!=": return z3.mkNot(z3.mkEq(L, R));
                    case "<":  return z3.mkBVULT(L, R);
                    case "<=": return z3.mkBVULE(L, R);
                    case ">":  return z3.mkBVUGT(L, R);
                    case ">=": return z3.mkBVUGE(L, R);
                }
            }
        }

        BuildResult fb = buildBV(ctx, 32);
        if (fb != null && fb.bv != null) {
            BitVecExpr zero = z3.mkBV(0, fb.bitWidth);
            return z3.mkNot(z3.mkEq(fb.bv, zero));
        }
        return null;
    }

    // ========== Fallback scan: extract char/wchar_t arrays from raw for-block text ==========

    private void scanAndSolveArraysInRawForBlock(String rawForText) {
        if (rawForText == null || rawForText.isEmpty()) return;

        Map<String, Integer> allLocalVarWidths = new HashMap<>();
        registerLocalTypeDecls(rawForText, "uchar", 8, allLocalVarWidths);
        registerLocalTypeDecls(rawForText, "char", 8, allLocalVarWidths);
        registerLocalTypeDecls(rawForText, "ushort", 16, allLocalVarWidths);
        registerLocalTypeDecls(rawForText, "wchar_t", 16, allLocalVarWidths);
        registerLocalTypeDecls(rawForText, "uint", 32, allLocalVarWidths);
        registerLocalTypeDecls(rawForText, "int", 32, allLocalVarWidths);
        registerLocalTypeDecls(rawForText, "long", 32, allLocalVarWidths);

        Pattern arrPat = Pattern.compile("(wchar_t|char)\\s*([A-Za-z_][A-Za-z0-9_]*)\\s*\\[([^\\]]+)\\]",
                Pattern.DOTALL);
        Matcher m = arrPat.matcher(rawForText);
        while (m.find()) {
            String t = m.group(1);
            String varName = m.group(2);
            String exprStr = m.group(3);
            int arrPos = m.start();

            Map<String, Integer> localVarWidthsUpTo = buildLocalWidthsUpTo(rawForText, arrPos);

            String enc = "wchar_t".equals(t) ? "UTF-16" : "UTF-8";
            System.out.printf("Fallback scan: detected %s array %s[%s]\n", t, varName, exprStr);

            BuildResult brCap = buildBVFromString(exprStr, 32, localVarWidthsUpTo);
            if (brCap != null && brCap.bv != null) {
                System.out.println("    Capacity Z3 expression: " + brCap.bv);
            } else {
                System.out.println("    Capacity Z3 expression: <unable to parse expression>");
            }

            String lenExprStr = tryStripPlusOne(exprStr);
            BuildResult brLen = null;
            if (lenExprStr != null) {
                brLen = buildBVFromString(lenExprStr, 32, localVarWidthsUpTo);
            }
            if (brLen == null && brCap != null && brCap.bv != null) {
                brLen = capMinusOne(brCap);
            }
            if (brLen == null || brLen.bv == null) {
                System.out.println("    lenBV Z3 expression: <unable to parse expression>");
                continue;
            }
            System.out.println("    lenBV Z3 expression: " + brLen.bv);

            List<BoolExpr> domCs = extractDominatingConstraints(rawForText, arrPos, localVarWidthsUpTo);

            MaxResult mr = maximizeUnsignedBVFromBuild(brLen, domCs);
            if (mr == null || mr.modelValue == null) {
                System.out.println("    Prediction failed or no upper bound (unsupported expression/insufficient constraints)");
                continue;
            }

            System.out.printf("    Max string length: %s (0x%s)\n", mr.modelValue, mr.modelValue.toString(16));
            if ("UTF-16".equals(enc)) utf16MaxLen = max(utf16MaxLen, mr.modelValue);
            else utf8MaxLen = max(utf8MaxLen, mr.modelValue);

            if (brCap != null && brCap.bv != null) {
                BigInteger capMax = mr.modelValue.add(BigInteger.ONE);
                System.out.printf("    Max capacity: %s (0x%s)\n", capMax, capMax.toString(16));
            }

            printModelForUsedVars(mr.model, brLen.usedVars);
        }
    }

    private Map<String, Integer> buildLocalWidthsUpTo(String raw, int pos) {
        String head = (pos <= 0) ? "" : raw.substring(0, Math.min(pos, raw.length()));
        Map<String, Integer> m = new HashMap<>();
        registerLocalTypeDecls(head, "uchar", 8, m);
        registerLocalTypeDecls(head, "char", 8, m);
        registerLocalTypeDecls(head, "ushort", 16, m);
        registerLocalTypeDecls(head, "wchar_t", 16, m);
        registerLocalTypeDecls(head, "uint", 32, m);
        registerLocalTypeDecls(head, "int", 32, m);
        registerLocalTypeDecls(head, "long", 32, m);
        return m;
    }

    private String tryStripPlusOne(String expr) {
        if (expr == null) return null;
        String s = stripOuterParens(expr.trim());
        Matcher m1 = Pattern.compile("^(.*)\\+\\s*1\\s*$", Pattern.DOTALL).matcher(s);
        if (m1.find()) {
            String left = m1.group(1).trim();
            if (!left.isEmpty()) return stripOuterParens(left);
        }
        Matcher m2 = Pattern.compile("^1\\s*\\+\\s*(.*)$", Pattern.DOTALL).matcher(s);
        if (m2.find()) {
            String right = m2.group(1).trim();
            if (!right.isEmpty()) return stripOuterParens(right);
        }
        return null;
    }

    private String stripOuterParens(String s) {
        if (s == null) return null;
        s = s.trim();
        while (s.startsWith("(") && s.endsWith(")")) {
            int depth = 0;
            boolean ok = true;
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c == '(') depth++;
                else if (c == ')') {
                    depth--;
                    if (depth == 0 && i != s.length() - 1) { ok = false; break; }
                }
                if (depth < 0) { ok = false; break; }
            }
            if (ok && depth == 0) {
                s = s.substring(1, s.length() - 1).trim();
            } else break;
        }
        return s;
    }

    private BuildResult capMinusOne(BuildResult brCap) {
        BitVecExpr one = z3.mkBV(1, brCap.bitWidth);
        BitVecExpr lenBV = z3.mkBVSub(brCap.bv, one);
        return BuildResult.wrap(lenBV, brCap.usedVars, brCap.bitWidth);
    }

    private void registerLocalTypeDecls(String text, String type, int bitWidth, Map<String, Integer> out) {
        String pat = "\\b" + Pattern.quote(type) + "\\s*([A-Za-z_][A-Za-z0-9_]*)\\b";
        Matcher m = Pattern.compile(pat).matcher(text);
        while (m.find()) {
            String var = m.group(1);
            out.put(var, bitWidth);
        }
    }

    // ========== Maximize (based on BuildResult) ==========

    private MaxResult maximizeUnsignedBVFromBuild(BuildResult br) {
        return maximizeUnsignedBVFromBuild(br, null);
    }

    private MaxResult maximizeUnsignedBVFromBuild(BuildResult br, List<BoolExpr> extraConstraints) {
        Optimize opt = z3.mkOptimize();
        try {
            if (br == null || br.bv == null) return null;

            for (BoolExpr be : pcStack.peek()) opt.Add(be);

            if (extraConstraints != null) {
                for (BoolExpr be : extraConstraints) opt.Add(be);
            }

            for (Map.Entry<String, Integer> e : br.usedVars.entrySet()) {
                String var = e.getKey();
                int bw = e.getValue();
                BitVecExpr v = z3.mkBVConst(var, bw);
                BigInteger max = BigInteger.ONE.shiftLeft(bw).subtract(BigInteger.ONE);
                opt.Add(z3.mkBVULE(z3.mkBV(0, bw), v));
                opt.Add(z3.mkBVULE(v, z3.mkBV(max.toString(), bw)));
            }

            IntExpr obj = (IntExpr) z3.mkBV2Int(br.bv, false);
            opt.MkMaximize(obj);

            if (opt.Check() != Status.SATISFIABLE) return new MaxResult(null, null);
            Model m = opt.getModel();
            Expr iv = m.evaluate(obj, true);
            if (iv.isIntNum()) return new MaxResult(((IntNum) iv).getBigInteger(), m);
            return new MaxResult(null, m);
        } catch (Z3Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private List<BoolExpr> extractDominatingConstraints(String raw, int targetPos, Map<String, Integer> extraVarWidths) {
        List<BoolExpr> out = new ArrayList<>();
        if (raw == null || raw.isEmpty()) return out;

        int i = 0;
        while (true) {
            int k = raw.indexOf("if(", i);
            if (k < 0) break;
            int lp = k + 2;
            int rp = findMatchingParen(raw, lp);
            if (rp < 0) { i = k + 2; continue; }

            String condStr = raw.substring(lp + 1, rp).trim();

            int j = rp + 1;
            while (j < raw.length() && Character.isWhitespace(raw.charAt(j))) j++;
            if (j >= raw.length()) { i = rp + 1; continue; }

            if (raw.charAt(j) == '{') {
                int rb = findMatchingBrace(raw, j);
                if (rb < 0) { i = j + 1; continue; }

                if (targetPos >= j && targetPos <= rb) {
                    BoolExpr be = buildBoolFromString(condStr, extraVarWidths);
                    if (be != null) out.add(be);
                } else {
                    int p = rb + 1;
                    while (p < raw.length() && Character.isWhitespace(raw.charAt(p))) p++;
                    if (p < raw.length() && raw.startsWith("else", p)) {
                        int q = p + 4;
                        while (q < raw.length() && Character.isWhitespace(raw.charAt(q))) q++;

                        if (q < raw.length() && raw.startsWith("if(", q)) {
                            int lp2 = q + 2;
                            int rp2 = findMatchingParen(raw, lp2);
                            if (rp2 > lp2) {
                                String cond2 = raw.substring(lp2 + 1, rp2).trim();
                                int b2 = rp2 + 1;
                                while (b2 < raw.length() && Character.isWhitespace(raw.charAt(b2))) b2++;
                                if (b2 < raw.length() && raw.charAt(b2) == '{') {
                                    int eb2 = findMatchingBrace(raw, b2);
                                    if (eb2 > b2 && targetPos >= b2 && targetPos <= eb2) {
                                        BoolExpr c1 = buildBoolFromString(condStr, extraVarWidths);
                                        BoolExpr c2 = buildBoolFromString(cond2, extraVarWidths);
                                        if (c1 != null) out.add(z3.mkNot(c1));
                                        if (c2 != null) out.add(c2);
                                    }
                                    i = eb2 + 1;
                                    continue;
                                }
                            }
                        }
                        if (q < raw.length() && raw.charAt(q) == '{') {
                            int eb = findMatchingBrace(raw, q);
                            if (eb > q && targetPos >= q && targetPos <= eb) {
                                BoolExpr be = buildBoolFromString(condStr, extraVarWidths);
                                if (be != null) out.add(z3.mkNot(be));
                            }
                            i = eb + 1;
                            continue;
                        }
                    }
                }
                i = rb + 1;
            } else {
                i = rp + 1;
            }
        }

        return out;
    }

    private int findMatchingParen(String s, int pos) {
        if (pos >= s.length() || s.charAt(pos) != '(') return -1;
        int depth = 0;
        for (int i = pos; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') depth++;
            else if (c == ')') {
                depth--;
                if (depth == 0) return i;
            }
        }
        return -1;
    }

    private int findMatchingBrace(String s, int pos) {
        if (pos >= s.length() || s.charAt(pos) != '{') return -1;
        int depth = 0;
        for (int i = pos; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '{') depth++;
            else if (c == '}') {
                depth--;
                if (depth == 0) return i;
            }
        }
        return -1;
    }

    private void printModelForUsedVars(Model m, Map<String, Integer> usedVars) {
        if (m == null || usedVars == null || usedVars.isEmpty()) return;
        System.out.println("    Model variable values:");
        for (Map.Entry<String, Integer> e : usedVars.entrySet()) {
            String var = e.getKey();
            int bw = e.getValue();
            BitVecExpr v = z3.mkBVConst(var, bw);
            Expr val = m.evaluate(v, true);
            if (val instanceof BitVecNum) {
                BigInteger bi = ((BitVecNum) val).getBigInteger();
                BigInteger unsigned = toUnsigned(bi, bw);
                System.out.printf("      %s = %s (0x%s)\n", var, unsigned, unsigned.toString(16));
            } else if (val != null) {
                System.out.printf("      %s = %s\n", var, val.toString());
            }
        }
    }

    private static BigInteger toUnsigned(BigInteger v, int bw) {
        if (v.signum() >= 0) return v;
        return v.add(BigInteger.ONE.shiftLeft(bw));
    }

    // ========= String expression -> Z3 BitVec (for fallback scan) =========

    private BuildResult buildBVFromString(String expr, int defaultBitWidth, Map<String, Integer> extraVarWidths) {
        if (expr == null) return null;

        List<Token> tokens = tokenize(expr);
        if (tokens.isEmpty()) return null;

        tokens = markUnary(tokens);

        List<Token> rpn = toRPN(tokens);
        if (rpn == null || rpn.isEmpty()) return null;

        Deque<BuildResult> stack = new ArrayDeque<>();
        for (Token t : rpn) {
            switch (t.type) {
                case NUMBER: {
                    BigInteger bi = parseNumber(t.text);
                    BitVecNum bv = z3.mkBV(bi.toString(), defaultBitWidth);
                    stack.push(BuildResult.constBV(z3, bv, defaultBitWidth));
                    break;
                }
                case IDENT: {
                    String name = t.text;
                    int bw = (extraVarWidths != null && extraVarWidths.containsKey(name))
                            ? extraVarWidths.get(name) : lookupVarBitWidth(name, defaultBitWidth);
                    stack.push(BuildResult.varBV(z3, name, bw));
                    break;
                }
                case OP: {
                    if ("u+".equals(t.text)) {
                        // no-op
                    } else if ("u-".equals(t.text)) {
                        BuildResult a = stack.pop();
                        stack.push(BuildResult.wrap(z3.mkBVNeg(a.bv), a.usedVars, a.bitWidth));
                    } else if (isBinaryOp(t.text)) {
                        BuildResult b = stack.pop();
                        BuildResult a = stack.pop();
                        int bw = Math.max(a.bitWidth, b.bitWidth);
                        BitVecExpr A = ensureWidth(a.bv, a.bitWidth, bw);
                        BitVecExpr B = ensureWidth(b.bv, b.bitWidth, bw);
                        Map<String, Integer> used = mergeUsed(a.usedVars, b.usedVars);

                        switch (t.text) {
                            case "+":  stack.push(BuildResult.wrap(z3.mkBVAdd(A, B), used, bw)); break;
                            case "-":  stack.push(BuildResult.wrap(z3.mkBVSub(A, B), used, bw)); break;
                            case "*":  stack.push(BuildResult.wrap(z3.mkBVMul(A, B), used, bw)); break;
                            case "/":  stack.push(BuildResult.wrap(z3.mkBVUDiv(A, B), used, bw)); break;
                            case "%":  stack.push(BuildResult.wrap(z3.mkBVURem(A, B), used, bw)); break;
                            case "<<": stack.push(BuildResult.wrap(z3.mkBVSHL(A, B), used, bw)); break;
                            case ">>": stack.push(BuildResult.wrap(z3.mkBVLSHR(A, B), used, bw)); break;
                            case "&":  stack.push(BuildResult.wrap(z3.mkBVAND(A, B), used, bw)); break;
                            case "|":  stack.push(BuildResult.wrap(z3.mkBVOR(A, B), used, bw)); break;
                            default: return null;
                        }
                    } else {
                        return null;
                    }
                    break;
                }
                default:
                    return null;
            }
        }
        return stack.isEmpty() ? null : stack.pop();
    }

    // ========== Tokenizer/parser utilities (for fallback string expressions) ==========

    private enum TokType { NUMBER, IDENT, OP, LPAREN, RPAREN }

    private static class Token {
        final TokType type;
        final String text;
        Token(TokType type, String text) { this.type = type; this.text = text; }
        public String toString() { return type + ":" + text; }
    }

    private List<Token> tokenize(String s) {
        List<Token> out = new ArrayList<>();
        int n = s.length();
        int i = 0;
        while (i < n) {
            char c = s.charAt(i);

            if (Character.isWhitespace(c)) { i++; continue; }

            if (Character.isDigit(c)) {
                int j = i;
                if (j + 1 < n && s.charAt(j) == '0' && (s.charAt(j + 1) == 'x' || s.charAt(j + 1) == 'X')) {
                    j += 2;
                    while (j < n && isHexDigit(s.charAt(j))) j++;
                } else {
                    while (j < n && Character.isDigit(s.charAt(j))) j++;
                }
                out.add(new Token(TokType.NUMBER, s.substring(i, j)));
                i = j;
                continue;
            }

            if (Character.isLetter(c) || c == '_') {
                int j = i + 1;
                while (j < n) {
                    char cj = s.charAt(j);
                    if (Character.isLetterOrDigit(cj) || cj == '_') j++; else break;
                }
                out.add(new Token(TokType.IDENT, s.substring(i, j)));
                i = j;
                continue;
            }

            if (i + 1 < n) {
                String two = s.substring(i, i + 2);
                if ("<<".equals(two) || ">>".equals(two)) {
                    out.add(new Token(TokType.OP, two));
                    i += 2;
                    continue;
                }
            }

            switch (c) {
                case '+': case '-': case '*': case '/': case '%':
                case '&': case '|':
                    out.add(new Token(TokType.OP, String.valueOf(c)));
                    i++; break;
                case '(':
                    out.add(new Token(TokType.LPAREN, "("));
                    i++; break;
                case ')':
                    out.add(new Token(TokType.RPAREN, ")"));
                    i++; break;
                default:
                    i++; break;
            }
        }
        return out;
    }

    private boolean isHexDigit(char c) {
        return (c >= '0' && c <= '9')
                || (c >= 'a' && c <= 'f')
                || (c >= 'A' && c <= 'F');
    }

    private List<Token> markUnary(List<Token> in) {
        List<Token> out = new ArrayList<>();
        Token prev = null;
        for (Token t : in) {
            if (t.type == TokType.OP && ("+".equals(t.text) || "-".equals(t.text))) {
                boolean unary = (prev == null)
                        || prev.type == TokType.OP
                        || prev.type == TokType.LPAREN;
                if (unary) {
                    out.add(new Token(TokType.OP, ("+".equals(t.text) ? "u+" : "u-")));
                } else {
                    out.add(t);
                }
            } else {
                out.add(t);
            }
            if (t.type != TokType.OP || (!"u+".equals(t.text) && !"u-".equals(t.text))) {
                prev = t;
            }
        }
        return out;
    }

    private List<Token> toRPN(List<Token> tokens) {
        List<Token> out = new ArrayList<>();
        Deque<Token> ops = new ArrayDeque<>();
        for (Token t : tokens) {
            switch (t.type) {
                case NUMBER:
                case IDENT:
                    out.add(t);
                    break;
                case OP:
                    if ("u+".equals(t.text) || "u-".equals(t.text)) {
                        ops.push(t);
                        break;
                    }
                    while (!ops.isEmpty() && ops.peek().type == TokType.OP
                            && prec(ops.peek()) >= prec(t)) {
                        out.add(ops.pop());
                    }
                    ops.push(t);
                    break;
                case LPAREN:
                    ops.push(t);
                    break;
                case RPAREN:
                    while (!ops.isEmpty() && ops.peek().type != TokType.LPAREN) {
                        out.add(ops.pop());
                    }
                    if (ops.isEmpty() || ops.peek().type != TokType.LPAREN) return null;
                    ops.pop();
                    while (!ops.isEmpty() && ops.peek().type == TokType.OP
                            && ("u+".equals(ops.peek().text) || "u-".equals(ops.peek().text))) {
                        out.add(ops.pop());
                    }
                    break;
            }
        }
        while (!ops.isEmpty()) {
            if (ops.peek().type == TokType.LPAREN) return null;
            out.add(ops.pop());
        }
        return out;
    }

    private boolean isBinaryOp(String op) {
        return "+-*/%<<>>&|".contains(op);
    }

    private int prec(Token t) {
        String op = t.text;
        if ("u+".equals(op) || "u-".equals(op)) return 6;
        if ("*".equals(op) || "/".equals(op) || "%".equals(op)) return 5;
        if ("+".equals(op) || "-".equals(op)) return 4;
        if ("<<".equals(op) || ">>".equals(op)) return 3;
        if ("&".equals(op)) return 2;
        if ("|".equals(op)) return 1;
        return 0;
    }

    private BigInteger parseNumber(String tok) {
        if (tok.startsWith("0x") || tok.startsWith("0X")) return new BigInteger(tok.substring(2), 16);
        return new BigInteger(tok);
    }

    // ========= Generic Bool parsing (string): if/while condition -> Z3 BoolExpr ==========

    private enum BTokType { NUMBER, IDENT, OP, LPAREN, RPAREN }

    private static class BToken {
        final BTokType type;
        final String text;
        BToken(BTokType type, String text) { this.type = type; this.text = text; }
    }

    static class Val {
        BitVecExpr bv; int bw; BoolExpr bb; Map<String,Integer> used = new HashMap<>();
        boolean isBool() { return bb != null; }
        static Val ofBV(BitVecExpr e, int bw, Map<String,Integer> u) {
            Val v = new Val();
            v.bv = e;
            v.bw = bw;
            if (u != null) v.used.putAll(u);
            return v;
        }
        static Val ofBool(BoolExpr b, Map<String,Integer> u) {
            Val v = new Val();
            v.bb = b;
            if (u != null) v.used.putAll(u);
            return v;
        }
    }

    private BoolExpr buildBoolFromString(String cond, Map<String, Integer> extraVarWidths) {
        if (cond == null || cond.isEmpty()) return null;
        List<BToken> tokens = tokenizeBool(cond);
        if (tokens.isEmpty()) return null;
        List<BToken> rpn = toRPNBool(tokens);
        if (rpn == null || rpn.isEmpty()) return null;

        Deque<Val> st = new ArrayDeque<>();
        for (BToken t : rpn) {
            switch (t.type) {
                case NUMBER: {
                    BigInteger bi = parseNumber(t.text);
                    BitVecNum bv = z3.mkBV(bi.toString(), 32);
                    st.push(Val.ofBV(bv, 32, null));
                    break;
                }
                case IDENT: {
                    String name = t.text;
                    int bw = (extraVarWidths != null && extraVarWidths.containsKey(name))
                            ? extraVarWidths.get(name) : lookupVarBitWidth(name, 32);
                    st.push(Val.ofBV(z3.mkBVConst(name, bw), bw, Collections.singletonMap(name, bw)));
                    break;
                }
                case OP: {
                    String op = t.text;
                    if ("u+".equals(op) || "u-".equals(op) || "!".equals(op)) {
                        Val a = st.pop();
                        if ("!".equals(op)) {
                            BoolExpr base = a.isBool() ? a.bb : z3.mkNot(z3.mkEq(a.bv, z3.mkBV(0, a.bw)));
                            st.push(Val.ofBool(z3.mkNot(base), a.used));
                        } else {
                            if (a.isBool()) return null;
                            BitVecExpr res = "u-".equals(op) ? z3.mkBVNeg(a.bv) : a.bv;
                            st.push(Val.ofBV(res, a.bw, a.used));
                        }
                        break;
                    }
                    if ("&&".equals(op) || "||".equals(op)) {
                        Val b = st.pop(), a = st.pop();
                        BoolExpr A = a.isBool() ? a.bb : z3.mkNot(z3.mkEq(a.bv, z3.mkBV(0, a.bw)));
                        BoolExpr B = b.isBool() ? b.bb : z3.mkNot(z3.mkEq(b.bv, z3.mkBV(0, b.bw)));
                        BoolExpr r = "&&".equals(op) ? z3.mkAnd(A, B) : z3.mkOr(A, B);
                        st.push(Val.ofBool(r, mergeUsed(a.used, b.used)));
                        break;
                    }
                    if (isRelOp(op) || "==".equals(op) || "!=".equals(op)) {
                        Val b = st.pop(), a = st.pop();
                        if (a.isBool() || b.isBool()) return null;
                        int bw = Math.max(a.bw, b.bw);
                        BitVecExpr A = ensureWidth(a.bv, a.bw, bw), B = ensureWidth(b.bv, b.bw, bw);
                        BoolExpr r;
                        switch (op) {
                            case "==": r = z3.mkEq(A, B); break;
                            case "!=": r = z3.mkNot(z3.mkEq(A, B)); break;
                            case "<":  r = z3.mkBVULT(A, B); break;
                            case "<=": r = z3.mkBVULE(A, B); break;
                            case ">":  r = z3.mkBVUGT(A, B); break;
                            case ">=": r = z3.mkBVUGE(A, B); break;
                            default: return null;
                        }
                        st.push(Val.ofBool(r, mergeUsed(a.used, b.used)));
                        break;
                    }
                    {
                        Val b = st.pop(), a = st.pop();
                        if (a.isBool() || b.isBool()) return null;
                        int bw = Math.max(a.bw, b.bw);
                        BitVecExpr A = ensureWidth(a.bv, a.bw, bw), B = ensureWidth(b.bv, b.bw, bw);
                        Map<String,Integer> used = mergeUsed(a.used, b.used);
                        BitVecExpr r;
                        switch (op) {
                            case "+":  r = z3.mkBVAdd(A, B); break;
                            case "-":  r = z3.mkBVSub(A, B); break;
                            case "*":  r = z3.mkBVMul(A, B); break;
                            case "/":  r = z3.mkBVUDiv(A, B); break;
                            case "%":  r = z3.mkBVURem(A, B); break;
                            case "<<": r = z3.mkBVSHL(A, B); break;
                            case ">>": r = z3.mkBVLSHR(A, B); break;
                            case "&":  r = z3.mkBVAND(A, B); break;
                            case "|":  r = z3.mkBVOR(A, B); break;
                            default: return null;
                        }
                        st.push(Val.ofBV(r, bw, used));
                    }
                    break;
                }
                default: return null;
            }
        }

        if (st.isEmpty()) return null;
        Val last = st.pop();
        return last.isBool() ? last.bb : z3.mkNot(z3.mkEq(last.bv, z3.mkBV(0, last.bw)));
    }

    private List<BToken> tokenizeBool(String s) {
        List<BToken> out = new ArrayList<>();
        int n = s.length(), i = 0;
        while (i < n) {
            char c = s.charAt(i);
            if (Character.isWhitespace(c)) { i++; continue; }

            if (Character.isDigit(c)) {
                int j = i;
                if (j + 1 < n && s.charAt(j) == '0' && (s.charAt(j+1)=='x'||s.charAt(j+1)=='X')) {
                    j += 2; while (j < n && isHexDigit(s.charAt(j))) j++;
                } else {
                    while (j < n && Character.isDigit(s.charAt(j))) j++;
                }
                out.add(new BToken(BTokType.NUMBER, s.substring(i, j)));
                i = j; continue;
            }

            if (Character.isLetter(c) || c == '_') {
                int j = i+1;
                while (j < n) {
                    char cj = s.charAt(j);
                    if (Character.isLetterOrDigit(cj) || cj == '_') j++; else break;
                }
                out.add(new BToken(BTokType.IDENT, s.substring(i, j)));
                i = j; continue;
            }

            if (i + 1 < n) {
                String two = s.substring(i, i+2);
                if (two.equals("<<") || two.equals(">>") || two.equals("&&") || two.equals("||")
                        || two.equals("==") || two.equals("!=") || two.equals("<=") || two.equals(">=")) {
                    out.add(new BToken(BTokType.OP, two));
                    i += 2; continue;
                }
            }

            switch (c) {
                case '+': case '-': case '*': case '/': case '%':
                case '&': case '|': case '!': case '<': case '>':
                    out.add(new BToken(BTokType.OP, String.valueOf(c))); i++; break;
                case '(':
                    out.add(new BToken(BTokType.LPAREN, "(")); i++; break;
                case ')':
                    out.add(new BToken(BTokType.RPAREN, ")")); i++; break;
                default:
                    i++;
            }
        }

        List<BToken> ret = new ArrayList<>();
        BToken prev = null;
        for (BToken t : out) {
            if (t.type == BTokType.OP && (t.text.equals("+") || t.text.equals("-"))) {
                boolean unary = (prev == null) || prev.type == BTokType.OP || prev.type == BTokType.LPAREN;
                ret.add(new BToken(BTokType.OP, unary ? ("u" + t.text) : t.text));
            } else {
                ret.add(t);
            }
            if (!(t.type == BTokType.OP && (t.text.equals("u+") || t.text.equals("u-")))) prev = t;
        }
        return ret;
    }

    private boolean isRelOp(String op) {
        return "==".equals(op) || "!=".equals(op) || "<".equals(op) || "<=".equals(op) || ">".equals(op) || ">=".equals(op);
    }

    private int precBool(String op) {
        if ("u+".equals(op) || "u-".equals(op) || "!".equals(op)) return 8;
        if ("*".equals(op) || "/".equals(op) || "%".equals(op)) return 7;
        if ("+".equals(op) || "-".equals(op)) return 6;
        if ("<<".equals(op) || ">>".equals(op)) return 5;
        if ("&".equals(op)) return 4;
        if ("|".equals(op)) return 3;
        if ("<".equals(op) || "<=".equals(op) || ">".equals(op) || ">=".equals(op)) return 2;
        if ("==".equals(op) || "!=".equals(op)) return 1;
        if ("&&".equals(op)) return 0;
        if ("||".equals(op)) return -1;
        return -2;
    }

    private List<BToken> toRPNBool(List<BToken> in) {
        List<BToken> out = new ArrayList<>();
        Deque<BToken> ops = new ArrayDeque<>();
        for (BToken t : in) {
            switch (t.type) {
                case NUMBER:
                case IDENT:
                    out.add(t); break;
                case OP: {
                    while (!ops.isEmpty() && ops.peek().type == BTokType.OP
                            && precBool(ops.peek().text) >= precBool(t.text)) {
                        out.add(ops.pop());
                    }
                    ops.push(t);
                    break;
                }
                case LPAREN: ops.push(t); break;
                case RPAREN:
                    while (!ops.isEmpty() && ops.peek().type != BTokType.LPAREN) out.add(ops.pop());
                    if (ops.isEmpty() || ops.peek().type != BTokType.LPAREN) return null;
                    ops.pop();
                    while (!ops.isEmpty() && ops.peek().type == BTokType.OP
                            && (ops.peek().text.equals("u+") || ops.peek().text.equals("u-") || ops.peek().text.equals("!"))) {
                        out.add(ops.pop());
                    }
                    break;
            }
        }
        while (!ops.isEmpty()) {
            if (ops.peek().type == BTokType.LPAREN) return null;
            out.add(ops.pop());
        }
        return out;
    }

    // ========= Common utilities =========

    private static class MaxResult {
        final BigInteger modelValue;
        final Model model;
        MaxResult(BigInteger mv, Model m) {
            this.modelValue = mv;
            this.model = m;
        }
    }

    private static class BuildResult {
        final BitVecExpr bv;
        final Map<String, Integer> usedVars;
        final int bitWidth;

        private BuildResult(BitVecExpr bv, Map<String, Integer> usedVars, int bitWidth) {
            this.bv = bv;
            this.usedVars = usedVars;
            this.bitWidth = bitWidth;
        }

        static BuildResult wrap(BitVecExpr e, Map<String, Integer> used, int bw) {
            return new BuildResult(e, used, bw);
        }

        static BuildResult varBV(com.microsoft.z3.Context z3, String name, int bw) {
            Map<String, Integer> m = new HashMap<>();
            m.put(name, bw);
            return new BuildResult(z3.mkBVConst(name, bw), m, bw);
        }

        static BuildResult constBV(com.microsoft.z3.Context z3, BitVecNum c, int bw) {
            return new BuildResult(c, new HashMap<>(), bw);
        }
    }

    private int lookupVarBitWidth(String name, int defaultBitWidth) {
        for (Iterator<Map<String, Integer>> it = scopeStack.descendingIterator(); it.hasNext(); ) {
            Map<String, Integer> m = it.next();
            if (m.containsKey(name)) return m.get(name);
        }
        return defaultBitWidth;
    }

    private void declareVar(String name, Integer bitWidth) {
        if (bitWidth == null) bitWidth = 32;
        scopeStack.peek().put(name, bitWidth);
    }

    private Integer inferBitWidthFromType(String t) {
        String s = t.trim().toLowerCase(Locale.ROOT);
        if (s.equals("uchar") || s.equals("uint8") || s.equals("byte") || s.equals("char")) return 8;
        if (s.equals("ushort") || s.equals("uint16") || s.equals("wchar_t")) return 16;
        if (s.equals("uint") || s.equals("int") || s.equals("uint32") || s.equals("long")) return 32;
        return 32;
    }

    private boolean isNumberToken(String t) {
        if (t == null || t.isEmpty()) return false;
        if (t.startsWith("0x") || t.startsWith("0X")) return true;
        for (int i = 0; i < t.length(); i++) {
            char c = t.charAt(i);
            if (i == 0 && (c == '+' || c == '-')) continue;
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    private BitVecNum parseBVConst(String token, int bitWidth) {
        if (token.startsWith("0x") || token.startsWith("0X")) {
            BigInteger bi = new BigInteger(token.substring(2), 16);
            BigInteger mask = BigInteger.ONE.shiftLeft(bitWidth).subtract(BigInteger.ONE);
            bi = bi.and(mask);
            return z3.mkBV(bi.toString(), bitWidth);
        }
        BigInteger bi = new BigInteger(token);
        BigInteger mask = BigInteger.ONE.shiftLeft(bitWidth).subtract(BigInteger.ONE);
        bi = bi.and(mask);
        return z3.mkBV(bi.toString(), bitWidth);
    }

    private BitVecExpr ensureWidth(BitVecExpr e, int curr, int target) {
        if (curr == target) return e;
        if (curr < target) return z3.mkZeroExt(target - curr, e);
        return z3.mkExtract(target - 1, 0, e);
    }

    private Map<String, Integer> mergeUsed(Map<String, Integer> a, Map<String, Integer> b) {
        Map<String, Integer> m = new HashMap<>(a);
        for (Map.Entry<String, Integer> e : b.entrySet()) {
            m.merge(e.getKey(), e.getValue(), Math::max);
        }
        return m;
    }

    private String classifyEncoding(String typeText) {
        String s = typeText.trim().toLowerCase(Locale.ROOT);
        if (s.equals("wchar_t")) return "UTF-16";
        if (s.equals("char")) return "UTF-8";
        return null;
    }

    private static BigInteger max(BigInteger a, BigInteger b) {
        if (a == null) return b;
        if (b == null) return a;
        return a.max(b);
    }
}