package com.compis.clases;

import java.util.ArrayList;

public class MipsConverter {

    int registerOcupationDepth=0;
    boolean inMain=false;
    boolean inStruct=false;
    String dataDeclaration="";
    String textDeclaration="";
    ArrayList<String> structs = new ArrayList<>();
    public void addVariable(String variableName, int scope, String type, boolean isArray, int arraySize){
        String returnValue = variableName + scope +": "+getMipsVarType(type);
        if(!inStruct){
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
    public void enterMethod(String methodName, int scope){
        textDeclaration = textDeclaration + methodName+scope+":\n";
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
        registerOcupationDepth--;
    }
    public void addStruct(String structName,int scope){
        structs.add(structName+scope);
        inStruct=true;
    }
    public void exitStruct(){
        inStruct=false;
    }
    
    public String enterWhile(String whileName, int scope, String comparisson){
        return whileName+scope+":\n";
    }
    public String exitWhile(String whileName, int scope){
        return "j "+whileName+scope+"\n"+
        "exit"+whileName+scope+"\n";
    }
    public String enterIf(String methodName, int scope){
        return methodName+scope+":\n";
    }
    public String exitIf(String merthodName, int scope){
        return "jr $ra\n";
    }
    public String getVariable (String varname,int scope, String vartype){
        if (vartype.equals("int")){
            return "lw $t"+(registerOcupationDepth-1)+", "+varname+scope;
        }else{
            return "lb $t"+(registerOcupationDepth-1)+", "+varname+scope;
        }
    }
    
    
    public String finishUp(){
        return ".data\n"+dataDeclaration+"\n.text\n"+textDeclaration;
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
