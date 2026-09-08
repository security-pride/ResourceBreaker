// Generated from /Users/userxxx/J010Parser/src/main/java/com/userxxx/parser/AndroidResourceParser.g4 by ANTLR 4.13.2
package com.userxxx.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class AndroidResourceParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		TYPEDEF=1, ENUM=2, STRUCT=3, UNION=4, VOID=5, LOCAL=6, IF=7, ELSE=8, SWITCH=9, 
		CASE=10, BREAK=11, DEFAULT=12, RETURN=13, WHILE=14, FOR=15, COMMENT=16, 
		MULTILINE_COMMENT=17, LPAREN=18, RPAREN=19, LBRACE=20, RBRACE=21, LBRACK=22, 
		RBRACK=23, SEMI=24, COLON=25, COMMA=26, DOT=27, ASSIGN=28, PLUS=29, MINUS=30, 
		STAR=31, DIV=32, MOD=33, AND=34, AND2=35, OR=36, OR2=37, XOR=38, NOT=39, 
		QUESTION=40, ARROW=41, LSHIFT=42, RSHIFT=43, EQUAL=44, NOTEQUAL=45, LT=46, 
		LTEQ=47, GT=48, GTEQ=49, PLUSPLUS=50, MINUSMINUS=51, PLUSEQ=52, MINUSEQ=53, 
		STAREQ=54, DIVEQ=55, MODEQ=56, ANDEQ=57, OREQ=58, XOREQ=59, LSHIFTEQ=60, 
		RSHIFTEQ=61, HEX_NUMBER=62, NUMBER=63, IDENTIFIER=64, STRING=65, WS=66;
	public static final int
		RULE_file = 0, RULE_statement = 1, RULE_breakStmt = 2, RULE_whileStmt = 3, 
		RULE_switchStmt = 4, RULE_switchBlockStatementGroup = 5, RULE_switchLabel = 6, 
		RULE_forStmt = 7, RULE_exprStmt = 8, RULE_ifStmt = 9, RULE_statementOrBlock = 10, 
		RULE_block = 11, RULE_returnStmt = 12, RULE_typedefDecl = 13, RULE_enumDecl = 14, 
		RULE_enumItemList = 15, RULE_enumItem = 16, RULE_structDecl = 17, RULE_structItemList = 18, 
		RULE_structItem = 19, RULE_unionDecl = 20, RULE_varDecl = 21, RULE_argList = 22, 
		RULE_funcCall = 23, RULE_type = 24, RULE_funcDecl = 25, RULE_funcBody = 26, 
		RULE_paramList = 27, RULE_param = 28, RULE_assignStmt = 29, RULE_expr = 30, 
		RULE_fieldAttributes = 31, RULE_attribute = 32;
	private static String[] makeRuleNames() {
		return new String[] {
			"file", "statement", "breakStmt", "whileStmt", "switchStmt", "switchBlockStatementGroup", 
			"switchLabel", "forStmt", "exprStmt", "ifStmt", "statementOrBlock", "block", 
			"returnStmt", "typedefDecl", "enumDecl", "enumItemList", "enumItem", 
			"structDecl", "structItemList", "structItem", "unionDecl", "varDecl", 
			"argList", "funcCall", "type", "funcDecl", "funcBody", "paramList", "param", 
			"assignStmt", "expr", "fieldAttributes", "attribute"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'typedef'", "'enum'", "'struct'", "'union'", "'void'", "'local'", 
			"'if'", "'else'", "'switch'", "'case'", "'break'", "'default'", "'return'", 
			"'while'", "'for'", null, null, "'('", "')'", "'{'", "'}'", "'['", "']'", 
			"';'", "':'", "','", "'.'", "'='", "'+'", "'-'", "'*'", "'/'", "'%'", 
			"'&'", "'&&'", "'|'", "'||'", "'^'", "'!'", "'?'", "'->'", "'<<'", "'>>'", 
			"'=='", "'!='", "'<'", "'<='", "'>'", "'>='", "'++'", "'--'", "'+='", 
			"'-='", "'*='", "'/='", "'%='", "'&='", "'|='", "'^='", "'<<='", "'>>='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "TYPEDEF", "ENUM", "STRUCT", "UNION", "VOID", "LOCAL", "IF", "ELSE", 
			"SWITCH", "CASE", "BREAK", "DEFAULT", "RETURN", "WHILE", "FOR", "COMMENT", 
			"MULTILINE_COMMENT", "LPAREN", "RPAREN", "LBRACE", "RBRACE", "LBRACK", 
			"RBRACK", "SEMI", "COLON", "COMMA", "DOT", "ASSIGN", "PLUS", "MINUS", 
			"STAR", "DIV", "MOD", "AND", "AND2", "OR", "OR2", "XOR", "NOT", "QUESTION", 
			"ARROW", "LSHIFT", "RSHIFT", "EQUAL", "NOTEQUAL", "LT", "LTEQ", "GT", 
			"GTEQ", "PLUSPLUS", "MINUSMINUS", "PLUSEQ", "MINUSEQ", "STAREQ", "DIVEQ", 
			"MODEQ", "ANDEQ", "OREQ", "XOREQ", "LSHIFTEQ", "RSHIFTEQ", "HEX_NUMBER", 
			"NUMBER", "IDENTIFIER", "STRING", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "AndroidResourceParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public AndroidResourceParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FileContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public FileContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_file; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterFile(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitFile(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitFile(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FileContext file() throws RecognitionException {
		FileContext _localctx = new FileContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_file);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(69);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4608307748012758290L) != 0) || _la==IDENTIFIER || _la==STRING) {
				{
				{
				setState(66);
				statement();
				}
				}
				setState(71);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementContext extends ParserRuleContext {
		public TypedefDeclContext typedefDecl() {
			return getRuleContext(TypedefDeclContext.class,0);
		}
		public EnumDeclContext enumDecl() {
			return getRuleContext(EnumDeclContext.class,0);
		}
		public StructDeclContext structDecl() {
			return getRuleContext(StructDeclContext.class,0);
		}
		public FuncDeclContext funcDecl() {
			return getRuleContext(FuncDeclContext.class,0);
		}
		public AssignStmtContext assignStmt() {
			return getRuleContext(AssignStmtContext.class,0);
		}
		public ReturnStmtContext returnStmt() {
			return getRuleContext(ReturnStmtContext.class,0);
		}
		public ExprStmtContext exprStmt() {
			return getRuleContext(ExprStmtContext.class,0);
		}
		public VarDeclContext varDecl() {
			return getRuleContext(VarDeclContext.class,0);
		}
		public IfStmtContext ifStmt() {
			return getRuleContext(IfStmtContext.class,0);
		}
		public ForStmtContext forStmt() {
			return getRuleContext(ForStmtContext.class,0);
		}
		public WhileStmtContext whileStmt() {
			return getRuleContext(WhileStmtContext.class,0);
		}
		public SwitchStmtContext switchStmt() {
			return getRuleContext(SwitchStmtContext.class,0);
		}
		public BreakStmtContext breakStmt() {
			return getRuleContext(BreakStmtContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_statement);
		try {
			setState(85);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(72);
				typedefDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(73);
				enumDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(74);
				structDecl();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(75);
				funcDecl();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(76);
				assignStmt();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(77);
				returnStmt();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(78);
				exprStmt();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(79);
				varDecl();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(80);
				ifStmt();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(81);
				forStmt();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(82);
				whileStmt();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(83);
				switchStmt();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(84);
				breakStmt();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BreakStmtContext extends ParserRuleContext {
		public TerminalNode BREAK() { return getToken(AndroidResourceParser.BREAK, 0); }
		public TerminalNode SEMI() { return getToken(AndroidResourceParser.SEMI, 0); }
		public BreakStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_breakStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterBreakStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitBreakStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitBreakStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BreakStmtContext breakStmt() throws RecognitionException {
		BreakStmtContext _localctx = new BreakStmtContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_breakStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(87);
			match(BREAK);
			setState(88);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhileStmtContext extends ParserRuleContext {
		public TerminalNode WHILE() { return getToken(AndroidResourceParser.WHILE, 0); }
		public TerminalNode LPAREN() { return getToken(AndroidResourceParser.LPAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(AndroidResourceParser.RPAREN, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public WhileStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whileStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterWhileStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitWhileStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitWhileStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhileStmtContext whileStmt() throws RecognitionException {
		WhileStmtContext _localctx = new WhileStmtContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_whileStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(90);
			match(WHILE);
			setState(91);
			match(LPAREN);
			setState(92);
			expr(0);
			setState(93);
			match(RPAREN);
			setState(94);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SwitchStmtContext extends ParserRuleContext {
		public TerminalNode SWITCH() { return getToken(AndroidResourceParser.SWITCH, 0); }
		public TerminalNode LPAREN() { return getToken(AndroidResourceParser.LPAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(AndroidResourceParser.RPAREN, 0); }
		public TerminalNode LBRACE() { return getToken(AndroidResourceParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(AndroidResourceParser.RBRACE, 0); }
		public List<SwitchBlockStatementGroupContext> switchBlockStatementGroup() {
			return getRuleContexts(SwitchBlockStatementGroupContext.class);
		}
		public SwitchBlockStatementGroupContext switchBlockStatementGroup(int i) {
			return getRuleContext(SwitchBlockStatementGroupContext.class,i);
		}
		public List<SwitchLabelContext> switchLabel() {
			return getRuleContexts(SwitchLabelContext.class);
		}
		public SwitchLabelContext switchLabel(int i) {
			return getRuleContext(SwitchLabelContext.class,i);
		}
		public SwitchStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterSwitchStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitSwitchStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitSwitchStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchStmtContext switchStmt() throws RecognitionException {
		SwitchStmtContext _localctx = new SwitchStmtContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_switchStmt);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(96);
			match(SWITCH);
			setState(97);
			match(LPAREN);
			setState(98);
			expr(0);
			setState(99);
			match(RPAREN);
			setState(100);
			match(LBRACE);
			setState(104);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(101);
					switchBlockStatementGroup();
					}
					} 
				}
				setState(106);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			}
			setState(110);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CASE || _la==DEFAULT) {
				{
				{
				setState(107);
				switchLabel();
				}
				}
				setState(112);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(113);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SwitchBlockStatementGroupContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public List<SwitchLabelContext> switchLabel() {
			return getRuleContexts(SwitchLabelContext.class);
		}
		public SwitchLabelContext switchLabel(int i) {
			return getRuleContext(SwitchLabelContext.class,i);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public SwitchBlockStatementGroupContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchBlockStatementGroup; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterSwitchBlockStatementGroup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitSwitchBlockStatementGroup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitSwitchBlockStatementGroup(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchBlockStatementGroupContext switchBlockStatementGroup() throws RecognitionException {
		SwitchBlockStatementGroupContext _localctx = new SwitchBlockStatementGroupContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_switchBlockStatementGroup);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(116); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(115);
					switchLabel();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(118); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(127);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TYPEDEF:
			case ENUM:
			case STRUCT:
			case VOID:
			case LOCAL:
			case IF:
			case SWITCH:
			case CASE:
			case BREAK:
			case DEFAULT:
			case RETURN:
			case WHILE:
			case FOR:
			case LPAREN:
			case RBRACE:
			case PLUS:
			case MINUS:
			case STAR:
			case AND:
			case NOT:
			case PLUSPLUS:
			case MINUSMINUS:
			case HEX_NUMBER:
			case NUMBER:
			case IDENTIFIER:
			case STRING:
				{
				setState(123);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4608307748012758290L) != 0) || _la==IDENTIFIER || _la==STRING) {
					{
					{
					setState(120);
					statement();
					}
					}
					setState(125);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case LBRACE:
				{
				setState(126);
				block();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SwitchLabelContext extends ParserRuleContext {
		public TerminalNode CASE() { return getToken(AndroidResourceParser.CASE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode COLON() { return getToken(AndroidResourceParser.COLON, 0); }
		public TerminalNode DEFAULT() { return getToken(AndroidResourceParser.DEFAULT, 0); }
		public SwitchLabelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchLabel; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterSwitchLabel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitSwitchLabel(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitSwitchLabel(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchLabelContext switchLabel() throws RecognitionException {
		SwitchLabelContext _localctx = new SwitchLabelContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_switchLabel);
		try {
			setState(135);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(129);
				match(CASE);
				setState(130);
				expr(0);
				setState(131);
				match(COLON);
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(133);
				match(DEFAULT);
				setState(134);
				match(COLON);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForStmtContext extends ParserRuleContext {
		public TerminalNode FOR() { return getToken(AndroidResourceParser.FOR, 0); }
		public TerminalNode LPAREN() { return getToken(AndroidResourceParser.LPAREN, 0); }
		public List<TerminalNode> SEMI() { return getTokens(AndroidResourceParser.SEMI); }
		public TerminalNode SEMI(int i) {
			return getToken(AndroidResourceParser.SEMI, i);
		}
		public TerminalNode RPAREN() { return getToken(AndroidResourceParser.RPAREN, 0); }
		public StatementOrBlockContext statementOrBlock() {
			return getRuleContext(StatementOrBlockContext.class,0);
		}
		public AssignStmtContext assignStmt() {
			return getRuleContext(AssignStmtContext.class,0);
		}
		public ExprStmtContext exprStmt() {
			return getRuleContext(ExprStmtContext.class,0);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ForStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterForStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitForStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitForStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForStmtContext forStmt() throws RecognitionException {
		ForStmtContext _localctx = new ForStmtContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_forStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(137);
			match(FOR);
			setState(138);
			match(LPAREN);
			setState(142);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				{
				setState(139);
				assignStmt();
				}
				break;
			case 2:
				{
				setState(140);
				exprStmt();
				}
				break;
			case 3:
				{
				setState(141);
				match(SEMI);
				}
				break;
			}
			setState(145);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 18)) & ~0x3f) == 0 && ((1L << (_la - 18)) & 263895677745153L) != 0)) {
				{
				setState(144);
				expr(0);
				}
			}

			setState(147);
			match(SEMI);
			setState(149);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 18)) & ~0x3f) == 0 && ((1L << (_la - 18)) & 263895677745153L) != 0)) {
				{
				setState(148);
				expr(0);
				}
			}

			setState(151);
			match(RPAREN);
			setState(152);
			statementOrBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprStmtContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(AndroidResourceParser.SEMI, 0); }
		public ExprStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterExprStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitExprStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitExprStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprStmtContext exprStmt() throws RecognitionException {
		ExprStmtContext _localctx = new ExprStmtContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_exprStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(154);
			expr(0);
			setState(155);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IfStmtContext extends ParserRuleContext {
		public TerminalNode IF() { return getToken(AndroidResourceParser.IF, 0); }
		public TerminalNode LPAREN() { return getToken(AndroidResourceParser.LPAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(AndroidResourceParser.RPAREN, 0); }
		public List<StatementOrBlockContext> statementOrBlock() {
			return getRuleContexts(StatementOrBlockContext.class);
		}
		public StatementOrBlockContext statementOrBlock(int i) {
			return getRuleContext(StatementOrBlockContext.class,i);
		}
		public TerminalNode ELSE() { return getToken(AndroidResourceParser.ELSE, 0); }
		public IfStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterIfStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitIfStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitIfStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IfStmtContext ifStmt() throws RecognitionException {
		IfStmtContext _localctx = new IfStmtContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_ifStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(157);
			match(IF);
			setState(158);
			match(LPAREN);
			setState(159);
			expr(0);
			setState(160);
			match(RPAREN);
			setState(161);
			statementOrBlock();
			setState(164);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				{
				setState(162);
				match(ELSE);
				setState(163);
				statementOrBlock();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementOrBlockContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public TerminalNode LBRACE() { return getToken(AndroidResourceParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(AndroidResourceParser.RBRACE, 0); }
		public StatementOrBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statementOrBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterStatementOrBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitStatementOrBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitStatementOrBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementOrBlockContext statementOrBlock() throws RecognitionException {
		StatementOrBlockContext _localctx = new StatementOrBlockContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_statementOrBlock);
		int _la;
		try {
			setState(175);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TYPEDEF:
			case ENUM:
			case STRUCT:
			case VOID:
			case LOCAL:
			case IF:
			case SWITCH:
			case BREAK:
			case RETURN:
			case WHILE:
			case FOR:
			case LPAREN:
			case PLUS:
			case MINUS:
			case STAR:
			case AND:
			case NOT:
			case PLUSPLUS:
			case MINUSMINUS:
			case HEX_NUMBER:
			case NUMBER:
			case IDENTIFIER:
			case STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(166);
				statement();
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 2);
				{
				setState(167);
				match(LBRACE);
				setState(171);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4608307748012758290L) != 0) || _la==IDENTIFIER || _la==STRING) {
					{
					{
					setState(168);
					statement();
					}
					}
					setState(173);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(174);
				match(RBRACE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BlockContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(AndroidResourceParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(AndroidResourceParser.RBRACE, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(177);
			match(LBRACE);
			setState(181);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4608307748012758290L) != 0) || _la==IDENTIFIER || _la==STRING) {
				{
				{
				setState(178);
				statement();
				}
				}
				setState(183);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(184);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ReturnStmtContext extends ParserRuleContext {
		public TerminalNode RETURN() { return getToken(AndroidResourceParser.RETURN, 0); }
		public TerminalNode SEMI() { return getToken(AndroidResourceParser.SEMI, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ReturnStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterReturnStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitReturnStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitReturnStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReturnStmtContext returnStmt() throws RecognitionException {
		ReturnStmtContext _localctx = new ReturnStmtContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_returnStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(186);
			match(RETURN);
			setState(188);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 18)) & ~0x3f) == 0 && ((1L << (_la - 18)) & 263895677745153L) != 0)) {
				{
				setState(187);
				expr(0);
				}
			}

			setState(190);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypedefDeclContext extends ParserRuleContext {
		public TerminalNode TYPEDEF() { return getToken(AndroidResourceParser.TYPEDEF, 0); }
		public TerminalNode IDENTIFIER() { return getToken(AndroidResourceParser.IDENTIFIER, 0); }
		public TerminalNode SEMI() { return getToken(AndroidResourceParser.SEMI, 0); }
		public EnumDeclContext enumDecl() {
			return getRuleContext(EnumDeclContext.class,0);
		}
		public StructDeclContext structDecl() {
			return getRuleContext(StructDeclContext.class,0);
		}
		public UnionDeclContext unionDecl() {
			return getRuleContext(UnionDeclContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public FieldAttributesContext fieldAttributes() {
			return getRuleContext(FieldAttributesContext.class,0);
		}
		public TypedefDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typedefDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterTypedefDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitTypedefDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitTypedefDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypedefDeclContext typedefDecl() throws RecognitionException {
		TypedefDeclContext _localctx = new TypedefDeclContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_typedefDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(192);
			match(TYPEDEF);
			setState(197);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				{
				setState(193);
				enumDecl();
				}
				break;
			case 2:
				{
				setState(194);
				structDecl();
				}
				break;
			case 3:
				{
				setState(195);
				unionDecl();
				}
				break;
			case 4:
				{
				setState(196);
				type();
				}
				break;
			}
			setState(199);
			match(IDENTIFIER);
			setState(201);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LT) {
				{
				setState(200);
				fieldAttributes();
				}
			}

			setState(203);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EnumDeclContext extends ParserRuleContext {
		public TerminalNode ENUM() { return getToken(AndroidResourceParser.ENUM, 0); }
		public TerminalNode LBRACE() { return getToken(AndroidResourceParser.LBRACE, 0); }
		public EnumItemListContext enumItemList() {
			return getRuleContext(EnumItemListContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(AndroidResourceParser.RBRACE, 0); }
		public TerminalNode LT() { return getToken(AndroidResourceParser.LT, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode GT() { return getToken(AndroidResourceParser.GT, 0); }
		public TerminalNode IDENTIFIER() { return getToken(AndroidResourceParser.IDENTIFIER, 0); }
		public TerminalNode SEMI() { return getToken(AndroidResourceParser.SEMI, 0); }
		public EnumDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterEnumDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitEnumDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitEnumDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumDeclContext enumDecl() throws RecognitionException {
		EnumDeclContext _localctx = new EnumDeclContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_enumDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(205);
			match(ENUM);
			setState(210);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LT) {
				{
				setState(206);
				match(LT);
				setState(207);
				type();
				setState(208);
				match(GT);
				}
			}

			setState(212);
			match(LBRACE);
			setState(213);
			enumItemList();
			setState(214);
			match(RBRACE);
			setState(216);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				{
				setState(215);
				match(IDENTIFIER);
				}
				break;
			}
			setState(219);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SEMI) {
				{
				setState(218);
				match(SEMI);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EnumItemListContext extends ParserRuleContext {
		public List<EnumItemContext> enumItem() {
			return getRuleContexts(EnumItemContext.class);
		}
		public EnumItemContext enumItem(int i) {
			return getRuleContext(EnumItemContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(AndroidResourceParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(AndroidResourceParser.COMMA, i);
		}
		public EnumItemListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumItemList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterEnumItemList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitEnumItemList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitEnumItemList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumItemListContext enumItemList() throws RecognitionException {
		EnumItemListContext _localctx = new EnumItemListContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_enumItemList);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(221);
			enumItem();
			setState(226);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(222);
					match(COMMA);
					setState(223);
					enumItem();
					}
					} 
				}
				setState(228);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
			}
			setState(230);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(229);
				match(COMMA);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EnumItemContext extends ParserRuleContext {
		public AssignStmtContext assignStmt() {
			return getRuleContext(AssignStmtContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(AndroidResourceParser.IDENTIFIER, 0); }
		public EnumItemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumItem; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterEnumItem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitEnumItem(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitEnumItem(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumItemContext enumItem() throws RecognitionException {
		EnumItemContext _localctx = new EnumItemContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_enumItem);
		try {
			setState(234);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(232);
				assignStmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(233);
				match(IDENTIFIER);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StructDeclContext extends ParserRuleContext {
		public TerminalNode STRUCT() { return getToken(AndroidResourceParser.STRUCT, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(AndroidResourceParser.IDENTIFIER, 0); }
		public TerminalNode LPAREN() { return getToken(AndroidResourceParser.LPAREN, 0); }
		public ParamListContext paramList() {
			return getRuleContext(ParamListContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(AndroidResourceParser.RPAREN, 0); }
		public FieldAttributesContext fieldAttributes() {
			return getRuleContext(FieldAttributesContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(AndroidResourceParser.SEMI, 0); }
		public StructDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_structDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterStructDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitStructDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitStructDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StructDeclContext structDecl() throws RecognitionException {
		StructDeclContext _localctx = new StructDeclContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_structDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(236);
			match(STRUCT);
			setState(238);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER) {
				{
				setState(237);
				match(IDENTIFIER);
				}
			}

			setState(244);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(240);
				match(LPAREN);
				setState(241);
				paramList();
				setState(242);
				match(RPAREN);
				}
			}

			setState(246);
			block();
			setState(248);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LT) {
				{
				setState(247);
				fieldAttributes();
				}
			}

			setState(251);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SEMI) {
				{
				setState(250);
				match(SEMI);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StructItemListContext extends ParserRuleContext {
		public List<StructItemContext> structItem() {
			return getRuleContexts(StructItemContext.class);
		}
		public StructItemContext structItem(int i) {
			return getRuleContext(StructItemContext.class,i);
		}
		public StructItemListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_structItemList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterStructItemList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitStructItemList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitStructItemList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StructItemListContext structItemList() throws RecognitionException {
		StructItemListContext _localctx = new StructItemListContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_structItemList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(256);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 1)) & ~0x3f) == 0 && ((1L << (_la - 1)) & -9223372036854775745L) != 0)) {
				{
				{
				setState(253);
				structItem();
				}
				}
				setState(258);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StructItemContext extends ParserRuleContext {
		public TypedefDeclContext typedefDecl() {
			return getRuleContext(TypedefDeclContext.class,0);
		}
		public StructDeclContext structDecl() {
			return getRuleContext(StructDeclContext.class,0);
		}
		public VarDeclContext varDecl() {
			return getRuleContext(VarDeclContext.class,0);
		}
		public AssignStmtContext assignStmt() {
			return getRuleContext(AssignStmtContext.class,0);
		}
		public EnumDeclContext enumDecl() {
			return getRuleContext(EnumDeclContext.class,0);
		}
		public UnionDeclContext unionDecl() {
			return getRuleContext(UnionDeclContext.class,0);
		}
		public StructItemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_structItem; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterStructItem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitStructItem(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitStructItem(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StructItemContext structItem() throws RecognitionException {
		StructItemContext _localctx = new StructItemContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_structItem);
		try {
			setState(265);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(259);
				typedefDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(260);
				structDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(261);
				varDecl();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(262);
				assignStmt();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(263);
				enumDecl();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(264);
				unionDecl();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UnionDeclContext extends ParserRuleContext {
		public TerminalNode UNION() { return getToken(AndroidResourceParser.UNION, 0); }
		public TerminalNode LBRACE() { return getToken(AndroidResourceParser.LBRACE, 0); }
		public StructItemListContext structItemList() {
			return getRuleContext(StructItemListContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(AndroidResourceParser.RBRACE, 0); }
		public TerminalNode IDENTIFIER() { return getToken(AndroidResourceParser.IDENTIFIER, 0); }
		public TerminalNode SEMI() { return getToken(AndroidResourceParser.SEMI, 0); }
		public UnionDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unionDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterUnionDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitUnionDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitUnionDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnionDeclContext unionDecl() throws RecognitionException {
		UnionDeclContext _localctx = new UnionDeclContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_unionDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(267);
			match(UNION);
			setState(268);
			match(LBRACE);
			setState(269);
			structItemList();
			setState(270);
			match(RBRACE);
			setState(272);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				{
				setState(271);
				match(IDENTIFIER);
				}
				break;
			}
			setState(275);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SEMI) {
				{
				setState(274);
				match(SEMI);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VarDeclContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(AndroidResourceParser.IDENTIFIER, 0); }
		public TerminalNode SEMI() { return getToken(AndroidResourceParser.SEMI, 0); }
		public TerminalNode LPAREN() { return getToken(AndroidResourceParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(AndroidResourceParser.RPAREN, 0); }
		public TerminalNode LBRACK() { return getToken(AndroidResourceParser.LBRACK, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode RBRACK() { return getToken(AndroidResourceParser.RBRACK, 0); }
		public TerminalNode ASSIGN() { return getToken(AndroidResourceParser.ASSIGN, 0); }
		public FieldAttributesContext fieldAttributes() {
			return getRuleContext(FieldAttributesContext.class,0);
		}
		public ArgListContext argList() {
			return getRuleContext(ArgListContext.class,0);
		}
		public TerminalNode LOCAL() { return getToken(AndroidResourceParser.LOCAL, 0); }
		public VarDeclContext varDecl() {
			return getRuleContext(VarDeclContext.class,0);
		}
		public VarDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterVarDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitVarDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitVarDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarDeclContext varDecl() throws RecognitionException {
		VarDeclContext _localctx = new VarDeclContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_varDecl);
		int _la;
		try {
			setState(303);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRUCT:
			case VOID:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(277);
				type();
				setState(278);
				match(IDENTIFIER);
				setState(284);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LPAREN) {
					{
					setState(279);
					match(LPAREN);
					setState(281);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (((((_la - 18)) & ~0x3f) == 0 && ((1L << (_la - 18)) & 263895677745153L) != 0)) {
						{
						setState(280);
						argList();
						}
					}

					setState(283);
					match(RPAREN);
					}
				}

				setState(290);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LBRACK) {
					{
					setState(286);
					match(LBRACK);
					setState(287);
					expr(0);
					setState(288);
					match(RBRACK);
					}
				}

				setState(294);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ASSIGN) {
					{
					setState(292);
					match(ASSIGN);
					setState(293);
					expr(0);
					}
				}

				setState(297);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LT) {
					{
					setState(296);
					fieldAttributes();
					}
				}

				setState(299);
				match(SEMI);
				}
				break;
			case LOCAL:
				enterOuterAlt(_localctx, 2);
				{
				setState(301);
				match(LOCAL);
				setState(302);
				varDecl();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgListContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(AndroidResourceParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(AndroidResourceParser.COMMA, i);
		}
		public ArgListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterArgList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitArgList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitArgList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgListContext argList() throws RecognitionException {
		ArgListContext _localctx = new ArgListContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_argList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(305);
			expr(0);
			setState(310);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(306);
				match(COMMA);
				setState(307);
				expr(0);
				}
				}
				setState(312);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FuncCallContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(AndroidResourceParser.IDENTIFIER, 0); }
		public TerminalNode LPAREN() { return getToken(AndroidResourceParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(AndroidResourceParser.RPAREN, 0); }
		public ArgListContext argList() {
			return getRuleContext(ArgListContext.class,0);
		}
		public FuncCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterFuncCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitFuncCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitFuncCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncCallContext funcCall() throws RecognitionException {
		FuncCallContext _localctx = new FuncCallContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_funcCall);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(313);
			match(IDENTIFIER);
			setState(314);
			match(LPAREN);
			setState(316);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 18)) & ~0x3f) == 0 && ((1L << (_la - 18)) & 263895677745153L) != 0)) {
				{
				setState(315);
				argList();
				}
			}

			setState(318);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeContext extends ParserRuleContext {
		public TerminalNode STRUCT() { return getToken(AndroidResourceParser.STRUCT, 0); }
		public List<TerminalNode> IDENTIFIER() { return getTokens(AndroidResourceParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(AndroidResourceParser.IDENTIFIER, i);
		}
		public TerminalNode LT() { return getToken(AndroidResourceParser.LT, 0); }
		public TerminalNode GT() { return getToken(AndroidResourceParser.GT, 0); }
		public List<TerminalNode> COMMA() { return getTokens(AndroidResourceParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(AndroidResourceParser.COMMA, i);
		}
		public TerminalNode VOID() { return getToken(AndroidResourceParser.VOID, 0); }
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_type);
		int _la;
		try {
			setState(336);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRUCT:
				enterOuterAlt(_localctx, 1);
				{
				setState(320);
				match(STRUCT);
				setState(321);
				match(IDENTIFIER);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(322);
				match(IDENTIFIER);
				setState(333);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LT) {
					{
					setState(323);
					match(LT);
					setState(324);
					match(IDENTIFIER);
					setState(329);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(325);
						match(COMMA);
						setState(326);
						match(IDENTIFIER);
						}
						}
						setState(331);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(332);
					match(GT);
					}
				}

				}
				break;
			case VOID:
				enterOuterAlt(_localctx, 3);
				{
				setState(335);
				match(VOID);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FuncDeclContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(AndroidResourceParser.IDENTIFIER, 0); }
		public TerminalNode LPAREN() { return getToken(AndroidResourceParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(AndroidResourceParser.RPAREN, 0); }
		public FuncBodyContext funcBody() {
			return getRuleContext(FuncBodyContext.class,0);
		}
		public ParamListContext paramList() {
			return getRuleContext(ParamListContext.class,0);
		}
		public FuncDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterFuncDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitFuncDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitFuncDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncDeclContext funcDecl() throws RecognitionException {
		FuncDeclContext _localctx = new FuncDeclContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_funcDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(338);
			type();
			setState(339);
			match(IDENTIFIER);
			setState(340);
			match(LPAREN);
			setState(342);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 3)) & ~0x3f) == 0 && ((1L << (_la - 3)) & 2305843009213693957L) != 0)) {
				{
				setState(341);
				paramList();
				}
			}

			setState(344);
			match(RPAREN);
			setState(345);
			funcBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FuncBodyContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(AndroidResourceParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(AndroidResourceParser.RBRACE, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public FuncBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterFuncBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitFuncBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitFuncBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncBodyContext funcBody() throws RecognitionException {
		FuncBodyContext _localctx = new FuncBodyContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_funcBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(347);
			match(LBRACE);
			setState(351);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4608307748012758290L) != 0) || _la==IDENTIFIER || _la==STRING) {
				{
				{
				setState(348);
				statement();
				}
				}
				setState(353);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(354);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParamListContext extends ParserRuleContext {
		public List<ParamContext> param() {
			return getRuleContexts(ParamContext.class);
		}
		public ParamContext param(int i) {
			return getRuleContext(ParamContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(AndroidResourceParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(AndroidResourceParser.COMMA, i);
		}
		public ParamListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_paramList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterParamList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitParamList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitParamList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParamListContext paramList() throws RecognitionException {
		ParamListContext _localctx = new ParamListContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_paramList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(356);
			param();
			setState(361);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(357);
				match(COMMA);
				setState(358);
				param();
				}
				}
				setState(363);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParamContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(AndroidResourceParser.IDENTIFIER, 0); }
		public TerminalNode AND() { return getToken(AndroidResourceParser.AND, 0); }
		public TerminalNode STAR() { return getToken(AndroidResourceParser.STAR, 0); }
		public ParamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_param; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterParam(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitParam(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitParam(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParamContext param() throws RecognitionException {
		ParamContext _localctx = new ParamContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_param);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(364);
			type();
			setState(366);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR || _la==AND) {
				{
				setState(365);
				_la = _input.LA(1);
				if ( !(_la==STAR || _la==AND) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(368);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AssignStmtContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(AndroidResourceParser.IDENTIFIER, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(AndroidResourceParser.ASSIGN, 0); }
		public TerminalNode PLUSEQ() { return getToken(AndroidResourceParser.PLUSEQ, 0); }
		public TerminalNode MINUSEQ() { return getToken(AndroidResourceParser.MINUSEQ, 0); }
		public TerminalNode STAREQ() { return getToken(AndroidResourceParser.STAREQ, 0); }
		public TerminalNode DIVEQ() { return getToken(AndroidResourceParser.DIVEQ, 0); }
		public TerminalNode MODEQ() { return getToken(AndroidResourceParser.MODEQ, 0); }
		public TerminalNode ANDEQ() { return getToken(AndroidResourceParser.ANDEQ, 0); }
		public TerminalNode OREQ() { return getToken(AndroidResourceParser.OREQ, 0); }
		public TerminalNode XOREQ() { return getToken(AndroidResourceParser.XOREQ, 0); }
		public TerminalNode LSHIFTEQ() { return getToken(AndroidResourceParser.LSHIFTEQ, 0); }
		public TerminalNode RSHIFTEQ() { return getToken(AndroidResourceParser.RSHIFTEQ, 0); }
		public TerminalNode SEMI() { return getToken(AndroidResourceParser.SEMI, 0); }
		public AssignStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterAssignStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitAssignStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitAssignStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignStmtContext assignStmt() throws RecognitionException {
		AssignStmtContext _localctx = new AssignStmtContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_assignStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(370);
			match(IDENTIFIER);
			setState(371);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 4607182419068452864L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(372);
			expr(0);
			setState(374);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
			case 1:
				{
				setState(373);
				match(SEMI);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode AND() { return getToken(AndroidResourceParser.AND, 0); }
		public TerminalNode STAR() { return getToken(AndroidResourceParser.STAR, 0); }
		public TerminalNode PLUS() { return getToken(AndroidResourceParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(AndroidResourceParser.MINUS, 0); }
		public TerminalNode NOT() { return getToken(AndroidResourceParser.NOT, 0); }
		public FuncCallContext funcCall() {
			return getRuleContext(FuncCallContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(AndroidResourceParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(AndroidResourceParser.RPAREN, 0); }
		public TerminalNode IDENTIFIER() { return getToken(AndroidResourceParser.IDENTIFIER, 0); }
		public TerminalNode NUMBER() { return getToken(AndroidResourceParser.NUMBER, 0); }
		public TerminalNode HEX_NUMBER() { return getToken(AndroidResourceParser.HEX_NUMBER, 0); }
		public TerminalNode STRING() { return getToken(AndroidResourceParser.STRING, 0); }
		public TerminalNode PLUSPLUS() { return getToken(AndroidResourceParser.PLUSPLUS, 0); }
		public TerminalNode MINUSMINUS() { return getToken(AndroidResourceParser.MINUSMINUS, 0); }
		public TerminalNode DIV() { return getToken(AndroidResourceParser.DIV, 0); }
		public TerminalNode MOD() { return getToken(AndroidResourceParser.MOD, 0); }
		public TerminalNode LSHIFT() { return getToken(AndroidResourceParser.LSHIFT, 0); }
		public TerminalNode RSHIFT() { return getToken(AndroidResourceParser.RSHIFT, 0); }
		public TerminalNode OR() { return getToken(AndroidResourceParser.OR, 0); }
		public TerminalNode EQUAL() { return getToken(AndroidResourceParser.EQUAL, 0); }
		public TerminalNode NOTEQUAL() { return getToken(AndroidResourceParser.NOTEQUAL, 0); }
		public TerminalNode LT() { return getToken(AndroidResourceParser.LT, 0); }
		public TerminalNode LTEQ() { return getToken(AndroidResourceParser.LTEQ, 0); }
		public TerminalNode GT() { return getToken(AndroidResourceParser.GT, 0); }
		public TerminalNode GTEQ() { return getToken(AndroidResourceParser.GTEQ, 0); }
		public TerminalNode AND2() { return getToken(AndroidResourceParser.AND2, 0); }
		public TerminalNode OR2() { return getToken(AndroidResourceParser.OR2, 0); }
		public TerminalNode DOT() { return getToken(AndroidResourceParser.DOT, 0); }
		public TerminalNode LBRACK() { return getToken(AndroidResourceParser.LBRACK, 0); }
		public TerminalNode RBRACK() { return getToken(AndroidResourceParser.RBRACK, 0); }
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 60;
		enterRecursionRule(_localctx, 60, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(390);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
			case 1:
				{
				setState(377);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 570693779456L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(378);
				expr(11);
				}
				break;
			case 2:
				{
				setState(379);
				funcCall();
				}
				break;
			case 3:
				{
				setState(380);
				match(LPAREN);
				setState(381);
				expr(0);
				setState(382);
				match(RPAREN);
				}
				break;
			case 4:
				{
				setState(384);
				match(IDENTIFIER);
				}
				break;
			case 5:
				{
				setState(385);
				match(NUMBER);
				}
				break;
			case 6:
				{
				setState(386);
				match(HEX_NUMBER);
				}
				break;
			case 7:
				{
				setState(387);
				match(STRING);
				}
				break;
			case 8:
				{
				setState(388);
				_la = _input.LA(1);
				if ( !(_la==PLUSPLUS || _la==MINUSMINUS) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(389);
				expr(1);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(410);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,50,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(408);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,49,_ctx) ) {
					case 1:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(392);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(393);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 13296681877504L) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(394);
						expr(14);
						}
						break;
					case 2:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(395);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(396);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 1108479519490048L) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(397);
						expr(13);
						}
						break;
					case 3:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(398);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(399);
						match(DOT);
						setState(400);
						expr(4);
						}
						break;
					case 4:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(401);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(402);
						match(LBRACK);
						setState(403);
						expr(0);
						setState(404);
						match(RBRACK);
						}
						break;
					case 5:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(406);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(407);
						_la = _input.LA(1);
						if ( !(_la==PLUSPLUS || _la==MINUSMINUS) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
						break;
					}
					} 
				}
				setState(412);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,50,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FieldAttributesContext extends ParserRuleContext {
		public TerminalNode LT() { return getToken(AndroidResourceParser.LT, 0); }
		public List<AttributeContext> attribute() {
			return getRuleContexts(AttributeContext.class);
		}
		public AttributeContext attribute(int i) {
			return getRuleContext(AttributeContext.class,i);
		}
		public TerminalNode GT() { return getToken(AndroidResourceParser.GT, 0); }
		public List<TerminalNode> COMMA() { return getTokens(AndroidResourceParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(AndroidResourceParser.COMMA, i);
		}
		public FieldAttributesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldAttributes; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterFieldAttributes(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitFieldAttributes(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitFieldAttributes(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldAttributesContext fieldAttributes() throws RecognitionException {
		FieldAttributesContext _localctx = new FieldAttributesContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_fieldAttributes);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(413);
			match(LT);
			setState(414);
			attribute();
			setState(419);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(415);
				match(COMMA);
				setState(416);
				attribute();
				}
				}
				setState(421);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(422);
			match(GT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AttributeContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(AndroidResourceParser.IDENTIFIER, 0); }
		public TerminalNode ASSIGN() { return getToken(AndroidResourceParser.ASSIGN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public AttributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attribute; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).enterAttribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AndroidResourceParserListener ) ((AndroidResourceParserListener)listener).exitAttribute(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AndroidResourceParserVisitor ) return ((AndroidResourceParserVisitor<? extends T>)visitor).visitAttribute(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeContext attribute() throws RecognitionException {
		AttributeContext _localctx = new AttributeContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_attribute);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(424);
			match(IDENTIFIER);
			setState(425);
			match(ASSIGN);
			setState(426);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 30:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 13);
		case 1:
			return precpred(_ctx, 12);
		case 2:
			return precpred(_ctx, 3);
		case 3:
			return precpred(_ctx, 10);
		case 4:
			return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001B\u01ad\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"+
		"\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007\u001e"+
		"\u0002\u001f\u0007\u001f\u0002 \u0007 \u0001\u0000\u0005\u0000D\b\u0000"+
		"\n\u0000\f\u0000G\t\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001V\b\u0001\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0005\u0004g\b\u0004\n\u0004\f\u0004j\t\u0004"+
		"\u0001\u0004\u0005\u0004m\b\u0004\n\u0004\f\u0004p\t\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0005\u0004\u0005u\b\u0005\u000b\u0005\f\u0005v\u0001"+
		"\u0005\u0005\u0005z\b\u0005\n\u0005\f\u0005}\t\u0005\u0001\u0005\u0003"+
		"\u0005\u0080\b\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0003\u0006\u0088\b\u0006\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0003\u0007\u008f\b\u0007\u0001\u0007\u0003"+
		"\u0007\u0092\b\u0007\u0001\u0007\u0001\u0007\u0003\u0007\u0096\b\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0003\t\u00a5\b\t\u0001\n\u0001"+
		"\n\u0001\n\u0005\n\u00aa\b\n\n\n\f\n\u00ad\t\n\u0001\n\u0003\n\u00b0\b"+
		"\n\u0001\u000b\u0001\u000b\u0005\u000b\u00b4\b\u000b\n\u000b\f\u000b\u00b7"+
		"\t\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0003\f\u00bd\b\f\u0001"+
		"\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0003\r\u00c6\b\r\u0001"+
		"\r\u0001\r\u0003\r\u00ca\b\r\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0003\u000e\u00d3\b\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0003\u000e\u00d9\b\u000e\u0001\u000e\u0003"+
		"\u000e\u00dc\b\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0005\u000f\u00e1"+
		"\b\u000f\n\u000f\f\u000f\u00e4\t\u000f\u0001\u000f\u0003\u000f\u00e7\b"+
		"\u000f\u0001\u0010\u0001\u0010\u0003\u0010\u00eb\b\u0010\u0001\u0011\u0001"+
		"\u0011\u0003\u0011\u00ef\b\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0003\u0011\u00f5\b\u0011\u0001\u0011\u0001\u0011\u0003\u0011\u00f9"+
		"\b\u0011\u0001\u0011\u0003\u0011\u00fc\b\u0011\u0001\u0012\u0005\u0012"+
		"\u00ff\b\u0012\n\u0012\f\u0012\u0102\t\u0012\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0003\u0013\u010a\b\u0013\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0003\u0014\u0111"+
		"\b\u0014\u0001\u0014\u0003\u0014\u0114\b\u0014\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0003\u0015\u011a\b\u0015\u0001\u0015\u0003\u0015"+
		"\u011d\b\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0003\u0015"+
		"\u0123\b\u0015\u0001\u0015\u0001\u0015\u0003\u0015\u0127\b\u0015\u0001"+
		"\u0015\u0003\u0015\u012a\b\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001"+
		"\u0015\u0003\u0015\u0130\b\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0005"+
		"\u0016\u0135\b\u0016\n\u0016\f\u0016\u0138\t\u0016\u0001\u0017\u0001\u0017"+
		"\u0001\u0017\u0003\u0017\u013d\b\u0017\u0001\u0017\u0001\u0017\u0001\u0018"+
		"\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018"+
		"\u0005\u0018\u0148\b\u0018\n\u0018\f\u0018\u014b\t\u0018\u0001\u0018\u0003"+
		"\u0018\u014e\b\u0018\u0001\u0018\u0003\u0018\u0151\b\u0018\u0001\u0019"+
		"\u0001\u0019\u0001\u0019\u0001\u0019\u0003\u0019\u0157\b\u0019\u0001\u0019"+
		"\u0001\u0019\u0001\u0019\u0001\u001a\u0001\u001a\u0005\u001a\u015e\b\u001a"+
		"\n\u001a\f\u001a\u0161\t\u001a\u0001\u001a\u0001\u001a\u0001\u001b\u0001"+
		"\u001b\u0001\u001b\u0005\u001b\u0168\b\u001b\n\u001b\f\u001b\u016b\t\u001b"+
		"\u0001\u001c\u0001\u001c\u0003\u001c\u016f\b\u001c\u0001\u001c\u0001\u001c"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0003\u001d\u0177\b\u001d"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e"+
		"\u0001\u001e\u0001\u001e\u0003\u001e\u0187\b\u001e\u0001\u001e\u0001\u001e"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e"+
		"\u0001\u001e\u0001\u001e\u0005\u001e\u0199\b\u001e\n\u001e\f\u001e\u019c"+
		"\t\u001e\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0005\u001f\u01a2"+
		"\b\u001f\n\u001f\f\u001f\u01a5\t\u001f\u0001\u001f\u0001\u001f\u0001 "+
		"\u0001 \u0001 \u0001 \u0001 \u0000\u0001<!\u0000\u0002\u0004\u0006\b\n"+
		"\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.0246"+
		"8:<>@\u0000\u0006\u0002\u0000\u001f\u001f\"\"\u0002\u0000\u001c\u001c"+
		"4=\u0003\u0000\u001d\u001f\"\"\'\'\u0001\u000023\u0003\u0000\u001d\"$"+
		"$*+\u0003\u0000##%%,1\u01db\u0000E\u0001\u0000\u0000\u0000\u0002U\u0001"+
		"\u0000\u0000\u0000\u0004W\u0001\u0000\u0000\u0000\u0006Z\u0001\u0000\u0000"+
		"\u0000\b`\u0001\u0000\u0000\u0000\nt\u0001\u0000\u0000\u0000\f\u0087\u0001"+
		"\u0000\u0000\u0000\u000e\u0089\u0001\u0000\u0000\u0000\u0010\u009a\u0001"+
		"\u0000\u0000\u0000\u0012\u009d\u0001\u0000\u0000\u0000\u0014\u00af\u0001"+
		"\u0000\u0000\u0000\u0016\u00b1\u0001\u0000\u0000\u0000\u0018\u00ba\u0001"+
		"\u0000\u0000\u0000\u001a\u00c0\u0001\u0000\u0000\u0000\u001c\u00cd\u0001"+
		"\u0000\u0000\u0000\u001e\u00dd\u0001\u0000\u0000\u0000 \u00ea\u0001\u0000"+
		"\u0000\u0000\"\u00ec\u0001\u0000\u0000\u0000$\u0100\u0001\u0000\u0000"+
		"\u0000&\u0109\u0001\u0000\u0000\u0000(\u010b\u0001\u0000\u0000\u0000*"+
		"\u012f\u0001\u0000\u0000\u0000,\u0131\u0001\u0000\u0000\u0000.\u0139\u0001"+
		"\u0000\u0000\u00000\u0150\u0001\u0000\u0000\u00002\u0152\u0001\u0000\u0000"+
		"\u00004\u015b\u0001\u0000\u0000\u00006\u0164\u0001\u0000\u0000\u00008"+
		"\u016c\u0001\u0000\u0000\u0000:\u0172\u0001\u0000\u0000\u0000<\u0186\u0001"+
		"\u0000\u0000\u0000>\u019d\u0001\u0000\u0000\u0000@\u01a8\u0001\u0000\u0000"+
		"\u0000BD\u0003\u0002\u0001\u0000CB\u0001\u0000\u0000\u0000DG\u0001\u0000"+
		"\u0000\u0000EC\u0001\u0000\u0000\u0000EF\u0001\u0000\u0000\u0000F\u0001"+
		"\u0001\u0000\u0000\u0000GE\u0001\u0000\u0000\u0000HV\u0003\u001a\r\u0000"+
		"IV\u0003\u001c\u000e\u0000JV\u0003\"\u0011\u0000KV\u00032\u0019\u0000"+
		"LV\u0003:\u001d\u0000MV\u0003\u0018\f\u0000NV\u0003\u0010\b\u0000OV\u0003"+
		"*\u0015\u0000PV\u0003\u0012\t\u0000QV\u0003\u000e\u0007\u0000RV\u0003"+
		"\u0006\u0003\u0000SV\u0003\b\u0004\u0000TV\u0003\u0004\u0002\u0000UH\u0001"+
		"\u0000\u0000\u0000UI\u0001\u0000\u0000\u0000UJ\u0001\u0000\u0000\u0000"+
		"UK\u0001\u0000\u0000\u0000UL\u0001\u0000\u0000\u0000UM\u0001\u0000\u0000"+
		"\u0000UN\u0001\u0000\u0000\u0000UO\u0001\u0000\u0000\u0000UP\u0001\u0000"+
		"\u0000\u0000UQ\u0001\u0000\u0000\u0000UR\u0001\u0000\u0000\u0000US\u0001"+
		"\u0000\u0000\u0000UT\u0001\u0000\u0000\u0000V\u0003\u0001\u0000\u0000"+
		"\u0000WX\u0005\u000b\u0000\u0000XY\u0005\u0018\u0000\u0000Y\u0005\u0001"+
		"\u0000\u0000\u0000Z[\u0005\u000e\u0000\u0000[\\\u0005\u0012\u0000\u0000"+
		"\\]\u0003<\u001e\u0000]^\u0005\u0013\u0000\u0000^_\u0003\u0016\u000b\u0000"+
		"_\u0007\u0001\u0000\u0000\u0000`a\u0005\t\u0000\u0000ab\u0005\u0012\u0000"+
		"\u0000bc\u0003<\u001e\u0000cd\u0005\u0013\u0000\u0000dh\u0005\u0014\u0000"+
		"\u0000eg\u0003\n\u0005\u0000fe\u0001\u0000\u0000\u0000gj\u0001\u0000\u0000"+
		"\u0000hf\u0001\u0000\u0000\u0000hi\u0001\u0000\u0000\u0000in\u0001\u0000"+
		"\u0000\u0000jh\u0001\u0000\u0000\u0000km\u0003\f\u0006\u0000lk\u0001\u0000"+
		"\u0000\u0000mp\u0001\u0000\u0000\u0000nl\u0001\u0000\u0000\u0000no\u0001"+
		"\u0000\u0000\u0000oq\u0001\u0000\u0000\u0000pn\u0001\u0000\u0000\u0000"+
		"qr\u0005\u0015\u0000\u0000r\t\u0001\u0000\u0000\u0000su\u0003\f\u0006"+
		"\u0000ts\u0001\u0000\u0000\u0000uv\u0001\u0000\u0000\u0000vt\u0001\u0000"+
		"\u0000\u0000vw\u0001\u0000\u0000\u0000w\u007f\u0001\u0000\u0000\u0000"+
		"xz\u0003\u0002\u0001\u0000yx\u0001\u0000\u0000\u0000z}\u0001\u0000\u0000"+
		"\u0000{y\u0001\u0000\u0000\u0000{|\u0001\u0000\u0000\u0000|\u0080\u0001"+
		"\u0000\u0000\u0000}{\u0001\u0000\u0000\u0000~\u0080\u0003\u0016\u000b"+
		"\u0000\u007f{\u0001\u0000\u0000\u0000\u007f~\u0001\u0000\u0000\u0000\u0080"+
		"\u000b\u0001\u0000\u0000\u0000\u0081\u0082\u0005\n\u0000\u0000\u0082\u0083"+
		"\u0003<\u001e\u0000\u0083\u0084\u0005\u0019\u0000\u0000\u0084\u0088\u0001"+
		"\u0000\u0000\u0000\u0085\u0086\u0005\f\u0000\u0000\u0086\u0088\u0005\u0019"+
		"\u0000\u0000\u0087\u0081\u0001\u0000\u0000\u0000\u0087\u0085\u0001\u0000"+
		"\u0000\u0000\u0088\r\u0001\u0000\u0000\u0000\u0089\u008a\u0005\u000f\u0000"+
		"\u0000\u008a\u008e\u0005\u0012\u0000\u0000\u008b\u008f\u0003:\u001d\u0000"+
		"\u008c\u008f\u0003\u0010\b\u0000\u008d\u008f\u0005\u0018\u0000\u0000\u008e"+
		"\u008b\u0001\u0000\u0000\u0000\u008e\u008c\u0001\u0000\u0000\u0000\u008e"+
		"\u008d\u0001\u0000\u0000\u0000\u008f\u0091\u0001\u0000\u0000\u0000\u0090"+
		"\u0092\u0003<\u001e\u0000\u0091\u0090\u0001\u0000\u0000\u0000\u0091\u0092"+
		"\u0001\u0000\u0000\u0000\u0092\u0093\u0001\u0000\u0000\u0000\u0093\u0095"+
		"\u0005\u0018\u0000\u0000\u0094\u0096\u0003<\u001e\u0000\u0095\u0094\u0001"+
		"\u0000\u0000\u0000\u0095\u0096\u0001\u0000\u0000\u0000\u0096\u0097\u0001"+
		"\u0000\u0000\u0000\u0097\u0098\u0005\u0013\u0000\u0000\u0098\u0099\u0003"+
		"\u0014\n\u0000\u0099\u000f\u0001\u0000\u0000\u0000\u009a\u009b\u0003<"+
		"\u001e\u0000\u009b\u009c\u0005\u0018\u0000\u0000\u009c\u0011\u0001\u0000"+
		"\u0000\u0000\u009d\u009e\u0005\u0007\u0000\u0000\u009e\u009f\u0005\u0012"+
		"\u0000\u0000\u009f\u00a0\u0003<\u001e\u0000\u00a0\u00a1\u0005\u0013\u0000"+
		"\u0000\u00a1\u00a4\u0003\u0014\n\u0000\u00a2\u00a3\u0005\b\u0000\u0000"+
		"\u00a3\u00a5\u0003\u0014\n\u0000\u00a4\u00a2\u0001\u0000\u0000\u0000\u00a4"+
		"\u00a5\u0001\u0000\u0000\u0000\u00a5\u0013\u0001\u0000\u0000\u0000\u00a6"+
		"\u00b0\u0003\u0002\u0001\u0000\u00a7\u00ab\u0005\u0014\u0000\u0000\u00a8"+
		"\u00aa\u0003\u0002\u0001\u0000\u00a9\u00a8\u0001\u0000\u0000\u0000\u00aa"+
		"\u00ad\u0001\u0000\u0000\u0000\u00ab\u00a9\u0001\u0000\u0000\u0000\u00ab"+
		"\u00ac\u0001\u0000\u0000\u0000\u00ac\u00ae\u0001\u0000\u0000\u0000\u00ad"+
		"\u00ab\u0001\u0000\u0000\u0000\u00ae\u00b0\u0005\u0015\u0000\u0000\u00af"+
		"\u00a6\u0001\u0000\u0000\u0000\u00af\u00a7\u0001\u0000\u0000\u0000\u00b0"+
		"\u0015\u0001\u0000\u0000\u0000\u00b1\u00b5\u0005\u0014\u0000\u0000\u00b2"+
		"\u00b4\u0003\u0002\u0001\u0000\u00b3\u00b2\u0001\u0000\u0000\u0000\u00b4"+
		"\u00b7\u0001\u0000\u0000\u0000\u00b5\u00b3\u0001\u0000\u0000\u0000\u00b5"+
		"\u00b6\u0001\u0000\u0000\u0000\u00b6\u00b8\u0001\u0000\u0000\u0000\u00b7"+
		"\u00b5\u0001\u0000\u0000\u0000\u00b8\u00b9\u0005\u0015\u0000\u0000\u00b9"+
		"\u0017\u0001\u0000\u0000\u0000\u00ba\u00bc\u0005\r\u0000\u0000\u00bb\u00bd"+
		"\u0003<\u001e\u0000\u00bc\u00bb\u0001\u0000\u0000\u0000\u00bc\u00bd\u0001"+
		"\u0000\u0000\u0000\u00bd\u00be\u0001\u0000\u0000\u0000\u00be\u00bf\u0005"+
		"\u0018\u0000\u0000\u00bf\u0019\u0001\u0000\u0000\u0000\u00c0\u00c5\u0005"+
		"\u0001\u0000\u0000\u00c1\u00c6\u0003\u001c\u000e\u0000\u00c2\u00c6\u0003"+
		"\"\u0011\u0000\u00c3\u00c6\u0003(\u0014\u0000\u00c4\u00c6\u00030\u0018"+
		"\u0000\u00c5\u00c1\u0001\u0000\u0000\u0000\u00c5\u00c2\u0001\u0000\u0000"+
		"\u0000\u00c5\u00c3\u0001\u0000\u0000\u0000\u00c5\u00c4\u0001\u0000\u0000"+
		"\u0000\u00c6\u00c7\u0001\u0000\u0000\u0000\u00c7\u00c9\u0005@\u0000\u0000"+
		"\u00c8\u00ca\u0003>\u001f\u0000\u00c9\u00c8\u0001\u0000\u0000\u0000\u00c9"+
		"\u00ca\u0001\u0000\u0000\u0000\u00ca\u00cb\u0001\u0000\u0000\u0000\u00cb"+
		"\u00cc\u0005\u0018\u0000\u0000\u00cc\u001b\u0001\u0000\u0000\u0000\u00cd"+
		"\u00d2\u0005\u0002\u0000\u0000\u00ce\u00cf\u0005.\u0000\u0000\u00cf\u00d0"+
		"\u00030\u0018\u0000\u00d0\u00d1\u00050\u0000\u0000\u00d1\u00d3\u0001\u0000"+
		"\u0000\u0000\u00d2\u00ce\u0001\u0000\u0000\u0000\u00d2\u00d3\u0001\u0000"+
		"\u0000\u0000\u00d3\u00d4\u0001\u0000\u0000\u0000\u00d4\u00d5\u0005\u0014"+
		"\u0000\u0000\u00d5\u00d6\u0003\u001e\u000f\u0000\u00d6\u00d8\u0005\u0015"+
		"\u0000\u0000\u00d7\u00d9\u0005@\u0000\u0000\u00d8\u00d7\u0001\u0000\u0000"+
		"\u0000\u00d8\u00d9\u0001\u0000\u0000\u0000\u00d9\u00db\u0001\u0000\u0000"+
		"\u0000\u00da\u00dc\u0005\u0018\u0000\u0000\u00db\u00da\u0001\u0000\u0000"+
		"\u0000\u00db\u00dc\u0001\u0000\u0000\u0000\u00dc\u001d\u0001\u0000\u0000"+
		"\u0000\u00dd\u00e2\u0003 \u0010\u0000\u00de\u00df\u0005\u001a\u0000\u0000"+
		"\u00df\u00e1\u0003 \u0010\u0000\u00e0\u00de\u0001\u0000\u0000\u0000\u00e1"+
		"\u00e4\u0001\u0000\u0000\u0000\u00e2\u00e0\u0001\u0000\u0000\u0000\u00e2"+
		"\u00e3\u0001\u0000\u0000\u0000\u00e3\u00e6\u0001\u0000\u0000\u0000\u00e4"+
		"\u00e2\u0001\u0000\u0000\u0000\u00e5\u00e7\u0005\u001a\u0000\u0000\u00e6"+
		"\u00e5\u0001\u0000\u0000\u0000\u00e6\u00e7\u0001\u0000\u0000\u0000\u00e7"+
		"\u001f\u0001\u0000\u0000\u0000\u00e8\u00eb\u0003:\u001d\u0000\u00e9\u00eb"+
		"\u0005@\u0000\u0000\u00ea\u00e8\u0001\u0000\u0000\u0000\u00ea\u00e9\u0001"+
		"\u0000\u0000\u0000\u00eb!\u0001\u0000\u0000\u0000\u00ec\u00ee\u0005\u0003"+
		"\u0000\u0000\u00ed\u00ef\u0005@\u0000\u0000\u00ee\u00ed\u0001\u0000\u0000"+
		"\u0000\u00ee\u00ef\u0001\u0000\u0000\u0000\u00ef\u00f4\u0001\u0000\u0000"+
		"\u0000\u00f0\u00f1\u0005\u0012\u0000\u0000\u00f1\u00f2\u00036\u001b\u0000"+
		"\u00f2\u00f3\u0005\u0013\u0000\u0000\u00f3\u00f5\u0001\u0000\u0000\u0000"+
		"\u00f4\u00f0\u0001\u0000\u0000\u0000\u00f4\u00f5\u0001\u0000\u0000\u0000"+
		"\u00f5\u00f6\u0001\u0000\u0000\u0000\u00f6\u00f8\u0003\u0016\u000b\u0000"+
		"\u00f7\u00f9\u0003>\u001f\u0000\u00f8\u00f7\u0001\u0000\u0000\u0000\u00f8"+
		"\u00f9\u0001\u0000\u0000\u0000\u00f9\u00fb\u0001\u0000\u0000\u0000\u00fa"+
		"\u00fc\u0005\u0018\u0000\u0000\u00fb\u00fa\u0001\u0000\u0000\u0000\u00fb"+
		"\u00fc\u0001\u0000\u0000\u0000\u00fc#\u0001\u0000\u0000\u0000\u00fd\u00ff"+
		"\u0003&\u0013\u0000\u00fe\u00fd\u0001\u0000\u0000\u0000\u00ff\u0102\u0001"+
		"\u0000\u0000\u0000\u0100\u00fe\u0001\u0000\u0000\u0000\u0100\u0101\u0001"+
		"\u0000\u0000\u0000\u0101%\u0001\u0000\u0000\u0000\u0102\u0100\u0001\u0000"+
		"\u0000\u0000\u0103\u010a\u0003\u001a\r\u0000\u0104\u010a\u0003\"\u0011"+
		"\u0000\u0105\u010a\u0003*\u0015\u0000\u0106\u010a\u0003:\u001d\u0000\u0107"+
		"\u010a\u0003\u001c\u000e\u0000\u0108\u010a\u0003(\u0014\u0000\u0109\u0103"+
		"\u0001\u0000\u0000\u0000\u0109\u0104\u0001\u0000\u0000\u0000\u0109\u0105"+
		"\u0001\u0000\u0000\u0000\u0109\u0106\u0001\u0000\u0000\u0000\u0109\u0107"+
		"\u0001\u0000\u0000\u0000\u0109\u0108\u0001\u0000\u0000\u0000\u010a\'\u0001"+
		"\u0000\u0000\u0000\u010b\u010c\u0005\u0004\u0000\u0000\u010c\u010d\u0005"+
		"\u0014\u0000\u0000\u010d\u010e\u0003$\u0012\u0000\u010e\u0110\u0005\u0015"+
		"\u0000\u0000\u010f\u0111\u0005@\u0000\u0000\u0110\u010f\u0001\u0000\u0000"+
		"\u0000\u0110\u0111\u0001\u0000\u0000\u0000\u0111\u0113\u0001\u0000\u0000"+
		"\u0000\u0112\u0114\u0005\u0018\u0000\u0000\u0113\u0112\u0001\u0000\u0000"+
		"\u0000\u0113\u0114\u0001\u0000\u0000\u0000\u0114)\u0001\u0000\u0000\u0000"+
		"\u0115\u0116\u00030\u0018\u0000\u0116\u011c\u0005@\u0000\u0000\u0117\u0119"+
		"\u0005\u0012\u0000\u0000\u0118\u011a\u0003,\u0016\u0000\u0119\u0118\u0001"+
		"\u0000\u0000\u0000\u0119\u011a\u0001\u0000\u0000\u0000\u011a\u011b\u0001"+
		"\u0000\u0000\u0000\u011b\u011d\u0005\u0013\u0000\u0000\u011c\u0117\u0001"+
		"\u0000\u0000\u0000\u011c\u011d\u0001\u0000\u0000\u0000\u011d\u0122\u0001"+
		"\u0000\u0000\u0000\u011e\u011f\u0005\u0016\u0000\u0000\u011f\u0120\u0003"+
		"<\u001e\u0000\u0120\u0121\u0005\u0017\u0000\u0000\u0121\u0123\u0001\u0000"+
		"\u0000\u0000\u0122\u011e\u0001\u0000\u0000\u0000\u0122\u0123\u0001\u0000"+
		"\u0000\u0000\u0123\u0126\u0001\u0000\u0000\u0000\u0124\u0125\u0005\u001c"+
		"\u0000\u0000\u0125\u0127\u0003<\u001e\u0000\u0126\u0124\u0001\u0000\u0000"+
		"\u0000\u0126\u0127\u0001\u0000\u0000\u0000\u0127\u0129\u0001\u0000\u0000"+
		"\u0000\u0128\u012a\u0003>\u001f\u0000\u0129\u0128\u0001\u0000\u0000\u0000"+
		"\u0129\u012a\u0001\u0000\u0000\u0000\u012a\u012b\u0001\u0000\u0000\u0000"+
		"\u012b\u012c\u0005\u0018\u0000\u0000\u012c\u0130\u0001\u0000\u0000\u0000"+
		"\u012d\u012e\u0005\u0006\u0000\u0000\u012e\u0130\u0003*\u0015\u0000\u012f"+
		"\u0115\u0001\u0000\u0000\u0000\u012f\u012d\u0001\u0000\u0000\u0000\u0130"+
		"+\u0001\u0000\u0000\u0000\u0131\u0136\u0003<\u001e\u0000\u0132\u0133\u0005"+
		"\u001a\u0000\u0000\u0133\u0135\u0003<\u001e\u0000\u0134\u0132\u0001\u0000"+
		"\u0000\u0000\u0135\u0138\u0001\u0000\u0000\u0000\u0136\u0134\u0001\u0000"+
		"\u0000\u0000\u0136\u0137\u0001\u0000\u0000\u0000\u0137-\u0001\u0000\u0000"+
		"\u0000\u0138\u0136\u0001\u0000\u0000\u0000\u0139\u013a\u0005@\u0000\u0000"+
		"\u013a\u013c\u0005\u0012\u0000\u0000\u013b\u013d\u0003,\u0016\u0000\u013c"+
		"\u013b\u0001\u0000\u0000\u0000\u013c\u013d\u0001\u0000\u0000\u0000\u013d"+
		"\u013e\u0001\u0000\u0000\u0000\u013e\u013f\u0005\u0013\u0000\u0000\u013f"+
		"/\u0001\u0000\u0000\u0000\u0140\u0141\u0005\u0003\u0000\u0000\u0141\u0151"+
		"\u0005@\u0000\u0000\u0142\u014d\u0005@\u0000\u0000\u0143\u0144\u0005."+
		"\u0000\u0000\u0144\u0149\u0005@\u0000\u0000\u0145\u0146\u0005\u001a\u0000"+
		"\u0000\u0146\u0148\u0005@\u0000\u0000\u0147\u0145\u0001\u0000\u0000\u0000"+
		"\u0148\u014b\u0001\u0000\u0000\u0000\u0149\u0147\u0001\u0000\u0000\u0000"+
		"\u0149\u014a\u0001\u0000\u0000\u0000\u014a\u014c\u0001\u0000\u0000\u0000"+
		"\u014b\u0149\u0001\u0000\u0000\u0000\u014c\u014e\u00050\u0000\u0000\u014d"+
		"\u0143\u0001\u0000\u0000\u0000\u014d\u014e\u0001\u0000\u0000\u0000\u014e"+
		"\u0151\u0001\u0000\u0000\u0000\u014f\u0151\u0005\u0005\u0000\u0000\u0150"+
		"\u0140\u0001\u0000\u0000\u0000\u0150\u0142\u0001\u0000\u0000\u0000\u0150"+
		"\u014f\u0001\u0000\u0000\u0000\u01511\u0001\u0000\u0000\u0000\u0152\u0153"+
		"\u00030\u0018\u0000\u0153\u0154\u0005@\u0000\u0000\u0154\u0156\u0005\u0012"+
		"\u0000\u0000\u0155\u0157\u00036\u001b\u0000\u0156\u0155\u0001\u0000\u0000"+
		"\u0000\u0156\u0157\u0001\u0000\u0000\u0000\u0157\u0158\u0001\u0000\u0000"+
		"\u0000\u0158\u0159\u0005\u0013\u0000\u0000\u0159\u015a\u00034\u001a\u0000"+
		"\u015a3\u0001\u0000\u0000\u0000\u015b\u015f\u0005\u0014\u0000\u0000\u015c"+
		"\u015e\u0003\u0002\u0001\u0000\u015d\u015c\u0001\u0000\u0000\u0000\u015e"+
		"\u0161\u0001\u0000\u0000\u0000\u015f\u015d\u0001\u0000\u0000\u0000\u015f"+
		"\u0160\u0001\u0000\u0000\u0000\u0160\u0162\u0001\u0000\u0000\u0000\u0161"+
		"\u015f\u0001\u0000\u0000\u0000\u0162\u0163\u0005\u0015\u0000\u0000\u0163"+
		"5\u0001\u0000\u0000\u0000\u0164\u0169\u00038\u001c\u0000\u0165\u0166\u0005"+
		"\u001a\u0000\u0000\u0166\u0168\u00038\u001c\u0000\u0167\u0165\u0001\u0000"+
		"\u0000\u0000\u0168\u016b\u0001\u0000\u0000\u0000\u0169\u0167\u0001\u0000"+
		"\u0000\u0000\u0169\u016a\u0001\u0000\u0000\u0000\u016a7\u0001\u0000\u0000"+
		"\u0000\u016b\u0169\u0001\u0000\u0000\u0000\u016c\u016e\u00030\u0018\u0000"+
		"\u016d\u016f\u0007\u0000\u0000\u0000\u016e\u016d\u0001\u0000\u0000\u0000"+
		"\u016e\u016f\u0001\u0000\u0000\u0000\u016f\u0170\u0001\u0000\u0000\u0000"+
		"\u0170\u0171\u0005@\u0000\u0000\u01719\u0001\u0000\u0000\u0000\u0172\u0173"+
		"\u0005@\u0000\u0000\u0173\u0174\u0007\u0001\u0000\u0000\u0174\u0176\u0003"+
		"<\u001e\u0000\u0175\u0177\u0005\u0018\u0000\u0000\u0176\u0175\u0001\u0000"+
		"\u0000\u0000\u0176\u0177\u0001\u0000\u0000\u0000\u0177;\u0001\u0000\u0000"+
		"\u0000\u0178\u0179\u0006\u001e\uffff\uffff\u0000\u0179\u017a\u0007\u0002"+
		"\u0000\u0000\u017a\u0187\u0003<\u001e\u000b\u017b\u0187\u0003.\u0017\u0000"+
		"\u017c\u017d\u0005\u0012\u0000\u0000\u017d\u017e\u0003<\u001e\u0000\u017e"+
		"\u017f\u0005\u0013\u0000\u0000\u017f\u0187\u0001\u0000\u0000\u0000\u0180"+
		"\u0187\u0005@\u0000\u0000\u0181\u0187\u0005?\u0000\u0000\u0182\u0187\u0005"+
		">\u0000\u0000\u0183\u0187\u0005A\u0000\u0000\u0184\u0185\u0007\u0003\u0000"+
		"\u0000\u0185\u0187\u0003<\u001e\u0001\u0186\u0178\u0001\u0000\u0000\u0000"+
		"\u0186\u017b\u0001\u0000\u0000\u0000\u0186\u017c\u0001\u0000\u0000\u0000"+
		"\u0186\u0180\u0001\u0000\u0000\u0000\u0186\u0181\u0001\u0000\u0000\u0000"+
		"\u0186\u0182\u0001\u0000\u0000\u0000\u0186\u0183\u0001\u0000\u0000\u0000"+
		"\u0186\u0184\u0001\u0000\u0000\u0000\u0187\u019a\u0001\u0000\u0000\u0000"+
		"\u0188\u0189\n\r\u0000\u0000\u0189\u018a\u0007\u0004\u0000\u0000\u018a"+
		"\u0199\u0003<\u001e\u000e\u018b\u018c\n\f\u0000\u0000\u018c\u018d\u0007"+
		"\u0005\u0000\u0000\u018d\u0199\u0003<\u001e\r\u018e\u018f\n\u0003\u0000"+
		"\u0000\u018f\u0190\u0005\u001b\u0000\u0000\u0190\u0199\u0003<\u001e\u0004"+
		"\u0191\u0192\n\n\u0000\u0000\u0192\u0193\u0005\u0016\u0000\u0000\u0193"+
		"\u0194\u0003<\u001e\u0000\u0194\u0195\u0005\u0017\u0000\u0000\u0195\u0199"+
		"\u0001\u0000\u0000\u0000\u0196\u0197\n\u0002\u0000\u0000\u0197\u0199\u0007"+
		"\u0003\u0000\u0000\u0198\u0188\u0001\u0000\u0000\u0000\u0198\u018b\u0001"+
		"\u0000\u0000\u0000\u0198\u018e\u0001\u0000\u0000\u0000\u0198\u0191\u0001"+
		"\u0000\u0000\u0000\u0198\u0196\u0001\u0000\u0000\u0000\u0199\u019c\u0001"+
		"\u0000\u0000\u0000\u019a\u0198\u0001\u0000\u0000\u0000\u019a\u019b\u0001"+
		"\u0000\u0000\u0000\u019b=\u0001\u0000\u0000\u0000\u019c\u019a\u0001\u0000"+
		"\u0000\u0000\u019d\u019e\u0005.\u0000\u0000\u019e\u01a3\u0003@ \u0000"+
		"\u019f\u01a0\u0005\u001a\u0000\u0000\u01a0\u01a2\u0003@ \u0000\u01a1\u019f"+
		"\u0001\u0000\u0000\u0000\u01a2\u01a5\u0001\u0000\u0000\u0000\u01a3\u01a1"+
		"\u0001\u0000\u0000\u0000\u01a3\u01a4\u0001\u0000\u0000\u0000\u01a4\u01a6"+
		"\u0001\u0000\u0000\u0000\u01a5\u01a3\u0001\u0000\u0000\u0000\u01a6\u01a7"+
		"\u00050\u0000\u0000\u01a7?\u0001\u0000\u0000\u0000\u01a8\u01a9\u0005@"+
		"\u0000\u0000\u01a9\u01aa\u0005\u001c\u0000\u0000\u01aa\u01ab\u0003<\u001e"+
		"\u0000\u01abA\u0001\u0000\u0000\u00004EUhnv{\u007f\u0087\u008e\u0091\u0095"+
		"\u00a4\u00ab\u00af\u00b5\u00bc\u00c5\u00c9\u00d2\u00d8\u00db\u00e2\u00e6"+
		"\u00ea\u00ee\u00f4\u00f8\u00fb\u0100\u0109\u0110\u0113\u0119\u011c\u0122"+
		"\u0126\u0129\u012f\u0136\u013c\u0149\u014d\u0150\u0156\u015f\u0169\u016e"+
		"\u0176\u0186\u0198\u019a\u01a3";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}