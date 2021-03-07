package de.unipassau.abc.generation.mocks;

import de.unipassau.abc.data.MethodInvocation;

import java.util.List;

public class ShadowSequence {
    private MethodInvocation targetCallInstance;
    private MethodInvocation extractCallInstance;
    private List<MethodInvocation> setMockCallInstances;

    public ShadowSequence(MethodInvocation targetCallInstance, MethodInvocation extractCallInstance, List<MethodInvocation> setMockCallInstances) {
        this.targetCallInstance = targetCallInstance;
        this.extractCallInstance = extractCallInstance;
        this.setMockCallInstances = setMockCallInstances;
    }

    public MethodInvocation getTargetCallInstance() {
        return targetCallInstance;
    }

    public MethodInvocation getExtractCallInstance() {
        return extractCallInstance;
    }

    public List<MethodInvocation> getSetMockCallInstances() {
        return setMockCallInstances;
    }
}
