package de.unipassau.abc.generation.mocks;

import java.util.ArrayList;
import java.util.List;

import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;

public class CarvingShadow {

	public final List<ExecutionFlowGraph> executionFlowGraphs;
	public final List<DataDependencyGraph> dataDependencyGraphs;

	public CarvingShadow() {
		executionFlowGraphs = new ArrayList<ExecutionFlowGraph>();
		dataDependencyGraphs = new ArrayList<DataDependencyGraph>();
	}
}
