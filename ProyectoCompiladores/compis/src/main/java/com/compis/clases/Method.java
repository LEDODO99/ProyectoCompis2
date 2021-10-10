package com.compis.clases;

import java.util.ArrayList;

public class Method extends CommonData {

    ArrayList<Variable> parameters = new ArrayList();
    public Method(String type, String name, String signature, String error, String line, String collumn,
            int scopeCurrent, int scopeBefore) {
        super(type, name, signature, error, line, collumn, scopeCurrent, scopeBefore);
        // TODO Auto-generated constructor stub
    }
    public void addParameter(Variable newPar){
        parameters.add(newPar);
    }

    public ArrayList<Variable> getParameters(){
        return parameters;
    }
}
