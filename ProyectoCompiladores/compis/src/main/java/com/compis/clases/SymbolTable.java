package com.compis.clases;

import java.util.ArrayList;

public class SymbolTable {
    int scopeCurrent;
    int scopeBefore;
    int offset;
    ArrayList<Variable> variables;
    ArrayList<Method> methods;
    ArrayList<Struct> structs;
    ArrayList<Error> errors;

    public SymbolTable(int scopeCurrent, int scopeBefore, int offset) {
        this.scopeBefore = scopeBefore;
        this.scopeCurrent = scopeCurrent;
        this.offset = offset;
        variables = new ArrayList<>();
        methods = new ArrayList<>();
        structs = new ArrayList<>();
        errors = new ArrayList<>();
    }

    public String getVariableByName(String name) {
        return null;
    }

    public String getMethodByName(String name) {
        return null;
    }

    public String getStructByName(String name) {
        return null;
    }

    public String getErrorByName(String name) {
        return null;
    }

    public boolean doesNameExist(String name) {
        for (int i=0; i<variables.size();i++){
            if (variables.get(i).getName().equals(name))
            return true;
        }
        for (int i=0; i<methods.size();i++){
            if (methods.get(i).getName().equals(name))
            return true;
        }
        for (int i=0; i<structs.size();i++){
            if (structs.get(i).getName().equals(name))
            return true;
        }
        for (int i=0; i<errors.size();i++){
            if (errors.get(i).getName().equals(name))
            return true;
        }
        return false;
    }

    public boolean addVariable(String name, String type) {
        if (doesNameExist(name)) {
            return false;
        } else {
            variables.add(new Variable(type, name, null, null, null, null, scopeCurrent, scopeBefore));
            return true;
        }
    }
    public boolean addMethod(String name, String type) {
        if (doesNameExist(name)) {
            return false;
        } else {
            methods.add(new Method(type, name, null, null, null, null, scopeCurrent, scopeBefore));
            return true;
        }
    }
    public boolean addError(String name, String type) {
        if (doesNameExist(name)) {
            return false;
        } else {
            errors.add(new Error(type, name, null, null, null, null, scopeCurrent, scopeBefore));
            return true;
        }
    }
    public boolean addStruct(String name, String type) {
        if (doesNameExist(name)) {
            return false;
        } else {
            structs.add(new Struct(type, name, null, null, null, null, scopeCurrent, scopeBefore));
            return true;
        }
    }

}
