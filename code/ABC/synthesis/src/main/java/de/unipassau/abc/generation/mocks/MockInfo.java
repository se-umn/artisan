package de.unipassau.abc.generation.mocks;

import de.unipassau.abc.data.MethodInvocation;
import java.util.List;

// TODO Alessio: Not sure what this represent
public class MockInfo {
    private MethodInvocation mock;
    private List<MethodInvocation> calls;

    public MockInfo(MethodInvocation mock, List<MethodInvocation> calls) {
        this.mock = mock;
        this.calls = calls;
    }

     public MethodInvocation getMock() {
        return this.mock;
    }

    public List<MethodInvocation> getCalls() {
        return this.calls;
    }
}
