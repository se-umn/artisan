package org.hotelme.systemtests;

import org.hotelme.Main;
import org.hotelme.utils.SystemTestUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestHotelReserveRoom {// extends AbstractHotelSystemTest {

	@Rule
	public final TextFromStandardInputStream userInputs = TextFromStandardInputStream.emptyStandardInputStream();

	@Rule
	public final ExpectedSystemExit exit = ExpectedSystemExit.none();

	@Test
	public void systemTest() {

		SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");

		DateTime checkInDate = new DateTime( System.currentTimeMillis());
		DateTime checkOutDate = checkInDate.plusDays(2);
	    DateTimeFormatter fmt = DateTimeFormat.forPattern("MM/dd/yy");
	    
		userInputs.provideLines("1", // 1. Sign-In
				"mb", "qweasd", // Credentials
				"2", // 2. Reserve a Room 
				fmt.print( checkInDate ), // Check in
				fmt.print( checkOutDate ), // Check out = Checkin + 1
				"1", // 1. Happy Double
				"2", // 2. No
				"5", // 5. Sign Out
				"3" // 3. Exit
		);

		exit.expectSystemExitWithStatus(0);

		//
		Main.main(new String[] {});
	}
}
