package de.unipassau.testsubject;

public class DummySystemTestGetSimpleWithParameter {

	public static void main(String[] args) {
		System.out.println("DummySystemTestGetSimpleWithParameter.main()");
		Fluffy fluffy = FluffyFactory.makeAFluffy();
		DummyObject dummyObject = DummyObjectFactory.getSimpleWithParameter(fluffy);
		dummyObject.end();
	}
}
