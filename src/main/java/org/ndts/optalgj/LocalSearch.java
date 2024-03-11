package org.ndts.optalgj;

public class LocalSearch<Input, Solution> implements OptimizationAlgorithm<Input, Solution> {
	private final FeasibleSolutions<Input, Solution> feasibleSolutions;
	private final ObjectiveFunction<Solution> objectiveFunction;
	private final Neighborhood<Solution> neighborhood;

	public LocalSearch(FeasibleSolutions<Input, Solution> feasibleSolutions, ObjectiveFunction<Solution> objectiveFunction, Neighborhood<Solution> neighborhood) {
		this.feasibleSolutions = feasibleSolutions;
		this.objectiveFunction = objectiveFunction;
		this.neighborhood = neighborhood;
	}

	public Solution run(Input input) {
		var currentSolution = feasibleSolutions.arbitrarySolution(input);
		do {
			var neighbors = neighborhood.apply(currentSolution);
			if (neighbors.isEmpty()) {
				break;
			}
			var minSolution = currentSolution;
			var minObjective = objectiveFunction.apply(minSolution);
			for (var neighborSolution : neighbors) {
				var neighborObjective = objectiveFunction.apply(neighborSolution);
				if (minObjective > neighborObjective) {
					minSolution = neighborSolution;
					minObjective = neighborObjective;
				}
			}
			if (minSolution == currentSolution) {
				break;
			} else {
				currentSolution = minSolution;
			}
		} while (true);
		return currentSolution;
	}
}
