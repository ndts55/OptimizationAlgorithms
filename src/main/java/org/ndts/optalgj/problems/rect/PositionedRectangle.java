package org.ndts.optalgj.problems.rect;


public class PositionedRectangle {
	private final Rectangle rectangle;
	private final int area;
	private int x;
	private int y;
	private boolean rotated; // without this we'd have to rotate the rectangle properties

	PositionedRectangle(final Rectangle rectangle, final int x, final int y,
						final boolean rotated) {
		this.rectangle = rectangle;
		this.x = x;
		this.y = y;
		this.rotated = rotated;
		this.area = rectangle.area();
	}

	PositionedRectangle(final Rectangle rectangle) {
		this(rectangle, 0, 0, false);
	}

	PositionedRectangle(final PositionedRectangle positionedRectangle) {
		this(positionedRectangle.rectangle, positionedRectangle.x, positionedRectangle.y,
			positionedRectangle.rotated);
	}

	public boolean overlapsWith(final PositionedRectangle other) {
		var l1x = this.x;
		var l1y = this.y;
		var r1x = this.x + this.width();
		var r1y = this.y + this.height();
		var l2x = other.x;
		var l2y = other.y;
		var r2x = other.x + other.width();
		var r2y = other.y + other.height();
		// do not overlap if one rectangle is to the left or above the other
		return !(l1x >= r2x || l2x >= r1x || l1y >= r2y || l2y >= r1y);
	}

	public boolean outOfBounds(int boxLength) {
		return x < 0 || y < 0 || x + width() > boxLength - 1 || y + height() > boxLength - 1;
	}

	public void rotate() {
		rotated = !rotated;
	}

	public void transformTo(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void transformTo(int x, int y, boolean rotated) {
		this.x = x;
		this.y = y;
		this.rotated = rotated;
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public int width() {
		return rotated ? rectangle.height() : rectangle.width();
	}

	public int height() {
		return rotated ? rectangle.width() : rectangle.height();
	}

	public boolean rotated() {
		return rotated;
	}

	public int area() {
		return area;
	}

	public int id() {
		return rectangle.id();
	}
}
