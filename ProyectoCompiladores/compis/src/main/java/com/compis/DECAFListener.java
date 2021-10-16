// Generated from DECAF.g4 by ANTLR 4.9.2
package com.compis;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link DECAFParser}.
 */
public interface DECAFListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link DECAFParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(DECAFParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link DECAFParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(DECAFParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link DECAFParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(DECAFParser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link DECAFParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(DECAFParser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link DECAFParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVarDeclaration(DECAFParser.VarDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link DECAFParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVarDeclaration(DECAFParser.VarDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link DECAFParser#structDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterStructDeclaration(DECAFParser.StructDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link DECAFParser#structDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitStructDeclaration(DECAFParser.StructDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link DECAFParser#varType}.
	 * @param ctx the parse tree
	 */
	void enterVarType(DECAFParser.VarTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DECAFParser#varType}.
	 * @param ctx the parse tree
	 */
	void exitVarType(DECAFParser.VarTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DECAFParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMethodDeclaration(DECAFParser.MethodDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link DECAFParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMethodDeclaration(DECAFParser.MethodDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link DECAFParser#methodType}.
	 * @param ctx the parse tree
	 */
	void enterMethodType(DECAFParser.MethodTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DECAFParser#methodType}.
	 * @param ctx the parse tree
	 */
	void exitMethodType(DECAFParser.MethodTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DECAFParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameter(DECAFParser.ParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link DECAFParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameter(DECAFParser.ParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link DECAFParser#parameterType}.
	 * @param ctx the parse tree
	 */
	void enterParameterType(DECAFParser.ParameterTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DECAFParser#parameterType}.
	 * @param ctx the parse tree
	 */
	void exitParameterType(DECAFParser.ParameterTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DECAFParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(DECAFParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link DECAFParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(DECAFParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link DECAFParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(DECAFParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link DECAFParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(DECAFParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link DECAFParser#location}.
	 * @param ctx the parse tree
	 */
	void enterLocation(DECAFParser.LocationContext ctx);
	/**
	 * Exit a parse tree produced by {@link DECAFParser#location}.
	 * @param ctx the parse tree
	 */
	void exitLocation(DECAFParser.LocationContext ctx);
	/**
	 * Enter a parse tree produced by {@link DECAFParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(DECAFParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DECAFParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(DECAFParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DECAFParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStatement(DECAFParser.ReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link DECAFParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStatement(DECAFParser.ReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link DECAFParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(DECAFParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link DECAFParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(DECAFParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link DECAFParser#elseStatement}.
	 * @param ctx the parse tree
	 */
	void enterElseStatement(DECAFParser.ElseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link DECAFParser#elseStatement}.
	 * @param ctx the parse tree
	 */
	void exitElseStatement(DECAFParser.ElseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link DECAFParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatement(DECAFParser.WhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link DECAFParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatement(DECAFParser.WhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link DECAFParser#methodCall}.
	 * @param ctx the parse tree
	 */
	void enterMethodCall(DECAFParser.MethodCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link DECAFParser#methodCall}.
	 * @param ctx the parse tree
	 */
	void exitMethodCall(DECAFParser.MethodCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link DECAFParser#arg}.
	 * @param ctx the parse tree
	 */
	void enterArg(DECAFParser.ArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link DECAFParser#arg}.
	 * @param ctx the parse tree
	 */
	void exitArg(DECAFParser.ArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link DECAFParser#op}.
	 * @param ctx the parse tree
	 */
	void enterOp(DECAFParser.OpContext ctx);
	/**
	 * Exit a parse tree produced by {@link DECAFParser#op}.
	 * @param ctx the parse tree
	 */
	void exitOp(DECAFParser.OpContext ctx);
	/**
	 * Enter a parse tree produced by {@link DECAFParser#arith_op}.
	 * @param ctx the parse tree
	 */
	void enterArith_op(DECAFParser.Arith_opContext ctx);
	/**
	 * Exit a parse tree produced by {@link DECAFParser#arith_op}.
	 * @param ctx the parse tree
	 */
	void exitArith_op(DECAFParser.Arith_opContext ctx);
	/**
	 * Enter a parse tree produced by {@link DECAFParser#rel_op}.
	 * @param ctx the parse tree
	 */
	void enterRel_op(DECAFParser.Rel_opContext ctx);
	/**
	 * Exit a parse tree produced by {@link DECAFParser#rel_op}.
	 * @param ctx the parse tree
	 */
	void exitRel_op(DECAFParser.Rel_opContext ctx);
	/**
	 * Enter a parse tree produced by {@link DECAFParser#eq_op}.
	 * @param ctx the parse tree
	 */
	void enterEq_op(DECAFParser.Eq_opContext ctx);
	/**
	 * Exit a parse tree produced by {@link DECAFParser#eq_op}.
	 * @param ctx the parse tree
	 */
	void exitEq_op(DECAFParser.Eq_opContext ctx);
	/**
	 * Enter a parse tree produced by {@link DECAFParser#cond_op}.
	 * @param ctx the parse tree
	 */
	void enterCond_op(DECAFParser.Cond_opContext ctx);
	/**
	 * Exit a parse tree produced by {@link DECAFParser#cond_op}.
	 * @param ctx the parse tree
	 */
	void exitCond_op(DECAFParser.Cond_opContext ctx);
	/**
	 * Enter a parse tree produced by {@link DECAFParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(DECAFParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link DECAFParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(DECAFParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link DECAFParser#int_literal}.
	 * @param ctx the parse tree
	 */
	void enterInt_literal(DECAFParser.Int_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link DECAFParser#int_literal}.
	 * @param ctx the parse tree
	 */
	void exitInt_literal(DECAFParser.Int_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link DECAFParser#char_literal}.
	 * @param ctx the parse tree
	 */
	void enterChar_literal(DECAFParser.Char_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link DECAFParser#char_literal}.
	 * @param ctx the parse tree
	 */
	void exitChar_literal(DECAFParser.Char_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link DECAFParser#bool_literal}.
	 * @param ctx the parse tree
	 */
	void enterBool_literal(DECAFParser.Bool_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link DECAFParser#bool_literal}.
	 * @param ctx the parse tree
	 */
	void exitBool_literal(DECAFParser.Bool_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link DECAFParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(DECAFParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link DECAFParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(DECAFParser.AssignmentContext ctx);
}