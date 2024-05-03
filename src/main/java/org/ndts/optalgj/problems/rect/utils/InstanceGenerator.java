package org.ndts.optalgj.problems.rect.utils;

import org.ndts.optalgj.problems.rect.domain.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InstanceGenerator {
	public static List<Rectangle> generateInstances(int count, int minWidth, int maxWidth,
													int minHeight, int maxHeight) {
		return IntStream.range(0, count).mapToObj(i -> new Rectangle(i, RNG.nextInt(minHeight,
			maxHeight), RNG.nextInt(minWidth, maxWidth))).collect(Collectors.toCollection(ArrayList::new));
	}
}
