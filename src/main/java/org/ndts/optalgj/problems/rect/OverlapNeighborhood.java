package org.ndts.optalgj.problems.rect;

/*
Die geometriebasierte Nachbarschaft wird angepasst auf die Situation, dass Rechtecke sich zu einem
gewissen Prozentsatz überlappen dürfen. Die Überlappung zweier Rechtecke ist dabei die gemeinsame
Fläche geteilt durch das Maximum der beiden Rechteckflächen. Dieser Prozentsatz ist zu Beginn 100
(so, dass eine Optimallösung einfach zu finden ist). Im Laufe der Zeit reduziert sich der
Prozentsatz, und Verletzungen werden hart in der Zielfunktion bestraft. Am Ende müssen Sie natürlich
dafür sorgen, dass schlussendlich eine garantiert überlappungsfreie Lösung entsteht.
 */

import org.ndts.optalgj.algs.ObjectiveFunction;

public class OverlapNeighborhood extends GeometricNeighborhood {
	private final static double ZERO_SNAP_THRESHOLD = 0.001;
	private double overlap = 1.0;

	@Override
	public Output betterNeighbor(Output initial, ObjectiveFunction<Output> obj) {
		final var result = super.betterNeighbor(initial, obj);
		final var resultIsNull = result == null;
		final var overlapsExistInInitial = initial.hasOverlaps();
		if (resultIsNull)
			if (overlapsExistInInitial) {
				// TODO maybe target boxes with overlaps directly?
				// IDEAS
				// - split box with overlaps into two boxes without overlaps
				// - move rectangle with a lot of overlap to another overlap-free position
				calculateNextOverlap(true);
				return initial;
			} else return null;
		else {
			calculateNextOverlap(false);
			return result;
		}
	}

	private void calculateNextOverlap(final boolean sharp) {
		// TODO think of a good way to decrease the overlap over time
		// IDEAS
		// - decrease a fixed amount on every call
		// - set to zero if sharp
		// - decrease a percentage on every call -> could multiply by ~0.95
		// - set to percentage of iterations so far with regard to a max number of
		// iterations where overlap is allowed
		// - snap to zero when close enough
		if (overlap == 0) return;
		if (sharp || overlap < ZERO_SNAP_THRESHOLD) {
			overlap = 0;
			return;
		}
		overlap *= 0.95;
	}

	@Override
	protected boolean canFitInSame(final int rectangleIndex, final Box box) {
		return super.canFitInSame(rectangleIndex, box) || box.overlapPercentageAt(rectangleIndex) <= overlap;
	}

	@Override
	protected boolean canFitInOther(final PositionedRectangle rectangle, final Box box) {
		return super.canFitInOther(rectangle, box) || box.possibleOverlapPercentage(rectangle) <= overlap;
	}
}
