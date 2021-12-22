package de.unipassau.abc.evaluation;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.generation.data.CarvedTest;
import de.unipassau.abc.generation.utils.TestMethodNamer;

public class MethodUnderTestWithLocalCounterNamer implements TestMethodNamer {

    // Stores the mapping MUT, current inded
    private Map<String, AtomicInteger> mutMap = new HashMap();

    @Override
    public String generateTestMethodName(CarvedTest carvedTest) {

        String classUnderTestName = JimpleUtils
                .getClassNameForMethod(carvedTest.getMethodUnderTest().getMethodSignature());
        String simpleMethodName = JimpleUtils.getMethodName(carvedTest.getMethodUnderTest().getMethodSignature());

        String fqn = classUnderTestName + "." + simpleMethodName;

        if( carvedTest.getMethodUnderTest().isConstructor() ) {
            fqn = fqn.replace("<init>", "constructor");
        }
        
        if (!mutMap.containsKey(fqn)) {
            mutMap.put(fqn, new AtomicInteger(1));
        }
        
        // Ugly but safe ?!
        String methodName = fqn.replaceAll("\\.", "_");
        return String.format("test_%s_%03d", methodName, mutMap.get(fqn).getAndIncrement());
    }

}
