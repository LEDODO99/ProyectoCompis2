package com.compis.clases;

import java.util.ArrayList;

public class Method extends CommonData {

    ArrayList<Variable> parameters = new ArrayList<>();
    int returnDirectionOfset = 0;
    int methodSize = 4;
    ArrayList<ArrayList<Variable>> localVars = new ArrayList<>();

    public Method(String type, String name,  String line, String collumn,
            int scopeCurrent, int scopeBefore, int offset) {
        super(type, name, line, collumn, scopeCurrent, scopeBefore, offset);
        // TODO Auto-generated constructor stub
    }
    public void addParameter(Variable newPar){
        newPar.setOffset(4);
        updateParametersOffset(newPar.getType());
        parameters.add(newPar);
    }

    public ArrayList<Variable> getParameters(){
        return parameters;
    }

    private void updateParametersOffset(String newParamType){
        int paramAddition = 1;
        if(newParamType.equals("int")){
            paramAddition=4;
        }
        for (int i=0; i<parameters.size();i++){
            parameters.get(i).setOffset(parameters.get(i).getOffset()+paramAddition);
        }
        methodSize+=paramAddition;
    
    }
    public void addLocalVar(Variable newVar){
        if (!isVarInParams(newVar.getName())){
            if (newVar.getType().equals("int")){
                returnDirectionOfset+=4;
                for (int i=0; i<localVars.size();i++){
                    for (int j=0; j<localVars.get(i).size();j++)
                        localVars.get(i).get(j).setOffset(localVars.get(i).get(j).getOffset()+4);
                }
                methodSize+=4;
            }
            else{
                returnDirectionOfset+=1;
                for (int i=0; i<localVars.size();i++){
                    for (int j=0; j<localVars.get(i).size();j++)
                        localVars.get(i).get(j).setOffset(localVars.get(i).get(j).getOffset()+1);
                }
                methodSize+=1;
            }
            if (localVars.size()>0){
                localVars.get(localVars.size()-1).add(newVar);
            }else{
                localVars.add(new ArrayList<>());
                localVars.get(localVars.size()-1).add(newVar);
            }
        }
    }
    public ArrayList<Variable> getLocalVars(){
        ArrayList<Variable> ar = new ArrayList<>();
        for (int i=0; i<localVars.size(); i++){
            for (int j=0; j<localVars.get(i).size();j++){
                ar.add(localVars.get(i).get(j));
            }
        }
        return ar;
    }
    public int getVariableOffset (String varName){
        ArrayList<Variable> vltemp = getLocalVars();
        for (int i=0; i<vltemp.size();i++){
            if(vltemp.get(i).getName().equals(varName)){
                return vltemp.get(i).getOffset();
            }
        }
        for (int i=0; i<parameters.size(); i++){
            if (parameters.get(i).getName().equals(varName)){
                return returnDirectionOfset + parameters.get(i).getOffset();
            }
        }
        return 0;
    }
    public int getDirectionOffset(){
        return returnDirectionOfset;
    }
    public int goUp(){
        if (localVars.size()>0){
            ArrayList<Variable> aListEliminar = localVars.get(localVars.size()-1);
            int espacioALiberar = 0;
            for (int i = 0; i<aListEliminar.size();i++){
                if(aListEliminar.get(i).getType().equals("int"))
                    espacioALiberar+=4;
                else
                    espacioALiberar+=1;
            }
            returnDirectionOfset-=espacioALiberar;
            methodSize-=espacioALiberar;
            localVars.remove(localVars.size()-1);
            return espacioALiberar;
        }
        return 0;
    }
    public int getLastBlockSize(){
        if (localVars.size()>0){
            ArrayList<Variable> aListEliminar = localVars.get(localVars.size()-1);
            int espacioALiberar = 0;
            for (int i = 0; i<aListEliminar.size();i++){
                if(aListEliminar.get(i).getType().equals("int"))
                    espacioALiberar+=4;
                else
                    espacioALiberar+=1;
            }
            return espacioALiberar;
        }
        return 0;
    }
    public int getMethodSize(){
        return this.methodSize;
    }
    public void addScope(){
        localVars.add(new ArrayList<>());
    }
    private boolean isVarInParams(String varname){
        boolean isIt = false;

        for (int i=0; i<parameters.size(); i++){
            if (varname.equals(parameters.get(i).getName()))
                return true;
        }

        return isIt;
    }
    
}
