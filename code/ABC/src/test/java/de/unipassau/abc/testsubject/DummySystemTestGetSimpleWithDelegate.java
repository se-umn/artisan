package de.unipassau.abc.testsubject;

public class DummySystemTestGetSimpleWithDelegate {

	public static void main(String[] args) {
		System.out.println("DummySystemTestGetSimpleWithDelegate.main()");
		DummyObject dummyObject = DummyObjectFactory.getSimpleWithDelegate();
		dummyObject.end();
	}
}
