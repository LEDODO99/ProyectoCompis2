package com.compis.clases;

public class Struct extends CommonData{
    private int insideScope;
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
