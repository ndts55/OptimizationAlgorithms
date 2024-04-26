package org.ndts.optalgj.gui;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Task;
import org.ndts.optalgj.algs.GreedySearchVariant;
import org.ndts.optalgj.algs.LocalSearch;
import org.ndts.optalgj.algs.LocalSearchVariant;
import org.ndts.optalgj.algs.OptimizationAlgorithm;
import org.ndts.optalgj.problems.rect.*;

public class SearchTask extends Task<Output> {
	private final OptimizationAlgorithm<Input, Output> algorithm;
	private final Input input;
	private final LongProperty iteration = new SimpleLongProperty(0);

	public SearchTask(final OptimizationAlgorithm<Input, Output> algorithm,
					  final Input input) {
		this.algorithm = algorithm;
		this.input = input;
	}

	public SearchTask(final LocalSearchVariant neighborhoodVariant, Input input) {
		this(new LocalSearch<>(new SimpleSolutionConstructor(),
			switch (neighborhoodVariant) {
				case Geometric, Rules -> new BoxCountMinimization();
				case Overlap -> new BoxCountAndOverlaps();
			},
			switch (neighborhoodVariant) {
				case LocalSearchVariant.Geometric -> new GeometricNeighborhood();
				case LocalSearchVariant.Overlap -> new OverlapNeighborhood();
				case LocalSearchVariant.Rules -> new RuleNeighborhood();
			}), input);
	}

	public SearchTask(final GreedySearchVariant neighborhoodVariant, Input input) {
		// TODO construct SearchTask for greedy search
		throw new UnsupportedOperationException();
	}

	public ReadOnlyLongProperty iterationProperty() {
		return iteration;
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
			// TODO call updateValue every X ms instead of on every iteration
			if (progressed) updateValue(algorithm.currentOutput());
			iteration.set(algorithm.currentIteration());
		} while (!isCancelled() && progressed);
		return algorithm.bestOutput();
	}

	@Override
	protected void cancelled() {
		algorithm.cancel();
		super.cancelled();
	}
}
