package de.unipassau.abc.carving;

import java.util.List;

import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.Pair;

public interface MethodCarver {

	public List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carve(List<MethodInvocation> methodInvocationsToCarve) throws CarvingException;
}
