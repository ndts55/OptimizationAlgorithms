package org.ndts.optalgj.algs;

import java.util.Queue;

public class GreedySearch<Solution extends CopyConstructible<Solution>,
	Wrapper extends GreedyWrapper<Solution>> implements OptimizationAlgorithm<Solution> {
	private final ObjectiveFunction<Solution> obj;
	private final TraversalStrategy<Solution, Wrapper> traversalStrategy;
	private final Oracle<Solution, Wrapper> oracle;
	private final Queue<Wrapper> queue;
	private int iteration = 0;
	private Solution bestSolution;
	private Solution currentSolution;

	public GreedySearch(ObjectiveFunction<Solution> obj,
						TraversalStrategy<Solution, Wrapper> traversalStrategy, Oracle<Solution,
		Wrapper> oracle, Queue<Wrapper> queue, Wrapper initialData) {
		this.obj = obj;
		this.traversalStrategy = traversalStrategy;
		this.oracle = oracle;
		this.queue = queue;
		this.queue.add(initialData);
		currentSolution = initialData.solution();
	}

	@Override
	public boolean iterate() {
		if (queue.isEmpty()) return false;

		iteration += 1;

		final var startingPoint = queue.remove();
		currentSolution = startingPoint.solution();
		if (oracle.isLeaf(startingPoint)) {
			if (bestSolution == null || obj.evaluate(currentSolution) < obj.evaluate(bestSolution)) {
				bestSolution = currentSolution.copy();
			}
		} else {
			final var items = traversalStrategy.traverse(startingPoint);
			queue.addAll(items);
		}

		return true;
	}

	@Override
	public void cancel() {
		traversalStrategy.cancel();
	}

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
