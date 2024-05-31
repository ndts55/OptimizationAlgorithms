package org.ndts.optalgj.problems.rect.node;

import org.ndts.optalgj.algs.GreedyNode;
import org.ndts.optalgj.problems.rect.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

record Position(int x, int y, int remWidth, int remHeight) {
}

public class LegoNode implements GreedyNode<Output, LegoNode> {
	protected final List<Rectangle> rectangles;
	private final List<List<Position>> freePositionsPerBox;
	private final Output output;

	private LegoNode(Output output, List<List<Position>> freePositionsPerBox,
					 List<Rectangle> rectangles) {
		this.output = output;
		this.freePositionsPerBox = freePositionsPerBox;
		this.rectangles = rectangles;
	}

	public LegoNode(Input input) {
		this(new Output(input.boxLength(), new ArrayList<>() {{
			add(new Box());
		}}), new ArrayList<>() {{
			add(new ArrayList<>() {{
				add(new Position(0, 0, input.boxLength(), input.boxLength()));
			}});
		}}, new ArrayList<>(input.rectangles()));
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
		final var nextPositionsPerBox = new ArrayList<List<Position>>();

		for (var i = 0; i < freePositionsPerBox.size(); i += 1) {
			final var box = child.boxes().get(i);
			final var freePositions = freePositionsPerBox.get(i);
			nextPositionsPerBox.add(new ArrayList<>());
			final var nextPositions = nextPositionsPerBox.getLast();
			for (final var pos : freePositions) {
				PositionedRectangle pr = null;
				for (final var rect : rects) {
					final var rotated = findFittingRotation(rect, pos);
					if (rotated.isPresent()) {
						pr = new PositionedRectangle(rect, pos.x(), pos.y(), rotated.get());
						break;
					}
				}
				if (pr != null) {
					rects.remove(pr.rectangle());
					box.add(pr);
					// position to the right
					if (pos.remWidth() > pr.width())
						nextPositions.add(new Position(pos.x() + pr.width(), pos.y(),
							pos.remWidth() - pr.width(), pr.height()));
					// position below
					if (pos.remHeight() > pr.height())
						nextPositions.add(new Position(pos.x(), pos.y() + pr.height(),
							pos.remWidth(), pos.remHeight() - pr.height()));
				} else nextPositions.add(pos);
			}
		}

		if (rectangles.size() == rects.size()) {
			child.boxes().add(new Box());
			nextPositionsPerBox.add(new ArrayList<>() {{
				add(new Position(0, 0, child.boxLength(), child.boxLength()));
			}});
		}

		return List.of(new LegoNode(child, nextPositionsPerBox, rects));
	}

	private Optional<Boolean> findFittingRotation(Rectangle rect, Position pos) {
		if (Math.min(rect.width(), rect.height()) > Math.max(pos.remWidth(), pos.remHeight()))
			return Optional.empty();
		return Stream.of(false, true).filter(rotated -> {
			final var width = rotated ? rect.height() : rect.width();
			final var height = rotated ? rect.width() : rect.height();
			return (pos.remWidth() >= width) && (pos.remHeight() >= height);
		}).findAny();
	}

	@Override
	public boolean isLeaf() {
		return rectangles.isEmpty();
	}
}
