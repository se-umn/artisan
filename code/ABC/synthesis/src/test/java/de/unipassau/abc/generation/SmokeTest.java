package de.unipassau.abc.generation;

import java.io.IOException;

import org.junit.Test;

import de.unipassau.abc.evaluation.Main;
import de.unipassau.abc.exceptions.ABCException;

public class SmokeTest {

	/*
	 * trace
	 * traces/Trace-testCalculateAndIncrementByOneWithLogging-1603454200461.txt
	 */

	@Test
	public void mainTest() throws IOException, ABCException {
		String[] args = {
				 
				"--trace-files", //
				"./src/test/resources/abc.basiccalculator/Trace-testCalculateAndIncrementByOneWithLogging-1603454200461.txt", // 
				"--android-jar", //
				"/Users/gambi/Library/Android/sdk/platforms/android-28/android.jar", //
				"--apk",//
				"/Users/gambi/action-based-test-carving/apps-src/BasicCalculator/app/build/outputs/apk/debug/app-debug.apk"
		};
		
		Main.main(args);
		
	}
}
