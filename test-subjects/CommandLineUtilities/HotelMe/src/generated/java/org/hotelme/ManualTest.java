package org.hotelme;

import org.hotelme.utils.SystemTestUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

public class ManualTest {

	@Rule
	public final ExpectedSystemExit exit = ExpectedSystemExit.none();

	@Test
	public void systemTest() {

		SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");

		exit.expectSystemExitWithStatus(0);

		//
		Main.main(new String[] {});
	}
}
