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

import java.util.Optional;

// TODO implement geometric neighborhood with overlap for local search
public class OverlapNeighborhood extends GeometricNeighborhood {
	private static final double OVERLAP_STEP = 0.5;
	private double overlap = 100.0;

	@Override
	public Output betterNeighbor(Output initial, ObjectiveFunction<Output> obj) {
		final var result = super.betterNeighbor(initial, obj);
		if (result != null) {
			overlap -= OVERLAP_STEP;
			return result;
		}
		final var overlapBoxIndex = firstBoxIndexWithOverlaps(initial);
		if (overlapBoxIndex.isEmpty()) return null;
		// Algorithm wants to terminate but there are still some overlaps.
		overlap -= OVERLAP_STEP;
		overlap /= 2;
		return initial;
	}

	private Optional<Integer> firstBoxIndexWithOverlaps(final Output output) {
		for (var i = 0; i < output.boxes().size(); i += 1)
			if (output.boxes().get(i).hasOverlaps()) return Optional.of(i);
		return Optional.empty();
	}

	@Override
	protected boolean canFitInSame(final int rectangleIndex, final Box box) {
		return super.canFitInSame(rectangleIndex, box) || box.overlapAt(rectangleIndex) <= overlap;
	}

	@Override
	protected boolean canFitInOther(final PositionedRectangle rectangle, final Box box) {
		return super.canFitInOther(rectangle, box) || box.possibleOverlap(rectangle) <= overlap;
	}
}
