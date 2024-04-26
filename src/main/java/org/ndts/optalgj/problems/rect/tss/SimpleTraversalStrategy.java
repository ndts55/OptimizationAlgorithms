package org.ndts.optalgj.problems.rect.tss;

import org.ndts.optalgj.algs.Oracle;
import org.ndts.optalgj.algs.TraversalStrategy;
import org.ndts.optalgj.problems.rect.domain.*;
import org.ndts.optalgj.problems.rect.utils.Fits;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class SimpleTraversalStrategy extends TraversalStrategy<Output, SimpleWrapper> {
	public static Oracle<Output, SimpleWrapper> oracle() {
		return wrapper -> wrapper.rectangles().isEmpty();
	}

	public static Queue<SimpleWrapper> queue() {
		return new ArrayDeque<>();
	}

	public static SimpleWrapper initialData(Input input) {
		return new SimpleWrapper(new Output(input.boxLength()), input.rectangles());
	}

	@Override
	public List<SimpleWrapper> traverse(SimpleWrapper wrapper) {
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
		return List.of(new SimpleWrapper(output, rectangles));
	}

	private boolean tryToFit(Box box, PositionedRectangle rectangle, int boxLength) {
		return Fits.tryToFit(box, rectangle, boxLength, this::isCancelled,
			(b, pr) -> !b.wouldOverlap(pr));
	}
}
