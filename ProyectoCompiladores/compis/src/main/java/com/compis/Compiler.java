package com.compis;

import java.util.ArrayList;

import com.compis.clases.SymbolTable;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Compiler extends DECAFBaseListener {
    private ArrayList<SymbolTable> symbolTables;
    private ArrayList<ParseTree> parentTrees;
    private String returnMessage;
    private boolean noErrors = true;

    public Compiler() {

    }

    public String Compile(String program) {
        returnMessage = "";
        symbolTables = new ArrayList<>();
        parentTrees = new ArrayList<>();
        DECAFLexer lexer = new DECAFLexer(CharStreams.fromString(program));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DECAFParser parser = new DECAFParser(tokens);
        ParseTree tree = parser.program();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(this, tree);
        if (noErrors) {
            returnMessage = "No Errors";
        } else {
            returnMessage = "Errors:\n" + returnMessage;
        }

        return returnMessage;
    }

    @Override
    public void enterVarDeclaration(DECAFParser.VarDeclarationContext ctx) {
        ParseTree parentTree = ctx.getParent();
        int parentTreeIndex = 0;
        String ctxName = ctx.ID().getText();
        String ctxType = ctx.varType().getText();
        if (parentTrees.contains(parentTree)) {
            parentTreeIndex = parentTrees.indexOf(parentTree);
            System.out.println("parentTreeIndex: "+parentTreeIndex);
            boolean variableAdded = symbolTables.get(parentTreeIndex).addVariable(ctxName, ctxType);
            if (!variableAdded) {
                noErrors = false;
                returnMessage = returnMessage + "Variable Name \"" + ctxName + "\" is already in use in su ambito. \n";
            }
            try {
                if (0 >= Integer.parseInt(ctx.NUM().getText())) {
                    noErrors = false;
                    returnMessage = returnMessage + "Variable \"" + ctx.ID().getText()
                            + "\" is declared as an array with size of 0 or less. \n";
                }
            } catch (Exception e) {
                // nada
            }
        } else {
            parentTreeIndex = parentTrees.size();
            System.out.println("parentTreeIndex: "+parentTreeIndex);
            int parentTreeIndexMinus1 = parentTreeIndex - 1;
            System.out.println("parentTreeIndexMinus1: "+parentTreeIndexMinus1);
            if (parentTreeIndexMinus1 < 0)
                parentTreeIndexMinus1 = 0;
            symbolTables.add(new SymbolTable(parentTreeIndex, parentTreeIndexMinus1, 0));
            boolean variableAdded = symbolTables.get(parentTreeIndex).addVariable(ctxName, ctxType);
            if (!variableAdded) {
                noErrors = false;
                returnMessage = returnMessage + "Variable Name \"" + ctxName + "\" is already in use in su ambito. \n";
            }
            try {
                if (0 >= Integer.parseInt(ctx.NUM().getText())) {
                    noErrors = false;
                    returnMessage = returnMessage + "Variable \"" + ctx.ID().getText()
                            + "\" is declared as an array with size of 0 or less. \n";
                }
            } catch (Exception e) {
                // nada
            }
        }
        System.out.println("\nparse Tree index: "+parentTrees.toString());

    }

    @Override
    public void enterMethodDeclaration(DECAFParser.MethodDeclarationContext ctx) {
        ParseTree parentTree = ctx.getParent();
        int parentTreeIndex = 0;
        String ctxName = ctx.ID().getText();
        String ctxType = ctx.methodType().getText();
        if (parentTrees.contains(parentTree)) {
            parentTreeIndex = parentTrees.indexOf(parentTree);

            boolean methodAdded = symbolTables.get(parentTreeIndex).addMethod(ctxName, ctxType);
            if (!methodAdded) {
                noErrors = false;
                returnMessage = returnMessage + "Method Name \"" + ctxName + "\" is already in use. \n";
            }
        } else {
            parentTreeIndex = parentTrees.size();
            int parentTreeIndexMinus1 = parentTreeIndex - 1;
            if (parentTreeIndexMinus1 < 0)
                parentTreeIndexMinus1 = 0;
            symbolTables.add(new SymbolTable(parentTreeIndex, parentTreeIndexMinus1, 0));
            boolean methodAdded = symbolTables.get(parentTreeIndex).addMethod(ctxName, ctxType);
            if (!methodAdded) {
                noErrors = false;
                returnMessage = returnMessage + "Method Name \"" + ctxName + "\" is already in use. \n";
            }
        }
        System.out.println("\nparse Tree index: "+parentTrees.toString());
    }
    @Override
    public void enterStructDeclaration(DECAFParser.StructDeclarationContext ctx) {
        ParseTree parentTree = ctx.getParent();
        int parentTreeIndex = 0;
        String ctxName = ctx.ID().getText();
        if (parentTrees.contains(parentTree)) {
            parentTreeIndex = parentTrees.indexOf(parentTree);

            boolean methodAdded = symbolTables.get(parentTreeIndex).addStruct(ctxName, null);
            if (!methodAdded) {
                noErrors = false;
                returnMessage = returnMessage + "Struct Name \"" + ctxName + "\" is already in use. \n";
            }
        } else {
            parentTreeIndex = parentTrees.size();
            int parentTreeIndexMinus1 = parentTreeIndex - 1;
            if (parentTreeIndexMinus1 < 0)
                parentTreeIndexMinus1 = 0;
            symbolTables.add(new SymbolTable(parentTreeIndex, parentTreeIndexMinus1, 0));
            boolean methodAdded = symbolTables.get(parentTreeIndex).addStruct(ctxName, null);
            if (!methodAdded) {
                noErrors = false;
                returnMessage = returnMessage + "Struct Name \"" + ctxName + "\" is already in use. \n";
            }
        }
        System.out.println("\nparse Tree index: "+parentTrees.toString());
    }

}
