package de.unipassau.abc.testsubject2;

import java.util.Arrays;

public class ArrayHandlingClass {

	public String handleStringArray() {
		StringBuffer buffer = new StringBuffer("buffer");
		return buffer.toString();
	}

	public void callMeMaybe(String[] theArray) {
		System.out.println(Arrays.toString(theArray));
		
		// Private method
		cannotCarveMe(theArray);
	}

	// This is a private method, we cannot carve it. And probably we should not be able to use it in the testing.
	private void cannotCarveMe(String[] theArray) {
		System.out.println(Arrays.toString(theArray));
	}

	// This 
	public static void main(String[] args) {
		

	}
}
