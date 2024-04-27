package org.ndts.optalgj.gui;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Task;
import org.ndts.optalgj.algs.*;
import org.ndts.optalgj.problems.rect.domain.Input;
import org.ndts.optalgj.problems.rect.domain.Output;
import org.ndts.optalgj.problems.rect.nbhs.GeometricNeighborhood;
import org.ndts.optalgj.problems.rect.nbhs.OverlapNeighborhood;
import org.ndts.optalgj.problems.rect.nbhs.RuleNeighborhood;
import org.ndts.optalgj.problems.rect.objs.BoxCountAndOverlaps;
import org.ndts.optalgj.problems.rect.objs.BoxCountMinimization;
import org.ndts.optalgj.problems.rect.tss.LegoTraversalStrategy;
import org.ndts.optalgj.problems.rect.tss.SimpleTraversalStrategy;
import org.ndts.optalgj.problems.rect.utils.SolutionConstructor;

public class SearchTask extends Task<Output> {
	private final OptimizationAlgorithm<Output> algorithm;
	private final LongProperty iteration = new SimpleLongProperty(0);

	public SearchTask(final OptimizationAlgorithm<Output> algorithm) {
		this.algorithm = algorithm;
	}

	public SearchTask(final LocalSearchVariant neighborhoodVariant, Input input) {
		this(new LocalSearch<>(switch (neighborhoodVariant) {
			case Geometric, Rules -> new BoxCountMinimization();
			case Overlap -> new BoxCountAndOverlaps();
		}, switch (neighborhoodVariant) {
			case LocalSearchVariant.Geometric -> new GeometricNeighborhood();
			case LocalSearchVariant.Overlap -> new OverlapNeighborhood();
			case LocalSearchVariant.Rules -> new RuleNeighborhood();
		}, SolutionConstructor.forLocal(input)));
	}

	public SearchTask(final GreedySearchVariant searchVariant, Input input) {
		this(switch (searchVariant) {
			case VariantA ->
				new GreedySearch<>(new BoxCountMinimization(), new SimpleTraversalStrategy(),
					SimpleTraversalStrategy.oracle(), SimpleTraversalStrategy.queue(),
					SimpleTraversalStrategy.initialData(input));
			case VariantB ->
				new GreedySearch<>(new BoxCountMinimization(), new LegoTraversalStrategy(),
					LegoTraversalStrategy.oracle(), LegoTraversalStrategy.queue(),
					LegoTraversalStrategy.initialData(input));
		});
	}

	public ReadOnlyLongProperty iterationProperty() {
		return iteration;
	}

	@Override
	protected Output call() {
		if (isCancelled()) return algorithm.best();
		var progressed = false;
		do {
			try {
				progressed = algorithm.iterate();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// TODO call updateValue every X ms instead of on every iteration
			if (progressed) updateValue(algorithm.current());
			iteration.set(algorithm.iteration());
		} while (!isCancelled() && progressed);
		return algorithm.best();
	}

	@Override
	protected void cancelled() {
		algorithm.cancel();
		super.cancelled();
	}
}
