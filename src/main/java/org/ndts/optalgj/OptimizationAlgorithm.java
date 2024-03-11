package org.ndts.optalgj;

public interface OptimizationAlgorithm<Input, Solution> {
	Solution run(Input input);
}
