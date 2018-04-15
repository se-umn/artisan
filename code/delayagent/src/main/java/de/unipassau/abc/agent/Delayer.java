package de.unipassau.abc.agent;

import java.util.concurrent.ThreadLocalRandom;

public class Delayer {

	private final static long minDelay = Long.parseLong(System.getProperty("min.delay", "0"));
	private final static long maxDelay = Long.parseLong(System.getProperty("max.delay", "0"));

	public static void delay() {
		if (minDelay > 0 && maxDelay > 0 && maxDelay > minDelay) {
			try {
				long artificialDelay = ThreadLocalRandom.current().nextLong(minDelay, maxDelay);
				//
				// System.out.println("\n\nDelayer.delay() --> " +
				// artificialDelay +"\n\n");
				//
				Thread.currentThread().sleep(artificialDelay);
			} catch (InterruptedException e) {
			}
		}
	}
}
