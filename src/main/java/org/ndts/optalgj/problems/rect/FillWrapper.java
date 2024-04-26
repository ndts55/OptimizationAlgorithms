package org.ndts.optalgj.problems.rect;

import org.ndts.optalgj.algs.GreedyWrapper;

import java.util.List;

public record FillWrapper(Output output,
						  List<Rectangle> rectangles) implements GreedyWrapper<Output> {
	@Override
	public Output solution() {
		return output;
	}
}
