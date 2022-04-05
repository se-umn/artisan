package de.unipassau.abc.generation.mocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.carving.CarvedExecution;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.DataNode;
import de.unipassau.abc.data.DataNodeFactory;
import de.unipassau.abc.data.EnumConstant;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.NullInstance;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.data.ObjectInstanceFactory;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.data.PrimitiveNodeFactory;
import de.unipassau.abc.data.PrimitiveValue;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.generation.data.CarvedTest;
import edu.emory.mathcs.backport.java.util.concurrent.atomic.AtomicInteger;

public class MockGenerator {

    private final static Logger logger = LoggerFactory.getLogger(MockGenerator.class);
    
    private static final String MOCK_SIGNATURE = "<org.mockito.Mockito: java.lang.Object mock(java.lang.Class)>";
    private static final String RETURN_SIGNATURE = "<org.mockito.Mockito: org.mockito.stubbing.Stubber doReturn(java.lang.Object)>";
    private static final String RETURN_VOID_SIGNATURE = "<org.mockito.Mockito: org.mockito.stubbing.Stubber doNothing()>";

    private static final String WHEN_SIGNATURE = "<org.mockito.stubbing.Stubber: java.lang.Object when(java.lang.Object)>";
    private static final String EXTRACT_SIGNATURE = "<org.robolectric.shadow.api.Shadow: java.lang.Object extract(java.lang.Object)>";
    private static final String SET_SIGNATURE = "<java.lang.Object: void setMockFor(java.lang.String,java.lang.Object)>";
    private static final String STRICT_SIGNATURE = "<java.lang.Object: void setStrictShadow()>";

    // This is a bit tricky
    private AtomicInteger uniqueIdGenerator = new AtomicInteger(0);
    private AtomicInteger logicaTimeStampGenerator = new AtomicInteger(0);
    // Pay attention that this should be unique in absolute terms and there are some
    // that are fixed, e.g., 123 and 1234
    // We cannot use 0
    private AtomicInteger freshObjectId = new AtomicInteger(1);

//            MethodInvocation.INVOCATION_TRACE_ID_NA_CONSTANT);

    

    /**
     * Generate mocks is an iterative process. We start from the carvedTest and
     * mock visible (but dangling objects). While doing so we might expose
     * additional dependencies that before were buries in the carved execution,
     * therefore we need to re-carve and mock them again...
     * 
     * @param carvedTest
     * @param carvedExecution
     */
    public void generateMocks(CarvedTest carvedTest, CarvedExecution carvedExecution) {
        // There's another Issue: the mocks ID must be generated in reverse order since
        // we are going backwards
        uniqueIdGenerator.addAndGet(-1000);
        logicaTimeStampGenerator.addAndGet(-1000);

        // We assume that there are no more than 1000 calls in each mocking cycle
        boolean repeatMocking = generateMocksForVisibleCalls(carvedTest, carvedExecution);
        while (repeatMocking) {
            uniqueIdGenerator.addAndGet(-1000);
            logicaTimeStampGenerator.addAndGet(-1000);
            repeatMocking = generateMocksForVisibleCalls(carvedTest, carvedExecution);
        }

        // Does this require some sort of iteration?
        // I am not sure this really matter here...
        logicaTimeStampGenerator.addAndGet(-1000);
        uniqueIdGenerator.addAndGet(-1000);
        carvedTest = generateShadows(carvedTest, carvedExecution);

    }

    /**
     * Return true only if it has generated new mocks, in that case, we might need
     * to repeate the process one more time
     * 
     * @param carvedTest
     * @param carvedExecution
     * @return
     */
    public boolean generateMocksForVisibleCalls(CarvedTest carvedTest, CarvedExecution carvedExecution) {
        logger.info("-------------------------");
        logger.info("  MOCKING OBJECT IN VISIBLE CALLS " + carvedTest.getMethodUnderTest());
        logger.info("-------------------------");

        ExecutionFlowGraph carvedTestExecutionFlowGraph = carvedTest.getExecutionFlowGraph();
        DataDependencyGraph carvedTestDataDependencyGraph = carvedTest.getDataDependencyGraph();

        MethodInvocation methodInvocationUnderTest = carvedTest.getMethodUnderTest();

        List<MockInfo> mockInfoList = new ArrayList<MockInfo>();

        boolean newMocksGenerated = false;

        // Stubbers and EnumItems shall not be mocked
        for (ObjectInstance danglingObject : carvedTestDataDependencyGraph.getDanglingObjects().stream()
                .filter(obj -> !obj.getType().equals("org.mockito.stubbing.Stubber"))
                .filter(obj -> !(obj instanceof EnumConstant)).collect(Collectors.toList())) {

            List<MethodInvocation> allCallsThatRequireTheDanglingObject = new ArrayList<>(
                    carvedExecution.getDataDependencyGraphContainingTheMethodInvocationUnderTest()
                            .getMethodInvocationsForOwner(danglingObject).stream().filter(
                                    // Filters Mockito limitations and non-sensical mocking, like equals()
                                    mi -> !mi.getMethodSignature().contains("boolean equals")

                            ).collect(Collectors.toList()));

            // If a call is to super type (same object, same call, subsumed but with
            // different type) we skip it, i.e., it's not a recursive call
            Collections.sort(allCallsThatRequireTheDanglingObject);

            List<MethodInvocation> callsThatRequireTheDanglingObject = new ArrayList<>();
            for (MethodInvocation mi : allCallsThatRequireTheDanglingObject) {

                // How it is possible that
                // carvedExecution.getCallGraphContainingTheMethodInvocation(mi) == null ?!
                // It seems that only calls to findItem cause this issue... so maybe there's
                // something fishy about finding the ids.
                if (carvedExecution.getCallGraphContainingTheMethodInvocation(mi) == null) {
                    logger.error("There is no call graph that contains " + mi);
                    continue;
                }

                List<MethodInvocation> possiblySubsumingMI = allCallsThatRequireTheDanglingObject.stream().filter(_mi ->
                // We care about _mi that happens before us
                _mi.isBefore(mi) &&
                // and are in the same CG
                        carvedExecution.getCallGraphContainingTheMethodInvocation(_mi)
                                .equals(carvedExecution.getCallGraphContainingTheMethodInvocation(mi))
                        &&
                        // and subsume us - if we are in the same graph
                        carvedExecution.getCallGraphContainingTheMethodInvocation(_mi)
                                .getMethodInvocationsSubsumedBy(_mi).contains(mi))
                        .collect(Collectors.toList());

                // Check whenter any of those call has the method name (and parameter), but
                // different type
                List<MethodInvocation> callsToSuper = possiblySubsumingMI.stream().filter(smi ->
                // Same return type
                JimpleUtils.getReturnType(smi.getMethodSignature())
                        .equals(JimpleUtils.getReturnType(mi.getMethodSignature()))
                        && JimpleUtils.getMethodName(smi.getMethodSignature())
                                .equals(JimpleUtils.getMethodName(mi.getMethodSignature()))
                        && Arrays.equals(JimpleUtils.getParameterList(smi.getMethodSignature()),
                                JimpleUtils.getParameterList(mi.getMethodSignature()))
                        && !JimpleUtils.getClassNameForMethod(smi.getMethodSignature())
                                .equals(JimpleUtils.getClassNameForMethod(mi.getMethodSignature())))
                        .collect(Collectors.toList());

                if (callsToSuper.size() > 0) {
                    logger.debug("Found usage of super " + callsToSuper + " : " + mi);
                } else {
                    callsThatRequireTheDanglingObject.add(mi);
                }

            }

            logger.debug("Found that dangling object " + danglingObject + " is invoked "
                    + callsThatRequireTheDanglingObject.size() + " times");
//            callsThatRequireTheDanglingObject.forEach(mi -> {
//                logger.debug("-- " + mi);
//            });

            // TODO Do we need to add a patch for the containers like EnumSet?
            if (JimpleUtils.isArray(danglingObject.getType())) {
                // Instead of mocking the array we need to create one with the expected size and
                // filled with the content in the expected place
                logger.debug("Mock array " + danglingObject);

                // Look for the length operation (TODO guess the size of the array taking the
                // max index otherwise)
                MethodInvocation newArrayOperation = null;
                //
                List<MethodInvocation> storeOperations = new ArrayList<>();

                int arraySize = -1;
                int maxStoreIndex = 0;

                for (MethodInvocation callOnArray : callsThatRequireTheDanglingObject) {
                    if (JimpleUtils.getMethodName(callOnArray.getMethodSignature()).equals("length")) {
                        arraySize = Integer.parseInt(((PrimitiveValue) callOnArray.getReturnValue()).toString());
                    }

                    if (JimpleUtils.getMethodName(callOnArray.getMethodSignature()).equals("get")) {
                        int storedPosition = Integer.parseInt(
                                ((PrimitiveValue) callOnArray.getActualParameterInstances().get(0)).toString());
                        maxStoreIndex = (maxStoreIndex > storedPosition) ? maxStoreIndex : storedPosition;
                    }
                }
                arraySize = (arraySize != -1) ? arraySize : maxStoreIndex;
                DataNode arraySizeParameter = PrimitiveNodeFactory.get("int", "" + arraySize);

                // Create the newArrayOperation and make it return the danglingObject
                String arrayConstructorSignature = "<" + danglingObject.getType() + ": void <init>(int)>";
                MethodInvocation initArray = new MethodInvocation(logicaTimeStampGenerator.getAndIncrement(),
                        // MethodInvocation.INVOCATION_TRACE_ID_NA_CONSTANT,
                        uniqueIdGenerator.getAndIncrement(), arrayConstructorSignature);
                initArray.setConstructor(true);
                initArray.setStatic(false);
                initArray.setNecessary(true);
                initArray.setActualParameterInstances(Arrays.asList(arraySizeParameter));
                initArray.setOwner(danglingObject);

                // Inject the created call inside the graphs
                carvedTestExecutionFlowGraph.enqueueMethodInvocations(initArray);
                // Fix the data deps
                carvedTestDataDependencyGraph.addMethodInvocationWithoutAnyDependency(initArray);
                carvedTestDataDependencyGraph.addDataDependencyOnActualParameter(initArray, arraySizeParameter, 0);

                // At this point we can attempt to generate the calls that store whatever
                // objects in the expected position

                for (MethodInvocation callOnArray : callsThatRequireTheDanglingObject) {
                    if (JimpleUtils.getMethodName(callOnArray.getMethodSignature()).equals("length")) {
                        arraySize = Integer.parseInt(((PrimitiveValue) callOnArray.getReturnValue()).toString());
                    }

                    if (JimpleUtils.getMethodName(callOnArray.getMethodSignature()).equals("get")) {
                        DataNode positionToFill = callOnArray.getActualParameterInstances().get(0);
                        DataNode objectToStore = callOnArray.getReturnValue();

                        // Create the newArrayOperation and make it return the danglingObject
                        String arrayStoreSignature = "<" + danglingObject.getType() + ": void store(int,"
                                + danglingObject.getType().toString().replaceFirst("\\[\\]", "") + ")>";

                        MethodInvocation storeToArray = new MethodInvocation(logicaTimeStampGenerator.getAndIncrement(),
                                // MethodInvocation.INVOCATION_TRACE_ID_NA_CONSTANT,
                                uniqueIdGenerator.getAndIncrement(), arrayStoreSignature);
                        storeToArray.setConstructor(false);
                        storeToArray.setStatic(false);
                        storeToArray.setNecessary(true);
                        storeToArray.setActualParameterInstances(Arrays.asList(positionToFill, objectToStore));
                        storeToArray.setOwner(danglingObject);

                        // Inject the created call inside the graphs
                        carvedTestExecutionFlowGraph.enqueueMethodInvocations(storeToArray);
                        // Fix the data deps
                        carvedTestDataDependencyGraph.addMethodInvocationWithoutAnyDependency(storeToArray);
                        carvedTestDataDependencyGraph.addDataDependencyOnActualParameter(storeToArray, positionToFill,
                                0);
                        carvedTestDataDependencyGraph.addDataDependencyOnActualParameter(storeToArray, objectToStore,
                                1);
                    }

                }

            } else {
                /*
                 * Original code for mocking. It should produce code like this:
                 *
                 * Foo foo1 = Mockito.mock(Foo.class); Foo foo2 = foo1.doReturn("whatever"); Foo
                 * foo3 = when(foo2) foo3.callBar();
                 */

                ObjectInstance instanceToMock = null;

                if (callsThatRequireTheDanglingObject.size() > 0) {
                    instanceToMock = ObjectInstanceFactory
                            .get(danglingObject.getType() + "@" + uniqueIdGenerator.getAndIncrement());
                } else {
                    logger.debug("Mock initializer will return the dangling object ");
                    instanceToMock = danglingObject;
                }

                DataNode classLiteralToMock = PrimitiveNodeFactory.createClassLiteralFor(instanceToMock);

                // A mock object is created calling the Mockito framework and then configured.
                // At each configuration a new instance is returned
                // so we need to get the very last one and replace all the uses of the dangling
                // objects with that mocked one

                MethodInvocation initMock = new MethodInvocation(logicaTimeStampGenerator.getAndIncrement(),
//                        MethodInvocation.INVOCATION_TRACE_ID_NA_CONSTANT,
                        uniqueIdGenerator.getAndIncrement(), MOCK_SIGNATURE);
                initMock.setHasGenericReturnType(true);
                initMock.setStatic(true);
                initMock.setNecessary(true);
                initMock.setActualParameterInstances(Arrays.asList(classLiteralToMock));
                initMock.setReturnValue((DataNode) instanceToMock);

                // Inject mock creation inside the graphs
                carvedTestExecutionFlowGraph.enqueueMethodInvocations(initMock);

                carvedTestDataDependencyGraph.addMethodInvocationWithoutAnyDependency(initMock);
                carvedTestDataDependencyGraph.addDataDependencyOnActualParameter(initMock, classLiteralToMock, 0);
                carvedTestDataDependencyGraph.addDataDependencyOnReturn(initMock, (DataNode) instanceToMock);

                logger.debug("Created mock initializer " + initMock);

                // Configure the mock object
                List<MethodInvocation> callSet = new ArrayList<MethodInvocation>();
                // need to use the carved execution here because the methods we care about are
                // not be in the test yet
                logger.debug("Configure the mock objects");

                for (int i = 0; i < callsThatRequireTheDanglingObject.size(); i++) {

                    MethodInvocation dependentCall = callsThatRequireTheDanglingObject.get(i);

                    logger.debug("* Configure mock for calling " + dependentCall);

                    // if return value is non-primitive (ie, an ObjectInstance), we may want to use
                    // a previously defined mock here (or at least try)
                    // relatedly, handle java.lang.String objects as below (can strings appear as
                    // dangling objects???)

                    ObjectInstance returnStubber = returnStubber = ObjectInstanceFactory
                            .get("org.mockito.stubbing.Stubber@" + uniqueIdGenerator.getAndIncrement());
                    if (JimpleUtils.hasVoidReturnType(dependentCall.getMethodSignature())) {
                        // Handle mocked methods that return nothing, i.e., void

                        MethodInvocation doReturnNothingMock = new MethodInvocation(
                                logicaTimeStampGenerator.getAndIncrement(), uniqueIdGenerator.getAndIncrement(),
                                RETURN_VOID_SIGNATURE);
                        doReturnNothingMock.setStatic(true);
                        doReturnNothingMock.setNecessary(true);
                        doReturnNothingMock.setReturnValue((DataNode) returnStubber);

                        carvedTestExecutionFlowGraph.enqueueMethodInvocations(doReturnNothingMock);

                        carvedTestDataDependencyGraph.addMethodInvocationWithoutAnyDependency(doReturnNothingMock);
                        carvedTestDataDependencyGraph.addDataDependencyOnReturn(doReturnNothingMock, returnStubber);

                        logger.debug("\t Adding doReturn " + doReturnNothingMock);
                        // handle calls to mock
                        callSet.add(doReturnNothingMock);
                    } else {
                        DataNode returnValue;
                        try {
                            returnValue = carvedExecution.getDataDependencyGraphContainingTheMethodInvocationUnderTest()
                                    .getReturnValue(dependentCall).get();
                        } catch (ABCException e) {
                            // Should we fail silently?
                            logger.error("Failed to retrieve return value for " + dependentCall, e);
                            throw new RuntimeException(e);
                        }

                        DataNode doReturnArgument = null;
                        if (returnValue instanceof NullInstance) {
                            doReturnArgument = DataNodeFactory.get(returnValue.getType(), null);
                        } else if (returnValue instanceof PrimitiveValue) {
                            doReturnArgument = DataNodeFactory.get(returnValue.getType(),
                                    ((PrimitiveValue) returnValue).getStringValue());
                        } else {
                            doReturnArgument = DataNodeFactory.get(returnValue.getType(), returnValue.toString());
                        }
                        // TODO If return value is a NullInstance we should return a Null Instance ?

                        MethodInvocation doReturnMock = new MethodInvocation(logicaTimeStampGenerator.getAndIncrement(),
//                            MethodInvocation.INVOCATION_TRACE_ID_NA_CONSTANT, 
                                uniqueIdGenerator.getAndIncrement(), RETURN_SIGNATURE);
                        doReturnMock.setStatic(true);
                        doReturnMock.setActualParameterInstances(Arrays.asList(doReturnArgument));
                        doReturnMock.setReturnValue((DataNode) returnStubber);
                        doReturnMock.setNecessary(true);

                        carvedTestExecutionFlowGraph.enqueueMethodInvocations(doReturnMock);

                        carvedTestDataDependencyGraph.addMethodInvocationWithoutAnyDependency(doReturnMock);
                        carvedTestDataDependencyGraph.addDataDependencyOnActualParameter(doReturnMock, doReturnArgument,
                                0);
                        carvedTestDataDependencyGraph.addDataDependencyOnReturn(doReturnMock, returnStubber);

                        logger.debug("\t Adding doReturn " + doReturnMock);
                        // handle calls to mock
                        callSet.add(doReturnMock);
                    }

                    MethodInvocation whenMock = new MethodInvocation(logicaTimeStampGenerator.getAndIncrement(),
//                            MethodInvocation.INVOCATION_TRACE_ID_NA_CONSTANT,
                            uniqueIdGenerator.getAndIncrement(), WHEN_SIGNATURE);
                    whenMock.setHasGenericReturnType(true);
                    whenMock.setOwner(returnStubber);
                    whenMock.setActualParameterInstances(Arrays.asList(instanceToMock));
                    whenMock.setNecessary(true);

                    // TODO This might be wrong !
                    // If this is the last call to mock we need to make it return the dangling
                    // object, not a new instance
                    ObjectInstance whenReturn = null;
                    if (i == callsThatRequireTheDanglingObject.size() - 1) {
                        logger.debug("\t Link the mock to the dangling object");
                        whenReturn = danglingObject;
                    } else {
//                        logger.debug("\t Create a temporary mock object");
                        whenReturn = ObjectInstanceFactory
                                .get(danglingObject.getType() + "@" + uniqueIdGenerator.getAndIncrement());
                    }
                    // set the appropriate return value
                    whenMock.setReturnValue(whenReturn);

                    carvedTestExecutionFlowGraph.enqueueMethodInvocations(whenMock);
                    carvedTestDataDependencyGraph.addMethodInvocationWithoutAnyDependency(whenMock);

                    carvedTestDataDependencyGraph.addDataDependencyOnActualParameter(whenMock, instanceToMock, 0);
                    carvedTestDataDependencyGraph.addDataDependencyOnOwner(whenMock, returnStubber);
                    carvedTestDataDependencyGraph.addDataDependencyOnReturn(whenMock, whenReturn);

//                    logger.debug("\t Adding When " + whenMock);

                    // Now we need to invoke the original call on the mocked object so to configure
                    // it.
                    MethodInvocation dependentCallCopy = dependentCall.clone();

                    // TODO Why this code is duplicated? !
                    dependentCallCopy.setInvocationTraceId(logicaTimeStampGenerator.getAndIncrement());
                    dependentCallCopy.setInvocationCount(uniqueIdGenerator.getAndIncrement());

                    dependentCallCopy.setOwner(whenReturn);
                    // Why we need it? should this be already part of dependentCall.clone()
                    dependentCallCopy.setStatic(false);
                    dependentCallCopy.setNecessary(true);

                    // Override the return value to break dependencies
                    if (!JimpleUtils.hasVoidReturnType(dependentCallCopy.getMethodSignature())) {
                        dependentCallCopy.setReturnValue(ObjectInstanceFactory.get(
                                dependentCallCopy.getReturnValue().getType() + "@" + freshObjectId.getAndIncrement()));
                    }

                    // TODO How do we handle parameters of this call? We keep the same as the
                    // original one but shouldn't we list them here as part of the ddg?
                    carvedTestExecutionFlowGraph.enqueueMethodInvocations(dependentCallCopy);
                    carvedTestDataDependencyGraph.addMethodInvocationWithoutAnyDependency(dependentCallCopy);
                    carvedTestDataDependencyGraph.addDataDependencyOnOwner(dependentCallCopy, whenReturn);
                    // TODO
                    // Propagate any paramater to this call... The issue here is that we should use
                    // parameter matchers !
                    for (int j = 0; j < dependentCallCopy.getActualParameterInstances().size(); j++) {
                        DataNode parameter = dependentCallCopy.getActualParameterInstances().get(j);
                        carvedTestDataDependencyGraph.addDataDependencyOnActualParameter(dependentCallCopy, parameter,
                                j);
                    }

//                    logger.debug("\t Adding Mocked Call " + dependentCallCopy);

                    // handle calls to mock
                    callSet.add(whenMock);
                    callSet.add(dependentCallCopy);
                }

                // At this point we configured the mock objects for each call and we have many
                // object instances that in reality correspond to the same one. The last call to
                // the mock returns our
                // dangling object

                mockInfoList.add(new MockInfo(initMock, callSet));
                newMocksGenerated = true;
            }

            // TODO Not sure this is really needed
            // adding dummy dependencies between mocks
//            for (int i = 0; i < mockInfoList.size() - 1; ++i) {
//                MockInfo currMock = mockInfoList.get(i);
//                MockInfo nextMock = mockInfoList.get(i + 1);
//                for (MethodInvocation methodInvocation : currMock.getCalls()) {
//                    carvedTestDataDependencyGraph.addDataDependencyOnDummy(methodInvocation, nextMock.getMock());
//                }
//            }
//            for (int i = 0; i < mockInfoList.size() - 1; ++i) {
//                MockInfo currMock = mockInfoList.get(i);
//                for (MethodInvocation methodInvocation : currMock.getCalls()) {
//                    carvedTestDataDependencyGraph.addDataDependencyOnDummy(methodInvocation, methodInvocationUnderTest);
//                }
//            }

        }

        return newMocksGenerated;
    }

    /**
     * Generating shadows consists in:
     * <ol>
     * <li>1. finding the Views that should be shadowed by look at findViewById
     * method invocation.
     * 
     * We need a shadow every time we need to simulate an interaction with an
     * Android object that is NOT visible in the test, but used somewhere. For
     * example, a text field set that is read by the method under test.</li>
     * <li>2. Programming the behavior of those objects, by looking at how they are
     * used</li>
     * 
     * <li>3. Linking the shadow to the mocked object</li>
     * 
     * <li>4. Inserting the shadow in the right place in the carved test. Shadows
     * are only possible AFTER an activity is created (and setup?)</li>
     * 
     * </ol>
     * 
     * 
     * 
     * @param carvedTest
     * @param carvedExecution
     * @return
     */
    public CarvedTest generateShadows(CarvedTest carvedTest, CarvedExecution carvedExecution) {
        logger.info("-------------------------");
        logger.info("  SHADOWING " + carvedTest.getMethodUnderTest());
        logger.info("-------------------------");

        ExecutionFlowGraph carvedTestExecutionFlowGraph = carvedTest.getExecutionFlowGraph();
        DataDependencyGraph carvedTestDataDependencyGraph = carvedTest.getDataDependencyGraph();

        MethodInvocation methodInvocationUnderTest = carvedTest.getMethodUnderTest();

        List<MockInfo> mockInfoList = new ArrayList<MockInfo>();

        // Find the findViewById method invocations in the carved test and methods
        // subsumed by it
        final List<MethodInvocation> allMethodInvocationsInTheCarvedTest = new ArrayList<MethodInvocation>();

        carvedTest.getExecutionFlowGraph().getOrderedMethodInvocations().stream()//
                // TODO this should be safe...
                .filter(mi -> !mi.isSynthetic())//
                .forEach(dmi -> {
                    allMethodInvocationsInTheCarvedTest.add(dmi);
                    allMethodInvocationsInTheCarvedTest.addAll(
                            // Some calls, like fail(), have been added to the carvedTest instead of the
                            // carvedExecution, so there's no
                            // getCallGraphContainingTheMethodInvocation for them in the carvedExecution

                            carvedExecution.getCallGraphContainingTheMethodInvocation(dmi)
                                    .getMethodInvocationsSubsumedBy(dmi));
                });

        // Filter out duplicates
        Set<MethodInvocation> uniqueMethodInvocations = new HashSet<MethodInvocation>(
                allMethodInvocationsInTheCarvedTest);
        // Sort by timestamp
        allMethodInvocationsInTheCarvedTest.clear();
        allMethodInvocationsInTheCarvedTest.addAll(uniqueMethodInvocations);
        Collections.sort(allMethodInvocationsInTheCarvedTest);

        // ALESSIO: Why here we limit ONLY to look at the call graph that contains the
        // method under
        // test
//        if (!JimpleUtils.isActivityLifecycle(carvedTest.getMethodUnderTest().getMethodSignature())) {
//            subsumedCandidateMethodInvocations
//                    .addAll(carvedExecution.getCallGraphContainingTheMethodInvocationUnderTest()
//                            .getMethodInvocationsSubsumedBy(carvedTest.getMethodUnderTest()));
//        }
////        else {
////            System.out.println(">> Skipping LifeCycle Method: " + carvedTest.getMethodUnderTest().getMethodSignature());
////        }

        // Accumulate ALL the calls to made in this test unless the method under test is
        // an ActivityLifecycle...
        // TODO Why this matters? I guess the only exception is onCreate and onSetup ?
//        if (!JimpleUtils.isActivityLifecycle(carvedTest.getMethodUnderTest().getMethodSignature())) {
//
//            for (MethodInvocation candidateMethodInvocation : directlyCallableMethods) {
//
//                if (candidateMethodInvocation.isSynthetic()) {
//                    continue;
//                }
//
//                // Skip mocked and other generated method calls
//                if (carvedExecution.getCallGraphContainingTheMethodInvocation(candidateMethodInvocation) == null) {
//                    continue;
//                }
//
//                try {
//                    allMethodInvocationsInTheCarvedTest
//                            .addAll(carvedExecution.getCallGraphContainingTheMethodInvocation(candidateMethodInvocation)
//                                    .getMethodInvocationsSubsumedBy(candidateMethodInvocation));
//                } catch (NullPointerException e) {
//                    logger.error("NULL!");
//                    throw e;
//                }
//            }
//        }

        // Make sure we do not consider activity (and probably fragments and similar
        // here?)
        // I guess we should not consider findViewById contained in onCreate since we
        // need that method to get the shadow!
//      for (MethodInvocation subsumedCandidate : allMethodInvocationsInTheCarvedTest) {
//      if (subsumedCandidate.getMethodSignature().equals(
//              "<android.app.Activity: android.view.View findViewById(int,java.lang.String,java.lang.String)>")) {
//          logger.debug(" Found " + subsumedCandidate + " called on " + subsumedCandidate.getOwner());
//          carvedTestExecutionFlowGraph.enqueueMethodInvocations(subsumedCandidate);
//          carvedTestDataDependencyGraph.addMethodInvocationWithoutAnyDependency(subsumedCandidate);
//      }
//  }

        // Retrieve all the objects that must be shadowed
        Map<ObjectInstance, MethodInvocation> viewsToShadowAndCorrespondingFindViewById = allMethodInvocationsInTheCarvedTest
                .stream()
                .filter(mi -> mi.getMethodSignature().equals(
                        "<android.app.Activity: android.view.View findViewById(int,java.lang.String,java.lang.String)>"))
                // If mi is subsumed by onCreate we cannot consider it
                .filter(mi -> !carvedExecution.getCallGraphContainingTheMethodInvocation(mi)
                        .getOrderedSubsumingMethodInvocationsFor(mi).stream()
                        .filter(mmi -> mmi.getMethodSignature().contains("void onCreate(android.os.Bundle)"))
                        .findFirst().isPresent())
                .collect(Collectors.toMap(mi -> (ObjectInstance) mi.getReturnValue(), mi -> mi));

        if (viewsToShadowAndCorrespondingFindViewById.keySet().size() == 0) {
            logger.debug("There are no object Views to Shadow!");
            return carvedTest;
        }

        // This is where we need to insert the calls. It must be there, otherwise
        // shadowing cannot happen !
        // Just go to the next call
        final int onCreateTraceID = allMethodInvocationsInTheCarvedTest.stream()
                .filter(mi -> mi.getMethodSignature().contains("void onCreate(android.os.Bundle)"))
                .map(mi -> mi.getInvocationTraceId()).findFirst().get() + 1;

        // Temporary store the list of generated method invocations
        List<MethodInvocation> generatedMethodInvocations = new ArrayList();

        for (Entry<ObjectInstance, MethodInvocation> entry : viewsToShadowAndCorrespondingFindViewById.entrySet()) {
            // TODO Should this be another object (fresh instance) instead?
            ObjectInstance objectViewToShadow = entry.getKey();
            MethodInvocation findViewById = entry.getValue();

            logger.debug("Found " + objectViewToShadow + "provided by" + findViewById + " that must be shadowed");

            // Collect the uses of this view to configure the mocked object
            List<MethodInvocation> methodInvocationsOnTheObjectViewToShadow = allMethodInvocationsInTheCarvedTest
                    .stream().filter(mi -> !mi.isStatic() && mi.getOwner().equals(objectViewToShadow)
                            && mi.getInvocationTraceId() >= onCreateTraceID)
                    .collect(Collectors.toList());

            // Generate the mockery for the uses of the objectViewToShadow (the
            // instanceToMock).
            /*
             * Since generating mocks might expose new needs for mock we need to repeat the
             * process again
             */
            List<List<MethodInvocation>> theMockery = generateMockeryRecursively(carvedExecution, carvedTest,
                    objectViewToShadow, methodInvocationsOnTheObjectViewToShadow);

            // Invoke the activity findViewByID to return the objectViewToShadow. Clone the
            // original method !
            MethodInvocation reFindTheView = findViewById.clone();
            logger.debug("Inserting " + reFindTheView);
            generatedMethodInvocations.add(reFindTheView);

            Pair<MethodInvocation, CarvingShadow> extractShadowPair = generateTheExtractShadow(carvedTest,
                    objectViewToShadow);
            MethodInvocation extractShadow = extractShadowPair.getFirst();
            CarvingShadow currentlyConfiguredCarvingShadow = extractShadowPair.getSecond();

            ObjectInstance shadowedView = (ObjectInstance) extractShadow.getReturnValue();

            logger.debug("Inserting " + extractShadow);
            generatedMethodInvocations.add(extractShadow);
            logger.debug("Inserting Mockery");
            theMockery.forEach(aMockery -> {
                generatedMethodInvocations.addAll(aMockery);
            });

            logger.debug("Link mockery to shadow");
            // We care only about the last mockery here since it is the one setting the
            // ObjectView
            List<MethodInvocation> mockeryOfViewObject = theMockery.get(theMockery.size() - 1);
            ObjectInstance mockedObject = (ObjectInstance) mockeryOfViewObject.get(0).getReturnValue();
            mockeryOfViewObject.stream()
                    .filter(mi -> !mi.isStatic()
                            && !(mi.getMethodSignature().equals(RETURN_VOID_SIGNATURE)
                                    || mi.getMethodSignature().equals(RETURN_SIGNATURE))
                            && !mi.getMethodSignature().equals(WHEN_SIGNATURE))
                    .forEach(mocked -> {
                        logger.debug("Linking " + mocked);
                        String stubbedMethodSignature = mocked.getMethodSignature();
                        // Encode this to create the String node
                        String stringEncoded = Arrays.toString(stubbedMethodSignature.getBytes());
                        // Create the data node for the carved test
                        PrimitiveValue setShadowParameter = new PrimitiveValue(uniqueIdGenerator.getAndIncrement(),
                                "java.lang.String", stringEncoded);
                        // Create the method call that set the shadow
                        MethodInvocation setShadowMethodInvocation = new MethodInvocation(-1, -1, SET_SIGNATURE);
                        setShadowMethodInvocation.setPublic(true);
                        setShadowMethodInvocation.setOwner(shadowedView);
                        setShadowMethodInvocation.setActualParameterInstances(
                                Arrays.asList(new DataNode[] { setShadowParameter, mockedObject }));
                        generatedMethodInvocations.add(setShadowMethodInvocation);

                        // Configure the Carving shadow so it can generate the stub for this call
                        // TODO Assume this is called only once, otherwise we might need to count how
                        // many times that happened !
                        currentlyConfiguredCarvingShadow.getStubbedMethods().add(stubbedMethodSignature);
                    });

            MethodInvocation setStrictShadowMethodInvocation = new MethodInvocation(-1, -1, STRICT_SIGNATURE);
            setStrictShadowMethodInvocation.setOwner(shadowedView);
            generatedMethodInvocations.add(setStrictShadowMethodInvocation);

        }

        // Assign the right IDs to the generatedMethodInvocations
        Collections.reverse(generatedMethodInvocations);
        generatedMethodInvocations.forEach(mi -> {
            // This is the actual time stamp
            mi.setInvocationTraceId(onCreateTraceID);
            // This is the tie breaker
            mi.setInvocationCount(uniqueIdGenerator.getAndIncrement());
        });
        // Put them back in the original order and store them in the carvedTest
        Collections.reverse(generatedMethodInvocations);
        generatedMethodInvocations.forEach(mi -> {
            carvedTestExecutionFlowGraph.enqueueMethodInvocations(mi);
            //
            if (!mi.isStatic()) {
                carvedTestDataDependencyGraph.addDataDependencyOnOwner(mi, mi.getOwner());
            }

            if (!JimpleUtils.hasVoidReturnType(mi.getMethodSignature())) {
                carvedTestDataDependencyGraph.addDataDependencyOnReturn(mi, mi.getReturnValue());
            }

            for (int i = 0; i < mi.getActualParameterInstances().size(); i++) {
                carvedTestDataDependencyGraph.addDataDependencyOnActualParameter(mi,
                        mi.getActualParameterInstances().get(i), i);
            }
        });

        return carvedTest;
    }

    private Pair<MethodInvocation, CarvingShadow> generateTheExtractShadow(CarvedTest carvedTest,
            ObjectInstance objectViewToShadow) {
        // Extract objectViewToShadow into an actual shadow
        String shadowType = "ABC"
                + objectViewToShadow.getType().split("\\.")[objectViewToShadow.getType().split("\\.").length - 1]
                + "Shadow" + carvedTest.getUniqueIdentifier();

        ObjectInstance shadowedView = ObjectInstanceFactory
                .get(String.format("%s@%d", shadowType, uniqueIdGenerator.getAndIncrement()));

        CarvingShadow carvingShadow = new CarvingShadow(objectViewToShadow.getType(), shadowType);
        carvedTest.addShadow(carvingShadow);

        // Register all the method invocations in a list and finally inject them into
        // the graphs

        MethodInvocation extractShadow = new MethodInvocation(-1, -1, EXTRACT_SIGNATURE);

        extractShadow.setHasGenericReturnType(true);
        extractShadow.setStatic(true);

        extractShadow.setActualParameterInstances(Arrays.asList(objectViewToShadow));

        extractShadow.setReturnValue(shadowedView);
        return new Pair(extractShadow, carvingShadow);
    }

    /**
     * Generate the mockery for the instanceToMock, then check whether this
     * introduces dangling objects and then iteratively generate the mocks for
     * those.
     * 
     * Note we need to (re)mock only paramters
     * 
     * @param carvedExecution
     * @param instanceToMock
     * @param methodInvocationsToMock
     * @return
     */
    private List<List<MethodInvocation>> generateMockeryRecursively(CarvedExecution carvedExecution,
            CarvedTest carvedTest, ObjectInstance instanceToMock, List<MethodInvocation> methodInvocationsToMock) {

        List<List<MethodInvocation>> fullMockery = new ArrayList<List<MethodInvocation>>();

        List<MethodInvocation> initialMockery = generateMockery(carvedExecution, instanceToMock,
                methodInvocationsToMock);

        // Return the parameters of the doReturn methods only if they are ObjectInstance
        Set<ObjectInstance> potentiallyDanglingObjects = initialMockery.stream()
                .filter(mi -> mi.getMethodSignature().equals(RETURN_SIGNATURE))
                .map(mi -> mi.getActualParameterInstances().get(0)).filter(dn -> dn instanceof ObjectInstance)
                .map(dn -> (ObjectInstance) dn).collect(Collectors.toSet());
        // Check whether any of those potentially dangling objects are inside the carved
        // test already
        potentiallyDanglingObjects.forEach(danglingObject -> {
            if (!carvedTest.getDataDependencyGraph().getObjectInstances().contains(danglingObject)) {
                logger.debug("Found dangling object " + danglingObject);
                // Here find the uses of the dangling object and configure the mock
                carvedExecution.dataDependencyGraphs.forEach(ddg -> {
                    if (ddg.getObjectInstances().contains(danglingObject)) {
                        List<MethodInvocation> usesOfDanglingObject = new ArrayList(
                                ddg.getMethodInvocationsForOwner(danglingObject));
                        Collections.sort(usesOfDanglingObject);
                        // Generate the mockery for this dangling object
                        List<List<MethodInvocation>> derivedMockery = generateMockeryRecursively(carvedExecution,
                                carvedTest, danglingObject, usesOfDanglingObject);
                        // Since the latest generated mockery refers to objects in the past we need to
                        // add it in front of the existing partial mockery

                        // Flatten the list
                        derivedMockery.forEach(dm -> {
                            fullMockery.add(dm);
                        });

                    }
                });

            } else {
                logger.debug("Mock will return the existing object " + danglingObject);
            }
        });

        // The partial mockery is always the last
        fullMockery.add(initialMockery);
        return fullMockery;
    }

    /**
     * Generate a sequence of method invocations (that are NOT yet registered in any
     * graph!) that create a mock object for objectToMock and configure it to mock
     * the methodInvocationsToMock. ObjectInstances and the like are stored inside
     * the method invocation objects
     * 
     * @param objectViewToShadow
     * @param methodInvocationsOnTheObjectViewToShadow
     * @return
     */
    private List<MethodInvocation> generateMockery(CarvedExecution carvedExecution, ObjectInstance instanceToMock,
            List<MethodInvocation> methodInvocationsToMock) {

        logger.debug("Generate mockery for " + instanceToMock + " and " + methodInvocationsToMock.size()
                + " method invocations");

        List<MethodInvocation> theMockery = new ArrayList<MethodInvocation>();

        // create mock for owner
        logger.debug("\t Create the Mock object of " + instanceToMock);
        ObjectInstance classToMock = ObjectInstanceFactory
                .get(instanceToMock.getType() + "@" + uniqueIdGenerator.getAndIncrement());

        DataNode classLiteralToMock = PrimitiveNodeFactory.createClassLiteralFor(instanceToMock);

        // A mock object is created calling the Mockito framework and then configured.
        // At each configuration a new instance is returned
        // so we need to get the very last one and replace all the uses of the dangling
        // objects with that mocked one

        // -1 indicate that this invocation has to be placed yet !
        MethodInvocation initMock = new MethodInvocation(-1, -1, MOCK_SIGNATURE);
        initMock.setHasGenericReturnType(true);
        initMock.setStatic(true);
        initMock.setNecessary(true);
        initMock.setActualParameterInstances(Arrays.asList(classLiteralToMock));
        initMock.setReturnValue((DataNode) instanceToMock);

        theMockery.add(initMock);

        logger.debug("\t Created mock initializer " + initMock);

        // Configure the mock object
        for (int i = 0; i < methodInvocationsToMock.size(); i++) {

            MethodInvocation methodInvocationToMock = methodInvocationsToMock.get(i);

            logger.debug("\t Configure mock for calling " + methodInvocationToMock);

            ObjectInstance returnStubber = returnStubber = ObjectInstanceFactory
                    .get("org.mockito.stubbing.Stubber@" + uniqueIdGenerator.getAndIncrement());

            if (JimpleUtils.hasVoidReturnType(methodInvocationToMock.getMethodSignature())) {
                // Handle mocked methods that return nothing, i.e., void

                MethodInvocation doReturnNothingMock = new MethodInvocation(-1, -1, RETURN_VOID_SIGNATURE);
                doReturnNothingMock.setStatic(true);
                doReturnNothingMock.setNecessary(true);
                doReturnNothingMock.setReturnValue((DataNode) returnStubber);

                logger.debug("\t Adding doReturnNothing " + doReturnNothingMock);
                theMockery.add(doReturnNothingMock);
            } else {
                DataNode returnValue;
                try {
                    returnValue = carvedExecution.getDataDependencyGraphContainingTheMethodInvocationUnderTest()
                            .getReturnValue(methodInvocationToMock).get();
                } catch (ABCException e) {
                    // Should we fail silently?
                    logger.error("Failed to retrieve return value for " + methodInvocationToMock, e);
                    throw new RuntimeException(e);
                }

                DataNode doReturnArgument = null;
                if (returnValue instanceof NullInstance) {
                    doReturnArgument = DataNodeFactory.get(returnValue.getType(), null);
                } else if (returnValue instanceof PrimitiveValue) {
                    doReturnArgument = DataNodeFactory.get(returnValue.getType(),
                            ((PrimitiveValue) returnValue).getStringValue());
                } else {
                    doReturnArgument = DataNodeFactory.get(returnValue.getType(), returnValue.toString());
                }
                // TODO If return value is a NullInstance we should return a Null Instance ?

                MethodInvocation doReturnMock = new MethodInvocation(-1, -1, RETURN_SIGNATURE);
                doReturnMock.setStatic(true);
                doReturnMock.setActualParameterInstances(Arrays.asList(doReturnArgument));
                doReturnMock.setReturnValue((DataNode) returnStubber);
                doReturnMock.setNecessary(true);

                logger.debug("\t Adding doReturn " + doReturnMock);

                theMockery.add(doReturnMock);
            }

            ObjectInstance whenReturn = ObjectInstanceFactory
                    .get(instanceToMock.getType() + "@" + uniqueIdGenerator.getAndIncrement());

            MethodInvocation whenMock = new MethodInvocation(-1, -1, WHEN_SIGNATURE);
            whenMock.setHasGenericReturnType(true);
            whenMock.setOwner(returnStubber);
            whenMock.setActualParameterInstances(Arrays.asList(instanceToMock));
            whenMock.setNecessary(true);
            whenMock.setReturnValue(whenReturn);

            logger.debug("\t Adding when " + whenMock);

            theMockery.add(whenMock);

            // Now we need to replicate the original call on the mocked object to configure
            // it.
            MethodInvocation dependentCallCopy = methodInvocationToMock.clone();

            // TODO Why this code is duplicated? !
            dependentCallCopy.setInvocationTraceId(-1);
            dependentCallCopy.setInvocationCount(-1);
            dependentCallCopy.setOwner(whenReturn);

            dependentCallCopy.setNecessary(true);

            // TODO What about parameters instead, should we create the anew?

            // Override the return value, i.e., return a different object, so we break
            // dependencies
            if (!JimpleUtils.hasVoidReturnType(dependentCallCopy.getMethodSignature())) {
                dependentCallCopy.setReturnValue(ObjectInstanceFactory
                        .get(dependentCallCopy.getReturnValue().getType() + "@" + uniqueIdGenerator.getAndIncrement()));
            }

            logger.debug("\t Adding Actual Mocked Call " + dependentCallCopy);
            theMockery.add(dependentCallCopy);
        }
        return theMockery;
    }

//    // find the minimum method invocation count of all the MethodInvocations
//    // which are owned by the same object as the findViewById calls we have
//    // found, excluding MethodInvocations which are those findViewById calls,
//    // the init method, or onCreate
//    private int deduceMaximumShadowMethodInvocationCount(List<ObjectInstance> methodOwners, CarvedTest carvedTest,
//            CarvedExecution carvedExecution) {
//
//        int invocationCount = carvedTest.getMethodUnderTest().getInvocationCount();
//
//        for (ObjectInstance objectInstance : methodOwners) {
//
//            // should we attempt to get a different callgraph here?
//            Collection<MethodInvocation> ownerMethods = carvedExecution
//                    .getDataDependencyGraphContainingTheMethodInvocationUnderTest()
//                    .getMethodInvocationsForOwner(objectInstance);
//
//            for (MethodInvocation mi : ownerMethods) {
//
//                if (mi.getInvocationCount() < invocationCount && !mi.getMethodSignature().equals(
//                        "<android.app.Activity: android.view.View findViewById(int,java.lang.String,java.lang.String)>")
//                        && !mi.getMethodSignature().contains("void <init>")
//                        && !JimpleUtils.isActivityLifecycle(mi.getMethodSignature())) {
//
//                    logger.debug("Identified new bound: " + mi);
//
//                    invocationCount = mi.getInvocationCount();
//
//                }
//            }
//        }
//
//        return invocationCount;
//    }
}
