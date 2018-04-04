package de.unipassau.abc.testsubject4;

import java.io.IOException;

public class ClassAccess {

	public static void main(String[] args) throws IOException {


		String name = String.class.getName();
		System.out.println("Class name " + name);
	}
}
