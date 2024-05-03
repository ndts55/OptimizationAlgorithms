package org.ndts.optalgj.problems.rect.node;

import org.ndts.optalgj.algs.GreedyNode;
import org.ndts.optalgj.problems.rect.domain.*;
import org.ndts.optalgj.problems.rect.utils.Fits;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public record SimpleNode(Output output,
						 ArrayList<Rectangle> rectangles) implements GreedyNode<Output,
	SimpleNode> {
	public SimpleNode(Input input) {
		this(new Output(input.boxLength(), new ArrayList<>() {{
			add(new Box());
		}}), new ArrayList<>(input.rectangles()));
		rectangles.sort(Comparator.comparingInt(Rectangle::area).reversed());
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
		final var box = child.boxes().getLast();

		fillBox(box, child.boxLength(), rects);

		if (!rects.isEmpty()) child.boxes().add(new Box());
		return List.of(new SimpleNode(child, rects));
	}

	private void fillBox(Box box, int boxLength, List<Rectangle> rects) {
		final var toRemove = new ArrayList<Rectangle>();
		final var boxArea = output.boxLength() * output.boxLength();
		for (var rect : rects) {
			if (boxArea == box.occupiedArea()) break;
			final var pr = new PositionedRectangle(rect);
			if (tryToFit(box, pr, boxLength)) {
				box.add(pr);
				toRemove.add(rect);
			}
		}
		rects.removeAll(toRemove);
	}

	@Override
	public boolean isLeaf() {return rectangles.isEmpty();}

	private boolean tryToFit(Box box, PositionedRectangle rectangle, int boxLength) {
		return Fits.tryToFit(box, rectangle, boxLength, () -> false,
			(b, pr) -> !b.wouldOverlap(pr));
	}
}
