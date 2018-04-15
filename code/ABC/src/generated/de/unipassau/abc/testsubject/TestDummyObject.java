package de.unipassau.abc.testsubject;

import org.junit.Test;

public class TestDummyObject {

    @Test()
    public void test_1() {
        de.unipassau.abc.testsubject.DummyObject deunipassauabctestsubjectdummyObject00;
        deunipassauabctestsubjectdummyObject00 = new de.unipassau.abc.testsubject.DummyObject();
        deunipassauabctestsubjectdummyObject00.end();
    }

    @Test()
    public void test_2() {
        de.unipassau.abc.testsubject.DummyObject deunipassauabctestsubjectdummyObject00;
        deunipassauabctestsubjectdummyObject00 = de.unipassau.abc.testsubject.DummyObjectFactory.getSimple();
        deunipassauabctestsubjectdummyObject00.end();
    }
}