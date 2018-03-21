package de.unipassau.abc.testsubject2;

import java.util.Arrays;

/**
 * This class is a corner case, as it uses in the main (the system test) private methods, that cannot be carved anyway
 * @author gambi
 *
 */
public class ArrayHandlingClass3 {

	public String handleStringArray() {
		StringBuffer buffer = new StringBuffer("buffer");
		return buffer.toString();
	}

	public void callMeMaybe(String[] theArray) {
		System.out.println(Arrays.toString(theArray));
	}

	// This is a private method, we cannot carve it. And probably we should not be able to use it in the testing.
	private void cannotCarveMe(String[] theArray) {
		System.out.println(Arrays.toString(theArray));
	}

	// This 
	public static void main(String[] args) {
		ArrayHandlingClass3 c = new ArrayHandlingClass3();
		String[] theArray = new String[10];

		theArray[0] = "Test";
		theArray[1] = "Foo";
		theArray[2] = "Bar";
		theArray[3] = c.handleStringArray();
		//
		c.cannotCarveMe(theArray);
		//
		c.callMeMaybe(theArray);

	}
}
