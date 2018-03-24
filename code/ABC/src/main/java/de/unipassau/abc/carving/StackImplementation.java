package de.unipassau.abc.carving;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private ExecutionFlowGraph exectuionFlowGraph;

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

	private List<MethodInvocationMatcher> purityMatchers;

	private boolean purityFlag = false;
	private MethodInvocation pureMethod = null; // This is the method which sets
												// the purity flag

	final private MethodInvocationMatcher systemExitMatcher = MethodInvocationMatcher
			.byMethod("<java.lang.System: void exit(int)>");

	// State variable controlling the parser
	private boolean systemExitReached = false;

	public StackImplementation(List<MethodInvocationMatcher> purityMatchers) {
		this.purityMatchers = purityMatchers;
	}

	// This method is hard to test, and cannot be public since requires setup
	// from the method calling it
	private int parseMethodStart(int startLine, List<String> allLines,
			List<MethodInvocationMatcher> externalInterfaceMatchers) {
		logger.trace("StackImplementation.parseMethodStart()" + allLines.get(startLine));

		// Assuming that there's no ";" inside any of those elements !!!
		// Probably we should move to something structured, that is which encode
		// those elements more precisely !!
		// Turns out that Reference to Arrays have that !
		String[] tokens = allLines.get(startLine).split(";");

		// tokens[0] is METHOD_START_TOKEN
		String typeOfInvocation = tokens[1];
		String jimpleMethod = tokens[2];

		MethodInvocation methodInvocation = new MethodInvocation(jimpleMethod, invocationCount.incrementAndGet());
		methodInvocation.setInvocationType(typeOfInvocation);

		// Check with soot ?
		try {

			if (!typeOfInvocation.equals("ArrayOperation") ) {
				// This might be required to get to the method in the first
				// place
//				System.out.println(
//						"StackImplementation.parseMethodStart() Checking method visibility: " + methodInvocation);
				Scene.v().loadClassAndSupport(JimpleUtils.getClassNameForMethod(jimpleMethod));
				methodInvocation.setPrivate(Scene.v().getMethod(jimpleMethod).isPrivate());
			}

		} catch (Throwable e) {
			// This fails for java classes
			logger.info("StackImplementation.parseMethodStart() Swallow:  " + e);
//			e.printStackTrace();
		}

		// Check if this method belongs to an external interface
		for (MethodInvocationMatcher externalInterfaceMatcher : externalInterfaceMatchers) {
			if (externalInterfaceMatcher.matches(methodInvocation)) {
				methodInvocation.setBelongsToExternalInterface(true);
			} else {
				System.out.println(externalInterfaceMatcher + " does not match " + methodInvocation);
			}
		}

		if (isPure(methodInvocation)) {
			purityFlag = true;
			pureMethod = methodInvocation;
			logger.trace("StackImplementation.parseMethodEnd() Switch PURITY ON");
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
		callGraph.push(methodInvocation, purityFlag);

		if (!purityFlag) {
			// This also tracks parameter dependency
			dataDependencyGraph.addMethodInvocation(methodInvocation, actualParameters);
			exectuionFlowGraph.enqueueMethodInvocations(methodInvocation);
		} else {
			logger.trace(methodInvocation + " excluded by purity");
		}

		// Check if this method is System.exit
		if (systemExitMatcher.matches(methodInvocation)) {
			logger.debug("Reached System.exit " + methodInvocation);
			systemExitReached = true;
		}

		return peekIndex - 1;
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
		String xmlFile = tokens[2];
		// Static methods
		StringBuffer thisObject = new StringBuffer(tokens[3]);
		int peekIndex = startLine + 1;
		// Accumulate strings
		while (peekIndex < allLines.size() && !allLines.get(peekIndex).startsWith(Trace.METHOD_START_TOKEN)
				&& !allLines.get(peekIndex).startsWith(Trace.METHOD_END_TOKEN)
				&& !allLines.get(peekIndex).startsWith(Trace.METHOD_OBJECT_TOKEN)) {
			thisObject.append("\n");
			thisObject.append(allLines.get(peekIndex));
			peekIndex++;
		}

		// Static methods are automatically skipped
		ObjectInstance owner = new ObjectInstance(thisObject.toString());
		//
		MethodInvocation methodInvocation = callGraph.peek();
		methodInvocation.setOwner(owner);
		methodInvocation.setXmlDumpForOwner(xmlFile);

		if (!purityFlag) {
			dataDependencyGraph.addDataDependencyOnOwner(methodInvocation, thisObject.toString());
			exectuionFlowGraph.addOwnerToMethodInvocation(methodInvocation, thisObject.toString());
		}
		return peekIndex - 1;
	}

	private int parseMethodEnd(int startLine, List<String> allLines) {
		logger.trace("StackImplementation.parseMethodEnd() " + allLines.get(startLine));
		String[] tokens = allLines.get(startLine).split(";");
		// tokens[0] is METHOD_END_TOKEN

		String jimpleMethod = tokens[1];

		// Void methods are not reported...
		StringBuffer returnValue = new StringBuffer();
		String xmlFile = null;
		int peekIndex = startLine + 1;
		if (tokens.length > 2) {
			xmlFile = tokens[2];
			returnValue.append(tokens[3]);

			// Accumulate strings
			while (peekIndex < allLines.size() && !allLines.get(peekIndex).startsWith(Trace.METHOD_START_TOKEN)
					&& !allLines.get(peekIndex).startsWith(Trace.METHOD_END_TOKEN)
					&& !allLines.get(peekIndex).startsWith(Trace.METHOD_OBJECT_TOKEN)) {
				returnValue.append("\n");
				returnValue.append(allLines.get(peekIndex));
				peekIndex++;
			}
			// logger.info(">> Return value " + jimpleMethod + " returnValue " +
			// returnValue);

		}

		MethodInvocation methodInvocation = callGraph.pop();

		if (!purityFlag) {

			if (!JimpleUtils.isVoid(JimpleUtils.getReturnType(methodInvocation.getJimpleMethod()))) {
				methodInvocation.setXmlDumpForReturn(xmlFile);
			}

			// TODO This is tricky: one might actually encode the string null
			// !!!
			dataDependencyGraph.addDataDependencyOnReturn(methodInvocation,
					(returnValue.length() != 0) ? returnValue.toString() : null);

		}

		// Reset the purity state of the parser
		if (methodInvocation.equals(pureMethod)) {
			pureMethod = null;
			purityFlag = false;
			logger.trace("StackImplementation.parseMethodEnd() Switch PURITY OFF");
		}

		return peekIndex - 1;
	}

	private boolean isPure(MethodInvocation methodInvocation) {
		for (MethodInvocationMatcher purityMatcher : purityMatchers) {
			if (purityMatcher.matches(methodInvocation)) {
				logger.trace(methodInvocation + " is pure");
				return true;
			}
		}
		return false;
	}

	private int parseTraceFile(List<String> lines, // Lines of the trace file
			List<MethodInvocationMatcher> externalInterfaceMatchers, int initialPosition)
			throws FileNotFoundException, IOException {

		// Reinitialize all the parsing state variables
		exectuionFlowGraph = new ExecutionFlowGraph();
		dataDependencyGraph = new DataDependencyGraph();
		callGraph = new CallGraph();
		systemExitReached = false;
		//

		logger.debug("Start parsing from position " + initialPosition);

		int positionAfterSystemExit = -1;

		for (int i = initialPosition; i < lines.size(); i++) {
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
	 */
	public Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parseTrace(String traceFilePath,
			List<MethodInvocationMatcher> externalInterfaceMatchers) throws FileNotFoundException, IOException {

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
		result.put(UUID.randomUUID().toString(), new Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>(
				exectuionFlowGraph, dataDependencyGraph, callGraph));

		while (position != -1 && position < lines.size()) {
			position = parseTraceFile(lines, externalInterfaceMatchers, position);
			//
			result.put(UUID.randomUUID().toString(), new Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>(
					exectuionFlowGraph, dataDependencyGraph, callGraph));
		}

		return result;
	}

	public CallGraph getCallGraph() {
		return callGraph;
	}

	// public DuplicateGraph getDuplicateGraph() {
	// return duplicateGraph;
	// }

	public DataDependencyGraph getDataDependencyGraph() {
		return dataDependencyGraph;
	}

	public ExecutionFlowGraph getExectuionFlowGraph() {
		return exectuionFlowGraph;
	}

	// public boolean checkParamIsAObject(String param) {
	// logger.debug("checkParamIsAObject : The parameter is " + param);
	// return Graph_Details.instancesHashId.containsKey(param);
	// }

	// public void paramEdgeCreation(String param) {
	// Iterator<Map.Entry<String, ArrayList<String>>> it =
	// Graph_Details.instancesHashId.entrySet().iterator();
	//
	// while (it.hasNext()) {
	// Map.Entry<String, ArrayList<String>> entry = it.next();
	// String key = entry.getKey();
	// if (key.equals(param)) {
	// ArrayList<String> list = entry.getValue();
	// for (String s : list)
	// if (s.contains("<init()>")) {
	//
	// }
	// }
	// }
	// }
	//
	// /**
	// * Gives the parameter values for a method if parameter is present
	// *
	// * @param methodName
	// * - Full name of the method for which parameters are needed
	// * @return the parameter values
	// */
	// public String paramOfMethod(String methodName) {
	// if (Graph_Details.hashParam.containsKey(methodName))
	// return Graph_Details.hashParam.get(methodName);
	// return null;
	// }

	// // Why we need a differnt method here ?/
	// public void parentsCarve(String methodName, String objectOfMUT) {
	//
	// HashSet<String> parents = null; // sgv.getParents(methodName);
	//
	// for (String each : parents) {
	//
	// if (!(each.equalsIgnoreCase("main")) &&
	// !alreadyCoveredParents.contains(each)) {
	// // logger.debug("Old Valueee "+ each );
	// alreadyCoveredParents.add(each);
	// returnCountOfMUTBeforeParent(each);
	// each = each.substring(0, each.lastIndexOf('>') + 1);
	// // logger.debug("Valueee "+ each );
	//
	// // FIXME this looks really interesting... how to han
	// // Why this returns a null objectID?
	// List<String> objectIds = getObjectInstanceIdsForMethod(each);
	// if (objectIds.isEmpty()) {
	// System.err.println("ERROR: Object id is NULL for " + each);
	// return;
	// }
	// String objectID = objectIds.get(0);
	//
	// testCaseInstructions.clear();
	// extractConstructors(objectID);
	//
	// logger.debug("Entering helper method from parent");
	//
	// carveHelperMethod(objectOfMUT, Graph_Details.carvingMethod);
	//
	// addMethods();
	// methodCarve(each);
	// ArrayList<String> list = new ArrayList<String>(testCaseInstructions);
	// for (String s : list) {
	// for (String external : externalDependencyList) {
	// if (s.contains(external)) {
	//
	// if (!externalTestCases.isEmpty()) {
	//
	// testCaseInstructions.addAll(0, externalTestCases);
	// }
	// }
	// }
	// }
	// ArrayList<String> listFinal = new
	// ArrayList<String>(testCaseInstructions);
	// int index = Graph_Details.testCases.size() + 1;
	// Graph_Details.parentAndNumberOfMUTChildren.put(index,
	// numberOfMUTChildren(each));
	// Graph_Details.testCases.put(index, listFinal);
	// parentsCarve(each, objectOfMUT);
	// }
	//
	// }
	//
	// }

	// public int numberOfMUTChildren(String parent) {
	// HashSet<String> allChildren = null;// duplicateGraph.getChildren(parent);
	//
	// int countOfMUTChildren = 0;
	// for (String each : allChildren) {
	// if (each.contains(Graph_Details.carvingMethod))
	// countOfMUTChildren++;
	// }
	// return countOfMUTChildren;
	// }

	// // Not sure we still need this...
	// public void returnCountOfMUTBeforeParent(String parent) {
	// MethodInvocation wrongParent = new MethodInvocation(parent, parent, -1);
	// Set<String> previousMethods = exectuionFlowGraph.getParents(wrongParent);
	// for (String eachMethod : previousMethods) {
	// // logger.debug("The parents are :"+eachMethod);
	// if (eachMethod.contains(Graph_Details.carvingMethod)) {
	// String count = eachMethod.substring(eachMethod.lastIndexOf('>') + 1);
	// if (!count.isEmpty()) {
	//
	// Graph_Details.parentMethodAndMUTStartCount.put(parent,
	// Integer.parseInt(count));
	// } else
	// Graph_Details.parentMethodAndMUTStartCount.put(parent, 0);
	// // logger.debug("The edgeCount is : "+edgeCount);
	// } else {
	// Graph_Details.parentMethodAndMUTStartCount.put(parent, 0);
	// }
	// }
	//
	// }

	// public List<String> getObjectInstanceIdsForMethod(String jimpleMethod) {
	// List<String> methodTargetInstances = new ArrayList<>();
	// // TODO WHy this uses this data structure since we build the graph ?!
	// // Can we
	// // simply lookup the graph, find the nodes, and such ?
	// // We might use a cache and a lookup table, but shall go and point
	// // directly to the right instances/nodes
	// Iterator<Map.Entry<String, ArrayList<String>>> it =
	// Graph_Details.instancesHashId.entrySet().iterator();
	// while (it.hasNext()) {
	// Map.Entry<String, ArrayList<String>> entry = it.next();
	// ArrayList<String> list = entry.getValue();
	// String key = entry.getKey();
	// for (String s : list) {
	// if (s.equals(jimpleMethod)) {
	// methodTargetInstances.add(key);
	// }
	// }
	// }
	// // logger.error("Cannot find an object id for this method : " + method);
	// // return null;
	// return methodTargetInstances;
	//
	// }

	// /**
	// * Generates the first test case
	// *
	// * @param object-
	// * Id of the object whose method needs to be carved
	// * @param methodUnderTest
	// * - Method for carving
	// */
	// public void level0TestCarving(String objectInstance, String
	// methodUnderTest) {
	// logger.debug("The object id is " + objectInstance + " and the method to
	// carve is" + methodUnderTest);
	//
	// // THis contains the method invocations which build up the test case
	// testCaseInstructions.clear();
	//
	// // The followin implementation by Pallavi its an simple heuristic.
	//
	// // Ensures that recursively, all the constructors of all the
	// // objects/dependencies for the test are called !
	// extractConstructors(objectInstance);
	//
	// // FIXME Not sure we can reconstrcu paramters of the constructors as
	// // well !!!
	// // recreateDependenciesForContructor but why this is not simply
	// // carving the constructor ?
	//
	// // TODO This one no idea
	// // This one call carving to all the predecessors of the MUT... at
	// // least
	// // this is what it looks like.
	// // Probably to ensure only the relevant calls where made ?
	// carveHelperMethod(objectInstance, methodUnderTest);
	//
	// // Collects the methods which potentially change the state of the
	// // object.
	// // Why isn't this linked to the previous one ?!
	// listMethodsToAdd(objectInstance, methodUnderTest);
	//
	// // Why hrere there's no object id ?
	// methodCarve(methodUnderTest);
	//
	// // CArving: start from MUT
	// // Include all the invocations that happen before (Execution graph) -
	// // this excludes methods that happened after MUT
	// // NOTE: DataNode Dependency is not directed ! It links instances with
	// // method invocations !
	// // Include all the invocations that have are transitively reached via
	// // data dependency from MUT (DataNode dependency) - this excludes main
	// and
	// // caller methods, but includes also methods which produce parameters
	// // that are used somewhere in the chain
	// // Exclude all the invocations the are subsumed, i.e., that are
	// // reachable by the transitive closure of the call graph of any method
	// // (Call Graph)
	// setBasedMethodCarving(objectInstance, methodUnderTest);
	// ArrayList<String> list = new ArrayList<String>(testCaseInstructions);
	// // Collections.copy(list, testCaseInstructions);
	// for (String s : list) {
	// for (String external : externalDependencyList) {
	// if (s.contains(external)) {
	//
	// if (!externalTestCases.isEmpty()) {
	//
	// testCaseInstructions.addAll(0, externalTestCases);
	// }
	// }
	// }
	// }
	// ArrayList<String> listFinal = new
	// ArrayList<String>(testCaseInstructions);
	//
	// Graph_Details.testCases.put(1, listFinal);
	//
	// }

	// // WHY STATIC METHODS SHOULD BE TREATED DIFFERENTLY? They STILL REQUIRE
	// // PARAMETERS TO BE SETUP !
	// public void level0TestCarvingForStaticMethods(String method) {
	// testCaseInstructions.clear();
	//
	// // Why there's not object id here ?!
	// methodCarve(method);
	//
	// ArrayList<String> list = new ArrayList<String>(testCaseInstructions);
	//
	// for (String s : list) {
	// for (String external : externalDependencyList) {
	// if (s.contains(external)) {
	//
	// if (!externalTestCases.isEmpty()) {
	//
	// testCaseInstructions.addAll(0, externalTestCases);
	// }
	// }
	// }
	// }
	// ArrayList<String> listFinal = new
	// ArrayList<String>(testCaseInstructions);
	//
	// Graph_Details.testCases.put(1, listFinal);
	// // logger.debug("The test case is "+testCaseInstructions.get(1));
	// }

	// public String methodCarve(String methodName) {
	//
	// logger.debug("method carve " + methodName);
	// // TODO What's FLAG ? Some way to capture "static" methods? or
	// // constructors ?
	// if (!flag) {
	// // Why we cannot add the same method multiple times ?
	// if (!testCaseInstructions.contains(methodName)) {
	// testCaseInstructions.add(methodName);
	// }
	// } else {
	// if (!testCaseInstructions.contains(methodName))
	// testCaseInstructions.add(0, methodName);
	// }
	// // Basically we check if the method under test requires parameters, in
	// // that case we need to reconstruct them as well...
	// if (Graph_Details.hashParam.containsKey(methodName)) {
	// logger.debug("inside if method carve");
	// // Parameters are present
	// // Store all the parameters
	// // Take the parameter types as an arraylist
	// ArrayList<String> parameterTypes = parameterTypeReturn(methodName);
	//
	// for (String s : parameterTypes)
	// logger.trace("The type of parameter is " + s);
	//
	// // Parameters are present
	// // Get the actual VALUE for the paramters ? what if there's more ?
	// // what if those are objects ? Does this return (I hope) the
	// // reference to them?
	// String allParams = paramOfMethod(methodName);
	// // Store all the parameters
	// listOfParameters.clear();
	// listOfParameters.addAll(parameterList(allParams));
	//
	// // FIXME THis is wrong... but here I suspect we need to match
	// // methods and methodInvocations !
	// MethodInvocation wrong = new MethodInvocation(methodName, -1);
	//
	// if (dataDependencyGraph.vertexExists(wrong)) {
	// // There is atleast 1 parameter which is an object again
	// flag = false;
	// logger.debug("inside vertices if method carve");
	// Iterator<String> it = listOfParameters.iterator();
	// while (it.hasNext()) {
	// String each = it.next();
	// logger.debug(each);
	// if (Graph_Details.instancesHashId.containsKey(each)) {
	// // Object
	// // logger.debug("inside vertices second if method
	// // carve" +each);
	// // Instead of the object id as the parameter value I
	// // have replace with the
	// // init of that object
	// logger.debug("Heree");
	// for (String external : externalDependencyList) {
	// if (each.contains(external)) {
	// logger.debug("There is an external dependencies");
	// if (Graph_Details.instancesHashId.containsKey(each)) {
	// ArrayList<String> list = Graph_Details.instancesHashId.get(each);
	// for (String method : list) {
	// if (method.contains("init"))
	// externalinitCarve(each);
	// else
	// externalResourceExecution(method);
	// }
	// }
	//
	// } else {
	// logger.debug("Going for init carve");
	// extractConstructors(each);
	// }
	// }
	// if (externalDependencyList.isEmpty())
	// extractConstructors(each);
	//
	// }
	//
	// }
	// } else {
	// /**
	// * All parameters are primitive type Send a list of parameters
	// * and the entire method to Soot and there Soot handles the
	// * conversion and building function
	// *
	// */
	// // logger.debug("inside vertices else method carve");
	// // Check type of parameter for each
	// flag = false;
	// logger.debug("Entering parameter check");
	// Iterator<String> it = listOfParameters.iterator();
	// while (it.hasNext()) {
	// String each = it.next();
	// // logger.debug("Index of "+each+" is
	// // "+listOfParameters.indexOf(each));
	// String type = parameterTypes.get(listOfParameters.indexOf(each));
	// logger.debug("Paramter " + each);
	// logger.debug("The type is " + type);
	//
	// // Special case Hardcoded for the Employ/HotelBooking cases
	// // !
	// // if (type.equals("java.util.Scanner")) {
	// // logger.debug("Scanner object");
	// // String signature = "<java.util.Scanner: void
	// // <init>(java.io.InputStream)>";
	// // String parameterSignature = "<java.lang.System:
	// // java.io.InputStream in>";
	// // Graph_Details.hashParam.put(signature,
	// // parameterSignature);
	// // Graph_Details.methodAndTypeOfInvocation.put(signature,
	// // "SpecialInvokeExpr");
	// // testCaseInstructions.add(0, signature);
	// // } else
	// // if (type.equals("java.lang.String") || type.equals("int")
	// // || type.equals("double")
	// // || type.equals("boolean") || type.equals("float") ||
	// // type.equals("long")) {
	// //
	// // logger.debug("Primitive type");
	// //
	// // }
	// // Again this is a special case for Date ...
	// // else if (type.equals("java.util.Date")) {
	// // // String signature3="<java.util.Date: void <init>()>";
	// // // //String parameterSignature1="EEE MMM dd HH:mm:ss zzz
	// // // yyyy";
	// // // //Graph_Details.hashParam.put(signature1,
	// // // parameterSignature1);
	// // //
	// // Graph_Details.methodAndTypeOfInvocation.put(signature3,
	// // // "SpecialInvokeExpr");
	// // // testCaseInstructions.add(0,signature3);
	// // logger.debug("Date object");
	// // String signature2 = "<java.text.DateFormat:
	// // java.util.Date parse(java.lang.String)>";
	// // String parameterSignature2 = "Wed Aug 09 00:00:00 CEST
	// // 2017";
	// // Graph_Details.hashParam.put(signature2,
	// // parameterSignature2);
	// // Graph_Details.methodAndTypeOfInvocation.put(signature2,
	// // "VirtualInvokeExpr");
	// // Graph_Details.returnValues.put(signature2, "Wed Aug 09
	// // 00:00:00 CEST 2017");
	// // testCaseInstructions.add(0, signature2);
	// // String signature1 = "<java.text.SimpleDateFormat: void
	// // <init>(java.lang.String)>";
	// // String parameterSignature1 = "EEE MMM dd HH:mm:ss zzz
	// // yyyy";
	// // Graph_Details.hashParam.put(signature1,
	// // parameterSignature1);
	// // Graph_Details.methodAndTypeOfInvocation.put(signature1,
	// // "SpecialInvokeExpr");
	// // testCaseInstructions.add(0, signature1);
	// // }
	//
	// // If the parameter is primitive, it's byValue semantic so
	// // we don't to anything, otherwise we process the parameter
	// // byReference
	// if (!isPrimitive(type)) {
	// // else {
	// flag = true;
	// ArrayList<String> preFunction = methodWithreturnType(type);
	// for (String s : preFunction) {
	// methodCarve(s);
	// }
	// }
	// }
	// }
	//
	// } else {
	//
	// // No parameters are present
	// // logger.debug("Inside else");
	// flag = false;
	// return methodName;
	// }
	//
	// return methodName;
	// }

	private boolean isPrimitive(String type) {
		return type.equals("boolean") || type.equals("byte") || type.equals("char") || type.equals("short")
				|| type.equals("int") || type.equals("long") || type.equals("float") || type.equals("double") //
				// Include Strings in the primitive types
				|| type.equals("java.lang.String");
	}

	// /**
	// * This one carves only the methods of the CUT which comes BEFORE the
	// * invocation of the MUT.
	// *
	// * @param objectId
	// * @param methodUnderTest
	// */
	// public void carveHelperMethod(String objectId, String methodUnderTest) {
	// if (Graph_Details.instancesHashId.containsKey(objectId)) {
	//
	// List<String> allMethod = Graph_Details.instancesHashId.get(objectId);
	//
	// System.out.println("StackImplementation.carveHelperMethod() " +
	// allMethod);
	// // Probably this shall skip Constructors ?
	// for (String method : allMethod) {
	//
	// if (method.contains("<init>")) {
	// logger.debug("StackImplementation.carveHelperMethod() Skip constructor
	// : " + method);
	// continue;
	// }
	//
	// // How to indentify each and every invokation of this ?
	// // What if I invoke init, foo, foo, bar ? and want to carve foo
	// // ? and the second foo ?!
	// if (method.equals(methodUnderTest)) {
	// break;
	// } else {
	// // TODO Not sure this will work.. How shall we prevent to
	// // reinstaintiate and reconfigure the same object over and
	// // over ?
	// // WE literally check if the same method INVOCATION is
	// // ALREADY THERE !
	// // FIXME We need a specific class for these types !
	// // WHY There's no objectID here ?
	// methodCarve(method);
	// }
	// }
	// }
	//
	// }

	// /**
	// * Create a list of the methods to potentially we should add to the test
	// * case to recreate the state of the object. It overwrites the global
	// * variable "extraMethodsAdded"
	// **/
	// public void listMethodsToAdd(String objectId, String methodUnderTest) {
	//
	// logger.debug("method to carve " + methodUnderTest);
	//
	// // For the objectId get all the method executions
	//
	// if (Graph_Details.instancesHashId.containsKey(objectId) // &&
	// // !Graph_Details.carvingMethod.contains("HotelView") &&
	// // !Graph_Details.carvingMethod.contains("Room") // Oh Come on !!
	// ) {
	// // Get all the methods that were invoked on this instance. Note that
	// // the methods are ORDERED by the time of their invocation
	// // Note that ALL the invocations are here
	// List<String> allMethodInvocations =
	// Graph_Details.instancesHashId.get(objectId);
	// // Collects ALL the methods which were invoked BEFORE the method
	// // under test... Basically, this code collects all the methods which
	// // might change the internal state of the object
	// // am not sure how this can work
	// for (String method : allMethodInvocations) {
	// if (method.equals(methodUnderTest))
	// break;
	// else if (checkIfMethodHasTestCase(method))
	// break;
	// else {
	//
	// extraMethodsAdded.add(method);
	// }
	// }
	// }
	//
	// }

	// public void addMethods() {
	// for (String method : extraMethodsAdded) {
	// methodCarve(method);
	// }
	// }

	// Was this to verify the implementation is correct of for some other goal?
	// public boolean checkIfMethodHasTestCase(String method) {
	// if (!Graph_Details.testCases.isEmpty()) {
	// Iterator<Map.Entry<Integer, List<String>>> it =
	// Graph_Details.testCases.entrySet().iterator();
	// while (it.hasNext()) {
	// Map.Entry<Integer, List<String>> entry = it.next();
	// List<String> list = entry.getValue();
	// int key = entry.getKey();
	// if (list.contains(method))
	// return true;
	//
	// }
	//
	// }
	// return false;
	// }

	// public ArrayList<String> methodWithreturnType(String returnType) {
	// logger.debug(returnType);
	// ArrayList<String> matchingFunctions = new ArrayList<String>();
	// Iterator<Map.Entry<String, String>> it =
	// Graph_Details.returnTypes.entrySet().iterator();
	// logger.debug("The elements are(return Type) ::::::::::::::::::::::");
	// while (it.hasNext()) {
	//
	// Map.Entry<String, String> entry = it.next();
	//
	// String key = entry.getKey();
	//
	// String returnValue = entry.getValue();
	// if (returnValue.equals(returnType))
	// matchingFunctions.add(key);
	//
	// }
	//
	// return matchingFunctions;
	// }

	// /**
	// * Constructor for the given object. This captures the dependencies via
	// * parameters, if a paramter is required we need to generate it, so we go
	// * back in the graph and find how. Why we need this in the first place ?
	// *
	// * @param objectId
	// */
	// public void extractConstructors(String objectId) {
	//
	// String constructor = methodObject(objectId);
	//
	// logger.debug("The constructor for " + objectId + " is the method " +
	// constructor);
	//
	// // Not sure... WHy are we checking if the Construcotr is there?
	// if (!testCaseInstructions.contains(constructor))
	// testCaseInstructions.add(0, constructor);
	//
	// logger.debug(" Test cases " + testCaseInstructions);
	//
	// // hashParam stores the formal parameters for each methos...
	// if (Graph_Details.hashParam.containsKey(constructor)) {
	// // Parameters are present
	// // Store all the parameters
	// // Take the parameter types as an arraylist
	// ArrayList<String> parameterTypes = parameterTypeReturn(constructor);
	// // Parameters are present
	// String allParams = paramOfMethod(constructor);
	// logger.trace("Contructor actual parameters " + allParams);
	// // Store all the parameters
	// // listOfParameters.clear();
	//
	// listOfParameters.addAll(parameterList(allParams));
	//
	// // Check if we stored a dependency in the dependency graph...
	// // This assumes that the dependency graph is correct
	// MethodInvocation wrongConstructor = new MethodInvocation(constructor,
	// -1);
	// if (dataDependencyGraph.vertexExists(wrongConstructor)) {
	// logger.debug("Dependancy present");
	// // There is atleast 1 parameter which is an object again
	// // logger.debug("inside vertices if");
	// Iterator<String> it = listOfParameters.iterator();
	// while (it.hasNext()) {
	// String each = it.next();
	// logger.debug("The parameters is :: " + each);
	// if (Graph_Details.instancesHashId.containsKey(each)) {
	// // Object
	// logger.debug("inside vertices second if");
	// // Instead of the object id as the parameter value I
	// // have replace with the
	// // init of that object
	// int i = listOfParameters.indexOf(each);
	// if (i != -1)
	// listOfParameters.remove(i);
	//
	// extractConstructors(each);
	//
	// }
	//
	// }
	// } else {
	// /**
	// * All parameters are primitive type Send a list of parameters
	// * and the entire method to Soot and there Soot handles the
	// * conversion and building function
	// */
	// // logger.debug("inside vertices else");
	//
	// // return init;
	//
	// }
	// // ArrayList<String> list=new ArrayList<String>(listOfParameters);
	// // parameters.put(init, list);
	// } else {
	// // TODO This is impossible, throw exception !
	// // No parameters are present
	// // logger.debug("Inside else");
	// // return init;
	// }
	// // return init;
	// }

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

	// /**
	// * Returns the <init> (constructor?) for the particular object Note that
	// * staic initializers are <cinit> methods ... right?
	// *
	// * @param objectId
	// * @return init Method
	// */
	// public String methodObject(String objectId) {
	// String className = "";
	// logger.debug("Object id is " + objectId);
	//
	// // Extract the class name for this object
	// if (objectId.contains("@")) {
	// className = objectId.substring(0, objectId.indexOf("@"));
	// }
	// // else {
	// // className = "<java.sql.Date: void <init>(long)>";
	// // }
	// // logger.debug("method trying to find " + className);
	//
	// if (Graph_Details.instancesHashId.containsKey(objectId)) {
	// ArrayList<String> list = Graph_Details.instancesHashId.get(objectId);
	// for (String each : list) {
	// // If the item in list contains init(constructor) return the
	// // constructor back
	// // logger.debug("output "+each);
	// if (each.contains("<init>") && each.contains(className)) {
	// logger.debug("Returning " + each);
	// // returnValues=each;
	// return each;
	// }
	// }
	//
	// logger.error("Instance " + objectId + " was never initialized ?!");
	// // TODO Make this a app specific exception:
	// throw new RuntimeException("Instance " + objectId + " was never
	// initialized ?!");
	// }
	// logger.error("Cannot find instance " + objectId);
	// // TODO Make this a app specific exception:
	// throw new RuntimeException("Cannot find instance " + objectId);
	// // else {
	// // String staticMethod = "";
	// // // Graph_Details.showReturnValues();
	// // if (Graph_Details.isReturnValuePresent(objectId)) {
	// // logger.debug("return value bi map has the value");
	// // staticMethod = Graph_Details.returnValueIfPresent(objectId);
	// // return staticMethod;
	// // }
	// // }
	// // return "Not init";
	// }

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

	// /**
	// * Generates a list for external dependent objects
	// *
	// * @param methodName
	// * @return
	// */
	// public String externalResourceExecution(String methodName) {
	// if (!externalTestCases.contains(methodName))
	// externalTestCases.add(methodName);
	// if (Graph_Details.hashParam.containsKey(methodName)) {
	// // logger.debug("inside if method carve");
	// // Parameters are present
	// // Store all the parameters
	// // Take the parameter types as an arraylist
	// // ArrayList<String> parameterTypes=parameterTypeReturn(methodName);
	// // Parameters are present
	// String allParams = paramOfMethod(methodName);
	// // Store all the parameters
	// // listOfParameters.clear();
	// listOfParameters.addAll(parameterList(allParams));
	// MethodInvocation wrong = new MethodInvocation(methodName, -1);
	// if (dataDependencyGraph.vertexExists(wrong)) {
	// // There is atleast 1 parameter which is an object again
	// // logger.debug("inside vertices if method carve");
	// Iterator<String> it = listOfParameters.iterator();
	// while (it.hasNext()) {
	// String each = it.next();
	// logger.debug(each);
	// if (Graph_Details.instancesHashId.containsKey(each)) {
	// // Object
	// // logger.debug("inside vertices second if method
	// // carve" +each);
	// // Instead of the object id as the parameter value I
	// // have replace with the
	// // init of that object
	// //
	// listOfParameters.set(listOfParameters.indexOf(each),methodCarve(each));
	//
	// externalinitCarve(each);
	//
	// }
	//
	// }
	// } else {
	// /**
	// * All parameters are primitive type Send a list of parameters
	// * and the entire method to Soot and there Soot handles the
	// * conversion and building function
	// */
	// // logger.debug("inside vertices else method carve");
	// return methodName;
	//
	// }
	// // ArrayList<String> list=new ArrayList<String>(listOfParameters);
	// // parameters.put(methodName, list);
	// } else {
	//
	// // No parameters are present
	// // logger.debug("Inside else");
	// return methodName;
	// }
	//
	// return methodName;
	// }

	// /**
	// * Generates the init for external dependencies
	// *
	// * @param objectId
	// */
	// public void externalinitCarve(String objectId) {
	//
	// String init = methodObject(objectId);
	// // logger.debug("the method is "+init);
	//
	// if (!externalTestCases.contains(init))
	// externalTestCases.add(0, init);
	//
	// if (Graph_Details.hashParam.containsKey(init)) {
	//
	// // logger.debug("inside if");
	// // Parameters are present
	// // Store all the parameters
	// // Take the parameter types as an arraylist
	// ArrayList<String> parameterTypes = parameterTypeReturn(init);
	// // Parameters are present
	// String allParams = paramOfMethod(init);
	// // Store all the parameters
	// // listOfParameters.clear();
	// listOfParameters.addAll(parameterList(allParams));
	// // logger.debug("Print");
	//
	// MethodInvocation wrongConstructor = new MethodInvocation(init, -1);
	//
	// if (dataDependencyGraph.vertexExists(wrongConstructor)) {
	// // logger.debug("Print");
	// // There is atleast 1 parameter which is an object again
	// // logger.debug("inside vertices if");
	// Iterator<String> it = listOfParameters.iterator();
	// while (it.hasNext()) {
	// String each = it.next();
	// // logger.debug("The parameters is ::; "+each);
	// if (Graph_Details.instancesHashId.containsKey(each)) {
	// // Object
	// logger.debug("inside vertices second if");
	// // Instead of the object id as the parameter value I
	// // have replace with the
	// // init of that object
	// int i = listOfParameters.indexOf(each);
	// if (i != -1)
	// listOfParameters.remove(i);
	// // listOfParameters.set(listOfParameters.indexOf(each),initCarve(each));
	// extractConstructors(each);
	//
	// // parameterTypes.set(parameterTypes.indexOf(each),replace);
	//
	// }
	//
	// }
	// } else {
	// /**
	// * All parameters are primitive type Send a list of parameters
	// * and the entire method to Soot and there Soot handles the
	// * conversion and building function
	// */
	// // logger.debug("inside vertices else");
	//
	// // return init;
	//
	// }
	// // ArrayList<String> list=new ArrayList<String>(listOfParameters);
	// // parameters.put(init, list);
	// } else {
	//
	// // No parameters are present
	// // logger.debug("Inside else");
	// // return init;
	// }
	// // return init;
	// }

}
