package de.unipassau.carving;

import java.util.List;

import de.unipassau.data.Pair;

public interface MethodCarver {

	/**
	 * methodRegExp follows Jimple format with
	 * 
	 * @param methodInvocationMatcher
	 * @return
	 */
	public List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carve(MethodInvocationMatcher carveBy,
			MethodInvocationMatcher excludeBy);
}
