package org.ndts.optalgj.problems.rect.nbhs;

import org.ndts.optalgj.algs.ObjectiveFunction;
import org.ndts.optalgj.problems.rect.domain.Box;
import org.ndts.optalgj.problems.rect.utils.Fits;
import org.ndts.optalgj.problems.rect.domain.Output;
import org.ndts.optalgj.problems.rect.domain.PositionedRectangle;
import org.ndts.optalgj.problems.rect.utils.RNG;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

// TODO Improve this neighborhood so that it produces acceptable solutions
public class RuleNeighborhood extends LocalSearchNeighborhood {
	// region Class Attributes
	protected static final int MAX_ACTION_COUNT = 50;
	protected List<PositionedRectangle> rectangles;
	// endregion

	// region Constants (but as functions)
	@Override
	protected long stallThreshold() {return 400;}
	// endregion

	// region Better Neighbor
	@Override
	protected Output findBetterNeighbor(final Output initial, final double initialEvaluation,
										final ObjectiveFunction<Output> obj) {
		final var rectangles = extractRectangles(initial);
		for (var i = 0; !isCancelled() && i < MAX_ACTION_COUNT; i += 1) {
			switch (getRandomAction()) {
				case Swap -> swapRectangles(rectangles);
				case Move -> moveRectangles(rectangles);
			}
			final var output = new Output(initial.boxLength(), constructBoxes(initial.boxLength(),
				rectangles));
			if (obj.evaluate(output) < initialEvaluation) {
				sortOutput(output);
				return output;
			}
		}
		return initial;
	}

	protected void sortOutput(final Output output) {
		output.boxes().sort(Comparator.comparingInt(Box::occupiedArea));
	}

	private RuleAction getRandomAction() {
		final var first = RuleAction.values()[RNG.nextIndex(RuleAction.values().length)];
		final var second = RuleAction.values()[RNG.nextIndex(RuleAction.values().length)];
		if (first == RuleAction.Swap && second == RuleAction.Swap) {return RuleAction.Swap;}
		return RuleAction.Move;
	}
	// endregion

	// region Swap Rectangles
	protected void swapRectangles(final List<PositionedRectangle> rectangles) {
		// IDEAS
		// - swap two rectangles
		// - generate n pairs that are swapped
		final var swapCount = RNG.nextInt(1, 3);
		for (var i = 0; i < swapCount; i += 1) {
			final var leftIndex = RNG.nextIndex(rectangles.size());
			final var rightIndex = RNG.nextIndex(rectangles.size());
			if (leftIndex == rightIndex) continue;
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
			final var dstIndex = RNG.nextIndex(rectangles.size() - 1);
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
		final var boxes = new ArrayList<Box>() {{add(new Box());}};
		for (var rect : rectangles) {
			final var box = boxes.getLast();
			if (tryToFit(box, rect, boxLength)) {
				box.add(rect);
			} else {
				rect.transformTo(0, 0, false);
				boxes.add(new Box(new ArrayList<>() {{add(rect);}}));
			}
		}
		return boxes;
	}

	protected List<PositionedRectangle> extractRectangles(final Output output) {
		return output.boxes().stream().map(Box::new).flatMap(b -> b.rectangles().stream()).collect(Collectors.toCollection(ArrayList::new));
	}
	// endregion
}
