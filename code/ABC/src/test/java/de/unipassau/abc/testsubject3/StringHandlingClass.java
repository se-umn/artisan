package de.unipassau.abc.testsubject3;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class StringHandlingClass {

	public static void main(String[] args) throws IOException {
		String aString = "TEMP";
		/*
		 * If we invoke some method on aString, this might result in capturing
		 * without capturing its return value, we force the ASSIGN:
		 * aString.getBytes(). Otherwise the constant value are simply passed as parameter !
		 * 
		 * In case the ASSIGN is generated, then we track the INIT method for the string.
		 */
		//
		File tempFile = Files.createTempFile(aString, "BAR").toFile();
		tempFile.deleteOnExit();
		System.out.println("StringHandlingClass.main() The file is " + tempFile.toString());
		
		String bString = "Ciccio";
		bString.getBytes();

	}
}
