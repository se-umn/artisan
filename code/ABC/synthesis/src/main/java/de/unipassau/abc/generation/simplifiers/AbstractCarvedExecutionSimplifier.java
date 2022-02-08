package de.unipassau.abc.generation.simplifiers;

import de.unipassau.abc.carving.CarvedExecution;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.exceptions.ABCException;

public abstract class AbstractCarvedExecutionSimplifier implements CarvedExecutionSimplifier {

    @Override
    public abstract CarvedExecution simplify(CarvedExecution carvedExecution)
          throws CarvingException, ABCException;

    public CarvedExecution resetIsNecessaryTag(CarvedExecution carvedExecution) {
        carvedExecution.executionFlowGraphs.forEach(
              eg -> eg.getOrderedMethodInvocations().forEach(mi -> mi.setNecessary(false)));
        return carvedExecution;
    }

}
