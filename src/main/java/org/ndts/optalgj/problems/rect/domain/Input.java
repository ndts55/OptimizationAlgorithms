package org.ndts.optalgj.problems.rect.domain;

import java.util.List;

public record Input(List<Rectangle> rectangles, int boxLength) {
	public Input {
		for (var rect : rectangles)
			if (rect.height() > boxLength || rect.width() > boxLength)
				throw new IllegalArgumentException();
	}
}
