package de.unipassau.abc.generation;

import java.io.File;
import java.io.IOException;

import de.unipassau.abc.generation.data.CarvedTest;
import de.unipassau.abc.generation.utils.TestCaseOrganizer;

public interface TestCaseWriter {

	/**
	 * Organize the given carved tests into test cases (aka classes) using the given
	 * TestCaseGrouper and store them to files inside the outputFolder
	 * 
	 * @param outputFolder
	 * @param carvedTestCases
	 * @throws IOException
	 */
	public void write(File outputFolder, TestCaseOrganizer testOrganizer, CarvedTest... carvedTestCases) throws IOException;
}
