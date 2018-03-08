package de.unipassau.carving;

import java.util.List;

import de.unipassau.data.Pair;

public interface MethodCarver {

	/**
	 * methodRegExp follows Jimple format with 
	 * @param methodRegExp
	 * @return
	 */
	public List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carve(String methodRegExp);
}
