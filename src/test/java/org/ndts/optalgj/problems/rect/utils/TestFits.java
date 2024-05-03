package org.ndts.optalgj.problems.rect.utils;

import org.junit.jupiter.api.Test;
import org.ndts.optalgj.problems.rect.domain.Box;
import org.ndts.optalgj.problems.rect.domain.PositionedRectangle;
import org.ndts.optalgj.problems.rect.domain.Rectangle;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.ndts.optalgj.problems.rect.utils.Fits.tryToFit;

public class TestFits {
	@Test
	public void testFillWith1x1Rects() {
		final var boxLength = 20;
		final var box = new Box();
		IntStream.range(0, boxLength * boxLength).forEach(i -> {
			final var rect = new PositionedRectangle(new Rectangle(0, 1, 1), 0, 0, false);
			assertTrue(tryToFit(box, rect, boxLength));
			box.add(rect);
		});
		final var rect = new PositionedRectangle(new Rectangle(0, 1, 1), 0, 0, false);
		assertFalse(tryToFit(box, rect, boxLength));
	}
}
