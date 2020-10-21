package org.junit.runner;

import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.experimental.categories.Category;
import org.junit.internal.TextListener;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.notification.Failure;

/**
 * A simple wrapper around JUnitCore to easily configure it.
 * 
 * @see <a href=
 *      "https://www.logicbig.com/tutorials/unit-testing/junit/junit-core.html">
 *      https://www.logicbig.com/tutorials/unit-testing/junit/junit-core.html</a>
 * 
 * @see <a href=
 *      "https://stackoverflow.com/questions/41261889/is-there-a-way-to-disable-org-junit-runner-junitcores-stdout-output">
 *      https://stackoverflow.com/questions/41261889/is-there-a-way-to-disable-org-junit-runner-junitcores-stdout-output</a>
 * 
 * @author gambi
 *
 */
public class JunitTestRunner {

	private final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

	static final class SuppressingOutputTextListener extends TextListener {

		public SuppressingOutputTextListener(PrintStream writer) {
			super(writer);
		}

		@Override
		public void testAssumptionFailure(org.junit.runner.notification.Failure failure) {
			System.out.println(
					"Assumption not met for " + failure.getDescription().getClassName() + ": " + failure.getMessage());
		}

		@Override
		public void testStarted(Description description) {
			System.out.println("\t - Running test: " + description.getClassName() + "." + description.getMethodName());
		}

		@Override
		public void testFailure(Failure failure) {
			// Make sure that the hideous E is not printed anynmore
		}
	}

	/**
	 * This is supposed to be called using start process from Main
	 * 
	 * @param args
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws ClassNotFoundException {

		System.out.println("");
		System.out.print("Starting test executions of tests: " + dtf.format(LocalDateTime.now()));
		System.out.println("");

		/*
		 * This method re-implements JunitCore.main() but replaces the hideous default
		 * TextListener with out that logs which test starts. To make it possible
		 * without too much hassle I declare the same package as in JUnitCore
		 */
		JUnitCore junit = new JUnitCore();

		Request request = null;

		/*
		 * This enables to run a single test case in a test case
		 */
		if (args.length == 1 && args[0].contains("#")) {
			String testClassName = args[0].split("#")[0];
			String testMethodName = args[0].split("#")[1];
			Class testClass = Class.forName(testClassName);
			request = Request.method(testClass, testMethodName);

		} else {
			JUnitCommandLineParseResult jUnitCommandLineParseResult = JUnitCommandLineParseResult.parse(args);
			request = jUnitCommandLineParseResult.createRequest(JUnitCore.defaultComputer());
		}

		junit.addListener(new SuppressingOutputTextListener(System.out));

		Result result = junit.run(request);

		System.exit(result.wasSuccessful() ? 0 : 1);

	}

}