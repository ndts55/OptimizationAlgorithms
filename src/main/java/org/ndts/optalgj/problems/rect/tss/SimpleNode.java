package org.ndts.optalgj.problems.rect.tss;

import org.ndts.optalgj.algs.GreedyNode;
import org.ndts.optalgj.problems.rect.domain.*;
import org.ndts.optalgj.problems.rect.utils.Fits;

import java.util.ArrayList;
import java.util.List;

public record SimpleNode(Output output,
						 List<Rectangle> rectangles) implements GreedyNode<Output, SimpleNode> {
	public SimpleNode(Input input) {
		this(new Output(input.boxLength()), input.rectangles());
	}

	@Override
	public Output solution() {
		return output;
	}

	@Override
	public List<SimpleNode> descend() {
		if (isLeaf()) return new ArrayList<>();
		final var child = new Output(output);
		final var rects = new ArrayList<>(rectangles);
		for (var box : child) {
			final var toRemove = new ArrayList<Rectangle>();
			for (var rect : rects) {
				final var pr = new PositionedRectangle(rect);
				if (tryToFit(box, pr, child.boxLength())) {
					box.add(pr);
					toRemove.add(rect);
				}
			}
			rects.removeAll(toRemove);
		}
		if (!rects.isEmpty()) child.boxes().add(new Box());
		return List.of(new SimpleNode(child, rects));
	}

	@Override
	public boolean isLeaf() {return rectangles.isEmpty();}

	private boolean tryToFit(Box box, PositionedRectangle rectangle, int boxLength) {
		return Fits.tryToFit(box, rectangle, boxLength, () -> false,
			(b, pr) -> !b.wouldOverlap(pr));
	}
}
