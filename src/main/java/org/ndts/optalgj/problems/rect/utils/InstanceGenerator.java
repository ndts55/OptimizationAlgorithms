package org.ndts.optalgj.problems.rect.utils;

import org.ndts.optalgj.problems.rect.domain.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class InstanceGenerator {
	public static List<Rectangle> generateInstances(int count, int minWidth, int maxWidth,
													int minHeight, int maxHeight) {
		var result = new ArrayList<Rectangle>(count);
		for (int i = 0; i < count; i++)
			result.add(new Rectangle(i, RNG.nextInt(minHeight, maxHeight), RNG.nextInt(minWidth,
				maxWidth)));
		return result;
	}
}
