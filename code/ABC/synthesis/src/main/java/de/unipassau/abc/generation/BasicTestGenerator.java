package de.unipassau.abc.generation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.carving.BasicCarver;
import de.unipassau.abc.carving.CarvedExecution;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.AndroidMethodInvocation;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.DataDependencyGraphImpl;
import de.unipassau.abc.data.DataNode;
import de.unipassau.abc.data.DataNodeFactory;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.ExecutionFlowGraphImpl;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.generation.assertions.AssertionGenerator;
import de.unipassau.abc.generation.assertions.CarvingAssertion;
import de.unipassau.abc.generation.assertions.NullValueAssertionGenerator;
import de.unipassau.abc.generation.assertions.PrimitiveValueAssertionGenerator;
import de.unipassau.abc.generation.data.AndroidCarvedTest;
import de.unipassau.abc.generation.data.CarvedTest;
import de.unipassau.abc.generation.data.CatchBlock;
import de.unipassau.abc.generation.mocks.MockGenerator;
import de.unipassau.abc.generation.simplifiers.AndroidMultiActivitySimplifier;
import de.unipassau.abc.generation.simplifiers.CarvedExecutionSimplifier;
import de.unipassau.abc.generation.simplifiers.RobolectricSimplifier;
import de.unipassau.abc.parsing.ParsedTrace;

public class BasicTestGenerator implements TestGenerator {

    /**
     * A reference test would look like this:
     * 
     * - Mock definition and programming
     * 
     * - Setup of the Activity
     * 
     * - Shadown configuration: findViewById + Mocking
     * 
     * - Test "execution
     * 
     * - Assertions
     */

    private final static Logger logger = LoggerFactory.getLogger(BasicTestGenerator.class);

    private List<CarvedExecutionSimplifier> simplifiers = Arrays.asList(
            // Make sure that only one activity is present in the carved test
            new AndroidMultiActivitySimplifier(),
            // Transform android-calls into robolectric-calls
            new RobolectricSimplifier());

    @Override
    public Collection<CarvedTest> generateTests(List<MethodInvocation> targetMethodsInvocations, ParsedTrace trace)
            throws CarvingException, ABCException {
        // NOT EASY TO FIND DUPLICATES :)
        Collection<CarvedTest> carvedTests = new ArrayList<>();

        /*
         * Extract only the necessary invocations from the trace, using direct
         * data-dependencies
         */
        BasicCarver carver = new BasicCarver(trace);

        for (MethodInvocation targetMethodInvocation : targetMethodsInvocations) {
            // TODO When carving multiple targes we can use
            // carver.carve(List<methodInvocations>), this invalidate automatically
            // the cache
            carver.clearTheCache();

            for (CarvedExecution carvedExecution : carver.carve(targetMethodInvocation)) {
                try {

//                    ALESSIO: CarvedExecution CONTAINS the implicit dependencies, not sure why the CARVED test does
//                    not, maybe we cut them away to simplify synthesis?
                    CarvedTest carvedTest = generateCarvedTestFromCarvedExecution(carvedExecution);

                    carvedTests.add(carvedTest);

                    logger.info("-------------------------");
                    logger.info("DONE CARVING:" + targetMethodInvocation + " from " + carvedExecution.traceId);
                    logger.info("-------------------------");

                } catch (CarvingException e) {
                    // TODO May be too coarse exception
                    logger.error("-------------------------");
                    logger.error("Error Carving " + targetMethodInvocation + " from " + carvedExecution.traceId);
                    logger.error("Reason: " + e.getMessage());
                    logger.error("-------------------------");
                }
            }
        }

        return carvedTests;
    }

    /**
     * 
     * In general, a carved test is a list of command executions that do not violate
     * constraints on visibility (cannot invoke private methods), definition-usages
     * of variabls (cannot use before it is defined). However, in some cases, the
     * carved tests must have additional properties. For example, if we are testing
     * an AndroidActivity that is stared with an Intent, we need to make sure we do
     * it the right way (see
     * https://stackoverflow.com/questions/17942646/robolectric-2-create-activity-under-test-with-intent).
     * 
     * And this requires us to use heuristic to understand what type of tests we
     * want/need to create
     * 
     * @param carvedExecution
     * @return
     * @throws CarvingException
     */
    public CarvedTest generateCarvedTestFromCarvedExecution(CarvedExecution optimisticCarvedExecution)
            throws CarvingException {

        /**
         * A carved test can be implemented as a list of method executions (i.e,.
         * ExecutionFlowGraph) connected with DataDependencies (i.e,
         * DataDependencyGraph);
         */
        ExecutionFlowGraph executionFlowGraph = new ExecutionFlowGraphImpl();
        DataDependencyGraph dataDependencyGraph = new DataDependencyGraphImpl();
        MethodInvocation methodInvocationUnderTest = optimisticCarvedExecution.methodInvocationUnderTest;
        // logging
        logger.info("-------------------------");
        logger.info("  GENERATING TEST FOR: " + methodInvocationUnderTest);
        logger.info("-------------------------");

        logger.info("-------------------------");
        logger.info("  TRY TO SIMPLIFY TEST EXECUTION");
        logger.info("-------------------------");

        // TODO At this point the carver selected the methods that belong to the trace.
        // However, we may not be able to generate the test because it will nevertheless
        // violates some conditions. For example, that more than ONE activity is
        // created.
        // Therefore we need to attempt simplify the slice to get as close as possible
        // to what we want/need
        CarvedExecution _carvedExecution = optimisticCarvedExecution;
        //
        for (CarvedExecutionSimplifier simplifier : this.simplifiers) {
            try {
                _carvedExecution = simplifier.simplify(_carvedExecution);
            } catch (CarvingException ce) {
                logger.error("Simplification Failed", ce);
                throw ce;
            } catch (ABCException e) {
                logger.error("Simplification Failed", e);
                throw new CarvingException("Simplification Failed", e);
            }
        }

        //
        final CarvedExecution carvedExecution = _carvedExecution;
        /*
         * The carved execution contains a collection of (complete) call graphs, but in
         * the test we can only directly invoke the root(s) of those graphs, as all the
         * other invocations will be automatically executed (unless we breakpoint the
         * execution?).
         *
         */
        final List<MethodInvocation> directlyCallableMethodInvocations = new ArrayList<>();
        carvedExecution.callGraphs.forEach(callGraph -> directlyCallableMethodInvocations.addAll(callGraph.getRoots()));
        Collections.sort(directlyCallableMethodInvocations);
        // logging
        logger.info("Directly callable method invocations:");
        directlyCallableMethodInvocations.stream().map(m -> m.toString()).forEach(logger::info);

        /*
         * Validation: If the directlyCallableMethodInvocations do not contain the
         * target method invocation AND this was not on purpose (e.g., simplification),
         * this test needs some adjustment. For the moment, simply raise an exception.
         */
        boolean needsFix = false;
        if (!directlyCallableMethodInvocations.stream()
                .anyMatch(mi -> mi.equals(carvedExecution.methodInvocationUnderTest))
                && !carvedExecution.isMethodInvocationUnderTestWrapped) {

            // This might break the test because it might expose a method that is necessary
            // but subsumes the MUT resulting in invoking the method under test twice!
            logger.info("Method invocation under test " + carvedExecution.methodInvocationUnderTest
                    + " is not visible. Try to recover ...");
            needsFix = true;

            List<MethodInvocation> necessaryAndSubsuming = carvedExecution
                    .getCallGraphContainingTheMethodInvocationUnderTest()
                    .getOrderedSubsumingMethodInvocationsFor(carvedExecution.methodInvocationUnderTest).stream()
                    .filter(mi -> mi.isNecessary()).collect(Collectors.toList());

            if (necessaryAndSubsuming.size() > 0) {
                throw new CarvingException("We cannot make visible " + carvedExecution.methodInvocationUnderTest
                        + " because it is subsumed by necessary invocations: " + necessaryAndSubsuming);
            }

            /*
             * Recovery works as follow. Recursively replace the invocation of a parent
             * method with its execution until the mehtod invocation under test becomes
             * visible again. Next, remove the "un-necessary" method invocations from the
             * carve execution.
             */

            CallGraph callGraph = carvedExecution.getCallGraphContainingTheMethodInvocationUnderTest();

            // Reverse iteration
            List<MethodInvocation> methodsSubsumingMethodInvocationUnderTest = callGraph
                    .getOrderedSubsumingMethodInvocationsFor(carvedExecution.methodInvocationUnderTest);
            // logging
            logger.info("Subsuming method invocations:");
            methodsSubsumingMethodInvocationUnderTest.stream().map(m -> m.toString()).forEach(logger::info);

            ListIterator<MethodInvocation> listIterator = methodsSubsumingMethodInvocationUnderTest
                    .listIterator(methodsSubsumingMethodInvocationUnderTest.size());

            while (listIterator.hasPrevious()) {
                MethodInvocation methodInvocationToReplace = listIterator.previous();
                callGraph.replaceMethodInvocationWithExecution(methodInvocationToReplace);
            }

            // TODO FIXME I suspect that at this point we lost the information on relevant
            // invocations because the methodInvocationUnderTest is not listed among the
            // relevant method invocations ?!

            /*
             * At this point we should get rid of the method invocations that became visible
             * after replacing their caller but are not strictly necessary for carving, but
             * have been included only because subsumed
             */

            // THIS PROBABLY CAN BE DONE WITH SOME reduce/map magic...
            List<MethodInvocation> unecessaryMethodInvocations = callGraph.getRoots().stream()
                    .filter(mi -> !mi.isNecessary()).collect(Collectors.toList());
            // logging
            logger.info("Unnecessary method invocations:");
            unecessaryMethodInvocations.stream().map(m -> m.toString()).forEach(logger::info);

            for (MethodInvocation removeMe : unecessaryMethodInvocations) {
                /*
                 * Remove unnecessary method invocations and all the method invocations that are
                 * subsumed from it
                 */
                callGraph.remove(removeMe);
            }

        }

        // Check if, after the recovery, the method under test is still not visible...
        directlyCallableMethodInvocations.clear();
        carvedExecution.callGraphs.forEach(callGraph -> directlyCallableMethodInvocations.addAll(callGraph.getRoots()));
        if (needsFix) {
            // logging
            logger.info("New directly callable method invocations:");
            directlyCallableMethodInvocations.stream().map(m -> m.toString()).forEach(logger::info);
        }

        if (!directlyCallableMethodInvocations.stream()
                .anyMatch(mi -> mi.equals(carvedExecution.methodInvocationUnderTest))
                && !carvedExecution.isMethodInvocationUnderTestWrapped) {
            throw new CarvingException("Target method invocation " + carvedExecution.methodInvocationUnderTest
                    + "is not directly visible ");
        }

        logger.info("-------------------------");
        logger.info(" VALIDATING THE CARVED EXECUTION ");
        logger.info("-------------------------");

        /*
         * Validation: If the directlyCallableMethodInvocations contain private methods,
         * that's an error
         * 
         * TODO Unless we want to use Reflection and call private methods anyway !
         */
        for (MethodInvocation directlyCallableMethodInvocation : directlyCallableMethodInvocations) {
            if (directlyCallableMethodInvocation.isPrivate()) {
                // TODO Describe
                throw new CarvingException(
                        "Carved tests cannot make use of PRIVATE methods! Offending method invocation: "
                                + directlyCallableMethodInvocation);
            }
        }

        //
        /*
         * Sort the method invocations by their original order of execution. This
         * represents the body of the test
         */
        Collections.sort(directlyCallableMethodInvocations);

        // We need to ensure that dataDependecies are maintained in the carved test as
        // they may undergo some post-processing that require to know that. But we need
        // to be sure that theres nothing more than the necessary here.

        /*
         * Generate the structures required for test synthesis. MOVE THIS TO AN EXTERNAL
         * METHOD MAYBE?
         */
        for (MethodInvocation methodInvocation : directlyCallableMethodInvocations) {
            // TODO For some reason here we do not look up the data dep graph, but directly
            // look inside the methodInvocation object

            executionFlowGraph.enqueueMethodInvocations(methodInvocation);
            // Fix the data deps
            dataDependencyGraph.addMethodInvocationWithoutAnyDependency(methodInvocation);
            if (!methodInvocation.isStatic()) {
                dataDependencyGraph.addDataDependencyOnOwner(methodInvocation, methodInvocation.getOwner());
            }
            /*
             * If the method invocation is exceptional, there's no return type there !
             */
            if (!JimpleUtils.hasVoidReturnType(methodInvocation.getMethodSignature())
                    && !methodInvocation.isExceptional()) {
                dataDependencyGraph.addDataDependencyOnReturn(methodInvocation, methodInvocation.getReturnValue());
            }

            ListIterator<DataNode> it = methodInvocation.getActualParameterInstances().listIterator();
            while (it.hasNext()) {
                int position = it.nextIndex();
                DataNode parameter = it.next();
                dataDependencyGraph.addDataDependencyOnActualParameter(methodInvocation, parameter, position);
            }

//            for (DataDependencyGraph ddg : carvedExecution.dataDependencyGraphs) {
//                for (ObjectInstance obj : ddg.getObjectInstances()) {
//                    ddg.getMethodInvocationsWhichUse(obj).forEach(System.out::println);
//                }
//            }

            DataDependencyGraph ddg = carvedExecution
                    .getDataDependencyGraphContainingTheMethodInvocation(methodInvocation);

            MethodInvocation theMethodInvocation = ddg.getAllMethodInvocations().stream()
                    .filter(mi -> mi.equals(methodInvocation)).findFirst().get();

            // Add the implicit dependencies here
            ListIterator<DataNode> implicitIt = ddg.getImplicitDataDependenciesOf(theMethodInvocation).listIterator();

            while (implicitIt.hasNext()) {
                DataNode implicitDataDependency = implicitIt.next();
                dataDependencyGraph.addImplicitDataDependency(methodInvocation, implicitDataDependency);
            }

        }

        // If the method is not exceptional (but this might not be 100% correct enables
        // spies if needed
        if (!methodInvocationUnderTest.isExceptional()) {
            /*
             * SPY or not SPY
             */

            if (!directlyCallableMethodInvocations.contains(methodInvocationUnderTest)
                    && !carvedExecution.isMethodInvocationUnderTestWrapped) {
                // SPY
                throw new CarvingException(
                        "Method Invocation under test is not visible in the carved tests. This requires SPY-ing, which not yet implemented!");
            }
        }

        /*
         * Create the test from the execution
         */

        //
        CarvedTest carvedTest = null;

        if (methodInvocationUnderTest.isExceptional()) {
            carvedTest = generateExceptionalTestCase(methodInvocationUnderTest, executionFlowGraph, dataDependencyGraph,
                    carvedExecution);

        } else {

            // Include assertions here
            boolean containsAndroidMethodInvocations = containsAndroidMethodInvocations(executionFlowGraph,
                    carvedExecution.callGraphs);
            if (containsAndroidMethodInvocations) {
                carvedTest = new AndroidCarvedTest(methodInvocationUnderTest, //
                        executionFlowGraph, dataDependencyGraph, carvedExecution.traceId);
            } else {
                carvedTest = new CarvedTest(methodInvocationUnderTest, //
                        executionFlowGraph, dataDependencyGraph, carvedExecution.traceId);
            }
            // Add the assertions using the AssertionGenerationPipeline (basically, add all
            // the assertions that can be added).
            if (!JimpleUtils.hasVoidReturnType(carvedTest.getMethodUnderTest().getMethodSignature())) {
                // TODO We might not be able to handle the case of a NULL string since we
                // consider String as primitive type, and primitives cannot be null...
                if (JimpleUtils
                        .isPrimitive(JimpleUtils.getReturnType(carvedTest.getMethodUnderTest().getMethodSignature())) ||
                //
                        JimpleUtils.isString(
                                JimpleUtils.getReturnType(carvedTest.getMethodUnderTest().getMethodSignature()))) {
                    AssertionGenerator assertionGenerator = new PrimitiveValueAssertionGenerator();

                    CarvingAssertion returnValueAssertion = assertionGenerator.generateAssertionsFor(carvedTest,
                            carvedExecution);

                    carvedTest.addAssertion(returnValueAssertion);

                } else if (JimpleUtils
                        .isArray(JimpleUtils.getReturnType(carvedTest.getMethodUnderTest().getMethodSignature()))) {
                    logger.warn(
                            "Implementing assertions over array is not yet implemented. There will not be assertions for method under test: "
                                    + carvedTest.getMethodUnderTest());
                    // throw new NotImplementedException("Assertions over arrays not yet
                    // implemented");
                } else {
                    // Those are regular objects

                    // Assert whether the return value is null/not-null
                    AssertionGenerator assertionGenerator = new NullValueAssertionGenerator();
                    CarvingAssertion nullValueAssertion = assertionGenerator.generateAssertionsFor(carvedTest,
                            carvedExecution);
                    carvedTest.addAssertion(nullValueAssertion);

                    // Assert the value for boxed types that are NOT null
                    if (JimpleUtils.isBoxedPrimitive(
                            JimpleUtils.getReturnType(carvedTest.getMethodUnderTest().getMethodSignature()))
                            && !((ObjectInstance) carvedExecution.methodInvocationUnderTest.getReturnValue())
                                    .isNull()) {
                        CarvingAssertion returnValueAssertion = assertionGenerator.generateAssertionsFor(carvedTest,
                                carvedExecution);
                        carvedTest.addAssertion(returnValueAssertion);
                    }

                    // Assert the value for String types that are NOT null
                    if (JimpleUtils
                            .isString(JimpleUtils.getReturnType(carvedTest.getMethodUnderTest().getMethodSignature()))
                            && !((ObjectInstance) carvedExecution.methodInvocationUnderTest.getReturnValue())
                                    .isNull()) {
                        CarvingAssertion returnValueAssertion = assertionGenerator.generateAssertionsFor(carvedTest,
                                carvedExecution);
                        carvedTest.addAssertion(returnValueAssertion);
                    }
                }
            }
        }

        /*
         * Add mocks and shadows
         */
        MockGenerator mockGenerator = new MockGenerator();
        mockGenerator.generateMocks(carvedTest, carvedExecution);

        /*
         * TODO Add assertions here
         */

        return carvedTest;
    }

    private CarvedTest generateExceptionalTestCase(MethodInvocation methodInvocationUnderTest, //
            ExecutionFlowGraph executionFlowGraph, //
            DataDependencyGraph dataDependencyGraph, //
            CarvedExecution carvedExecution) {

        // TODO Move the following logic into separated methods. Probably an assertion
        // generator of some sort

        /*
         * If the execution was exceptional, we need to include try-fail + catch-pass
         * exception code + catch-fail ...
         */

        // Create the String message for the fail operation.
        final String defaultFailMessage = "Expected exception was not thrown!";
        DataNode defaultFailMessageNode = DataNodeFactory.get("java.lang.String",
                Arrays.toString(defaultFailMessage.getBytes()));

        // Create the invocation to "fail" with message in case. This must be the last
        // invocation of the carved test, and it will be wrapped by the try catch
        int nextMethodTraceId = executionFlowGraph.getLastMethodInvocation().getInvocationTraceId() + 1;
        int nextMethodInvocationCount = executionFlowGraph.getLastMethodInvocation().getInvocationCount() - 1;

        MethodInvocation defaultFailMethodInvocation = new MethodInvocation(nextMethodTraceId,
                nextMethodInvocationCount, SyntheticMethodSignatures.FAIL_WITH_MESSAGE);
        defaultFailMethodInvocation.setPublic(true);
        defaultFailMethodInvocation.setStatic(true);
        defaultFailMethodInvocation.setSyntheticMethod(true);

        // Attach the parameter to the method invocation. This is a quite dangerous
        // duplication of information. We should probably rely only and only on the
        // graphs !
        List<DataNode> actualParameterInstances = new ArrayList<>();
        actualParameterInstances.add(defaultFailMessageNode);
        defaultFailMethodInvocation.setActualParameterInstances(actualParameterInstances);

        // Add the execution by the end of the test
        executionFlowGraph.enqueueMethodInvocations(defaultFailMethodInvocation);

        // Make sure we include the data dependencies on the string
        dataDependencyGraph.addMethodInvocationWithoutAnyDependency(defaultFailMethodInvocation);
        dataDependencyGraph.addDataDependencyOnActualParameter(defaultFailMethodInvocation, defaultFailMessageNode, 0);

        logger.info("Added fail() call" + defaultFailMethodInvocation );
        
        // How do we tell that we want to catch that specific exception?
        ObjectInstance expectedException = methodInvocationUnderTest.getRaisedException();

        ExecutionFlowGraph expectedExceptionCatchBlockExecutionFlowGraph = new ExecutionFlowGraphImpl();
        DataDependencyGraph expectedExceptionCatchBlockDataDependencyGraph = new DataDependencyGraphImpl();

        // If we reach the following block an exception was raised but this is not the
        List<String> expectedExceptions = new ArrayList<>();
        expectedExceptions.add(expectedException.getType());
        CatchBlock catchExpectedException = new CatchBlock(expectedExceptions,
                expectedExceptionCatchBlockExecutionFlowGraph, expectedExceptionCatchBlockDataDependencyGraph);

        // There's no such a thing like PASS for the test. TODO Add other assertions if
        // needed, cause, message, etc.

        // one we expected
        ExecutionFlowGraph unexpectedExceptionCatchBlockExecutionFlowGraph = new ExecutionFlowGraphImpl();
        DataDependencyGraph unexpectedExceptionCatchBlockDataDependencyGraph = new DataDependencyGraphImpl();

        // Create the String message for the fail operation.
        final String unexpectedExceptionFailMessage = "A unexpected exception was thrown!";
        DataNode unexpectedExceptionFailMessageNode = DataNodeFactory.get("java.lang.String",
                Arrays.toString(unexpectedExceptionFailMessage.getBytes()));

        // Create the invocation to "fail" with message in case. This is the first
        // invocation inside the catch block
        int methodCount = 0;
        MethodInvocation failMethodInvocation = new MethodInvocation(MethodInvocation.INVOCATION_TRACE_ID_NA_CONSTANT,
                methodCount, SyntheticMethodSignatures.FAIL_WITH_MESSAGE);
        failMethodInvocation.setPublic(true);
        failMethodInvocation.setStatic(true);
        failMethodInvocation.setSyntheticMethod(true);
        //
        List<DataNode> actualParameterInstances2 = new ArrayList<DataNode>();
        actualParameterInstances2.add(unexpectedExceptionFailMessageNode);
        // TODO Get rid of data duplication between nodes and graphs...
        failMethodInvocation.setActualParameterInstances(actualParameterInstances2);

        // Methods
        unexpectedExceptionCatchBlockExecutionFlowGraph.enqueueMethodInvocations(failMethodInvocation);
        // Data
        unexpectedExceptionCatchBlockDataDependencyGraph.addMethodInvocationWithoutAnyDependency(failMethodInvocation);
        unexpectedExceptionCatchBlockDataDependencyGraph.addDataDependencyOnActualParameter(failMethodInvocation,
                unexpectedExceptionFailMessageNode, 0);

        List<String> unexpectedExceptions = new ArrayList<>();
        // We use Throwable to fix !97. It should be ok, in 99% of the cases, since
        // application methods do not usually throw "Throwable"
        unexpectedExceptions.add("java.lang.Throwable");
        CatchBlock catchUnexpectedException = new CatchBlock(unexpectedExceptions,
                unexpectedExceptionCatchBlockExecutionFlowGraph, unexpectedExceptionCatchBlockDataDependencyGraph);

        boolean containsAndroidMethodInvocations = containsAndroidMethodInvocations(executionFlowGraph,
                carvedExecution.callGraphs);

        // Patch to enable Android Specific extensions. Probably we need a better way to
        // do so
        // TODO Note that the method invocation is not the ONLY way to eastablish
        // whether or not the test case android related...
        if (containsAndroidMethodInvocations) {
            /*
             * carvedTest = new AndroidCarvedTest(methodInvocationUnderTest,
             * unexpectedExceptionCatchBlockExecutionFlowGraph,
             * unexpectedExceptionCatchBlockDataDependencyGraph);
             */
            return new AndroidCarvedTest(methodInvocationUnderTest, executionFlowGraph, dataDependencyGraph,
                    catchExpectedException, catchUnexpectedException, carvedExecution.traceId);
        } else {
            return new CarvedTest(methodInvocationUnderTest, //
                    executionFlowGraph, dataDependencyGraph, // Test Body + Fail
                    catchExpectedException, catchUnexpectedException, carvedExecution.traceId);
        }

    }

    /**
     * For each method invocation that we call explicitly from the execution flow
     * graph, we check if it or any of the subsumed method invocations belongs to
     * android
     *
     * @param executionFlowGraph
     * @param callGraphs
     * @return
     */
    private boolean containsAndroidMethodInvocations(ExecutionFlowGraph executionFlowGraph,
            Collection<CallGraph> callGraphs) {
        for (MethodInvocation mi : executionFlowGraph.getOrderedMethodInvocations()) {

            // Check if any of the explicitly called mi's is an android method invocation
            if (mi instanceof AndroidMethodInvocation) {
                return true;
            }

            // Check if any of the explicitly called mi's is an call to Robolectric - This
            // should be enough, since we need it to make our tests work
            if (mi.getMethodSignature().contains(
                    "<org.robolectric.Robolectric: org.robolectric.android.controller.ActivityController buildActivity")) {
                return true;
            }

            // Check if some of the invocations subsumed by mi is an android one
            for (CallGraph callGraph : callGraphs) {
                if (callGraph.getAllMethodInvocations().contains(mi)) {
                    boolean containsAndroidMethodInvocations = callGraph.getMethodInvocationsSubsumedBy(mi).stream()
                            .anyMatch(subMi -> subMi instanceof AndroidMethodInvocation);

                    if (containsAndroidMethodInvocations) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
