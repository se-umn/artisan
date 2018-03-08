package de.unipassau.instrumentation;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import de.unipassau.utils.SystemTest;

public class InstrumentTracerTest {

	@Test
	@Category(SystemTest.class)
	public void instrumentAndTrace(){
		fail("Not implemented! ");
		InstrumentTracer tracer = new InstrumentTracer();
		tracer.main( new String[]{"./src/test/resources/Employee.jar"});
	}
}
