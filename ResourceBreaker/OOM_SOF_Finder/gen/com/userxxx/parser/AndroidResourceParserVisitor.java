// Generated from /Users/userxxx/J010Parser/src/main/java/com/userxxx/parser/AndroidResourceParser.g4 by ANTLR 4.13.2
package com.userxxx.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link AndroidResourceParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface AndroidResourceParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#file}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFile(AndroidResourceParser.FileContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(AndroidResourceParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#breakStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakStmt(AndroidResourceParser.BreakStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#whileStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStmt(AndroidResourceParser.WhileStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#switchStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchStmt(AndroidResourceParser.SwitchStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#switchBlockStatementGroup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchBlockStatementGroup(AndroidResourceParser.SwitchBlockStatementGroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#switchLabel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchLabel(AndroidResourceParser.SwitchLabelContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#forStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStmt(AndroidResourceParser.ForStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#exprStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprStmt(AndroidResourceParser.ExprStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#ifStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStmt(AndroidResourceParser.IfStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#statementOrBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementOrBlock(AndroidResourceParser.StatementOrBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(AndroidResourceParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#returnStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStmt(AndroidResourceParser.ReturnStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#typedefDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedefDecl(AndroidResourceParser.TypedefDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#enumDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumDecl(AndroidResourceParser.EnumDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#enumItemList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumItemList(AndroidResourceParser.EnumItemListContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#enumItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumItem(AndroidResourceParser.EnumItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#structDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructDecl(AndroidResourceParser.StructDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#structItemList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructItemList(AndroidResourceParser.StructItemListContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#structItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructItem(AndroidResourceParser.StructItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#unionDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnionDecl(AndroidResourceParser.UnionDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#varDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDecl(AndroidResourceParser.VarDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#argList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgList(AndroidResourceParser.ArgListContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#funcCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncCall(AndroidResourceParser.FuncCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(AndroidResourceParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#funcDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncDecl(AndroidResourceParser.FuncDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#funcBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncBody(AndroidResourceParser.FuncBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#paramList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParamList(AndroidResourceParser.ParamListContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam(AndroidResourceParser.ParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#assignStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignStmt(AndroidResourceParser.AssignStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(AndroidResourceParser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#fieldAttributes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldAttributes(AndroidResourceParser.FieldAttributesContext ctx);
	/**
	 * Visit a parse tree produced by {@link AndroidResourceParser#attribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute(AndroidResourceParser.AttributeContext ctx);
}