package de.unipassau.abc.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import de.unipassau.abc.utils.JimpleUtils;

public class JimpleUtilsTest {

	@Test
	public void testGetParameterListForMethodWithoutParameters(){
		String jimpleMethod = "<org.employee.DummyObjectFactory: org.employee.DummyObjectToPassAsParameter createNewDummyObject()>";
		String[] parameterList = JimpleUtils.getParameterList(jimpleMethod );
		assertEquals(0, parameterList.length);
	}
}
