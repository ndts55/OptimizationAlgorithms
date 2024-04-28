package org.ndts.optalgj.problems.rect.neighborhood;

import org.ndts.optalgj.algs.Neighborhood;
import org.ndts.optalgj.algs.ObjectiveFunction;
import org.ndts.optalgj.problems.rect.domain.Box;
import org.ndts.optalgj.problems.rect.domain.Output;
import org.ndts.optalgj.problems.rect.domain.PositionedRectangle;
import org.ndts.optalgj.problems.rect.utils.Fits;
import org.ndts.optalgj.problems.rect.utils.RNG;

import java.util.ArrayList;
import java.util.Comparator;

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

public class GeometricNeighborhood extends Neighborhood<Output> {
	// region protected Attributes
	protected final Comparator<Box> boxComparator = Comparator.comparingInt(Box::size);
	// endregion

	// region Constants (but as functions)
	protected int maxActionCount() {return 200;}

	@Override
	protected long stallThreshold() {return 200;}

	// endregion

	// region Better Neighbor
	@Override
	protected Output findBetterNeighbor(final Output initial, final double initialEvaluation,
										final ObjectiveFunction<Output> obj) {
		var output = new Output(initial);
		final var maxActionCount = maxActionCount();
		for (var i = 0; !isCancelled() && obj.evaluate(output) >= initialEvaluation && i < maxActionCount; i++)
			switch (GeometricAction.values()[RNG.nextIndex(GeometricAction.values().length)]) {
				case MoveWithin -> moveRectangleInBox(output, 100);
				case MoveBetween -> moveRectangleToOtherBox(output);
				case Merge -> redistributeSmallestBox(output);
			}
		sortOutput(output);
		return output;
	}

	protected void sortOutput(final Output output) {
		output.boxes().sort(boxComparator);
	}
	// endregion

	// region Move Rectangle in Box
	protected void moveRectangleInBox(final Output output, int attempts) {
		for (var i = 0; !isCancelled() && i < attempts; i++) {
			if (attemptToMoveRectangleInBox(output)) return;
		}
	}

	protected boolean attemptToMoveRectangleInBox(final Output output) {
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
		for (var col = 0; !isCancelled() && col < backupX; col++)
			for (var row = 0; row < backupY; row++) {
				rectangle.transformTo(col, row);
				var fits = !rectangle.outOfBounds(boxLength) && canFitInSame(box, rectangleIndex);
				if (fits) return true;
				rectangle.rotate();
				fits = !rectangle.outOfBounds(boxLength) && canFitInSame(box, rectangleIndex);
				if (fits) return true;
				rectangle.rotate();
			}
		rectangle.transformTo(backupX, backupY);
		return false;
	}
	// endregion

	// region Move Rectangle to Other Box
	protected void moveRectangleToOtherBox(final Output output) {
		if (output.boxes().size() <= 1) return;// no other box exists
		final var sourceBox = pickSourceBox(output);
		var destinationBox = pickRandomBox(output);
		while (!isCancelled() && sourceBox == destinationBox)
			destinationBox = pickRandomBox(output);
		if (isCancelled()) return;
		final var rectangleIndex = RNG.nextIndex(sourceBox.size());
		final var rectangle = sourceBox.get(rectangleIndex);
		final var freeBoxArea =
			(output.boxLength() * output.boxLength()) - destinationBox.occupiedArea();
		if (freeBoxArea >= rectangle.area() && tryToFit(destinationBox, rectangle,
			output.boxLength())) {
			// move from source to destination
			destinationBox.add(sourceBox.remove(rectangleIndex));
			if (sourceBox.size() == 0) output.boxes().remove(sourceBox);
		}
	}

	protected Box pickSourceBox(final Output output) {
		// pick multiple random boxes and pick the one with the fewest number of rectangles
		final var n = output.boxes().size();
		final var firstBox = output.boxes().get(RNG.nextIndex(n / 2));
		final var secondBox = output.boxes().get(RNG.nextIndex(n));
		if (secondBox.size() < firstBox.size()) return secondBox;
		else return firstBox;
	}

	protected Box pickRandomBox(final Output o) {
		return o.boxes().get(RNG.nextIndex(o.boxes().size()));
	}
	// endregion

	// region Redistribute Smallest Box
	protected void redistributeSmallestBox(final Output output) {
		if (output.boxes().size() <= 1) return;
		final var box0 = output.boxes().getFirst();
		final var totalBoxArea = output.boxLength() * output.boxLength();

		for (var i = 1; box0.size() > 0 && i < output.boxes().size(); i += 1) {
			final var box1 = output.boxes().get(i);
			final var toRemove = new ArrayList<PositionedRectangle>();
			for (var rectangle : box0)
				if (totalBoxArea - box1.occupiedArea() >= rectangle.area() && tryToFit(box1,
					rectangle, output.boxLength())) {
					box1.add(rectangle);
					toRemove.add(rectangle);
				}
			box0.rectangles().removeAll(toRemove);
		}
		if (box0.size() == 0) output.boxes().removeFirst();
	}
	// endregion

	// region Utils
	protected boolean tryToFit(final Box box, final PositionedRectangle rectangle, int boxLength) {
		return Fits.tryToFit(box, rectangle, boxLength, this::isCancelled, this::canFitInOther);
	}

	protected boolean canFitInSame(final Box box, final int rectangleIndex) {
		return !box.overlapExistsAt(rectangleIndex);
	}

	protected boolean canFitInOther(final Box box, final PositionedRectangle rectangle) {
		return !box.wouldOverlap(rectangle);
	}
	// endregion
}
