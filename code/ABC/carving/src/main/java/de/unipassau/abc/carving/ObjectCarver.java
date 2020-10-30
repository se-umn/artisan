package de.unipassau.abc.carving;

import java.util.List;

import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.ObjectInstance;
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

	public List<CarvedExecution> carveAsParameter(ObjectInstance parameter, MethodInvocation methodInvocation)
			throws CarvingException, ABCException;

	public List<CarvedExecution> carveAsReturnValue(ObjectInstance parameter, MethodInvocation methodInvocation)
			throws CarvingException, ABCException;

}
