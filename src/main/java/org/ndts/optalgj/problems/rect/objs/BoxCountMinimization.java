package org.ndts.optalgj.problems.rect.objs;

import org.ndts.optalgj.algs.ObjectiveFunction;
import org.ndts.optalgj.problems.rect.domain.Output;

public class BoxCountMinimization implements ObjectiveFunction<Output> {
	@Override
	public Double evaluate(Output output) {
		return (double) output.boxes().size();
	}
}
