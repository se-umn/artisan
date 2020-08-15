package de.unipassau.abc.parsing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.Triplette;

public class ParsedTraceImpl implements ParsedTrace {

	private static final Logger logger = LoggerFactory.getLogger(ParsedTrace.class);

	// Thread Name vs Parsed Data
	private Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> content = new HashMap<>();

	private String systemTestID = null;

	public ParsedTraceImpl(String systemTestID) {
		this.systemTestID = systemTestID;
	}

	@Override
	public void reset() {
		for (Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> partial : this.content.values()) {
			// Having this into a single place instead of duplicate info would have
			// helped...
			partial.getFirst().reset();
			partial.getSecond().reset();
			partial.getThird().reset();
		}

	}

	@Override
	public Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> getUIThreadParsedTrace() {
		return getThreadParsedTraceFor(MAIN_THREAD);
	}

	@Override
	public Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> getParsedTrace() {
		return this.content;
	}

	@Override
	public Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> getThreadParsedTraceFor(String threadName) {
		return this.content.get(threadName);
	}

	@Override
	public void setContent(Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> content) {
		this.content = content;

	}

	@Override
	public void storeTo(File outputArtifactTo) throws FileNotFoundException, IOException {
		logger.info("Storing parsed trace to " + outputArtifactTo);
		String baseFileName = this.systemTestID;
		baseFileName += ".parsed.xml";

		// TODO Metadata include systemTestID and the list of thread/files?

		for (Entry<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> threadLocal : content
				.entrySet()) {

			String fileName = encode(threadLocal.getKey()) + "." + baseFileName;
			File storeTo = new File(outputArtifactTo, fileName);

			try (OutputStream writer = new FileOutputStream(storeTo)) {
				logger.info("\t Storing " + storeTo.getAbsolutePath());
				XStream xStream = new XStream();
				// Store the entire Entry, so we keep the threadName
				xStream.toXML(threadLocal, writer);
			}
		}
	}

	private String encode(String toEncode) {
		return toEncode.replaceAll("\\W+", "_");
	}

	public static ParsedTraceImpl loadFromDirectory(File sourceDirectory) {
		logger.info("Loading parsed trace from " + sourceDirectory);

		Map<String, // ThreadName
				Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> content = new HashMap<>();

		XStream xStream = new XStream(new StaxDriver());

		ParsedTraceImpl parsedTrace = null;

		for (File file : sourceDirectory.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".parsed.xml");
			}
		})) {
			if (parsedTrace == null) {
				// ModernAsyncTask_5.trace.log.parsed.xml -> trace
				String systemTestID = file.getName().replaceAll(".parsed.xml", "");
				systemTestID = systemTestID.split("\\.")[1];
				parsedTrace = new ParsedTraceImpl(systemTestID);
			}
			logger.info("\t Loading data from " + file);
			Entry<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> entry = (Entry<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>>) xStream
					.fromXML(file);
			content.put(entry.getKey(), entry.getValue());
		}

		parsedTrace.setContent(content);
		return parsedTrace;
	}

	// Mostly for testing
	@Override
	public int getThreadCount() {
		return content.keySet().size();
	}

}
