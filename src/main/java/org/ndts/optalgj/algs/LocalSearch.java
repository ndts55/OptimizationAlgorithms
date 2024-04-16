package org.ndts.optalgj.algs;

public class LocalSearch<Input, Output extends CopyConstructible<Output>> implements OptimizationAlgorithm<Input, Output> {
	private final SolutionConstructor<Input, Output> solutionConstructor;
	private final ObjectiveFunction<Output> obj;
	private final Neighborhood<Output> neighborhood;
	private Output currentSolution;
	private Output bestSolution;
	private int currentIteration = 0;

	public LocalSearch(SolutionConstructor<Input, Output> solutionConstructor,
					   ObjectiveFunction<Output> objectiveFunction,
					   Neighborhood<Output> neighborhood) {
		this.solutionConstructor = solutionConstructor;
		this.obj = objectiveFunction;
		this.neighborhood = neighborhood;
	}

	@Override
	public void initialize(Input input) {
		currentSolution = solutionConstructor.arbitrarySolution(input);
		bestSolution = currentSolution;
	}

	@Override
	public boolean iteration() {
		currentSolution = neighborhood.betterNeighbor(currentSolution, obj);
		currentIteration += 1;
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
	public Output bestOutput() {
		return bestSolution;
	}

	@Override
	public Output currentOutput() {
		return currentSolution.copy();
	}

	@Override
	public int currentIteration() {return currentIteration;}
}
