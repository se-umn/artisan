package de.unipassau.abc.testsubject;

public class DummySystemTestGetSimpleWithNonRequiredParameter {

	public static void main(String[] args) {
		Goofy goofy = new Goofy();
		Fluffy fluffy = FluffyFactory.makeAFluffyWitGoofy( goofy );
		DummyObject dummyObject = DummyObjectFactory.getSimpleWithParameter(fluffy);
		dummyObject.end();
	}
}
