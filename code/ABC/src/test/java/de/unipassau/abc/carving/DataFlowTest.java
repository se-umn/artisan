package de.unipassau.abc.carving;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.xml.ws.Action;

import org.df4j.core.boundconnector.messagescalar.ScalarInput;
import org.df4j.core.simplenode.messagescalar.CompletablePromise;
import org.df4j.core.tasknode.AsyncAction;
import org.junit.Assert;
import org.junit.Test;

import de.unipassau.abc.carving.steps.CarvingNode;
import de.unipassau.abc.data.Pair;
import no.systek.dataflow.StepExecutor;

public class DataFlowTest {

//    public static class Square extends AsyncAction<Integer> {
//        final CompletablePromise<Integer> result = new CompletablePromise<>();
//        final ScalarInput<Integer> param = new ScalarInput<>(this);
//
//        @Action
//        public void compute(Integer arg) {
//            int res = arg*arg;
//            result.complete(res);
//        }
//    }
//    public static class Sum extends AsyncAction<Integer> {
//        final CompletablePromise<Integer> result = new CompletablePromise<>();
//        final ScalarInput<Integer> paramX = new ScalarInput<>(this);
//        final ScalarInput<Integer> paramY = new ScalarInput<>(this);
//
//        @Action
//        public void compute(Integer argX, Integer argY) {
//            int res = argX + argY;
//            result.complete(res);
//        }
//    }
//    
//    @Test
//    public void testAP() throws ExecutionException, InterruptedException {
//        // create 3 nodes
//        Square sqX = new Square();
//        Square sqY = new Square();
//        Sum sum = new Sum();
//        // make 2 connections
//        sqX.result.subscribe(sum.paramX);
//        sqY.result.subscribe(sum.paramY);
//        // start all the nodes
//        sqX.start();
//        sqY.start();
//        sum.start();
//        // provide input information:
//        sqX.param.post(3);
//        sqY.param.post(4);
//        // get the result
//        int res = sum.result.get();
//        Assert.assertEquals(25, res);
//    }
    
    @Test
    public void testDependencies() {
        ObjectInstance mainActivity = ObjectInstanceFactory.get("com.farmerbb.notepad.activity.MainActivity@51164323");
        NullInstance bundle = NullNodeFactory.get("android.os.Bundle");
        
        MethodInvocation onCreate = new MethodInvocation(5, "<com.farmerbb.notepad.activity.MainActivity: void onCreate(android.os.Bundle)>");
        onCreate.setOwner( mainActivity );
        onCreate.setActualParameterInstances( Arrays.asList( bundle ) );
        
        CarvingNode onCreateNode = new CarvingNode( onCreate );
        CarvingNode mainActivityNode = new CarvingNode( mainActivity, onCreate);
        CarvingNode bundleNode = new CarvingNode( bundle, onCreate );
        
        onCreateNode.acceptAsOwner( mainActivityNode );
        onCreateNode.acceptAsParameterInPosition( bundleNode, 0);
        
        MethodInvocation init3 = new MethodInvocation(3, "<android.support.v7.app.AppCompatActivity: void <init>()>");
        init3.setConstructor(true);
        // Is this necessary ?
        init3.setOwner(mainActivity);
        CarvingNode init3Node = new CarvingNode( init3 );
        
        mainActivityNode.acceptAsStateChangingMethod( init3Node );
        
        // get the result
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        StepExecutor stepExecutor = new StepExecutor(executorService, s -> {
        }, () -> null, 5, 20, TimeUnit.SECONDS);
        
        System.out.println();
        System.out.println();
        
        Pair<ExecutionFlowGraph, DataDependencyGraph> result = onCreateNode.executeWith(stepExecutor);
        
        System.out.println("Execution: " + result.getFirst().getOrderedMethodInvocations());
        System.out.println("Data : " + result.getSecond().getDataNodes());
        
    }
    
    @Test
    public void testConnections() {
        // invoke a method with two parameters
        List<DataNode> actualParameterInstances = new ArrayList<>();
        actualParameterInstances.add(PrimitiveNodeFactory.get("int", "1"));
        actualParameterInstances.add(PrimitiveNodeFactory.get("int", "2"));

        DataNode returnValue = PrimitiveNodeFactory.get("int", "3");

        MethodInvocation sum = new MethodInvocation(1, "<ABC: int sum(int, int)>");
        sum.setStatic(true);
        sum.setActualParameterInstances(actualParameterInstances);
        sum.setReturnValue(returnValue);

        List<CarvingNode> parameterNodes = new ArrayList<>();
        for (DataNode p : actualParameterInstances) {
            parameterNodes.add(new CarvingNode(p, sum));
        }


        CarvingNode sumMethodInvocation = new CarvingNode(sum);

        for (int position = 0; position < parameterNodes.size(); position++) {
            // sum depends on the other two
            CarvingNode parameterNode = parameterNodes.get(position);
            sumMethodInvocation.acceptAsParameterInPosition(parameterNode, position);
        }
        // get the result
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        StepExecutor stepExecutor = new StepExecutor(executorService, s -> {
        }, () -> null, 5, 20, TimeUnit.SECONDS);
        Pair<ExecutionFlowGraph, DataDependencyGraph> result = sumMethodInvocation.executeWith(stepExecutor);

        System.out.println("DataFlowTest.testConnections() " + result.getFirst().getOrderedMethodInvocations());
        System.out.println("DataFlowTest.testConnections() " + result.getSecond().getDataNodes());

    }

    @Test
    public void testObjectInstances() {
        // invoke a method with two parameters
        List<DataNode> actualParameterInstances = new ArrayList<>();
        actualParameterInstances.add(DataNodeFactory.get("java.lang.Object", "java.lang.Object@1"));
        actualParameterInstances.add(DataNodeFactory.get("int", "2"));

        DataNode returnValue = DataNodeFactory.get("abc.foo.Bar", "abc.foo.Bar@1234");

        MethodInvocation sum = new MethodInvocation(1, "<ABC: abc.foo.Bar sum(java.lang.Object, int)>");
        sum.setStatic(true);
        sum.setActualParameterInstances(actualParameterInstances);
        sum.setReturnValue(returnValue);

        List<CarvingNode> parameterNodes = new ArrayList<>();
        for (DataNode p : actualParameterInstances) {
            parameterNodes.add(new CarvingNode(p, sum));
        }


        CarvingNode sumMethodInvocation = new CarvingNode(sum);

        for (int position = 0; position < parameterNodes.size(); position++) {
            // sum depends on the other two
            CarvingNode parameterNode = parameterNodes.get(position);
            // sum depends on the other two
            sumMethodInvocation.acceptAsParameterInPosition(parameterNode, position);
        }
        // get the result
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        StepExecutor stepExecutor = new StepExecutor(executorService, s -> {
        }, () -> null, 5, 20, TimeUnit.MINUTES);
        Pair<ExecutionFlowGraph, DataDependencyGraph> result = sumMethodInvocation.executeWith(stepExecutor);

        System.out.println("DataFlowTest.testConnections() " + result.getFirst().getOrderedMethodInvocations());
        System.out.println("DataFlowTest.testConnections() " + result.getSecond().getDataNodes());

    }
}
