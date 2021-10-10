package com.compis.clases;

public class CommonData {
    String type;
    String name;
    String signature;
    String error;
    String line;
    String collumn;
    int scopeCurrent;
    int scopeBefore;
    
    public CommonData(String type, String name, String signature, String error, String line, String collumn, int scopeCurrent, int scopeBefore){
        this.type = type;
        this.name = name;
        this.signature = signature;
        this.error = error;
        this.line = line;
        this.collumn = collumn;
        this.scopeCurrent = scopeCurrent;
        this.scopeBefore = scopeBefore;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignature() {
        return this.signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getLine() {
        return this.line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getCollumn() {
        return this.collumn;
    }

    public void setCollumn(String collumn) {
        this.collumn = collumn;
    }

    public int getScopeCurrent() {
        return this.scopeCurrent;
    }

    public void setScopeCurrent(int scopeCurrent) {
        this.scopeCurrent = scopeCurrent;
    }

    public int getScopeBefore() {
        return this.scopeBefore;
    }

    public void setScopeBefore(int scopeBefore) {
        this.scopeBefore = scopeBefore;
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
            "}";
    }
    
    
}
