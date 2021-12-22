package de.unipassau.abc.generation;

import java.io.File;
import java.io.IOException;

import com.github.javaparser.ast.CompilationUnit;

import de.unipassau.abc.generation.data.CarvedTest;
import de.unipassau.abc.generation.utils.TestCaseOrganizer;
import de.unipassau.abc.generation.utils.TestClass;
import de.unipassau.abc.generation.utils.TestMethodNamer;

public interface TestCaseWriter {

//	/**
//	 * Organize the given carved tests into test cases (aka classes) using the given
//	 * TestCaseGrouper and store them to files inside the outputFolder
//	 * 
//	 * @param outputFolder
//	 * @param carvedTestCases
//	 * @throws IOException
//	 */
//	public void write(File outputFolder, TestCaseOrganizer testOrganizer, CarvedTest... carvedTestCases) throws IOException;
    
    // This seems the only one we are really using, so 
    public CompilationUnit generateJUnitTestCase(TestClass testCase, TestMethodNamer testMethodNamer);
}
