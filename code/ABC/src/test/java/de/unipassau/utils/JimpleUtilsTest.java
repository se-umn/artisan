package de.unipassau.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class JimpleUtilsTest {

	@Test
	public void testGetParameterListForMethodWithoutParameters(){
		String jimpleMethod = "<org.employee.DummyObjectFactory: org.employee.DummyObjectToPassAsParameter createNewDummyObject()>";
		String[] parameterList = JimpleUtils.getParameterList(jimpleMethod );
		assertEquals(0, parameterList.length);
	}
}
