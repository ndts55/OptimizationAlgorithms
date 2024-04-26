package org.ndts.optalgj.problems.rect.tss;

import org.ndts.optalgj.algs.GreedyWrapper;
import org.ndts.optalgj.problems.rect.domain.Output;
import org.ndts.optalgj.problems.rect.domain.Rectangle;

import java.util.List;

public record SimpleWrapper(Output output,
							List<Rectangle> rectangles) implements GreedyWrapper<Output> {
	@Override
	public Output solution() {
		return output;
	}
}
