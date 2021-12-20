package de.unipassau.abc.parsing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.Triplette;

public interface ParsedTrace {

	// Is this platform specific ?
	public final String MAIN_THREAD = "[UI:main]";

	public Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> getParsedTrace();

	// Shorthand for getThreadParsedTraceFor(MAIN_THREAD)
	public Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> getUIThreadParsedTrace();

	public Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> getThreadParsedTraceFor(String threadName);

	void setContent(Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> content);

	void storeTo(File outputArtifactTo) throws FileNotFoundException, IOException;

	// Mostly for testing
	int getThreadCount();

	String traceFileName();

}