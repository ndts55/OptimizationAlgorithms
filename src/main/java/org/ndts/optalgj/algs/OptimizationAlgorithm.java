package org.ndts.optalgj.algs;

public interface OptimizationAlgorithm<Solution> {
	/**
	 * @return Whether a better current solution was found.
	 */
	boolean iterate();

	void cancel();

	/**
	 * @return Best output so far.
	 */
	Solution best();

	/**
	 * @return Current output.
	 */
	Solution current();

	int iteration();
}
