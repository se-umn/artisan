package de.unipassau.abc.data;

import edu.emory.mathcs.backport.java.util.concurrent.atomic.AtomicInteger;

public class ImplicitDependencyOnStaticClass extends ObjectInstance {

    private static AtomicInteger id = new AtomicInteger(0);

    private String className;

    public ImplicitDependencyOnStaticClass(String className) {
//        abc.basiccalculator.UiStorage@0
        // TODO Generate Automatically. I do not like how this is stored In the name...
        super(className + "@" + 0);
        this.className = className;
    }

    public String getStaticClassName() {
        return this.className;
    }

}
