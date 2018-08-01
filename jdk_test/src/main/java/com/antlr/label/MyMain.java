package com.antlr.label;

import java.io.FileInputStream;
import java.io.InputStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class MyMain {
    public static void main(String[] args) throws Exception {
        String inputFile = "/Users/lcc/IdeaProjects/JdkSource/src/main/java/com/antlr/label/data.ini";                      //文件读取数据
        InputStream is = System.in;

        if ( inputFile!=null ) is = new FileInputStream(inputFile);
        ANTLRInputStream input = new ANTLRInputStream(is);

        CalExprLexer lexer = new CalExprLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CalExprParser parser = new CalExprParser(tokens);
        ParseTree tree = parser.prog();                      // 生成语法树
        MyVisitor visitor = new MyVisitor();
        visitor.visit(tree);
    }
}
