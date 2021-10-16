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
        if (type.equals("int")){
            saveLine="sw $t"+registerOcupationDepth+", "+variableName+tempVariable.getScopeCurrent();
            //Checking if array
            try{
                Integer.parseInt(ctx.location().expression().getText());
                registerOcupationDepth++;
                recursiveExpressionWriter(ctx.location().expression(), symbolTable, scope);
                registerOcupationDepth--;
                saveLine=saveLine+"($t"+(registerOcupationDepth+1)+")";
            }catch (Exception e){

            }
        }else if (type.contains("struct")){
            Struct tempStruct=symbolTable.getStructInScope(scope, type.substring(6));
            String attribute = ctx.location().location().ID().getText();
            int offset = 0;
            for (int i=0;i<tempStruct.getAtributes().size();i++){
                if(!tempStruct.getAtributes().get(i).getName().equals(attribute)){
                    if(tempStruct.getAtributes().get(i).getType().equals("int")){
                        offset+=4;
                    }else if(tempStruct.getAtributes().get(i).getType().contains("struct")){
                        offset+=symbolTable.getStructInScope(tempVariable.scopeCurrent, tempStruct.getAtributes().get(i).getType().substring(6)).getMemorySize();
                    }else{
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
                        saveLine="sw $t"+registerOcupationDepth+", "+variableName +tempVariable.getScopeCurrent() +"+"+offset;
                    }else{
                        try{
                            int position = Integer.parseInt(ctx.location().location().expression().getText());
                            offset+=(position);
                        }catch(Exception e){
            
                        }
                        saveLine="sb $t"+registerOcupationDepth+", "+variableName +tempVariable.getScopeCurrent() +"+"+offset;
                    }
                    break;
                }
            }
            //Checking if array
            try{
                ctx.location().expression().getText();
                registerOcupationDepth++;
                recursiveExpressionWriter(ctx.location().expression(), symbolTable, scope);
                registerOcupationDepth--;
                saveLine=saveLine+"($t"+(registerOcupationDepth+1)+")";
            }catch (Exception e){

            }
            
        }else{
            saveLine="sb $t"+registerOcupationDepth+", "+variableName+tempVariable.getScopeCurrent();
            //Checking if array
            try{
                Integer.parseInt(ctx.location().expression().getText());
                registerOcupationDepth++;
                recursiveExpressionWriter(ctx.location().expression(), symbolTable, scope);
                registerOcupationDepth--;
                saveLine=saveLine+"($t"+(registerOcupationDepth+1)+")";
            }catch (Exception e){

            }
        }
        textDeclaration = textDeclaration+saveLine+"\n";
    }

    

    public void addVariable(String variableName, int scope, String type, boolean isArray, int arraySize, int scopeMemorySize){
        if(!inStruct){
            String returnValue = variableName + scope +": "+getMipsVarType(type);
            if(type.contains("struct")){
                if (isArray){
                    returnValue = variableName+scope+": .space "+(scopeMemorySize*arraySize);
                }else{
                    returnValue = variableName+scope+": .space "+scopeMemorySize;
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
        Method tempMethod = symbolTable.getMethodByName(methodName);
        ArrayList<Variable> temporalVars=tempMethod.getParameters();
        for (int i=0; i<temporalVars.size();i++){
            addVariable(temporalVars.get(i).getName(), scope, temporalVars.get(i).type, temporalVars.get(i).isArray, temporalVars.get(i).arraySize, 0);   
            if (temporalVars.get(i).getType().equals("int")){
                textDeclaration = textDeclaration + "sw $a"+i+", "+temporalVars.get(i).getName()+scope+"\n";
            }else{
                textDeclaration = textDeclaration + "sb $a"+i+", "+temporalVars.get(i).getName()+scope+"\n";
            }
        }
        
        if ((methodName+scope).equals("main0")){
            inMain=true;
            textDeclaration = addMain() + textDeclaration;
        }
    }
    public void exitMethod(){
        if (inMain)
            textDeclaration = textDeclaration + addEndOfMain();
        else
            textDeclaration = textDeclaration + "jr $ra\n";
        registerOcupationDepth=0;
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
        textDeclaration=textDeclaration+"beq $t"+registerOcupationDepth+", $zero, exitif"+scope+ifNumbers.get(ifNumbers.size()-1).toString()+"\n";
        ifScopes.add(scope);
        ifNumbers.set(ifNumbers.size()-1,ifNumbers.get(ifNumbers.size()-1)+1);
        ifNumbers.add(0);


    }
    public void enterElse(){
        int scope = ifScopes.get(ifScopes.size()-1);
        textDeclaration=textDeclaration+"j endif"+scope+(ifNumbers.get(ifNumbers.size()-2)-1)+"\n";
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
        textDeclaration = textDeclaration + "move $v1, $t"+registerOcupationDepth+"\n";
        textDeclaration = textDeclaration + "jr $ra\n";


    }
    public void enterWhile(int scope, DECAFParser.ExpressionContext ctx,SymbolTable symbolTable){
        textDeclaration=textDeclaration+"while"+scope+whileNumbers.get(whileNumbers.size()-1).toString()+":\n";
        recursiveExpressionWriter(ctx, symbolTable, scope);
        //Realizador de comparador
        textDeclaration=textDeclaration+"beq $t"+registerOcupationDepth+", $zero, exitwhile"+scope+whileNumbers.get(whileNumbers.size()-1).toString()+"\n";
        whileScopes.add(scope);
        whileNumbers.set(whileNumbers.size()-1,whileNumbers.get(whileNumbers.size()-1)+1);
        whileNumbers.add(0);
        
    }
    public void exitWhile(){
        whileNumbers.remove(whileNumbers.size()-1);
        int scope = whileScopes.get(whileScopes.size()-1);
        textDeclaration=textDeclaration+"j while"+scope+(whileNumbers.get(whileNumbers.size()-1)-1)+"\n";
        textDeclaration=textDeclaration+"exitwhile"+scope+(whileNumbers.get(whileNumbers.size()-1)-1)+":\n";
        whileScopes.remove(whileScopes.size()-1);
    }

    public void recursiveLocationWriter(DECAFParser.LocationContext ctx, SymbolTable symbolTable,int scope){
        String variableName = ctx.ID().getText();
        Variable tempVariable = symbolTable.getVariableInScope(scope, variableName);
        String type = tempVariable.getType();
        String loadLine="";
        if (type.equals("int")){
            loadLine="lw $t"+registerOcupationDepth+", "+variableName+tempVariable.getScopeCurrent();
            //Checking if array
            try{
                Integer.parseInt(ctx.expression().getText());
                registerOcupationDepth++;
                recursiveExpressionWriter(ctx.location().expression(), symbolTable, scope);
                registerOcupationDepth--;
                loadLine=loadLine+"($t"+(registerOcupationDepth+1)+")";
            }catch (Exception e){

            }
        }else if (type.contains("struct")){
            Struct tempStruct=symbolTable.getStructInScope(scope, type.substring(6));
            String attribute = ctx.location().ID().getText();
            int offset = 0;
            for (int i=0;i<tempStruct.getAtributes().size();i++){
                if(!tempStruct.getAtributes().get(i).getName().equals(attribute)){
                    if(tempStruct.getAtributes().get(i).getType().equals("int")){
                        offset+=4;
                    }else if(tempStruct.getAtributes().get(i).getType().contains("struct")){
                        offset+=symbolTable.getStructInScope(tempVariable.scopeCurrent, tempStruct.getAtributes().get(i).getType().substring(6)).getMemorySize();
                    }else{
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
                        loadLine="lw $t"+registerOcupationDepth+", "+variableName+tempVariable.getScopeCurrent()+"+"+offset;
                    }else{
                        try{
                            int position = Integer.parseInt(ctx.location().expression().getText());
                            offset+=(position);
                        }catch(Exception e){
            
                        }
                        loadLine="lb $t"+registerOcupationDepth+", "+variableName+tempVariable.getScopeCurrent() +"+"+offset;
                    }
                    break;
                }
            }
            //Checking if array
            try{
                ctx.location().expression().getText();
                registerOcupationDepth++;
                recursiveExpressionWriter(ctx.expression(), symbolTable, scope);
                registerOcupationDepth--;
                loadLine=loadLine+"($t"+(registerOcupationDepth+1)+")";
            }catch (Exception e){

            }
            
        }else{
            loadLine="lb $t"+registerOcupationDepth+", "+variableName+tempVariable.getScopeCurrent();
            //Checking if array
            try{
                Integer.parseInt(ctx.expression().getText());
                registerOcupationDepth++;
                recursiveExpressionWriter(ctx.expression(), symbolTable, scope);
                registerOcupationDepth--;
                loadLine=loadLine+"($t"+(registerOcupationDepth+1)+")";
            }catch (Exception e){

            }
        }
        textDeclaration = textDeclaration+loadLine+"\n";
    }

    public void operationWriter(DECAFParser.ExpressionContext exp1, DECAFParser.ExpressionContext exp2, DECAFParser.OpContext opctx,SymbolTable symbolTable, int scope){
        recursiveExpressionWriter(exp1, symbolTable, scope);
        registerOcupationDepth++;
        recursiveExpressionWriter(exp2, symbolTable, scope);
        registerOcupationDepth--;
        if(opctx.getText().equals("==")){
            textDeclaration = textDeclaration + "seq $t"+registerOcupationDepth+", $t"+registerOcupationDepth+", $t"+(registerOcupationDepth+1)+"\n";
        }
        else if(opctx.getText().equals("!=")){
            textDeclaration = textDeclaration + "sne $t"+registerOcupationDepth+", $t"+registerOcupationDepth+", $t"+(registerOcupationDepth+1)+"\n";
        }
        else if(opctx.getText().equals(">")){
            textDeclaration = textDeclaration + "sgt $t"+registerOcupationDepth+", $t"+registerOcupationDepth+", $t"+(registerOcupationDepth+1)+"\n";
        }
        else if(opctx.getText().equals(">=")){
            textDeclaration = textDeclaration + "sge $t"+registerOcupationDepth+", $t"+registerOcupationDepth+", $t"+(registerOcupationDepth+1)+"\n";
        }
        else if(opctx.getText().equals("<")){
            textDeclaration = textDeclaration + "slt $t"+registerOcupationDepth+", $t"+registerOcupationDepth+", $t"+(registerOcupationDepth+1)+"\n";
        }
        else if(opctx.getText().equals("<=")){
            textDeclaration = textDeclaration + "sle $t"+registerOcupationDepth+", $t"+registerOcupationDepth+", $t"+(registerOcupationDepth+1)+"\n";
        }
        else if(opctx.getText().equals("+")){
            textDeclaration = textDeclaration + "add $t"+registerOcupationDepth+", $t"+registerOcupationDepth+", $t"+(registerOcupationDepth+1)+"\n";
        }
        else if(opctx.getText().equals("-")){
            textDeclaration = textDeclaration + "sub $t"+registerOcupationDepth+", $t"+registerOcupationDepth+", $t"+(registerOcupationDepth+1)+"\n";
        }
        else if(opctx.getText().equals("/")){
            textDeclaration = textDeclaration + "div $t"+registerOcupationDepth+", $t"+registerOcupationDepth+", $t"+(registerOcupationDepth+1)+"\n";
        }
        else if(opctx.getText().equals("*")){
            textDeclaration = textDeclaration + "mul $t"+registerOcupationDepth+", $t"+registerOcupationDepth+", $t"+(registerOcupationDepth+1)+"\n";
        }
        else if(opctx.getText().equals("%")){
            textDeclaration = textDeclaration + "div $t"+registerOcupationDepth+", $t"+registerOcupationDepth+", $t"+(registerOcupationDepth+1)+"\n";
            textDeclaration = textDeclaration + "mfhi $t"+registerOcupationDepth+"\n";
        }
        else if(opctx.getText().equals("&&")){
            textDeclaration = textDeclaration + "and $t"+registerOcupationDepth+", $t"+registerOcupationDepth+", $t"+(registerOcupationDepth+1)+"\n";
        }
        else if(opctx.getText().equals("||")){
            textDeclaration = textDeclaration + "or $t"+registerOcupationDepth+", $t"+registerOcupationDepth+", $t"+(registerOcupationDepth+1)+"\n";
        }

    }
    public void recursiveExpressionWriter(DECAFParser.ExpressionContext ctx, SymbolTable symbolTable, int scope){
        //location
        try{
            ctx.location().getText();
            recursiveLocationWriter(ctx.location(), symbolTable, scope);
        }catch (Exception e){

        }
        //methodCall
        try{
            ctx.methodCall().getText();
            methodCallWriter(ctx.methodCall(), symbolTable);
            textDeclaration = textDeclaration + "move $t0, $v1\n";
            registerOcupationDepth=1;
        }catch (Exception e){

        }
        //literal
        try{
            ctx.literal().getText();
            textDeclaration=textDeclaration+"li $t"+registerOcupationDepth+", "+ctx.literal().getText()+"\n";

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
            textDeclaration=textDeclaration+"seq $t"+registerOcupationDepth+", $t"+registerOcupationDepth+", $zero\n";
        }
        //negative
        if(ctx.getText().charAt(0)=='-'){
            recursiveExpressionWriter(ctx.expression(0), symbolTable, scope);
            textDeclaration=textDeclaration+"mul $t"+registerOcupationDepth+", $t"+registerOcupationDepth+", -1\n";
        }
    }
    public void methodCallWriter(DECAFParser.MethodCallContext ctx, SymbolTable symbolTable){
        int argQuantity=ctx.arg().size();
        String methodName = ctx.ID().getText();
        Method currentMethod=symbolTable.getMethodByName(methodName);
        int methodScope = currentMethod.getScopeCurrent();
        methodName=methodName+methodScope;
        String toAdd="";
        if(argQuantity==currentMethod.getParameters().size()){
            for (int i = 0; i<argQuantity;i++){
                recursiveExpressionWriter(ctx.arg().get(i).expression(), symbolTable, symbolTable.getScopeCurrent()); 
                toAdd=toAdd+"move $a"+i+", $t"+registerOcupationDepth+"\n";
            }
        }
        toAdd=toAdd +"jal "+methodName+"\n";
        toAdd=toAdd + "move $t"+registerOcupationDepth+", $v1\n";
        textDeclaration=textDeclaration+toAdd;
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

    public String divideFunction(){
        String divideFnct;
        divideFnct="divide:\n"+
	    "div $v1,$a0,$a1\n"+
	    "jr $ra\n";
        return divideFnct;
    }
    public String modFunction(){
        String divideFnct;
        divideFnct="mod:\n"+
        "div $v1,$a0,$a1\n"+
        "mfhi $v1\n"+
        "jr $ra\n";
        return divideFnct;
    }
    public String mulFunction(){
        String divideFnct;
        divideFnct="multiplication:\n"+
        "mul $v1,$a0,$a1\n"+
        "jr $ra\n";
        return divideFnct;
    }
    public String addFunction(){
        String divideFnct;
        divideFnct="addition:\n"+
        "add $v1,$a0,$a1\n"+
        "jr $ra\n";
        return divideFnct;
    }
    public String subFunction(){
        String divideFnct;
        divideFnct="substract:\n"+
        "sub $v1,$a0,$a1\n"+
        "jr $ra\n";
        return divideFnct;
    }
    public String lessThanFunction(){
        String divideFnct;
        divideFnct="lessthan:\n"+
        "slt $v1,$a0,$a1\n"+
        "jr $ra\n";
        return divideFnct;
    }
    public String lessEqFunction(){
        String divideFnct;
        divideFnct="lesseq:\n"+
        "sle $v1,$a0,$a1\n"+
        "jr $ra\n";
        return divideFnct;
    }
    public String greaterThanFunction(){
        String divideFnct;
        divideFnct="greaterthan:\n"+
        "sgt $v1,$a0,$a1\n"+
        "jr $ra\n";
        return divideFnct;
    }
    public String greaterEqFunction(){
        String divideFnct;
        divideFnct="greatereq:\n"+
        "sge $v1,$a0,$a1\n"+
        "jr $ra\n";
        return divideFnct;
    }
    public String equalToFunction(){
        String divideFnct;
        divideFnct="equalto:\n"+
        "seq $v1,$a0,$a1\n"+
        "jr $ra\n";
        return divideFnct;
    }
    public String notequalToFunction(){
        String divideFnct;
        divideFnct="notequalto:\n"+
        "seq $v1,$a0,$a1\n"+
        "seq $v1,$v1,$zero\n"+
        "jr $ra\n";
        return divideFnct;
    }
    public String addMain(){
        return "jal main0 \n";
    }
    public String addEndOfMain(){
        return "li $v0, 10\n"+
        "syscall\n";
    }
}
