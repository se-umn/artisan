package de.unipassau.abc.generation.mocks;

import static java.util.Collections.reverse;

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
import edu.emory.mathcs.backport.java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.UnaryOperator;

public class MockGenerator {

    private static final String MOCK_SIGNATURE = "<org.mockito.Mockito: java.lang.Object mock(java.lang.Class)>";
    private static final String RETURN_SIGNATURE = "<org.mockito.Mockito: org.mockito.stubbing.Stubber doReturn(java.lang.Object)>";
    private static final String WHEN_SIGNATURE = "<org.mockito.stubbing.Stubber: java.lang.Object when(java.lang.Object)>";
    private static final String EXTRACT_SIGNATURE = "<org.robolectric.shadow.api.Shadow: java.lang.Object extract(java.lang.Object)>";
    private static final String SET_SIGNATURE = "<java.lang.Object: void setMockFor(java.lang.String,java.lang.Object)>";

    private AtomicInteger id = new AtomicInteger(-100);

    // replace method invocation instead of adding to CarvingMock dependency graph?
    public Triplette<CarvingMock, CarvingShadow, CarvedTest> generateMocks(CarvedTest carvedTest, CarvedExecution carvedExecution) {

        CarvingMock carvingMock = new CarvingMock();
        CarvingShadow carvingShadow = new CarvingShadow();

        DataDependencyGraph carvedDataDependencyGraph = carvedTest.getDataDependencyGraph();

        ExecutionFlowGraph mockExecutionFlowGraph = new ExecutionFlowGraphImpl();
        DataDependencyGraph mockDataDependencyGraph = new DataDependencyGraphImpl(); 

        ExecutionFlowGraph shadowExecutionFlowGraph = new ExecutionFlowGraphImpl();
        DataDependencyGraph shadowDataDependencyGraph = new DataDependencyGraphImpl();

        for (ObjectInstance danglingObject : carvedTest.getDataDependencyGraph().getDanglingObjects()) {

            ObjectInstance classToMock = ObjectInstanceFactory.get(danglingObject.getType() + "@" + id.getAndIncrement());
            DataNode classLiteralToMock = PrimitiveNodeFactory.createClassLiteralFor(classToMock);

            MethodInvocation initMock = new MethodInvocation(id.getAndIncrement(), MOCK_SIGNATURE);
            initMock.setHasGenericReturnType(true);
            initMock.setStatic(true);
            initMock.setActualParameterInstances(Arrays.asList(classLiteralToMock));
            initMock.setReturnValue((DataNode) classToMock);

            mockExecutionFlowGraph.enqueueMethodInvocations(initMock);

            mockDataDependencyGraph.addMethodInvocationWithoutAnyDependency(initMock);
            mockDataDependencyGraph.addDataDependencyOnActualParameter(initMock, classLiteralToMock, 0);
            mockDataDependencyGraph.addDataDependencyOnReturn(initMock, (DataNode) classToMock);

            // need to use the carvedexecution here because the methods we care about are not be in the test yet
            for (MethodInvocation dependentCall : carvedExecution.getDataDependencyGraphContainingTheMethodInvocationUnderTest().getMethodInvocationsForOwner(danglingObject)) {

                DataNode returnValue;

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

                mockExecutionFlowGraph.enqueueMethodInvocations(doReturnMock);

                mockDataDependencyGraph.addMethodInvocationWithoutAnyDependency(doReturnMock);
                mockDataDependencyGraph.addDataDependencyOnActualParameter(doReturnMock, doReturnArgument, 0);
                mockDataDependencyGraph.addDataDependencyOnReturn(doReturnMock, doReturnReturn);

                ObjectInstance whenReturn = ObjectInstanceFactory.get(danglingObject.getType() + "@" + id.getAndIncrement());

                MethodInvocation whenMock = new MethodInvocation(id.getAndIncrement(), WHEN_SIGNATURE);
                whenMock.setHasGenericReturnType(true);
                whenMock.setOwner(doReturnReturn);
                whenMock.setActualParameterInstances(Arrays.asList(classToMock));
                whenMock.setReturnValue(whenReturn);

                mockExecutionFlowGraph.enqueueMethodInvocations(whenMock);

                mockDataDependencyGraph.addMethodInvocationWithoutAnyDependency(whenMock);
                mockDataDependencyGraph.addDataDependencyOnActualParameter(whenMock, classToMock, 0);
                mockDataDependencyGraph.addDataDependencyOnOwner(whenMock, doReturnReturn);
                mockDataDependencyGraph.addDataDependencyOnReturn(whenMock, whenReturn);

                MethodInvocation dependentCallCopy = dependentCall.clone();

                dependentCallCopy.setOwner(whenReturn);
                dependentCallCopy.setStatic(false);

                mockExecutionFlowGraph.enqueueMethodInvocations(dependentCallCopy);

                mockDataDependencyGraph.addMethodInvocationWithoutAnyDependency(dependentCallCopy);
                mockDataDependencyGraph.addDataDependencyOnOwner(dependentCallCopy, whenReturn);
            }

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
                carvedDataDependencyGraph.replaceMethodInvocation(dependentCall, newDependentCall);
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

            mockExecutionFlowGraph.enqueueMethodInvocations(initMock);

            mockDataDependencyGraph.addMethodInvocationWithoutAnyDependency(initMock);
            mockDataDependencyGraph.addDataDependencyOnActualParameter(initMock, classLiteralToMock, 0);
            mockDataDependencyGraph.addDataDependencyOnReturn(initMock, classToMock);

            mockMapping.put(mockTarget.getFirst(), classToMock);

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

                mockExecutionFlowGraph.enqueueMethodInvocations(doReturnMock);

                mockDataDependencyGraph.addMethodInvocationWithoutAnyDependency(doReturnMock);
                mockDataDependencyGraph.addDataDependencyOnActualParameter(doReturnMock, doReturnArgument, 0);
                mockDataDependencyGraph.addDataDependencyOnReturn(doReturnMock, (DataNode) doReturnReturn);

                ObjectInstance whenReturn = ObjectInstanceFactory.get(mockTarget.getFirst().getType() + "@" + id.getAndIncrement());

                MethodInvocation whenMock = new MethodInvocation(id.getAndIncrement(), WHEN_SIGNATURE);
                whenMock.setHasGenericReturnType(true);
                whenMock.setOwner(doReturnReturn);
                whenMock.setActualParameterInstances(Arrays.asList(classToMock));
                whenMock.setReturnValue(whenReturn);

                mockExecutionFlowGraph.enqueueMethodInvocations(whenMock);

                mockDataDependencyGraph.addMethodInvocationWithoutAnyDependency(whenMock);
                mockDataDependencyGraph.addDataDependencyOnActualParameter(whenMock, classToMock, 0);
                mockDataDependencyGraph.addDataDependencyOnOwner(whenMock, doReturnReturn);
                mockDataDependencyGraph.addDataDependencyOnReturn(whenMock, whenReturn);

                MethodInvocation callCopy = call.clone();

                callCopy.setOwner(whenReturn);
                callCopy.setStatic(false);

                mockExecutionFlowGraph.enqueueMethodInvocations(callCopy);

                mockDataDependencyGraph.addMethodInvocationWithoutAnyDependency(callCopy);
                mockDataDependencyGraph.addDataDependencyOnOwner(callCopy, whenReturn);
            }
        }

        List<MethodInvocation> targetMethodList = new ArrayList<MethodInvocation>();
        targetMethodInvocations.forEach(targetMethodList::addAll);

        for (MethodInvocation targetCall : targetMethodList) {

                DataNode returnValue;

                try {
                    returnValue = carvedExecution.getDataDependencyGraphContainingTheMethodInvocationUnderTest().getReturnValue(targetCall).get();
                } catch (ABCException | NoSuchElementException e) {
                    continue;
                }

                String shadowType = returnValue.getType() + "Shadow";
                ObjectInstance extractReturn = ObjectInstanceFactory.get(String.format("%s@%d", shadowType, id.getAndIncrement()));
                carvingShadow.types.add(shadowType);
                MethodInvocation extractShadow = new MethodInvocation(id.getAndIncrement(), EXTRACT_SIGNATURE);
                extractShadow.setHasGenericReturnType(true);
                extractShadow.setStatic(true);
                extractShadow.setActualParameterInstances(Arrays.asList(returnValue));
                extractShadow.setReturnValue(extractReturn);

                shadowExecutionFlowGraph.enqueueMethodInvocations(extractShadow);

                shadowDataDependencyGraph.addMethodInvocationWithoutAnyDependency(extractShadow);
                shadowDataDependencyGraph.addDataDependencyOnActualParameter(extractShadow, returnValue, 0);
                shadowDataDependencyGraph.addDataDependencyOnReturn(extractShadow, extractReturn);

                ObjectInstance mockClassTarget = mockMapping.get(returnValue);
                List<MethodInvocation> mockMethodTargetList = new ArrayList<>();

                for (Pair<ObjectInstance, List<MethodInvocation>> callPair : transitiveMockingList) {
                    if (callPair.getFirst().equals(returnValue)) {
                        mockMethodTargetList = callPair.getSecond();
                    }
                }

                for (MethodInvocation mockCallTarget : mockMethodTargetList) {

                    List<Integer> callTargetCodepoints = new ArrayList<Integer>();

                    mockCallTarget.getMethodSignature().codePoints().forEach(callTargetCodepoints::add);

                    DataNode setShadowParameter = new PrimitiveValue(id.getAndIncrement(), "java.lang.String", callTargetCodepoints.toString());

                    MethodInvocation setShadow = new MethodInvocation(id.getAndIncrement(), SET_SIGNATURE);
                    setShadow.setOwner(extractReturn);
                    setShadow.setActualParameterInstances(Arrays.asList(setShadowParameter, mockClassTarget));

                    shadowExecutionFlowGraph.enqueueMethodInvocations(setShadow);

                    shadowDataDependencyGraph.addMethodInvocationWithoutAnyDependency(setShadow);
                    shadowDataDependencyGraph.addDataDependencyOnActualParameter(setShadow, setShadowParameter, 0);
                    shadowDataDependencyGraph.addDataDependencyOnActualParameter(setShadow, mockClassTarget, 1);
                }

                System.out.println(returnValue);
        }

        System.out.println(transitiveMethodOwners);
        System.out.println(transitiveMethodInvocations);

        carvingMock.executionFlowGraphs.add(mockExecutionFlowGraph);
        carvingMock.dataDependencyGraphs.add(mockDataDependencyGraph);

        carvingShadow.executionFlowGraphs.add(shadowExecutionFlowGraph);
        carvingShadow.dataDependencyGraphs.add(shadowDataDependencyGraph);

        carvedTest.setDataDependencyGraph(carvedDataDependencyGraph);

        return new Triplette<>(carvingMock, carvingShadow, carvedTest);
    }
}
