package org.ndts.optalgj.utils;

import java.util.Random;

public class RNG {
	private static final Random random = new Random();

	public static int nextInt(int min, int max) {
		return random.nextInt(max - min + 1) + min;
	}

	public static int nextIndex(int size) {
		return nextInt(0, size - 1);
	}

	public static int nextInt(int max) {
		return random.nextInt(max + 1);
	}

	public static int nextPercent() {
		return random.nextInt(101);
	}
}
