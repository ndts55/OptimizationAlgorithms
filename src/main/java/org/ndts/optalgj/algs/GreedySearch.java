package org.ndts.optalgj.algs;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

public class GreedySearch<Solution extends CopyConstructible<Solution>,
	Node extends GreedyNode<Solution, Node>> implements OptimizationAlgorithm<Solution> {
	private final ObjectiveFunction<Solution> obj;
	private final Queue<Node> queue;
	private final AtomicBoolean cancelled = new AtomicBoolean(false);
	private int iteration = 0;
	private Solution bestSolution;
	private Solution currentSolution;

	public GreedySearch(ObjectiveFunction<Solution> obj, Queue<Node> queue, Node initialData) {
		this.obj = obj;
		this.queue = queue;
		this.queue.add(initialData);
		currentSolution = initialData.solution();
	}

	@Override
	public boolean iterate() {
		if (queue.isEmpty() || isCancelled()) return false;

		iteration += 1;

		var node = queue.remove();
		currentSolution = node.solution();
		if (node.isLeaf()) {
			if (bestSolution == null || obj.evaluate(currentSolution) < obj.evaluate(bestSolution)) {
				bestSolution = currentSolution.copy();
			}
		} else {
			queue.addAll(node.descend());
		}

		return true;
	}

	@Override
	public void cancel() {
		cancelled.setRelease(true);
	}

	private boolean isCancelled() {return cancelled.getAcquire();}

	@Override
	public Solution best() {
		return bestSolution;
	}

	@Override
	public Solution current() {
		return currentSolution.copy();
	}

	@Override
	public int iteration() {
		return iteration;
	}
}
