package org.ndts.optalgj.algs;

public class LocalSearch<Solution extends CopyConstructible<Solution>> implements OptimizationAlgorithm<Solution> {
	private final ObjectiveFunction<Solution> obj;
	private final Neighborhood<Solution> neighborhood;
	private Solution currentSolution;
	private Solution bestSolution;
	private int iteration = 0;

	public LocalSearch(ObjectiveFunction<Solution> objectiveFunction,
					   Neighborhood<Solution> neighborhood, Solution initialSolution) {
		this.obj = objectiveFunction;
		this.neighborhood = neighborhood;
		this.currentSolution = initialSolution;
		this.bestSolution = currentSolution;
	}

	@Override
	public boolean iterate() {
		currentSolution = neighborhood.betterNeighbor(currentSolution, obj);
		iteration += 1;
		if (currentSolution == null) return false;
		if (obj.evaluate(currentSolution) < obj.evaluate(bestSolution))
			bestSolution = currentSolution.copy();
		return true;
	}

	@Override
	public void cancel() {
		neighborhood.cancel();
	}

	@Override
	public Solution best() {
		return bestSolution;
	}

	@Override
	public Solution current() {
		return currentSolution.copy();
	}

	@Override
	public int iteration() {return iteration;}
}
