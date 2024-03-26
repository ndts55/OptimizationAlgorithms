package org.ndts.optalgj.gui;

import javafx.beans.property.ReadOnlyLongProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.ndts.optalgj.algs.GreedyNeighborhoodVariant;
import org.ndts.optalgj.algs.LocalNeighborhoodVariant;
import org.ndts.optalgj.problems.rect.Input;
import org.ndts.optalgj.problems.rect.Output;

public class SearchService extends Service<Output> {
	private final SearchTask task;
	private final ReadOnlyLongProperty iteration;

	public SearchService(SearchTask task) {
		this.task = task;
		this.iteration = task.iterationProperty();
	}

	public SearchService(final LocalNeighborhoodVariant neighborhoodVariant, Input input) {
		this(new SearchTask(neighborhoodVariant, input));
	}

	public SearchService(final GreedyNeighborhoodVariant neighborhoodVariant, Input input) {
		this(new SearchTask(neighborhoodVariant, input));
	}

	public ReadOnlyLongProperty iterationProperty() {
		return iteration;
	}

	@Override
	protected Task<Output> createTask() {
		return task;
	}
}
