package de.unipassau.abc.generation.mocks;

import static java.util.Collections.reverse;
import static java.lang.Integer.max;

import de.unipassau.abc.carving.CarvedExecution;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.DataDependencyGraphImpl;
import de.unipassau.abc.data.DataNode;
import de.unipassau.abc.data.DataNodeFactory;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.ExecutionFlowGraphImpl;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.data.ObjectInstanceFactory;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.data.PrimitiveNodeFactory;
import de.unipassau.abc.data.PrimitiveValue;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.generation.data.CarvedTest;
import de.unipassau.abc.generation.mocks.MockInfo;
import edu.emory.mathcs.backport.java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.UnaryOperator;

public class MockGenerator {

    private static final String MOCK_SIGNATURE = "<org.mockito.Mockito: java.lang.Object mock(java.lang.Class)>";
    private static final String RETURN_SIGNATURE = "<org.mockito.Mockito: org.mockito.stubbing.Stubber doReturn(java.lang.Object)>";
    private static final String WHEN_SIGNATURE = "<org.mockito.stubbing.Stubber: java.lang.Object when(java.lang.Object)>";
    private static final String EXTRACT_SIGNATURE = "<org.robolectric.shadow.api.Shadow: java.lang.Object extract(java.lang.Object)>";
    private static final String SET_SIGNATURE = "<java.lang.Object: void setMockFor(java.lang.String,java.lang.Object)>";

    private AtomicInteger id = new AtomicInteger(-1000000);

    // replace method invocation instead of adding to CarvingMock dependency graph?
    public void generateMocks(CarvedTest carvedTest, CarvedExecution carvedExecution) {

        ExecutionFlowGraph carvedTestExecutionFlowGraph = carvedTest.getExecutionFlowGraph();
        DataDependencyGraph carvedTestDataDependencyGraph = carvedTest.getDataDependencyGraph();
        MethodInvocation methodInvocationUnderTest = carvedTest.getMethodUnderTest();

        List<MockInfo> mockInfoList = new ArrayList<MockInfo>();

        for (ObjectInstance danglingObject : carvedTest.getDataDependencyGraph().getDanglingObjects()) {

            ObjectInstance classToMock = ObjectInstanceFactory.get(danglingObject.getType() + "@" + id.getAndIncrement());
            DataNode classLiteralToMock = PrimitiveNodeFactory.createClassLiteralFor(classToMock);

            MethodInvocation initMock = new MethodInvocation(id.getAndIncrement(), MOCK_SIGNATURE);
            initMock.setHasGenericReturnType(true);
            initMock.setStatic(true);
            initMock.setActualParameterInstances(Arrays.asList(classLiteralToMock));
            initMock.setReturnValue((DataNode) classToMock);
            carvedTestExecutionFlowGraph.enqueueMethodInvocations(initMock);
            carvedTestDataDependencyGraph.addMethodInvocationWithoutAnyDependency(initMock);
            carvedTestDataDependencyGraph.addDataDependencyOnActualParameter(initMock, classLiteralToMock, 0);
            carvedTestDataDependencyGraph.addDataDependencyOnReturn(initMock, (DataNode) classToMock);

            List<MethodInvocation> callSet = new ArrayList<MethodInvocation>();
            // need to use the carved execution here because the methods we care about are not be in the test yet
            for (MethodInvocation dependentCall : carvedExecution.getDataDependencyGraphContainingTheMethodInvocationUnderTest().getMethodInvocationsForOwner(danglingObject)) {

                DataNode returnValue = null;

                try {
                    returnValue = carvedExecution.getDataDependencyGraphContainingTheMethodInvocationUnderTest().getReturnValue(dependentCall).get();
                } catch (ABCException | NoSuchElementException e) {
                    continue;
                }

                DataNode doReturnArgument = DataNodeFactory.get(returnValue.getType(), returnValue.toString());
                ObjectInstance doReturnReturn = ObjectInstanceFactory.get("org.mockito.stubbing.Stubber@" + id.getAndIncrement());

                MethodInvocation doReturnMock = new MethodInvocation(id.getAndIncrement(), RETURN_SIGNATURE);
                doReturnMock.setStatic(true);
                doReturnMock.setActualParameterInstances(Arrays.asList(doReturnArgument));
                doReturnMock.setReturnValue((DataNode) doReturnReturn);

                carvedTestExecutionFlowGraph.enqueueMethodInvocations(doReturnMock);
                carvedTestDataDependencyGraph.addMethodInvocationWithoutAnyDependency(doReturnMock);
                carvedTestDataDependencyGraph.addDataDependencyOnActualParameter(doReturnMock, doReturnArgument, 0);
                carvedTestDataDependencyGraph.addDataDependencyOnReturn(doReturnMock, doReturnReturn);

                ObjectInstance whenReturn = ObjectInstanceFactory.get(danglingObject.getType() + "@" + id.getAndIncrement());

                MethodInvocation whenMock = new MethodInvocation(id.getAndIncrement(), WHEN_SIGNATURE);
                whenMock.setHasGenericReturnType(true);
                whenMock.setOwner(doReturnReturn);
                whenMock.setActualParameterInstances(Arrays.asList(classToMock));
                whenMock.setReturnValue(whenReturn);

                carvedTestExecutionFlowGraph.enqueueMethodInvocations(whenMock);
                carvedTestDataDependencyGraph.addMethodInvocationWithoutAnyDependency(whenMock);
                carvedTestDataDependencyGraph.addDataDependencyOnActualParameter(whenMock, classToMock, 0);
                carvedTestDataDependencyGraph.addDataDependencyOnOwner(whenMock, doReturnReturn);
                carvedTestDataDependencyGraph.addDataDependencyOnReturn(whenMock, whenReturn);

                MethodInvocation dependentCallCopy = dependentCall.clone();
                dependentCallCopy.setOwner(whenReturn);
                dependentCallCopy.setStatic(false);

                carvedTestExecutionFlowGraph.enqueueMethodInvocations(dependentCallCopy);
                carvedTestDataDependencyGraph.addMethodInvocationWithoutAnyDependency(dependentCallCopy);
                carvedTestDataDependencyGraph.addDataDependencyOnOwner(dependentCallCopy, whenReturn);
                //handle calls to mock
                callSet.add(doReturnMock);
                callSet.add(whenMock);
                callSet.add(dependentCallCopy);
            }
            mockInfoList.add(new MockInfo(initMock, callSet));

            // THIS DATA DEPENDENCY GRAPH MAY NEED TO BE THE TEST'S; NEED TO CHANGE METHODS IN TEST DIRECTLY
            for (MethodInvocation dependentCall : carvedTest.getDataDependencyGraph().getMethodInvocationsWhichUse(danglingObject)) {
                List<DataNode> dependentCallParameters = carvedTest.getDataDependencyGraph().getParametersOf(dependentCall);
                UnaryOperator<DataNode> op = arg -> { if (arg.equals(danglingObject)) { return classToMock; } else { return arg; }};
                dependentCallParameters.replaceAll(op);
                // deep copy of the method object
                MethodInvocation newDependentCall = dependentCall.clone();
                // i'm getting a reference to the object???????????
                // newDependentCall.setActualParameterInstances(dependentCallParameters);
                dependentCall.setActualParameterInstances(dependentCallParameters);
                // adding to datadep graph not needed?? (this doesn't work???)
                carvedTestDataDependencyGraph.replaceMethodInvocation(dependentCall, newDependentCall);
            }
        }

        List<Pair<ObjectInstance, List<MethodInvocation>>> targetMockingList = new ArrayList<>();
        List<List<MethodInvocation>> targetMethodInvocations = new ArrayList<>();
        List<ObjectInstance> targetMethodOwners = new ArrayList<>();

        for (MethodInvocation call : carvedTest.getExecutionFlowGraph().getOrderedMethodInvocations()) {
            if (call.getMethodSignature().equals("<android.app.Activity: android.view.View findViewById(int)>")) {
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
                        transitiveMethodInvocations.get(transitiveMethodOwners.indexOf(transitiveCall.getReturnValue())).addAll(dependentCalls);
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

        // correct ordering for list; start at methods which are owned by previously returned objects
        //
        // it is possible for there to exist methods in the list which are at positions earlier
        // (in the original list, that is) than methods which return objects, despite the methods in
        // question not returning objects themselves (ie, representing an endpoint in the object creation
        // tree, in which methods return objects, which have further methods called on them)
        //
        // it not however, possible for a method to be present in the list earlier (again, in the original list)
        // than those methods needed to create the object it is called on (the above traversal operates generationally;
        // to make this more apparent, the list could be further nested by generation, but the semantics above are
        // preserved either way)
        //
        // thus, traversing the list backwards allows us to synthesize mocks in the correct order; as such, the
        // list containing the mocking information is reversed here
        reverse(transitiveMockingList);

        HashMap<ObjectInstance, ObjectInstance> mockMapping = new HashMap<ObjectInstance, ObjectInstance>();

        for (Pair<ObjectInstance, List<MethodInvocation>> mockTarget : transitiveMockingList) {
            // create mock for owner
            ObjectInstance classToMock = ObjectInstanceFactory.get(mockTarget.getFirst().getType() + "@" + id.getAndIncrement());
            DataNode classLiteralToMock = PrimitiveNodeFactory.createClassLiteralFor(classToMock);

            MethodInvocation initMock = new MethodInvocation(id.getAndIncrement(), MOCK_SIGNATURE);
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
                    returnValue = carvedExecution.getDataDependencyGraphContainingTheMethodInvocationUnderTest().getReturnValue(call).get();
                } catch (ABCException | NoSuchElementException e) {
                    continue;
                }

                // mock doReturn first; check whether the call returns an ObjectInstance
                // if so, query the hash table for it

                DataNode doReturnArgument;

                // TODO String are handled as primitive types, this might create some confusion
                if (returnValue instanceof ObjectInstance) {
                    doReturnArgument = mockMapping.get(returnValue);
                } else {
                    /*
                     * Primitive values are singletons, so there's no need to create another node
                     * with their same type and content.
                     */
                    doReturnArgument = returnValue;
                    System.out.println(returnValue);

                }

                ObjectInstance doReturnReturn = ObjectInstanceFactory.get("org.mockito.stubbing.Stubber@" + id.getAndIncrement());

                MethodInvocation doReturnMock = new MethodInvocation(id.getAndIncrement(), RETURN_SIGNATURE);
                doReturnMock.setStatic(true);
                doReturnMock.setActualParameterInstances(Arrays.asList(doReturnArgument));
                doReturnMock.setReturnValue((DataNode) doReturnReturn);

                carvedTestExecutionFlowGraph.enqueueMethodInvocations(doReturnMock);

                carvedTestDataDependencyGraph.addMethodInvocationWithoutAnyDependency(doReturnMock);
                carvedTestDataDependencyGraph.addDataDependencyOnActualParameter(doReturnMock, doReturnArgument, 0);
                carvedTestDataDependencyGraph.addDataDependencyOnReturn(doReturnMock, (DataNode) doReturnReturn);

                ObjectInstance whenReturn = ObjectInstanceFactory.get(mockTarget.getFirst().getType() + "@" + id.getAndIncrement());

                MethodInvocation whenMock = new MethodInvocation(id.getAndIncrement(), WHEN_SIGNATURE);
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

                callCopy.setOwner(whenReturn);
                callCopy.setStatic(false);

                carvedTestExecutionFlowGraph.enqueueMethodInvocations(callCopy);

                carvedTestDataDependencyGraph.addMethodInvocationWithoutAnyDependency(callCopy);
                carvedTestDataDependencyGraph.addDataDependencyOnOwner(callCopy, whenReturn);
                //handle calls to mock
                callSet.add(doReturnMock);
                callSet.add(whenMock);
                callSet.add(callCopy);
            }
            mockInfoList.add(new MockInfo(initMock, callSet));
        }

        //adding dummy dependencies between mocks
        for (int i=0; i<mockInfoList.size()-1; ++i){
            MockInfo currMock = mockInfoList.get(i);
            MockInfo nextMock = mockInfoList.get(i+1);
            for(MethodInvocation methodInvocation:currMock.getCalls()) {
                carvedTestDataDependencyGraph.addDataDependencyOnDummy(methodInvocation, nextMock.getMock());
            }
        }
        for (int i=0; i<mockInfoList.size()-1; ++i){
            MockInfo currMock = mockInfoList.get(i);
            for(MethodInvocation methodInvocation:currMock.getCalls()) {
                carvedTestDataDependencyGraph.addDataDependencyOnDummy(methodInvocation, methodInvocationUnderTest);
            }
        }

        List<MethodInvocation> targetMethodList = new ArrayList<MethodInvocation>();
        targetMethodInvocations.forEach(targetMethodList::addAll);
        List<ShadowInfo> shadowInfoList = new ArrayList<ShadowInfo>();
        for (MethodInvocation targetCall : targetMethodList) {

                DataNode returnValue;

                try {
                    returnValue = carvedExecution.getDataDependencyGraphContainingTheMethodInvocationUnderTest().getReturnValue(targetCall).get();
                } catch (ABCException | NoSuchElementException e) {
                    continue;
                }

                String shadowType = returnValue.getType() + "Shadow";
                ObjectInstance extractReturn = ObjectInstanceFactory.get(String.format("%s@%d", shadowType, id.getAndIncrement()));

                MethodInvocation extractShadow = new MethodInvocation(id.getAndIncrement(), EXTRACT_SIGNATURE);
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

                    DataNode setShadowParameter = new PrimitiveValue(id.getAndIncrement(), "java.lang.String", callTargetCodepoints.toString());

                    MethodInvocation setShadow = new MethodInvocation(id.getAndIncrement(), SET_SIGNATURE);
                    setShadow.setOwner(extractReturn);
                    setShadow.setActualParameterInstances(Arrays.asList(setShadowParameter, mockClassTarget));

                    carvedTestExecutionFlowGraph.enqueueMethodInvocations(setShadow);

                    carvedTestDataDependencyGraph.addMethodInvocationWithoutAnyDependency(setShadow);
                    carvedTestDataDependencyGraph.addDataDependencyOnActualParameter(setShadow, setShadowParameter, 0);
                    carvedTestDataDependencyGraph.addDataDependencyOnActualParameter(setShadow, mockClassTarget, 1);
                    //handle calls to shadow
                    callSet.add(setShadow);
                }
                shadowInfoList.add(new ShadowInfo(extractShadow, callSet));
        }

        //adding dummy dependencies between shadows
        for (int i=0; i<shadowInfoList.size()-1; ++i){
            ShadowInfo currShadow = shadowInfoList.get(i);
            ShadowInfo nextShadow = shadowInfoList.get(i+1);
            for(MethodInvocation methodInvocation:currShadow.getCalls()) {
                carvedTestDataDependencyGraph.addDataDependencyOnDummy(methodInvocation, nextShadow.getShadow());
            }
        }
        //add depedency to carved method
        for (int i=0; i<shadowInfoList.size()-1; ++i){
            ShadowInfo currShadow = shadowInfoList.get(i);
            for(MethodInvocation methodInvocation:currShadow.getCalls()) {
                carvedTestDataDependencyGraph.addDataDependencyOnDummy(methodInvocation, methodInvocationUnderTest);
            }
        }
    }
}
