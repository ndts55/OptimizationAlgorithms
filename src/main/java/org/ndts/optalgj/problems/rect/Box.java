package org.ndts.optalgj.problems.rect;

import java.util.Iterator;
import java.util.List;

public record Box(int length, int totalArea,
		  List<PositionedRectangle> rectangles) implements Iterable<PositionedRectangle> {

	public Box(final int length, final List<PositionedRectangle> rectangles) {
		this(length, length * length, rectangles);
	}

	public int size() {
		return rectangles.size();
	}

	public PositionedRectangle get(final int i) {
		return rectangles.get(i);
	}

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
	public boolean overlapsExistAt(int index) {
		final var rectangle = get(index);
		for (var i = 0; i < size(); i += 1) {
			if (index != i && rectangle.overlapsWith(get(i))) {
				return true;
			}
		}
		return false;
	}

	public int occupiedArea() {
		return rectangles.stream().mapToInt(PositionedRectangle::area).sum();
	}

	public int freeArea() {
		return totalArea - occupiedArea();
	}
}
