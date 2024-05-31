package org.ndts.optalgj.problems.rect.node;

import org.junit.jupiter.api.Test;
import org.ndts.optalgj.problems.rect.domain.Input;
import org.ndts.optalgj.problems.rect.domain.Rectangle;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestLegoNode {
	@Test
	public void testFits1x1Rects() {
		final var boxLength = 20;
		final var rectangles =
			IntStream.range(0, boxLength * boxLength).mapToObj(i -> new Rectangle(i, 1, 1)).collect(Collectors.toCollection(ArrayList<Rectangle>::new));
		var node = new LegoNode(new Input(rectangles, boxLength));
		while (!node.isLeaf()) node = node.descend().getFirst();
		assertEquals(1, node.solution().boxes().size());
	}
}
