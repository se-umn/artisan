package de.unipassau.abc.testsubject2;

import java.util.Arrays;

public class ArrayHandlingClass {

	public String handleStringArray(){
		StringBuffer buffer = new StringBuffer("buffer");
		return buffer.toString();
	}

	public void callMeMaybe(String[] theArray) {
		System.out.println( Arrays.toString( theArray ) );
	}
	
	
	public static void main(String[] args) {
		ArrayHandlingClass c = new ArrayHandlingClass();
		String[] theArray = new String[10];
		
		
		theArray[0] = "Test";
		theArray[1] = "Foo";
		theArray[2] = "Bar";
		theArray[3] = c.handleStringArray();
		
		c.callMeMaybe( theArray );
	}
}
