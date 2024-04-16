package org.ndts.optalgj.utils;

import org.ndts.optalgj.problems.rect.Box;
import org.ndts.optalgj.problems.rect.PositionedRectangle;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class Fits {
	public static boolean tryToFit(Box box, PositionedRectangle rectangle, int boxLength,
								   Supplier<Boolean> isCancelled, BiFunction<Box,
		PositionedRectangle, Boolean> isFitting) {
		if (boxLength * boxLength - box.occupiedArea() < rectangle.area()) return false;
		final var backupX = rectangle.x();
		final var backupY = rectangle.y();
		final var backupRotated = rectangle.rotated();
		final var smallestSide = Math.min(rectangle.width(), rectangle.height());
		final var maxIndex = boxLength - smallestSide;
		for (var row = 0; !isCancelled.get() && row < maxIndex; row += 1) {
			for (var col = 0; !isCancelled.get() && col < maxIndex; col += 1) {
				rectangle.transformTo(col, row, false);
				if (!rectangle.outOfBounds(boxLength) && isFitting.apply(box, rectangle))
					return true;
				rectangle.rotate();
				if (!rectangle.outOfBounds(boxLength) && isFitting.apply(box, rectangle))
					return true;
				rectangle.rotate();
			}
		}
		rectangle.transformTo(backupX, backupY, backupRotated);
		return false;
	}
}
