package integration;

import org.junit.jupiter.api.Test;
import org.ndts.optalgj.algs.GreedySearch;
import org.ndts.optalgj.algs.GreedySearchVariant;
import org.ndts.optalgj.problems.rect.MinBoxObjs;
import org.ndts.optalgj.problems.rect.node.LegoNode;
import org.ndts.optalgj.problems.rect.node.SimpleNode;
import org.ndts.optalgj.problems.rect.node.SortedLegoNode;
import org.ndts.optalgj.problems.rect.node.SortedSimpleNode;

import java.util.ArrayDeque;

import static integration.Utils.getInput;
import static integration.Utils.runSearch;

public class TestGreedy {
	@Test
	public void testVariantA() {
		runSearch("Simple",
			new GreedySearch<>(MinBoxObjs.getRecommended(GreedySearchVariant.VariantA),
				new ArrayDeque<>(), new SimpleNode(getInput())));
	}

	@Test
	public void testVariantA2() {
		runSearch("Simple",
			new GreedySearch<>(MinBoxObjs.getRecommended(GreedySearchVariant.VariantA),
				new ArrayDeque<>(), new SortedSimpleNode(getInput())));
	}

	@Test
	public void testVariantB() {
		runSearch("Lego",
			new GreedySearch<>(MinBoxObjs.getRecommended(GreedySearchVariant.VariantB),
				new ArrayDeque<>(), new LegoNode(getInput())));
	}

	@Test
	public void testVariantB2() {
		runSearch("Lego",
			new GreedySearch<>(MinBoxObjs.getRecommended(GreedySearchVariant.VariantB),
				new ArrayDeque<>(), new SortedLegoNode(getInput())));
	}
}
