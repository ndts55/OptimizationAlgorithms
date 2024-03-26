package org.ndts.optalgj.problems.rect;

import org.ndts.optalgj.algs.Neighborhood;
import org.ndts.optalgj.algs.ObjectiveFunction;
import org.ndts.optalgj.utils.RNG;

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
public class GeometricNeighborhood implements Neighborhood<Output> {
	private static final int BOX_PICK_SIZE_THRESHOLD = 2;
	private static final long MAX_NEIGHBOR_SEARCHES_WITHOUT_IMPROVEMENT = 1000; // TODO does this number make sense?
	private final AtomicBoolean cancelled = new AtomicBoolean(false);
	private long neighborSearchActionCount = 0;

	@Override
	public void cancel() {
		cancelled.setRelease(true);
	}

	@Override
	public boolean isCancelled() {
		return cancelled.getAcquire();
	}

	@Override
	public Output betterNeighbor(Output initial, ObjectiveFunction<Output> obj) {
		neighborSearchActionCount = 0;
		final var options = new int[]{0, 1, 2};
		final var selector = options[RNG.nextIndex(options.length)];
		final var initialEvaluation = obj.evaluate(initial);
		var output = initial.copy();
		var advancing = true;
		do advancing = findBetterNeighbor(selector, output, obj);
		while (!isCancelled() && advancing && obj.evaluate(output) > initialEvaluation);
		return output;
	}

	private boolean findBetterNeighbor(int selector, Output output, ObjectiveFunction<Output> obj) {
		final var evaluationBefore = obj.evaluate(output);
		// TODO should the number of attempts depend on the total number of rectangles somehow?
		final var actionTaken = switch (selector) {
			case 0 ->
				// move random rectangle within its box
				moveRectangleInBox(output, 100);
			case 1 ->
				// move random rectangle from one box to another
				moveRectangleToOtherBox(output);
			case 2 ->
				// rotate random rectangle within its box
				rotateRectangle(output, 100);
			default -> throw new UnsupportedOperationException();
		};
		if (actionTaken) neighborSearchActionCount += 1;
		return obj.evaluate(output) < evaluationBefore && neighborSearchActionCount <= MAX_NEIGHBOR_SEARCHES_WITHOUT_IMPROVEMENT;
	}


	private boolean moveRectangleInBox(final Output o, int attempts) {
		final var boxLength = o.boxLength();
		final var halfBoxLength = boxLength / 2;
		final var quarterBoxLength = halfBoxLength / 2;
		final var n = o.boxes().size();
		for (var i = 0; !isCancelled() && i < attempts; i++) {
			// get random box
			final var box = o.boxes().get(RNG.nextIndex(n));
			// get random rectangle in that box
			final var rectangleIndex = RNG.nextIndex(box.size());
			final var rectangle = box.get(rectangleIndex);
			// create backup of rectangle position
			final var backupX = rectangle.x();
			final var backupY = rectangle.y();
			// move rectangle a random amount within the box
			final var newX = Math.clamp(rectangle.x() + RNG.nextInt(halfBoxLength) - quarterBoxLength, 0, boxLength - rectangle.width());
			final var newY = Math.clamp(rectangle.y() + RNG.nextInt(halfBoxLength) - quarterBoxLength, 0, boxLength - rectangle.height());
			rectangle.transformTo(newX, newY);
			// if overlap then revert to previous values
			if (rectangle.outOfBounds(boxLength) || box.overlapsExistAt(rectangleIndex))
				rectangle.transformTo(backupX, backupY);
			else return true;

		}
		return false;
	}

	private boolean rotateRectangle(final Output o, int attempts) {
		final var n = o.boxes().size();
		for (var i = 0; !isCancelled() && i < attempts; i++) {
			final var box = o.boxes().get(RNG.nextIndex(n));
			final var rectangleIndex = RNG.nextIndex(box.size());
			final var rectangle = box.get(rectangleIndex);
			rectangle.rotate();
			if (rectangle.outOfBounds(o.boxLength()) || box.overlapsExistAt(rectangleIndex))
				rectangle.rotate();
			else return true;
		}
		return false;
	}

	private boolean moveRectangleToOtherBox(final Output o) {
		if (o.boxes().size() <= 1) return false;// no other box exists
		final var sourceBox = pickSourceBox(o);
		var destinationBox = pickDestinationBox(o);
		while (!isCancelled() && sourceBox == destinationBox)
			destinationBox = pickDestinationBox(o);
		if (isCancelled()) return false;
		final var rectangleIndex = RNG.nextIndex(sourceBox.size());
		final var rectangle = sourceBox.get(rectangleIndex);
		final var freeBoxArea = (o.boxLength() * o.boxLength()) - destinationBox.occupiedArea();
		if (freeBoxArea >= rectangle.area()) {
			final var fits = tryToFit(rectangle, destinationBox, o.boxLength());
			// move from source to destination
			if (fits) destinationBox.add(sourceBox.remove(rectangleIndex));
		}
		if (sourceBox.size() == 0) o.boxes().remove(sourceBox);
		return true;
	}


	private Box pickSourceBox(final Output o) {
		// pick multiple random boxes and pick the one with the fewest number of rectangles
		final var n = o.boxes().size();
		final var firstBox = o.boxes().get(RNG.nextIndex(n));
		if (firstBox.size() <= BOX_PICK_SIZE_THRESHOLD) return firstBox;
		final var secondBox = o.boxes().get(RNG.nextIndex(n));
		if (secondBox.size() < firstBox.size()) return secondBox;
		else return firstBox;
	}

	private Box pickDestinationBox(final Output o) {
		return o.boxes().get(RNG.nextIndex(o.boxes().size()));
	}

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
					fits = canFit(rectangle, box);
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
					fits = canFit(rectangle, box);
				}
				// rotate back if no fit was found
				if (fits) break;
				else rectangle.rotate();

			}
		}
		if (!fits) rectangle.transformTo(backupX, backupY, backupRotated);
		return fits;
	}

	private boolean canFit(PositionedRectangle rectangle, Box box) {
		for (var rect : box) if (rect.overlapsWith(rectangle)) return false;
		return true;
	}
}
