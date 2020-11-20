package de.unipassau.abc.generation.mocks;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Set;
import java.util.NoSuchElementException;
import java.util.function.UnaryOperator;

import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.generation.data.CarvedTest;
import de.unipassau.abc.carving.CarvedExecution;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.DataDependencyGraphImpl;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.ExecutionFlowGraphImpl;
import de.unipassau.abc.data.DataNode;
import de.unipassau.abc.data.DataNodeFactory;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.data.ObjectInstanceFactory;
import de.unipassau.abc.data.PrimitiveNodeFactory;
import de.unipassau.abc.data.Pair;

import edu.emory.mathcs.backport.java.util.concurrent.atomic.AtomicInteger;

public class MockGenerator {

    private static final String MOCK_SIGNATURE = "<org.mockito.Mockito: java.lang.Object mock(java.lang.Class)>";
    private static final String WHEN_SIGNATURE = "<org.mockito.Mockito: org.mockito.stubbing.OngoingStubbing when(java.lang.Object)>";
    private static final String RETURN_SIGNATURE = "<org.mockito.stubbing.OngoingStubbing: org.mockito.stubbing.OngoingStubbing thenReturn(java.lang.Object)>";

    private static final AtomicInteger id = new AtomicInteger(1);
    private static final AtomicInteger objectId = new AtomicInteger(1);

    public Pair<CarvingMock, CarvedTest> generateMocks(CarvedTest carvedTest, CarvedExecution carvedExecution) {

        CarvingMock carvingMock = new CarvingMock();

        DataDependencyGraph carvedDataDependencyGraph = carvedTest.getDataDependencyGraph();

        ExecutionFlowGraph mockExecutionFlowGraph = new ExecutionFlowGraphImpl();
        DataDependencyGraph mockDataDependencyGraph = new DataDependencyGraphImpl(); 

        for (MethodInvocation carvedCall : carvedTest.getDataDependencyGraph().getAllMethodInvocations()) {
            System.out.println("carved: " + carvedCall);
            // remove the method in carvedexecution along with this
            for (MethodInvocation androidCall : carvedExecution.getCallGraphContainingTheMethodInvocation(carvedCall).getMethodInvocationsSubsumedBy(carvedCall)) {
                if (androidCall.getMethodSignature().contains("android")) {
                    System.out.println("android call: " + androidCall);
                }
            }

            // for (ObjectInstance danglingObject : carvedTest.getDataDependencyGraph().getDanglingObjects()) {
            //     for (MethodInvocation dependentCall : carvedExecution.getDataDependencyGraphContainingTheMethodInvocation(carvedCall).getMethodInvocationsForOwner(danglingObject)) {
            //         System.out.println("called on object: " + dependentCall);
            //     }
            // }
        }

        for (ObjectInstance danglingObject : carvedTest.getDataDependencyGraph().getDanglingObjects()) {

            ObjectInstance classToMock = ObjectInstanceFactory.get(danglingObject.getType() + "@" + objectId.getAndIncrement());
            DataNode classLiteralToMock = PrimitiveNodeFactory.createClassLiteralFor(classToMock);

            MethodInvocation initMock = new MethodInvocation(id.getAndIncrement(), MOCK_SIGNATURE);
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

                dependentCall.setOwner(classToMock);
                mockExecutionFlowGraph.enqueueMethodInvocations(dependentCall);
                mockDataDependencyGraph.addDataDependencyOnOwner(dependentCall, classToMock);

                // this should be the argument to thenReturn
                DataNode whenArgument = DataNodeFactory.get(returnValue.getType(), returnValue.toString());
                // owner for thenReturn
                ObjectInstance whenReturn = ObjectInstanceFactory.get("org.mockito.stubbing.OngoingStubbing@" + objectId.getAndIncrement());

                MethodInvocation whenMock = new MethodInvocation(id.getAndIncrement(), WHEN_SIGNATURE);
                whenMock.setStatic(true);
                whenMock.setActualParameterInstances(Arrays.asList(whenArgument));
                whenMock.setReturnValue((DataNode) whenReturn);

                mockExecutionFlowGraph.enqueueMethodInvocations(whenMock);

                mockDataDependencyGraph.addMethodInvocationWithoutAnyDependency(whenMock);
                mockDataDependencyGraph.addDataDependencyOnActualParameter(whenMock, whenArgument, 0);
                mockDataDependencyGraph.addDataDependencyOnReturn(whenMock, (DataNode) whenReturn);

                ObjectInstance thenReturnReturn = ObjectInstanceFactory.get("org.mockito.stubbing.OngoingStubbing@" + objectId.getAndIncrement());

                MethodInvocation returnMock = new MethodInvocation(id.getAndIncrement(), RETURN_SIGNATURE);
                returnMock.setOwner(whenReturn);
                returnMock.setActualParameterInstances(Arrays.asList(whenArgument));
                returnMock.setReturnValue((DataNode) thenReturnReturn);

                mockExecutionFlowGraph.enqueueMethodInvocations(returnMock);

                mockDataDependencyGraph.addMethodInvocationWithoutAnyDependency(returnMock);
                mockDataDependencyGraph.addDataDependencyOnActualParameter(returnMock, whenArgument, 0);
                mockDataDependencyGraph.addDataDependencyOnOwner(returnMock, whenReturn);
                mockDataDependencyGraph.addDataDependencyOnReturn(returnMock, (DataNode) thenReturnReturn);

                System.out.println("called on object: " + dependentCall);
            }

            // THIS DATA DEPENDENCY GRAPH MAY NEED TO BE THE TEST'S; NEED TO CHANGE METHODS IN TEST DIRECTLY
            for (MethodInvocation dependentCall : carvedTest.getDataDependencyGraph().getMethodInvocationsWhichUse(danglingObject)) {
                List<DataNode> dependentCallParameters = carvedDataDependencyGraph.getParametersOf(dependentCall);
                UnaryOperator<DataNode> op = arg -> { if (arg.equals(danglingObject)) { return classToMock; } else { return arg; }};
                dependentCallParameters.replaceAll(op);
                MethodInvocation newDependentCall = dependentCall;
                dependentCall.setActualParameterInstances(dependentCallParameters);
            }

        }

        carvingMock.dataDependencyGraphs.add(mockDataDependencyGraph);
        carvingMock.executionFlowGraphs.add(mockExecutionFlowGraph);

        carvedTest.setDataDependencyGraph(carvedDataDependencyGraph);

        return new Pair<CarvingMock, CarvedTest>(carvingMock, carvedTest);
    }
}
