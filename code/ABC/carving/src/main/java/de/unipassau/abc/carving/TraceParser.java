package de.unipassau.abc.carving;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.MethodInvocationMatcher;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.exceptions.ABCException;

public interface TraceParser {

	/**
	 * Return a structure representing the execution of several system tests. Each
	 * system test is uniquely identified by a string object
	 * 
	 * @param traceFilePath
	 * @param externalInterfaceMatchers
	 * @param stringsAsObjects
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws CarvingException
	 */
	public Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parseTrace(String traceFilePath,
			List<MethodInvocationMatcher> externalInterfaceMatchers)
			throws FileNotFoundException, IOException, ABCException;
}
