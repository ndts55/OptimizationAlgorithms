package org.ndts.optalgj.problems.rect.node;

import org.ndts.optalgj.algs.GreedyNode;
import org.ndts.optalgj.problems.rect.domain.Input;
import org.ndts.optalgj.problems.rect.domain.Output;
import org.ndts.optalgj.problems.rect.domain.PositionedRectangle;
import org.ndts.optalgj.problems.rect.domain.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public record LegoNode(Output output, TreeSet<PositionedRectangle> tree,
					   List<Rectangle> rectangles) implements GreedyNode<Output, LegoNode> {
	public LegoNode(Input input) {
		this(new Output(input.boxLength()), new TreeSet<>(), input.rectangles());
	}

	@Override
	public Output solution() {
		return output;
	}

	@Override
	public List<LegoNode> descend() {
		if (isLeaf()) return new ArrayList<>();
		final var child = new Output(output);
		final var rects = new ArrayList<>(rectangles);
		final var box = child.boxes().getLast();
		return List.of();
	}

	@Override
	public boolean isLeaf() {
		return rectangles.isEmpty();
	}
}
