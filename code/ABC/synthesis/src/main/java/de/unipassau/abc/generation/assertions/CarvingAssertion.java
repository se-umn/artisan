package de.unipassau.abc.generation.assertions;

import java.util.ArrayList;
import java.util.List;

import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;

/**
 * For the moment a simple DTO to contain execution flow and data dependency
 * information for each assertion
 * 
 * @author gambitemp
 *
 */
public class CarvingAssertion {

	public final List<ExecutionFlowGraph> executionFlowGraphs;
	public final List<DataDependencyGraph> dataDependencyGraphs;

	public CarvingAssertion() {
		executionFlowGraphs = new ArrayList<ExecutionFlowGraph>();
		dataDependencyGraphs = new ArrayList<DataDependencyGraph>();
	}

}
