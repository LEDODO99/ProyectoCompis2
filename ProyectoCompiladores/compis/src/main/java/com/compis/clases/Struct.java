package com.compis.clases;

import java.util.ArrayList;

public class Struct extends CommonData{
    private int insideScope;
    private int memorySize;
    
    private ArrayList<Variable> attributes = new ArrayList<>();
    public Struct(String type, String name, String signature, String error, String line, String collumn,
            int scopeCurrent, int scopeBefore) {
        super(type, name, signature, error, line, collumn, scopeCurrent, scopeBefore);
        //TODO Auto-generated constructor stub
    }
    public void setInsideScope(int insideScope){
        this.insideScope=insideScope;
    }
    public int getInsideScope (){
        return insideScope;
    }
    public ArrayList<Variable> getAtributes(){
        return attributes;
    }
    public void addAttribute(Variable newAttribute){
        attributes.add(newAttribute);
    }
    public void setMemorySize(int memorySize){
        this.memorySize=memorySize;
    }
    public int getMemorySize(){
        return this.memorySize;
    }
    public String toString() {
        return "{" +
            " type='" + getType() + "'" +
            ", name='" + getName() + "'" +
            ", signature='" + getSignature() + "'" +
            ", error='" + getError() + "'" +
            ", line='" + getLine() + "'" +
            ", collumn='" + getCollumn() + "'" +
            ", scopeCurrent='" + getScopeCurrent() + "'" +
            ", scopeBefore='" + getScopeBefore() + "'" +
            " insideScope='" + getInsideScope() + "'" +
            "}";
    }

}
