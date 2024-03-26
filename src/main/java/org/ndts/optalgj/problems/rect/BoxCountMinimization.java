package org.ndts.optalgj.problems.rect;

import org.ndts.optalgj.algs.ObjectiveFunction;

public class BoxCountMinimization implements ObjectiveFunction<Output> {
	@Override
	public Double evaluate(Output output) {
		return (double) output.boxes().size();
	}
}
