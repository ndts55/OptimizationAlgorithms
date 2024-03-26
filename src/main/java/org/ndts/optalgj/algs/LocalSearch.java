package org.ndts.optalgj.algs;

public class LocalSearch<Input, Output extends DeepCopy<Output>> implements OptimizationAlgorithm<Input, Output> {
	private final FeasibleSolutions<Input, Output> feasibleSolutions;
	private final ObjectiveFunction<Output> obj;
	private final Neighborhood<Output> neighborhood;

	public LocalSearch(FeasibleSolutions<Input, Output> feasibleSolutions, ObjectiveFunction<Output> objectiveFunction, Neighborhood<Output> neighborhood) {
		this.feasibleSolutions = feasibleSolutions;
		this.obj = objectiveFunction;
		this.neighborhood = neighborhood;
	}

	@Override
	public Output apply(Input input) {
		var currentSolution = feasibleSolutions.arbitrarySolution(input);
		var bestSolution = currentSolution;
		do {
			currentSolution = neighborhood.betterNeighbor(currentSolution, obj);
			if (currentSolution == null) {
				// No better neighbor found.
				break;
			}
			var currentEvaluation = obj.evaluate(currentSolution);
			var bestEvaluation = obj.evaluate(bestSolution);
			if (currentEvaluation <= bestEvaluation) {
				bestSolution = currentSolution.deepCopy();
			}
		} while (true);
		return bestSolution;
	}
}
