package de.unipassau.abc.testsubject;

public class JUnitAssertions {

	public void assertionWithCasting() {
		int A = 10;
		int B = 20;

		org.junit.Assert.assertEquals(A, B);
	}

	public int returnAnInt() {
		return 5;
	}

	public void assertionWithCastingAndReturnType() {
		int X = returnAnInt();

		org.junit.Assert.assertEquals(5, X);
	}
	
	public void methodInvocations(){
		int i = 10;
		Integer integer = Integer.valueOf( i );
		long l = integer.longValue();
		l = l + 5;
		System.out.println("l is " + l);
	}
}
