package de.unipassau.testsubject;

public class DummySystemTestGetDoubleModifiedWithDelegate {

	public static void main(String[] args) {
		System.out.println("DummySystemTestGetDoubleModifiedWithDelegate.main()");
		DummyObject dummyObject = DummyObjectFactory.getDoubleModifiedWithDelegate();
		dummyObject.end();
	}
}
