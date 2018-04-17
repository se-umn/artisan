package org.hotelme.systemtests;

import org.hotelme.Main;
import org.hotelme.utils.SystemTestUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestInvalidLogin {// extends AbstractHotelSystemTest {

	@Rule
	public final TextFromStandardInputStream userInputs = TextFromStandardInputStream.emptyStandardInputStream();

	@Rule
	public final ExpectedSystemExit exit = ExpectedSystemExit.none();

	@Test
	public void systemTest() {

		SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");

		// Prepare the mocks
		userInputs.provideLines(//
				"1",
				"bar", // Name
				"foo", // Pwd
				"3" // Exit
		);

		// Assert that it actually exits
		exit.expectSystemExitWithStatus(0);

		//
		Main.main(new String[] {});
	}
}
