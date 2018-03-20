package de.unipassau.abc.carving;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import de.unipassau.abc.data.Triplette;

public interface TraceParser {

	/**
	 * Return a structure representing the execution of several system tests.
	 * Each system test is uniquely identified by a string object
	 * 
	 * @param traceFilePath
	 * @param externalInterfaceMatchers
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parseTrace(String traceFilePath,
			List<MethodInvocationMatcher> externalInterfaceMatchers) throws FileNotFoundException, IOException;
}
