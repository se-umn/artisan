package de.unipassau.abc.carving;

import java.util.List;

import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.exceptions.ABCException;

/**
 * Conceptually similar to Method Carver this interface identifies those objects
 * that can produce carved executions that, once executed, return an object
 * instance in the status observed at a given moment during the execution,
 * either cause the objectInstance was used as a parameter of the method
 * invocation or because it was returned by it
 * 
 * @author gambitemp
 *
 */
public interface ObjectCarver {

	// TODO Do we need to say which parameter this was? Why? multiple instances of
	// the same type used as parameter?
	public List<Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> carveAsParameter(
			ObjectInstance parameter, MethodInvocation methodInvocation) throws CarvingException, ABCException;

	public List<Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> carveAsReturnValue(
			ObjectInstance parameter, MethodInvocation methodInvocation) throws CarvingException, ABCException;

}
