package de.unipassau.abc.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.SystemUtils;
import org.junit.Assert;

import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.data.Triplette;

public class ABCTestUtils {
	private final static FilenameFilter javaFileNameFilter = new FilenameFilter() {

		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".java");
		}
	};

	public static void printFiles(File outputDir, final String fileExtension) {
		try {
			Files.walkFileTree(outputDir.toPath(), new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if (file.toString().endsWith(fileExtension)) {
						System.out.println("PRINTING CONTENT OF FILE " + file);
						System.out.println("========================================");
						for (String line : Files.readAllLines(file, Charset.defaultCharset())) {
							System.out.println(line);
						}
					}
					return super.visitFile(file, attrs);
				}
			});
		} catch (IOException e) {
			org.junit.Assert.fail("Cannot count files " + e);
		}
		
	}
	public static void printJavaClasses(File outputDir) throws IOException {
		printFiles(outputDir, ".java");

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

	public static String buildJUnit4Classpath() {
		StringBuilder junitCPBuilder = new StringBuilder();
		for (String cpEntry : SystemUtils.JAVA_CLASS_PATH.split(File.pathSeparator)) {
			if (cpEntry.contains("junit-4")) {
				junitCPBuilder.append(cpEntry).append(File.pathSeparator);
			} else if (cpEntry.contains("hamcrest-core")) {
				junitCPBuilder.append(cpEntry).append(File.pathSeparator);
			}
		}
		junitCPBuilder.reverse().deleteCharAt(0).reverse();
		return junitCPBuilder.toString();

	}

	public static int countFiles(File outputDir, final String fileExtension) {
		final AtomicInteger count = new AtomicInteger(0);
		try {
			Files.walkFileTree(outputDir.toPath(), new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if (file.toString().endsWith(fileExtension)) {
						count.incrementAndGet();
					}
					return super.visitFile(file, attrs);
				}
			});
		} catch (IOException e) {
			org.junit.Assert.fail("Cannot count files " + e);
		}
		return count.get();
	}

	public static File writeTraceToTempFile(List<String> lines) {
		File traceFile;
		try {
			traceFile = Files.createTempFile("TEMP", "trace").toFile();
			traceFile.deleteOnExit();
			try (PrintStream out = new PrintStream(new FileOutputStream(traceFile))) {
				for (String line : lines)
					out.println(line);
			}
			return traceFile;
		} catch (IOException e) {
			Assert.fail("ABCTestUtils.writeTraceToTempFile() Raised " + e);
		}
		// This is dead code, but the compiler fails if this code is not here
		return null;
	}

	public static String getTestSubjectJar() {
		String traceJar = "./libs/testsubject-tests.jar"; // Eclipse testing

		if (!new File(traceJar).exists()) {
			traceJar = "../libs/testsubject-tests.jar"; // Actual usage ...
			if (!new File(traceJar).exists()) {
				throw new RuntimeException(new File(traceJar).getAbsolutePath() + " file is missing");
			}
		}

		return traceJar;

	}

}
