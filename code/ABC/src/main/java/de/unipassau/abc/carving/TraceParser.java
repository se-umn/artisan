package de.unipassau.abc.carving;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface TraceParser {

	public void parseTraceFile(String traceFilePath, List<MethodInvocationMatcher> externalInterfaceMatchers) throws FileNotFoundException, IOException;
}
