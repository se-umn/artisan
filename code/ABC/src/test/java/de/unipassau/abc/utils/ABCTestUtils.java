package de.unipassau.abc.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

import de.unipassau.abc.carving.CallGraph;
import de.unipassau.abc.carving.DataDependencyGraph;
import de.unipassau.abc.carving.ExecutionFlowGraph;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.data.Triplette;

public class ABCTestUtils {
	private final static FilenameFilter javaFileNameFilter = new FilenameFilter() {

		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".java");
		}
	};
	
	public static void printJavaClasses(File outputDir) throws IOException {
		System.out.println("PRINTING CONTENT OF FILE " + outputDir );
		System.out.println("========================================");
		for (File javaFile : outputDir.listFiles(javaFileNameFilter)) {
			System.out.println("File: " + javaFile.getAbsolutePath());
			for (String line : Files.readAllLines(javaFile.toPath(), Charset.defaultCharset())) {
				System.out.println(line);
			}
		}

	}

	public static void visualize(Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace) {
		parsedTrace.getFirst().visualize();
		parsedTrace.getSecond().visualize();
		parsedTrace.getThird().visualize();
	}

	public static void visualize(List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests) {
		for (Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTest : carvedTests) {
			carvedTest.getFirst().visualize();
			carvedTest.getSecond().visualize();
		}

	}
}
