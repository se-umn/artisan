package de.unipassau.testsubject;

public class DummySystemTestGetModified {

	public static void main(String[] args) {
		System.out.println("DummySystemTestGetModified.main()");
		DummyObject dummyObject = DummyObjectFactory.getModified();
		dummyObject.end();
	}
}
