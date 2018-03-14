package de.unipassau.abc.testsubject;

public class DummyCreator {

	public DummyObject createNewDummySimple(){
		return new DummyObject();
	}

	public DummyObject createNewDummyModified() {
		DummyObject dummyObject = new DummyObject();
		dummyObject.foo();
		return dummyObject;
	}
}

