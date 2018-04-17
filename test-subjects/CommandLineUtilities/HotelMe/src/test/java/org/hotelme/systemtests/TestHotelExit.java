package org.hotelme.systemtests;

import org.hotelme.Main;
import org.hotelme.utils.SystemTestUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

/**
 * Using super classes in the tests is not covered by the actual
 * implementations. This is because in the trace we cannot observe all the
 * initializations made by JUnit. We see the instantiation of this test as
 * "<INIT>" of its super class, but not "<INIT>" of this class, because that
 * call is made by JUnit. This creates awkward situations that can be handled
 * only using heuristics ATM
 * 
 * The use of @Before/@After also is tricky, but we can handle it. We do
 * because, despite we do not see the invocation of the method, we observe its
 * executions. This works as long as there's no parameters in these calls
 * 
 * @author gambi
 *
 */
public class TestHotelExit /* extends AbstractHotelSystemTest */ {

	@Rule
	public final TextFromStandardInputStream userInputs = TextFromStandardInputStream.emptyStandardInputStream();

	@Rule
	public final ExpectedSystemExit exit = ExpectedSystemExit.none();

	@Test
	public void systemTest() {

		SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
		
		// Prepare the mocks
		userInputs.provideLines("3");

		// Assert that it actually exits
		exit.expectSystemExitWithStatus(0);

		Main.main(new String[] {});
	}
}
