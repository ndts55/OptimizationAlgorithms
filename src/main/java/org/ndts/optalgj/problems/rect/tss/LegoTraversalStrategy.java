package org.ndts.optalgj.problems.rect.tss;

import org.ndts.optalgj.algs.Oracle;
import org.ndts.optalgj.algs.TraversalStrategy;
import org.ndts.optalgj.problems.rect.domain.Input;
import org.ndts.optalgj.problems.rect.domain.Output;

import java.util.List;
import java.util.Queue;

// TODO implement LegoTraversalStrategy
public class LegoTraversalStrategy extends TraversalStrategy<Output, LegoNode> {
	public static Oracle<Output, LegoNode> oracle() {
		throw new UnsupportedOperationException();
	}

	public static Queue<LegoNode> queue() {
		throw new UnsupportedOperationException();
	}

	public static LegoNode initialData(Input input) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected List<LegoNode> traverse(LegoNode wrapper) {
		return List.of();
	}
}
