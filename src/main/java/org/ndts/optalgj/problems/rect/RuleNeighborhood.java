package org.ndts.optalgj.problems.rect;

import org.ndts.optalgj.algs.ObjectiveFunction;
import org.ndts.optalgj.utils.Fits;
import org.ndts.optalgj.utils.RNG;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

public class RuleNeighborhood extends LocalSearchNeighborhood {
	// region Class Attributes
	protected static final int MAX_ACTION_COUNT = 1000;
	protected List<PositionedRectangle> rectangles;
	// endregion

	// region Constants (but as functions)
	@Override
	protected long stallThreshold() {return 40;}
	// endregion

	// region Better Neighbor
	@Override
	protected Output findBetterNeighbor(final Output initial, final double initialEvaluation,
										final ObjectiveFunction<Output> obj) {
		if (rectangles == null) rectangles = extractRectangles(initial);
		final var output = new Output(initial);
		var bestOutput = new Output(output);
		for (var i = 0; !isCancelled() && obj.evaluate(output) >= initialEvaluation && i < MAX_ACTION_COUNT; i += 1) {
			switch (RuleAction.values()[RNG.nextIndex(RuleAction.values().length)]) {
				case Swap -> swapRectangles(rectangles);
				case Move -> moveRectangles(rectangles);
			}
			output.boxes().clear();
			output.boxes().addAll(constructBoxes(output.boxLength(), rectangles));
			if (obj.evaluate(output) < obj.evaluate(bestOutput)) bestOutput = output.copy();
		}
		if (initialEvaluation >= obj.evaluate(bestOutput)) {
			return bestOutput;
		} else {
			return initial;
		}
	}
	// endregion

	// region Swap Rectangles
	protected void swapRectangles(final List<PositionedRectangle> rectangles) {
		// IDEAS
		// - swap two rectangles
		// - generate n pairs that are swapped
		final var swapCount = RNG.nextInt(1, 5);
		for (var i = 0; i < swapCount; i += 1) {
			final var leftIndex = RNG.nextIndex(rectangles.size());
			var rightIndex = RNG.nextIndex(rectangles.size());
			while (leftIndex == rightIndex) rightIndex = RNG.nextIndex(rectangles.size());
			Collections.swap(rectangles, leftIndex, rightIndex);
		}
	}
	// endregion

	// region Move Rectangles
	protected void moveRectangles(final List<PositionedRectangle> rectangles) {
		// IDEAS
		// - pick n rectangles and move them to a random index
		final var moveCount = RNG.nextInt(1, 5);
		for (var i = 0; i < moveCount; i += 1) {
			final var srcIndex = RNG.nextIndex(rectangles.size());
			var dstIndex = RNG.nextIndex(rectangles.size());
			while (srcIndex == dstIndex) dstIndex = RNG.nextIndex(rectangles.size() - 1);
			rectangles.add(dstIndex, rectangles.remove(srcIndex));
		}
	}
	// endregion

	// region Utils
	protected boolean tryToFit(Box box, PositionedRectangle rectangle, int boxLength) {
		return Fits.tryToFit(box, rectangle, boxLength, this::isCancelled,
			(b, r) -> !b.wouldOverlap(r));
	}

	protected List<Box> constructBoxes(int boxLength, List<PositionedRectangle> rectangles) {
		final var boxes = new ArrayList<Box>(1) {{add(new Box());}};
		var i = 0;
		while (i < rectangles.size()) {
			final var rectangle = rectangles.get(i);
			// try to fit into the last box
			final var box = boxes.getLast();
			if (tryToFit(box, rectangle, boxLength)) {
				box.add(rectangle);
				i += 1;
			} else boxes.add(new Box());
		}
		return boxes;
	}

	protected List<PositionedRectangle> extractRectangles(final Output output) {
		return output.boxes().stream().flatMap(b -> b.rectangles().stream()).collect(Collectors.toCollection(ArrayList::new));
	}
	// endregion
}
