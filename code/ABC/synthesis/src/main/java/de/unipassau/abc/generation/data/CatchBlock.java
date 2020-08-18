package de.unipassau.abc.generation.data;

import java.util.List;

import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;

public class CatchBlock {

	private ExecutionFlowGraph executionFlowGraph;
	private DataDependencyGraph dataDependencyGraph;
	//
	private List<String> capturedExceptions;

	public CatchBlock(List<String> capturedExceptions, ExecutionFlowGraph executionFlowGraph,
			DataDependencyGraph dataDependencyGraph) {
		this.executionFlowGraph = executionFlowGraph;
		this.dataDependencyGraph = dataDependencyGraph;
		this.capturedExceptions = capturedExceptions;
	}

	public ExecutionFlowGraph getExecutionFlowGraph() {
		return executionFlowGraph;
	}

	public DataDependencyGraph getDataDependencyGraph() {
		return dataDependencyGraph;
	}

	public List<String> getCapturedExceptions() {
		return capturedExceptions;
	}

}
