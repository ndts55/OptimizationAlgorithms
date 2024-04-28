package org.ndts.optalgj.algs;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Neighborhood<T> {
	private final AtomicBoolean cancelled = new AtomicBoolean(false);
	protected long lastSignificantImprovement = 0;
	protected long currentIteration = 0;

	protected T betterNeighbor(final T initial, final ObjectiveFunction<T> obj) {
		final var initialEvaluation = obj.evaluate(initial);
		var output = findBetterNeighbor(initial, initialEvaluation, obj);
		final var outputEvaluation = obj.evaluate(output);
		if (outputEvaluation < initialEvaluation) lastSignificantImprovement = currentIteration;
		if ((currentIteration - lastSignificantImprovement) >= stallThreshold()) return null;
		currentIteration += 1;
		return output;
	}

	protected abstract T findBetterNeighbor(final T initial, final double initialEvaluation,
											final ObjectiveFunction<T> obj);

	protected abstract long stallThreshold();

	public void cancel() {cancelled.setRelease(true);}

	protected boolean isCancelled() {return cancelled.getAcquire();}
}
