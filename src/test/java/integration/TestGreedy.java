package integration;

import org.junit.jupiter.api.Test;
import org.ndts.optalgj.algs.GreedySearch;
import org.ndts.optalgj.algs.GreedySearchVariant;
import org.ndts.optalgj.problems.rect.MinBoxObjs;
import org.ndts.optalgj.problems.rect.node.SimpleNode;

import java.util.ArrayDeque;

import static integration.Utils.getInput;
import static integration.Utils.runSearch;

public class TestGreedy {
	@Test
	public void testVariantA() {
		runSearch("Variant A",
			new GreedySearch<>(MinBoxObjs.getRecommended(GreedySearchVariant.VariantA),
				new ArrayDeque<>(), new SimpleNode(getInput())));
	}
}
