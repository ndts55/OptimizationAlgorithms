package org.ndts.optalgj.gui;

import javafx.beans.property.ReadOnlyLongProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.ndts.optalgj.algs.GreedySearchVariant;
import org.ndts.optalgj.algs.LocalSearchVariant;
import org.ndts.optalgj.problems.rect.domain.Input;
import org.ndts.optalgj.problems.rect.domain.Output;

public class SearchService extends Service<Output> {
	private final SearchTask task;
	private final ReadOnlyLongProperty iteration;

	public SearchService(SearchTask task) {
		this.task = task;
		this.iteration = task.iterationProperty();
	}

	public SearchService(final LocalSearchVariant neighborhoodVariant, Input input) {
		this(new SearchTask(neighborhoodVariant, input));
	}

	public SearchService(final GreedySearchVariant neighborhoodVariant, Input input) {
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
