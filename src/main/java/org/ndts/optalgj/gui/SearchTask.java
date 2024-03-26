package org.ndts.optalgj.gui;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Task;
import org.ndts.optalgj.algs.GreedyNeighborhoodVariant;
import org.ndts.optalgj.algs.LocalNeighborhoodVariant;
import org.ndts.optalgj.algs.LocalSearch;
import org.ndts.optalgj.algs.OptimizationAlgorithm;
import org.ndts.optalgj.problems.rect.*;

public class SearchTask extends Task<Output> {
	private final OptimizationAlgorithm<Input, Output> algorithm;
	private final Input input;
	private final LongProperty iteration = new SimpleLongProperty(0);

	public SearchTask(final OptimizationAlgorithm<Input, Output> algorithm, final Input input) {
		this.algorithm = algorithm;
		this.input = input;
	}

	public SearchTask(final LocalNeighborhoodVariant neighborhoodVariant, Input input) {
		this(new LocalSearch<>(new SimpleSolutionConstructor(), new BoxCountMinimization(), switch (neighborhoodVariant) {
			case LocalNeighborhoodVariant.Geometric -> new GeometricNeighborhood();
			case LocalNeighborhoodVariant.Rules, LocalNeighborhoodVariant.Overlap ->
				throw new UnsupportedOperationException();
		}), input);
	}

	public SearchTask(final GreedyNeighborhoodVariant neighborhoodVariant, Input input) {
		// TODO construct SearchTask for greedy search
		throw new UnsupportedOperationException();
	}

	public ReadOnlyLongProperty iterationProperty() {
		return iteration;
	}

	private void countIteration() {
		iteration.set(iteration.get() + 1);
	}

	@Override
	protected Output call() {
		algorithm.initialize(input);
		if (isCancelled()) return algorithm.bestOutput();
		var progressed = false;
		do {
			try {
				progressed = algorithm.iteration();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (progressed) updateValue(algorithm.currentOutput());
			countIteration();
		} while (!isCancelled() && progressed);
		return algorithm.bestOutput();
	}

	@Override
	protected void cancelled() {
		algorithm.cancel();
		super.cancelled();
	}
}
