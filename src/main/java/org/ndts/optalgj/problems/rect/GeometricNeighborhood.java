package org.ndts.optalgj.problems.rect;

import org.ndts.optalgj.algs.Neighborhood;
import org.ndts.optalgj.algs.ObjectiveFunction;
import org.ndts.optalgj.utils.RNG;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicBoolean;

/*
Ein Nachbar lässt sich erzeugen, indem Rechtecke direkt verschoben werden, sowohl innerhalb einer
Box als auch von einer Box zur anderen. Im Prinzip sind Sie frei darin, wie Sie das machen. Sie sind
auch frei darin, die Zielfunktion so abzuändern, dass Nachbarn auch dann besser sind, wenn die
eigentlich zu minimierende Zielfunktion nicht besser ist, der Nachbar aber nach heuristischen
Überlegungen näher an einer Verbesserung dran ist. Zum Beispiel könnte es sinnvoll sein, einen
Schritt zu belohnen, bei dem die Anzahl Rechtecke in einer Box, in der ohnehin nur wenige Rechtecke
in dieser Box sind, weiter verringert wird, auch wenn die Box damit (noch) nicht leer ist.
 */

enum GeometricAction {
	MoveWithin, MoveBetween, Merge
}

public class GeometricNeighborhood implements Neighborhood<Output> {
	// region Constants
	private static final long STALL_THRESHOLD = 1000;
	private static final int MAX_ACTION_COUNT = 1000;
	// endregion

	// region Private Attributes
	private final AtomicBoolean cancelled = new AtomicBoolean(false);
	protected long lastSignificantImprovement = 0;
	protected long currentIteration = 0;
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
		var output = findBetterNeighbor(initial, obj);
		var outputEvaluation = obj.evaluate(output);
		if (outputEvaluation < initialEvaluation) lastSignificantImprovement = currentIteration;
		if ((currentIteration - lastSignificantImprovement) >= STALL_THRESHOLD) return null;
		currentIteration += 1;
		return output;
	}

	protected Output findBetterNeighbor(final Output initial,
										final ObjectiveFunction<Output> obj) {
		final var initialEvaluation = obj.evaluate(initial);
		var output = initial.copy();
		for (var i = 0; !isCancelled() && obj.evaluate(output) >= initialEvaluation && i < MAX_ACTION_COUNT; i++)
			switch (GeometricAction.values()[RNG.nextIndex(GeometricAction.values().length)]) {
				case MoveWithin -> moveRectangleInBox(output, 100);
				case MoveBetween -> moveRectangleToOtherBox(output);
				case Merge -> mergeSmallestBoxes(output);
			}
		output.boxes().sort(Comparator.comparingInt(Box::size));
		return output;
	}
	// endregion

	// region Move Rectangle in Box
	private boolean moveRectangleInBox(final Output output, int attempts) {
		for (var i = 0; !isCancelled() && i < attempts; i++) {
			if (attemptToMoveRectangleInBox(output)) return true;
		}
		return false;
	}

	private boolean attemptToMoveRectangleInBox(final Output output) {
		final var box = pickRandomBox(output);
		if (box.size() == 1) {
			final var r = box.get(0);
			if (r.x() != 0 || r.y() != 0) {
				r.transformTo(0, 0);
				return true;
			} else return false;
		}
		// get random rectangle in that box
		final var rectangleIndex = RNG.nextIndex(box.size());
		final var rectangle = box.get(rectangleIndex);
		// create backup of rectangle position
		final var backupX = rectangle.x();
		final var backupY = rectangle.y();
		// move rectangle to the smallest position
		final var boxLength = output.boxLength();
		for (var col = 0; !isCancelled() && col < rectangle.x(); col++)
			if (rectangle.y() != col) for (var row = 0; row < rectangle.y(); row++) {
				rectangle.transformTo(col, row);
				var fits = !rectangle.outOfBounds(boxLength) && canFitInSame(rectangleIndex, box);
				if (fits) return true;
				rectangle.rotate();
				fits = !rectangle.outOfBounds(boxLength) && canFitInSame(rectangleIndex, box);
				if (fits) return true;
				rectangle.rotate();
			}
		rectangle.transformTo(backupX, backupY);
		return false;
	}
	// endregion

	// region Move Rectangle to Other Box
	private boolean moveRectangleToOtherBox(final Output output) {
		if (output.boxes().size() <= 1) return false;// no other box exists
		final var sourceBox = pickSourceBox(output);
		var destinationBox = pickRandomBox(output);
		while (!isCancelled() && sourceBox == destinationBox)
			destinationBox = pickRandomBox(output);
		if (isCancelled()) return false;
		final var rectangleIndex = RNG.nextIndex(sourceBox.size());
		final var rectangle = sourceBox.get(rectangleIndex);
		final var freeBoxArea =
			(output.boxLength() * output.boxLength()) - destinationBox.occupiedArea();
		if (freeBoxArea >= rectangle.area() && tryToFit(rectangle, destinationBox,
			output.boxLength())) {
			// move from source to destination
			destinationBox.add(sourceBox.remove(rectangleIndex));
			if (sourceBox.size() == 0) output.boxes().remove(sourceBox);
		}
		return true;
	}

	private Box pickSourceBox(final Output output) {
		// pick multiple random boxes and pick the one with the fewest number of rectangles
		final var n = output.boxes().size();
		final var firstBox = output.boxes().get(RNG.nextIndex(n / 2));
		final var secondBox = output.boxes().get(RNG.nextIndex(n));
		if (secondBox.size() < firstBox.size()) return secondBox;
		else return firstBox;
	}

	private Box pickRandomBox(final Output o) {
		return o.boxes().get(RNG.nextIndex(o.boxes().size()));
	}
	// endregion

	// region Merge Smallest Boxes
	private boolean mergeSmallestBoxes(final Output output) {
		if (output.boxes().size() <= 1) return false;
		final var box0 = output.boxes().getFirst();
		final var box1 = output.boxes().get(1);
		final var totalBoxArea = output.boxLength() * output.boxLength();
		var toRemove = new ArrayList<PositionedRectangle>();
		for (var rectangle : box0)
			if (totalBoxArea - box1.occupiedArea() >= rectangle.area() && tryToFit(rectangle, box1
				, output.boxLength())) {
				box1.add(rectangle);
				toRemove.add(rectangle);
			}
		box0.rectangles().removeAll(toRemove);
		if (box0.size() == 0) output.boxes().removeFirst();
		return !toRemove.isEmpty();
	}
	// endregion

	// region Utils
	private boolean tryToFit(PositionedRectangle rectangle, Box box, int boxLength) {
		final var backupX = rectangle.x();
		final var backupY = rectangle.y();
		final var backupRotated = rectangle.rotated();
		final var smallerRectangleSide = Math.min(rectangle.width(), rectangle.height());
		final var maxRow = boxLength - smallerRectangleSide;
		var fits = false;
		for (var row = 0; !isCancelled() && row < maxRow; row += 1) {
			if (row + rectangle.height() < boxLength) {
				// check regular
				for (var col = 0; !fits && col < boxLength - rectangle.width(); col += 1) {
					rectangle.transformTo(col, row);
					if (rectangle.outOfBounds(boxLength)) continue;
					fits = canFitInOther(rectangle, box);
				}
				// found a fitting configuration
				if (fits) break;

			}
			// fits is still false at this point
			if (row + rectangle.width() < boxLength) {
				// check rotated
				rectangle.rotate();
				for (var col = 0; !fits && col < boxLength - rectangle.width(); col += 1) {
					rectangle.transformTo(col, row);
					if (rectangle.outOfBounds(boxLength)) continue;
					fits = canFitInOther(rectangle, box);
				}
				// rotate back if no fit was found
				if (fits) break;
				else rectangle.rotate();

			}
		}
		if (!fits) rectangle.transformTo(backupX, backupY, backupRotated);
		return fits;
	}

	protected boolean canFitInSame(final int rectangleIndex, final Box box) {
		return !box.overlapExistsAt(rectangleIndex);
	}

	protected boolean canFitInOther(final PositionedRectangle rectangle, final Box box) {
		return !box.wouldOverlap(rectangle);
	}
	// endregion
}
