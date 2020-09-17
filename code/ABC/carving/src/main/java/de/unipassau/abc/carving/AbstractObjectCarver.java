package de.unipassau.abc.carving;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.exceptions.ABCException;

/**
 * Simple class to ensure basic invariants on ObjectCarving hold.
 * 
 * @author gambitemp
 *
 */
public abstract class AbstractObjectCarver implements ObjectCarver {

	private final Logger logger = LoggerFactory.getLogger(AbstractObjectCarver.class);

	@Override
	public List<CarvedExecution> carveAsParameter(ObjectInstance parameter, MethodInvocation methodInvocation)
			throws CarvingException, ABCException {

		List<CarvedExecution> carvingResult = new ArrayList<CarvedExecution>();

		if (!methodInvocation.getActualParameterInstances().contains(parameter)) {
			logger.warn("The method " + methodInvocation + " does not contain object " + parameter + "as parameter");
			return carvingResult;
		}

		return carveAsParameterAfterValidation(parameter, methodInvocation);
	}

	@Override
	public List<CarvedExecution> carveAsReturnValue(ObjectInstance parameter, MethodInvocation methodInvocation)
			throws CarvingException, ABCException {

		List<CarvedExecution> carvingResult = new ArrayList<CarvedExecution>();

		if (!methodInvocation.getReturnValue().equals(parameter)) {
			logger.warn("The method " + methodInvocation + " does not return object " + parameter);
			return carvingResult;
		}

		return carveAsReturnValueAfterValidation(parameter, methodInvocation);
	}

	public abstract List<CarvedExecution> carveAsParameterAfterValidation(ObjectInstance parameter,
			MethodInvocation methodInvocation) throws CarvingException, ABCException;

	public abstract List<CarvedExecution> carveAsReturnValueAfterValidation(ObjectInstance parameter,
			MethodInvocation methodInvocation) throws CarvingException, ABCException;

}
