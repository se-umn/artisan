package de.unipassau.abc.parsing;

import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.CallGraphImpl;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.DataDependencyGraphImpl;
import de.unipassau.abc.data.DataNode;
import de.unipassau.abc.data.DataNodeFactory;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.ExecutionFlowGraphImpl;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.tracing.Trace;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import soot.SootMethod;

/**
 * Parser "only" parse one trace file at time, and generates the data structures
 * required for carving, namely an ExecutionFlowGraph, a DataDependencyGraph,
 * and a CallGraph
 *
 * Parser considers the following heuristic: - Thread Name contains UI: this is
 * the "main" thread of the android app - Thread Name contains Asynch/Task: this
 * is a background thread that execute a single task (and dies) - Any other
 * thread name indicates some background service.
 *
 * Calls are interpreted using the tokens defined by {@link de.unipassau.abc.instrumentation.Monitor}
 *
 * To account for multiple thread, use the thread name as context and the actual
 * position in the file as id (they get interleaved but are still ordered!)
 *
 * One way to achieve this is to create different instances of the parses which
 * share the same ID generator and invoke them by looking up the instance using
 * the thread name.
 *
 * TODO: why the first tokens are encapsulated with [ ]? this is not neccessary
 * as they are tokens TODO: Maybe we can replace the DELIMITER to some char
 * which is NEVER used, like, * or % or | Strings are encoded so they are
 * basically digits
 *
 * @author gambi
 *
 */
public class TraceParserImpl extends TraceParser {

	private ParsedTraceImpl parsedTrace;

  public TraceParserImpl(boolean multiThreaded) {
    super(multiThreaded);
  }

  public TraceParserImpl() {
    this(false);
  }

  @Override
	public ParsedTrace setupParsedTrace(File traceFile) {
		this.parsedTrace = new ParsedTraceImpl(traceFile.getName());
		return parsedTrace;
	}

	private Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedElements
      = new ConcurrentHashMap<>();

	// TODO Maybe some of the following parsing and the like can be moved into Trace
	// ?

	// Thread Name, but to do so, we need to change this code

//	MethodInvocation methodInvocation = null;
//TODO Why do we need to have a reference HERE to soot classes? I think that's relevant only if we manage that otherwise, we'll use a decorator to pass over the graph and include soot-related properties 
//	SootClass sootClass = null;
//	// TODO This probably can be handled way better...
//	if (!isArrayMethod(methodSignature)) {
//		sootClass = Scene.v().getSootClass(JimpleUtils.getClassNameForMethod(methodSignature));
//	} else {
//		logger.debug("Found synthetic method array ", methodOwner, methodSignature);
//	}

	@Override
	public void validate() throws ABCException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endOfExecution() {
		this.parsedTrace.setContent(this.parsedElements);
	}

	@Override
	public void parseMethodReturnWithException(AtomicInteger globalInvocationCount, ParsedLine tokens)
			throws ABCException {

		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> local = getOrInitializeDataStructure(
				tokens.threadName);

		String methodSignature = tokens.methodSignature;
		String methodOwner = tokens.methodOwner;
		String parameterString = tokens.parametersOrReturnValue;

		MethodInvocation methodInvocation;
		String exceptionAsString = parameterString;

		//
		assert exceptionAsString != null;
		/*
		 * Retrieve the last method invocation registered Check that this corresponds to
		 * the one we just parsed Method Signatures must match and owner must match
		 * unless this is a call to constructor
		 */
		methodInvocation = local.getThird().pop();

		methodInvocation.setExceptional(true);

		/*
		 * Check this is actually the call we are looking for
		 */
		if (!methodInvocation.getMethodSignature().equals(methodSignature)) {

			List<MethodInvocation> subsumingMethods = local.getThird()
					.getOrderedSubsumingMethodInvocationsFor(methodInvocation);

			throw new ABCException("Unexpected method return from " + methodInvocation + "\nSubsuming methods; "
					+ ParsingUtils.prettyPrint(subsumingMethods));
		}

		if (!methodInvocation.isStatic() && !JimpleUtils.isConstructorOrClassConstructor(methodSignature)) {
			// We compare by object id since all the owner
			// attributes are not there yet
			String storedMethodOwner = methodInvocation.getOwner().getObjectId();

			if (!storedMethodOwner.equals(methodOwner)) {

				List<MethodInvocation> subsumingMethods = local.getThird()
						.getOrderedSubsumingMethodInvocationsFor(methodInvocation);

				throw new ABCException("Method owner does not match with " + storedMethodOwner + "\nSubsuming methods; "
						+ subsumingMethods.toString().replaceAll(",", "\n"));
			}

		}

		/*
		 * At this point we found a matching method call we update the data structures.
		 */
		if (JimpleUtils.isConstructor(methodSignature)) {
			/*
			 * Ownership info for constructors are available only by the end of the
			 * invocation... This is not 100% accurate but we try to live with that...
			 */
			ObjectInstance owner = new ObjectInstance(methodOwner);
			// TODO Move to decorator
//			owner.setAndroidActivity(isActivity(sootClass));
//			owner.setAndroidFragment(isFragment(sootClass));
			methodInvocation.setOwner(owner);
			local.getSecond().addDataDependencyOnOwner(methodInvocation, owner);
			// Still not sure WHY we need to register the data
			// deps
			// if
			// we have that into the methodInvocation and data
			// deps...
			// executionFlowGraph.addOwnerToMethodInvocation(methodInvocation,
			// owner);
		}
		/*
		 * Store Instance of Exception.
		 */
		ObjectInstance exceptionValue = DataNodeFactory.getFromException(exceptionAsString);
		methodInvocation.setRaisedException(exceptionValue);
		local.getSecond().addDataDependencyOnReturn(methodInvocation, exceptionValue);

		if (logger.isTraceEnabled()) {
			logger.trace("Method invocation:" + methodInvocation + " raised exception " + exceptionValue);
		}

	}

	@Override
	public void parseMethodReturn(AtomicInteger globalInvocationCount, ParsedLine tokens) throws ABCException {

		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> local = getOrInitializeDataStructure(
				tokens.threadName);

		String methodSignature = tokens.methodSignature;
		String methodOwner = tokens.methodOwner;
		String parameterString = tokens.parametersOrReturnValue;

		MethodInvocation methodInvocation;
		String returnValueAsString = parameterString.isEmpty() ? null : parameterString;
		/*
		 * Retrieve the last method invocation registered Check that this corresponds to
		 * the one we just parsed Method Signatures must match and owner must match
		 * unless this is a call to constructor
		 */
		methodInvocation = local.getThird().pop();

		/*
		 * Check this is actually the call we are looking for
		 */
		if (!methodInvocation.getMethodSignature().equals(methodSignature)) {

			List<MethodInvocation> subsumingMethods = local.getThird()
					.getOrderedSubsumingMethodInvocationsFor(methodInvocation);

			throw new ABCException("Unexpected method return from " + methodInvocation + "\nSubsuming methods; "
					+ ParsingUtils.prettyPrint(subsumingMethods));
		}

		if (!methodInvocation.isStatic() && !JimpleUtils.isConstructorOrClassConstructor(methodSignature)) {
			// We compare by object id since all the owner
			// attributes are not there yet
			String storedMethodOwner = methodInvocation.getOwner().getObjectId();

			if (!storedMethodOwner.equals(methodOwner)) {

				List<MethodInvocation> subsumingMethods = local.getThird()
						.getOrderedSubsumingMethodInvocationsFor(methodInvocation);

				throw new ABCException("Method owner does not match with " + storedMethodOwner + "\nSubsuming methods; "
						+ subsumingMethods.toString().replaceAll(",", "\n"));
			}

		}

		/*
		 * At this point we found a matching method call we update the data structures.
		 */
		if (JimpleUtils.isConstructor(methodSignature)) {
			/*
			 * Ownership info for constructors are available only by the end of the
			 * invocation... This is not 100% accurate but we try to live with that...
			 */
			ObjectInstance owner = new ObjectInstance(methodOwner);
			// TODO Move to decorator
//			owner.setAndroidActivity(isActivity(sootClass));
//			owner.setAndroidFragment(isFragment(sootClass));
			methodInvocation.setOwner(owner);
			local.getSecond().addDataDependencyOnOwner(methodInvocation, owner);
			// Still not sure WHY we need to register the data
			// deps
			// if
			// we have that into the methodInvocation and data
			// deps...
			// executionFlowGraph.addOwnerToMethodInvocation(methodInvocation,
			// owner);
		}
		/*
		 * Return type
		 */
		String returnType = methodSignature.trim().split(" ")[1];
		if (!JimpleUtils.isVoid(returnType)) {
			DataNode returnValue = DataNodeFactory.get(returnType, returnValueAsString);
			methodInvocation.setReturnValue(returnValue);
			local.getSecond().addDataDependencyOnReturn(methodInvocation, returnValue);
		}
		if (logger.isTraceEnabled())
			logger.trace("Returning from Method invocation:" + methodInvocation + ""
					+ ((methodInvocation.isStatic()) ? " static" : "")
					+ ((methodInvocation.isLibraryCall()) ? " libCall" : ""));
	}

	public void parseMethodInvocation_OLD(AtomicInteger globalInvocationCount, ParsedLine tokens) throws ABCException {
		try {

			Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> local = getOrInitializeDataStructure(
					tokens.threadName);

			String methodSignature = tokens.methodSignature;
			String openingToken = tokens.methodToken;
			String methodOwner = tokens.methodOwner;
			String parameterString = tokens.parametersOrReturnValue;

			MethodInvocation methodInvocation;
			/*
			 * This is the basic carving object, a method call/method invocation
			 */
			methodInvocation = new MethodInvocation(globalInvocationCount.incrementAndGet(), methodSignature);
			/*
			 * Explicitly set the relevant attributes of the object
			 */
			methodInvocation.setLibraryCall(openingToken.equals(Trace.LIB_METHOD_START_TOKEN));
			methodInvocation.setSyntheticMethod(openingToken.equals(Trace.SYNTHETIC_METHOD_START_TOKEN));
			methodInvocation.setPrivate(openingToken.equals(Trace.PRIVATE_METHOD_START_TOKEN));

			if (methodInvocation.isSynthetic()) {
				/*
				 * A call which lacks the owner and is not an instance constructor must be
				 * static. Note there's no way from the methodSignature to distinguish static vs
				 * instance calls but we cannot load the soot method for syntethic method call
				 * generated by ABC.
				 */
				if (!JimpleUtils.isConstructor(methodSignature)) {
					methodInvocation.setStatic(methodOwner.isEmpty());
				}
				// Since the method is generate we need to use an
				// heuristic
				// to see if that's lifecycle

			} else {
				// TODO Do we really need this information from soot? or in general? MOVE TO
				// DECORATOR ?
				SootMethod sootMethod = ParsingUtils.getSootMethodFor(methodSignature);

				if (sootMethod != null) {

					methodInvocation.setStatic(sootMethod.isStatic());
					methodInvocation.setPrivate(sootMethod.isPrivate());
					methodInvocation.setPublic(sootMethod.isPublic());
					methodInvocation.setProtected(sootMethod.isProtected());
				} else if (ParsingUtils.isArrayMethod(methodSignature)) {
					/*
					 * This is a method that we created on the fly, it does not exist for real...
					 */
					methodInvocation.setStatic(false);
					methodInvocation.setPrivate(false);
					methodInvocation.setPublic(true);
					methodInvocation.setProtected(false);

					methodInvocation.setConstructor(JimpleUtils.isConstructorOrClassConstructor(methodSignature));

				} else {
					/*
					 * At this point sootMethod might be null for special classes like
					 * "BuildConfig". In this case we rely on heuristics to set the details of the
					 * methodInvocation. Usually those classes have no methods except <clinit>
					 */

					if (JimpleUtils.isClassConstructor(methodSignature)) {
						methodInvocation.setStatic(true);
						// Really necessary ?
						methodInvocation.setConstructor(true);
					}
				}
			}

			// TODO Move to decorator
//			if (isActivity(sootClass)) {
//				methodInvocation.setAndroidActivityCallback(JimpleUtils.isActivityLifecycle(methodSignature));
//			} else if (isFragment(sootClass)) {
//				methodInvocation.setAndroidFragmentCallback(JimpleUtils.isFragmentLifecycle(methodSignature));
//			}

			/*
			 * Update the parsing data
			 */
			local.getFirst().enqueueMethodInvocations(methodInvocation);
			local.getSecond().addMethodInvocationWithoutAnyDependency(methodInvocation);
			local.getThird().push(methodInvocation);

			/*
			 * Update data dependencies and link the various objects
			 */

			/*
			 * Method Ownership for non static, non constructor method (including static
			 * initializers)
			 */
			if (!(methodInvocation.isStatic() || JimpleUtils.isConstructorOrClassConstructor(methodSignature))) {
				ObjectInstance owner = new ObjectInstance(methodOwner);
				// Is the sootclass is null we cannot tell.
				// TODO Move to decorator
//				owner.setAndroidActivity(isActivity(sootClass));
//				owner.setAndroidFragment(isFragment(sootClass));

				methodInvocation.setOwner(owner);
				local.getSecond().addDataDependencyOnOwner(methodInvocation, owner);
			}

			/*
			 * Parameters
			 */
			String[] actualParameters = getActualParameters(methodSignature, parameterString);
			String[] formalParameters = getFormalParameters(methodSignature);
			List<DataNode> actualParameterInstances = new ArrayList<>();

			for (int position = 0; position < actualParameters.length; position++) {
				String actualParameterAsString = actualParameters[position];
				String formalParameter = formalParameters[position];
				DataNode actualParameter = DataNodeFactory.get(formalParameter, actualParameterAsString);
				actualParameterInstances.add(actualParameter);
				local.getSecond().addDataDependencyOnActualParameter(methodInvocation, actualParameter, position);
			}
			methodInvocation.setActualParameterInstances(actualParameterInstances);

			if (logger.isTraceEnabled())
				logger.trace("Starting Method invocation:" + methodInvocation + ""
						+ ((methodInvocation.isStatic()) ? " static" : "")
						+ ((methodInvocation.isLibraryCall()) ? " libCall" : ""));
		} catch (Throwable e) {
			throw new ABCException("Cannot parsed method invocation", e);
		}
	}

	/**
	 * Parse method attributes but do not try to use Soot as backend to get
	 * additional data about methods
	 *
	 * @param globalInvocationCount
	 * @param tokens
	 * @throws ABCException
	 */
	public void basicParseMethodInvocation(AtomicInteger globalInvocationCount, ParsedLine tokens) throws ABCException {

		try {

			Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> local = getOrInitializeDataStructure(
					tokens.threadName);

			ExecutionFlowGraph executionFlowGraph = local.getFirst();
			DataDependencyGraph dataDependencyGraph = local.getSecond();
			CallGraph callGraph = local.getThird();

			String methodSignature = tokens.methodSignature;
			String openingToken = tokens.methodToken;
			String methodOwner = tokens.methodOwner;
			String parameterString = tokens.parametersOrReturnValue;

			MethodInvocation methodInvocation;
			/*
			 * This is the basic carving object, a method call/method invocation
			 */
			methodInvocation = new MethodInvocation(globalInvocationCount.incrementAndGet(), methodSignature);
			/*
			 * Explicitly set the relevant attributes of the object
			 */
			methodInvocation.setLibraryCall(openingToken.equals(Trace.LIB_METHOD_START_TOKEN));
			methodInvocation.setSyntheticMethod(openingToken.equals(Trace.SYNTHETIC_METHOD_START_TOKEN));
			methodInvocation.setPrivate(openingToken.equals(Trace.PRIVATE_METHOD_START_TOKEN));

			/*
			 * Update the parsing data
			 */
			executionFlowGraph.enqueueMethodInvocations(methodInvocation);
			dataDependencyGraph.addMethodInvocationWithoutAnyDependency(methodInvocation);
			callGraph.push(methodInvocation);

			/*
			 * Update data dependencies and link the various objects
			 */

			if (methodOwner != null) {
				// Instance method
				ObjectInstance owner = new ObjectInstance(methodOwner);
				methodInvocation.setOwner(owner);
				dataDependencyGraph.addDataDependencyOnOwner(methodInvocation, owner);
				methodInvocation.setStatic(false);
			} else if (JimpleUtils.isConstructor(methodSignature)) {
				// The constructor will return this object as result of the invocation
				methodInvocation.setConstructor(true);
				methodInvocation.setStatic(false);
			} else {
				// Here are actual static methods
				methodInvocation.setStatic(true);
				// Possibly constructors
				methodInvocation.setConstructor(JimpleUtils.isClassConstructor(methodSignature));
			}

			/*
			 * Parameters data
			 */
			String[] actualParameters = getActualParameters(methodSignature, parameterString);
			String[] formalParameters = getFormalParameters(methodSignature);
			List<DataNode> actualParameterInstances = new ArrayList<>();

			for (int position = 0; position < actualParameters.length; position++) {
				String actualParameterAsString = actualParameters[position];
				String formalParameter = formalParameters[position];
				// Store the information about parameter in a node
				DataNode actualParameter = DataNodeFactory.get(formalParameter, actualParameterAsString);
				actualParameterInstances.add(actualParameter);
				// Link the method invocation with its parameters
				dataDependencyGraph.addDataDependencyOnActualParameter(methodInvocation, actualParameter, position);
			}
			// TODO Why do we need to duplicate this information here?
			methodInvocation.setActualParameterInstances(actualParameterInstances);

			if (logger.isTraceEnabled()) {
				logger.trace("Starting Method invocation:" + methodInvocation + ""
						+ ((methodInvocation.isStatic()) ? " static" : "")
						+ ((methodInvocation.isLibraryCall()) ? " libCall" : ""));
			}
		} catch (Throwable e) {
			throw new ABCException("Cannot parsed method invocation", e);
		}

	}

	/*
	 * Initialize the data structure holding the parsing results or getting the
	 * existing one
	 */
	private Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> getOrInitializeDataStructure(
			String threadName) {
		if (!parsedElements.containsKey(threadName)) {
			parsedElements.put(threadName, new Triplette<>(
          new ExecutionFlowGraphImpl(), new DataDependencyGraphImpl(), new CallGraphImpl()));
		}
		return parsedElements.get(threadName);
	}

	@Override
	public void parseMethodInvocation(AtomicInteger globalInvocationCount, ParsedLine tokens) throws ABCException {
		basicParseMethodInvocation(globalInvocationCount, tokens);
	}

	@Override
	public void parsePrivateMethodInvocation(AtomicInteger globalInvocationCount, ParsedLine tokens)
			throws ABCException {
		// TODO
		basicParseMethodInvocation(globalInvocationCount, tokens);

	}

	@Override
	public void parseSyntheticMethodInvocation(AtomicInteger globalInvocationCount, ParsedLine tokens)
			throws ABCException {
		// TODO
		basicParseMethodInvocation(globalInvocationCount, tokens);

	}

}
