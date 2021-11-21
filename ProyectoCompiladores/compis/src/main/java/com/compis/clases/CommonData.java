package com.compis.clases;

public class CommonData {
    String type;
    String name;
    String line;
    String collumn;
    int scopeCurrent;
    int scopeBefore;
    int offset;
    
    public CommonData(String type, String name, String line, String collumn, int scopeCurrent, int scopeBefore, int offset){
        this.type = type;
        this.name = name;
        this.line = line;
        this.collumn = collumn;
        this.scopeCurrent = scopeCurrent;
        this.scopeBefore = scopeBefore;
        this.offset = offset;
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

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
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
            "}";
    }
    
    
}
