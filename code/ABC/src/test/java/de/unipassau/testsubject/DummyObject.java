package de.unipassau.testsubject;

public class DummyObject {

	private boolean bar = false;
	private boolean foo = false;

	private Fluffy fluffy;

	public DummyObject(Fluffy fluffy) {
		this.fluffy = fluffy;
	}

	public DummyObject() {
	}

	public Fluffy getFluffy() {
		return fluffy;
	}

	public boolean isBar() {
		return bar;
	}

	public boolean isFoo() {
		return foo;
	}

	public void foo() {
		foo = true;
	}

	public void bar() {
		bar = true;
	}

	@Override
	public String toString() {
		return super.toString() + " Foo " + foo + ", Bar " + bar;
	}

	public void end() {
		System.out.println("DummyObject.end()");
	}
}
