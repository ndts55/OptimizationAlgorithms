package org.ndts.optalgj.problems.rect.tss;

import org.ndts.optalgj.algs.GreedyNode;
import org.ndts.optalgj.problems.rect.domain.Output;
import org.ndts.optalgj.problems.rect.domain.Rectangle;

import java.util.List;
import java.util.TreeSet;

public record LegoNode(Output output,
					   List<TreeSet<Rectangle>> trees) implements GreedyNode<Output> {

	@Override
	public Output solution() {
		return output;
	}
}
