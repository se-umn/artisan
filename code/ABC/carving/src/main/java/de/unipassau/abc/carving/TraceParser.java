package de.unipassau.abc.carving;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.parsing.ParsedTrace;

public interface TraceParser {

	/**
	 * Return a structure representing the executions captured by the traces.
	 * 
	 * 
	 * @param traceFilePath
	 * @param externalInterfaceMatchers
	 * @param stringsAsObjects
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws CarvingException
	 */
	public ParsedTrace parseTrace(File traceFile) throws FileNotFoundException, IOException, ABCException;

}
