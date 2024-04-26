package org.ndts.optalgj.problems.rect;

import org.ndts.optalgj.algs.Oracle;
import org.ndts.optalgj.algs.TraversalStrategy;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class FillTraversalStrategy extends TraversalStrategy<Output, FillWrapper> {
	public static Oracle<Output, FillWrapper> oracle() {
		return wrapper -> wrapper.rectangles().isEmpty();
	}

	public static Queue<FillWrapper> queue() {
		return new ArrayDeque<>();
	}

	public static FillWrapper initialData(Input input) {
		return new FillWrapper(new Output(input.boxLength()), input.rectangles());
	}

	@Override
	public List<FillWrapper> traverse(FillWrapper wrapper) {
		final var output = new Output(wrapper.solution());
		final var rectangles = new ArrayList<>(wrapper.rectangles());
		for (var box : output) {
			final var toRemove = new ArrayList<Rectangle>();
			for (var rect : rectangles) {
				final var pr = new PositionedRectangle(rect);
				if (tryToFit(box, pr, output.boxLength())) {
					box.add(pr);
					toRemove.add(rect);
				}
			}
			rectangles.removeAll(toRemove);
		}
		if (!rectangles.isEmpty()) output.boxes().add(new Box());
		return List.of(new FillWrapper(output, rectangles));
	}

	private boolean tryToFit(Box box, PositionedRectangle rectangle, int boxLength) {
		return Fits.tryToFit(box, rectangle, boxLength, this::isCancelled,
			(b, pr) -> !b.wouldOverlap(pr));
	}
}
