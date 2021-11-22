package com.compis.clases;

import java.util.ArrayList;

import com.compis.DECAFParser;

public class MipsConverter {

    private int registerOcupationDepth=0;
    private boolean inMain=false;
    private boolean inStruct=false;
    private String dataDeclaration="";
    private String textDeclaration="";
    private ArrayList<String> structs = new ArrayList<>();

    private boolean isCallingFunc=false;
    private String methodBeingCalled=null;
    private int currentCallOfster=0;

    private String currentMethod=null;

    private ArrayList<String> ifs = new ArrayList<>();
    private ArrayList<String> whiles = new ArrayList<>();
    private ArrayList<Integer> ifNumbers = new ArrayList<>();
    private ArrayList<Integer> whileNumbers = new ArrayList<>();
    private ArrayList<Integer> ifScopes = new ArrayList<>();
    private ArrayList<Integer> whileScopes = new ArrayList<>();

    public MipsConverter(){
        whileNumbers.add(0);
        ifNumbers.add(0);
    }
    
    public void addAssigment(DECAFParser.AssignmentContext ctx, int scope, SymbolTable symbolTable){
        //save expression to register
        recursiveExpressionWriter(ctx.expression(), symbolTable, scope);
        //save register to variable
        String variableName = ctx.location().ID().getText();
        Variable tempVariable = symbolTable.getVariableInScope(scope, variableName);
        String type = tempVariable.getType();
        String saveLine="";
        if(tempVariable.isGlobal){
            saveLine=" $t"+registerOcupationDepth+", "+variableName;
        }else{
            saveLine=" $t"+registerOcupationDepth+", "+symbolTable.getMethodByName(currentMethod).getVariableOffset(variableName)+"($sp)";
        }
        if (type.equals("int")){
            saveLine="sw"+saveLine;
            //Checking if array
            try{
                ctx.location().expression().getText();
                registerOcupationDepth++;
                recursiveExpressionWriter(ctx.location().expression(), symbolTable, scope);
                registerOcupationDepth--;
                textDeclaration = textDeclaration+"\tmul $t"+(registerOcupationDepth+1)+", $t"+(registerOcupationDepth+1)+", 4\n";
                if(tempVariable.isGlobal)
                    saveLine=saveLine+"($t"+(registerOcupationDepth+1)+")";
                else{
                    textDeclaration = textDeclaration + "\tadd $t"+(registerOcupationDepth+1)+", $t"+(registerOcupationDepth+1)+", $sp\n";
                    saveLine = saveLine.replace("$sp", "$t"+(registerOcupationDepth+1));
                }
            }catch (Exception e){

            }
        }else if (type.contains("struct")){
            Struct tempStruct=symbolTable.getStructInScope(scope, type.substring(6));
            String attribute = ctx.location().location().ID().getText();
            int offset = 0;
            for (int i=0;i<tempStruct.getAtributes().size();i++){
                if(!tempStruct.getAtributes().get(i).getName().equals(attribute)){
                    if(tempStruct.getAtributes().get(i).getType().equals("int")){
                        if (tempStruct.getAtributes().get(i).isArray)
                            offset+=(4*tempStruct.getAtributes().get(i).arraySize);
                        else    
                            offset+=4;
                    }else if(tempStruct.getAtributes().get(i).getType().contains("struct")){
                        if (tempStruct.getAtributes().get(i).isArray)
                            offset+=(symbolTable.getStructInScope(tempVariable.scopeCurrent, tempStruct.getAtributes().get(i).getType().substring(6)).getMemorySize()*tempStruct.getAtributes().get(i).arraySize);
                        else    
                            offset+=symbolTable.getStructInScope(tempVariable.scopeCurrent, tempStruct.getAtributes().get(i).getType().substring(6)).getMemorySize();
                    }else{
                        if (tempStruct.getAtributes().get(i).isArray)
                            offset+=(1*tempStruct.getAtributes().get(i).arraySize);
                        else    
                            offset+=1;
                    }
                }else{
                    if(tempStruct.getAtributes().get(i).getType().equals("int")){
                        //Checking if attribute is array
                        try{
                            int position = Integer.parseInt(ctx.location().location().expression().getText());
                            offset+=(position*4);
                        }catch(Exception e){
            
                        }
                        if(tempVariable.isGlobal){
                            saveLine="sw $t"+registerOcupationDepth+", "+variableName+"+"+offset;
                        }else{
                            saveLine="sw $t"+registerOcupationDepth+", "+(offset+symbolTable.getMethodByName(currentMethod).getVariableOffset(variableName))+"($sp)";
                        }
                    }else{
                        try{
                            int position = Integer.parseInt(ctx.location().location().expression().getText());
                            offset+=(position);
                        }catch(Exception e){
            
                        }
                        if(tempVariable.isGlobal){
                            saveLine="sb $t"+registerOcupationDepth+", "+variableName+"+"+offset;
                        }else{
                            saveLine="sb $t"+registerOcupationDepth+", "+(offset+symbolTable.getMethodByName(currentMethod).getVariableOffset(variableName))+"($sp)";
                        }
                    }
                    break;
                }
            }
            //Checking if array
            try{
                ctx.expression().getText();
                registerOcupationDepth++;
                recursiveExpressionWriter(ctx.expression(), symbolTable, scope);
                registerOcupationDepth--;
                textDeclaration = textDeclaration+"\tmul $t"+(registerOcupationDepth+1)+", $t"+(registerOcupationDepth+1)+", "+tempStruct.getMemorySize()+"\n";
                if(tempVariable.isGlobal)
                    saveLine=saveLine+"($t"+(registerOcupationDepth+1)+")";
                else{
                    textDeclaration = textDeclaration + "\tadd $t"+(registerOcupationDepth+1)+", $t"+(registerOcupationDepth+1)+", $sp\n";
                    saveLine = saveLine.replace("$sp", "$t"+(registerOcupationDepth+1));
                }
            }catch (Exception e){

            }
            
        }else{
            saveLine="sb"+saveLine;
            //Checking if array
            try{
                Integer.parseInt(ctx.location().expression().getText());
                registerOcupationDepth++;
                recursiveExpressionWriter(ctx.expression(), symbolTable, scope);
                registerOcupationDepth--;
                textDeclaration = textDeclaration+"\tmul $t"+(registerOcupationDepth+1)+", $t"+(registerOcupationDepth+1)+", 1\n";
                if(tempVariable.isGlobal)
                    saveLine=saveLine+"($t"+(registerOcupationDepth+1)+")";
                else{
                    textDeclaration = textDeclaration + "\tadd $t"+(registerOcupationDepth+1)+", $t"+(registerOcupationDepth+1)+", $sp\n";
                    saveLine = saveLine.replace("$sp", "$t"+(registerOcupationDepth+1));
                }
            }catch (Exception e){

            }
        }
        textDeclaration = textDeclaration+"\t"+saveLine+"\n";
    }

    

    public void addVariable(String variableName, int scope, String type, boolean isArray, int arraySize, int scopeMemorySize){
        if(!inStruct){
            if (scope==0){
                String returnValue = variableName +": "+getMipsVarType(type);
                if(type.contains("struct")){
                    if (isArray){
                        returnValue = variableName+": .space "+(scopeMemorySize*arraySize);
                    }else{
                        returnValue = variableName+": .space "+scopeMemorySize;
                    }
                    dataDeclaration = dataDeclaration+returnValue+"\n";
                }else{
                    if (isArray){
                        for (int i=1; i<arraySize;i++){
                            returnValue = returnValue + ", "+getEmptyVarValue(type);
                        }
                        dataDeclaration = dataDeclaration + returnValue + "\n";
                    }else{
                        dataDeclaration = dataDeclaration + returnValue + "\n";
                    }
                }
            }else{
                if (type.contains("struct")){
                    if(isArray){
                        textDeclaration=textDeclaration+"\taddi $sp, $sp -"+(scopeMemorySize*arraySize)+"\n";
                    }else{
                        textDeclaration=textDeclaration+"\taddi $sp, $sp -"+scopeMemorySize+"\n";
                    }
                }else if(type.equals("int")){
                    if(isArray){
                        textDeclaration=textDeclaration+"\taddi $sp, $sp -"+(4*arraySize)+"\n";
                    }else{
                        textDeclaration=textDeclaration+"\taddi $sp, $sp -4"+"\n";
                    }
                }else{
                    if(isArray){
                        textDeclaration=textDeclaration+"\taddi $sp, $sp -"+(1*arraySize)+"\n";
                    }else{
                        textDeclaration=textDeclaration+"\taddi $sp, $sp -1"+"\n";
                    }
                }
            }
        }
    }
    private String getMipsVarType(String typeToValue){
        if(typeToValue.equals("int"))
            return ".word 0";
        if(typeToValue.equals("char"))
            return ".byte ' '";
        if(typeToValue.equals("boolean"))
            return ".byte 0";
        return null;
    }
    private String getEmptyVarValue(String typeToValue){
        if(typeToValue.equals("int"))
            return "0";
        if(typeToValue.equals("char"))
            return "\' \'";
        if(typeToValue.equals("boolean"))
            return "0";
        return null;
    }
    public void enterMethod(String methodName, int scope, SymbolTable symbolTable){
        textDeclaration = textDeclaration + methodName+symbolTable.getScopeBefore()+":\n";
        textDeclaration = textDeclaration +"\taddi $sp, $sp, -4\n\tsw $ra, 0($sp)\n";

        currentMethod = methodName;
        
        if ((methodName+symbolTable.getScopeBefore()).equals("main0")){
            inMain=true;
            textDeclaration = addMain() + textDeclaration;
        }
    }
    public void exitBlock(SymbolTable symbolTable){
        if(currentMethod!=null){
            int blockSize=symbolTable.getMethodByName(currentMethod).getLastBlockSize();
            textDeclaration = textDeclaration + "\taddi $sp, $sp, "+blockSize+"\n";
        }
    }
    public void exitMethod(SymbolTable symbolTable){
        if (inMain)
            textDeclaration = textDeclaration + addEndOfMain();
        else{
            int returnOffset = symbolTable.getMethodByName(currentMethod).getDirectionOffset();
            textDeclaration = textDeclaration+"\tlw $ra "+returnOffset+"($sp)\n";
            int blockSize=symbolTable.getMethodByName(currentMethod).getMethodSize();
            textDeclaration = textDeclaration + "\taddi $sp, $sp, "+blockSize+"\n";
            textDeclaration = textDeclaration + "\tjr $ra\n";
        }
        registerOcupationDepth=0;
        currentMethod=null;
    }
    public void addStruct(String structName,int scope){
        structs.add(structName+scope);
        inStruct=true;
    }
    public void exitStruct(){
        inStruct=false;
    }
    
    
    
    public String finishUp(){
        return ".data\n"+dataDeclaration+"\n.text\n"+textDeclaration;
    }
    
    public void enterIf(int scope, DECAFParser.ExpressionContext ctx, SymbolTable symbolTable, boolean hasElse){
        textDeclaration=textDeclaration+"if"+scope+ifNumbers.get(ifNumbers.size()-1).toString()+":\n";
        String nameofExit;
        recursiveExpressionWriter(ctx, symbolTable, scope);
        //Realizador de comparador
        if(hasElse)
            nameofExit="exitif";
        else
            nameofExit="endif";
        textDeclaration=textDeclaration+"\tbeq $t"+registerOcupationDepth+", $zero, "+nameofExit+scope+ifNumbers.get(ifNumbers.size()-1).toString()+"\n";
        ifScopes.add(scope);
        ifNumbers.set(ifNumbers.size()-1,ifNumbers.get(ifNumbers.size()-1)+1);
        ifNumbers.add(0);


    }
    public void enterElse(){
        int scope = ifScopes.get(ifScopes.size()-1);
        textDeclaration=textDeclaration+"\tj endif"+scope+(ifNumbers.get(ifNumbers.size()-2)-1)+"\n";
        textDeclaration=textDeclaration+"exitif"+scope+(ifNumbers.get(ifNumbers.size()-2)-1)+":\n";
    }
    public void exitIf(){
        ifNumbers.remove(ifNumbers.size()-1);
        int scope = ifScopes.get(ifScopes.size()-1);
        textDeclaration=textDeclaration+"endif"+scope+(ifNumbers.get(ifNumbers.size()-1)-1)+":\n";
        ifScopes.remove(ifScopes.size()-1);
    }
    public void doReturn(DECAFParser.ExpressionContext ctx,SymbolTable symbolTable, int scope){
        recursiveExpressionWriter(ctx, symbolTable, scope);
        int returnOffset = symbolTable.getMethodByName(currentMethod).getDirectionOffset();
        textDeclaration = textDeclaration + "\tmove $v1, $t"+registerOcupationDepth+"\n";
        textDeclaration = textDeclaration+"\tlw $ra "+returnOffset+"($sp)\n";
        int blockSize=symbolTable.getMethodByName(currentMethod).getMethodSize();
        textDeclaration = textDeclaration + "\taddi $sp, $sp, "+blockSize+"\n";
        textDeclaration = textDeclaration + "\tjr $ra\n";


    }
    public void enterWhile(int scope, DECAFParser.ExpressionContext ctx,SymbolTable symbolTable){
        textDeclaration=textDeclaration+"while"+scope+whileNumbers.get(whileNumbers.size()-1).toString()+":\n";
        recursiveExpressionWriter(ctx, symbolTable, scope);
        //Realizador de comparador
        textDeclaration=textDeclaration+"\tbeq $t"+registerOcupationDepth+", $zero, exitwhile"+scope+whileNumbers.get(whileNumbers.size()-1).toString()+"\n";
        whileScopes.add(scope);
        whileNumbers.set(whileNumbers.size()-1,whileNumbers.get(whileNumbers.size()-1)+1);
        whileNumbers.add(0);
        
    }
    public void exitWhile(){
        whileNumbers.remove(whileNumbers.size()-1);
        int scope = whileScopes.get(whileScopes.size()-1);
        textDeclaration=textDeclaration+"\tj while"+scope+(whileNumbers.get(whileNumbers.size()-1)-1)+"\n";
        textDeclaration=textDeclaration+"exitwhile"+scope+(whileNumbers.get(whileNumbers.size()-1)-1)+":\n";
        whileScopes.remove(whileScopes.size()-1);
    }

    public void recursiveLocationWriter(DECAFParser.LocationContext ctx, SymbolTable symbolTable,int scope){
        String variableName = ctx.ID().getText();
        Variable tempVariable = symbolTable.getVariableInScope(scope, variableName);
        String type = tempVariable.getType();
        String loadLine="";
        if(tempVariable.isGlobal){
            loadLine=" $t"+registerOcupationDepth+", "+variableName;
        }else{
            loadLine=" $t"+registerOcupationDepth+", "+(symbolTable.getMethodByName(currentMethod).getVariableOffset(variableName)+currentCallOfster)+"($sp)";
        }
        if (type.equals("int")){
            loadLine="lw"+loadLine;
            //Checking if array
            try{
                ctx.expression().getText();
                registerOcupationDepth++;
                recursiveExpressionWriter(ctx.expression(), symbolTable, scope);
                registerOcupationDepth--;
                textDeclaration = textDeclaration+"\tmul $t"+(registerOcupationDepth+1)+", $t"+(registerOcupationDepth+1)+", 4\n";
                if(tempVariable.isGlobal)
                    loadLine=loadLine+"($t"+(registerOcupationDepth+1)+")";
                else{
                    textDeclaration = textDeclaration + "\tadd $t"+(registerOcupationDepth+1)+", $t"+(registerOcupationDepth+1)+", $sp\n";
                    loadLine = loadLine.replace("$sp", "$t"+(registerOcupationDepth+1));
                }
            }catch (Exception e){

            }
        }else if (type.contains("struct")){
            Struct tempStruct=symbolTable.getStructInScope(scope, type.substring(6));
            String attribute = ctx.location().ID().getText();
            int offset = 0;
            for (int i=0;i<tempStruct.getAtributes().size();i++){
                if(!tempStruct.getAtributes().get(i).getName().equals(attribute)){
                    if(tempStruct.getAtributes().get(i).getType().equals("int")){
                        if (tempStruct.getAtributes().get(i).isArray)
                            offset+=(4*tempStruct.getAtributes().get(i).arraySize);
                        else    
                            offset+=4;
                    }else if(tempStruct.getAtributes().get(i).getType().contains("struct")){
                        if (tempStruct.getAtributes().get(i).isArray)
                            offset+=(symbolTable.getStructInScope(tempVariable.scopeCurrent, tempStruct.getAtributes().get(i).getType().substring(6)).getMemorySize()*tempStruct.getAtributes().get(i).arraySize);
                        else    
                            offset+=symbolTable.getStructInScope(tempVariable.scopeCurrent, tempStruct.getAtributes().get(i).getType().substring(6)).getMemorySize();
                    }else{
                        if (tempStruct.getAtributes().get(i).isArray)
                            offset+=(1*tempStruct.getAtributes().get(i).arraySize);
                        else    
                            offset+=1;
                    }
                }else{
                    if(tempStruct.getAtributes().get(i).getType().equals("int")){
                        //Checking if attribute is array
                        try{
                            int position = Integer.parseInt(ctx.location().expression().getText());
                            offset+=(position*4);
                        }catch(Exception e){
            
                        }
                        if(tempVariable.isGlobal){
                            loadLine="lw $t"+registerOcupationDepth+", "+variableName+"+"+offset;
                        }else{
                            loadLine="lw $t"+registerOcupationDepth+", "+(offset+symbolTable.getMethodByName(currentMethod).getVariableOffset(variableName))+"($sp)";
                        }
                    }else{
                        try{
                            int position = Integer.parseInt(ctx.location().expression().getText());
                            offset+=(position);
                        }catch(Exception e){
            
                        }
                        if(tempVariable.isGlobal){
                            loadLine="lb $t"+registerOcupationDepth+", "+variableName+"+"+offset;
                        }else{
                            loadLine="lb $t"+registerOcupationDepth+", "+(offset+symbolTable.getMethodByName(currentMethod).getVariableOffset(variableName))+"($sp)";
                        }
                    }
                    break;
                }
            }
            //Checking if array
            try{
                ctx.expression().getText();
                registerOcupationDepth++;
                recursiveExpressionWriter(ctx.expression(), symbolTable, scope);
                registerOcupationDepth--;
                if(tempVariable.isGlobal)
                    loadLine=loadLine+"($t"+(registerOcupationDepth+1)+")";
            else{
                textDeclaration = textDeclaration + "\tadd $t"+(registerOcupationDepth+1)+", $t"+(registerOcupationDepth+1)+", $sp\n";
                loadLine = loadLine.replace("$sp", "$t"+(registerOcupationDepth+1));
            }
            }catch (Exception e){

            }
            
        }else{
            loadLine="lb"+loadLine;
            //Checking if array
            try{
                Integer.parseInt(ctx.expression().getText());
                registerOcupationDepth++;
                recursiveExpressionWriter(ctx.expression(), symbolTable, scope);
                registerOcupationDepth--;
                textDeclaration = textDeclaration+"\tmul $t"+(registerOcupationDepth+1)+", $t"+(registerOcupationDepth+1)+", 1\n";
                if(tempVariable.isGlobal)
                    loadLine=loadLine+"($t"+(registerOcupationDepth+1)+")";
                else{
                    textDeclaration = textDeclaration + "\tadd $t"+(registerOcupationDepth+1)+", $t"+(registerOcupationDepth+1)+", $sp\n";
                    loadLine = loadLine.replace("$sp", "$t"+(registerOcupationDepth+1));
                }
            }catch (Exception e){

            }
        }
        textDeclaration = textDeclaration+"\t"+loadLine+"\n";
    }

    public void operationWriter(DECAFParser.ExpressionContext exp1, DECAFParser.ExpressionContext exp2, DECAFParser.OpContext opctx,SymbolTable symbolTable, int scope){
        recursiveExpressionWriter(exp2, symbolTable, scope);
        registerOcupationDepth++;
        recursiveExpressionWriter(exp1, symbolTable, scope);
        registerOcupationDepth--;
        if(opctx.getText().equals("==")){
            textDeclaration = textDeclaration + "\tseq $t"+registerOcupationDepth+", $t"+(registerOcupationDepth+1)+", $t"+registerOcupationDepth+"\n";
        }
        else if(opctx.getText().equals("!=")){
            textDeclaration = textDeclaration + "\tsne $t"+registerOcupationDepth+", $t"+(registerOcupationDepth+1)+", $t"+registerOcupationDepth+"\n";
        }
        else if(opctx.getText().equals(">")){
            textDeclaration = textDeclaration + "\tsgt $t"+registerOcupationDepth+", $t"+(registerOcupationDepth+1)+", $t"+registerOcupationDepth+"\n";
        }
        else if(opctx.getText().equals(">=")){
            textDeclaration = textDeclaration + "\tsge $t"+registerOcupationDepth+", $t"+(registerOcupationDepth+1)+", $t"+registerOcupationDepth+"\n";
        }
        else if(opctx.getText().equals("<")){
            textDeclaration = textDeclaration + "\tslt $t"+registerOcupationDepth+", $t"+(registerOcupationDepth+1)+", $t"+registerOcupationDepth+"\n";
        }
        else if(opctx.getText().equals("<=")){
            textDeclaration = textDeclaration + "\tsle $t"+registerOcupationDepth+", $t"+(registerOcupationDepth+1)+", $t"+registerOcupationDepth+"\n";
        }
        else if(opctx.getText().equals("+")){
            textDeclaration = textDeclaration + "\tadd $t"+registerOcupationDepth+", $t"+(registerOcupationDepth+1)+", $t"+registerOcupationDepth+"\n";
        }
        else if(opctx.getText().equals("-")){
            textDeclaration = textDeclaration + "\tsub $t"+registerOcupationDepth+", $t"+(registerOcupationDepth+1)+", $t"+registerOcupationDepth+"\n";
        }
        else if(opctx.getText().equals("/")){
            textDeclaration = textDeclaration + "\tdiv $t"+registerOcupationDepth+", $t"+(registerOcupationDepth+1)+", $t"+registerOcupationDepth+"\n";
        }
        else if(opctx.getText().equals("*")){
            textDeclaration = textDeclaration + "\tmul $t"+registerOcupationDepth+", $t"+(registerOcupationDepth+1)+", $t"+registerOcupationDepth+"\n";
        }
        else if(opctx.getText().equals("%")){
            textDeclaration = textDeclaration + "\tdiv $t"+registerOcupationDepth+", $t"+(registerOcupationDepth+1)+", $t"+registerOcupationDepth+"\n";
            textDeclaration = textDeclaration + "\tmfhi $t"+registerOcupationDepth+"\n";
        }
        else if(opctx.getText().equals("&&")){
            textDeclaration = textDeclaration + "\tand $t"+registerOcupationDepth+", $t"+(registerOcupationDepth+1)+", $t"+registerOcupationDepth+"\n";
        }
        else if(opctx.getText().equals("||")){
            textDeclaration = textDeclaration + "\tor $t"+registerOcupationDepth+", $t"+(registerOcupationDepth+1)+", $t"+registerOcupationDepth+"\n";
        }

    }
    public void recursiveExpressionWriter(DECAFParser.ExpressionContext ctx, SymbolTable symbolTable, int scope){
        //location
        try{
            String a = ctx.location().getText();
            recursiveLocationWriter(ctx.location(), symbolTable, scope);
        }catch (Exception e){

        }
        //methodCall
        try{
            ctx.methodCall().getText();
            methodCallWriter(ctx.methodCall(), symbolTable);
        }catch (Exception e){

        }
        //literal
        try{
            ctx.literal().getText();
            textDeclaration=textDeclaration+"\tli $t"+registerOcupationDepth+", "+ctx.literal().getText()+"\n";

        }catch (Exception e){

        }
        //op
        try{
            ctx.op().getText();
            operationWriter(ctx.expression(0), ctx.expression(1), ctx.op(), symbolTable, scope);
        }catch(Exception e){

        }
        //negation
        if(ctx.getText().charAt(0)=='!'){
            recursiveExpressionWriter(ctx.expression(0), symbolTable, scope);
            textDeclaration=textDeclaration+"\tseq $t"+registerOcupationDepth+", $t"+registerOcupationDepth+", $zero\n";
        }
        //negative
        if(ctx.getText().charAt(0)=='-'){
            recursiveExpressionWriter(ctx.expression(0), symbolTable, scope);
            textDeclaration=textDeclaration+"\tmul $t"+registerOcupationDepth+", $t"+registerOcupationDepth+", -1\n";
        }
    }
    public void methodCallWriter(DECAFParser.MethodCallContext ctx, SymbolTable symbolTable){
        isCallingFunc =true;
        methodBeingCalled = ctx.ID().getText();
        int argQuantity=ctx.arg().size();
        String methodName = ctx.ID().getText();
        Method currentMethod=symbolTable.getMethodByName(methodName);
        int methodScope = currentMethod.getScopeCurrent();
        methodName=methodName+methodScope;
        String toAdd="";
        if(argQuantity==currentMethod.getParameters().size()){
            for (int i = 0; i<argQuantity;i++){
                recursiveExpressionWriter(ctx.arg().get(i).expression(), symbolTable, symbolTable.getScopeCurrent()); 
                if (currentMethod.getParameters().get(i).getType().equals("int")){
                    textDeclaration=textDeclaration+"\taddi $sp, $sp, -4\n\tsw $t"+(registerOcupationDepth)+", 0($sp)\n";
                    currentCallOfster+=4;
                }else{
                    textDeclaration=textDeclaration+"\taddi $sp, $sp, -1\n\tsb $t"+(registerOcupationDepth)+", 0($sp)\n";
                    currentCallOfster+=1;
                }
            }
        }
        toAdd=toAdd +"\tjal "+methodName+"\n";
        toAdd=toAdd + "\tmove $t"+registerOcupationDepth+", $v1\n";
        textDeclaration=textDeclaration+toAdd;
        isCallingFunc=false;
        currentCallOfster=0;
        methodBeingCalled=null;
    }
    /*
    private String locationEval(DECAFParser.LocationContext ctx, SymbolTable symbolTable){
        compiler.setSymbolTable(symbolTable);
        String type = compiler.recursiveLocationType(ctx, symbolTable.getScopeCurrent());
        if (type.equals("int")){
            String a =("lw $t"+registerOcupationDepth+", ");
        }else{

        }
        return null;
    }*/
    public String argEval(DECAFParser.ArgContext ctx, SymbolTable symbolTable){
        return null;
    }

    
    public String addMain(){
        return "\tj main0 \n";
    }
    public String addEndOfMain(){
        return "\tli $v0, 10\n"+
        "\tsyscall\n";
    }
}
