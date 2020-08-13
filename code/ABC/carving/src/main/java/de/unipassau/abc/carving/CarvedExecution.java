package de.unipassau.abc.carving;

import java.util.Collection;

import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;

/**
 * This class holds all the fragments extracted during the carving. For the
 * moment this is just a DTO
 * 
 * @author gambi
 *
 */
public class CarvedExecution {

	public Collection<ExecutionFlowGraph> executionFlowGraphs;
	public Collection<DataDependencyGraph> dataDependencyGraphs;
	public Collection<CallGraph> callGraphs;

}
