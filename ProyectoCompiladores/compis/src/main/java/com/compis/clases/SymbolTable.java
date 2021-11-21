package com.compis.clases;

import java.util.ArrayList;

public class SymbolTable {
    int scopeCurrent;
    int scopeBefore;
    int offset;
    private String currentMethodName=null;
    ArrayList<Integer> scopeBefores;
    ArrayList<String> scopes;
    ArrayList<Variable> variables;
    ArrayList<Method> methods;
    ArrayList<Struct> structs;
    ArrayList<Error> errors;

    public SymbolTable() {
        this.scopes = new ArrayList<>();
        this.scopes.add("Global");
        this.scopeBefores = new ArrayList<>();
        this.scopeBefores.add(-1);
        this.scopeBefore = -1;
        this.scopeCurrent = 0;
        this.offset = 0;
        variables = new ArrayList<>();
        methods = new ArrayList<>();
        structs = new ArrayList<>();
        errors = new ArrayList<>();
    }

    public Variable getVariableByName(String name) {
        int tempScope = scopeCurrent;
        while (tempScope>=0){
            for (int i=0; i < variables.size(); i++){
                if (variables.get(i).getName().equals(name)){
                    return variables.get(i);
                }
            }
            tempScope=scopeBefores.get(tempScope);
        }
        return null;
    }

    public Method getMethodByName(String name) {
        int tempScope = scopeCurrent;
        while (tempScope>=0){
            for (int i=0; i < methods.size(); i++){
                if (methods.get(i).getName().equals(name)){
                    return methods.get(i);
                }
            }
            tempScope=scopeBefores.get(tempScope);
        }
        return null;
    }

    public Struct getStructByName(String name) {
        int tempScope = scopeCurrent;
        while (tempScope>=0){
            for (int i=0; i < structs.size(); i++){
                if (structs.get(i).getName().equals(name)){
                    return structs.get(i);
                }
            }
            tempScope=scopeBefores.get(tempScope);
        }
        return null;
    }

    public String getErrorByName(String name) {
        return null;
    }

    public boolean doesNameExist(String name) {
        for (int i=0; i<variables.size();i++){
            if ((variables.get(i).getName().equals(name))&&(variables.get(i).getScopeCurrent()==this.scopeCurrent))
            return true;
        }
        for (int i=0; i<methods.size();i++){
            if ((methods.get(i).getName().equals(name))&&(methods.get(i).getScopeCurrent()==this.scopeCurrent))
            return true;
        }
        for (int i=0; i<structs.size();i++){
            if ((structs.get(i).getName().equals(name))&&(structs.get(i).getScopeCurrent()==this.scopeCurrent))
            return true;
        }
        for (int i=0; i<errors.size();i++){
            if ((errors.get(i).getName().equals(name))&&(errors.get(i).getScopeCurrent()==this.scopeCurrent))
            return true;
        }
        return false;
    }

    public boolean addVariable(String name, String type) {
        if (doesNameExist(name)) {
            return false;
        } else {
            Variable tmpVar = new Variable(type, name, null, null, scopeCurrent, scopeBefore,0);
            if (scopeCurrent == 0)
                tmpVar.setIsGlobal(true);
            else
                tmpVar.setIsGlobal(false);
                if (currentMethodName!=null)
                    methods.get(methods.size()-1).addLocalVar(tmpVar);
            variables.add(tmpVar);
            return true;
        }
    }
    public boolean addMethod(String name, String type) {
        if (doesNameExist(name)) {
            return false;
        } else {
            currentMethodName = name;
            methods.add(new Method(type, name,  null, null, scopeCurrent, scopeBefore, 0));
            this.scopes.add("Method"+name);
            this.scopeBefore = this.scopeCurrent;
            this.scopeBefores.add(this.scopeBefore);
            this.scopeCurrent = scopes.size()-1;
            return true;
        }
    }
    public boolean addError(String name, String type) {
        if (doesNameExist(name)) {
            return false;
        } else {
            errors.add(new Error(type, name, null, null, scopeCurrent, scopeBefore, 0));
            return true;
        }
    }
    public boolean addStruct(String name, String type, ArrayList<String> attrNames, ArrayList<String> attrTypes, ArrayList<Integer> arrayLengths) {
        if (doesNameExist(name)) {
            return false;
        } else {
            Struct newstruct= new Struct(type, name, null, null, scopeCurrent, scopeBefore, 0);
            int structSize=0;
            for (int i=0; i<attrNames.size();i++){
                if(attrTypes.get(i).equals("int"))
                    structSize+=(4*arrayLengths.get(i));
                else if(attrTypes.get(i).equals("char")||attrTypes.get(i).equals("boolean")){
                    structSize+=(arrayLengths.get(i));
                }else{
                    structSize+=(getStructInScope(scopeCurrent, attrTypes.get(i).substring(6)).getMemorySize()*arrayLengths.get(i));
                }
                newstruct.addAttribute(new Variable(attrTypes.get(i),attrNames.get(i),null,null,scopes.size(), scopeCurrent,0));
            }
            newstruct.setMemorySize(structSize);
            structs.add(newstruct);
            this.scopes.add("Struct"+name);
            this.scopeBefore = this.scopeCurrent;
            this.scopeBefores.add(this.scopeBefore);
            this.scopeCurrent = scopes.size()-1;
            newstruct.setInsideScope(this.scopeCurrent);
            return true;
        }
    }
    public boolean goUpScope(){
        this.scopeCurrent = this.scopeBefore;
        this.scopeBefore = this.scopeBefores.get(this.scopeCurrent);
        if(currentMethodName!=null){
            getMethodByName(currentMethodName).goUp();
        }
        return true;
    }

    public int getScopeCurrent() {
        return this.scopeCurrent;
    }
    public void serVarAsArray(String varname,int arraySize){
        getVariableInScope(scopeCurrent,varname).setArraySize(arraySize);
        getVariableInScope(scopeCurrent,varname).setIsArray(true);
    }

    public int getScopeBefore() {
        return this.scopeBefore;
    }
    public Variable getVariableInScope(int scope, String name){
        int tempScope = scope;
        while (tempScope>=0){
            for (int i=0; i < variables.size(); i++){
                if (variables.get(i).getName().equals(name)&&(tempScope==variables.get(i).getScopeCurrent())){
                    return variables.get(i);
                }
            }
            tempScope=scopeBefores.get(tempScope);
        }
        return null;
    }
    public Method getMethodInScope(int scope, String name){
        int tempScope = scope;
        while (tempScope>=0){
            for (int i=0; i < methods.size(); i++){
                if (methods.get(i).getName().equals(name)){
                    return methods.get(i);
                }
            }
            tempScope=scopeBefores.get(tempScope);
        }
        return null;
    }
    public Struct getStructInScope(int scope, String name){
        int tempScope = scope;
        while (tempScope>=0){
            for (int i=0; i < structs.size(); i++){
                if (structs.get(i).getName().equals(name)&&(tempScope==structs.get(i).getScopeCurrent())){
                    return structs.get(i);
                }
            }
            tempScope=scopeBefores.get(tempScope);
        }
        return null;
    }
    public void addScope(){
        this.scopes.add("Block "+this.scopeCurrent);
        this.scopeBefore = this.scopeCurrent;
        this.scopeBefores.add(this.scopeBefore);
        this.scopeCurrent = scopes.size()-1;
        if(currentMethodName!=null){
            getMethodByName(currentMethodName).addScope();
        }
    }
    public ArrayList<String> getScopes(){
        return scopes;
    }
    public ArrayList<Integer> getScopeBefores(){
        return scopeBefores;
    }
    public ArrayList<Variable> getAllVariables(){
        return variables;
    }
    public ArrayList<Method> getAllMethods(){
        return methods;
    }
    public ArrayList<Struct> getAllStructs(){
        return structs;
    }
    public void addParameter(String name, String type){
        Variable tmpVar = new Variable(type, name, null, null, scopeCurrent, scopeBefore,0);
        tmpVar.setIsGlobal(false);
        int methodPositon = methods.size()-1;
        methods.get(methodPositon).addParameter(tmpVar);
        addVariable(name, type);
        getVariableByName(name).setIsParameter(true);
    }
    public void exitMethod(){
        currentMethodName=null;
    }

}
