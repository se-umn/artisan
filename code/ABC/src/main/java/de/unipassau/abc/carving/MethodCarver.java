package de.unipassau.abc.carving;

import java.util.List;

import de.unipassau.abc.data.Pair;

public interface MethodCarver {

	/**
	 * methodRegExp follows Jimple format with
	 * 
	 * @param methodInvocationMatcher
	 * @return
	 */
	public List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carve(MethodInvocationMatcher carveBy, List<MethodInvocationMatcher> excludeBy);
}
