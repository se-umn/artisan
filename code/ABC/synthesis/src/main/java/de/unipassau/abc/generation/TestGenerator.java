package de.unipassau.abc.generation;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.util.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.ABCUtils;
import de.unipassau.abc.carving.CarvedExecution;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.MethodInvocationMatcher;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.instrumentation.CarvingTag;
import soot.ArrayType;
import soot.Body;
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
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.IntConstant;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NewArrayExpr;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.toolkits.annotation.j5anno.AnnotationGenerator;
import soot.util.Chain;

public class TestGenerator {
	// Per Method
	// private static final AtomicInteger localId = new AtomicInteger(0);

	private static final Logger logger = LoggerFactory.getLogger(TestGenerator.class);
	private Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTrace;

	public TestGenerator(Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTrace) {
		this.parsedTrace = parsedTrace;
	}

	/**
	 * Test case generation requires only the list of instructions and the data
	 * dependencies between them. We obtain those by carving.
	 * 
	 * We generate one test class for each CUT and one test for each MUT
	 * 
	 * TODO If we reset the count for the local, HERE we can easily find duplicate
	 * test cases...
	 * 
	 * The return value might be smaller than the input because we filter out
	 * duplicate test methods
	 * 
	 * @param carvedTests
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ABCException
	 */
	public List<Map.Entry<CarvedExecution, SootMethod>> generateTestCases(List<CarvedExecution> carvedTests,
			TestClassGenerator testClassGenerator) throws IOException, InterruptedException {

		throw new NotImplementedException();

//		List<Map.Entry<CarvedExecution, SootMethod>> testCases = new ArrayList<>();
//
//		// Why we need to initialize soot here otherwise we cannot create
//		// methods and classes, and such
//		// To probably there's also something else to include here
//		for (CarvedExecution carvedTest : carvedTests) {
//
//			long start = System.currentTimeMillis();
//
//			// Check basic properties, variables initialized before used
//			try {
//				validate(carvedTest);
//			} catch (ABCException e) {
//				logger.error(" Error " + e.getMessage() + " while generating test: "
//						+ e.getCarvedTest().getFirst().getOrderedMethodInvocations());
//
//				/// Find if we have the call in the parsed trace !
//
//				continue;
//			}
//
//			// Get the mut, which by definition is the last invocation executed
//			MethodInvocation mut = carvedTest.getFirst().getLastMethodInvocation();
//
//			// Somehow this does include the executions we removed ?!
//			// logger.info("Generate Test: " + mut);
//
//			SootClass testClass = testClassGenerator.getTestClassFor(mut);
//			try {
//				// TODO For the moment we name tests after their position and
//				// MUT
//				// ,i.e., last element
//				SootMethod testMethod = generateAndAddTestMethodToTestClass(testClass, carvedTest);
//				if (testMethod != null) {
//					testCases
//							.add(new AbstractMap.SimpleEntry(
//									new Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>(
//											carvedTest.getFirst(), carvedTest.getSecond(), carvedTest.getThird()),
//									testMethod));
//				}
//			} catch (ABCException e) {
//				logger.warn("Cannot generate test ", e);
//			} finally {
//				logger.info("Generate Test : " + mut + " took " + (System.currentTimeMillis() - start) + " msec");
//			}
//		}
//		return testCases;
	}

	private void validate(CarvedExecution carvedExecution) throws ABCException {
		throw new NotImplementedException();
//		
//		DataDependencyGraph dataDependencyGraph = carvedTest.getSecond();
//		Collection<ObjectInstance> instances = dataDependencyGraph.getObjectInstances();
//		for (ObjectInstance instance : instances) {
//
//			if (instance.equals(ObjectInstance.systemIn) || //
//					instance.equals(ObjectInstance.systemOut) || //
//					instance.equals(ObjectInstance.systemErr) //
//			) {
//				continue;
//			}
//
//			// TODO This does not cover the cases where the variable is
//			// initialzed AFTER its used !
//			// And this is exactly the case !!
//			if (dataDependencyGraph.getInitMethodInvocationFor(instance) == null
//					&& dataDependencyGraph.getMethodInvocationsWhichReturn(instance).isEmpty()) {
//
//				ABCException e = new ABCException(
//						"Object instance " + instance + " is invalid. No method initizalizes or returns it !");
//				e.setBrokenInstance(instance);
//				e.setCarvedTest(carvedTest);
//
//				MethodInvocation mut = carvedTest.getFirst().getLastMethodInvocation();
//				for (Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsed : parsedTrace.values()) {
//					if (parsed.getFirst().contains(mut)) {
//						DataDependencyGraph ddg = parsed.getSecond();
//						StringBuffer msg = new StringBuffer();
//						msg.append("Init for " + instance + " " + ddg.getInitMethodInvocationFor(instance));
//						msg.append("Returns for " + instance + " " + ddg.getMethodInvocationsWhichReturn(instance));
//						logger.warn(msg.toString());
//					}
//				}
//
//				throw e;
//			}
//
//		}

	}

	// THIS IS MOSTLY TAKEN FROM TEST CASE FACTORY
	// REFACTOR !!
	private AtomicInteger generatedTestCases = new AtomicInteger(0);

	/*
	 * generate a new test method add add that to the class unless there's already
	 * an equivalent one
	 */
	private SootMethod generateAndAddTestMethodToTestClass(SootClass testClass,
			Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> carvedTest) throws ABCException {

		// For each element in the DataDependencyGraph create
		ExecutionFlowGraph executionFlowGraph = carvedTest.getFirst();
		DataDependencyGraph dataDependencyGraph = carvedTest.getSecond();

		/*
		 * Standard JUnit 4 Test methods are public void and annotated with @Test. They
		 * also do not take parameters
		 * 
		 */
		String testMethodName = "test_" + generatedTestCases.incrementAndGet();
		logger.info(">> Generating test method " + testMethodName + " for class " + testClass.getName());

		SootMethod testMethod = new SootMethod(testMethodName, Collections.<Type>emptyList(), VoidType.v(),
				Modifier.PUBLIC);

		// Add the tag to this code. note that the last element is always the
		// MUT !
		testMethod.addTag(new CarvingTag(executionFlowGraph.getLastMethodInvocation().getMethodSignature() + "_"
				+ executionFlowGraph.getLastMethodInvocation().getInvocationCount()));
		// if (tag.getName().startsWith("carving")) {

		SootClass e = Scene.v().loadClassAndSupport("java.lang.Exception");
		testMethod.addExceptionIfAbsent(e);

		// Annotate the method with org.junit.Test
		AnnotationGenerator.v().annotate(testMethod, org.junit.Test.class);

		// Build the body from the input list of statements
		JimpleBody body = Jimple.v().newBody(testMethod);
		testMethod.setActiveBody(body);

		// Keep track of local per type per method
		Map<String, AtomicInteger> localMap = new HashMap<>();

		// Generating method body. We need this here to introduce the assignment
		// to System.In
		Chain<Unit> units = body.getUnits();

		// Generate the Local Variables for the Method
		for (ObjectInstance node : dataDependencyGraph.getObjectInstances()) {

			String type = node.getType();

			if (!localMap.containsKey(type)) {
				localMap.put(type, new AtomicInteger(0));
			}
			String localId = String.format("%02d", localMap.get(type).getAndIncrement());

			// ARRAYS ARE STORES LIKE <className>[] and not as
			// [L<classname>;
			RefType variableType = null;

			if (type.endsWith("[]")) {
				variableType = RefType.v(type.replace("[]", ""));

				StringBuilder localName = new StringBuilder();
				// localName =
				// localName.substring(localName.lastIndexOf('.') + 1);
				for (String s : variableType.toString().split("\\.")) {
					// Apply code conventions
					localName.append(s.replaceFirst(s.substring(0, 1), s.substring(0, 1).toLowerCase()));
				}
				localName = localName.append("Array");
				localName.append(localId);

				// Local local1 =
				// localGenerator.generateLocal(ArrayType.v(RefType.v("java.lang.String"),
				// 1));
				// FIXME This might be another reason why it failed to generate
				// a correct bytecode !
				Local newArrayLocal = Jimple.v().newLocal(localName.toString(), ArrayType.v(variableType, 1));
				body.getLocals().add(newArrayLocal);

//				logger.trace("  >>>> Create a new local ARRAY variable " + newArrayLocal + " of type " + type
//						+ " and node " + node + " " + node.hashCode());
				//
				dataDependencyGraph.setSootValueFor(node, newArrayLocal);
			} else {

				variableType = RefType.v(type);
				StringBuilder localName = new StringBuilder();
				// localName =
				// localName.substring(localName.lastIndexOf('.') + 1);
				for (String s : variableType.toString().split("\\.")) {
					// Apply code conventions
					localName.append(s.replaceFirst(s.substring(0, 1), s.substring(0, 1).toLowerCase()));
				}
				localName.append(localId);
				//
				Local localVariable = Jimple.v().newLocal(localName.toString(), variableType);
				body.getLocals().add(localVariable);

				// Debug
//				logger.trace("  >>>> Create a new local variable " + localVariable + " of type " + type + " and node "
//						+ node + " " + node.hashCode());

				// String initialization ? This should be called later ?
				dataDependencyGraph.setSootValueFor(node, localVariable);

				// InputStream ina = System.in;
				// TODO I do not like this but it might help
				// StaticFieldRef shall be initialized
//				if (node.equals(ObjectInstance.systemIn)) {
//					units.add(Jimple.v().newAssignStmt(localVariable, Jimple.v().newStaticFieldRef(
//							Scene.v().getField("<java.lang.System: java.io.InputStream in>").makeRef())));
//				} else if (node.equals(ObjectInstance.systemOut)) {
//					// Patch in place initialization for this...
//					units.add(Jimple.v().newAssignStmt(localVariable, Jimple.v().newStaticFieldRef(
//							Scene.v().getField("<java.lang.System: java.io.PrintStream out>").makeRef())));
//				} else if (node.equals(ObjectInstance.systemErr)) {
//					// Patch in place initialization for this...
//					units.add(Jimple.v().newAssignStmt(localVariable, Jimple.v().newStaticFieldRef(
//							Scene.v().getField("<java.lang.System: java.io.PrintStream err>").makeRef())));
//				}

			}

			// }
			//
		}

		List<MethodInvocation> orderedMethodInvocations = executionFlowGraph.getOrderedMethodInvocations();
		// By definition the MUT is the last invoked
		MethodInvocation methodInvocationToCarve = executionFlowGraph.getLastMethodInvocation();

		// This one should contain the <init> functions as well...
		// for (MethodInvocation methodInvocation : orderedMethodInvocations) {
		// System.out.println( methodInvocation );
		// }

		// Build the test body
		for (MethodInvocation methodInvocation : orderedMethodInvocations) {

			/// Do we have <init> calls here ?!

			// Get the object upon which we invoke the method
			Local objLocal = dataDependencyGraph.getObjectLocalFor(methodInvocation);
			// Parameters can be objects and primitive
			// Note that those must be ORDERED, since parameters are positional
			// !

			// Look at the parameterCheck(m) method !
			List<Value> parametersValues = dataDependencyGraph.getParametersSootValueFor(methodInvocation);

			// Store the return value to build the data dependencies
			Value returnValue = dataDependencyGraph.getReturnObjectLocalFor(methodInvocation);

//			logger.debug("Generating  " + methodInvocation + " owner " + objLocal + " parameters " + parametersValues
//					+ " with return value " + returnValue);
			//
			Local actualReturnValue = null;
			if (returnValue instanceof Local) {
				actualReturnValue = (Local) returnValue;
			}
			// else {
			// logger.debug("We do not track return values of primitive types
			// for " + methodInvocation
			// + " with return value " + returnValue);
			// }

			// When we process the methodInvocationToCarve, we also generate a
			// final assignment that enable regression testing
			// Stmt returnStmt = null;
			// FIXME For some reason, Soot removes this while generating
			// bytecode. Most likely is some weird optimization which stripes
			// off unused variables... but that's annoying !
			if (methodInvocation.equals(methodInvocationToCarve)
					&& !JimpleUtils.isVoid(JimpleUtils.getReturnType(methodInvocation.getMethodSignature()))) {
				String type = JimpleUtils.getReturnType(methodInvocation.getMethodSignature());
				// This shall be null at this point, since we do not use the
				// return value ?
				actualReturnValue = Jimple.v().newLocal("returnValue", RefType.v(type));

				body.getLocals().add(actualReturnValue);

				// returnObjLocal = UtilInstrumenter.generateFreshLocal(body,
				// RefType.v(type));

				// Debug
//				logger.info("  >>>> Create a new local variable " + actualReturnValue + " of type " + type
				// + " to store the output of " + methodInvocationToCarve);
				// FIXME: I have no idea of what a RetStmt is, but it does the
				// trick, which is it results in an actual java assignment
				// units.add( Jimple.v().newRetStmt( actualReturnValue ) );
			}

			// We need to use add because some method invocations are actually
			// more than 1 unit !
			addUnitFor(body, methodInvocation, objLocal, parametersValues, actualReturnValue);

		}

		// Insert final void return to close the test method
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

			return testMethod;
		} else {
			logger.info("Found duplicate method " + testMethodName);
			return null;
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
	 * in this case we take A.get() while carving A.set because of A not because of
	 * the data dependency x
	 * 
	 * The carved test will be: int x = A.get(); A.set(X); where X is the actual
	 * value we observed at runtime.
	 * 
	 * TODO Explicitly CAST every operation !
	 * 
	 */
	private void addUnitFor( /* Chain<Unit> units */
			Body body, MethodInvocation methodInvocation, Local objLocal, List<Value> parametersValues,
			Local returnObjLocal) throws ABCException {

		Chain<Unit> units = body.getUnits();

//		logger.debug("Processing method invocation " + methodInvocation.getJimpleMethod() + " -- "
//				+ methodInvocation.getInvocationType() + " on local " + objLocal + " with params " + parametersValues
//				+ " to return " + returnObjLocal);

		Jimple jimple = Jimple.v();

		// This is supposed to be the constructor !
		SootMethod method = null;
		switch (methodInvocation.getInvocationType()) {
		case "SpecialInvokeExpr":
			method = ABCUtils.lookUpSootMethod(methodInvocation.getMethodSignature());
//			method = getSootMethod(methodInvocation.getJimpleMethod());
			// This is a constructor so I need to call new and then <init>
			// with
			// the right parameteres
			RefType cType = RefType.v(JimpleUtils.getClassNameForMethod(methodInvocation.getMethodSignature()));
			// Call "new"
			Stmt assignToNew = jimple.newAssignStmt(objLocal, jimple.newNewExpr(cType));
			// Call init + parameters
			InvokeStmt invokeStmt = jimple
					.newInvokeStmt(jimple.newSpecialInvokeExpr(objLocal, method.makeRef(), parametersValues));

			// This automatically captures the return value of <init> in
			// case we
			// need it for regression assertions
			units.add(assignToNew);
			units.add(invokeStmt);
			break;
		case "VirtualInvokeExpr":
			// Some methods are only defined in super classes, so getting the
			// method directly by name is not working !
			method = Scene.v().getMethod(methodInvocation.getMethodSignature());

			if (returnObjLocal != null) {

				// If the types are different then cast the assignment

				if (!method.getReturnType().toString().equals(returnObjLocal.getType().toString())) {
//					System.out.println("TestGenerator.addUnitFor() CAST " + method.getReturnType() + " into "
//							+ returnObjLocal.getType() + " for " + method);
					// Create a local of type defined by the method
					Local uncasted = jimple.newLocal(returnObjLocal.getName() + "Uncasted", method.getReturnType());
					body.getLocals().add(uncasted);

					// Make the call to the uncasted type
					Stmt assignStmtUncasted = jimple.newAssignStmt(uncasted,
							jimple.newVirtualInvokeExpr(objLocal, method.makeRef(), parametersValues));
					units.add(assignStmtUncasted);

					// Now make the case the cast and assign to the correct
					// object
					final Stmt assignStmt = jimple.newAssignStmt(returnObjLocal,
							jimple.newCastExpr(uncasted, returnObjLocal.getType()));

					units.add(assignStmt);
				} else {
					// Now make the case the cast and assign to the correct
					// object
					final Stmt assignStmt = jimple.newAssignStmt(returnObjLocal,
							jimple.newVirtualInvokeExpr(objLocal, method.makeRef(), parametersValues));

					units.add(assignStmt);
				}

			} else {
				Stmt callStmt = jimple
						.newInvokeStmt(jimple.newVirtualInvokeExpr(objLocal, method.makeRef(), parametersValues));
				units.add(callStmt);
			}
			break;

		case "StaticInvokeExpr":
			method = Scene.v().getMethod(methodInvocation.getMethodSignature());
			if (returnObjLocal != null) {

				if (!method.getReturnType().toString().equals(returnObjLocal.getType().toString())) {
//					System.out.println("TestGenerator.addUnitFor() CAST " + method.getReturnType() + " into "
//							+ returnObjLocal.getType() + " for " + method);

					// Create a local of type defined by the method
					Local uncasted = jimple.newLocal(returnObjLocal.getName() + "Uncasted", method.getReturnType());
					body.getLocals().add(uncasted);

					// Make the call to the uncasted type
					final Stmt uncastedAssignStmt = jimple.newAssignStmt(uncasted,
							jimple.newStaticInvokeExpr(method.makeRef(), parametersValues));
					units.add(uncastedAssignStmt);

					// Now make the case the cast and assign to the correct
					// object
					final Stmt assignStmt = jimple.newAssignStmt(returnObjLocal,
							jimple.newCastExpr(uncasted, returnObjLocal.getType()));

					units.add(assignStmt);
				} else {
					final Stmt assignStmt = jimple.newAssignStmt(returnObjLocal,
							jimple.newStaticInvokeExpr(method.makeRef(), parametersValues));

					units.add(assignStmt);
				}
			} else {
				final Stmt callStmt = jimple
						.newInvokeStmt(jimple.newStaticInvokeExpr(method.makeRef(), parametersValues));
				units.add(callStmt);
			}
			break;
		case "InterfaceInvokeExpr":
			method = Scene.v().getMethod(methodInvocation.getMethodSignature());
			if (returnObjLocal != null) {

				if (!method.getReturnType().toString().equals(returnObjLocal.getType().toString())) {
//					System.out.println("TestGenerator.addUnitFor() CAST " + method.getReturnType() + " into "
//							+ returnObjLocal.getType() + " for " + method);
					// Create a local of type defined by the method
					Local uncasted = jimple.newLocal(returnObjLocal.getName() + "Uncasted", method.getReturnType());
					body.getLocals().add(uncasted);

					// Make the call to the uncasted type
					Stmt assignStmtUncasted = jimple.newAssignStmt(uncasted,
							jimple.newInterfaceInvokeExpr(objLocal, method.makeRef(), parametersValues));
					units.add(assignStmtUncasted);

					// Now make the case the cast and assign to the correct
					// object
					final Stmt assignStmt = jimple.newAssignStmt(returnObjLocal,
							jimple.newCastExpr(uncasted, returnObjLocal.getType()));

					units.add(assignStmt);
				} else {
					final Stmt assignStmt = jimple.newAssignStmt(returnObjLocal,
							jimple.newInterfaceInvokeExpr(objLocal, method.makeRef(), parametersValues));

					units.add(assignStmt);
				}
			} else {
				Stmt callStmt = jimple
						.newInvokeStmt(jimple.newInterfaceInvokeExpr(objLocal, method.makeRef(), parametersValues));
				units.add(callStmt);
			}
			break;
		/// THE FOLLOWING ARE THE ARTIFICIAL CALLS THAT ABC GENERATED
		case "StringOperation": // This is a static call which generates out of
								// the blue the required string. The value is
								// store in the corresponding XML file
			// System.out.println("String initialization " + methodInvocation);
			try {
				// This is actually an assignment to the return value
//				String xmlContent = (String) XMLDumper.loadObject(methodInvocation.getXmlDumpForReturn());
				String xmlContent = "";
				Stmt callStmt = jimple.newAssignStmt(returnObjLocal, StringConstant.v(xmlContent));
				units.add(callStmt);
				// System.out.println("Added " + callStmt);
			} catch (Throwable e) {
				throw new ABCException("Cannot find a dumped value for string " + returnObjLocal + " in file "
						+ methodInvocation.getXmlDumpForReturn(), e);
			}
			break;
		case "ArrayOperation":

			// Here we need to handle the fact that ArrayOperations are an
			// artefact
			// of ABC
			if (MethodInvocationMatcher.byMethod("<.*\\[\\]: void <init>(int)>").matches(methodInvocation)) {
				// Create an array to host the values
				// System.out.println("TestGenerator.addUnitFor() INIT ARRAY " +
				// objLocal);
				String arrayType = JimpleUtils.getClassNameForMethod(methodInvocation.getMethodSignature())
						.replace("[]", "");
				NewArrayExpr arrayExpr = Jimple.v().newNewArrayExpr(RefType.v(arrayType), parametersValues.get(0));
				Stmt arrayAssignment = Jimple.v().newAssignStmt(objLocal, arrayExpr);
				units.add(arrayAssignment);
				// System.out.println("TestGenerator.addUnitFor() " +
				// arrayAssignment);
			} else if (MethodInvocationMatcher.byMethod("<.*\\[\\]: void store(int,.*)>").matches(methodInvocation)) {
				// Assign in position the value
				// System.out.println("TestGenerator.addUnitFor() STORE ARRAY "
				// + objLocal + " " + parametersValues.get(0)
				// + " " + parametersValues.get(1));
				ArrayRef arrayRef1 = Jimple.v().newArrayRef(objLocal, parametersValues.get(0));
				AssignStmt assignStmt1 = Jimple.v().newAssignStmt(arrayRef1, parametersValues.get(1));
				units.add(assignStmt1);
				// System.out.println("TestGenerator.addUnitFor() " +
				// assignStmt1);
			} else if (MethodInvocationMatcher.byMethod("<.*\\[\\]: .* get(int)>").matches(methodInvocation)) {
				// TODO Access the value at position ?
				// System.out.println("TestGenerator.addUnitFor() GET ARRAY
				// ELEMENT " + objLocal + " " + parametersValues.get(0) + "
				// Disabled");
				ArrayRef arrayRef1 = Jimple.v().newArrayRef(objLocal, parametersValues.get(0));
				AssignStmt assignStmt1 = Jimple.v().newAssignStmt(returnObjLocal, arrayRef1);
				units.add(assignStmt1);
				// System.out.println("TestGenerator.addUnitFor() " +
				// assignStmt1);
			} else {
				// [main] ERROR de.unipassau.abc.generation.TestGenerator - ERROR WRONG method
				// invocation: <java.lang.String[]: java.lang.String get(int)>_27

				logger.error("ERROR WRONG method invocation: " + methodInvocation);
			}
			break;
		case "ClassOperation":
			/*
			 * This correspond to <instance>.class which returns the .class
			 * 
			 */

//			logger.trace("Processing method invocation " + methodInvocation.getJimpleMethod() + " -- "
//					+ methodInvocation.getInvocationType() + " on local " + objLocal + " with params "
//					+ parametersValues + " to return " + returnObjLocal);
			// Generate the call to <class>.class which returns a class to
			// returnObjectLocal. Read the "value" of the class from the XML
			// stored during the execution.
//			try {
//				Class clazz = (Class) XMLDumper.loadObject(methodInvocation.getXmlDumpForReturn());
			//
//				units.add(Jimple.v().newAssignStmt(returnObjLocal,
//						ClassConstant.v(clazz.getName().replaceAll("\\.", "/"))));
//			} catch (Throwable e) {
//				throw new ABCException("Cannot find a dumped value for Class " + returnObjLocal + " in file "
//						+ methodInvocation.getXmlDumpForReturn(), e);
//			}

			break;
		case "MainArgsOperation":
			/*
			 * This is to retrieve from XML and rebuild the original String args[] (if any
			 * ?!)
			 */

			// TODO Not sure the objLocal is really there !

//			logger.trace("Processing method invocation " + methodInvocation.getJimpleMethod() + " -- "
//					+ methodInvocation.getInvocationType() + " on local " + objLocal + " with params "
//					+ parametersValues + " to return " + returnObjLocal);
			try {
				// We need to rebuild the array...
				String[] stringArrayFromXmlContent = new String[] {};
//				(String[]) XMLDumper
//						.loadObject(methodInvocation.getXmlDumpForReturn());

				// Create a local which hosts the array
				NewArrayExpr arrayExpr = Jimple.v().newNewArrayExpr(RefType.v("java.lang.String"),
						IntConstant.v(stringArrayFromXmlContent.length));
				Stmt arrayAssignment = Jimple.v().newAssignStmt(returnObjLocal, arrayExpr);
				//
				units.add(arrayAssignment);
				// Fill in the array
				for (int i = 0; i < stringArrayFromXmlContent.length; i++) {
					ArrayRef arrayRef = Jimple.v().newArrayRef(returnObjLocal, IntConstant.v(i));
					AssignStmt assignStmt = Jimple.v().newAssignStmt(arrayRef,
							StringConstant.v(stringArrayFromXmlContent[i]));
					units.add(assignStmt);
				}
			} catch (Throwable e) {
				throw new ABCException("Cannot find a dumped value for string " + returnObjLocal + " in file "
						+ methodInvocation.getXmlDumpForReturn(), e);
			}
			break;

		case "StaticFieldOperation":
//			logger.info("Processing method invocation " + methodInvocation.getJimpleMethod() + " -- "
//					+ methodInvocation.getInvocationType() + " on local " + objLocal + " with params "
//					+ parametersValues + " to return " + returnObjLocal);
			// ParametersValues contains javaioinputStream00 which is the local poiting to
			// the field ref
			AssignStmt assignStmt = Jimple.v().newAssignStmt(returnObjLocal, parametersValues.get(0));
			units.add(assignStmt);
//			System.out.println(">>> Added " + assignStmt);
			// Access to System.in for example: <abc.StaticField: java.io.InputStream
			// get(java.lang.String)> This shall become an assignment to a local
			break;
		default:
			logger.error(
					"Unexpected Invocation type " + methodInvocation.getInvocationType() + " for " + methodInvocation);
			throw new NotImplementedException(
					"Unexpected Invocation type " + methodInvocation.getInvocationType() + " for " + methodInvocation);
		}
	}

	// Trap the exception and log useful messages
//	private SootMethod getSootMethod(String jimpleMethod) {
//		try {
//			return Scene.v().getMethod(jimpleMethod);
//		} catch (Throwable e) {
//			logger.error("Cannot find method " + jimpleMethod, e);
//
//			for (SootClass sClass : Scene.v().getApplicationClasses()) {
//				logger.error("" + sClass);
//			}
//
//			throw e;
//		}
//	}

//	public ArrayList<String> parameterTypeReturn(String method) {
//		ArrayList<String> types;
//		String parameterTypes = method.substring(method.indexOf("(") + 1, method.indexOf(")"));
//		logger.debug("Parameter types split " + parameterTypes);
//		if (parameterTypes.contains(","))// Multiple parameters
//			// Split on , and store in array list
//			types = new ArrayList<String>(Arrays.asList(parameterTypes.split(",")));
//		else// Single parameter
//		{
//			types = new ArrayList<String>();
//			types.add(parameterTypes);
//		}
//
//		return types;
//
//	}

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

}
