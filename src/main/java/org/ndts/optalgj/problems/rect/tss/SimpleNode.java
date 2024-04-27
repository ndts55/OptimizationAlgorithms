package org.ndts.optalgj.problems.rect.tss;

import org.ndts.optalgj.algs.GreedyNode;
import org.ndts.optalgj.problems.rect.domain.Output;
import org.ndts.optalgj.problems.rect.domain.Rectangle;

import java.util.List;

public record SimpleNode(Output output, List<Rectangle> rectangles) implements GreedyNode<Output> {
	@Override
	public Output solution() {
		return output;
	}
}
