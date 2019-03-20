package de.unipassau.abc.carving;

import static org.junit.Assert.*;

import org.junit.Test;

import de.unipassau.abc.carving.MethodInvocation;
import de.unipassau.abc.carving.MethodInvocationMatcher;
import de.unipassau.abc.carving.ObjectInstance;

public class MethodInvocationMatcherTest {

	// We test only the main scenarios: Given a well formed JIMPLE method
	// Exact method
	// match by class
	// match by package
	private MethodInvocation sampleMethodInvocation = new MethodInvocation(
			"<org.employee.Validation: int numberValidation(java.lang.String,org.employee.DummyObjectToPassAsParameter)>",
			1);

	@Test
	public void testByMethodWithArray() {
		MethodInvocation methodInvocation = new MethodInvocation("<de.unipassau.abc.testsubject2.ArrayHandlingClass: void callMeMaybe(java.lang.String[])>", 1);
		MethodInvocationMatcher methodMatcher = MethodInvocationMatcher
				.byMethodLiteral("<de.unipassau.abc.testsubject2.ArrayHandlingClass: void callMeMaybe(java.lang.String[])>");
		assertTrue(methodMatcher.matches(methodInvocation));
	}
	
	@Test
	public void testByMethodWithArrayReturnType() {
		MethodInvocation methodInvocation = new MethodInvocation("<de.unipassau.abc.testsubject2.ArrayHandlingClass: java.lang.String[] callMeMaybe()>", 1);
		MethodInvocationMatcher methodMatcher = MethodInvocationMatcher
				.byMethodLiteral("<de.unipassau.abc.testsubject2.ArrayHandlingClass: java.lang.String[] callMeMaybe()>");
		assertTrue(methodMatcher.matches(methodInvocation));
	}
	
	@Test
	public void testGetMatcherByInvocationWithArrayReturnType() {
		String inputCarveBy = "invocation=<directory.DirectoryDAO: directory.Record\\[\\] runCommand(directory.Command)>_7047";
		String[] carveByTokens = inputCarveBy.split("=");
		MethodInvocationMatcher carveBy = Carver.getMatcherFor(carveByTokens[0], carveByTokens[1]);
	}
	
	
	
	@Test
	public void testMethodWithRegEx() {
		MethodInvocation scannerReadString = new MethodInvocation("<<java.util.Scanner: java.lang.String next()>)>", 1);
		MethodInvocation scannerReadInt = new MethodInvocation("<<java.util.Scanner: int nextInt()>)>", 2);
		
		// We do not support for the moment regex for return type
		MethodInvocationMatcher scannerMethodMatcher = MethodInvocationMatcher
				.byMethod("<java.util.Scanner: .* next.*()>");
		assertTrue(scannerMethodMatcher.matches(scannerReadString));
		assertTrue(scannerMethodMatcher.matches(scannerReadInt));
		
		MethodInvocation junit3StyleWithParameters = new MethodInvocation("<siena.TestFilter: void main(java.lang.String[])>", 10);
		MethodInvocation junit3StyleWithoutParameters = new MethodInvocation("<siena.TestFilter: void foo()>", 10);
//		MethodInvocation junit3StyleWithReturnType = new MethodInvocation("<siena.foo.bar.TestFilter: siena.Bar foo()>", 10);
		
		MethodInvocationMatcher junit3StyleMatcherWithoutParameters = MethodInvocationMatcher.byMethod("<siena..*Test.*: .* .*()>");
		//
		MethodInvocationMatcher junit3StyleMatcher = MethodInvocationMatcher.byMethod("<siena..*Test.*: void .*(.*)>");
	
		assertTrue(junit3StyleMatcher.matches( junit3StyleWithParameters ) );
		assertFalse(junit3StyleMatcher.matches( junit3StyleWithoutParameters ) );
//		assertFalse(junit3StyleMatcher.matches( junit3StyleWithReturnType ) );
		
		assertFalse(junit3StyleMatcherWithoutParameters.matches( junit3StyleWithParameters ) );
		assertTrue(junit3StyleMatcherWithoutParameters.matches( junit3StyleWithoutParameters ) );
//		assertTrue(junit3StyleMatcherWithoutParameters.matches( junit3StyleWithReturnType ) );
	}
	
	@Test
	public void testGettersMatch(){ // (.*?) -- TODO How to define a catch all for the parameters ?
		MethodInvocationMatcher gettersMatcher = MethodInvocationMatcher.byMethodName("get.*");
		
		assertTrue( gettersMatcher.matches( new MethodInvocation("<siena.TestFilter: void getAlpha(java.lang.String[])>", -1)));
		assertTrue( gettersMatcher.matches( new MethodInvocation("<siena.TestFilter: java.lang.String getBeta(java.lang.String)>", -1)));
		
		assertTrue( gettersMatcher.matches( new MethodInvocation("<siena.TestFilter: int getGamma(java.lang.String,int)>", -1)));
		assertTrue( gettersMatcher.matches( new MethodInvocation("<siena.TestFilter: java.lang.Object getDelta()>", -1)));
		//
//		assertFalse( gettersMatcher.matches( new MethodInvocation("<siena.TestFilter: void fooget(java.lang.String,int)>", -1)));
	}

	@Test
	public void testExactMatch() {
		MethodInvocationMatcher mim = new MethodInvocationMatcher("org.employee.Validation", "int", "numberValidation",
				"java.lang.String", "org.employee.DummyObjectToPassAsParameter");
		assertTrue(mim.matches(sampleMethodInvocation));
	}
	
	@Test
	public void testMatchWithParameterArray1() {
		MethodInvocationMatcher mim = MethodInvocationMatcher.byClass("java.nio.file.Files");
		MethodInvocation methodInvocationWithArray = new MethodInvocation("<java.nio.file.Files: java.nio.file.Path createTempDirectory(java.lang.String,java.nio.file.attribute.FileAttribute[])>",1);
		assertTrue(mim.matches(methodInvocationWithArray));
	}
	
	@Test
	public void testMatchWithParameterArray2() {
		MethodInvocationMatcher mim = MethodInvocationMatcher.byClass("java.nio.file.Files");
		MethodInvocation methodInvocationWithArray = new MethodInvocation("<java.nio.file.Files: java.nio.file.Path createTempDirectory(java.nio.file.attribute.FileAttribute[],java.lang.String)>",1);
		assertTrue(mim.matches(methodInvocationWithArray));
	}
	
	@Test
	public void testMatchWithParameterArray3() {
		MethodInvocationMatcher mim = MethodInvocationMatcher.byClass("java.nio.file.Files");
		MethodInvocation methodInvocationWithArray = new MethodInvocation("<java.nio.file.Files: java.nio.file.Path createTempDirectory(java.nio.file.attribute.FileAttribute[])>",1);
		assertTrue(mim.matches(methodInvocationWithArray));
	}
	
	@Test
	public void testMatchWithParameterArray4() {
		MethodInvocationMatcher mim = MethodInvocationMatcher.byClass("java.nio.file.Files");
		MethodInvocation methodInvocationWithArray = new MethodInvocation("<java.nio.file.Files: java.nio.file.Path createTempDirectory(java.lang.String[],java.nio.file.attribute.FileAttribute[])>",1);
		assertTrue(mim.matches(methodInvocationWithArray));
	}
	
	@Test
	public void testMatchWithParameterArray5() {
		MethodInvocationMatcher mim = MethodInvocationMatcher.byClass("java.nio.file.Files");
		MethodInvocation methodInvocationWithArray = new MethodInvocation("<java.nio.file.Files: java.nio.file.Path createTempDirectory(java.lang.String[],int,java.nio.file.attribute.FileAttribute[])>",1);
		assertTrue(mim.matches(methodInvocationWithArray));
	}
	

	@Test
	public void testDoesNotExactMatch() {
		MethodInvocationMatcher mim = new MethodInvocationMatcher("org.employee.Validation", "int2", "numberValidation",
				"java.lang.String", "org.employee.DummyObjectToPassAsParameter");
		assertFalse(mim.matches(sampleMethodInvocation));
	}

	@Test
	public void testMatchByClass() {
		MethodInvocationMatcher mim = MethodInvocationMatcher.byClass("org.employee.Validation");
		assertTrue(mim.matches(sampleMethodInvocation));
	}

	@Test
	public void testDoesNotMatchByClass() {
		MethodInvocationMatcher mim = MethodInvocationMatcher.byClass("org.employee.DummyObjectToPassAsParameter");
		assertFalse(mim.matches(sampleMethodInvocation));
	}

	@Test
	public void testMatchByPackage() {
		MethodInvocationMatcher mim = MethodInvocationMatcher.byPackage("org.employee.*");
		assertTrue(mim.matches(sampleMethodInvocation));
	}

	@Test
	public void testDoesNotMatchByPackage() {
		MethodInvocationMatcher mim = MethodInvocationMatcher.byPackage("com.foo.*");
		assertFalse(mim.matches(sampleMethodInvocation));
	}

	@Test
	public void testMatchByReturn() {
		MethodInvocationMatcher mim = MethodInvocationMatcher.byReturnType("int");
		assertTrue(mim.matches(sampleMethodInvocation));
	}

	@Test
	public void testDoesNotMatchByReturnWithPrimitive() {
		MethodInvocationMatcher mim = MethodInvocationMatcher.byReturnType("float");
		assertFalse(mim.matches(sampleMethodInvocation));
	}

	@Test
	public void testDoesNotMatchByReturnWithObject() {
		MethodInvocationMatcher mim = MethodInvocationMatcher.byReturnType("org.employee.DummyObjectToPassAsParameter");
		assertFalse(mim.matches(sampleMethodInvocation));
	}

	@Test
	public void testMatchByInstance() {
		// Add Instance Information on sampleMethodInvocation
		ObjectInstance objectInstance = new ObjectInstance("org.employee.Validation@195600860");
		sampleMethodInvocation.setOwner(objectInstance);
		MethodInvocationMatcher mim = MethodInvocationMatcher.byInstance(objectInstance);
		assertTrue(mim.matches(sampleMethodInvocation));
	}

	@Test
	public void testDoesNotMatchByInstanceOfSameType() {
		ObjectInstance objectInstance = new ObjectInstance("org.employee.Validation@195600860");
		ObjectInstance anotherInstance = new ObjectInstance("org.employee.Validation@1365202186");
		sampleMethodInvocation.setOwner(objectInstance);
		MethodInvocationMatcher mim = MethodInvocationMatcher.byInstance(anotherInstance);
		assertFalse(mim.matches(sampleMethodInvocation));
	}

	@Test
	public void testDoesNotMatchByInstanceOfDifferentType() {
		ObjectInstance objectInstance = new ObjectInstance("org.employee.Validation@195600860");
		ObjectInstance anotherInstance = new ObjectInstance("org.employee.DummyObjectToPassAsParameter@1694819250");
		sampleMethodInvocation.setOwner(objectInstance);
		MethodInvocationMatcher mim = MethodInvocationMatcher.byInstance(anotherInstance);
		assertFalse(mim.matches(sampleMethodInvocation));
	}

}
