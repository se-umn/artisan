package de.unipassau.abc.carving;

import java.util.List;
import java.util.Map;

import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.exceptions.ABCException;

/**
 * Method Carvers compute a list of carved trace executions, but not yet tests.
 * Each carved execution is represented in terms of the usual Triplette.
 * 
 * @author gambitemp
 *
 */
public interface MethodCarver {

    /**
     * Return all the carved tests for the given method invocation
     * 
     * @param methodInvocation
     * @return
     * @throws CarvingException
     * @throws ABCException
     */
    public List<CarvedExecution> carve(MethodInvocation methodInvocation) throws CarvingException, ABCException;

    /**
     * Return all the carved tests for all the given method invocations
     * 
     * @param methodInvocations
     * @return
     * @throws CarvingException
     */
    public Map<MethodInvocation, List<CarvedExecution>> carve(List<MethodInvocation> methodInvocations)
            throws CarvingException, ABCException;

}
