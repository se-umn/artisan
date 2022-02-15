package de.unipassau.abc.generation.simplifiers;

import de.unipassau.abc.carving.CarvedExecution;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.exceptions.ABCException;

public interface CarvedExecutionSimplifier {

    /**
     * Returns a NEW carved execution obtained by simplifying the original one if
     * need. May return the original carvedExecution if simplification does not
     * apply.
     * 
     * NOTE: we work at the level of carved execution so we do not have mocks and
     * assertions in the way, and have the complete picture of the slice. If we work
     * at the level of carvedTest we would miss many things (due to method
     * subsumption)
     * 
     * @param carvedExecution
     * @return
     * @throws ABCException
     * @throws CarvingException
     */
    public CarvedExecution simplify(CarvedExecution carvedExecution) throws CarvingException, ABCException;

}
