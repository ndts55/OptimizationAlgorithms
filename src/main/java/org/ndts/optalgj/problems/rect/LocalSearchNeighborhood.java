package org.ndts.optalgj.problems.rect;

import org.ndts.optalgj.algs.Neighborhood;
import org.ndts.optalgj.algs.ObjectiveFunction;

import java.util.concurrent.atomic.AtomicBoolean;

// TODO implement this in Neighborhood directly, which should then be an abstract class
public abstract class LocalSearchNeighborhood implements Neighborhood<Output> {
	// region Attributes
	private final AtomicBoolean cancelled = new AtomicBoolean(false);
	protected long lastSignificantImprovement = 0;
	protected long currentIteration = 0;
	// endregion

	// region Cancellation
	@Override
	public void cancel() {
		cancelled.setRelease(true);
	}

	@Override
	public boolean isCancelled() {
		return cancelled.getAcquire();
	}
	// endregion

	@Override
	public Output betterNeighbor(final Output initial, final ObjectiveFunction<Output> obj) {
		var initialEvaluation = obj.evaluate(initial);
		var output = findBetterNeighbor(initial, initialEvaluation, obj);
		var outputEvaluation = obj.evaluate(output);
		if (outputEvaluation < initialEvaluation) lastSignificantImprovement = currentIteration;
		if ((currentIteration - lastSignificantImprovement) >= stallThreshold()) return null;
		currentIteration += 1;
		return output;
	}

	abstract Output findBetterNeighbor(final Output initial, final double initialEvaluation
		, final ObjectiveFunction<Output> obj);

	abstract long stallThreshold();
}
