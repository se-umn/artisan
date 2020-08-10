package de.unipassau.abc.parsing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;

import de.unipassau.abc.carving.exceptions.CarvingException;

/**
 * Wrap a parser such that it can be called from the command line
 * 
 * @author gambitemp
 *
 */
public class Main {

	private static final Logger logger = LoggerFactory.getLogger(TraceParser.class);

	public interface ParserCLI {
		@Option(longName = "android-jar")
		File getAndroidJar();

		@Option(longName = "apk")
		File getApk();

		// This is optional. Storing the parsed traces makes it faster to re-use them
		@Option(longName = "store-artifacts-to", defaultToNull = true)
		File getOutputDir();

		// ALESSIO: Why we need more than one trace?
		@Option(longName = "trace-files")
		List<File> getTraceFiles();

		@Option(longName = "parser", defaultValue = "basic")
		String getParser();
	}

	public static void main(String[] args) throws IOException, CarvingException {
		long startTime = System.nanoTime();

		ParserCLI cli = CliFactory.parseArguments(ParserCLI.class, args);

		/*
		 * Soot is a singleton and exposes static methods only, so we encapsulate its
		 * configuration inside static method calls
		 */
		ParsingUtils.setupSoot(cli.getAndroidJar(), cli.getApk());

		/*
		 * A test of an application might result in generating multiple traces if the
		 * app is decomissioned from the memory. However, here we assume that each trace
		 * is independent and the main method simply parses and stores the results for
		 * later processing into an XML object.
		 */
		TraceParser parser;
		switch (cli.getParser()) {
		case "basic":
			parser = new TraceParserImpl();
			break;
		case "gremlin":
			parser = new GremlinParser();
			break;
		default:
			logger.warn("Unrecognized parser, use 'basic' instead");
			parser = new TraceParserImpl();
			break;
		}

		List<ParsedTrace> theParsedTraces = new ArrayList<>();

		for (File traceFile : cli.getTraceFiles()) {
			try {
				logger.info("Parsing Trace: " + traceFile);
				theParsedTraces.add(parser.parseTrace(traceFile));
			} catch (Throwable e) {
				logger.error("Failed to parse " + traceFile, e);
			}
		}

		/*
		 * Serialize the parsed graphs to files so maybe we can avoid re-parsing the
		 * trace over and over and we might be able to limit the memory footprint of the
		 * process
		 */

		File outputArtifactTo = cli.getOutputDir();

		if (outputArtifactTo != null) {
			if (!outputArtifactTo.isDirectory()) {
				throw new CarvingException("Wrong input: " + outputArtifactTo.getAbsolutePath()
						+ " is a file, but should be a directory instead");
			}

			if (!outputArtifactTo.exists()) {
				Files.createDirectories(outputArtifactTo.toPath(), new FileAttribute[] {});
			}

			for (ParsedTrace parsedTrace : theParsedTraces) {
				parsedTrace.storeTo(outputArtifactTo);
			}
		}
	}

}
