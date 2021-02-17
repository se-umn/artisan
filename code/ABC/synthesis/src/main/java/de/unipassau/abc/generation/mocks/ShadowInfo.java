package de.unipassau.abc.generation.mocks;

import de.unipassau.abc.data.MethodInvocation;
import java.util.List;

public class ShadowInfo {
    private MethodInvocation shadow;
    private List<MethodInvocation> calls;

    public ShadowInfo(MethodInvocation shadow, List<MethodInvocation> calls) {
        this.shadow = shadow;
        this.calls = calls;
    }

     public MethodInvocation getShadow() {
        return this.shadow;
    }

    public List<MethodInvocation> getCalls() {
        return this.calls;
    }
}
