package org.ndts.optalgj.algs;

public interface OptimizationAlgorithm<Input, Output> {
	/**
	 * @param input Problem instance.
	 */
	void initialize(Input input);

	/**
	 * @return Whether a better current solution was found.
	 */
	boolean iteration();

	void cancel();

	/**
	 * @return Best output so far.
	 */
	Output bestOutput();

	/**
	 * @return Current output.
	 */
	Output currentOutput();

	int currentIteration();
}
