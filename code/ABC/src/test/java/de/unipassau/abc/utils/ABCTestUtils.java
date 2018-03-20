package de.unipassau.abc.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.SystemUtils;

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
		final String fileExtension = ".java";
		try {
			Files.walkFileTree(outputDir.toPath(), new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if (file.toString().endsWith( fileExtension )) {
						System.out.println("PRINTING CONTENT OF FILE " + file );
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
	
	public static String buildJUnit4Classpath(){
		StringBuilder junitCPBuilder = new StringBuilder();
		for( String cpEntry : SystemUtils.JAVA_CLASS_PATH.split(File.pathSeparator) ){
			if( cpEntry.contains("junit-4")){
				junitCPBuilder.append( cpEntry ).append( File.pathSeparator );
			} else if( cpEntry.contains("hamcrest-core")){
				junitCPBuilder.append( cpEntry ).append( File.pathSeparator );
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
					if (file.toString().endsWith( fileExtension )) {
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
}
