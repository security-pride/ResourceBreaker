package com.userxxx;

import com.userxxx.cfg.CallGraphHandler;
import com.userxxx.parser.AndroidResourceLexer;
import com.userxxx.parser.AndroidResourceParser;
import com.userxxx.parser.CPP14Lexer;
import com.userxxx.parser.CPP14Parser;
import com.userxxx.symbolic.executor.MainExecutor;
import com.userxxx.symbolic.parser.MainHandler;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Main {

    private static void ExecSyn() throws IOException {
        InputStream input = Main.class.getClassLoader().getResourceAsStream("AndroidResource.bt");
        CharStream charStream = CharStreams.fromStream(input);
        AndroidResourceLexer lexer = new AndroidResourceLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        AndroidResourceParser parser = new AndroidResourceParser(tokens);
        ParseTree tree = parser.file();

        MainHandler handler = new MainHandler();

        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(handler, tree);

        MainExecutor executor = new MainExecutor(tree, handler);
        executor.Exec();
    }

    public static void detectLoop() throws IOException {
        InputStream input = Main.class.getClassLoader().getResourceAsStream("VectorDrawable.cpp");
        if (input == null) throw new FileNotFoundException("VectorDrawable.cpp not found in resources");
        CharStream charStream = CharStreams.fromStream(input);
        CPP14Lexer lexer = new CPP14Lexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CPP14Parser parser = new CPP14Parser(tokens);
        CallGraphHandler handler = new CallGraphHandler();
        new ParseTreeWalker().walk(handler, parser.translationUnit());
        handler.getCallGraph().printAllLoops();
    }

    public static void main(String[] args) throws IOException {
        ExecSyn();
        detectLoop();
    }
}