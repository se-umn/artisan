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
	private MethodInvocation sampleMethodInvocation = new MethodInvocation("<org.employee.Validation: int numberValidation(java.lang.String,org.employee.DummyObjectToPassAsParameter)>", 1);
	
	@Test
	public void testExactMatch() {
		MethodInvocationMatcher mim = new MethodInvocationMatcher("org.employee.Validation", "int", "numberValidation", "java.lang.String", "org.employee.DummyObjectToPassAsParameter");
		assertTrue( mim.match(sampleMethodInvocation) );
	}
	
	@Test
	public void testDoesNotExactMatch() {
		MethodInvocationMatcher mim = new MethodInvocationMatcher("org.employee.Validation", "int2", "numberValidation", "java.lang.String", "org.employee.DummyObjectToPassAsParameter");
		assertFalse( mim.match(sampleMethodInvocation) );
	}

	@Test
	public void testMatchByClass() {
		MethodInvocationMatcher mim = MethodInvocationMatcher.byClass("org.employee.Validation");
		assertTrue(mim.match(sampleMethodInvocation));
	}
	
	@Test
	public void testDoesNotMatchByClass() {
		MethodInvocationMatcher mim = MethodInvocationMatcher.byClass("org.employee.DummyObjectToPassAsParameter");
		assertFalse(mim.match(sampleMethodInvocation));
	}

	@Test
	public void testMatchByPackage() {
		MethodInvocationMatcher mim = MethodInvocationMatcher.byPackage("org.employee.*");
		assertTrue( mim.match(sampleMethodInvocation) );
	}
	
	@Test
	public void testDoesNotMatchByPackage() {
		MethodInvocationMatcher mim = MethodInvocationMatcher.byPackage("com.foo.*");
		assertFalse( mim.match(sampleMethodInvocation) );
	}
	
	@Test
	public void testMatchByReturn(){
		MethodInvocationMatcher mim = MethodInvocationMatcher.byReturnType("int");
		assertTrue( mim.match(sampleMethodInvocation) );
	}
	
	@Test
	public void testDoesNotMatchByReturnWithPrimitive(){
		MethodInvocationMatcher mim = MethodInvocationMatcher.byReturnType("float");
		assertFalse( mim.match(sampleMethodInvocation) );
	}
	
	@Test
	public void testDoesNotMatchByReturnWithObject(){
		MethodInvocationMatcher mim = MethodInvocationMatcher.byReturnType("org.employee.DummyObjectToPassAsParameter");
		assertFalse( mim.match(sampleMethodInvocation) );
	}
	
	
	@Test
	public void testMatchByInstance(){
		// Add Instance Information on sampleMethodInvocation
		ObjectInstance objectInstance = new ObjectInstance("org.employee.Validation@195600860");
		sampleMethodInvocation.setOwner( objectInstance );
		MethodInvocationMatcher mim = MethodInvocationMatcher.byInstance(objectInstance);
		assertTrue( mim.match(sampleMethodInvocation) );
	}
	
	@Test
	public void testDoesNotMatchByInstanceOfSameType(){
		ObjectInstance objectInstance = new ObjectInstance("org.employee.Validation@195600860");
		ObjectInstance anotherInstance = new ObjectInstance("org.employee.Validation@1365202186");
		sampleMethodInvocation.setOwner( objectInstance );
		MethodInvocationMatcher mim = MethodInvocationMatcher.byInstance(anotherInstance );
		assertFalse( mim.match(sampleMethodInvocation) );
	}
	
	@Test
	public void testDoesNotMatchByInstanceOfDifferentType(){
		ObjectInstance objectInstance = new ObjectInstance("org.employee.Validation@195600860");
		ObjectInstance anotherInstance = new ObjectInstance("org.employee.DummyObjectToPassAsParameter@1694819250");
		sampleMethodInvocation.setOwner( objectInstance );
		MethodInvocationMatcher mim = MethodInvocationMatcher.byInstance(anotherInstance );
		assertFalse( mim.match(sampleMethodInvocation) );
	}
	
}