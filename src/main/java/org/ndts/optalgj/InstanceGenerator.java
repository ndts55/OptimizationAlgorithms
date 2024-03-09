package org.ndts.optalgj;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InstanceGenerator {

	public static List<Rectangle> generateInstances(int count, int minWidth, int maxWidth, int minHeight, int maxHeight) {
		var result = new ArrayList<Rectangle>(count);
		for (int i = 0; i < count; i++) {
			result.add(new Rectangle(nextInt(minHeight, maxHeight), nextInt(minWidth, maxWidth)));
		}
		return result;
	}

	private static final Random random = new Random();

	private static int nextInt(int min, int max) {
		return random.nextInt(max - min + 1) + min;
	}
}
