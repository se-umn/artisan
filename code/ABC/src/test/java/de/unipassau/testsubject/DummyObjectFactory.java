package de.unipassau.testsubject;

public class DummyObjectFactory {

	public static DummyObject getSimple() {
		return new DummyObject();
	}

	public static DummyObject getModified() {
		DummyObject dummyObject = new DummyObject();
		dummyObject.foo();
		return dummyObject;
	}

	public static DummyObject getSimpleWithDelegate() {
		DummyCreator dummyCreator = new DummyCreator();
		return dummyCreator.createNewDummySimple();
	}

	public static DummyObject getModifiedWithDelegate() {
		DummyCreator dummyCreator = new DummyCreator();
		return dummyCreator.createNewDummyModified();
	}

	public static DummyObject getDoubleModifiedWithDelegate() {
		DummyCreator dummyCreator = new DummyCreator();
		DummyObject dummyObject = dummyCreator.createNewDummyModified();
		dummyObject.bar();
		return dummyObject;
	}
	
	public static DummyObject getSimpleWithParameter(Fluffy fluffy) {
		return new DummyObject(fluffy);
	}
}
