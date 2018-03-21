package de.unipassau.abc.testsubject2;

public class Main {

	public static void main(String[] args) {
		ArrayHandlingClass c = new ArrayHandlingClass();
		String[] theArray = new String[10];

		theArray[0] = "Test";
		theArray[1] = "Foo";
		theArray[2] = "Bar";
		theArray[3] = c.handleStringArray();
		//
		c.callMeMaybe(theArray);
	}
}
