package de.unipassau.abc.testsubject2;

import java.util.Arrays;

/**
 * In this class we cannot carve callMeMaybe because carved tests assume that
 * whatever call was made on the instance before MUT stays there in the carved
 * test. Since handleStringArray is called before callMeMaybe, we need to include it.
 * At the same time handleStringArray calls callMeMaybe, meaning it subsumes it. Therefore, there's no way
 * for us to call callMeMaybe in "ISOLATION"
 * 
 * @author gambi
 *
 */
public class ArrayHandlingClass2 {

	public void handleStringArray() {
		StringBuffer buffer = new StringBuffer("buffer");
		// Inside method <de.unipassau.abc.testsubject2.ArrayHandlingClass: void
		// handleStringArray()> caseAssignStmt() r2 = newarray
		// (java.lang.String)[10]
		String[] theArray = new String[10];

		theArray[0] = "Test";
		theArray[1] = "Foo";
		theArray[2] = "Bar";
		theArray[3] = buffer.toString();

		callMeMaybe(theArray);

	}

	public void callMeMaybe(String[] theArray) {
		System.out.println(Arrays.toString(theArray));
	}

	public static void main(String[] args) {
		ArrayHandlingClass2 c = new ArrayHandlingClass2();
		c.handleStringArray();
	}
}
