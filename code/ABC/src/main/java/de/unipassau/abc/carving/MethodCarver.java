package de.unipassau.abc.carving;

import java.util.List;

import de.unipassau.abc.data.Pair;

public interface MethodCarver {

	public List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carve(List<MethodInvocation> methodInvocationsToCarve);
}
