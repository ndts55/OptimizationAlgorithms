package org.ndts.optalgj.problems.rect;

import org.ndts.optalgj.algs.GreedyWrapper;
import org.ndts.optalgj.algs.Oracle;
import org.ndts.optalgj.algs.TraversalStrategy;

import java.util.List;
import java.util.Queue;
import java.util.TreeSet;

record PackWrapper(Output output, List<TreeSet<Rectangle>> trees) implements GreedyWrapper<Output> {

	@Override
	public Output solution() {
		return output;
	}
}

public class PackTraversalStrategy extends TraversalStrategy<Output, PackWrapper> {
	public static Oracle<Output, PackWrapper> oracle() {
		throw new UnsupportedOperationException();
	}

	public static Queue<PackWrapper> queue() {
		throw new UnsupportedOperationException();
	}

	public static PackWrapper initialData(Input input) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected List<PackWrapper> traverse(PackWrapper wrapper) {
		return List.of();
	}
}
