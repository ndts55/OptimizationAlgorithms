package integration;

import org.junit.jupiter.api.Test;
import org.ndts.optalgj.algs.LocalSearch;
import org.ndts.optalgj.algs.LocalSearchVariant;
import org.ndts.optalgj.problems.rect.MinBoxObjs;
import org.ndts.optalgj.problems.rect.domain.Output;
import org.ndts.optalgj.problems.rect.neighborhood.GeometricNeighborhood;
import org.ndts.optalgj.problems.rect.neighborhood.OverlapNeighborhood;
import org.ndts.optalgj.problems.rect.neighborhood.RuleNeighborhood;
import org.ndts.optalgj.problems.rect.utils.SolutionConstructor;

import static integration.Utils.getInput;
import static integration.Utils.runSearch;

public class TestLocal {
	private static LocalSearch<Output> constructSearch(LocalSearchVariant variant) {
		return new LocalSearch<>(MinBoxObjs.getRecommended(variant), switch (variant) {
			case Geometric -> new GeometricNeighborhood();
			case Rules -> new RuleNeighborhood();
			case Overlap -> new OverlapNeighborhood();
		}, SolutionConstructor.forLocal(getInput()));
	}

	private static void runTest(String name, LocalSearchVariant variant) {
		runSearch(name, constructSearch(variant));
	}

	@Test
	public void testGeometric() {
		runTest("Geometric", LocalSearchVariant.Geometric);
	}

	@Test
	public void testRules() {
		runTest("Rules", LocalSearchVariant.Rules);
	}

	@Test
	public void testOverlap() {
		runTest("Overlap", LocalSearchVariant.Overlap);
	}
}
