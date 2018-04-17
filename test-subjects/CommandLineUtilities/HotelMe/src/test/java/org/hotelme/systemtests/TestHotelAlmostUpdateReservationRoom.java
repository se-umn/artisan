package org.hotelme.systemtests;

import java.util.ArrayList;
import java.util.List;

import org.hotelme.Main;
import org.hotelme.utils.SystemTestUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestHotelAlmostUpdateReservationRoom {// extends
													// AbstractHotelSystemTest
													// {

	@Rule
	public final TextFromStandardInputStream userInputs = TextFromStandardInputStream.emptyStandardInputStream();

	@Rule
	public final ExpectedSystemExit exit = ExpectedSystemExit.none();

	@Test
	public void systemTest() {

		SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");

		DateTime checkInDate = new DateTime(System.currentTimeMillis());
		DateTime checkOutDate = checkInDate.plusDays(2);
		DateTimeFormatter fmt = DateTimeFormat.forPattern("MM/dd/yy");

		// Build the input array
		List<String> commands = new ArrayList<>();
		commands.add("1"); // 1. Sign-In
		commands.add("mb");
		commands.add("qweasd");// Credentials

		commands.add("2"); // 2. Reserve a Room
		commands.add(fmt.print(checkInDate)); // Check in
		commands.add(fmt.print(checkOutDate)); // Check out = Checkin + 1
		commands.add("1"); // 1. Happy Double
		commands.add("1"); // 1. Yes
		commands.add("4"); // How many adults will be staying?

		// This room is not there
		commands.add("3"); // 3. Update Reservation
		commands.add("0"); // Select 1 room -> 0 To go back !
		//
		commands.add("5"); // 5. Sign Out
		commands.add("3"); // 3. Exit

		userInputs.provideLines(//
				commands.toArray(new String[] {}));

		exit.expectSystemExitWithStatus(0);

		//
		Main.main(new String[] {});
	}
}
