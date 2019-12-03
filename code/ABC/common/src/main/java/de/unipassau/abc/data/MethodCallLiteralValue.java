package de.unipassau.abc.data;

import java.util.ArrayList;
import java.util.List;

import soot.Value;

public class MethodCallLiteralValue implements ValueNode {

    private int uniqueId;
    private MethodInvocation methodInvocation;
    List<DataNode> parameterMatchers;
    
    public MethodCallLiteralValue(int incrementAndGet, MethodInvocation methodInvocation) {
        this.uniqueId = incrementAndGet;
        this.methodInvocation = methodInvocation;
    }
    
    public ObjectInstance getOwner(){
        return this.methodInvocation.getOwner();
    }

    public List<DataNode> getActualParameterInstances() {
        return this.methodInvocation.getActualParameterInstances();
    }
    
    public List<DataNode> getParameterMatchers() {
        if( this.parameterMatchers == null ){
            parameterMatchers = new ArrayList<>();
            for(DataNode actualParameter : this.methodInvocation.getActualParameterInstances() ){
                parameterMatchers.add( PrimitiveNodeFactory.createParameterMatcherLiteralValue(actualParameter ));
            }
        }
        return this.parameterMatchers;
    }
    
    public String getMethodSignature(){
        return this.methodInvocation.getMethodSignature();
    }

    @Override
    public Value getData() {
        throw new RuntimeException("Not implemented");
    }

}
