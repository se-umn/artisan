package de.unipassau.abc.generation.simplifiers;

import de.unipassau.abc.carving.BasicCarver;
import de.unipassau.abc.carving.CarvedExecution;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.DataNode;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.data.ObjectInstanceFactory;
import de.unipassau.abc.data.PrimitiveNodeFactory;
import de.unipassau.abc.exceptions.ABCException;
import edu.emory.mathcs.backport.java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RobolectricServiceSimplifier extends AbstractCarvedExecutionSimplifier {

    private static Logger logger = LoggerFactory.getLogger(RobolectricServiceSimplifier.class);

    private ObjectInstance robolectricServiceController;
    private AtomicInteger uniqueID = new AtomicInteger(20000);

    @Override
    public CarvedExecution simplify(CarvedExecution carvedExecution) throws ABCException {
        logger.info("Simplify using " + this.getClass());

        // This simplified should be used exclusion with ActivitySimplifier. I suspect
        // that only one of such elements can be handled by Robolectric...
//        carvedExecution = resetIsNecessaryTag(carvedExecution);

        carvedExecution = introduceControllerAndGetTheService(carvedExecution);
        carvedExecution = wrapOnCreate(carvedExecution);
        carvedExecution = wrapOnStartCommand(carvedExecution);
        carvedExecution = wrapOnBind(carvedExecution);
        carvedExecution = wrapOnUnbind(carvedExecution);
        carvedExecution = wrapOnRebind(carvedExecution);
        carvedExecution = wrapOnDestroy(carvedExecution);

        BasicCarver carver = new BasicCarver(carvedExecution);
        CarvedExecution reCarvedExecution = carver.recarve(carvedExecution.methodInvocationUnderTest).stream()
                .findFirst().get();

        reCarvedExecution.traceId = carvedExecution.traceId;
        reCarvedExecution.isMethodInvocationUnderTestWrapped = carvedExecution.isMethodInvocationUnderTestWrapped;

        return reCarvedExecution;
    }

    private CarvedExecution introduceControllerAndGetTheService(CarvedExecution carvedExecution) {
        logger.info("Introduce ServiceController and get the Service");

        carvedExecution.executionFlowGraphs.forEach(eg -> {
            Optional<MethodInvocation> maybeServiceConstructor = eg.getOrderedMethodInvocations().stream()
                    .filter(mi -> mi.isConstructor() && mi.getOwner().isAndroidService()).findFirst();
            if (maybeServiceConstructor.isPresent()) {
                MethodInvocation serviceConstructor = maybeServiceConstructor.get();
                ObjectInstance service = serviceConstructor.getOwner();

                DataDependencyGraph ddg = carvedExecution
                        .getDataDependencyGraphContainingTheMethodInvocation(serviceConstructor);
                CallGraph cg = carvedExecution.getCallGraphContainingTheMethodInvocation(serviceConstructor);
                List<MethodInvocation> subsumedMethodInvocations = new ArrayList<>(
                        cg.getMethodInvocationsSubsumedBy(serviceConstructor));
                Collections.sort(subsumedMethodInvocations);

                // Constructor -> Replace with Robolectric.buildService()
                robolectricServiceController = ObjectInstanceFactory
                        .get("org.robolectric.android.controller.ServiceController@321");
                String methodSignature = service.requiresIntent()
                        ? "<org.robolectric.Robolectric: org.robolectric.android.controller.ServiceController buildService(java.lang.Class,android.content.Intent)>"
                        : "<org.robolectric.Robolectric: org.robolectric.android.controller.ServiceController buildService(java.lang.Class)>";

                int serviceControllerGetID = uniqueID.getAndIncrement();
                int robolectricBuildServiceID = uniqueID.getAndIncrement();

                MethodInvocation robolectricBuildService = new MethodInvocation(
                        serviceConstructor.getInvocationTraceId(), robolectricBuildServiceID, methodSignature);

                robolectricBuildService.setNecessary(true);
                robolectricBuildService.setStatic(true);
                robolectricBuildService.setReturnValue(robolectricServiceController);
                ddg.addDataDependencyOnReturn(robolectricBuildService, robolectricServiceController);
                DataNode serviceClass = PrimitiveNodeFactory.createClassLiteralFor(service);
                robolectricBuildService.getActualParameterInstances().add(serviceClass);
                ddg.addDataDependencyOnActualParameter(robolectricBuildService, serviceClass, 0);

                if (service.requiresIntent()) {
                    ObjectInstance intent = service.getIntent();
                    robolectricBuildService.getActualParameterInstances().add(intent);
                    ddg.addDataDependencyOnActualParameter(robolectricBuildService, intent, 0);
                }

                methodSignature = "<org.robolectric.android.controller.ServiceController android.app.Service get()>";
                MethodInvocation serviceControllerGet = new MethodInvocation(serviceConstructor.getInvocationTraceId(),
                        serviceControllerGetID, methodSignature);

                serviceControllerGet.setNecessary(true);
                serviceControllerGet.setPublic(true);

                serviceControllerGet.setOwner(robolectricServiceController);
                ddg.addDataDependencyOnOwner(serviceControllerGet, robolectricServiceController);

                serviceControllerGet.setReturnValue(service);
                ddg.addDataDependencyOnReturn(serviceControllerGet, service);

                // Wrap the activity constructor with the call to get()
                try {
                    // After this call, activityConstructor should not be a root object anymore...
                    wrapWith(serviceConstructor, serviceControllerGet, eg, cg);
                    // This seems problematic. We add the node here, we get the node if we call
                    // getVertices, but if we call contains this returns FALSE ?!

                    // Does this change the content of the variable?
                    eg.insertNodeRightBefore(robolectricBuildService, serviceControllerGet);
                    // This breaks the code "sometimes" !?
                    cg.insertAsRoot(robolectricBuildService);
                } catch (ABCException e) {
                    throw new RuntimeException(e);
                }

                if (carvedExecution.methodInvocationUnderTest.equals(serviceConstructor)) {
                    carvedExecution.isMethodInvocationUnderTestWrapped = true;
                }
            }
        });

        return carvedExecution;
    }

    private CarvedExecution wrapOnCreate(CarvedExecution carvedExecution) {
        carvedExecution.executionFlowGraphs.forEach(eg -> {
            // There is only one activity so onCreate is called once
            Optional<MethodInvocation> maybeOnCreate = eg
                    .getOrderedMethodInvocations().stream().filter(mi -> !mi.isStatic()
                            && mi.getOwner().isAndroidService() && mi.getMethodSignature().contains("void onCreate()"))
                    .findFirst();

            // Introduce the controller first
            if (maybeOnCreate.isPresent()) {
                // Original Method invocation to WRAP
                MethodInvocation onCreate = maybeOnCreate.get();

                DataDependencyGraph ddg = carvedExecution.getDataDependencyGraphContainingTheMethodInvocation(onCreate);
                CallGraph cg = carvedExecution.getCallGraphContainingTheMethodInvocation(onCreate);

                // Create the new method wrapping invocation
                int invocationTraceId = onCreate.getInvocationTraceId();
                String methodSignature = "<org.robolectric.android.controller.ServiceController org.robolectric.android.controller.ServiceController create()>";

                MethodInvocation serviceControllerCreate = new MethodInvocation(invocationTraceId,
                        uniqueID.getAndIncrement(), methodSignature);
                // Force this to be necessary
                serviceControllerCreate.setNecessary(true);
                //
                serviceControllerCreate.setPublic(true);

                // The method invocation belongs to the controller
                serviceControllerCreate.setOwner(robolectricServiceController);
                ddg.addDataDependencyOnOwner(serviceControllerCreate, robolectricServiceController);
                // Fluent interface
                serviceControllerCreate.setReturnValue(robolectricServiceController);
                ddg.addDataDependencyOnReturn(serviceControllerCreate, robolectricServiceController);

                // Here we need to wrap the original call with the new one...
                try {
                    wrapWith(onCreate, serviceControllerCreate, eg, cg);
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

    private CarvedExecution wrapOnStartCommand(CarvedExecution carvedExecution) {
        carvedExecution.executionFlowGraphs.forEach(eg -> {
            Optional<MethodInvocation> maybeOnStartCommand = eg.getOrderedMethodInvocations().stream()
                    .filter(mi -> !mi.isStatic() && mi.getOwner().isAndroidService()
                            && mi.getMethodSignature().contains("int onStartCommand(android.content.Intent,int,int)"))
                    .findFirst();

            if (maybeOnStartCommand.isPresent()) {
                MethodInvocation onStartCommand = maybeOnStartCommand.get();
                DataDependencyGraph ddg = carvedExecution
                        .getDataDependencyGraphContainingTheMethodInvocation(onStartCommand);
                CallGraph cg = carvedExecution.getCallGraphContainingTheMethodInvocation(onStartCommand);

                int invocationTraceId = onStartCommand.getInvocationTraceId();
                String methodSignature = "<org.robolectric.android.controller.ServiceController org.robolectric.android.controller.ServiceController startCommand(int,int)>";

                MethodInvocation serviceControllerStartCommand = new MethodInvocation(invocationTraceId,
                        uniqueID.getAndIncrement(), methodSignature);
                serviceControllerStartCommand.setNecessary(true);
                serviceControllerStartCommand.setPublic(true);

                serviceControllerStartCommand.setOwner(robolectricServiceController);
                ddg.addDataDependencyOnOwner(serviceControllerStartCommand, robolectricServiceController);

                serviceControllerStartCommand.setReturnValue(robolectricServiceController);
                ddg.addDataDependencyOnReturn(serviceControllerStartCommand, robolectricServiceController);

                // Skip the first param
                List<DataNode> params = onStartCommand.getActualParameterInstances().subList(1, 3);

                serviceControllerStartCommand.getActualParameterInstances().addAll(params);
                IntStream.range(0, params.size()).forEach(
                        i -> ddg.addDataDependencyOnActualParameter(serviceControllerStartCommand, params.get(i), i));

                try {
                    wrapWith(onStartCommand, serviceControllerStartCommand, eg, cg);
                } catch (ABCException e) {
                    // TODO Probably we should use a more specific exception
                    throw new RuntimeException(e);
                }

                if (carvedExecution.methodInvocationUnderTest.equals(onStartCommand)) {
                    carvedExecution.isMethodInvocationUnderTestWrapped = true;
                }
            }
        });

        return carvedExecution;
    }

    private CarvedExecution wrapOnBind(CarvedExecution carvedExecution) {
        carvedExecution.executionFlowGraphs.forEach(eg -> {
            Optional<MethodInvocation> maybeOnBind = eg.getOrderedMethodInvocations().stream()
                    .filter(mi -> !mi.isStatic() && mi.getOwner().isAndroidService()
                            && mi.getMethodSignature().contains("android.os.IBinder onBind(android.content.Intent)"))
                    .findFirst();

            if (maybeOnBind.isPresent()) {
                MethodInvocation onBind = maybeOnBind.get();

                DataDependencyGraph ddg = carvedExecution.getDataDependencyGraphContainingTheMethodInvocation(onBind);
                CallGraph cg = carvedExecution.getCallGraphContainingTheMethodInvocation(onBind);

                int invocationTraceId = onBind.getInvocationTraceId();
                String methodSignature = "<org.robolectric.android.controller.ServiceController org.robolectric.android.controller.ServiceController bind()>";
                MethodInvocation serviceControllerBind = new MethodInvocation(invocationTraceId,
                        uniqueID.getAndIncrement(), methodSignature);
                serviceControllerBind.setNecessary(true);
                serviceControllerBind.setPublic(true);

                serviceControllerBind.setOwner(robolectricServiceController);
                ddg.addDataDependencyOnOwner(serviceControllerBind, robolectricServiceController);

                serviceControllerBind.setReturnValue(robolectricServiceController);
                ddg.addDataDependencyOnReturn(serviceControllerBind, robolectricServiceController);

                try {
                    wrapWith(onBind, serviceControllerBind, eg, cg);
                } catch (ABCException e) {
                    // TODO Probably we should use a more specific exception
                    throw new RuntimeException(e);
                }

                if (carvedExecution.methodInvocationUnderTest.equals(onBind)) {
                    carvedExecution.isMethodInvocationUnderTestWrapped = true;
                }
            }
        });

        return carvedExecution;
    }

    private CarvedExecution wrapOnRebind(CarvedExecution carvedExecution) {
        carvedExecution.executionFlowGraphs.forEach(eg -> {
            Optional<MethodInvocation> maybeOnRebind = eg.getOrderedMethodInvocations().stream()
                    .filter(mi -> !mi.isStatic() && mi.getOwner().isAndroidService()
                            && mi.getMethodSignature().contains("void onRebind(android.content.Intent)"))
                    .findFirst();

            if (maybeOnRebind.isPresent()) {
                MethodInvocation onRebind = maybeOnRebind.get();

                DataDependencyGraph ddg = carvedExecution.getDataDependencyGraphContainingTheMethodInvocation(onRebind);
                CallGraph cg = carvedExecution.getCallGraphContainingTheMethodInvocation(onRebind);

                int invocationTraceId = onRebind.getInvocationTraceId();
                String methodSignature = "<org.robolectric.android.controller.ServiceController org.robolectric.android.controller.ServiceController rebind()>";

                MethodInvocation serviceControllerRebind = new MethodInvocation(invocationTraceId,
                        uniqueID.getAndIncrement(), methodSignature);

                serviceControllerRebind.setNecessary(true);
                serviceControllerRebind.setPublic(true);

                serviceControllerRebind.setOwner(robolectricServiceController);
                ddg.addDataDependencyOnOwner(serviceControllerRebind, robolectricServiceController);

                serviceControllerRebind.setReturnValue(robolectricServiceController);
                ddg.addDataDependencyOnReturn(serviceControllerRebind, robolectricServiceController);

                try {
                    wrapWith(onRebind, serviceControllerRebind, eg, cg);
                } catch (ABCException e) {
                    // TODO Probably we should use a more specific exception
                    throw new RuntimeException(e);
                }

                if (carvedExecution.methodInvocationUnderTest.equals(onRebind)) {
                    carvedExecution.isMethodInvocationUnderTestWrapped = true;
                }
            }
        });

        return carvedExecution;
    }

    private CarvedExecution wrapOnUnbind(CarvedExecution carvedExecution) {
        carvedExecution.executionFlowGraphs.forEach(eg -> {
            Optional<MethodInvocation> maybeOnUnbind = eg.getOrderedMethodInvocations().stream()
                    .filter(mi -> !mi.isStatic() && mi.getOwner().isAndroidService()
                            && mi.getMethodSignature().contains("boolean onUnbind(android.content.Intent)"))
                    .findFirst();

            if (maybeOnUnbind.isPresent()) {
                MethodInvocation onUnbind = maybeOnUnbind.get();
                DataDependencyGraph ddg = carvedExecution.getDataDependencyGraphContainingTheMethodInvocation(onUnbind);
                CallGraph cg = carvedExecution.getCallGraphContainingTheMethodInvocation(onUnbind);

                int invocationTraceId = onUnbind.getInvocationTraceId();
                String methodSignature = "<org.robolectric.android.controller.ServiceController org.robolectric.android.controller.ServiceController unbind()>";

                MethodInvocation serviceControllerUnbind = new MethodInvocation(invocationTraceId,
                        uniqueID.getAndIncrement(), methodSignature);
                serviceControllerUnbind.setNecessary(true);
                serviceControllerUnbind.setPublic(true);

                serviceControllerUnbind.setOwner(robolectricServiceController);
                ddg.addDataDependencyOnOwner(serviceControllerUnbind, robolectricServiceController);

                serviceControllerUnbind.setReturnValue(robolectricServiceController);
                ddg.addDataDependencyOnReturn(serviceControllerUnbind, robolectricServiceController);

                try {
                    wrapWith(onUnbind, serviceControllerUnbind, eg, cg);
                } catch (ABCException e) {
                    // TODO Probably we should use a more specific exception
                    throw new RuntimeException(e);
                }

                if (carvedExecution.methodInvocationUnderTest.equals(onUnbind)) {
                    carvedExecution.isMethodInvocationUnderTestWrapped = true;
                }
            }
        });

        return carvedExecution;
    }

    private CarvedExecution wrapOnDestroy(CarvedExecution carvedExecution) {
        carvedExecution.executionFlowGraphs.forEach(eg -> {
            Optional<MethodInvocation> maybeOnDestroy = eg
                    .getOrderedMethodInvocations().stream().filter(mi -> !mi.isStatic()
                            && mi.getOwner().isAndroidService() && mi.getMethodSignature().contains("void onDestroy()"))
                    .findFirst();
            if (maybeOnDestroy.isPresent()) {
                MethodInvocation onDestroy = maybeOnDestroy.get();

                DataDependencyGraph ddg = carvedExecution
                        .getDataDependencyGraphContainingTheMethodInvocation(onDestroy);
                CallGraph cg = carvedExecution.getCallGraphContainingTheMethodInvocation(onDestroy);

                int invocationTraceId = onDestroy.getInvocationTraceId();
                String methodSignature = "<org.robolectric.android.controller.ServiceController org.robolectric.android.controller.ServiceController destroy()>";
                MethodInvocation serviceControllerDestroy = new MethodInvocation(invocationTraceId,
                        uniqueID.getAndIncrement(), methodSignature);
                serviceControllerDestroy.setNecessary(true);
                serviceControllerDestroy.setPublic(true);

                serviceControllerDestroy.setOwner(robolectricServiceController);
                ddg.addDataDependencyOnOwner(serviceControllerDestroy, robolectricServiceController);

                serviceControllerDestroy.setReturnValue(robolectricServiceController);
                ddg.addDataDependencyOnReturn(serviceControllerDestroy, robolectricServiceController);

                try {
                    wrapWith(onDestroy, serviceControllerDestroy, eg, cg);
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

    // This modifies the input values
    private void wrapWith(MethodInvocation original, MethodInvocation wrappingMethodInvocation, //
            ExecutionFlowGraph eg, CallGraph cg) throws ABCException {
        logger.info("Wrapping " + original + " with " + wrappingMethodInvocation);

        cg.wrapRootMethodInvocationWith(original, wrappingMethodInvocation);
        // Pay attention to the order, here we need to follow the meaning of the method
        // name ...
        eg.insertNodeRightBefore(wrappingMethodInvocation, original);
    }

}
