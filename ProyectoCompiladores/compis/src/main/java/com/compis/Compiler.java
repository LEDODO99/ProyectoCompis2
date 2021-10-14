package com.compis;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.compis.clases.Method;
import com.compis.clases.MipsConverter;
import com.compis.clases.Struct;
import com.compis.clases.SymbolTable;
import com.compis.clases.Variable;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Compiler extends DECAFBaseListener {
    private SymbolTable symbolTable;
    private int errorCount=0;
    private String returnMessage;
    private boolean noErrors = true;
    private boolean passedReturn = false;
    private String currentMethodType;
    private String currentMethodName;
    private MipsConverter mipsConverter = new MipsConverter();

    public Compiler() {

    }

    public String Compile(String program) {
        returnMessage = "";
        symbolTable = new SymbolTable();
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
        String ctxName = ctx.ID().getText();
        String ctxType = ctx.varType().getText();
        //symbolTables.add(new SymbolTable(parentTreeIndex, parentTreeIndexMinus1, 0));
        boolean variableAdded = symbolTable.addVariable(ctxName, ctxType);
        boolean isArray=false;
        if (!variableAdded) {
            noErrors = false;
            errorCount++;
            returnMessage = returnMessage + errorCount +". Variable Name \"" + ctxName + "\" is already in use in its scope. \n";
        }
        try {
            int arraySize=Integer.parseInt(ctx.NUM().getText());
            isArray=true;
            if (0 >= arraySize) {
                noErrors = false;
                errorCount++;
                returnMessage = returnMessage + errorCount +". Variable \"" + ctx.ID().getText()
                        + "\" is declared as an array with size of 0 or less. \n";
            }else{
                symbolTable.serVarAsArray(ctxName, arraySize);
                if (ctxType.contains("struct"))
                {
                    Struct tempStruct = symbolTable.getStructByName(ctxType.substring(6));
                    ArrayList<Variable> tempvariables=tempStruct.getAtributes();
                    for (int i=0; i<arraySize;i++){
                        for (int j=0;j<tempvariables.size();j++){
                            structRecursive(tempvariables.get(j),ctxName+i);
                        }
                    }
                }else{
                    mipsConverter.addVariable(ctxName, symbolTable.getScopeCurrent(), ctxType, true, Integer.parseInt(ctx.NUM().getText()));
                }
            }
        } catch (Exception e) {
            // nada
        }
        if(!isArray){
        if (ctxType.contains("struct"))
        {
            Struct tempStruct = symbolTable.getStructByName(ctxType.substring(6));
            ArrayList<Variable> tempvariables=tempStruct.getAtributes();
            for (int i=0;i<tempvariables.size();i++){
                structRecursive(tempvariables.get(i),ctxName+symbolTable.getScopeCurrent());
            }
        }
        else
        {
            mipsConverter.addVariable(ctxName, symbolTable.getScopeCurrent(), ctxType, false, 0);
        }}

    }
    public void structRecursive(Variable variableToAdd ,String nameo){
        variableToAdd=symbolTable.getVariableInScope(variableToAdd.getScopeCurrent(), variableToAdd.getName());
        if (variableToAdd.getType().contains("struct")){
            Struct tempStruct = symbolTable.getStructByName(variableToAdd.getType().substring(6));
            ArrayList<Variable> tempvariables=tempStruct.getAtributes();
            if (variableToAdd.getIsArray()){
                for (int i=0; i<variableToAdd.getArraySize();i++){
                    for (int j=0;j<tempvariables.size();j++){
                        structRecursive(tempvariables.get(j),nameo+variableToAdd.getName()+i);
                    }
                }
            }else{
                for (int i=0;i<tempvariables.size();i++){
                    structRecursive(tempvariables.get(i),nameo+variableToAdd.getName()+symbolTable.getScopeCurrent());
                }
            }
        }else{
            ArrayList<Variable> variables = symbolTable.getAllVariables();
            for (int i=0; i<variables.size();i++){
            }
            mipsConverter.addVariable(nameo+variableToAdd.getName(), symbolTable.getScopeCurrent(), variableToAdd.getType(), variableToAdd.getIsArray(), variableToAdd.getArraySize());
        }
    }
    @Override
    public void enterMethodDeclaration(DECAFParser.MethodDeclarationContext ctx) {
        int parentTreeIndex = 0;
        String ctxName = ctx.ID().getText();
        String ctxType = ctx.methodType().getText();
        currentMethodType = ctxType;
        int parentTreeIndexMinus1 = parentTreeIndex - 1;
        if (parentTreeIndexMinus1 < 0)
            parentTreeIndexMinus1 = 0;
        //symbolTables.add(new SymbolTable(parentTreeIndex, parentTreeIndexMinus1, 0));
        boolean methodAdded = symbolTable.addMethod(ctxName, ctxType);
        if (!methodAdded) {
            noErrors = false;
            errorCount++;
            returnMessage = returnMessage + errorCount +". Method Name \"" + ctxName + "\" is already in use. \n";
        }
        try{
            ctx.parameter().toString();
            int parameterQuant = ctx.parameter().size();
            for (int i=0; i<parameterQuant;i++){
                symbolTable.addParameter(ctx.parameter(i).ID().getText(), ctx.parameter(i).parameterType().getText());
            }
        }
        catch (Exception e){
        }
        currentMethodName=ctxName;
        mipsConverter.enterMethod(ctxName, symbolTable.getScopeBefore());
    }
    @Override
    public void enterStructDeclaration(DECAFParser.StructDeclarationContext ctx) {
        int varQuant = ctx.varDeclaration().size();
        ArrayList<String> listOfNames = new ArrayList<>();
        ArrayList<String> lsitOfTypes = new ArrayList<>();
        for (int i=0;i<varQuant;i++){
            listOfNames.add(ctx.varDeclaration(i).ID().getText());
            lsitOfTypes.add(ctx.varDeclaration(i).varType().getText());
        }
        String ctxName = ctx.ID().getText();
        //symbolTables.add(new SymbolTable(parentTreeIndex, parentTreeIndexMinus1, 0));
        boolean structAdded = symbolTable.addStruct(ctxName, null, listOfNames,lsitOfTypes);
        if (!structAdded) {
            noErrors = false;
            errorCount++;
            returnMessage = returnMessage + errorCount +". Struct Name \"" + ctxName + "\" is already in use. \n";
        }
        mipsConverter.addStruct(ctxName, symbolTable.getScopeBefore());
    }
    @Override
    public void exitStructDeclaration(DECAFParser.StructDeclarationContext ctx){
        symbolTable.goUpScope();
        mipsConverter.exitStruct();
    }
    @Override
    public void exitMethodDeclaration(DECAFParser.MethodDeclarationContext ctx){
        if(passedReturn==false && !currentMethodType.equals("void")){
            noErrors = false;
            errorCount++;
            returnMessage = returnMessage + errorCount +". Method \"" + currentMethodName + "\" expects "+currentMethodType+" but no return statement detected. \n";
        }
        currentMethodType=null;
        passedReturn=false;
        currentMethodName = null;
        symbolTable.goUpScope();
        mipsConverter.exitMethod();
    }
    @Override
    public void enterReturnStatement(DECAFParser.ReturnStatementContext ctx){
        if(currentMethodType!=null){
            passedReturn=true;
            if (currentMethodType.equals("void")){
                noErrors=false;
                errorCount++;
                returnMessage = returnMessage + errorCount +". Void method \"" + currentMethodName +"\" contains return statement\n"; 
            }else{
                try{
                    String methodName = ctx.expression().methodCall().ID().getText();
                    String returnType = recursiveExpressionType(ctx.expression(), symbolTable.getScopeCurrent());
                    if (returnType==null){
                        noErrors=false;
                        errorCount++;
                        returnMessage = returnMessage + errorCount +". Method \"" + currentMethodName +"\" referenced before declaration\n";
                    }else{
                        boolean equals = returnType.equals(currentMethodType);
                        if (!equals){
                            noErrors=false;
                            errorCount++;
                            returnMessage = returnMessage + errorCount +". Return type for \"" + currentMethodName +"\" and \""+methodName+ "\" not the same\n";
                        }
                    }
                }catch(Exception e){
                }
            }
        }
    }
    @Override
    public void enterBlock(DECAFParser.BlockContext ctx){
        symbolTable.addScope();
    }
    @Override
    public void exitBlock(DECAFParser.BlockContext ctx){
        symbolTable.goUpScope();
    }
    @Override
    public void enterIfStatement(DECAFParser.IfStatementContext ctx){
        String i = recursiveExpressionType(ctx.expression(),symbolTable.getScopeCurrent());
        if (i==null){
            noErrors=false;
            errorCount++;
            returnMessage = returnMessage + errorCount +" " +ctx.expression().getText()+" is not boolean\n";
        }
        else if (!i.equals("boolean")){
            noErrors=false;
            errorCount++;
            returnMessage = returnMessage + errorCount +" "+ctx.expression().getText()+" is not boolean\n";
        }
    }
    @Override
    public void enterWhileStatement(DECAFParser.WhileStatementContext ctx){
        String i = recursiveExpressionType(ctx.expression(),symbolTable.getScopeCurrent());
        if (i==null){
            noErrors=false;
            errorCount++;
            returnMessage = returnMessage + errorCount +" "+ctx.expression().getText()+" is not boolean\n";
        }
        if (!i.equals("boolean")){
            noErrors=false;
            errorCount++;
            returnMessage = returnMessage + errorCount +" "+ctx.expression().getText()+" is not boolean\n";
        }
    }
    @Override
    public void exitProgram(DECAFParser.ProgramContext ctx){
        ArrayList<Variable> variables=symbolTable.getAllVariables();
        ArrayList<Method> methods=symbolTable.getAllMethods();
        ArrayList<Struct> structs=symbolTable.getAllStructs();
        for (int i=0; i<variables.size();i++){
        }
        boolean hasMainMethod=symbolTable.getMethodInScope(0,"main")!=null;
        if (!hasMainMethod){
            noErrors=false;
            errorCount++;
            returnMessage = returnMessage + errorCount +" No main method\n";
        }
        if (noErrors){
            String MIPSProgram = mipsConverter.finishUp();
            try{
                BufferedWriter writer = new BufferedWriter(new FileWriter("mipsCompilado.asm"));
                writer.write(MIPSProgram);
                
                writer.close();
            }catch(IOException ex){
            }
        }

    }
    @Override
    public void enterMethodCall(DECAFParser.MethodCallContext ctx){
        int argAmount = ctx.arg().size();
        String methodName = ctx.ID().getText();
        Method returnMethod = symbolTable.getMethodByName(methodName);
        if (returnMethod==null){
            noErrors = false;
            errorCount++;
            returnMessage = returnMessage + errorCount +". " +methodName+" used before Declaration\n";
        }else{
            ArrayList<Variable> paramListToFulfil = returnMethod.getParameters();
            if (paramListToFulfil.size()==argAmount){
                for (int i=0; i<argAmount;i++){
                    if (!recursiveExpressionType(ctx.arg(i).expression(), symbolTable.getScopeCurrent()).equals(paramListToFulfil.get(i).getType())){
                        noErrors = false;
                        errorCount++;
                        returnMessage = returnMessage + errorCount +". Parameter types when calling "+methodName+" incorrect\n";
                    }
                }
            }else{
                noErrors = false;
                errorCount++;
                returnMessage = returnMessage + errorCount +". Parameter amount when calling "+methodName+" incorrect\n";
            }
        }
    }
    private String recursiveLocationType(DECAFParser.LocationContext ctx,int scope){
        String locationName = ctx.ID().getText();
        Variable returnVariable = symbolTable.getVariableInScope(scope, locationName);
        if(returnVariable == null){
            return null;
        }
        String varType = returnVariable.getType();
        //expression is int
        try{
            DECAFParser.ExpressionContext tempctx = ctx.expression();
            
            tempctx.getText();
            if (!recursiveExpressionType(tempctx,scope).equals("int")){
                return null;
            }
        }catch (Exception e){
        }
        //location exists
        try{
            DECAFParser.LocationContext tempctx= ctx.location();
            tempctx.getText();
            if (!varType.contains("struct")){
                return null;
            }else{
                String structName = varType.substring(6);
                Struct tempStruct = symbolTable.getStructInScope(scope,structName);
                int newScope = tempStruct.getInsideScope();
                return (recursiveLocationType(tempctx, newScope));
            }
        }catch (Exception e){
        }

        return varType;
    }
    private String MethodCallType(DECAFParser.MethodCallContext ctx){
        String methodName = ctx.ID().getText();
        Method returnMethod = symbolTable.getMethodByName(methodName);
        if (returnMethod==null){
            return null;
        }else{
            return returnMethod.getType();
        }
    }
    private String operationType(String type1, String type2, DECAFParser.OpContext ctx){
        if (type1==null || type2==null)
            return null;
        if (!type1.equals(type2)){
            return null;
        }
        else{
            if (type1.equals("boolean")){
                try{
                    DECAFParser.Eq_opContext eqTempCtx = ctx.eq_op();
                    eqTempCtx.toString();
                    return "boolean";
                }catch (Exception e){
                }
                try{
                    DECAFParser.Cond_opContext condTempCtx = ctx.cond_op();
                    condTempCtx.toString();
                    return "boolean";
                }catch (Exception e){
                }
            }else if(type1.equals("int")){
                try{
                    DECAFParser.Rel_opContext relTempCtx = ctx.rel_op();
                    relTempCtx.toString();
                    return "boolean";
                }catch (Exception e){
                }
                try{
                    DECAFParser.Eq_opContext eqTempCtx = ctx.eq_op();
                    eqTempCtx.toString();
                    return "boolean";
                }catch (Exception e){
                }
                try{
                    DECAFParser.Arith_opContext arithTempCtx = ctx.arith_op();
                    arithTempCtx.toString();
                    return "int";
                }catch (Exception e){
                }
            }else{
                try{
                    DECAFParser.Eq_opContext eqTempCtx = ctx.eq_op();
                    eqTempCtx.toString();
                    return "boolean";
                }catch (Exception e){
                }
                try{
                    DECAFParser.Rel_opContext relTempCtx = ctx.rel_op();
                    relTempCtx.toString();
                    return "boolean";
                }catch (Exception e){
                }
            }
            return null;
        }
    }
    private String literalType(DECAFParser.LiteralContext ctx){
        try{
            DECAFParser.Int_literalContext tempIntCtx = ctx.int_literal();
            tempIntCtx.toString();
            return "int";
        }catch (Exception e){
        }
        try{
            DECAFParser.Char_literalContext tempIntCtx = ctx.char_literal();
            tempIntCtx.toString();
            return "char";
        }catch (Exception e){
        }
        try{
            DECAFParser.Bool_literalContext tempIntCtx = ctx.bool_literal();
            tempIntCtx.toString();
            return "boolean";
        }catch (Exception e){
        }
        return null;
    }
    private String recursiveExpressionType(DECAFParser.ExpressionContext ctx,int scope){
        //location
        try{
            DECAFParser.LocationContext tempctx = ctx.location();
            tempctx.toString();
            return recursiveLocationType(tempctx, symbolTable.getScopeCurrent());
        }catch(Exception e){
        }
        //methodCall
        try{
            DECAFParser.MethodCallContext tempctx = ctx.methodCall();
            tempctx.toString();
            return MethodCallType(tempctx);
        }catch(Exception e){
        }
        //literal
        try{
            DECAFParser.LiteralContext tempLiteralctx = ctx.literal();
            tempLiteralctx.toString();

            return literalType(tempLiteralctx);
        }catch(Exception e){

        }
        //operator
        try{
            DECAFParser.OpContext tempOpCtx= ctx.op();
            tempOpCtx.toString();
            String type1=recursiveExpressionType(ctx.expression(0), scope);
            String type2=recursiveExpressionType(ctx.expression(1), scope);
            return operationType(type1, type2, tempOpCtx);
            
        }catch(Exception e){
        }
        //expression
        try{
            String expresion = ctx.getText();
            if(expresion.charAt(0)=='!'){
                if (recursiveExpressionType(ctx.expression(0), scope).equals("boolean")){
                    return "boolean";
                }else {
                    return null;
                }
            }else if(expresion.charAt(0)=='-'){
                if (recursiveExpressionType(ctx.expression(0), scope).equals("int")){
                    return "int";
                }else {
                    return null;
                }
            }else {
                return recursiveExpressionType(ctx.expression(0),scope);
            }
        }catch(Exception e){
        }
        return null;
    } 
}
