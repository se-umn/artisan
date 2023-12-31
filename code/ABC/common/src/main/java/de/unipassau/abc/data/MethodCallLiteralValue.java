package de.unipassau.abc.data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import soot.Value;

/**
 * What's this ? This is something we might need for mocking objects... smells
 * like an reflective object
 * 
 * @author gambitemp
 *
 */
public class MethodCallLiteralValue implements ValueNode, Cloneable {

    private int uniqueId;
    private MethodInvocation methodInvocation;
    List<DataNode> parameterMatchers;

    public MethodCallLiteralValue(int incrementAndGet, MethodInvocation methodInvocation) {
        this.uniqueId = incrementAndGet;
        this.methodInvocation = methodInvocation;
    }

    public MethodCallLiteralValue clone() {
        MethodCallLiteralValue cloned = null;
        try {
            cloned = (MethodCallLiteralValue) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        cloned.uniqueId = uniqueId;
        cloned.methodInvocation = methodInvocation.clone();
        cloned.parameterMatchers = parameterMatchers.stream().map(new Function<DataNode, DataNode>() {
            @Override
            public DataNode apply(DataNode t) {
                return t.clone();
            }
        }).collect(Collectors.toList());
        return cloned;
    }

    public ObjectInstance getOwner() {
        return this.methodInvocation.getOwner();
    }

    public List<DataNode> getActualParameterInstances() {
        return this.methodInvocation.getActualParameterInstances();
    }

    public List<DataNode> getParameterMatchers() {
        if (this.parameterMatchers == null) {
            parameterMatchers = new ArrayList<>();
            for (DataNode actualParameter : this.methodInvocation.getActualParameterInstances()) {
                parameterMatchers.add(PrimitiveNodeFactory.createParameterMatcherLiteralValue(actualParameter));
            }
        }
        return this.parameterMatchers;
    }

    public String getMethodSignature() {
        return this.methodInvocation.getMethodSignature();
    }

    @Override
    public Value getData() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public String getType() {
        throw new RuntimeException("Not implemented");
    }

}
