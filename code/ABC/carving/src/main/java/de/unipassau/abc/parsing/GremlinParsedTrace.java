package de.unipassau.abc.parsing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.apache.tinkerpop.gremlin.structure.Graph;

import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.Triplette;

/**
 * This implementation of the parsed trace rely on a single, shared graph
 * structure that is exported in terms of views {@link ExecutionFlowGraph},
 * {@link DataDependencyGraph }and {@link CallGraph}
 * 
 * @author gambitemp
 *
 */
public class GremlinParsedTrace implements ParsedTrace {

	private final Graph graph;
	private final String traceID;

	public GremlinParsedTrace(String traceID, Graph graph) {
		this.traceID = traceID;
		this.graph = graph;
	}

	@Override
	public Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> getUIThreadParsedTrace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setContent(Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> content) {
		throw new RuntimeException("Do not call me !");

	}

	@Override
	public void storeTo(File outputArtifactTo) throws FileNotFoundException, IOException {
		// TODO Not sure this belongs here

	}

	@Override
	public int getThreadCount() {
		// TODO Not sure this belongs here
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String traceFileName() {
		return traceID;
	}

	@Override
	public Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> getParsedTrace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> getThreadParsedTraceFor(String threadName) {
		// TODO Auto-generated method stub
		return null;
	}

}
