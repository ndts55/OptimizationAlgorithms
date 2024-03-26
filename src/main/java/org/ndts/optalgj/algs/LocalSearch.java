package org.ndts.optalgj.algs;

public class LocalSearch<Input, Output extends CopyConstructible<Output>> implements OptimizationAlgorithm<Input, Output> {
	private final FeasibleSolutions<Input, Output> feasibleSolutions;
	private final ObjectiveFunction<Output> obj;
	private final Neighborhood<Output> neighborhood;
	private Output currentSolution;
	private Output bestSolution;

	public LocalSearch(FeasibleSolutions<Input, Output> feasibleSolutions, ObjectiveFunction<Output> objectiveFunction, Neighborhood<Output> neighborhood) {
		this.feasibleSolutions = feasibleSolutions;
		this.obj = objectiveFunction;
		this.neighborhood = neighborhood;
	}

	@Override
	public void initialize(Input input) {
		currentSolution = feasibleSolutions.arbitrarySolution(input);
		bestSolution = currentSolution;
	}

	@Override
	public boolean iteration() {
		currentSolution = neighborhood.betterNeighbor(currentSolution, obj);
		if (currentSolution == null) return false;
		var currentEvaluation = obj.evaluate(currentSolution);
		var bestEvaluation = obj.evaluate(bestSolution);
		if (currentEvaluation <= bestEvaluation) bestSolution = currentSolution.copy();
		return true;
	}

	@Override
	public void cancel() {
		neighborhood.cancel();
	}

	@Override
	public Output bestOutput() {
		return bestSolution;
	}

	@Override
	public Output currentOutput() {
		return currentSolution.copy();
	}
}
