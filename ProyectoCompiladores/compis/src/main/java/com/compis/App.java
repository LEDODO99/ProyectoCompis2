package com.compis;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;


/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        //System.out.println("Hello World!");
        String prog="class Program { struct A { int a; }; struct B { int b[5]; struct A c; }; struct A y; struct A z; void main(void) { struct B y[5]; int i; int j; int k; i = 0; j=0; z.a = 3; while(i<=10) { y[j].b[0]=InputInt(); if(y[j].b[0]==5) { y[j].b[0]=z.a; k=factorial(ReturnNumber()); OutputInt(k); } y[j].c.a=factorial(y[j].b[0]); OutputInt(y[j].c.a); i = i + 1; } } int factorial(int n) { if (n==0) {return 1;} else {return n*factorial(n-1);} } void OutputInt(int n) { } int InputInt(void) {return 0;} int ReturnNumber(void) {return z.a;} }";
        
        DECAFLexer lexer=new DECAFLexer(CharStreams.fromString(prog));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DECAFParser parser = new DECAFParser(tokens);
        ParseTree tree = parser.program();
        System.out.println(tree.toStringTree(parser));
    }
    
}
