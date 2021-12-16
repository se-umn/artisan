package de.unipassau.abc.evaluation;

import java.util.concurrent.atomic.AtomicInteger;

import de.unipassau.abc.generation.data.CarvedTest;
import de.unipassau.abc.generation.utils.TestClass;
import de.unipassau.abc.generation.utils.TestMethodNamer;

public class LocalCounterNamer implements TestMethodNamer {

    private AtomicInteger localCounter = new AtomicInteger(1);

    @Override
    public String generateTestMethodName(CarvedTest carvedTest) {
        return String.format("test%03d" , localCounter.getAndIncrement());
    }

}
