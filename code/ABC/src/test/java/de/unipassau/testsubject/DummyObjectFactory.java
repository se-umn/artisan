package de.unipassau.testsubject;

public class DummyObjectFactory {

	public static DummyObject getSimple() {
		
		System.out.println("DummyObjectFactory.getSimple()");
		
		return new DummyObject();
	}

	public static DummyObject getModified() {
		System.out.println("DummyObjectFactory.getModified()");
		DummyObject dummyObject = new DummyObject();
		dummyObject.foo();
		return dummyObject;
	}

	public static DummyObject getSimpleWithDelegate() {
		System.out.println("DummyObjectFactory.getSimpleWithDelegate()");
		DummyCreator dummyCreator = new DummyCreator();
		return dummyCreator.createNewDummySimple();
	}

	public static DummyObject getModifiedWithDelegate() {
		System.out.println("DummyObjectFactory.getModifiedWithDelegate()");
		DummyCreator dummyCreator = new DummyCreator();
		return dummyCreator.createNewDummyModified();
	}

	public static DummyObject getDoubleModifiedWithDelegate() {
		System.out.println("DummyObjectFactory.getDoubleModifiedWithDelegate()");
		DummyCreator dummyCreator = new DummyCreator();
		DummyObject dummyObject = dummyCreator.createNewDummyModified();
		dummyObject.bar();
		return dummyObject;
	}
}
