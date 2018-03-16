package de.unipassau.abc.generation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.SystemUtils;
import org.jboss.util.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.ABCUtils;
import de.unipassau.abc.carving.DataDependencyGraph;
import de.unipassau.abc.carving.ExecutionFlowGraph;
import de.unipassau.abc.carving.MethodInvocation;
import de.unipassau.abc.carving.ObjectInstance;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.instrumentation.UtilInstrumenter;
import de.unipassau.abc.utils.JimpleUtils;
import soot.Local;
import soot.Modifier;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.VoidType;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.Stmt;
import soot.jimple.toolkits.annotation.j5anno.AnnotationGenerator;
import soot.options.Options;
import soot.util.Chain;

public class TestGenerator {
	// Per Method
	// private static final AtomicInteger localId = new AtomicInteger(0);

	private static final Logger logger = LoggerFactory.getLogger(TestGenerator.class);

	// Point to jar of target project. This is the original code, not the
	// instrumented one
	private String projectLib;

	public TestGenerator(String projectLib) {
		this.projectLib = projectLib;
	}

	private void setupSoot() {
		// System Settings Begin
		Options.v().set_allow_phantom_refs(true);
		Options.v().set_whole_program(true);
		// This would set soot cp as the current running app cp
		// Options.v().set_soot_classpath(System.getProperty("java.path"));

		String junit4Jar = null;
		String hamcrestCoreJar = null;

		for (String cpEntry : SystemUtils.JAVA_CLASS_PATH.split(File.pathSeparator)) {
			if (cpEntry.contains("junit-4")) {
				junit4Jar = cpEntry;
			} else if (cpEntry.contains("hamcrest-core")) {
				hamcrestCoreJar = cpEntry;
			}
		}

		String traceJar = ABCUtils.getTraceJar();

		ArrayList<String> necessaryJar = new ArrayList<String>();
		// Include here JUnit and Hamcrest
		necessaryJar.add(this.projectLib);
		necessaryJar.add(junit4Jar);
		necessaryJar.add(hamcrestCoreJar);
		necessaryJar.add(traceJar);

		/// TODO Maybe we need to filter out things ?
		// TODO Maybe we need to include XStream and XMLPull

		// This might be needed for the EVALUATION and Level+1 Carved tests
		// necessaryJar.add("./src/main/resources/Assertion.jar");

		/*
		 * To process a JAR file, just use the same option but provide a path to
		 * a JAR instead of a directory.
		 */
		Options.v().set_process_dir(necessaryJar);
		// Patch to work on Mac OS X
		System.setProperty("os.name", "Whatever");
		Scene.v().loadNecessaryClasses();
		System.setProperty("os.name", "Mac OS X");
	}

	/**
	 * Test case generation requires only the list of instructions and the data
	 * dependencies between them. We obtain those by carving.
	 * 
	 * We generate one test class for each CUT and one test for each MUT
	 * 
	 * TODO If we reset the count for the local, HERE we can easily find
	 * duplicate test cases...
	 * 
	 * @param carvedTests
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public Collection<SootClass> generateTestCases(List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests)
			throws IOException, InterruptedException {

		// contains directly on SootClass
		Map<String, SootClass> testClasses = new HashMap<>();

		// Why we need to initialize soot here otherwise we cannot create
		// methods and classes, and such
		// To probably there's also something else to include here
		setupSoot();

		for (Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTest : carvedTests) {

			// Get the mut, which by definition is the last invocation executed
			MethodInvocation mut = carvedTest.getFirst().getLastMethodInvocation();

			// Somehow this does include the executions we removed ?!
			logger.info("Generate Test: " + mut);

			String classUnderTest = JimpleUtils.getClassNameForMethod(mut.getJimpleMethod());

			// TODO Not sure this works !
			if (!testClasses.containsKey(classUnderTest)) {
				testClasses.put(classUnderTest, getTestClass(classUnderTest));
			}
			SootClass testClass = testClasses.get(classUnderTest);

			// TODO For the moment we name tests after their position and MUT
			// ,i.e., last element
			generateAndAddTestMethodToTestClass(testClass, carvedTest);
		}

		return testClasses.values();
	}

	// THIS IS MOSTLY TAKEN FROM TEST CASE FACTORY
	// REFACTOR !!
	private AtomicInteger generatedTestCases = new AtomicInteger(0);

	/*
	 * generate a new test method add add that to the class unless there's
	 * already an equivalent one
	 */
	private void generateAndAddTestMethodToTestClass(SootClass testClass,
			Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTest) {

		// For each element in the DataDependencyGraph create
		ExecutionFlowGraph executionFlowGraph = carvedTest.getFirst();
		DataDependencyGraph dataDependencyGraph = carvedTest.getSecond();

		/*
		 * Standard JUnit 4 Test methods are public void and annotated
		 * with @Test. They also do not take parameters
		 * 
		 */
		String testMethodName = "test_" + generatedTestCases.incrementAndGet();
		logger.debug("Generating test method " + testMethodName + " for class " + testClass.getName());

		SootMethod testMethod = new SootMethod(testMethodName, Collections.<Type>emptyList(), VoidType.v(),
				Modifier.PUBLIC);

		SootClass e = Scene.v().loadClassAndSupport("java.lang.Exception");
		testMethod.addExceptionIfAbsent(e);

		// Annotate the method with org.junit.Test
		AnnotationGenerator.v().annotate(testMethod, org.junit.Test.class);

		// Build the body from the input list of statements
		JimpleBody body = Jimple.v().newBody(testMethod);
		testMethod.setActiveBody(body);

		// Keep track of local per type per method
		Map<String, AtomicInteger> localMap = new HashMap<>();

		// Generate the Local Variables for the Method
		for (ObjectInstance node : dataDependencyGraph.getObjectInstances()) {
			String type = node.getType();

			if (!localMap.containsKey(type)) {
				localMap.put(type, new AtomicInteger(0));
			}
			int localId = localMap.get(type).getAndIncrement();

			String localName = RefType.v(type).getClassName();
			localName = localName.substring(localName.lastIndexOf('.') + 1);
			// Apply code conventions
			localName = localName.replaceFirst(localName.substring(0, 1), localName.substring(0, 1).toLowerCase());
			//
			Local localVariable = Jimple.v().newLocal(localName + localId, RefType.v(type));
			body.getLocals().add(localVariable);

			// Debug
			logger.trace("  >>>> Create a new local variable " + localVariable + " of type " + type + " and node "
					+ node + " " + node.hashCode());
			//
			dataDependencyGraph.setValueFor(node, localVariable);
		}

		// Generating method body
		Chain<Unit> units = body.getUnits();
		List<MethodInvocation> orderedMethodInvocations = executionFlowGraph.getOrderedMethodInvocations();
		// By definition the MUT is the last invoked
		MethodInvocation methodInvocationToCarve = executionFlowGraph.getLastMethodInvocation();
		for (MethodInvocation methodInvocation : orderedMethodInvocations) {

			// Get the object upon which we invoke the method
			Local objLocal = dataDependencyGraph.getObjectLocalFor(methodInvocation);
			// Parameters can be objects and primitive
			// Note that those must be ORDERED, since parameters are positional
			// !

			// Look at the parameterCheck(m) method !
			List<Value> parametersValues = dataDependencyGraph.getParametersSootValueFor(methodInvocation);

			// Store the return value to build the data dependencies
			Value returnValue = dataDependencyGraph.getReturnObjectLocalFor(methodInvocation);
			Local actualReturnValue = null;
			if (returnValue instanceof Local) {
				actualReturnValue = (Local) returnValue;
			} else {
				logger.debug("We do not track return values of primitive types");
			}

			// When we process the methodInvocationToCarve, we also generate a
			// final assignment that enable regression testing
			// Stmt returnStmt = null;
			if (methodInvocation.equals(methodInvocationToCarve)
					&& !JimpleUtils.isVoid(JimpleUtils.getReturnType(methodInvocation.getJimpleMethod()))) {
				String type = JimpleUtils.getReturnType(methodInvocation.getJimpleMethod());
				// This shall be null at this point, since we do not use the
				// return value ?
				actualReturnValue = Jimple.v().newLocal("returnValue", RefType.v(type));
				body.getLocals().add(actualReturnValue);
				// returnObjLocal = UtilInstrumenter.generateFreshLocal(body,
				// RefType.v(type));

				// Debug
				System.out.println("  >>>> Create a new local variable " + actualReturnValue + " of type " + type
						+ " to store the output of " + methodInvocationToCarve);
				// FIXME: I have no idea of what a RetStmt is, but it does the
				// trick, which is it results in an actual java assignment
				// returnStmt = Jimple.v().newRetStmt(returnObjLocal);
			}

			// We need to use add because some method invocations are actually
			// more than 1 unit !
			addUnitFor(units, methodInvocation, objLocal, parametersValues, actualReturnValue);

		}

		MethodInvocation methodInvocationUnderTest = executionFlowGraph.getLastMethodInvocation();

		if (!methodInvocationUnderTest.isStatic()) {
			Value actualOwner = dataDependencyGraph.getObjectLocalFor(methodInvocationUnderTest);
			Value expectedOwner = UtilInstrumenter.generateExpectedValueForOwner(methodInvocationUnderTest, body, units);
			AssertionGenerator.gerenateRegressionAssertionOnOwner(body, units, expectedOwner, actualOwner);
		}
		
		if (!JimpleUtils.isVoid(JimpleUtils.getReturnType(methodInvocationUnderTest.getJimpleMethod()))) {
			Value expectedReturnValue = dataDependencyGraph.getReturnObjectLocalFor(methodInvocationUnderTest);
			if (JimpleUtils.isPrimitive(expectedReturnValue.getType())
					|| JimpleUtils.isString(expectedReturnValue.getType())) {
				// Do nothing, since the expectedReturnValue is a primitive value
			} else {
				// Otherwise, load expectations from file:
				// Reads this from XML and introduce the code to load this
				// from XML
				expectedReturnValue = UtilInstrumenter.generateExpectedValueForReturn(methodInvocationUnderTest, body, units);
			}

			// Find the
			Local actualReturnValue = null; 
			for( Local local : body.getLocals().getElementsUnsorted() ){
				if( "returnValue".equals( local.getName()) ){
					actualReturnValue = local;
					break;
				}
			}
			AssertionGenerator.gerenateRegressionAssertionOnReturnValue(body, units, expectedReturnValue, actualReturnValue);
		}

		// Capture the return value of the MUT if that return something

		// Insert final void return
		units.add(Jimple.v().newReturnVoidStmt());

		// This is to handle the assertion of Level +1 test cases
		// lastsavedMethod = methodName;
		// if (index != 1)
		// addNewAssertionToJunit4TestCase(testClass, localVariables,
		// testStatements, s, testMethodName, times, index);

		// TODO Method naming might be fixed using tentativeMethod ID vs actual
		// method id
		// Check if there's an equivalent method in the class already:
		if (!JimpleUtils.classContainsEquivalentMethod(testClass, testMethod)) {
			// Associate the method with the class (i.e., reference to this).
			// Required since the method is not static
			testClass.addMethod(testMethod);
			// This identifies static methods
			body.insertIdentityStmts();
		} else {
			logger.debug("Found duplicate method " + testMethodName);
		}

	}

	//
	// Generate the right InvokeStmt based on the data inside MethodInvocation
	// FIXME At the moment we do not really have a nice way to track primitive
	// types, so if there's a data dep involving any of them we simply cut that
	// away and use the concrete values instead. This might make the resulting
	// test code less readable but also simpler. The awkward case is the one
	// like this:
	//
	/*
	 * int x = A.get(); x=x+1; A.set(x);
	 * 
	 * in this case we take A.get() while carving A.set because of A not because
	 * of the data dependency x
	 * 
	 * The carved test will be: int x = A.get(); A.set(X); where X is the actual
	 * value we observed at runtime.
	 * 
	 */
	private void addUnitFor(Chain<Unit> units, MethodInvocation methodInvocation, Local objLocal,
			List<Value> parametersValues, Local returnObjLocal) {
		logger.trace("Processing method invocation " + methodInvocation.getJimpleMethod() + " -- "
				+ methodInvocation.getInvocationType() + " on local " + objLocal + " with params " + parametersValues
				+ " to return " + returnObjLocal);

		Jimple jimple = Jimple.v();

		// This is supposed to be the constructor !
		SootMethod method = Scene.v().getMethod(methodInvocation.getJimpleMethod());

		switch (methodInvocation.getInvocationType()) {
		case "SpecialInvokeExpr":
			// This is a constructor so I need to call new and then <init> with
			// the right parameteres
			RefType cType = RefType.v(JimpleUtils.getClassNameForMethod(methodInvocation.getJimpleMethod()));
			// Call "new"
			Stmt assignToNew = jimple.newAssignStmt(objLocal, jimple.newNewExpr(cType));
			// Call init + parameters
			InvokeStmt invokeStmt = jimple
					.newInvokeStmt(jimple.newSpecialInvokeExpr(objLocal, method.makeRef(), parametersValues));

			// This automatically captures the return value of <init> in case we
			// need it for regression assertions
			units.add(assignToNew);
			units.add(invokeStmt);
			break;
		case "VirtualInvokeExpr":
			if (returnObjLocal != null) {
				Stmt assignStmt = jimple.newAssignStmt(returnObjLocal,
						jimple.newVirtualInvokeExpr(objLocal, method.makeRef(), parametersValues));
				units.add(assignStmt);
			} else {
				Stmt callStmt = jimple
						.newInvokeStmt(jimple.newVirtualInvokeExpr(objLocal, method.makeRef(), parametersValues));
				units.add(callStmt);
			}
			break;

		case "StaticInvokeExpr":
			if (returnObjLocal != null) {
				final Stmt assignStmt = jimple.newAssignStmt(returnObjLocal,
						jimple.newStaticInvokeExpr(method.makeRef(), parametersValues));
				units.add(assignStmt);
			} else {
				final Stmt callStmt = jimple
						.newInvokeStmt(jimple.newStaticInvokeExpr(method.makeRef(), parametersValues));
				units.add(callStmt);
			}
			break;

		default:
			logger.error("Unexpected Invocation type " + methodInvocation.getInvocationType());
			throw new NotImplementedException("Unexpected Invocation type " + methodInvocation.getInvocationType());
		}
	}

	private SootClass getTestClass(String classUnderTest) {
		String simpleClassName = classUnderTest.replaceAll("\\.", " ")
				.split(" ")[classUnderTest.replaceAll("\\.", " ").split(" ").length - 1];
		String testCaseName = "Test" + simpleClassName;

		SootClass sClass = new SootClass(testCaseName, Modifier.PUBLIC);
		sClass.setSuperclass(Scene.v().getSootClass("java.lang.Object"));
		Scene.v().addClass(sClass);

		return sClass;
	}

	// public void createTestCaseForEach(List<String> testCases, int times, int
	// index)
	// throws IOException, InterruptedException {
	//
	// logger.debug("The test cases are for single call function
	// ::::::::::::::::::::" + testCases);
	// Scene.v().loadClassAndSupport("java.lang.System");
	//
	// // Where those additional depependencies come from?
	// // Scene.v().loadClassAndSupport("java.util.Date");
	// // Scene.v().loadClassAndSupport("java.text.SimpleDateFormat");
	//
	// countOfLocal.set(1);
	// int count = 1;
	// String typeOfInvocation = "";
	// String methodObject = "";
	//
	// String lastData = testCases.get(testCases.size() - 1);
	//
	// // TODO Give appropriate name to test case
	// String testClassName = "Test_" + Graph_Details.carvingMethod
	// .substring(Graph_Details.carvingMethod.indexOf("<") + 1,
	// Graph_Details.carvingMethod.indexOf(":"));
	//
	// logger.debug("TestGenerator.createTestCaseForEach() test Class Name " +
	// testClassName);
	//
	// String allDataSplitWithSpace[] = lastData.split(" ");
	// String MUTSplit[] = Graph_Details.carvingMethod.split(" ");
	//
	// String testMethodName = "test_" + MUTSplit[2].substring(0,
	// MUTSplit[2].indexOf('(')) + "_"
	// + allDataSplitWithSpace[2].substring(0,
	// allDataSplitWithSpace[2].indexOf('('));
	//
	// // Every type of object would be stored to the hashSet and for each a
	// // local variable is created
	// for (String m : testCases) {
	// if (m.equals("Not init")) {
	// continue;
	// }
	// methodObject = m.substring(m.indexOf("<") + 1, m.indexOf(":"));
	// typesOfMethod.add(methodObject);
	// }
	//
	// /*
	// * For each type a local variable is created and corresponding type and
	// * variables are stored in a HashMap
	// */
	// Iterator<String> it = typesOfMethod.iterator();
	// while (it.hasNext()) {
	// String type = it.next();
	// String l = "objLocal" + countOfLocal.getAndIncrement();
	// Local objLocal = Jimple.v().newLocal(l, RefType.v(type));
	// //
	// System.out.println("Create a new local variable " + l + " of type " +
	// type);
	// typesAndLocals.put(type, objLocal);
	// localVariables.add(objLocal);
	// }
	//
	// Jimple jimple = Jimple.v();
	// for (String m : testCases) {
	// if (m.equals("Not init")) {
	// continue;
	// }
	// logger.debug("The method is " + m);
	// if (Graph_Details.methodAndTypeOfInvocation.containsKey(m))
	// typeOfInvocation = Graph_Details.methodAndTypeOfInvocation.get(m);
	// logger.debug("Type of invocation is " + typeOfInvocation);
	// methodObject = m.substring(m.indexOf("<") + 1, m.indexOf(":"));
	// Local objLocal = typesAndLocals.get(methodObject);
	// if (m.equals("<java.util.Scanner: void <init>(java.io.InputStream)>")) {
	// Local l = Jimple.v().newLocal("input", RefType.v("java.io.InputStream"));
	// final RefType foStreamType = RefType.v("java.util.Scanner");
	// localVariables.add(l);
	// statements.add(Jimple.v().newAssignStmt(l, Jimple.v().newStaticFieldRef(
	// Scene.v().getField("<java.lang.System: java.io.InputStream
	// in>").makeRef())));
	// Local l1 = Jimple.v().newLocal("scan", RefType.v("java.util.Scanner"));
	// localVariables.add(l1);
	// final SootMethod constructor = Scene.v()
	// .getMethod("<java.util.Scanner: void <init>(java.io.InputStream)>");
	// final Stmt assignStmt = jimple.newAssignStmt(l1,
	// jimple.newNewExpr(foStreamType));
	// Stmt invokeStmt = jimple.newInvokeStmt(jimple.newSpecialInvokeExpr(l1,
	// constructor.makeRef(), l));
	//
	// objectAndValue.put(m, l1);
	// statements.add(assignStmt);
	// statements.add(invokeStmt);
	// }
	//
	// if (typeOfInvocation.equals("SpecialInvokeExpr")
	// && !m.equals("<java.util.Scanner: void <init>(java.io.InputStream)>")) {
	// logger.debug("Inside special invoke if");
	// Stmt invokeStmt;
	// objectAndValue.put(m, objLocal);
	// RefType cType = RefType.v(methodObject);
	// Stmt s = jimple.newAssignStmt(objLocal, jimple.newNewExpr(cType));
	// SootMethod constructor = Scene.v().getMethod(m);
	// List<Value> argsToinsert = parameterCheck(m);
	// List<Value> finalArgsToInsert = new ArrayList<Value>(argsToinsert);
	// if (!argsToinsert.isEmpty()) {
	// for (Value v : argsToinsert) {
	// logger.debug("Special invoke args " + v);
	// int localcount = 1;
	//
	// }
	// }
	// argsToinsert.clear();
	// argsToinsert.addAll(finalArgsToInsert);
	// logger.debug("The final list of arguments is : " + argsToinsert);
	// final Stmt assignStmt;
	//
	// if (!argsToinsert.isEmpty())
	// invokeStmt = jimple
	// .newInvokeStmt(jimple.newSpecialInvokeExpr(objLocal,
	// constructor.makeRef(), argsToinsert));
	// else
	// invokeStmt = jimple.newInvokeStmt(jimple.newSpecialInvokeExpr(objLocal,
	// constructor.makeRef()));
	//
	// statements.add(s);
	// statements.add(invokeStmt);
	// }
	// if (typeOfInvocation.equals("VirtualInvokeExpr")) {
	// if (m.equals("<java.text.DateFormat: java.util.Date
	// parse(java.lang.String)>")) {
	//
	// objLocal = typesAndLocals.get("java.text.SimpleDateFormat");
	// logger.debug("Date referral" + objLocal);
	// SootMethod callingMethod = Scene.v()
	// .getMethod("<java.text.DateFormat: java.util.Date
	// parse(java.lang.String)>");
	// // Stmt
	// // callStmt=jimple.newInvokeStmt(jimple.newVirtualInvokeExpr(objLocal,
	// // callingMethod.makeRef(),StringConstant.v("Wed Aug 09
	// // 00:00:00 CEST 2017")));
	// // statements.add(callStmt);
	//
	// Local dateLocal = jimple.newLocal("dateLocal" + count,
	// RefType.v("java.util.Date"));
	// typesAndLocals.put("java.util.Date", dateLocal);
	// localVariables.add(dateLocal);
	// Stmt assignStmt = jimple.newAssignStmt(dateLocal,
	// jimple.newVirtualInvokeExpr(objLocal,
	// callingMethod.makeRef(), StringConstant.v("Wed Aug 09 00:00:00 CEST
	// 2017")));
	// statements.add(assignStmt);
	// count++;
	// continue;
	// }
	// Local returnValueLocal = Jimple.v().newLocal("returnValueLocal" +
	// countOfLocal.getAndIncrement(),
	// RefType.v("java.lang.Object"));
	// // typesAndLocals.put(type, objLocal);
	// localVariables.add(returnValueLocal);
	// logger.debug("Inside virtual invoke if");
	// objectAndValue.put(m, objLocal);
	// SootMethod callingMethod = Scene.v().getMethod(m);
	// final Stmt callStmt;
	// List<Value> argsToinsert = parameterCheck(m);
	// logger.debug("Arguments : " + argsToinsert);
	// if (!argsToinsert.isEmpty()) {
	// callStmt = jimple.newInvokeStmt(
	// jimple.newVirtualInvokeExpr(objLocal, callingMethod.makeRef(),
	// argsToinsert));
	// if (Graph_Details.returnValues.containsKey(m)) {
	// // String
	// // returnValues=Graph_Details.returnValueIfPresent(m);
	//
	// // if(!returnValues.equals("null")){
	// logger.debug("Return values");
	// Stmt assignStmt = jimple.newAssignStmt(returnValueLocal,
	// jimple.newVirtualInvokeExpr(objLocal, callingMethod.makeRef(),
	// argsToinsert));
	// Stmt returnStmt = jimple.newRetStmt(returnValueLocal);
	// statements.add(assignStmt);
	// if (m.equals(Graph_Details.carvingMethod)) {
	// logger.debug("Adding Method under test " + Graph_Details.carvingMethod);
	// statements.add(returnStmt);
	// }
	// /*
	// * } else statements.add(callStmt);
	// */
	// } else
	// statements.add(callStmt);
	// } else {
	// callStmt = jimple.newInvokeStmt(jimple.newVirtualInvokeExpr(objLocal,
	// callingMethod.makeRef()));
	// if (Graph_Details.returnValues.containsKey(m)) {
	// // List<String> returnValues=new
	// // ArrayList<String>(Graph_Details.returnValues.get(m));
	// // logger.debug("The return value is :
	// // "+returnValues);
	// // if(!returnValues.equals("null")){
	// logger.debug("Adding assignment statement");
	// Stmt assignStmt = jimple.newAssignStmt(returnValueLocal,
	// jimple.newVirtualInvokeExpr(objLocal, callingMethod.makeRef()));
	//
	// Stmt returnStmt = jimple.newRetStmt(returnValueLocal);
	// statements.add(assignStmt);
	// if (m.equals(Graph_Details.carvingMethod))
	// statements.add(returnStmt);
	//
	// // }
	// /*
	// * else statements.add(callStmt);
	// */
	// } else
	// statements.add(callStmt);
	// }
	// // statements.add(callStmt);
	// }
	// if (typeOfInvocation.equals("StaticInvokeExpr")) {
	// logger.debug("Inside Static Invoke " + objLocal);
	// objectAndValue.put(m, objLocal);
	// SootMethod callingMethod = Scene.v().getMethod(m);
	// final Stmt callStmt;
	// final Stmt assignStmt;
	// List<Value> argsToinsert = parameterCheck(m);
	//
	// if (!argsToinsert.isEmpty()) {
	// callStmt =
	// jimple.newInvokeStmt(jimple.newStaticInvokeExpr(callingMethod.makeRef(),
	// argsToinsert));
	// if (Graph_Details.returnValues.containsKey(m)) {
	// // List<String> returnValues=new
	// // ArrayList<String>(Graph_Details.returnValues.get(m));
	// // if(!returnValues.equals("null")){
	// assignStmt = jimple.newAssignStmt(objLocal,
	// jimple.newStaticInvokeExpr(callingMethod.makeRef(), argsToinsert));
	// statements.add(assignStmt);
	// // }
	// /*
	// * else statements.add(callStmt);
	// */
	// } else
	// statements.add(callStmt);
	// } else {
	// callStmt =
	// jimple.newInvokeStmt(jimple.newStaticInvokeExpr(callingMethod.makeRef()));
	// if (Graph_Details.returnValues.containsKey(m)) {
	// // List<String> returnValues=new
	// // ArrayList<String>(Graph_Details.returnValues.get(m));
	// // if(!returnValues.equals("null")){
	//
	// assignStmt = jimple.newAssignStmt(objLocal,
	// jimple.newStaticInvokeExpr(callingMethod.makeRef(), argsToinsert));
	// statements.add(assignStmt);
	//
	// // }
	// /*
	// * else statements.add(callStmt);
	// */
	// } else
	// statements.add(callStmt);
	// }
	//
	// // statements.add(callStmt);
	// }
	// }
	//
	// // createTestCaseForMultipleCalls(testCases);
	// toFile(localVariables, statements, "Bazzinga", testClassName,
	// testMethodName, times, index);
	// statements.clear();
	// localVariables.clear();
	//
	// }
	//
	// public List<Value> parameterCheck(String methodName) {
	// logger.debug("Checking for " + methodName);
	// List<Value> argsToInsert = new ArrayList<Value>();
	//
	// if (Graph_Details.hashParam.containsKey(methodName)) {
	// // Method has some parameters
	// // logger.debug("Checking for "+methodName);
	//
	// String method = paramOfMethod(methodName);
	// logger.debug("Parameters of the method are " + method);
	// // List of all parameters
	// ArrayList<String> list = new ArrayList<String>(parameterList(method));//
	// all
	// // parameters
	// ArrayList<String> typeOfParameter = new
	// ArrayList<String>(parameterTypeReturn(methodName));// all
	// // types
	// for (String each : list) {
	// logger.debug("Paramter " + each);
	// if (Graph_Details.instancesHashId.containsKey(each)) {
	// // Object id is the parameter
	// String init = methodObject(each);// logger.debug("The
	// // object is "+init);
	// if (objectAndValue.containsKey(init)) {
	// Local local = objectAndValue.get(init);
	// // logger.debug("Adding here");
	// argsToInsert.add(local);
	// }
	// if (init.equals("Not init")) {
	// // The key is present i.e the object id is present but
	// // there is no init
	// // It must be static init then
	// logger.debug("In Static first arguments");
	// String staticMethod = "";
	// // Graph_Details.showReturnValues();
	// if (Graph_Details.isReturnValuePresent(each)) {
	// logger.debug("return value bi map has the value");
	// staticMethod = Graph_Details.returnValueIfPresent(each);
	// if (objectAndValue.containsKey(staticMethod)) {
	// logger.debug("Object and value have the value");
	// Local local = objectAndValue.get(staticMethod);
	// argsToInsert.add(local);
	// } else {
	// logger.debug("object and value not present");
	// }
	// }
	// }
	// } else {
	// // Primitive variable and should be converted and added
	// String type = typeOfParameter.get(list.indexOf(each));
	// logger.debug("Its not a object parameter " + each);
	// logger.debug("The type is " + type);
	// switch (type) {
	// case "java.lang.String": {
	// // logger.debug("String parameter");
	// argsToInsert.add(StringConstant.v(each));
	// break;
	// }
	// case "int": {
	// argsToInsert.add(IntConstant.v(Integer.parseInt(each)));
	// break;
	// }
	// case "double": {
	// argsToInsert.add(DoubleConstant.v(Double.parseDouble(each)));
	// break;
	// }
	// case "float": {
	// argsToInsert.add(FloatConstant.v(Float.parseFloat(each)));
	// break;
	// }
	// case "long": {
	// argsToInsert.add(LongConstant.v(Long.parseLong(each)));
	// break;
	// }
	// case "boolean": {
	// if (each.equals(each))
	// argsToInsert.add(IntConstant.v(1));
	// else
	// argsToInsert.add(IntConstant.v(0));
	// break;
	// }
	// case "java.util.Date": {
	// Local dateLocal = typesAndLocals.get("java.util.Date");
	// logger.debug("Adding " + dateLocal + " to arguments");
	// argsToInsert.add(dateLocal);
	// break;
	// }
	// default: {
	// // Static Parameter
	// // Check where is the return function for it
	// logger.debug("In Static arguments");
	// String staticMethod = "";
	// // Graph_Details.showReturnValues();
	// if (Graph_Details.isReturnValuePresent(each)) {
	// logger.debug("return value bi map has the value");
	// staticMethod = Graph_Details.returnValueIfPresent(each);
	// if (objectAndValue.containsKey(staticMethod)) {
	// logger.debug("Object and value have the value");
	// Local local = objectAndValue.get(staticMethod);
	// argsToInsert.add(local);
	// } else {
	// if (each.endsWith("\"") && each.startsWith("\"")) {
	// each = each.substring(1, each.lastIndexOf("\""));
	// logger.debug("The substring after removing qoutes is " + each);
	// }
	// argsToInsert.add(StringConstant.v(each));// +"#"));
	// }
	// } else {
	// // Scanner arguments
	// if (typeOfParameter.get(list.indexOf(each)).equals("java.util.Scanner"))
	// {
	// Local l = objectAndValue.get("<java.util.Scanner: void
	// <init>(java.io.InputStream)>");
	// argsToInsert.add(l);
	// } else {
	// // convert to Object
	// if (each.endsWith("\"") && each.startsWith("\"")) {
	// each = each.substring(1, each.lastIndexOf("\""));
	// logger.debug("The substring after removing qoutes is " + each);
	// }
	// argsToInsert.add(StringConstant.v(each + "#"));
	// // logger.debug("Inside object
	// // conversion");
	// // Local
	// // l=Jimple.v().newLocal("objectConversionLocal",
	// // RefType.v("java.lang.Object"));
	// // Stmt assign=Jimple.v().newAssignStmt(l,
	// // StringConstant.v(each));
	// // statements.add(assign);
	// // localVariables.add(l);
	// // argsToInsert.add(l);
	// }
	// }
	// }
	// }
	// }
	// }
	//
	// }
	// if (!argsToInsert.isEmpty()) {
	// for (Value v : argsToInsert) {
	// logger.debug("The value of function parameter is " + v);
	// }
	// }
	// return argsToInsert;
	//
	// }

	public ArrayList<String> parameterTypeReturn(String method) {
		ArrayList<String> types;
		String parameterTypes = method.substring(method.indexOf("(") + 1, method.indexOf(")"));
		logger.debug("Parameter types split " + parameterTypes);
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

	// public String methodObject(String objectId) {
	// // logger.debug(objectId);
	// String carvingSub = "";
	// if (objectId.contains("@"))
	// carvingSub = objectId.substring(1, objectId.indexOf("@"));
	// else
	// carvingSub = "<java.sql.Date: void <init>(long)>";
	// if (Graph_Details.instancesHashId.containsKey(objectId)) {
	// ArrayList<String> list = Graph_Details.instancesHashId.get(objectId);
	// for (String each : list) {
	// // If the item in list contains init(constructor) return the
	// // constructor back
	// if (each.contains("<init>") && each.contains(carvingSub))
	// return each;
	//
	// }
	// }
	// return "Not init";
	// }

	// public String paramOfMethod(String methodName) {
	// if (Graph_Details.hashParam.containsKey(methodName))
	// return Graph_Details.hashParam.get(methodName);
	// return null;
	// }

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

	// public void toFile(List<Local> localVariables, List<Unit> statements,
	// String typeOfInvocation, String className,
	// String testMethodName, int times, int index) throws IOException,
	// InterruptedException {
	//
	// logger.debug("TestGenerator.toFile() className " + className + " : " +
	// testMethodName);
	// /*
	// * The output directory cannot be given directly as
	// * "/home/pallavi/KeplereeWorkspace/Soot_Instrumentation/src/test/java"
	// * So initially I put it in SootOutput and then copy the same using
	// * terminal and execute
	// */
	// // This probably should be in the factory instead ?
	// File outputDir = new File("./sootOutput/TestCases");
	// if (!outputDir.exists()) {
	// if (!outputDir.mkdirs()) {
	// throw new IOException("Cannot create test output directory " +
	// outputDir);
	// }
	// }
	// // Files.createTempDir();
	// // Why do we delete this on exit ?
	// // outputDir.deleteOnExit();
	// //
	//
	// TestCaseFactory factory = TestCaseFactory.getInstance();
	//
	// SootClass tc = factory.generateNewJUnit4TestCase(className, times,
	// index);
	// //
	// factory.addNewTestMethodToJunit4TestCase(tc, localVariables, statements,
	// typeOfInvocation, testMethodName,
	// times, index);
	//
	// //
	// factory.writeToDir(outputDir, tc);
	//
	// // Assertion
	// // Assert.assertEquals(1, outputDir.listFiles().length);
	// // Just for visual check on file content !
	// for (File file : outputDir.listFiles()) {
	//
	// try (BufferedReader br = new BufferedReader(new FileReader(file))) {
	// String line = null;
	// while ((line = br.readLine()) != null) {
	// logger.debug(line);
	// }
	// } catch (Exception e) {
	// Assert.fail(e.getMessage());
	// }
	// }
	//
	// }
	//
	// public ArrayList<String> returnAllObjectIDFromMethod(String method) {
	// ArrayList<String> allobjectId = new ArrayList<String>();
	// Iterator<Map.Entry<String, ArrayList<String>>> it =
	// Graph_Details.instancesHashId.entrySet().iterator();
	// while (it.hasNext()) {
	// Map.Entry<String, ArrayList<String>> entry = it.next();
	// ArrayList<String> list = entry.getValue();
	// String key = entry.getKey();
	// for (String s : list) {
	// if (s.equals(method)) {
	// allobjectId.add(key);
	// ;
	// }
	// }
	// }
	// return allobjectId;
	//
	// }

	// public void createTestCaseForMultipleCalls(List<String> testCases, int
	// times, int index)
	// throws IOException, InterruptedException {
	// // FIXME what's this ? a test case where there's multiple calls of the
	// // MUT, hence we need to perform some, but stop at the right point ?!
	// logger.debug("Test cases for multiple call" + testCases);
	// String typeOfInvocation = "";
	// String methodObject = "";
	// int count = 1;
	// String lastData = testCases.get(testCases.size() - 1);
	// // String
	// //
	// testClassName=lastData.substring(lastData.indexOf("<")+1,lastData.indexOf(":"));
	// String testClassName = "Test_" + Graph_Details.carvingMethod
	// .substring(Graph_Details.carvingMethod.indexOf("<") + 1,
	// Graph_Details.carvingMethod.indexOf(":"));
	// String allDataSplitWithSpace[] = lastData.split(" ");
	// String MUTSplit[] = Graph_Details.carvingMethod.split(" ");
	// String testMethodName = "test_" + MUTSplit[2].substring(0,
	// MUTSplit[2].indexOf('(')) + "_"
	// + allDataSplitWithSpace[2].substring(0,
	// allDataSplitWithSpace[2].indexOf('('));
	// // Clear all existing lists
	// typesOfMethod.clear();
	// localVariables.clear();
	// objectAndValue.clear();
	//
	// Jimple jimple = Jimple.v();
	//
	// Set<ArrayList<String>> calls = new HashSet<ArrayList<String>>();// List
	// // of
	// // all
	// // the
	// // functions
	// // that
	// // need
	// // to be
	// // called
	//
	// Set<ArrayList<String>> finalCall = new HashSet<ArrayList<String>>();//
	// Final
	// // call
	// // list..
	// // to
	// // be
	// // used
	// // always
	// for (String m : testCases) {
	//
	// if (m.equals("Not init")) {
	// continue;
	// }
	//
	// // Either the function will be duplicate or it wont be
	// // The idea is to first get a list of the testcases to run
	// // completely
	// //
	// // The function signature should be the original but the parameter
	// // check should be from the duplicated version
	//
	// // Check if this function has been called multiple times
	// // Dont need this for init function
	//
	// if (Graph_Details.hashIdForSameNameVertices.containsKey(m)) {
	// // This particular method is being called multiple times
	// logger.debug("Value of m is " + m);
	// ArrayList<String> allObjects = returnAllObjectIDFromMethod(m);
	// if (!m.contains("<init>")) {// &&m.contains("upgradeRoom")){
	// // There is no need to do double execution for an init
	// // Atleast for now
	// ArrayList<String> methodCollectionWithoutConstructor = new
	// ArrayList<String>(
	// Graph_Details.hashIdForSameNameVertices.get(m));
	// logger.debug("Duplicates: " + methodCollectionWithoutConstructor);
	// logger.debug("All objects size " + allObjects.size());
	// for (String method : methodCollectionWithoutConstructor) {
	// String objectId =
	// allObjects.get(methodCollectionWithoutConstructor.indexOf(method));
	//
	// logger.debug("Object ID " + objectId);
	// String init = methodObject(objectId);
	// ArrayList<String> addToSet = new ArrayList<String>();
	//
	// addToSet.add(init);
	// addToSet.add(method);
	// logger.debug("The array list is ::::::::::::::::::::::::::::: " +
	// addToSet);
	// calls.add(addToSet);
	//
	// }
	// }
	// // this calls list would have only the method which repeats and
	// // its constructor, it is not necessarily a valid TC
	// // All functions from the original test cases which were not
	// // repeated should also be included
	//
	// Iterator<ArrayList<String>> itCalls = calls.iterator();
	// ArrayList<String> methodCollection;
	// ArrayList<String> finalMethodCollection;
	// while (itCalls.hasNext()) {
	//
	// methodCollection = itCalls.next();// Holds the ArrayLists of
	// // repetition
	// finalMethodCollection = new ArrayList<String>(methodCollection);
	// ArrayList<String> methodCollectionWithOutCounts = new
	// ArrayList<String>();
	// for (String methodWithCount : methodCollection) {
	// String methodWithoutCount = methodWithCount.substring(0,
	// methodWithCount.lastIndexOf(">") + 1);
	// methodCollectionWithOutCounts.add(methodWithoutCount);
	// // Same arraylist but it does not have the count values
	// }
	// logger.debug("The original test cases are " + testCases);
	// logger.debug("The method without count is " +
	// methodCollectionWithOutCounts);
	// if (finalMethodCollection.size() != testCases.size()) {
	// logger.error("There are functions missing..... NOT VALID");
	// for (String methodInTestCases : testCases) {
	// if (!methodCollectionWithOutCounts.contains(methodInTestCases))
	// finalMethodCollection.add(testCases.indexOf(methodInTestCases),
	// methodInTestCases);
	// }
	// }
	// logger.debug("Each test case of ::::::::::::" + finalMethodCollection);//
	// This
	// // is
	// // the
	// // actual
	// // test cases that must be executed
	// finalCall.add(finalMethodCollection);
	// }
	// logger.debug("Final Call" + finalCall);
	//
	// } else {
	// // Shall we skip this ?
	// logger.warn(">>>> There no id for " + m + " skip it ?!");
	// continue;
	// }
	//
	// }
	//
	// // What's this ?
	// if (!finalCall.isEmpty()) {
	// logger.debug("not empty");
	// Iterator<ArrayList<String>> itCalls = finalCall.iterator();
	// while (itCalls.hasNext()) {
	// typesAndLocals.clear();
	//
	// ArrayList<String> methodCollection = itCalls.next();
	// /*
	// * Contains the methods which should be executed
	// */
	// for (String each : methodCollection) {
	//
	// // Skip the not init... but what's this is about ?
	// if (each.equals("Not init")) {
	// logger.debug("Skip " + each);
	// continue;
	// }
	//
	// logger.debug(
	// "TestGenerator.createTestCaseForMultipleCalls() Method for new list that
	// was created is : "
	// + each);
	//
	// methodObject = each.substring(each.indexOf("<") + 1, each.indexOf(":"));
	// typesOfMethod.add(methodObject);
	// String originalMethodToBeUsedAsSignature = "";
	// if (Graph_Details.hashIdForSameNameVertices.containsValue(each)) {
	// // logger.debug("Has vertices");
	// // The key for this value is the signature of the
	// // function which should be executed but the
	// // parameters should be of the value
	// Set<String> keySet = Graph_Details.hashIdForSameNameVertices.keySet();
	// for (String key : keySet) {
	// Iterator<String> it =
	// Graph_Details.hashIdForSameNameVertices.iterator(key);
	// while (it.hasNext()) {
	// String value = it.next();
	// if (value.equals(each))
	// originalMethodToBeUsedAsSignature = key;
	// }
	// }
	// } else
	// originalMethodToBeUsedAsSignature = each;// Else the
	// // signature
	// // is the
	// // same
	// // there are
	// // no copies
	//
	// // logger.debug("The value of the function being
	// // executed is "+originalMethodToBeUsedAsSignature);
	//
	// if
	// (Graph_Details.methodAndTypeOfInvocation.containsKey(originalMethodToBeUsedAsSignature))
	// typeOfInvocation = Graph_Details.methodAndTypeOfInvocation
	// .get(originalMethodToBeUsedAsSignature);
	// else if (Graph_Details.methodAndTypeOfInvocation.containsKey(m))
	// typeOfInvocation = Graph_Details.methodAndTypeOfInvocation.get(m);
	// // logger.debug("Type of invocation is
	// // "+typeOfInvocation);
	// Iterator<String> it = typesOfMethod.iterator();
	// while (it.hasNext()) {
	// String type = it.next();
	// logger.debug("The type of object2 " + type);
	// Local objLocal = Jimple.v().newLocal("objLocal" +
	// countOfLocal.getAndIncrement(),
	// RefType.v(type));
	// if (!typesAndLocals.containsKey(type))
	// typesAndLocals.put(type, objLocal);
	// localVariables.add(objLocal);
	//
	// }
	// if (originalMethodToBeUsedAsSignature
	// .contains("<java.util.Scanner: void <init>(java.io.InputStream)>")) {
	// Local l = Jimple.v().newLocal("input", RefType.v("java.io.InputStream"));
	// final RefType foStreamType = RefType.v("java.util.Scanner");
	// localVariables.add(l);
	// statements.add(Jimple.v().newAssignStmt(l, Jimple.v().newStaticFieldRef(
	// Scene.v().getField("<java.lang.System: java.io.InputStream
	// in>").makeRef())));
	// Local l1 = Jimple.v().newLocal("scan", RefType.v("java.util.Scanner"));
	// localVariables.add(l1);
	// final SootMethod constructor = Scene.v()
	// .getMethod("<java.util.Scanner: void <init>(java.io.InputStream)>");
	// final Stmt assignStmt = jimple.newAssignStmt(l1,
	// jimple.newNewExpr(foStreamType));
	// Stmt invokeStmt = jimple
	// .newInvokeStmt(jimple.newSpecialInvokeExpr(l1, constructor.makeRef(),
	// l));
	//
	// objectAndValue.put(originalMethodToBeUsedAsSignature, l1);
	// statements.add(assignStmt);
	// statements.add(invokeStmt);
	// }
	//
	// if (typeOfInvocation.equals("SpecialInvokeExpr") &&
	// !originalMethodToBeUsedAsSignature
	// .equals("<java.util.Scanner: void <init>(java.io.InputStream)>")) {
	// logger.debug("Entering special invoke");
	// // ObjectLocal is created only when there is a init else
	// // the local is just called
	// methodObject =
	// originalMethodToBeUsedAsSignature.substring(each.indexOf("<") + 1,
	// each.indexOf(":"));
	// Local objLocal = typesAndLocals.get(methodObject);
	// logger.debug("Inside special invoke if");
	// Stmt invokeStmt;
	// objectAndValue.put(originalMethodToBeUsedAsSignature, objLocal);
	// RefType cType = RefType.v(methodObject);
	// Stmt s = jimple.newAssignStmt(objLocal, jimple.newNewExpr(cType));
	// SootMethod constructor =
	// Scene.v().getMethod(originalMethodToBeUsedAsSignature);
	// List<Value> argsToinsert = parameterCheck(each);
	// // if(!argsToinsert.isEmpty()){
	// // for(Value v:argsToinsert){
	// // logger.debug("The value of function parameter
	// // for "+m+" is "+v);
	// // }
	// // }
	// if (!argsToinsert.isEmpty()) {
	//
	// invokeStmt = jimple.newInvokeStmt(
	// jimple.newSpecialInvokeExpr(objLocal, constructor.makeRef(),
	// argsToinsert));
	// } else
	// invokeStmt = jimple
	// .newInvokeStmt(jimple.newSpecialInvokeExpr(objLocal,
	// constructor.makeRef()));
	//
	// statements.add(s);
	// statements.add(invokeStmt);
	// logger.debug("Statements " + statements);
	// }
	// if (typeOfInvocation.equals("VirtualInvokeExpr")) {
	// logger.debug("Entering Virtual Invoke");
	// if (originalMethodToBeUsedAsSignature
	// .equals("<java.text.DateFormat: java.util.Date
	// parse(java.lang.String)>")) {
	//
	// Local objLocal = typesAndLocals.get("java.text.SimpleDateFormat");
	// logger.debug("Date referral" + objLocal);
	// SootMethod callingMethod = Scene.v()
	// .getMethod("<java.text.DateFormat: java.util.Date
	// parse(java.lang.String)>");
	// // Stmt
	// // callStmt=jimple.newInvokeStmt(jimple.newVirtualInvokeExpr(objLocal,
	// // callingMethod.makeRef(),StringConstant.v("Wed Aug
	// // 09 00:00:00 CEST 2017")));
	// // statements.add(callStmt);
	//
	// Local dateLocal = jimple.newLocal("dateLocal" + count,
	// RefType.v("java.util.Date"));
	// typesAndLocals.put("java.util.Date", dateLocal);
	// localVariables.add(dateLocal);
	// Stmt assignStmt = jimple.newAssignStmt(dateLocal,
	// jimple.newVirtualInvokeExpr(objLocal,
	// callingMethod.makeRef(), StringConstant.v("Wed Aug 09 00:00:00 CEST
	// 2017")));
	// statements.add(assignStmt);
	// count++;
	// continue;
	// }
	// methodObject =
	// originalMethodToBeUsedAsSignature.substring(each.indexOf("<") + 1,
	// each.indexOf(":"));
	// logger.debug(methodObject);
	// Local objLocal = typesAndLocals.get(methodObject);
	// logger.debug("objLocal " + objLocal);
	// logger.debug("Inside virtual invoke if");
	// objectAndValue.put(originalMethodToBeUsedAsSignature, objLocal);
	//
	// SootMethod callingMethod =
	// Scene.v().getMethod(originalMethodToBeUsedAsSignature);
	// logger.debug("callingMethod" + callingMethod);
	// final Stmt callStmt;
	// List<Value> argsToinsert = parameterCheck(each);
	// // if(!argsToinsert.isEmpty()){
	// // for(Value v:argsToinsert){
	// // logger.debug("The value of function parameter
	// // for "+m+" is "+v);
	// // }
	// // }
	// Local returnValueLocal = Jimple.v().newLocal(
	// "returnValueLocal" + countOfLocal.getAndIncrement(),
	// RefType.v("java.lang.Object"));
	// // typesAndLocals.put(type, objLocal);
	// localVariables.add(returnValueLocal);
	// if (!argsToinsert.isEmpty()) {
	// logger.debug("Not null");
	// callStmt = jimple.newInvokeStmt(
	// jimple.newVirtualInvokeExpr(objLocal, callingMethod.makeRef(),
	// argsToinsert));
	// if
	// (Graph_Details.returnValues.containsKey(originalMethodToBeUsedAsSignature))
	// {
	// logger.debug("Returns something");
	// Stmt assignStmt = jimple.newAssignStmt(returnValueLocal,
	// jimple.newVirtualInvokeExpr(objLocal, callingMethod.makeRef(),
	// argsToinsert));
	// Stmt returnStmt = jimple.newRetStmt(returnValueLocal);
	// statements.add(assignStmt);
	// if
	// (originalMethodToBeUsedAsSignature.equals(Graph_Details.carvingMethod)) {
	// logger.debug("Carving method reached");
	// statements.add(returnStmt);
	// }
	// } else {
	// statements.add(callStmt);
	// }
	//
	// } else {
	// logger.debug("null");
	// callStmt = jimple
	// .newInvokeStmt(jimple.newVirtualInvokeExpr(objLocal,
	// callingMethod.makeRef()));
	// if
	// (Graph_Details.returnValues.containsKey(originalMethodToBeUsedAsSignature))
	// {
	//
	// logger.debug("Adding assignment statement");
	// Stmt assignStmt = jimple.newAssignStmt(returnValueLocal,
	// jimple.newVirtualInvokeExpr(objLocal, callingMethod.makeRef()));
	//
	// Stmt returnStmt = jimple.newRetStmt(returnValueLocal);
	// statements.add(assignStmt);
	// if
	// (originalMethodToBeUsedAsSignature.equals(Graph_Details.carvingMethod))
	// statements.add(returnStmt);
	//
	// } else
	// statements.add(callStmt);
	// }
	//
	// }
	// if (typeOfInvocation.equals("StaticInvokeExpr")) {
	// logger.debug("Entering static invoke");
	// methodObject = originalMethodToBeUsedAsSignature
	// .substring(originalMethodToBeUsedAsSignature.indexOf("<") + 1,
	// each.indexOf(":"));
	// logger.debug(methodObject);
	// Local objLocal = typesAndLocals.get(methodObject);
	// logger.debug("objLocal " + objLocal);
	// logger.debug("Inside Static Invoke");
	// objectAndValue.put(originalMethodToBeUsedAsSignature, objLocal);
	// SootMethod callingMethod = Scene.v().getMethod(each);
	// final Stmt callStmt;
	// final Stmt assignStmt;
	// List<Value> argsToinsert = parameterCheck(each);
	// // if(!argsToinsert.isEmpty()){
	// // for(Value v:argsToinsert){
	// // logger.debug("The value of function parameter
	// // for "+m+" is "+v);
	// // }
	// // }
	// if (!argsToinsert.isEmpty()) {
	// callStmt = jimple
	// .newInvokeStmt(jimple.newStaticInvokeExpr(callingMethod.makeRef(),
	// argsToinsert));
	// if
	// (Graph_Details.returnValues.containsKey(originalMethodToBeUsedAsSignature))
	// {
	// // List<String> returnValues=new
	// //
	// ArrayList<String>(Graph_Details.returnValues.get(originalMethodToBeUsedAsSignature));
	// // if(!returnValues.equals("null")){
	// assignStmt = jimple.newAssignStmt(objLocal,
	// jimple.newStaticInvokeExpr(callingMethod.makeRef(), argsToinsert));
	// statements.add(assignStmt);
	// /*
	// * } else statements.add(callStmt);
	// */
	// } else
	// statements.add(callStmt);
	// } else {
	// callStmt =
	// jimple.newInvokeStmt(jimple.newStaticInvokeExpr(callingMethod.makeRef()));
	// if
	// (Graph_Details.returnValues.containsKey(originalMethodToBeUsedAsSignature))
	// {
	// // List<String> returnValues=new
	// //
	// ArrayList<String>(Graph_Details.returnValues.get(originalMethodToBeUsedAsSignature));
	// // if(!returnValues.equals("null")){
	// assignStmt = jimple.newAssignStmt(objLocal,
	// jimple.newStaticInvokeExpr(callingMethod.makeRef(), argsToinsert));
	// statements.add(assignStmt);
	// /*
	// * } else statements.add(callStmt);
	// */
	// } else
	// statements.add(callStmt);
	// }
	//
	// // statements.add(callStmt);
	// }
	// }
	// logger.debug("Statements: " + statements);
	// if (!(localVariables.isEmpty() && statements.isEmpty())) {
	// toFile(localVariables, statements, "Bazzinga", testClassName,
	// testMethodName, times, index);
	// statements.clear();
	// statements.clear();
	// }
	// }
	// }
	// }
}
