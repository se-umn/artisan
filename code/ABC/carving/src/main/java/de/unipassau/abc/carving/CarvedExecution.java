package de.unipassau.abc.carving;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.ObjectInstance;

/**
 * This class holds all the fragments extracted during the carving. For the
 * moment this is just a DTO
 * 
 * @author gambi
 *
 */
public class CarvedExecution {
    private static final Logger logger = LoggerFactory.getLogger(CarvedExecution.class);

    /*
     * This method invocation can be either the invocation carved or the context for
     * carving the object. If the object is not null, then it is the latter,
     * otherwise the former
     */
    public MethodInvocation methodInvocationUnderTest;
    // TODO Does it make sense to carve primitives?
    public ObjectInstance carvedObject;

    // Those elements do not have to be matched by position, since all of them are
    // needed to recreate the execution
    public Collection<ExecutionFlowGraph> executionFlowGraphs;
    public Collection<DataDependencyGraph> dataDependencyGraphs;
    public Collection<CallGraph> callGraphs;
    public String traceId;

    // It is possible that the MUT is replaced by a call to Robolectric, so it
    // cannot be found in the test literally.
    public boolean isMethodInvocationUnderTestWrapped;

    public CarvedExecution(String traceId) {
        this.executionFlowGraphs = new ArrayList<>();
        this.dataDependencyGraphs = new ArrayList<>();
        this.callGraphs = new ArrayList<>();
        this.traceId = traceId;
    }

    /**
     * Return the callGraph which contain the methodInvocationUnderTest or null
     *
     * @return
     */
    public CallGraph getCallGraphContainingTheMethodInvocationUnderTest() {
        try {
            return getCallGraphContainingTheMethodInvocation(this.methodInvocationUnderTest);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public CallGraph getCallGraphContainingTheMethodInvocation(MethodInvocation mi) {
        try {
            // https://stackoverflow.com/questions/22694884/filter-java-stream-to-1-and-only-1-element
            return callGraphs.stream().filter(cg -> cg.getAllMethodInvocations().contains(mi)).reduce((a, b) -> {
                // This should never happen anyway...
                throw new IllegalStateException("Multiple elements: " + a + ", " + b);
            }).get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public DataDependencyGraph getDataDependencyGraphContainingTheMethodInvocationUnderTest() {
        try {
            return this.getDataDependencyGraphContainingTheMethodInvocation(this.methodInvocationUnderTest);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public DataDependencyGraph getDataDependencyGraphContainingTheMethodInvocation(MethodInvocation mi) {
        try {
            return dataDependencyGraphs.stream().filter(ddg -> ddg.getAllMethodInvocations().contains(mi))
                    .reduce((a, b) -> {
                        throw new IllegalStateException("Multiple elements: " + a + ", " + b);
                    }).get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public ExecutionFlowGraph getExecutionFlowGraphContainingTheMethodInvocationUnderTest() {
        try {
            return this.getExecutionFlowGraphGraphContainingTheMethodInvocation(this.methodInvocationUnderTest);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public ExecutionFlowGraph getExecutionFlowGraphGraphContainingTheMethodInvocation(MethodInvocation mi) {
        try {
            return executionFlowGraphs.stream().filter(ddg -> ddg.getOrderedMethodInvocations().contains(mi))
                    .reduce((a, b) -> {
                        throw new IllegalStateException("Multiple elements: " + a + ", " + b);
                    }).get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Remove the method invocation and all the ones it subsumes
     * 
     * @param methodInvocationToRemove
     */
    public void removeTransitively(MethodInvocation methodInvocationToRemove) {
        logger.info("Removing transitively " + methodInvocationToRemove);
        getCallGraphContainingTheMethodInvocation(methodInvocationToRemove)
                .getMethodInvocationsSubsumedBy(methodInvocationToRemove).forEach(mtr -> {
                    remove(mtr);
                });
    }

    /**
     * This does not REMOVE transitively
     * 
     * @param methodInvocationToRemove
     */
    public void remove(MethodInvocation methodInvocationToRemove) {
        logger.debug("- Remove " + methodInvocationToRemove);

        // Null may happen because of transitive removal of subsumed nodes
        // We cannot simply remove the call from the call graph !
        if (getCallGraphContainingTheMethodInvocation(methodInvocationToRemove) != null) {
            getCallGraphContainingTheMethodInvocation(methodInvocationToRemove).replaceMethodInvocationWithExecution(methodInvocationToRemove);
//            getCallGraphContainingTheMethodInvocation(methodInvocationToRemove).remove(methodInvocationToRemove);
        }
        if (getDataDependencyGraphContainingTheMethodInvocation(methodInvocationToRemove) != null) {
            getDataDependencyGraphContainingTheMethodInvocation(methodInvocationToRemove)
                    .remove(methodInvocationToRemove);
        }
        if (getExecutionFlowGraphGraphContainingTheMethodInvocation(methodInvocationToRemove) != null) {
            getExecutionFlowGraphGraphContainingTheMethodInvocation(methodInvocationToRemove)
                    .remove(methodInvocationToRemove);
        }

    }

    public void printAll() {
        System.out.println("Execution Flow Graphs:");
        executionFlowGraphs.stream().forEach(efg -> efg.getOrderedMethodInvocations().forEach(System.out::println));
        System.out.println("Data Flow Graphs:");
        dataDependencyGraphs.stream().forEach(efg -> efg.getAllMethodInvocations().forEach(System.out::println));
        System.out.println("Call Graphs:");
        callGraphs.stream().forEach(efg -> efg.getAllMethodInvocations().forEach(System.out::println));

    }

    // Utility method to set the necessary tag for the listed methods. The others
    // are reset
    public void setNecessaryTagFor(final Collection<MethodInvocation> strictlyNecessaryMethods) {
        executionFlowGraphs.forEach(efg -> {
            efg.getOrderedMethodInvocations().stream().forEach(mi -> {
                if (mi.isNecessary()) {
//                    logger.info("Reset isNecessary for " + mi);
                    mi.setNecessary(false);
                }

                if (strictlyNecessaryMethods.contains(mi)) {
//                    logger.info("Restore isNecessary for " + mi);
                    mi.setNecessary(true);
                }
            });

        });
        callGraphs.forEach(cg -> {
            cg.getAllMethodInvocations().stream().forEach(mi -> {
                if (mi.isNecessary()) {
//                    logger.info("Reset isNecessary for " + mi);
                    mi.setNecessary(false);
                }

                if (strictlyNecessaryMethods.contains(mi)) {
//                    logger.info("Restore isNecessary for " + mi);
                    mi.setNecessary(true);
                }
            });

        });
        dataDependencyGraphs.forEach(ddg -> {
            ddg.getAllMethodInvocations().stream().forEach(mi -> {
                if (mi.isNecessary()) {
//                    logger.info("Reset isNecessary for " + mi);
                    mi.setNecessary(false);
                }

                if (strictlyNecessaryMethods.contains(mi)) {
//                    logger.info("Restore isNecessary for " + mi);
                    mi.setNecessary(true);
                }
            });

        });

    }

}
