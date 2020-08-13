
package de.unipassau.abc.parsing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.instrumentation.Monitor;
import de.unipassau.abc.tracing.Trace;

/**
 * Generic parser that emits "events" that subclasses need to handle
 * 
 * @author gambitemp
 *
 */
public abstract class TraceParser {

	class ParsedLine {
		public String lineNumber;
		public String threadName;
		public String methodContext;
		public String methodToken;
		public String methodOwner;
		public String methodSignature;
		public String parametersOrReturnValue;
	}

	// TODO This patter could be improved...
	public static final Pattern threadPattern = Pattern.compile("\\[((UI:main)|((Modern)?AsyncTask #[1-9][0-9]*))]");
	protected static final Logger logger = LoggerFactory.getLogger(TraceParser.class);

	// The final parsed trace
	protected ParsedTrace parsedTrace;

	/**
	 * TODO This is pyblic mostly for testability. Parses a single line of the
	 * trace.Ensuresthe right number of tokens are in there orfail with an exception
	 */
	public ParsedLine parseLine(String line) throws ABCException {
		try {
			// Remove the "ABC::" tag
			line = line.replaceFirst(Trace.ABC_TAG, "").trim();

			String[] _tokens = line.split(Trace.DELIMITER);

			// Extract the method id/Line?
			String methodId = _tokens[0].split(" ")[0];

			// Extracts the identifier of the thread that executed the invocation
			String threadName = _tokens[0].split(" ")[1];
//			.replaceFirst("\\[", "").replaceFirst("\\]", "");

			// Extract the context of the invocation
			String context = _tokens[1];
//			.substring(_tokens[1].indexOf('['), _tokens[1].indexOf(']')).replaceFirst("\\[",
//					"");

			// Extract method token. Token is DEFINED with [ ] so we do not need to remove
			// them
			String methodToken = _tokens[2];
//			.substring(_tokens[2].indexOf('['), _tokens[2].indexOf(']'))
//					.replaceFirst("\\[", "");

			/*
			 * TODO Static calls might be reported with a null owner... I do not really like
			 * this, but maybe we can distinguish them using the method token ?
			 */
			String methodOwner = _tokens[3];

			// Method Signature this is the JIMPLE format
			String methodSignature = _tokens[4];

			// Input parameters for method invocation, otherwise return value
			// We need to remove the "(" ")"
			String parametersOrReturnValue = _tokens[5].substring(1, _tokens[5].length() - 1);

			// TODO Arrays ?
// TODO Special case of array ? - Create a test for this ?
			//
//		if (_tokens.length != 4) {
//			// Does the line contains an array as OWNER ?! I guess in the end arrays can
//			// have method calls ...
//			// [>>];[Lorg.ametro.catalog.entities.TransportType;@71559508;<java.lang.Object:
//			// java.lang.Object clone()>;(); got 5 instead of 4
//			if (_tokens[1].startsWith("[") && _tokens[2].startsWith("@")) {
//				tokens[0] = _tokens[0];
//				tokens[2] = _tokens[3];
//				tokens[3] = _tokens[4];
//
//				// Convert the array owner to the expected form
//
//				tokens[1] = JimpleUtils.getBaseArrayType(_tokens[1]) + "[]" + _tokens[2];
//
//			} else {
//				throw new ABCException("Cannot parse line " + line + " got " + _tokens.length + " instead of 4");
//			}
//		} else {
//			tokens = _tokens;
//		}
//
//		// Flag the call as library or user

//
//		// Count formalParameters
//		String[] formalParameters = JimpleUtils.getParameterList(methodSignature);
//
//		// At this point take the remainder of the String and parse the
//		// parameters or the return value
//		String parameterString = tokens[3];
//
//		// Remove the opening ( and closing )
//		parameterString = parameterString.substring(1, parameterString.length() - 1);
			ParsedLine parsedLine = new ParsedLine();

			parsedLine.lineNumber = methodId;
			parsedLine.methodContext = context;
			parsedLine.methodOwner = methodOwner;
			parsedLine.methodSignature = methodSignature;
			parsedLine.methodToken = methodToken;
			parsedLine.parametersOrReturnValue = parametersOrReturnValue;
			parsedLine.threadName = threadName;
			return parsedLine;
		} catch (Throwable t) {
			throw new ABCException("Cannot parse line", t);
		}
	}

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
	public ParsedTrace parseTrace(File traceFile) throws FileNotFoundException, IOException, ABCException {
		/*
		 * Ensures that the "backend" initialized the parsedTraceObject
		 */
		this.parsedTrace = setupParsedTrace(traceFile);

		/*
		 * This is global id of method invocations, acts like a global logical clock
		 */
		AtomicInteger globalInvocationCount = new AtomicInteger(0);

		/*
		 * Create for each execution thread (String:key) a structure that contains the
		 * ordered list of executed method calls, a graph of method calls dependencies
		 * via data dependency (read and produced), and via method call
		 */

		try (BufferedReader b = new BufferedReader(new FileReader(traceFile))) {

			String currentLine = "";
			int lineNumber = 0;
			long startTime = System.currentTimeMillis();

			while ((currentLine = b.readLine()) != null) {

				// Increment line count
				lineNumber++;

				if (lineNumber % 10000 == 0) {
					long elapsed = System.currentTimeMillis() - startTime;
					logger.info("Parsed " + lineNumber + " lines took "
							+ String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(elapsed),
									TimeUnit.MILLISECONDS.toSeconds(elapsed)
											- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsed))));
					System.gc();
				}

				try {
					ParsedLine tokens = parseLine(currentLine);
					if (tokens.methodSignature == null) {
						throw new ABCException("Missing method signature in trace. Offending line: " + currentLine);
					}

					// TODO Open an issue here to setup filtering of method calls

					switch (tokens.methodToken) {

					case Monitor.METHOD_START_TOKEN:
					case Monitor.LIB_METHOD_START_TOKEN:
						parseMethodInvocation(globalInvocationCount, tokens);
						break;
					case Monitor.SYNTHETIC_METHOD_START_TOKEN:
						parseSyntheticMethodInvocation(globalInvocationCount, tokens);
						break;
					case Monitor.PRIVATE_METHOD_START_TOKEN:
						parsePrivateMethodInvocation(globalInvocationCount, tokens);
						break;
					case Monitor.METHOD_END_TOKEN:
						parseMethodReturn(globalInvocationCount, tokens);
						break;
					case Monitor.METHOD_END_TOKEN_FROM_EXCEPTION:
						parseMethodReturnWithException(globalInvocationCount, tokens);
						break;
					default:
						throw new ABCException("Invalid token" + tokens.methodToken);
					}
				} catch (Throwable t) {
					logger.error("Error while parsing line " + currentLine, t);
					throw t;
				}
			}
		}
		endOfExecution();

		validate();

		return this.parsedTrace;

	}

	public final static Pattern params = Pattern.compile("\\((.*?)\\)");

	public String[] getFormalParameters(String jimpleMethod) {

		Matcher matchPattern = params.matcher(jimpleMethod);
		if (matchPattern.find()) {
			String t = matchPattern.group(1);
			if (t.length() == 0) {
				return new String[] {};
			}
			return t.split(",");
		}
		return new String[] {};
	}

	// TODO If this is used only somewhere else, move it to TraceParseImpl maybe
	public String[] getActualParameters(String methodSignature, String parameterString) {
		String[] formalParameters = JimpleUtils.getParameterList(methodSignature);
		String[] actualParametersOrReturnValue = new String[formalParameters.length];

		if (formalParameters.length == 0) {
			return actualParametersOrReturnValue;
		}

		// TODO Consider to rebuild the strings first and then parse using spli
		String[] tokens = parameterString.split(",");
		int pCount = 0; // formalParameters.length;
		boolean accumulateStringContent = false;
		String partialStringValue = null;

		for (String token : tokens) {

			// Empty String. Note that we cannot reliably rely on formal parameters as they
			// might be Object
			if (token.equals("[]")) {
				accumulateStringContent = false;
			}
			// Single Char Strings
			else if (token.contains("[") && token.contains("]")) {
				accumulateStringContent = false;
			} else if (token.startsWith("[") && !token.contains("@")) { // Arrays have [Type form...
				accumulateStringContent = true;
				// Initialize the String
				partialStringValue = "";
			} else if (token.endsWith("]")) {
				accumulateStringContent = false;
				// Append the last token, without the delimiter
				partialStringValue += token;
			}

			if (accumulateStringContent) {
				// If the token represents a string-bytes, accumulate the
				// elements
				partialStringValue += token + ",";
			} else {
				if (partialStringValue == null) {
					// Handle null at first position
					if (token.isEmpty()) {
						actualParametersOrReturnValue[pCount] = null;
					} else {
						actualParametersOrReturnValue[pCount] = token;
					}
				} else {
					actualParametersOrReturnValue[pCount] = partialStringValue;
				}
				pCount++;
				partialStringValue = null;
			}
		}

		assert actualParametersOrReturnValue.length == formalParameters.length : "actual and formal parameters do not match";

		return actualParametersOrReturnValue;

	}

	// TODO Maybe filtering should be app-specific ?
	public boolean filter(String methodSignature) {
		if (methodSignature.contains("java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)")) {
			return true;
		}
		// else if( methodSignature.contains("" ) ){ return true; }
		else {
			return false;
		}
	}

	// TODO Move to decorator
//	private boolean isFragment(SootClass sootClass) {
//		return sootClass != null && !sootClass.isInterface() && ( //
//		Scene.v().getActiveHierarchy().isClassSubclassOfIncluding(sootClass, scFragment)
//				|| Scene.v().getActiveHierarchy().isClassSubclassOfIncluding(sootClass, scSupportFragment));
//	}

	// TODO Move to decorator
//	private boolean isActivity(SootClass sootClass) {
//		return sootClass != null && !sootClass.isInterface() && ( //
//		Scene.v().getActiveHierarchy().isClassSubclassOfIncluding(sootClass, scActivity)
//				|| Scene.v().getActiveHierarchy().isClassSubclassOfIncluding(sootClass, scSupportActivity));
//	}

	// TODO I would keep the parser generic and define DECORATORS that take the
	// parsed trace as input and include
	// the Android specific data there...
//	protected final SootClass scActivity = Scene.v().getSootClassUnsafe(JimpleUtils.ACTIVITY_CLASS);
//	protected final SootClass scFragment = Scene.v().getSootClassUnsafe(JimpleUtils.FRAGMENT_CLASS);
//	protected final SootClass scSupportActivity = Scene.v().getSootClassUnsafe(JimpleUtils.SUPPORT_ACTIVITY_CLASS);
//	protected final SootClass scSupportFragment = Scene.v().getSootClassUnsafe(JimpleUtils.SUPPORT_FRAGMENT_CLASS);

	public abstract ParsedTrace setupParsedTrace(File traceFile) throws ABCException;

	public abstract void validate() throws ABCException;

	public abstract void parsePrivateMethodInvocation(AtomicInteger globalInvocationCount, ParsedLine tokens)
			throws ABCException;

	public abstract void parseSyntheticMethodInvocation(AtomicInteger globalInvocationCount, ParsedLine tokens)
			throws ABCException;

	public abstract void parseMethodReturnWithException(AtomicInteger globalInvocationCount, ParsedLine tokens)
			throws ABCException;

	public abstract void parseMethodReturn(AtomicInteger globalInvocationCount, ParsedLine tokens) throws ABCException;

	public abstract void parseMethodInvocation(AtomicInteger globalInvocationCount, ParsedLine tokens)
			throws ABCException;

	public abstract void endOfExecution();
}
