package org.ndts.optalgj.problems.rect.neighborhood;

import org.ndts.optalgj.algs.Neighborhood;
import org.ndts.optalgj.algs.ObjectiveFunction;
import org.ndts.optalgj.problems.rect.domain.Box;
import org.ndts.optalgj.problems.rect.domain.Output;
import org.ndts.optalgj.problems.rect.domain.PositionedRectangle;
import org.ndts.optalgj.problems.rect.utils.Fits;
import org.ndts.optalgj.problems.rect.utils.RNG;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

public class RuleNeighborhood extends Neighborhood<Output> {
	// region Class Attributes
	protected static final int MAX_ACTION_COUNT = 60;
	// endregion

	// region Constants (but as functions)
	@Override
	protected long stallThreshold() {return 150;}
	// endregion

	// region Better Neighbor
	@Override
	protected Output findBetterNeighbor(final Output initial, final double initialEvaluation,
										final ObjectiveFunction<Output> obj) {
		final var rectangles = extractRectangles(initial);
		var output = new Output(initial);
		for (var i = 0; !isCancelled() && i < MAX_ACTION_COUNT; i += 1) {
			switch (getRandomAction()) {
				case Swap -> swapRectangles(rectangles);
				case Move -> moveRectangles(rectangles);
			}
			output = new Output(output.boxLength(), constructBoxes(output.boxLength(),
				rectangles));
			if (obj.evaluate(output) < initialEvaluation) {
				return output;
			}
		}
		return obj.evaluate(output) == initialEvaluation ? output : initial;
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
		IntStream.range(1, RNG.nextInt(1, 5)).forEach(i -> Collections.swap(rectangles,
			RNG.nextIndex(rectangles.size()), RNG.nextIndex(rectangles.size())));
	}
	// endregion

	// region Move Rectangles
	protected void moveRectangles(final List<PositionedRectangle> rectangles) {
		// IDEAS
		// - pick n rectangles and move them to a random index
		final var rSize = rectangles.size();
		IntStream.range(1, RNG.nextInt(1, 5)).forEach(i -> rectangles.add(RNG.nextIndex(rSize - 1)
			, rectangles.remove(RNG.nextIndex(rSize))));
	}
	// endregion

	// region Utils
	protected boolean tryToFit(Box box, PositionedRectangle rectangle, int boxLength) {
		return Fits.tryToFit(box, rectangle, boxLength, this::isCancelled);
	}

	protected List<Box> constructBoxes(int boxLength, List<PositionedRectangle> rectangles) {
		final var boxes = new ArrayList<Box>() {{add(new Box());}};
		var box = boxes.getLast();
		for (var rect : rectangles) {
			if (tryToFit(box, rect, boxLength)) {
				box.add(rect);
			} else {
				rect.transformTo(0, 0, false);
				boxes.add(new Box(new ArrayList<>() {{add(rect);}}));
				box = boxes.getLast();
			}
		}
		return boxes;
	}

	protected List<PositionedRectangle> extractRectangles(final Output output) {
		return output.boxes().stream().map(Box::new).flatMap(b -> b.rectangles().stream()).collect(Collectors.toCollection(ArrayList::new));
	}
	// endregion
}
