package org.ndts.optalgj.gui;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Task;
import org.ndts.optalgj.algs.*;
import org.ndts.optalgj.problems.rect.MinBoxObjs;
import org.ndts.optalgj.problems.rect.domain.Input;
import org.ndts.optalgj.problems.rect.domain.Output;
import org.ndts.optalgj.problems.rect.neighborhood.GeometricNeighborhood;
import org.ndts.optalgj.problems.rect.neighborhood.OverlapNeighborhood;
import org.ndts.optalgj.problems.rect.neighborhood.RuleNeighborhood;
import org.ndts.optalgj.problems.rect.node.LegoNode;
import org.ndts.optalgj.problems.rect.node.SimpleNode;
import org.ndts.optalgj.problems.rect.node.SortedLegoNode;
import org.ndts.optalgj.problems.rect.node.SortedSimpleNode;
import org.ndts.optalgj.problems.rect.utils.SolutionConstructor;

import java.util.ArrayDeque;

public class SearchTask extends Task<Output> {
	private final OptimizationAlgorithm<Output> algorithm;
	private final LongProperty iteration = new SimpleLongProperty(0);

	public SearchTask(final OptimizationAlgorithm<Output> algorithm) {
		this.algorithm = algorithm;
	}

	public SearchTask(final LocalSearchVariant variant, Input input) {
		this(new LocalSearch<>(MinBoxObjs.getRecommended(variant), switch (variant) {
			case LocalSearchVariant.Geometric -> new GeometricNeighborhood();
			case LocalSearchVariant.Overlap -> new OverlapNeighborhood();
			case LocalSearchVariant.Rules -> new RuleNeighborhood();
		}, SolutionConstructor.forLocal(input)));
	}

	public SearchTask(final GreedySearchVariant variant, Input input) {
		this(switch (variant) {
			case VariantA ->
				new GreedySearch<>(MinBoxObjs.getRecommended(variant), new ArrayDeque<>(),
					new SimpleNode(input));
			case VariantA2 ->
				new GreedySearch<>(MinBoxObjs.getRecommended(variant), new ArrayDeque<>(),
					new SortedSimpleNode(input));
			case VariantB ->
				new GreedySearch<>(MinBoxObjs.getRecommended(variant), new ArrayDeque<>(),
					new LegoNode(input));
			case VariantB2 ->
				new GreedySearch<>(MinBoxObjs.getRecommended(variant), new ArrayDeque<>(),
					new SortedLegoNode(input));
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
