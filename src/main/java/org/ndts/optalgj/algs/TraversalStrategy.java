package org.ndts.optalgj.algs;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class TraversalStrategy<Solution, Node extends GreedyNode<Solution>> {
	private final AtomicBoolean cancelled = new AtomicBoolean(false);

	protected abstract List<Node> traverse(Node node);

	protected void cancel() {
		cancelled.setRelease(true);
	}

	protected boolean isCancelled() {return cancelled.getAcquire();}
}
