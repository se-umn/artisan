package de.unipassau.testsubject;

public class DummySystemTestGetModifiedWithDelegate {

	public static void main(String[] args) {
		System.out.println("DummySystemTestGetModifiedWithDelegate.main()");
		DummyObject dummyObject = DummyObjectFactory.getModifiedWithDelegate();
		dummyObject.end();
	}
}
