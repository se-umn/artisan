package de.unipassau.carving;

import static org.junit.Assert.*;

import org.junit.Test;

public class MethodInvocationMatcherTest {

	// We test only the main scenarios: Given a well formed JIMPLE method
	// Exact method
	// match by class
	// match by package
	private MethodInvocation methodInvocation = new MethodInvocation("<org.employee.Validation: int numberValidation(java.lang.String,org.employee.DummyObjectToPassAsParameter)>", 1);
	
	@Test
	public void testExactMatch() {
		MethodInvationMatcher mim = new MethodInvationMatcher("org.employee.Validation", "int", "numberValidation", "java.lang.String", "org.employee.DummyObjectToPassAsParameter");
		assertTrue( mim.match(methodInvocation) );
	}
	
	@Test
	public void testDoesNotExactMatch() {
		MethodInvationMatcher mim = new MethodInvationMatcher("org.employee.Validation", "int2", "numberValidation", "java.lang.String", "org.employee.DummyObjectToPassAsParameter");
		assertFalse( mim.match(methodInvocation) );
	}

	@Test
	public void testMatchByClass() {
		MethodInvationMatcher mim = new MethodInvationMatcher("org.employee.Validation");
		assertTrue(mim.match(methodInvocation));
	}
	
	@Test
	public void testDoesNotMatchByClass() {
		MethodInvationMatcher mim = new MethodInvationMatcher("org.employee.DummyObjectToPassAsParameter");
		assertFalse(mim.match(methodInvocation));
	}

	@Test
	public void testMatchByPackage() {
		MethodInvationMatcher mim = new MethodInvationMatcher("org.employee.*");
		assertTrue( mim.match(methodInvocation) );
	}
	
	@Test
	public void testDoesNotMatchByPackage() {
		MethodInvationMatcher mim = new MethodInvationMatcher("com.employee.*");
		assertFalse( mim.match(methodInvocation) );
	}
}
