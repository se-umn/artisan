package de.unipassau.abc.testsubject;

public class DummySystemTestGetSimple {

	public static void main(String[] args) {
		System.out.println("DummySystemTestGetSimple.main()");
		DummyObject dummyObject = DummyObjectFactory.getSimple();
		dummyObject.end();
	}
}
