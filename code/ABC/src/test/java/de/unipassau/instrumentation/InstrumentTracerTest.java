package de.unipassau.instrumentation;

import java.net.URISyntaxException;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import de.unipassau.utils.SystemTest;

public class InstrumentTracerTest {

	@Test
	@Category(SystemTest.class)
	public void instrumentAndTrace() throws URISyntaxException{
		InstrumentTracer tracer = new InstrumentTracer();
		tracer.main( new String[]{"./src/test/resources/Employee.jar"});
	}
}
