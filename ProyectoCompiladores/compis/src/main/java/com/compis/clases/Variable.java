package com.compis.clases;

public class Variable extends CommonData {
    boolean isArray = false;
    int arraySize=0;
    boolean isParameter = false;
    boolean isGlobal = false;
    public Variable(String type, String name, String line, String collumn,
            int scopeCurrent, int scopeBefore, int offset) {
        super(type, name, line, collumn, scopeCurrent, scopeBefore, offset);
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
    public boolean getIsParameter(){
        return isParameter;
    }
    public void setIsParameter(boolean isParameter){
        this.isParameter=isParameter;
    }
    public boolean getIsGlobal(){
        return isGlobal;
    }
    public void setIsGlobal(boolean isGlobal){
        this.isGlobal=isGlobal;
    }

    @Override
    public String toString() {
        return "{" +
            " type='" + getType() + "'" +
            ", name='" + getName() + "'" +
            ", offset='" + getOffset() + "'" +
            ", line='" + getLine() + "'" +
            ", collumn='" + getCollumn() + "'" +
            ", scopeCurrent='" + getScopeCurrent() + "'" +
            ", scopeBefore='" + getScopeBefore() + "'" +
            " isArray='" + getIsArray() + "'" +
            ", arraySize='" + getArraySize() + "'" +
            "}";
    }    
}
