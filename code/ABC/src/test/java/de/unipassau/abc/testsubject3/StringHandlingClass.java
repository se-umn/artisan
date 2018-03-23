package de.unipassau.abc.testsubject3;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class StringHandlingClass {

	public static void main(String[] args) throws IOException {

		// This works
		// String aString = "TEMP";
		// File tempFile = Files.createTempFile(aString, "BAR").toFile();
		// tempFile.deleteOnExit();
		// System.out.println("StringHandlingClass.main() The file is " +
		// tempFile.toString());
		// This works
		// String[] stringArray = new String[3];
		// stringArray[0] = "Array0";

		// Solved by instrumenting also InvokeStmt
		//
		// File usernameFile = new File("username.txt");
		// This not sure
		File workingDir = Files.createTempDirectory("TEMP").toFile();
		File usernameFile = new File(workingDir, "username.txt");
		usernameFile.createNewFile();

		String usernameFileContent = "123";
		// THis returns the same path provided as input
		Files.write(usernameFile.toPath(), usernameFileContent.getBytes());

	}
}
