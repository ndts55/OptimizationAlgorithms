package org.ndts.optalgj.gui;

import org.ndts.optalgj.problems.rect.Rectangle;
import org.ndts.optalgj.utils.RNG;

import java.util.ArrayList;
import java.util.List;

public class InstanceGenerator {
	public static List<Rectangle> generateInstances(int count, int minWidth, int maxWidth, int minHeight, int maxHeight) {
		var result = new ArrayList<Rectangle>(count);
		for (int i = 0; i < count; i++) {
			result.add(
				new Rectangle(
					RNG.nextInt(minHeight, maxHeight),
					RNG.nextInt(minWidth, maxWidth)
				)
			);
		}
		return result;
	}
}