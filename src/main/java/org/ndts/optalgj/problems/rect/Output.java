package org.ndts.optalgj.problems.rect;

import org.ndts.optalgj.algs.CopyConstructible;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public record Output(int boxLength,
					 List<Box> boxes) implements CopyConstructible<Output>, Iterable<Box> {
	/**
	 * Copy-constructor
	 *
	 * @param output Output from which to copy data.
	 */
	public Output(final Output output) {
		this(output.boxLength, new ArrayList<>(output.boxes.size()));
		for (var box : output.boxes) boxes.add(new Box(box));
	}

	@Override
	public Output copy() {
		return new Output(this);
	}

	@Override
	public Iterator<Box> iterator() {
		return boxes.iterator();
	}

	/**
	 * @return The index of the first Box with overlaps or None.
	 */
	public Optional<Integer> firstBoxWithOverlap() {
		for (var i = 0; i < boxes.size(); i += 1)
			if (boxes.get(i).hasOverlaps()) return Optional.of(i);
		return Optional.empty();
	}

	public double usedSpace() {
		final var totalArea =
			(double) boxLength() * boxLength() * boxes().size();
		final var occupiedArea =
			(double) boxes().stream().mapToInt(Box::occupiedArea).sum();
		return occupiedArea / totalArea;
	}
}
