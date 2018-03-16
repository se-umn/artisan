package de.unipassau.abc.carving;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import de.unipassau.abc.data.Triplette;

public interface TraceParser {

	public Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parseTrace(String traceFilePath,
			List<MethodInvocationMatcher> externalInterfaceMatchers) throws FileNotFoundException, IOException;
}
