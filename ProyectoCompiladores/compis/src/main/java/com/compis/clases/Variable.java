package com.compis.clases;

public class Variable extends CommonData {
    boolean isArray = false;
    int arraySize=0;
    public Variable(String type, String name, String signature, String error, String line, String collumn,
            int scopeCurrent, int scopeBefore) {
        super(type, name, signature, error, line, collumn, scopeCurrent, scopeBefore);
        // TODO Auto-generated constructor stub
    }
    public void setIsArray(boolean isArray){
        this.isArray=isArray;
    }
    public void setArraySize(int arraySize){
        this.arraySize=arraySize;
    }
    public boolean getIsArray(){
        return isArray;
    }
    public int getArraySize(){
        return arraySize;
    }

    @Override
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
            " isArray='" + getIsArray() + "'" +
            ", arraySize='" + getArraySize() + "'" +
            "}";
    }    
}
