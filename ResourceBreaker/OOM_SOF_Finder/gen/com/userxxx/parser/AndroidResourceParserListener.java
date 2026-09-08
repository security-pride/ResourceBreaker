// Generated from /Users/userxxx/J010Parser/src/main/java/com/userxxx/parser/AndroidResourceParser.g4 by ANTLR 4.13.2
package com.userxxx.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link AndroidResourceParser}.
 */
public interface AndroidResourceParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#file}.
	 * @param ctx the parse tree
	 */
	void enterFile(AndroidResourceParser.FileContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#file}.
	 * @param ctx the parse tree
	 */
	void exitFile(AndroidResourceParser.FileContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(AndroidResourceParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(AndroidResourceParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#breakStmt}.
	 * @param ctx the parse tree
	 */
	void enterBreakStmt(AndroidResourceParser.BreakStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#breakStmt}.
	 * @param ctx the parse tree
	 */
	void exitBreakStmt(AndroidResourceParser.BreakStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#whileStmt}.
	 * @param ctx the parse tree
	 */
	void enterWhileStmt(AndroidResourceParser.WhileStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#whileStmt}.
	 * @param ctx the parse tree
	 */
	void exitWhileStmt(AndroidResourceParser.WhileStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#switchStmt}.
	 * @param ctx the parse tree
	 */
	void enterSwitchStmt(AndroidResourceParser.SwitchStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#switchStmt}.
	 * @param ctx the parse tree
	 */
	void exitSwitchStmt(AndroidResourceParser.SwitchStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#switchBlockStatementGroup}.
	 * @param ctx the parse tree
	 */
	void enterSwitchBlockStatementGroup(AndroidResourceParser.SwitchBlockStatementGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#switchBlockStatementGroup}.
	 * @param ctx the parse tree
	 */
	void exitSwitchBlockStatementGroup(AndroidResourceParser.SwitchBlockStatementGroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#switchLabel}.
	 * @param ctx the parse tree
	 */
	void enterSwitchLabel(AndroidResourceParser.SwitchLabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#switchLabel}.
	 * @param ctx the parse tree
	 */
	void exitSwitchLabel(AndroidResourceParser.SwitchLabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#forStmt}.
	 * @param ctx the parse tree
	 */
	void enterForStmt(AndroidResourceParser.ForStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#forStmt}.
	 * @param ctx the parse tree
	 */
	void exitForStmt(AndroidResourceParser.ForStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#exprStmt}.
	 * @param ctx the parse tree
	 */
	void enterExprStmt(AndroidResourceParser.ExprStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#exprStmt}.
	 * @param ctx the parse tree
	 */
	void exitExprStmt(AndroidResourceParser.ExprStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#ifStmt}.
	 * @param ctx the parse tree
	 */
	void enterIfStmt(AndroidResourceParser.IfStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#ifStmt}.
	 * @param ctx the parse tree
	 */
	void exitIfStmt(AndroidResourceParser.IfStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#statementOrBlock}.
	 * @param ctx the parse tree
	 */
	void enterStatementOrBlock(AndroidResourceParser.StatementOrBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#statementOrBlock}.
	 * @param ctx the parse tree
	 */
	void exitStatementOrBlock(AndroidResourceParser.StatementOrBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(AndroidResourceParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(AndroidResourceParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#returnStmt}.
	 * @param ctx the parse tree
	 */
	void enterReturnStmt(AndroidResourceParser.ReturnStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#returnStmt}.
	 * @param ctx the parse tree
	 */
	void exitReturnStmt(AndroidResourceParser.ReturnStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#typedefDecl}.
	 * @param ctx the parse tree
	 */
	void enterTypedefDecl(AndroidResourceParser.TypedefDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#typedefDecl}.
	 * @param ctx the parse tree
	 */
	void exitTypedefDecl(AndroidResourceParser.TypedefDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#enumDecl}.
	 * @param ctx the parse tree
	 */
	void enterEnumDecl(AndroidResourceParser.EnumDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#enumDecl}.
	 * @param ctx the parse tree
	 */
	void exitEnumDecl(AndroidResourceParser.EnumDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#enumItemList}.
	 * @param ctx the parse tree
	 */
	void enterEnumItemList(AndroidResourceParser.EnumItemListContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#enumItemList}.
	 * @param ctx the parse tree
	 */
	void exitEnumItemList(AndroidResourceParser.EnumItemListContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#enumItem}.
	 * @param ctx the parse tree
	 */
	void enterEnumItem(AndroidResourceParser.EnumItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#enumItem}.
	 * @param ctx the parse tree
	 */
	void exitEnumItem(AndroidResourceParser.EnumItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#structDecl}.
	 * @param ctx the parse tree
	 */
	void enterStructDecl(AndroidResourceParser.StructDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#structDecl}.
	 * @param ctx the parse tree
	 */
	void exitStructDecl(AndroidResourceParser.StructDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#structItemList}.
	 * @param ctx the parse tree
	 */
	void enterStructItemList(AndroidResourceParser.StructItemListContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#structItemList}.
	 * @param ctx the parse tree
	 */
	void exitStructItemList(AndroidResourceParser.StructItemListContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#structItem}.
	 * @param ctx the parse tree
	 */
	void enterStructItem(AndroidResourceParser.StructItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#structItem}.
	 * @param ctx the parse tree
	 */
	void exitStructItem(AndroidResourceParser.StructItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#unionDecl}.
	 * @param ctx the parse tree
	 */
	void enterUnionDecl(AndroidResourceParser.UnionDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#unionDecl}.
	 * @param ctx the parse tree
	 */
	void exitUnionDecl(AndroidResourceParser.UnionDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#varDecl}.
	 * @param ctx the parse tree
	 */
	void enterVarDecl(AndroidResourceParser.VarDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#varDecl}.
	 * @param ctx the parse tree
	 */
	void exitVarDecl(AndroidResourceParser.VarDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#argList}.
	 * @param ctx the parse tree
	 */
	void enterArgList(AndroidResourceParser.ArgListContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#argList}.
	 * @param ctx the parse tree
	 */
	void exitArgList(AndroidResourceParser.ArgListContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#funcCall}.
	 * @param ctx the parse tree
	 */
	void enterFuncCall(AndroidResourceParser.FuncCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#funcCall}.
	 * @param ctx the parse tree
	 */
	void exitFuncCall(AndroidResourceParser.FuncCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(AndroidResourceParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(AndroidResourceParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#funcDecl}.
	 * @param ctx the parse tree
	 */
	void enterFuncDecl(AndroidResourceParser.FuncDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#funcDecl}.
	 * @param ctx the parse tree
	 */
	void exitFuncDecl(AndroidResourceParser.FuncDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#funcBody}.
	 * @param ctx the parse tree
	 */
	void enterFuncBody(AndroidResourceParser.FuncBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#funcBody}.
	 * @param ctx the parse tree
	 */
	void exitFuncBody(AndroidResourceParser.FuncBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#paramList}.
	 * @param ctx the parse tree
	 */
	void enterParamList(AndroidResourceParser.ParamListContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#paramList}.
	 * @param ctx the parse tree
	 */
	void exitParamList(AndroidResourceParser.ParamListContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#param}.
	 * @param ctx the parse tree
	 */
	void enterParam(AndroidResourceParser.ParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#param}.
	 * @param ctx the parse tree
	 */
	void exitParam(AndroidResourceParser.ParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#assignStmt}.
	 * @param ctx the parse tree
	 */
	void enterAssignStmt(AndroidResourceParser.AssignStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#assignStmt}.
	 * @param ctx the parse tree
	 */
	void exitAssignStmt(AndroidResourceParser.AssignStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(AndroidResourceParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(AndroidResourceParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#fieldAttributes}.
	 * @param ctx the parse tree
	 */
	void enterFieldAttributes(AndroidResourceParser.FieldAttributesContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#fieldAttributes}.
	 * @param ctx the parse tree
	 */
	void exitFieldAttributes(AndroidResourceParser.FieldAttributesContext ctx);
	/**
	 * Enter a parse tree produced by {@link AndroidResourceParser#attribute}.
	 * @param ctx the parse tree
	 */
	void enterAttribute(AndroidResourceParser.AttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link AndroidResourceParser#attribute}.
	 * @param ctx the parse tree
	 */
	void exitAttribute(AndroidResourceParser.AttributeContext ctx);
}