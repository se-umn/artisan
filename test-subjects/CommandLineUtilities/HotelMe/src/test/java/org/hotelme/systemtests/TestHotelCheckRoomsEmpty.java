package org.hotelme.systemtests;

import org.hotelme.Main;
import org.hotelme.utils.SystemTestUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestHotelCheckRoomsEmpty {// extends AbstractHotelSystemTest {

	@Rule
	public final TextFromStandardInputStream userInputs = TextFromStandardInputStream.emptyStandardInputStream();

	@Rule
	public final ExpectedSystemExit exit = ExpectedSystemExit.none();

	@Test
	public void systemTest() {

		SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");

		userInputs.provideLines("1", // 1. Sign-In
				"mb", "qweasd", // Credentials
				"1", // 1. Check no Reserved Rooms
				"5", // Sign out
				"3" // 3. Exit
		);

		exit.expectSystemExitWithStatus(0);

		//
		Main.main(new String[] {});
	}
}
