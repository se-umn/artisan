package de.unipassau.abc.carving;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.ABCUtils;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.tracing.Trace;
import de.unipassau.abc.utils.JimpleUtils;
import soot.Scene;

/**
 * @author gambi
 *
 */

public class StackImplementation implements TraceParser {

	private static final Logger logger = LoggerFactory.getLogger(StackImplementation.class);

	/*
	 * Link nodes by time. Nodes are actual method invocations of non system,
	 * non filtered classes. Not all methods, but only the methods which does
	 * not involve control flow... which is wrong !
	 */
	private ExecutionFlowGraph executionFlowGraph;

	/*
	 * Track data dependencies among invocations: return values, parameters, and
	 * method owner
	 */
	private DataDependencyGraph dataDependencyGraph;

	/*
	 * Link nodes by called-by, subsumed-by relation
	 */
	private CallGraph callGraph;

	/*
	 * Global ID to uniquely identify each method invocation traced
	 */
	private AtomicInteger invocationCount = new AtomicInteger(0);

	/*
	 * Identify the test setup calls
	 */
	private List<MethodInvocationMatcher> testSetupMatchers;

	// private boolean purityFlag = false;
	// private MethodInvocation pureMethod = null;
	// private List<MethodInvocationMatcher> pureMethods;

	final private MethodInvocationMatcher systemExitMatcher = MethodInvocationMatcher
			.byMethod("<java.lang.System: void exit(int)>");

	// State variable controlling the parser
	private boolean systemExitReached = false;

	public StackImplementation(List<MethodInvocationMatcher> testSetupMatchers) {
		this.testSetupMatchers = testSetupMatchers;
	}

	// This method is hard to test, and cannot be public since requires setup
	// from the method calling it
	private int parseMethodStart(int startLine, List<String> allLines,
			List<MethodInvocationMatcher> externalInterfaceMatchers) throws CarvingException {
		logger.trace("StackImplementation.parseMethodStart()" + allLines.get(startLine));

		// Assuming that there's no ";" inside any of those elements !!!
		// Probably we should move to something structured, that is which encode
		// those elements more precisely !!
		// Turns out that Reference to Arrays have that !
		String[] tokens = allLines.get(startLine).split(";");

		// tokens[0] is METHOD_START_TOKEN
		String typeOfInvocation = tokens[1];
		String jimpleMethod = tokens[2];

		jimpleMethod = findCorrectJimpleMethod(jimpleMethod);

		// Unless it returns a "new" string, in that case we need to capture the
		// fact that a new string node is generated !
		if (!Carver.STRINGS_AS_OBJECTS && JimpleUtils.isString(JimpleUtils.getClassNameForMethod(jimpleMethod))
				&& !JimpleUtils.isString(JimpleUtils.getReturnType(jimpleMethod))

		) {
			logger.debug(
					"StackImplementation.parseMethodStart() Strings are primitives we do not track their methods, unless return a String");
			// FIXME Not sure if this is startLine + 1 ?!
			return startLine;
		}

		////

		MethodInvocation methodInvocation = new MethodInvocation(jimpleMethod, invocationCount.incrementAndGet());
		methodInvocation.setInvocationType(typeOfInvocation);
		if ("StaticInvokeExpr".equals(typeOfInvocation) ||
		//
				"ClassOperation".equals(typeOfInvocation) || "StaticFieldOperation".equals(typeOfInvocation)
				|| "FieldOperation".equals(typeOfInvocation) || "StringOperation".equals(typeOfInvocation)
				|| "MainArgsOperation".equals(typeOfInvocation)

		) {
			methodInvocation.setStatic(true);
		}

		// Can use directly ABCUtils.lookup !
		// Check with soot ?
		try {

			if (!ABCUtils.ARTIFICIAL_METHODS.contains(typeOfInvocation)) {
				// if (!typeOfInvocation.equals("ClassOperation") &&
				// !typeOfInvocation.equals("ArrayOperation")
				// && !typeOfInvocation.equals("StringOperation") &&
				// !typeOfInvocation.equals("StaticFieldOperation")
				// && !typeOfInvocation.equals("FieldOperation")) {
				// This might be required to get to the method in the first
				// place
				Scene.v().loadClassAndSupport(JimpleUtils.getClassNameForMethod(jimpleMethod));
				methodInvocation.setPrivate(Scene.v().getMethod(jimpleMethod).isPrivate());
				// Not sure how to handle those...
				// methodInvocation.setBelongsToAbstractClass(
				// Scene.v().getMethod(
				// jimpleMethod).getDeclaringClass().isAbstract() );
			}

		} catch (Throwable e) {
			String correctMethod = findCorrectJimpleMethod(jimpleMethod);
			logger.warn("StackImplementation.parseMethodStart() ERROR : " + correctMethod);
			// This fails for java classes
			logger.info("StackImplementation.parseMethodStart() Swallow:  " + e.getMessage());
			// e.printStackTrace();
		}

		// Check if this method belongs to an external interface
		for (MethodInvocationMatcher externalInterfaceMatcher : externalInterfaceMatchers) {
			if (externalInterfaceMatcher.matches(methodInvocation)) {
				methodInvocation.setBelongsToExternalInterface(true);
			}
		}

		logger.trace("InvocationType for " + jimpleMethod + " is " + typeOfInvocation);

		String returnType = jimpleMethod.replace('<', ' ').replace('>', ' ').trim().split(" ")[1];
		logger.trace("Return Type " + returnType);
		//

		int peekIndex = startLine + 1;

		// FIXME This parsing is wrong: we need to check that if the formal
		// parameters are there, then also the actualParameters are !
		String[] actualParameters = new String[] {};
		if (tokens.length > 3) {
			// This removes the opening '('
			StringBuilder actualParametersBuilder = new StringBuilder(tokens[3].substring(1, tokens[3].length()));
			// Accumulate strings

			while (peekIndex < allLines.size() && !allLines.get(peekIndex).startsWith(Trace.METHOD_START_TOKEN)
					&& !allLines.get(peekIndex).startsWith(Trace.METHOD_END_TOKEN)
					&& !allLines.get(peekIndex).startsWith(Trace.METHOD_OBJECT_TOKEN)) {
				actualParametersBuilder.append("\n");
				actualParametersBuilder.append(allLines.get(peekIndex));
				peekIndex++;
			}

			actualParametersBuilder.deleteCharAt(actualParametersBuilder.length() - 1);
			logger.trace("Actual Parameters " + actualParametersBuilder.toString());

			actualParameters = actualParametersBuilder.toString().split(",");

		} else {
			// TODO Why this should be called only if there's no parameters ?
			// pushImplementation(jimpleMethod);
			// return startLine;
			logger.trace("No formal parameters");
		}

		// We push nevertheless
		callGraph.push(methodInvocation, false);

		dataDependencyGraph.addMethodInvocation(methodInvocation, actualParameters);
		executionFlowGraph.enqueueMethodInvocations(methodInvocation);

		// Check if this method is System.exit
		if (systemExitMatcher.matches(methodInvocation)) {
			logger.debug("Reached System.exit " + methodInvocation);
			systemExitReached = true;
		}

		return peekIndex - 1;
	}

	public static String findCorrectJimpleMethod(String jimpleMethod) throws CarvingException {

		// Shall we keep a cache here ?
		if (JimpleUtils.getClassNameForMethod(jimpleMethod).startsWith("abc")) {
			// This is an artificial method
			return jimpleMethod;
		}

		if (JimpleUtils.getClassNameForMethod(jimpleMethod).endsWith("[]")) {
			// This is an artificial method
			return jimpleMethod;
		}

		if (JimpleUtils.getMethodName(jimpleMethod).equals("<init>")) {
			return jimpleMethod;
		}

		return ABCUtils.lookUpMethod(jimpleMethod);

	}

	// This is called at the end of the invocation, right before methodEnd
	// So invocationCount is wrong. We need to rely on the Stack for this !
	private int parseMethodThis(int startLine, List<String> allLines) {
		logger.trace("StackImplementation.parseMethodThis() " + allLines.get(startLine));

		// FIXME First line tokens - Assuming that there's no ";" inside any of
		// those elements !!!
		// Probably we should move to something structured, that is which encode
		// those elements more precisely !!
		String[] tokens = allLines.get(startLine).split(";");
		// tokens[0] is METHOD_OBJECT_TOKEN
		String jimpleMethod = tokens[1];
		// This might be empty but the ";" MUST be there !
		// String xmlFile = tokens[2];
		// Static methods
		StringBuffer thisObject = new StringBuffer(tokens[2]);

		// Unless it returns a "new" string, in that case we need to capture the
		// fact that a new string node is generated !
		if (!Carver.STRINGS_AS_OBJECTS && JimpleUtils.isString(JimpleUtils.getClassNameForMethod(jimpleMethod))
				&& !JimpleUtils.isString(JimpleUtils.getReturnType(jimpleMethod))

		) {
			logger.info(
					"StackImplementation.parseMethodStart() Strings are primitives we do not track their methods, unless return a String");
			// FIXME Not sure if this is startLine + 1 ?!
			return startLine;
		}

		int peekIndex = startLine + 1;

		// Static methods are automatically skipped
		ObjectInstance owner = new ObjectInstance(thisObject.toString());
		//
		MethodInvocation methodInvocation = callGraph.peek();
		methodInvocation.setOwner(owner);
		// methodInvocation.setXmlDumpForOwner(xmlFile);

		// if (!purityFlag) {
		dataDependencyGraph.addDataDependencyOnOwner(methodInvocation, thisObject.toString());
		executionFlowGraph.addOwnerToMethodInvocation(methodInvocation, thisObject.toString());
		// }
		return peekIndex - 1;
	}

	private int parseMethodEnd(int startLine, List<String> allLines) throws CarvingException {
		logger.trace("StackImplementation.parseMethodEnd() " + allLines.get(startLine));
		String[] tokens = allLines.get(startLine).split(";");
		// tokens[0] is METHOD_END_TOKEN

		String jimpleMethod = tokens[1];

		// Unless it returns a "new" string, in that case we need to capture the
		// fact that a new string node is generated !
		if (!Carver.STRINGS_AS_OBJECTS && JimpleUtils.isString(JimpleUtils.getClassNameForMethod(jimpleMethod))
				&& !JimpleUtils.isString(JimpleUtils.getReturnType(jimpleMethod))

		) {
			logger.info(
					"StackImplementation.parseMethodStart() Strings are primitives we do not track their methods, unless return a String");
			// FIXME Not sure if this is startLine + 1 ?!
			return startLine;
		}

		// Void methods are not reported...
		StringBuffer returnValue = new StringBuffer();
		String ownerXmlFile = null;
		String returnXmlFile = null;

		// The ";;;".split() returns an empty array
		if (tokens.length > 2) {
			ownerXmlFile = tokens[2];
		}

		int peekIndex = startLine + 1;
		if (tokens.length > 3) {
			returnXmlFile = tokens[3];
			returnValue.append(tokens[4]);

			// Accumulate strings
			// while (peekIndex < allLines.size() &&
			// !allLines.get(peekIndex).startsWith(Trace.METHOD_START_TOKEN)
			// && !allLines.get(peekIndex).startsWith(Trace.METHOD_END_TOKEN)
			// &&
			// !allLines.get(peekIndex).startsWith(Trace.METHOD_OBJECT_TOKEN)) {
			// returnValue.append("\n");
			// returnValue.append(allLines.get(peekIndex));
			// peekIndex++;
			// }
			// logger.info(">> Return value " + jimpleMethod + " returnValue " +
			// returnValue);

		}

		MethodInvocation methodInvocation = callGraph.pop();

		while (!methodInvocation.getJimpleMethod().equals(findCorrectJimpleMethod(jimpleMethod))) {
			// This can be explained as the result of an exception being throw
			// inside the method, which cause the method end NOT to be reached !
			// throw new RuntimeException("Wrong call stack found " +
			// methodInvocation.getJimpleMethod() + " but expecting " +
			// jimpleMethod );
			logger.warn("Possibly Exceptional behavior. Try to skip " + methodInvocation.getJimpleMethod());
			methodInvocation = callGraph.pop();
		}

		if (!methodInvocation.isStatic()) {
			methodInvocation.setXmlDumpForOwner(ownerXmlFile);
		}

		if (!JimpleUtils.isVoid(JimpleUtils.getReturnType(methodInvocation.getJimpleMethod()))) {
			methodInvocation.setXmlDumpForReturn(returnXmlFile);
		}

		// Ideally there's no need to keep track of the entire method, it would
		// be enough to register the creation of a new string
		if (!Carver.STRINGS_AS_OBJECTS
				&& JimpleUtils.isString(JimpleUtils.getReturnType(methodInvocation.getJimpleMethod()))) {

			// The value of the string is stored in the xmlFile
			DataNode stringValueNode = dataDependencyGraph.addDataDependencyOnReturn(methodInvocation,
					(returnXmlFile.trim().length() != 0) ? returnXmlFile : null);
			// Store the additional metadata to look up strings later
			((PrimitiveValue) stringValueNode).setRefid(returnValue.toString());
		} else {

			dataDependencyGraph.addDataDependencyOnReturn(methodInvocation,
					(returnValue.length() != 0) ? returnValue.toString() : null);
		}

		// }

		// Reset the purity state of the parser
		// if (methodInvocation.equals(pureMethod)) {
		// pureMethod = null;
		// purityFlag = false;
		// logger.trace("StackImplementation.parseMethodEnd() Switch PURITY
		// OFF");
		// }

		return peekIndex - 1;
	}

	// private boolean isPure(MethodInvocation methodInvocation) {
	// for (MethodInvocationMatcher purityMatcher : purityMatchers) {
	// if (purityMatcher.matches(methodInvocation)) {
	// logger.trace(methodInvocation + " is pure");
	// return true;
	// }
	// }
	// return false;
	// }

	private int parseTraceFile(List<String> lines, // Lines of the trace file
			List<MethodInvocationMatcher> externalInterfaceMatchers, int initialPosition)
			throws FileNotFoundException, IOException, CarvingException {

		// Reinitialize all the parsing state variables
		executionFlowGraph = new ExecutionFlowGraph();
		dataDependencyGraph = new DataDependencyGraph();
		callGraph = new CallGraph();
		systemExitReached = false;
		//

		logger.debug("Start parsing from position " + initialPosition);

		int positionAfterSystemExit = -1;

		for (int i = initialPosition; i < lines.size(); i++) {

			logger.trace("\t\t Next line " + i);

			String each = lines.get(i);

			// if (each.contains("org.apache.commons.codec.binary.")) {
			// logger.warn("Skip " + each);
			// continue;
			// }

			if (each.startsWith(Trace.METHOD_START_TOKEN)) {
				i = parseMethodStart(i, lines, externalInterfaceMatchers);
			} else if (each.startsWith(Trace.METHOD_OBJECT_TOKEN)) {
				i = parseMethodThis(i, lines);
			} else if (each.startsWith(Trace.METHOD_END_TOKEN)) {
				i = parseMethodEnd(i, lines);
			} else {
				logger.error("WARNING SKIP THE FOLLOWING LINE ! " + each);
			}

			if (systemExitReached) {
				// i is the position OF system exit
				positionAfterSystemExit = i + 1;
				break;
			}
		}

		callGraph.verify();

		return positionAfterSystemExit;
	}

	/**
	 * There might be multiple test cases, each terminating by a call to
	 * System.exit().
	 * 
	 * @param stringsAsObjects
	 * @throws CarvingException
	 */
	public Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parseTrace(String traceFilePath,
			List<MethodInvocationMatcher> externalInterfaceMatchers)
			throws FileNotFoundException, IOException, CarvingException {

		Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> result = new HashMap<>();

		// Read the file into a set of string, this might be a problem for long
		// traces...
		List<String> lines = new ArrayList<>();
		lines.addAll(Files.readAllLines(Paths.get(traceFilePath), Charset.defaultCharset()));

		// Parse the first system test, start from 0 and ends up in position
		// (position of System.exit
		// TODO maybe int is not good, but using long is cumbersome. Better an
		// iterator at this point
		int position = parseTraceFile(lines, externalInterfaceMatchers, 0);
		//
		for (MethodInvocation mi : executionFlowGraph.getOrderedMethodInvocations()) {
			if (isTestSetupCall(mi)) {
				// System.out.println(" Matched test setup call " + mi);
				callGraph.markParentAndPruneAfter(mi);

				//
				List<MethodInvocation> orderedSlice = new ArrayList<>(callGraph.getAll());
				Collections.sort(orderedSlice);

				logger.info(
						"Level_0_MethodCarver.processExternalInterfaces() Refined context size " + orderedSlice.size());
				//
				executionFlowGraph = executionFlowGraph.getSubGraph(orderedSlice);
				dataDependencyGraph = dataDependencyGraph.getSubGraph(orderedSlice);

			}
		} //

		result.put(UUID.randomUUID().toString(), new Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>(
				executionFlowGraph, dataDependencyGraph, callGraph));

		while (position != -1 && position < lines.size()) {
			position = parseTraceFile(lines, externalInterfaceMatchers, position);
			//

			for (MethodInvocation mi : executionFlowGraph.getOrderedMethodInvocations()) {
				if (isTestSetupCall(mi)) {
					// System.out.println(" Matched test setup call " + mi);
					callGraph.markParentAndPruneAfter(mi);

					//
					List<MethodInvocation> orderedSlice = new ArrayList<>(callGraph.getAll());
					Collections.sort(orderedSlice);

					logger.info("Level_0_MethodCarver.processExternalInterfaces() Refined context size "
							+ orderedSlice.size());
					//
					executionFlowGraph = executionFlowGraph.getSubGraph(orderedSlice);
					dataDependencyGraph = dataDependencyGraph.getSubGraph(orderedSlice);

				}
			}
			//
			result.put(UUID.randomUUID().toString(), new Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>(
					executionFlowGraph, dataDependencyGraph, callGraph));
		}

		return result;
	}

	public boolean isTestSetupCall(MethodInvocation mi) {
		for (MethodInvocationMatcher testSetupMatcher : testSetupMatchers) {
			if (testSetupMatcher.matches(mi)) {
				return true;
			}
		}
		return false;
	}

	public CallGraph getCallGraph() {
		return callGraph;
	}

	public DataDependencyGraph getDataDependencyGraph() {
		return dataDependencyGraph;
	}

	public ExecutionFlowGraph getExectuionFlowGraph() {
		return executionFlowGraph;
	}

	private boolean isPrimitive(String type) {
		return type.equals("boolean") || type.equals("byte") || type.equals("char") || type.equals("short")
				|| type.equals("int") || type.equals("long") || type.equals("float") || type.equals("double") //
				// Include Strings in the primitive types
				|| type.equals("java.lang.String");
	}

	/**
	 * Return all parameters of a method
	 * 
	 * @return ArrayList - The list of parameter types
	 * @param methodName
	 *            - The method whose parameter types are needed
	 */
	public ArrayList<String> parameterTypeReturn(String method) {
		ArrayList<String> types;
		String parameterTypes = method.substring(method.indexOf("(") + 1, method.indexOf(")"));
		// logger.debug(parameterTypes);
		// if(!parameterTypes.isEmpty()){//not blank
		if (parameterTypes.contains(","))// Multiple parameters
			// Split on , and store in array list
			types = new ArrayList<String>(Arrays.asList(parameterTypes.split(",")));
		else// Single parameter
		{
			types = new ArrayList<String>();
			types.add(parameterTypes);
		}

		return types;

	}

	/**
	 * Return the parameter list from the given parameter string
	 * 
	 * @param allParams
	 * @return
	 */
	public CopyOnWriteArrayList<String> parameterList(String allParams) {
		CopyOnWriteArrayList<String> types;

		if (allParams.contains(","))// Multiple parameters
			// Split on , and store in array list
			types = new CopyOnWriteArrayList<String>(Arrays.asList(allParams.split(",")));
		else// Single parameter
		{
			types = new CopyOnWriteArrayList<String>();
			types.add(allParams);
		}

		return types;
	}

}
