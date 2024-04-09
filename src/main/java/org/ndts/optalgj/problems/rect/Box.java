package org.ndts.optalgj.problems.rect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public record Box(List<PositionedRectangle> rectangles) implements Iterable<PositionedRectangle> {

	/**
	 * Copy-constructor
	 *
	 * @param box Box from which to copy data.
	 */
	public Box(final Box box) {
		this(new ArrayList<>(box.rectangles.size()));
		for (var r : box.rectangles) rectangles.add(new PositionedRectangle(r));
	}

	/**
	 * @return Number of rectangles in this Box.
	 */
	public int size() {
		return rectangles.size();
	}

	/**
	 * @param i Rectangle index.
	 * @return Rectangle at the specified index.
	 */
	public PositionedRectangle get(final int i) {
		return rectangles.get(i);
	}

	/**
	 * @param index Rectangle index.
	 * @return The removed PositionedRectangle object.
	 */
	public PositionedRectangle remove(final int index) {
		return rectangles.remove(index);
	}

	/**
	 * @param rectangle Element to add to this box.
	 */
	public void add(PositionedRectangle rectangle) {
		rectangles.add(rectangle);
	}

	@Override
	public Iterator<PositionedRectangle> iterator() {
		return rectangles.iterator();
	}

	/**
	 * @param index Index of the rectangle to check.
	 * @return Whether the rectangle at the specified index overlaps with any other rectangle.
	 */
	public boolean overlapExistsAt(int index) {
		final var rectangle = get(index);
		for (var i = 0; i < size(); i += 1)
			if (index != i && rectangle.overlapsWith(get(i))) return true;
		return false;
	}

	/**
	 * @return Whether this box contains PositionedRectangles that overlap.
	 */
	public boolean hasOverlaps() {
		for (var i = 0; i < size(); i += 1) if (overlapExistsAt(i)) return true;
		return false;
	}

	/**
	 * @param index Rectangle index.
	 * @return Accumulated percentage of overlapping area of the specified rectangle.
	 */
	public double overlapPercentageAt(int index) {
		final var rectangle = get(index);
		double overlap = 0.0;
		for (var i = 0; i < size(); i += 1) {
			if (index == i) continue;
			final var other = get(i);
			overlap += rectangle.overlapPercentage(other);
		}
		return overlap;
	}

	/**
	 * @param rectangle PositionedRectangle.
	 * @return Whether rectangle would overlap with any existing PositionedRectangles in this
	 * Box.
	 */
	public boolean wouldOverlap(final PositionedRectangle rectangle) {
		for (var r : rectangles) {
			if (rectangle.overlapsWith(r)) return true;
		}
		return false;
	}

	/**
	 * @param rectangle PositionedRectangle.
	 * @return Accumulated overlap percentage for all overlaps the given PositionedRectangle
	 * would cause.
	 */
	public double possibleOverlapPercentage(final PositionedRectangle rectangle) {
		double overlap = 0.0;
		for (var r : rectangles) {
			overlap += rectangle.overlapPercentage(r);
		}
		return overlap;
	}

	/**
	 * @return The total occupied area in this Box.
	 */
	public int occupiedArea() {
		return rectangles.stream().mapToInt(PositionedRectangle::area).sum();
	}
}
