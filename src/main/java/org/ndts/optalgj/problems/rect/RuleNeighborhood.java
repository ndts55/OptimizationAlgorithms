package org.ndts.optalgj.problems.rect;

import org.ndts.optalgj.algs.Neighborhood;
import org.ndts.optalgj.algs.ObjectiveFunction;
import org.ndts.optalgj.utils.RNG;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/*
Anstelle von zulässigen Lösungen, arbeitet die lokale Suche hier auf Permutationen von Rechtecken.
Analog zum Greedy-Algorithmus werden die Rechtecke in der Reihenfolge der Permutation in den Boxen
platziert. Als Regel: Die Nachbarschaft definieren Sie durch kleine Modifikationsschritte auf der
Permutation. Auch hier könnte es sinnvoll sein, Rechtecke in relativ leeren Boxen anderswo in der
Permutation zu platzieren.
 */

enum RuleAction {
	Swap, Move
}

public class RuleNeighborhood implements Neighborhood<Output> {
	// region Class Attributes
	protected final AtomicBoolean cancelled = new AtomicBoolean(false);
	protected int rectangleCount = -1;
	protected long lastSignificantImprovement = 0;
	protected long currentIteration = 0;
	// endregion

	// region Constants (but as functions)
	protected int maxActionCount() {return 200;}

	protected long stallThreshold() {return 100;}
	// endregion

	// region Cancellation
	@Override
	public void cancel() {
		cancelled.setRelease(true);
	}

	@Override
	public boolean isCancelled() {
		return cancelled.getAcquire();
	}
	// endregion

	// region Better Neighbor
	@Override
	public Output betterNeighbor(final Output initial, final ObjectiveFunction<Output> obj) {
		var initialEvaluation = obj.evaluate(initial);
		var output = findBetterNeighbor(initial, initialEvaluation, obj);
		var outputEvaluation = obj.evaluate(output);
		if (outputEvaluation < initialEvaluation) lastSignificantImprovement = currentIteration;
		if ((currentIteration - lastSignificantImprovement) >= stallThreshold()) return null;
		currentIteration += 1;
		return output;
	}

	protected Output findBetterNeighbor(final Output initial,
										final double initialEvaluation,
										final ObjectiveFunction<Output> obj) {
		// TODO implement rule-based neighborhood for local search
		var rectangles =
			initial.boxes().stream().flatMap(b -> b.rectangles().stream()).collect(Collectors.toCollection(() -> new ArrayList<>(Math.max(rectangleCount, 0))));
		rectangleCount = rectangles.size();
		final var output = new Output(initial);
		final var maxActionCount = maxActionCount();
		for (var i = 0; !isCancelled() && obj.evaluate(output) >= initialEvaluation && i < maxActionCount; i += 1)
			switch (RuleAction.values()[RNG.nextIndex(RuleAction.values().length)]) {
				case Swap -> swapRectangles(output, rectangles);
				case Move -> moveRectangles(output, rectangles);
			}
		return output;
	}
	// endregion

	// region Swap Rectangles
	protected void swapRectangles(final Output output,
								  final List<PositionedRectangle> rectangles) {
		// IDEAS
		// - swap two rectangles
		// - generate n pairs that are swapped
		// How do I determine whether this was a good move here?
		// What criteria is used to determine whether we should restore the previous state?
		throw new UnsupportedOperationException();
	}
	// endregion

	// region Move Rectangles
	protected void moveRectangles(final Output output,
								  final List<PositionedRectangle> rectangles) {
		throw new UnsupportedOperationException();
	}
	// endregion

	// region Utils
	protected boolean tryToFit(PositionedRectangle rectangle, Box box, int boxLength) {
		final var backupX = rectangle.x();
		final var backupY = rectangle.y();
		final var backupRotated = rectangle.rotated();
		final var smallestSide = Math.min(rectangle.width(), rectangle.height());
		final var maxRow = boxLength - smallestSide;
		var rowStart = 0;
		if (box.size() > 0) {
			final var lastRect = box.rectangles().getLast();
			rowStart = lastRect.y();
			if (lastRect.x() + lastRect.width() >= boxLength - smallestSide)
				rowStart += 1;
		}
		var fits = false;
		for (var row = rowStart; !isCancelled() && row < maxRow; row += 1) {
			if (row + rectangle.height() < boxLength) {
				for (var col = 0; !fits && col < boxLength - rectangle.width(); col += 1) {
					rectangle.transformTo(col, row);
					if (rectangle.outOfBounds(boxLength)) continue;
					fits = !box.wouldOverlap(rectangle);
				}
				if (fits) break;
			}
			if (row + rectangle.width() < boxLength) {
				rectangle.rotate();
				for (var col = 0; !fits && col < boxLength - rectangle.width(); col += 1) {
					rectangle.transformTo(col, row);
					if (rectangle.outOfBounds(boxLength)) continue;
					fits = !box.wouldOverlap(rectangle);
				}
				if (fits) break;
				else rectangle.rotate();
			}
		}
		if (!fits) rectangle.transformTo(backupX, backupY, backupRotated);
		return fits;
	}

	protected List<Box> constructBoxes(int boxLength, List<PositionedRectangle> rectangles) {
		final var boxes = new ArrayList<Box>(1) {{add(new Box());}};
		var i = 0;
		while (i < rectangleCount) {
			final var rectangle = rectangles.get(i);
			// try to fit into the last box
			final var box = boxes.getLast();
			if (tryToFit(rectangle, box, boxLength)) {
				box.add(rectangle);
				i += 1;
			} else boxes.add(new Box());
		}
		return boxes;
	}
	// endregion
}
