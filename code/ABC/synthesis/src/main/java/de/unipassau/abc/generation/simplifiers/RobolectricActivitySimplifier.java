package de.unipassau.abc.generation.simplifiers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.carving.BasicCarver;
import de.unipassau.abc.carving.CarvedExecution;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.DataDependencyGraphImpl;
import de.unipassau.abc.data.DataNode;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.data.ObjectInstanceFactory;
import de.unipassau.abc.data.PrimitiveNodeFactory;
import de.unipassau.abc.exceptions.ABCException;
import edu.emory.mathcs.backport.java.util.Collections;
import edu.emory.mathcs.backport.java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class transforms know patterns related to Activity life-cycle events in
 * calls to the Robolectric framework. Note we do not remove/replace any actual
 * calls to the app, instead we WRAP them.
 *
 * For instance: Robolectric.ActivityController.create() -> wraps (onSetup,
 * onCreate, etc.)
 *
 * The resulting test is simpler in the sense that the directly callable methods
 * are fewer since we mask them
 *
 *
 *
 * @author gambitemp
 *
 */
public class RobolectricActivitySimplifier extends AbstractCarvedExecutionSimplifier {
    private static Logger logger = LoggerFactory.getLogger(RobolectricActivitySimplifier.class);

    private ObjectInstance robolectricActivityController;

    private AtomicInteger uniqueID = new AtomicInteger(10000);

    @Override
    public CarvedExecution doSimplification(CarvedExecution carvedExecution) throws CarvingException, ABCException {

        /**
         * This code assumes that there is ONLY one activity per test, so it handles
         * only the FIRST activity!
         */
        logger.info("> Simplify using " + this.getClass());

        // TODO This can be refactored as many life-cycle events shared the same pattern

        // This introduces the ActivityController and call get() to retrieve the
        // Activity
        carvedExecution = resetIsNecessaryTag(carvedExecution);
        // Those also set the necessary tag

        carvedExecution = introduceControllerAndGetTheActivity(carvedExecution);
        carvedExecution = wrapOnCreate(carvedExecution);
        carvedExecution = wrapOnStart(carvedExecution);
        // TODO Visible might be used to carvedExecution =
        // wrapOnLoadFinished(carvedExecution);
        carvedExecution = wrapOnOptionsItemSelected(carvedExecution);
        carvedExecution = wrapOnCreateOptionsMenu(carvedExecution);
        carvedExecution = wrapOnStop(carvedExecution);
        carvedExecution = wrapOnPause(carvedExecution);
        carvedExecution = wrapOnResume(carvedExecution);
        carvedExecution = wrapOnDestroy(carvedExecution);

        return carvedExecution;
    }

    @Override
    public boolean appliesTo(CarvedExecution carvedExecution) {
        final AtomicBoolean trigger = new AtomicBoolean(false);

        carvedExecution.executionFlowGraphs.forEach(eg -> {
            if (!trigger.get()) {
                trigger.set(eg.getOrderedMethodInvocations().stream().filter(mi -> !mi.isStatic() //
                        && !mi.isExceptional() //
                        && mi.getOwner().isAndroidActivity()).count() > 0);
            }
        });

        return trigger.get();
    }

    private CarvedExecution introduceControllerAndGetTheActivity(CarvedExecution carvedExecution) {
        logger.debug("Introduce ActivityController and get the Activity");
        // We assume there are no more static constructors here !
        carvedExecution.executionFlowGraphs.forEach(eg -> {
            Optional<MethodInvocation> maybeActivityConstructor = eg.getOrderedMethodInvocations().stream()
                    .filter(mi -> mi.isConstructor() && mi.getOwner().isAndroidActivity()).findFirst();

            logger.debug(" FILTERED CALLS :" + eg.getOrderedMethodInvocations().stream().filter(mi -> mi.isConstructor() //
                    && !mi.isExceptional() //
                    && mi.getOwner().isAndroidActivity()).collect(Collectors.toSet()));

            // Introduce the controller first
            if (maybeActivityConstructor.isPresent()) {

                MethodInvocation activityConstructor = maybeActivityConstructor.get();
                ObjectInstance activity = activityConstructor.getOwner();

                DataDependencyGraph ddg = carvedExecution
                        .getDataDependencyGraphContainingTheMethodInvocation(activityConstructor);
                CallGraph cg = carvedExecution.getCallGraphContainingTheMethodInvocation(activityConstructor);

                // Replace the construction and the first subsuming call with the robolectric
                // calls
                List<MethodInvocation> subsumedMethodInvocations = new ArrayList<>(
                        cg.getMethodInvocationsSubsumedBy(activityConstructor));
                Collections.sort(subsumedMethodInvocations);

                // Constructor -> Replace with BuildActivity()
                robolectricActivityController = ObjectInstanceFactory
                        .get("org.robolectric.android.controller.ActivityController@123");

                String methodSignature = activity.requiresIntent()
                        ? "<org.robolectric.Robolectric: org.robolectric.android.controller.ActivityController buildActivity(java.lang.Class,android.content.Intent)>"
                        : "<org.robolectric.Robolectric: org.robolectric.android.controller.ActivityController buildActivity(java.lang.Class)>";

                // Create the call to generate the ActivityController
                int activityControllerGetID = uniqueID.getAndIncrement();
                int robolectricBuildActivityID = uniqueID.getAndIncrement();

                MethodInvocation robolectricBuildActivity = new MethodInvocation(
                        activityConstructor.getInvocationTraceId(), robolectricBuildActivityID, methodSignature);

                // Force this to be necessary
                robolectricBuildActivity.setNecessary(true);
                //
                robolectricBuildActivity.setStatic(true);

                robolectricBuildActivity.setReturnValue(robolectricActivityController);
                ddg.addDataDependencyOnReturn(robolectricBuildActivity, robolectricActivityController);
                //
                DataNode activityClass = PrimitiveNodeFactory.createClassLiteralFor(activity);
                robolectricBuildActivity.getActualParameterInstances().add(activityClass);
                ddg.addDataDependencyOnActualParameter(robolectricBuildActivity, activityClass, 0);
                //
                if (activity.requiresIntent()) {
                    ObjectInstance intent = activity.getIntent();
                    robolectricBuildActivity.getActualParameterInstances().add(intent);
                    ddg.addDataDependencyOnActualParameter(robolectricBuildActivity, intent, 1);
                }

                // Create the call to return Activity from the ActivityController
                methodSignature = "<org.robolectric.android.controller.ActivityController android.app.Activity get()>";

                // TODO Now sure about the IDs here !
                MethodInvocation activityControllerGet = new MethodInvocation(
                        activityConstructor.getInvocationTraceId(), activityControllerGetID, methodSignature);
                // Force this to be necessary
                activityControllerGet.setNecessary(true);
                //
                activityControllerGet.setPublic(true);

                activityControllerGet.setOwner(robolectricActivityController);
                ddg.addDataDependencyOnOwner(activityControllerGet, robolectricActivityController);

                activityControllerGet.setReturnValue(activity);
                ddg.addDataDependencyOnReturn(activityControllerGet, activity);

                // Wrap the activity constructor with the call to get()
                try {
                    // After this call, activityConstructor should not be a root object anymore...
                    wrapWith(activityConstructor, activityControllerGet, eg, cg);
                    // This seems problematic. We add the node here, we get the node if we call
                    // getVertices, but if we call contains this returns FALSE ?!

                    // Does this change the content of the variable?
                    eg.insertNodeRightBefore(robolectricBuildActivity, activityControllerGet);
                    // This breaks the code "sometimes" !?
                    cg.insertAsRoot(robolectricBuildActivity);
                } catch (ABCException e) {
                    throw new RuntimeException(e);
                }

                if (carvedExecution.methodInvocationUnderTest.equals(activityConstructor)) {
                    carvedExecution.isMethodInvocationUnderTestWrapped = true;
                }
            }
        });

        return carvedExecution;

    }

    // This modifies the input values
    private void wrapWith(MethodInvocation original, MethodInvocation wrappingMethodInvocation, //
            ExecutionFlowGraph eg, CallGraph cg) throws ABCException {
        logger.debug("Wrapping " + original + " with " + wrappingMethodInvocation);

        cg.wrapRootMethodInvocationWith(original, wrappingMethodInvocation);
        // Pay attention to the order, here we need to follow the meaning of the method
        // name ...
        eg.insertNodeRightBefore(wrappingMethodInvocation, original);
    }

    private CarvedExecution wrapOnCreateService(CarvedExecution carvedExecution) {
        carvedExecution.executionFlowGraphs.forEach(eg -> {
            // There is only one activity so onCreate is called once
            Optional<MethodInvocation> maybeOnCreate = eg.getOrderedMethodInvocations().stream()
                    .filter(mi -> !mi.isStatic() && mi.getOwner().isAndroidActivity()
                            && mi.getMethodSignature().contains("void onCreate(android.os.Bundle)"))
                    .findFirst();

            // Introduce the controller first
            if (maybeOnCreate.isPresent()) {
                // Original Method invocation to WRAP
                MethodInvocation onCreate = maybeOnCreate.get();

                DataDependencyGraph ddg = carvedExecution.getDataDependencyGraphContainingTheMethodInvocation(onCreate);
                CallGraph cg = carvedExecution.getCallGraphContainingTheMethodInvocation(onCreate);

                // Create the new method wrapping invocation
                int invocationTraceId = onCreate.getInvocationTraceId();
                int invocationCount = onCreate.getInvocationCount();
                String methodSignature = "<org.robolectric.android.controller.ActivityController org.robolectric.android.controller.ActivityController create()>";

                MethodInvocation activityControllerCreate = new MethodInvocation(invocationTraceId,
                        uniqueID.getAndIncrement(), methodSignature);
                // Force this to be necessary
                activityControllerCreate.setNecessary(true);
                //
                activityControllerCreate.setPublic(true);

                // The method invocation belongs to the controller
                activityControllerCreate.setOwner(robolectricActivityController);
                ddg.addDataDependencyOnOwner(activityControllerCreate, robolectricActivityController);
                // Fluent interface
                activityControllerCreate.setReturnValue(robolectricActivityController);
                ddg.addDataDependencyOnReturn(activityControllerCreate, robolectricActivityController);

                // Here we need to wrap the original call with the new one...
                try {
                    wrapWith(onCreate, activityControllerCreate, eg, cg);
                } catch (ABCException e) {
                    // TODO Probably we should use a more specific exception
                    throw new RuntimeException(e);
                }

                if (carvedExecution.methodInvocationUnderTest.equals(onCreate)) {
                    carvedExecution.isMethodInvocationUnderTestWrapped = true;
                }

            }
        });

        return carvedExecution;

    }

    private CarvedExecution wrapOnCreate(CarvedExecution carvedExecution) {
        carvedExecution.executionFlowGraphs.forEach(eg -> {
            // There is only one activity so onCreate is called once
            Optional<MethodInvocation> maybeOnCreate = eg.getOrderedMethodInvocations().stream()
                    .filter(mi -> !mi.isStatic() && mi.getOwner().isAndroidActivity()
                            && mi.getMethodSignature().contains("void onCreate(android.os.Bundle)"))
                    .findFirst();

            // Introduce the controller first
            if (maybeOnCreate.isPresent()) {
                // Original Method invocation to WRAP
                MethodInvocation onCreate = maybeOnCreate.get();

                DataDependencyGraph ddg = carvedExecution.getDataDependencyGraphContainingTheMethodInvocation(onCreate);
                CallGraph cg = carvedExecution.getCallGraphContainingTheMethodInvocation(onCreate);

                // Create the new method wrapping invocation
                int invocationTraceId = onCreate.getInvocationTraceId();
                int invocationCount = onCreate.getInvocationCount();
                String methodSignature = "<org.robolectric.android.controller.ActivityController org.robolectric.android.controller.ActivityController create()>";

                MethodInvocation activityControllerCreate = new MethodInvocation(invocationTraceId,
                        uniqueID.getAndIncrement(), methodSignature);
                // Force this to be necessary
                activityControllerCreate.setNecessary(true);
                //
                activityControllerCreate.setPublic(true);

                // The method invocation belongs to the controller
                activityControllerCreate.setOwner(robolectricActivityController);
                ddg.addDataDependencyOnOwner(activityControllerCreate, robolectricActivityController);
                // Fluent interface
                activityControllerCreate.setReturnValue(robolectricActivityController);
                ddg.addDataDependencyOnReturn(activityControllerCreate, robolectricActivityController);

                // Here we need to wrap the original call with the new one...
                try {
                    wrapWith(onCreate, activityControllerCreate, eg, cg);
                } catch (ABCException e) {
                    // TODO Probably we should use a more specific exception
                    throw new RuntimeException(e);
                }

                if (carvedExecution.methodInvocationUnderTest.equals(onCreate)) {
                    carvedExecution.isMethodInvocationUnderTestWrapped = true;
                }

            }
        });

        return carvedExecution;

    }

    // Feels like duplicated code here..
    private CarvedExecution wrapOnStart(CarvedExecution carvedExecution) {
        carvedExecution.executionFlowGraphs.forEach(eg -> {
            // There is only one activity so onCreate is called once
            Optional<MethodInvocation> maybeOnStart = eg.getOrderedMethodInvocations().stream()
                    .filter(mi -> !mi.isStatic() && !mi.isExceptional() //
                            && mi.getOwner().isAndroidActivity() && mi.getMethodSignature().contains("void onStart()"))
                    .findFirst();

            // Introduce the controller first
            if (maybeOnStart.isPresent()) {
                // Original Method invocation to WRAP
                MethodInvocation onStart = maybeOnStart.get();

                DataDependencyGraph ddg = carvedExecution.getDataDependencyGraphContainingTheMethodInvocation(onStart);
                CallGraph cg = carvedExecution.getCallGraphContainingTheMethodInvocation(onStart);

                // Create the new method wrapping invocation
                int invocationTraceId = onStart.getInvocationTraceId();
                String methodSignature = "<org.robolectric.android.controller.ActivityController org.robolectric.android.controller.ActivityController start()>";

                MethodInvocation activityControllerStart = new MethodInvocation(invocationTraceId,
                        uniqueID.getAndIncrement(), methodSignature);
                // Force this to be necessary
                activityControllerStart.setNecessary(true);
                //
                activityControllerStart.setPublic(true);

                // The method invocation belongs to the controller
                activityControllerStart.setOwner(robolectricActivityController);
                ddg.addDataDependencyOnOwner(activityControllerStart, robolectricActivityController);
                // Fluent interface
                activityControllerStart.setReturnValue(robolectricActivityController);
                ddg.addDataDependencyOnReturn(activityControllerStart, robolectricActivityController);

                // Here we need to wrap the original call with the new one...
                try {
                    wrapWith(onStart, activityControllerStart, eg, cg);
                } catch (ABCException e) {
                    // TODO Probably we should use a more specific exception
                    throw new RuntimeException(e);
                }

                if (carvedExecution.methodInvocationUnderTest.equals(onStart)) {
                    carvedExecution.isMethodInvocationUnderTestWrapped = true;
                }

            }
        });

        return carvedExecution;

    }

    private CarvedExecution wrapOnStop(CarvedExecution carvedExecution) {
        carvedExecution.executionFlowGraphs.forEach(eg -> {
            // There is only one activity so onCreate is called once
            Optional<MethodInvocation> maybeOnStop = eg.getOrderedMethodInvocations().stream()
                    .filter(mi -> !mi.isStatic() //
                            && !mi.isExceptional() //
                            && mi.getOwner().isAndroidActivity() //
                            && mi.getMethodSignature().contains("void onStop()"))
                    .findFirst();

            // Introduce the controller first
            if (maybeOnStop.isPresent()) {
                // Original Method invocation to WRAP
                MethodInvocation onStop = maybeOnStop.get();

                DataDependencyGraph ddg = carvedExecution.getDataDependencyGraphContainingTheMethodInvocation(onStop);
                CallGraph cg = carvedExecution.getCallGraphContainingTheMethodInvocation(onStop);

                // Create the new method wrapping invocation
                int invocationTraceId = onStop.getInvocationTraceId();
                String methodSignature = "<org.robolectric.android.controller.ActivityController org.robolectric.android.controller.ActivityController stop()>";

                MethodInvocation activityControllerStop = new MethodInvocation(invocationTraceId,
                        uniqueID.getAndIncrement(), methodSignature);
                // Force this to be necessary
                activityControllerStop.setNecessary(true);
                //
                activityControllerStop.setPublic(true);

                // The method invocation belongs to the controller
                activityControllerStop.setOwner(robolectricActivityController);
                ddg.addDataDependencyOnOwner(activityControllerStop, robolectricActivityController);
                // Fluent interface
                activityControllerStop.setReturnValue(robolectricActivityController);
                ddg.addDataDependencyOnReturn(activityControllerStop, robolectricActivityController);

                // Here we need to wrap the original call with the new one...
                try {
                    wrapWith(onStop, activityControllerStop, eg, cg);
                } catch (ABCException e) {
                    // TODO Probably we should use a more specific exception
                    throw new RuntimeException(e);
                }

                if (carvedExecution.methodInvocationUnderTest.equals(onStop)) {
                    carvedExecution.isMethodInvocationUnderTestWrapped = true;
                }

            }
        });

        return carvedExecution;

    }

    private CarvedExecution wrapOnResume(final CarvedExecution carvedExecution) {
        carvedExecution.executionFlowGraphs.forEach(eg -> {

            // OnResume Might be called multiple times, but shall avoid calls that are
            // subsumed here.
            final List<MethodInvocation> possiblyMoreThanOneOnResume = eg.getOrderedMethodInvocations().stream()
                    .filter(mi -> !mi.isStatic() //
                            && ! mi.isExceptional() //
                            && mi.getOwner().isAndroidActivity() //
                            && mi.getMethodSignature().contains("void onResume()")
            // We allow only root level android events, i.e., events directly triggered by
            // the framework
                            && carvedExecution.getCallGraphContainingTheMethodInvocation(mi).getRoots().contains(mi))
                    .collect(Collectors.toList());

            for (MethodInvocation onResume : possiblyMoreThanOneOnResume) {
                //
                DataDependencyGraph ddg = carvedExecution.getDataDependencyGraphContainingTheMethodInvocation(onResume);
                CallGraph cg = carvedExecution.getCallGraphContainingTheMethodInvocation(onResume);

                // Create the new method wrapping invocation
                int invocationTraceId = onResume.getInvocationTraceId();
                String methodSignature = "<org.robolectric.android.controller.ActivityController org.robolectric.android.controller.ActivityController resume()>";

                MethodInvocation activityControllerResume = new MethodInvocation(invocationTraceId,
                        uniqueID.getAndIncrement(), methodSignature);
                // Force this to be necessary
                activityControllerResume.setNecessary(true);
                activityControllerResume.setPublic(true);

                // The method invocation belongs to the controller
                activityControllerResume.setOwner(robolectricActivityController);
                ddg.addDataDependencyOnOwner(activityControllerResume, robolectricActivityController);
                // Fluent interface
                activityControllerResume.setReturnValue(robolectricActivityController);
                ddg.addDataDependencyOnReturn(activityControllerResume, robolectricActivityController);

                // Here we need to wrap the original call with the new one...
                try {
                    wrapWith(onResume, activityControllerResume, eg, cg);
                } catch (ABCException e) {
                    // TODO Probably we should use a more specific exception
                    throw new RuntimeException(e);
                }

                // In case this is indeed the method under test we need to mark it is wrapped
                // inside robolectric.
                // OR We need to be able to call the methods on it directly...
                if (carvedExecution.methodInvocationUnderTest.equals(onResume)) {
                    carvedExecution.isMethodInvocationUnderTestWrapped = true;
                }

            }
        });

        return carvedExecution;

    }

    private CarvedExecution wrapOnPause(final CarvedExecution carvedExecution) {
        carvedExecution.executionFlowGraphs.forEach(eg -> {

            // OnPause Might be called multiple times, but shall avoid calls that are
            // subsumed here.
            final List<MethodInvocation> possiblyMoreThanOneOnPause = eg.getOrderedMethodInvocations().stream()
                    .filter(mi -> !mi.isStatic() //
                            && ! mi.isExceptional() //
                            && mi.getOwner().isAndroidActivity() //
                            && mi.getMethodSignature().contains("void onPause()")
            // We allow only root level android events, i.e., events directly triggered by
            // the framework
                            && carvedExecution.getCallGraphContainingTheMethodInvocation(mi).getRoots().contains(mi))
                    .collect(Collectors.toList());

            for (MethodInvocation onPause : possiblyMoreThanOneOnPause) {
                //
                DataDependencyGraph ddg = carvedExecution.getDataDependencyGraphContainingTheMethodInvocation(onPause);
                CallGraph cg = carvedExecution.getCallGraphContainingTheMethodInvocation(onPause);

                // Create the new method wrapping invocation
                int invocationTraceId = onPause.getInvocationTraceId();
                String methodSignature = "<org.robolectric.android.controller.ActivityController org.robolectric.android.controller.ActivityController pause()>";

                MethodInvocation activityControllerPause = new MethodInvocation(invocationTraceId,
                        uniqueID.getAndIncrement(), methodSignature);
                // Force this to be necessary
                activityControllerPause.setNecessary(true);
                activityControllerPause.setPublic(true);

                // The method invocation belongs to the controller
                activityControllerPause.setOwner(robolectricActivityController);
                ddg.addDataDependencyOnOwner(activityControllerPause, robolectricActivityController);
                // Fluent interface
                activityControllerPause.setReturnValue(robolectricActivityController);
                ddg.addDataDependencyOnReturn(activityControllerPause, robolectricActivityController);

                // Here we need to wrap the original call with the new one...
                try {
                    wrapWith(onPause, activityControllerPause, eg, cg);
                } catch (ABCException e) {
                    // TODO Probably we should use a more specific exception
                    throw new RuntimeException(e);
                }

                // In case this is indeed the method under test we need to mark it is wrapped
                // inside robolectric.
                // OR We need to be able to call the methods on it directly...
                if (carvedExecution.methodInvocationUnderTest.equals(onPause)) {
                    carvedExecution.isMethodInvocationUnderTestWrapped = true;
                }

            }
        });

        return carvedExecution;

    }

    // https://stackoverflow.com/questions/61185996/how-to-unit-test-the-logic-in-the-ondestory-with-robolectric
    private CarvedExecution wrapOnDestroy(CarvedExecution carvedExecution) {
        carvedExecution.executionFlowGraphs.forEach(eg -> {
            // There is only one activity so onCreate is called once
            Optional<MethodInvocation> maybeOnDestroy = eg.getOrderedMethodInvocations().stream()
                    .filter(mi -> !mi.isStatic() //
                            && ! mi.isExceptional() //
                            && mi.getOwner().isAndroidActivity() //
                            && mi.getMethodSignature().contains("void onDestroy()"))
                    .findFirst();

            // Introduce the controller first
            if (maybeOnDestroy.isPresent()) {
                // Original Method invocation to WRAP
                MethodInvocation onDestroy = maybeOnDestroy.get();

                DataDependencyGraph ddg = carvedExecution
                        .getDataDependencyGraphContainingTheMethodInvocation(onDestroy);
                CallGraph cg = carvedExecution.getCallGraphContainingTheMethodInvocation(onDestroy);

                // Create the new method wrapping invocation
                int invocationTraceId = onDestroy.getInvocationTraceId();
                String methodSignature = "<org.robolectric.android.controller.ActivityController org.robolectric.android.controller.ActivityController destroy()>";

                MethodInvocation activityControllerDestroy = new MethodInvocation(invocationTraceId,
                        uniqueID.getAndIncrement(), methodSignature);
                // Force this to be necessary
                activityControllerDestroy.setNecessary(true);
                //
                activityControllerDestroy.setPublic(true);

                // The method invocation belongs to the controller
                activityControllerDestroy.setOwner(robolectricActivityController);
                ddg.addDataDependencyOnOwner(activityControllerDestroy, robolectricActivityController);
                // Fluent interface
                activityControllerDestroy.setReturnValue(robolectricActivityController);
                ddg.addDataDependencyOnReturn(activityControllerDestroy, robolectricActivityController);

                // Here we need to wrap the original call with the new one...
                try {
                    wrapWith(onDestroy, activityControllerDestroy, eg, cg);
                } catch (ABCException e) {
                    // TODO Probably we should use a more specific exception
                    throw new RuntimeException(e);
                }

                if (carvedExecution.methodInvocationUnderTest.equals(onDestroy)) {
                    carvedExecution.isMethodInvocationUnderTestWrapped = true;
                }

            }
        });

        return carvedExecution;

    }

    /*
     * Replaces the call to activity.onCreateOptionsMenu() to
     * activityController.visible() This requires that we introduce the controller
     * first ! TODO Note since onCreateOptionsMenu() must return true for the menu
     * to be displayed, if you return false it will not be shown. probably visible()
     * should be called ONLY when the return value of onCreateOptionsMenu() is true?
     * But how to test the opposite case?
     *
     *
     */
    private CarvedExecution wrapOnCreateOptionsMenu(CarvedExecution carvedExecution) {
        carvedExecution.executionFlowGraphs.forEach(eg -> {
            // TODO Shall we check that the activity is indeed the same?
            Optional<MethodInvocation> maybeOnCreateOptionsMenu = eg.getOrderedMethodInvocations().stream()
                    .filter(mi -> !mi.isStatic() //
                            && !mi.isExceptional() //
                            && mi.getOwner().isAndroidActivity() //
                            && mi.getMethodSignature().contains("onCreateOptionsMenu(android.view.Menu)"))
                    .findFirst();

            // Usually there's only one...
            if (maybeOnCreateOptionsMenu.isPresent()) {

                MethodInvocation onCreateOptionsMenu = maybeOnCreateOptionsMenu.get();
                DataDependencyGraph ddg = carvedExecution
                        .getDataDependencyGraphContainingTheMethodInvocation(onCreateOptionsMenu);
                CallGraph cg = carvedExecution.getCallGraphContainingTheMethodInvocation(onCreateOptionsMenu);

                int invocationTraceId = onCreateOptionsMenu.getInvocationTraceId();
                String methodSignature = "<org.robolectric.android.controller.ActivityController org.robolectric.android.controller.ActivityController visible()>";

                MethodInvocation activityControllerVisible = new MethodInvocation(invocationTraceId,
                        uniqueID.getAndIncrement(), methodSignature);

                activityControllerVisible.setNecessary(true);
                activityControllerVisible.setPublic(true);

                activityControllerVisible.setOwner(robolectricActivityController);
                ddg.addDataDependencyOnOwner(activityControllerVisible, robolectricActivityController);

                activityControllerVisible.setReturnValue(robolectricActivityController);
                ddg.addDataDependencyOnReturn(activityControllerVisible, robolectricActivityController);

                try {
                    wrapWith(onCreateOptionsMenu, activityControllerVisible, eg, cg);
                } catch (ABCException e) {
                    throw new RuntimeException(e);
                }

                if (carvedExecution.methodInvocationUnderTest.equals(onCreateOptionsMenu)) {
                    carvedExecution.isMethodInvocationUnderTestWrapped = true;
                }
            }
        });

        return carvedExecution;

    }

    /*
     * Replace the call to activity.onOptionsItemSelected(Menu); with
     * Shadows.shadowOf(activity).clickMenuItem(menu.R.id);
     * 
     * https://github.com/robolectric/robolectric/issues/1582
     * https://stackoverflow.com/questions/17486689/testing-a-cursorloader-with-
     * robolectric-mockito/21866892#21866892
     *
     */
    private CarvedExecution wrapOnOptionsItemSelected(CarvedExecution carvedExecution) {

        carvedExecution.executionFlowGraphs.forEach(eg -> {
            // TODO Shall we check that the activity is indeed the same?
            List<MethodInvocation> maybeOnOptionsItemSelected = eg.getOrderedMethodInvocations().stream()
                    .filter(mi -> !mi.isStatic() && !mi.isExceptional() //
                            && mi.getOwner().isAndroidActivity() //
                            && mi.getMethodSignature().contains("onOptionsItemSelected(android.view.MenuItem)"))
                    .collect(Collectors.toList());

            if (maybeOnOptionsItemSelected.size() > 0) {
                // Create the shadowActivity
                ObjectInstance shadowActivity = null;
                ObjectInstance activity = null;

                // This includes also the calls to super.onOptionsItemSelected. In that case, we
                // should skip!
                Map<ObjectInstance, MethodInvocation> ownerAndClass = new HashMap<ObjectInstance, MethodInvocation>();
                for (MethodInvocation onOptionsItemSelected : maybeOnOptionsItemSelected) {

                    // Same Owner, Same Method but different Type. We assume that they are sorted
                    // from the most specific to the most generic
                    if (ownerAndClass.containsKey(onOptionsItemSelected.getOwner()) && !JimpleUtils
                            .getClassNameForMethod(
                                    ownerAndClass.get(onOptionsItemSelected.getOwner()).getMethodSignature())
                            .equals(JimpleUtils.getClassNameForMethod(onOptionsItemSelected.getMethodSignature()))) {
                        logger.debug("Found a call to super type " + onOptionsItemSelected
                                + ". We skip it as we already processed "
                                + ownerAndClass.get(onOptionsItemSelected.getOwner()));
                        continue;
                    }

                    // It might happen that one method is not there anymore because it was removed
                    // by a previous execution as it was subsumed. In this case, we skip it.

                    DataDependencyGraph ddg = carvedExecution
                            .getDataDependencyGraphContainingTheMethodInvocation(onOptionsItemSelected);
                    CallGraph cg = carvedExecution.getCallGraphContainingTheMethodInvocation(onOptionsItemSelected);

                    if (ddg == null || cg == null) {
                        continue;
                    }

                    // Since we need to INJECT the call to create the ShadowActivity and we cannot
                    // inject new nodes in the exec flow graph we
                    // replace one of the call to remove instead

                    // Find the menuView ID buy looking at the findViewByIt methods that return it
                    ObjectInstance menuView = (ObjectInstance) onOptionsItemSelected.getActualParameterInstances()
                            .get(0);

                    Optional<MethodInvocation> findByIdReturningTheMenuView = findMenuViewId(carvedExecution, menuView);

                    if (!findByIdReturningTheMenuView.isPresent()) {
                        logger.warn("Cannot find the viewId of the menuView object " + menuView);
                        continue;
                    }

                    // Make sure the id are correct

                    // Prepare the call
                    int invocationTraceId = onOptionsItemSelected.getInvocationTraceId();

                    String methodSignature = "<org.robolectric.shadows.ShadowActivity boolean clickMenuItem(int)>";

                    MethodInvocation shadowActivityClickMenuItem = new MethodInvocation(invocationTraceId,
                            uniqueID.getAndIncrement(), methodSignature);

                    shadowActivityClickMenuItem.setNecessary(true);
                    shadowActivityClickMenuItem.setPublic(true);

                    // Setup the shadow activity if necessary
                    if (shadowActivity == null) {
                        // Since we sort method invocation inversely for the traceID we need to
                        // associate to this call the biggest ID among the two
                        // Replace the original call here
                        shadowActivity = ObjectInstanceFactory.get("org.robolectric.shadows.ShadowActivity@1234");
                        activity = onOptionsItemSelected.getOwner();

                        // Not 100% sure what will happen here !
                        int shadowActivityInvocationTraceId = onOptionsItemSelected.getInvocationTraceId();
                        methodSignature = "<org.robolectric.Shadows org.robolectric.shadows.ShadowActivity shadowOf(android.app.Activity)>";

                        MethodInvocation createShadowActivity = new MethodInvocation(shadowActivityInvocationTraceId,
                                uniqueID.getAndIncrement(), methodSignature);
                        //
                        createShadowActivity.setNecessary(true);
                        //
                        createShadowActivity.setStatic(true);
                        //
                        createShadowActivity.setReturnValue(shadowActivity);
                        ddg.addDataDependencyOnReturn(createShadowActivity, shadowActivity);
                        //
                        createShadowActivity.getActualParameterInstances().add(activity);
                        ddg.addDataDependencyOnActualParameter(createShadowActivity, activity, 0);

                        try {
                            cg.insertAsRoot(createShadowActivity);
                            eg.insertNodeRightBefore(createShadowActivity, onOptionsItemSelected);
                        } catch (ABCException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    // Fix the data deps
                    shadowActivityClickMenuItem.setOwner(shadowActivity);
                    ddg.addDataDependencyOnOwner(shadowActivityClickMenuItem, shadowActivity);
                    // Set the return value to the actual one
                    shadowActivityClickMenuItem.setReturnValue(onOptionsItemSelected.getReturnValue());
                    ddg.addDataDependencyOnReturn(shadowActivityClickMenuItem, onOptionsItemSelected.getReturnValue());
                    // Find the menuView ID buy looking at the findViewByIt methods that return it
                    // This is the first (and only parameter)
                    DataNode menuViewId = findByIdReturningTheMenuView.get().getActualParameterInstances().get(0);
                    //
                    shadowActivityClickMenuItem.getActualParameterInstances().add(menuViewId);
                    ddg.addDataDependencyOnActualParameter(shadowActivityClickMenuItem, menuViewId, 0);

                    // If a class calls super.onOptionsItemSelected we should not wrap it
                    try {
                        wrapWith(onOptionsItemSelected, shadowActivityClickMenuItem, eg, cg);
                    } catch (ABCException e) {
                        throw new RuntimeException(e);
                    }

                    if (carvedExecution.methodInvocationUnderTest.equals(onOptionsItemSelected)) {
                        carvedExecution.isMethodInvocationUnderTestWrapped = true;
                    }

                    // Make sure we cache this
                    ownerAndClass.put(onOptionsItemSelected.getOwner(), onOptionsItemSelected);
                }
            }
        });

        return carvedExecution;
    }

    // Look for the menuView in all the data dep graphs
    private Optional<MethodInvocation> findMenuViewId(CarvedExecution carvedExecution, ObjectInstance menuView) {
        DataDependencyGraph dataDependencyGraph = new DataDependencyGraphImpl();
        carvedExecution.dataDependencyGraphs.stream().forEach(ddg -> dataDependencyGraph.include(ddg));

        return dataDependencyGraph.getMethodInvocationsWhichReturn(menuView).stream()
                .filter(mi -> mi.getMethodSignature().contains("android.view.MenuItem findItem(int)")).findFirst();
    }

}
