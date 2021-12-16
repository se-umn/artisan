package de.unipassau.abc.generation.utils;

import de.unipassau.abc.generation.data.CarvedTest;

public interface TestMethodNamer {

    /**
     * Generate a name for each test method in the given carved test

     */
    public String generateTestMethodName(CarvedTest carvedTest);
}
