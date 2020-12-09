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
    private static final String RETURN_SIGNATURE = "<org.mockito.Mockito: org.mockito.stubbing.Stubber doReturn(java.lang.Object)>";
    private static final String WHEN_SIGNATURE = "<org.mockito.stubbing.Stubber: java.lang.Object when(java.lang.Object)>";

    private static final AtomicInteger id = new AtomicInteger(1);

    // replace method invocation instead of adding to CarvingMock dependency graph?
    public Pair<CarvingMock, CarvedTest> generateMocks(CarvedTest carvedTest, CarvedExecution carvedExecution) {

        CarvingMock carvingMock = new CarvingMock();

        DataDependencyGraph carvedDataDependencyGraph = carvedTest.getDataDependencyGraph();

        ExecutionFlowGraph mockExecutionFlowGraph = new ExecutionFlowGraphImpl();
        DataDependencyGraph mockDataDependencyGraph = new DataDependencyGraphImpl(); 

        for (ObjectInstance danglingObject : carvedTest.getDataDependencyGraph().getDanglingObjects()) {

            ObjectInstance classToMock = ObjectInstanceFactory.get(danglingObject.getType() + "@" + id.getAndIncrement());
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

                DataNode doReturnArgument = DataNodeFactory.get(returnValue.getType(), returnValue.toString());
                ObjectInstance doReturnReturn = ObjectInstanceFactory.get("org.mockito.stubbing.Stubber@" + id.getAndIncrement());

                MethodInvocation doReturnMock = new MethodInvocation(id.getAndIncrement(), RETURN_SIGNATURE);
                doReturnMock.setStatic(true);
                doReturnMock.setActualParameterInstances(Arrays.asList(doReturnArgument));
                doReturnMock.setReturnValue((DataNode) doReturnReturn);

                mockExecutionFlowGraph.enqueueMethodInvocations(doReturnMock);

                mockDataDependencyGraph.addMethodInvocationWithoutAnyDependency(doReturnMock);
                mockDataDependencyGraph.addDataDependencyOnActualParameter(doReturnMock, doReturnArgument, 0);
                mockDataDependencyGraph.addDataDependencyOnReturn(doReturnMock, (DataNode) doReturnReturn);

                ObjectInstance whenReturn = ObjectInstanceFactory.get(danglingObject.getType() + "@" + id.getAndIncrement());

                MethodInvocation whenMock = new MethodInvocation(id.getAndIncrement(), WHEN_SIGNATURE);
                whenMock.setOwner(doReturnReturn);
                whenMock.setActualParameterInstances(Arrays.asList(classToMock));
                whenMock.setReturnValue((DataNode) whenReturn);

                mockExecutionFlowGraph.enqueueMethodInvocations(whenMock);

                mockDataDependencyGraph.addMethodInvocationWithoutAnyDependency(whenMock);
                mockDataDependencyGraph.addDataDependencyOnActualParameter(whenMock, classToMock, 0);
                mockDataDependencyGraph.addDataDependencyOnOwner(whenMock, doReturnReturn);
                mockDataDependencyGraph.addDataDependencyOnReturn(whenMock, (DataNode) whenReturn);

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
                dependentCall.setActualParameterInstances(dependentCallParameters);
                // adding to datadep graph not needed?? (this doesn't work??)
                carvedDataDependencyGraph.replaceMethodInvocation(dependentCall, newDependentCall);
            }
        }

        carvingMock.dataDependencyGraphs.add(mockDataDependencyGraph);
        carvingMock.executionFlowGraphs.add(mockExecutionFlowGraph);

        carvedTest.setDataDependencyGraph(carvedDataDependencyGraph);

        return new Pair<CarvingMock, CarvedTest>(carvingMock, carvedTest);
    }
}
