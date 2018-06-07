package de.unipassau.abc.generation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.CallableDeclaration.Signature;

import de.unipassau.abc.carving.Carver;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.Pair;

public class TestTestSuiteMinimizer {

	@Test
	public void testRemovePure() throws IOException {

		Set<CompilationUnit> allTestClasses = Collections.EMPTY_SET;
		String resetEnvironmentBy = null;
		TestSuiteExecutor testSuiteExecutor = null;
		TestSuiteMinimizer minimizer = new TestSuiteMinimizer(allTestClasses, resetEnvironmentBy, testSuiteExecutor);
		//
		String javaCode = new String(
				Files.readAllBytes(Paths.get("./src/test/resources/javas-to-minimize/TestHotelController_1.java")));

		CompilationUnit testClass = JavaParser.parse(javaCode);

		Carver.removePureMethods(testClass);

		System.out.println(testClass);
	}

	@Test
	public void skipMandatoryStatements() throws IOException {
		List<File> projectJars = new ArrayList<>();
		projectJars.add(new File("./src/test/resources/HotelReservationSystem.jar"));
		projectJars.add(new File("./src/test/resources/HotelReservationSystem-tests.jar"));
		projectJars.add(new File("./src/test/resources/system-rules-1.17.0.jar"));

		String javaCode = new String(
				Files.readAllBytes(Paths.get("./src/test/resources/javas-to-minimize/TestHotelController_1.java")));
		CompilationUnit testClass = JavaParser.parse(javaCode);

		Set<CompilationUnit> allTestClasses = Collections.singleton(testClass);
		String resetEnvironmentBy = null;
		TestSuiteExecutor testSuiteExecutor = new FakeTestSuiteExecutor(projectJars);
		TestSuiteMinimizer minimizer = new TestSuiteMinimizer(allTestClasses, resetEnvironmentBy, testSuiteExecutor);
		//
		minimizer.minimizeTestMethods();
		//
		System.out.println(testClass);
	}

	@Test
	public void testMinimizeTestSuite() throws IOException, CarvingException {
		List<File> projectJars = new ArrayList<>();
		projectJars.add(new File("./src/test/resources/HotelReservationSystem.jar"));
		projectJars.add(new File("./src/test/resources/HotelReservationSystem-tests.jar"));
		projectJars.add(new File("./src/test/resources/system-rules-1.17.0.jar"));
		// Deps for the project are required as well !
		projectJars.add(new File("./src/test/resources/joda-time-2.9.4.jar"));
		projectJars.add(new File("./src/test/resources/mysql-connector-java-5.1.39.jar"));

		Set<CompilationUnit> testClasses = new HashSet<>();
		for (int i = 1; i <= 10; i++) {
			String javaCode = new String(Files.readAllBytes(Paths
					.get("/Users/gambi/action-based-test-carving/code/ABC/scripts/hotelme-abcOutput/org/hotelme/TestHotelController_"
							+ i + ".java")));
			CompilationUnit testClass = JavaParser.parse(javaCode);
			testClasses.add(testClass);
		}

		String resetEnvironmentBy = null;
		TestSuiteExecutor testSuiteExecutor = new TestSuiteExecutor(projectJars, Collections.EMPTY_LIST);

		TestSuiteMinimizer minimizer = new TestSuiteMinimizer(testClasses, resetEnvironmentBy, testSuiteExecutor);
		//
		System.out.println("Before Minimize " + testClasses.size());
		//
		minimizer.minimizeTestSuite();
		//
		System.out.println("After Minimize " + testClasses.size());
	}

	class FakeTestSuiteExecutor extends TestSuiteExecutor {

		public FakeTestSuiteExecutor(List<File> projectJars) {
			super(projectJars, Collections.EMPTY_LIST);
		}

		@Override
		public Collection<Pair<CompilationUnit, Signature>> executeAndReportFailedTests(
				Map<Pair<CompilationUnit, Signature>, Integer> currentIndex) {
			// All passed !
			return Collections.EMPTY_LIST;
		}
	}
}
