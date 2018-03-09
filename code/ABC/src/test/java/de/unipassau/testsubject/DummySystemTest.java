package de.unipassau.testsubject;

public class DummySystemTest {

	public static void main(String[] args) {
		System.out.println("DummySystemTest.main()");
		
		DummyObjectFactory.getSimple();
		DummyObjectFactory.getModified();
		DummyObjectFactory.getSimpleWithDelegate();
		DummyObjectFactory.getModifiedWithDelegate();
		DummyObjectFactory.getDoubleModifiedWithDelegate();
	}
}
