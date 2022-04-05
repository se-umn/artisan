package de.unipassau.abc.generation.simplifiers;

import de.unipassau.abc.carving.BasicCarver;
import de.unipassau.abc.carving.CarvedExecution;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.exceptions.ABCException;

public abstract class AbstractCarvedExecutionSimplifier implements CarvedExecutionSimplifier {

    @Override
    public CarvedExecution simplify(CarvedExecution carvedExecution) throws CarvingException, ABCException {

        if (appliesTo(carvedExecution)) {
            carvedExecution = doSimplification(carvedExecution);

            BasicCarver carver = new BasicCarver(carvedExecution);
            // Recarving the original method is not always necessary !
            CarvedExecution reCarvedExecution = carver.recarve(carvedExecution.methodInvocationUnderTest).stream()
                    .findFirst().get();

            reCarvedExecution.traceId = carvedExecution.traceId;
            reCarvedExecution.isMethodInvocationUnderTestWrapped = carvedExecution.isMethodInvocationUnderTestWrapped;

            return reCarvedExecution;

        } else {
            return carvedExecution;
        }

    }

    /**
     * Checks whether this simplifier should be applied to the carved execution
     * 
     * @param carvedExecution
     * @return
     */
    public abstract boolean appliesTo(CarvedExecution carvedExecution);

    /**
     * Implement the actual simplification, with consequent recarving
     * 
     * @param carvedExecution
     * @return
     * @throws CarvingException
     * @throws ABCException
     */
    public abstract CarvedExecution doSimplification(CarvedExecution carvedExecution)
            throws CarvingException, ABCException;

    public CarvedExecution resetIsNecessaryTag(CarvedExecution carvedExecution) {
        carvedExecution.executionFlowGraphs
                .forEach(eg -> eg.getOrderedMethodInvocations().forEach(mi -> mi.setNecessary(false)));
        return carvedExecution;
    }

}
