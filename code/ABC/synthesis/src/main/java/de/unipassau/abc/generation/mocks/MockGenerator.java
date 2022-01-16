package de.unipassau.abc.generation.mocks;

import static java.util.Collections.reverse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.apache.tools.ant.taskdefs.Concat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.carving.BasicCarver;
import de.unipassau.abc.carving.CarvedExecution;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.DataNode;
import de.unipassau.abc.data.DataNodeFactory;
import de.unipassau.abc.data.EnumConstant;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.ExecutionFlowGraphImpl;
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

    private final static Logger logger = LoggerFactory.getLogger(MockGenerator.class);

    /**
     * Generate mocks is an interative process. We start from the carvedTest and
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
        // Re-Carve if (TODO) new non dangling objects appeared !
//        BasicCarver carver = new BasicCarver(carvedExecution);
//        try {
//            carver.recarve(carvedExecution.methodInvocationUnderTest);
//        } catch (ABCException e) {
//            throw new RuntimeException(e);
//        }
        
        // Will this ever terminate?
        while (repeatMocking) {
            // Make sure we make room for the next cycle of deps
            uniqueIdGenerator.addAndGet(-1000);
            logicaTimeStampGenerator.addAndGet(-1000);
            repeatMocking = generateMocksForVisibleCalls(carvedTest, carvedExecution);
            //
//            carver = new BasicCarver(carvedExecution);
//            try {
//                carver.recarve(carvedExecution.methodInvocationUnderTest);
//            } catch (ABCException e) {
//                throw new RuntimeException(e);
//            }
        }
        // Does this require some sort of iteration?
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
                                    
                                    ).collect(Collectors.toList())
                    );

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
                    logger.error("There are is call graph that contains " + mi);
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
                    logger.info("Found usage of super " + callsToSuper + " : " + mi);
                } else {
                    callsThatRequireTheDanglingObject.add(mi);
                }

            }

            

            
            
            
            logger.info("Found that dangling object " + danglingObject + " is invoked "
                    + callsThatRequireTheDanglingObject.size() + " times");

            // TODO Do we need to add a patch for the containers like EnumSet?
            if (JimpleUtils.isArray(danglingObject.getType())) {
                // Instead of mocking the array we need to create one with the expected size and
                // filled with the content in the expected place
                logger.info("Mock array " + danglingObject);

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
                    logger.info("Mock initializer will return the dangling object ");
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

                logger.info("Created mock initializer " + initMock);

                // Configure the mock object
                List<MethodInvocation> callSet = new ArrayList<MethodInvocation>();
                // need to use the carved execution here because the methods we care about are
                // not be in the test yet
                logger.info("Configure the mock objects");

                for (int i = 0; i < callsThatRequireTheDanglingObject.size(); i++) {

                    MethodInvocation dependentCall = callsThatRequireTheDanglingObject.get(i);

                    logger.info("* Configure mock for calling " + dependentCall);

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

                        logger.info("\t Adding doReturn " + doReturnNothingMock);
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

                        logger.info("\t Adding doReturn " + doReturnMock);
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

                    // If this is the last call to mock we need to make it return the dangling
                    // object, not a new instance
                    ObjectInstance whenReturn = null;
                    if (i == callsThatRequireTheDanglingObject.size() - 1) {
                        logger.info("\t Link the mock to the dangling object");
                        whenReturn = danglingObject;
                    } else {
                        logger.info("\t Create a temporary mock object");
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

                    logger.info("\t Adding When " + whenMock);

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

                    logger.info("\t Adding Mocked Call " + dependentCallCopy);

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

    public CarvedTest generateShadows(CarvedTest carvedTest, CarvedExecution carvedExecution) {
        logger.info("-------------------------");
        logger.info("  SHADOWING " + carvedTest.getMethodUnderTest());
        logger.info("-------------------------");

        ExecutionFlowGraph carvedTestExecutionFlowGraph = carvedTest.getExecutionFlowGraph();
        DataDependencyGraph carvedTestDataDependencyGraph = carvedTest.getDataDependencyGraph();

        MethodInvocation methodInvocationUnderTest = carvedTest.getMethodUnderTest();

        List<MockInfo> mockInfoList = new ArrayList<MockInfo>();

        // Look up whether we need to create a shadow by looking at how findViewById
        // methods are called in the test

        List<MethodInvocation> candidateMethodInvocations = carvedTest.getExecutionFlowGraph()
                .getOrderedMethodInvocations();

        List<MethodInvocation> subsumedCandidateMethodInvocations = new ArrayList<MethodInvocation>();

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
        if (!JimpleUtils.isActivityLifecycle(carvedTest.getMethodUnderTest().getMethodSignature())) {

            for (MethodInvocation candidateMethodInvocation : candidateMethodInvocations) {

                if (candidateMethodInvocation.isSynthetic()) {
                    continue;
                }

                // Skip mocked and other generated method calls
                if (carvedExecution.getCallGraphContainingTheMethodInvocation(candidateMethodInvocation) == null) {
                    continue;
                }

                try {
                    subsumedCandidateMethodInvocations
                            .addAll(carvedExecution.getCallGraphContainingTheMethodInvocation(candidateMethodInvocation)
                                    .getMethodInvocationsSubsumedBy(candidateMethodInvocation));
                } catch (NullPointerException e) {
                    logger.error("NULL!");
                    throw e;
                }
            }
        }

        // Make sure we do not consider activity (and probably fragments and similar
        // here?)
        for (MethodInvocation subsumedCandidate : subsumedCandidateMethodInvocations) {
            if (subsumedCandidate.getMethodSignature().equals(
                    "<android.app.Activity: android.view.View findViewById(int,java.lang.String,java.lang.String)>")) {
                logger.info(" Found " + subsumedCandidate + " called on " + subsumedCandidate.getOwner());
                carvedTestExecutionFlowGraph.enqueueMethodInvocations(subsumedCandidate);
                carvedTestDataDependencyGraph.addMethodInvocationWithoutAnyDependency(subsumedCandidate);
            }
        }

        // TODO What to do with the calls INSIDE the method under test?

        // TODO Pay attention to duplicate calls !
        candidateMethodInvocations.addAll(subsumedCandidateMethodInvocations);

        List<List<MethodInvocation>> targetMethodInvocations = new ArrayList<>();
        List<ObjectInstance> targetMethodOwners = new ArrayList<>();

        int subsumedCallCount = 0;

        for (MethodInvocation call : candidateMethodInvocations) {

            if (call.getMethodSignature().equals(
                    "<android.app.Activity: android.view.View findViewById(int,java.lang.String,java.lang.String)>")) {

                if (subsumedCandidateMethodInvocations.contains(call)) {

                    subsumedCallCount += 1;
                }

                call.setHasGenericReturnType(true);

                if (targetMethodOwners.contains(call.getOwner())) {

                    targetMethodInvocations.get(targetMethodOwners.indexOf(call.getOwner())).add(call);

                } else {

                    targetMethodOwners.add(call.getOwner());

                    targetMethodInvocations.add(new ArrayList<>());

                    targetMethodInvocations.get(targetMethodInvocations.size() - 1).add(call);
                }
            }
        }

        List<MethodInvocation> transitiveMethodTraversalList = new ArrayList<>();
        targetMethodInvocations.forEach(transitiveMethodTraversalList::addAll);

        List<Pair<ObjectInstance, List<MethodInvocation>>> transitiveMockingList = new ArrayList<>();
        List<List<MethodInvocation>> transitiveMethodInvocations = new ArrayList<>();
        List<ObjectInstance> transitiveMethodOwners = new ArrayList<>();

        while (true) {

            List<MethodInvocation> nextList = new ArrayList<>();

            for (MethodInvocation transitiveCall : transitiveMethodTraversalList) {
                if (transitiveCall.getReturnValue() instanceof ObjectInstance) {
                    Collection<? extends MethodInvocation> dependentCalls = carvedExecution
                            .getDataDependencyGraphContainingTheMethodInvocationUnderTest()
                            .getMethodInvocationsForOwner((ObjectInstance) transitiveCall.getReturnValue());

                    if (transitiveMethodOwners.contains(transitiveCall.getReturnValue())) {
                        transitiveMethodInvocations.get(transitiveMethodOwners.indexOf(transitiveCall.getReturnValue()))
                                .addAll(dependentCalls);
                    } else {
                        transitiveMethodOwners.add((ObjectInstance) transitiveCall.getReturnValue());
                        transitiveMethodInvocations.add(new ArrayList<>());
                        transitiveMethodInvocations.get(transitiveMethodInvocations.size() - 1).addAll(dependentCalls);
                    }

                    nextList.addAll(dependentCalls);
                }
            }

            if (nextList.size() == 0) {
                break;
            } else {
                transitiveMethodTraversalList = nextList;
            }
        }

        // combine individual lists into final mock target list
        for (int i = 0; i < transitiveMethodOwners.size(); i++) {
            transitiveMockingList.add(new Pair<>(transitiveMethodOwners.get(i), transitiveMethodInvocations.get(i)));
        }

        // correct ordering for list; start at methods which are owned by previously
        // returned objects
        //
        // it is possible for there to exist methods in the list which are at positions
        // earlier
        // (in the original list, that is) than methods which return objects, despite
        // the methods in
        // question not returning objects themselves (ie, representing an endpoint in
        // the object creation
        // tree, in which methods return objects, which have further methods called on
        // them)
        //
        // it not however, possible for a method to be present in the list earlier
        // (again, in the original list)
        // than those methods needed to create the object it is called on (the above
        // traversal operates generationally;
        // to make this more apparent, the list could be further nested by generation,
        // but the semantics above are
        // preserved either way)
        //
        // thus, traversing the list backwards allows us to synthesize mocks in the
        // correct order; as such, the
        // list containing the mocking information is reversed here
        reverse(transitiveMockingList);

        HashMap<ObjectInstance, ObjectInstance> mockMapping = new HashMap<ObjectInstance, ObjectInstance>();

        HashMap<ObjectInstance, DataNode> stringMapping = new HashMap<ObjectInstance, DataNode>();

        for (Pair<ObjectInstance, List<MethodInvocation>> mockTarget : transitiveMockingList) {

            if (mockTarget.getFirst().getType().equals("java.lang.String")) {
                // this always should be a singleton list(?) containing toString()
                MethodInvocation toStringCall = mockTarget.getSecond().get(0);

                DataNode returnValue;

                try {
                    returnValue = carvedExecution.getDataDependencyGraphContainingTheMethodInvocationUnderTest()
                            .getReturnValue(toStringCall).get();
                } catch (ABCException | NoSuchElementException e) {
                    continue;
                }

                stringMapping.put(mockTarget.getFirst(), returnValue);

//                System.out.println(stringMapping);

                continue;
            }

            // create mock for owner
            ObjectInstance classToMock = ObjectInstanceFactory
                    .get(mockTarget.getFirst().getType() + "@" + uniqueIdGenerator.getAndIncrement());
            DataNode classLiteralToMock = PrimitiveNodeFactory.createClassLiteralFor(classToMock);

            MethodInvocation initMock = new MethodInvocation(logicaTimeStampGenerator.getAndIncrement(),
//                    MethodInvocation.INVOCATION_TRACE_ID_NA_CONSTANT,
                    uniqueIdGenerator.getAndIncrement(), MOCK_SIGNATURE);
            initMock.setHasGenericReturnType(true);
            initMock.setStatic(true);
            initMock.setActualParameterInstances(Collections.singletonList(classLiteralToMock));
            initMock.setReturnValue(classToMock);

            carvedTestExecutionFlowGraph.enqueueMethodInvocations(initMock);

            carvedTestDataDependencyGraph.addMethodInvocationWithoutAnyDependency(initMock);
            carvedTestDataDependencyGraph.addDataDependencyOnActualParameter(initMock, classLiteralToMock, 0);
            carvedTestDataDependencyGraph.addDataDependencyOnReturn(initMock, classToMock);

            mockMapping.put(mockTarget.getFirst(), classToMock);

            List<MethodInvocation> callSet = new ArrayList<MethodInvocation>();
            for (MethodInvocation call : mockTarget.getSecond()) {

                // mock all methods on above object

                DataNode returnValue;

                try {
                    returnValue = carvedExecution.getDataDependencyGraphContainingTheMethodInvocationUnderTest()
                            .getReturnValue(call).get();
                } catch (ABCException | NoSuchElementException e) {
                    continue;
                }

                // mock doReturn first; check whether the call returns an ObjectInstance
                // if so, query the hash table for it

                DataNode doReturnArgument;

                // TODO String are handled as primitive types, this might create some confusion
                if (returnValue.getType().equals("java.lang.String") && !(returnValue instanceof PrimitiveValue)) {
                    doReturnArgument = stringMapping.get(returnValue);
                } else if (returnValue instanceof ObjectInstance) {
                    doReturnArgument = mockMapping.get(returnValue);
                } else {
                    /*
                     * Primitive values are singletons, so there's no need to create another node
                     * with their same type and content.
                     */
                    doReturnArgument = returnValue;
                    logger.info("" + returnValue);

                }

                ObjectInstance doReturnReturn = ObjectInstanceFactory
                        .get("org.mockito.stubbing.Stubber@" + uniqueIdGenerator.getAndIncrement());

                MethodInvocation doReturnMock = new MethodInvocation(logicaTimeStampGenerator.getAndIncrement(),
//                        MethodInvocation.INVOCATION_TRACE_ID_NA_CONSTANT,
                        uniqueIdGenerator.getAndIncrement(), RETURN_SIGNATURE);
                doReturnMock.setStatic(true);
                doReturnMock.setActualParameterInstances(Arrays.asList(doReturnArgument));
                doReturnMock.setReturnValue((DataNode) doReturnReturn);

                carvedTestExecutionFlowGraph.enqueueMethodInvocations(doReturnMock);

                carvedTestDataDependencyGraph.addMethodInvocationWithoutAnyDependency(doReturnMock);
                carvedTestDataDependencyGraph.addDataDependencyOnActualParameter(doReturnMock, doReturnArgument, 0);
                carvedTestDataDependencyGraph.addDataDependencyOnReturn(doReturnMock, (DataNode) doReturnReturn);

                ObjectInstance whenReturn = ObjectInstanceFactory
                        .get(mockTarget.getFirst().getType() + "@" + uniqueIdGenerator.getAndIncrement());

                MethodInvocation whenMock = new MethodInvocation(logicaTimeStampGenerator.getAndIncrement(),
//                        MethodInvocation.INVOCATION_TRACE_ID_NA_CONSTANT,
                        uniqueIdGenerator.getAndIncrement(), WHEN_SIGNATURE);
                whenMock.setHasGenericReturnType(true);
                whenMock.setOwner(doReturnReturn);
                whenMock.setActualParameterInstances(Arrays.asList(classToMock));
                whenMock.setReturnValue(whenReturn);

                carvedTestExecutionFlowGraph.enqueueMethodInvocations(whenMock);

                carvedTestDataDependencyGraph.addMethodInvocationWithoutAnyDependency(whenMock);
                carvedTestDataDependencyGraph.addDataDependencyOnActualParameter(whenMock, classToMock, 0);
                carvedTestDataDependencyGraph.addDataDependencyOnOwner(whenMock, doReturnReturn);
                carvedTestDataDependencyGraph.addDataDependencyOnReturn(whenMock, whenReturn);

                MethodInvocation callCopy = call.clone();

                // Be sure we place this in the right place
                callCopy.setInvocationTraceId(logicaTimeStampGenerator.getAndIncrement());
                callCopy.setInvocationCount(uniqueIdGenerator.getAndIncrement());
                callCopy.setOwner(whenReturn);
                callCopy.setStatic(false);

                carvedTestExecutionFlowGraph.enqueueMethodInvocations(callCopy);

                carvedTestDataDependencyGraph.addMethodInvocationWithoutAnyDependency(callCopy);
                carvedTestDataDependencyGraph.addDataDependencyOnOwner(callCopy, whenReturn);
                // handle calls to mock
                callSet.add(doReturnMock);
                callSet.add(whenMock);
                callSet.add(callCopy);
            }
            mockInfoList.add(new MockInfo(initMock, callSet));
        }

        // adding dummy dependencies between mocks
        for (int i = 0; i < mockInfoList.size() - 1; ++i) {
            MockInfo currMock = mockInfoList.get(i);
            MockInfo nextMock = mockInfoList.get(i + 1);
            for (MethodInvocation methodInvocation : currMock.getCalls()) {
                carvedTestDataDependencyGraph.addDataDependencyOnDummy(methodInvocation, nextMock.getMock());
            }
        }
        for (int i = 0; i < mockInfoList.size() - 1; ++i) {
            MockInfo currMock = mockInfoList.get(i);
            for (MethodInvocation methodInvocation : currMock.getCalls()) {
                carvedTestDataDependencyGraph.addDataDependencyOnDummy(methodInvocation, methodInvocationUnderTest);
            }
        }

        List<MethodInvocation> targetMethodList = new ArrayList<MethodInvocation>();
        targetMethodInvocations.forEach(targetMethodList::addAll);

        List<ShadowInfo> shadowInfoList = new ArrayList<ShadowInfo>();

        List<ShadowSequence> shadowSequenceList = new ArrayList<ShadowSequence>();

        for (MethodInvocation targetCall : targetMethodList) {

            // need no special string/ObjectInstance handling here, because we must always
            // directly use the value findViewById() returns
            DataNode returnValue;

            try {
                returnValue = carvedExecution.getDataDependencyGraphContainingTheMethodInvocationUnderTest()
                        .getReturnValue(targetCall).get();
            } catch (ABCException | NoSuchElementException e) {
                continue;
            }

            String shadowType = "ABC"
                    + returnValue.getType().split("\\.")[returnValue.getType().split("\\.").length - 1] + "Shadow"
                    + carvedTest.getUniqueIdentifier();
            ObjectInstance extractReturn = ObjectInstanceFactory
                    .get(String.format("%s@%d", shadowType, uniqueIdGenerator.getAndIncrement()));
            CarvingShadow carvingShadow = new CarvingShadow(returnValue.getType(), shadowType);
            carvedTest.addShadow(carvingShadow);

            MethodInvocation extractShadow = new MethodInvocation(logicaTimeStampGenerator.getAndIncrement(),
//                    MethodInvocation.INVOCATION_TRACE_ID_NA_CONSTANT,
                    uniqueIdGenerator.getAndIncrement(), EXTRACT_SIGNATURE);

            extractShadow.setHasGenericReturnType(true);
            extractShadow.setStatic(true);
            extractShadow.setActualParameterInstances(Arrays.asList(returnValue));
            extractShadow.setReturnValue(extractReturn);

            carvedTestExecutionFlowGraph.enqueueMethodInvocations(extractShadow);

            carvedTestDataDependencyGraph.addMethodInvocationWithoutAnyDependency(extractShadow);
            carvedTestDataDependencyGraph.addDataDependencyOnActualParameter(extractShadow, returnValue, 0);
            carvedTestDataDependencyGraph.addDataDependencyOnReturn(extractShadow, extractReturn);

            ObjectInstance mockClassTarget = mockMapping.get(returnValue);
            List<MethodInvocation> mockMethodTargetList = new ArrayList<>();

            for (Pair<ObjectInstance, List<MethodInvocation>> callPair : transitiveMockingList) {
                if (callPair.getFirst().equals(returnValue)) {
                    mockMethodTargetList = callPair.getSecond();
                }
            }

            List<MethodInvocation> callSet = new ArrayList<MethodInvocation>();
            for (MethodInvocation mockCallTarget : mockMethodTargetList) {

                List<Integer> callTargetCodepoints = new ArrayList<Integer>();

                mockCallTarget.getMethodSignature().codePoints().forEach(callTargetCodepoints::add);

                DataNode setShadowParameter = new PrimitiveValue(uniqueIdGenerator.getAndIncrement(),
                        "java.lang.String", callTargetCodepoints.toString());

                MethodInvocation setShadow = new MethodInvocation(logicaTimeStampGenerator.getAndIncrement(),
//                        MethodInvocation.INVOCATION_TRACE_ID_NA_CONSTANT,
                        uniqueIdGenerator.getAndIncrement(), SET_SIGNATURE);
                setShadow.setOwner(extractReturn);
                setShadow.setActualParameterInstances(Arrays.asList(setShadowParameter, mockClassTarget));

                carvedTestExecutionFlowGraph.enqueueMethodInvocations(setShadow);

                carvedTestDataDependencyGraph.addMethodInvocationWithoutAnyDependency(setShadow);
                carvedTestDataDependencyGraph.addDataDependencyOnActualParameter(setShadow, setShadowParameter, 0);
                carvedTestDataDependencyGraph.addDataDependencyOnActualParameter(setShadow, mockClassTarget, 1);
                // handle calls to shadow
                callSet.add(setShadow);

                MethodInvocation strictShadow = new MethodInvocation(logicaTimeStampGenerator.getAndIncrement(),
//                        MethodInvocation.INVOCATION_TRACE_ID_NA_CONSTANT,
                        uniqueIdGenerator.getAndIncrement(), STRICT_SIGNATURE);
                strictShadow.setOwner(extractReturn);
                strictShadow.setActualParameterInstances(Arrays.asList());

                carvedTestExecutionFlowGraph.enqueueMethodInvocations(strictShadow);

                carvedTestDataDependencyGraph.addMethodInvocationWithoutAnyDependency(strictShadow);
                // handle calls to shadow
                callSet.add(strictShadow);

                if (setShadowParameter instanceof PrimitiveValue) {
                    PrimitiveValue pv = (PrimitiveValue) setShadowParameter;
                    carvingShadow.getStubbedMethods().add(pv.toString().replaceAll("\"", ""));
                }
            }
            shadowInfoList.add(new ShadowInfo(extractShadow, callSet));
            shadowSequenceList.add(new ShadowSequence(targetCall, extractShadow, callSet));
        }

        // adding dummy dependencies between shadows
        for (int i = 0; i < shadowInfoList.size() - 1; ++i) {
            ShadowInfo currShadow = shadowInfoList.get(i);
            ShadowInfo nextShadow = shadowInfoList.get(i + 1);
            for (MethodInvocation methodInvocation : currShadow.getCalls()) {
                carvedTestDataDependencyGraph.addDataDependencyOnDummy(methodInvocation, nextShadow.getShadow());
            }
        }
        // add depedency to carved method
        for (int i = 0; i < shadowInfoList.size() - 1; ++i) {
            ShadowInfo currShadow = shadowInfoList.get(i);
            for (MethodInvocation methodInvocation : currShadow.getCalls()) {
                carvedTestDataDependencyGraph.addDataDependencyOnDummy(methodInvocation, methodInvocationUnderTest);
            }
        }

        List<MethodInvocation> orderedMethodInvocations = carvedTestExecutionFlowGraph.getMethodInvocationsSortedByID();

        int maxInvocationCount = deduceMaximumShadowMethodInvocationCount(targetMethodOwners, carvedTest,
                carvedExecution);
        int incrementalInvocationCount = 0;

        for (MethodInvocation targetCandidate : orderedMethodInvocations) {
            if (targetCandidate.getInvocationCount() >= maxInvocationCount) {
                if (subsumedCandidateMethodInvocations.contains(targetCandidate)) {
                    targetCandidate.setInvocationCount(maxInvocationCount + incrementalInvocationCount);
                    incrementalInvocationCount += 1;
                } else {
                    targetCandidate.setInvocationCount(targetCandidate.getInvocationCount() + subsumedCallCount);
                }
            }
        }

        for (ShadowSequence shadowSequence : shadowSequenceList) {
            for (MethodInvocation orderedMethodInvocationInstance : orderedMethodInvocations) {
                if (orderedMethodInvocationInstance.getInvocationCount() > shadowSequence.getTargetCallInstance()
                        .getInvocationCount()) {
                    orderedMethodInvocationInstance
                            .setInvocationCount(orderedMethodInvocationInstance.getInvocationCount()
                                    + shadowSequence.getSetMockCallInstances().size() + 1);
                }
            }

            orderedMethodInvocations.get(orderedMethodInvocations.indexOf(shadowSequence.getExtractCallInstance()))
                    .setInvocationCount(shadowSequence.getTargetCallInstance().getInvocationCount() + 1);

            for (int idIncr = 2; idIncr <= shadowSequence.getSetMockCallInstances().size() + 1; idIncr++) {
                orderedMethodInvocations
                        .get(orderedMethodInvocations.indexOf(shadowSequence.getSetMockCallInstances().get(idIncr - 2)))
                        .setInvocationCount(shadowSequence.getTargetCallInstance().getInvocationCount() + idIncr);
            }
        }

        ExecutionFlowGraph reorderedExecutionFlowGraph = new ExecutionFlowGraphImpl();

        for (MethodInvocation orderedMethodInvocationInstance : orderedMethodInvocations) {
            reorderedExecutionFlowGraph.enqueueMethodInvocations(orderedMethodInvocationInstance);
        }

        carvedTest.setExecutionFlowGraph(reorderedExecutionFlowGraph);

//        System.out.println(orderedMethodInvocations);
        return carvedTest;
    }

    // find the minimum method invocation count of all the MethodInvocations
    // which are owned by the same object as the findViewById calls we have
    // found, excluding MethodInvocations which are those findViewById calls,
    // the init method, or onCreate
    private int deduceMaximumShadowMethodInvocationCount(List<ObjectInstance> methodOwners, CarvedTest carvedTest,
            CarvedExecution carvedExecution) {

        int invocationCount = carvedTest.getMethodUnderTest().getInvocationCount();

        for (ObjectInstance objectInstance : methodOwners) {

            // should we attempt to get a different callgraph here?
            Collection<MethodInvocation> ownerMethods = carvedExecution
                    .getDataDependencyGraphContainingTheMethodInvocationUnderTest()
                    .getMethodInvocationsForOwner(objectInstance);

            for (MethodInvocation mi : ownerMethods) {

                if (mi.getInvocationCount() < invocationCount && !mi.getMethodSignature().equals(
                        "<android.app.Activity: android.view.View findViewById(int,java.lang.String,java.lang.String)>")
                        && !mi.getMethodSignature().contains("void <init>")
                        && !JimpleUtils.isActivityLifecycle(mi.getMethodSignature())) {

                    logger.info("Identified new bound: " + mi);

                    invocationCount = mi.getInvocationCount();

                }
            }
        }

        return invocationCount;
    }
}
