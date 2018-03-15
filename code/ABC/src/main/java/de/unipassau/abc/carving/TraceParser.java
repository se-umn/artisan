package de.unipassau.abc.carving;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface TraceParser {

	public void parseTraceFile(String traceFilePath) throws FileNotFoundException, IOException;
}
