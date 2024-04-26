package org.ndts.optalgj.problems.rect.nbhs;

/*
Die geometriebasierte Nachbarschaft wird angepasst auf die Situation, dass Rechtecke sich zu einem
gewissen Prozentsatz überlappen dürfen. Die Überlappung zweier Rechtecke ist dabei die gemeinsame
Fläche geteilt durch das Maximum der beiden Rechteckflächen. Dieser Prozentsatz ist zu Beginn 100
(so, dass eine Optimallösung einfach zu finden ist). Im Laufe der Zeit reduziert sich der
Prozentsatz, und Verletzungen werden hart in der Zielfunktion bestraft. Am Ende müssen Sie natürlich
dafür sorgen, dass schlussendlich eine garantiert überlappungsfreie Lösung entsteht.
 */

import org.ndts.optalgj.algs.ObjectiveFunction;
import org.ndts.optalgj.problems.rect.domain.Box;
import org.ndts.optalgj.problems.rect.domain.Output;
import org.ndts.optalgj.problems.rect.domain.PositionedRectangle;

import java.util.ArrayList;

public class OverlapNeighborhood extends GeometricNeighborhood {
	private final static double ZERO_SNAP_THRESHOLD = 0.001;
	private double overlap = 1.0;

	@Override
	public Output betterNeighbor(final Output initial, final ObjectiveFunction<Output> obj) {
		final var result = super.betterNeighbor(initial, obj);
		final var resultIsNull = result == null;
		final var nonNullOutput = resultIsNull ? initial : result;
		if (overlap == 0) {
			return nonNullOutput.firstBoxWithOverlap().map(i -> targetOverlap(nonNullOutput, i)).orElse(result);
		} else {
			calculateNextOverlap(resultIsNull);
			return nonNullOutput;
		}
	}

	private void calculateNextOverlap(final boolean sharp) {
		// IDEAS
		// - decrease a fixed amount on every call
		// - set to zero if sharp
		// - decrease a percentage on every call -> could multiply by ~0.95
		// - set to percentage of iterations so far with regard to a max number of
		// iterations where overlap is allowed
		// - snap to zero when close enough
		if (sharp || overlap < ZERO_SNAP_THRESHOLD) overlap = 0;
		else overlap *= 0.95;
	}

	private Output targetOverlap(final Output result, final int overlapIndex) {
		// IDEAS
		// - split box with overlaps into multiple boxes without overlaps
		// - move rectangle with a lot of overlap to another overlap-free position
		final var output = new Output(result);
		final var offendingBox = output.boxes().remove(overlapIndex);
		final var newBoxes = new ArrayList<Box>() {{add(new Box());}};
		var nbi = 0;
		for (var rectangle : offendingBox) {
			while (!isCancelled() && !tryToFit(newBoxes.get(nbi), rectangle, output.boxLength())) {
				newBoxes.add(new Box());
				nbi += 1;
			}
			newBoxes.get(nbi).add(rectangle);
		}
		newBoxes.sort(boxComparator);
		var i = output.boxes().size() - 1;
		while (!newBoxes.isEmpty()) {
			final var box = newBoxes.removeLast();
			while (i > 0 && boxComparator.compare(box, output.boxes().get(i)) >= 0) i -= 1;
			output.boxes().add(i, box);
		}
		return output;
	}

	@Override
	protected boolean canFitInSame(final Box box, final int rectangleIndex) {
		return super.canFitInSame(box, rectangleIndex) || box.overlapPercentageAt(rectangleIndex) <= overlap;
	}

	@Override
	protected boolean canFitInOther(final Box box, final PositionedRectangle rectangle) {
		return super.canFitInOther(box, rectangle) || box.possibleOverlapPercentage(rectangle) <= overlap;
	}
}
