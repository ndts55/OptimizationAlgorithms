package org.ndts.optalgj.problems.rect;

import org.ndts.optalgj.algs.Neighborhood;
import org.ndts.optalgj.algs.ObjectiveFunction;
import org.ndts.optalgj.utils.RNG;

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
	private final int boxLength;
	private final int halfBoxLength;
	private final int quarterBoxLength;


	public GeometricNeighborhood(int boxLength) {
		this.boxLength = boxLength;
		halfBoxLength = boxLength / 2;
		quarterBoxLength = boxLength / 4;
	}

	@Override
	public Output betterNeighbor(Output initial, ObjectiveFunction<Output> obj) {
		// TODO move random rectangle within its box
		// TODO move random rectangle from one box to another
		// TODO rotate random rectangle within its box
		// TODO how to decide which of these actions to take?
		throw new UnsupportedOperationException();
	}

	private void moveRectangleInBox(final Output o, int attempts) {
		final var n = o.boxes().size();
		for (var i = 0; i < attempts; i++) {
			// get random box
			final var box = o.boxes().get(RNG.nextIndex(n));
			// get random rectangle in that box
			final var rectangleIndex = RNG.nextIndex(box.size());
			final var rectangle = box.get(rectangleIndex);
			// create backup of rectangle position
			final var backupX = rectangle.x();
			final var backupY = rectangle.y();
			// move rectangle a random amount within the box
			final var newX = Math.clamp(rectangle.x() + RNG.nextInt(halfBoxLength) - quarterBoxLength, 0, boxLength - rectangle.width() - 1);
			final var newY = Math.clamp(rectangle.y() + RNG.nextInt(halfBoxLength) - quarterBoxLength, 0, boxLength - rectangle.height() - 1);
			rectangle.transformTo(newX, newY);
			// if overlap then revert to previous values
			if (box.overlapsExistAt(rectangleIndex)) {
				rectangle.transformTo(backupX, backupY);
			} else {
				return;
			}
		}
	}

	private void rotateRectangle(final Output o, int attempts) {
		final var n = o.boxes().size();
		for (var i = 0; i < attempts; i++) {
			final var box = o.boxes().get(RNG.nextIndex(n));
			final var rectangleIndex = RNG.nextIndex(box.size());
			final var rectangle = box.get(rectangleIndex);
			rectangle.rotate();
			if (box.overlapsExistAt(rectangleIndex)) {
				rectangle.rotate();
			} else {
				return;
			}
		}
	}

	private void moveRectangleToOtherBox(final Output o) {
		if (o.boxes().size() <= 1) {
			// no other box exists
			return;
		}
		final var sourceBox = pickSourceBox(o);
		var destinationBox = pickDestinationBox(o);
		while (sourceBox == destinationBox) {
			destinationBox = pickDestinationBox(o);
		}
		final var sourceIndex = RNG.nextIndex(sourceBox.size());
		final var rectangle = sourceBox.get(sourceIndex);
		final var fits = tryToFit(rectangle, destinationBox);
		if (fits) {
			// move from source to destination
			destinationBox.add(sourceBox.remove(sourceIndex));
		}
	}


	private Box pickSourceBox(final Output o) {
		// pick multiple random boxes and pick the one with the fewest number of rectangles
		final var n = o.boxes().size();
		final var firstBox = o.boxes().get(RNG.nextIndex(n));
		if (firstBox.size() <= BOX_PICK_SIZE_THRESHOLD) {
			return firstBox;
		}
		final var secondBox = o.boxes().get(RNG.nextIndex(n));
		if (secondBox.size() < firstBox.size()) {
			return secondBox;
		} else {
			return firstBox;
		}
	}

	private Box pickDestinationBox(final Output o) {
		return o.boxes().get(RNG.nextIndex(o.boxes().size()));
	}

	private boolean tryToFit(PositionedRectangle rectangle, Box box) {
		if (box.freeArea() < rectangle.area()) {
			return false;
		}
		final var backupX = rectangle.x();
		final var backupY = rectangle.y();
		final var backupRotated = rectangle.rotated();
		final var smallerRectangleSide = Math.min(rectangle.width(), rectangle.height());
		var fits = false;
		for (var row = 0; row < boxLength - smallerRectangleSide; row += 1) {
			if (row + rectangle.height() < boxLength) {
				// check regular
				for (var col = 0; !fits && col < boxLength - rectangle.width(); col += 1) {
					rectangle.transformTo(col, row);
					fits = canFit(rectangle, box);
				}
				if (fits) {
					// found a fitting configuration
					break;
				}
			}
			// fits is still false at this point
			if (row + rectangle.width() < boxLength) {
				// check rotated
				rectangle.rotate();
				for (var col = 0; !fits && col < boxLength - rectangle.width(); col += 1) {
					rectangle.transformTo(col, row);
					fits = canFit(rectangle, box);
				}
				if (fits) {
					break;
				} else {
					// rotate back if no fit was found
					rectangle.rotate();
				}
			}
		}
		if (!fits) {
			rectangle.transformTo(backupX, backupY, backupRotated);
		}
		return fits;
	}

	private boolean canFit(PositionedRectangle rectangle, Box box) {
		for (var rect : box) {
			if (rect.overlapsWith(rectangle)) {
				return false;
			}
		}
		return true;
	}
}
