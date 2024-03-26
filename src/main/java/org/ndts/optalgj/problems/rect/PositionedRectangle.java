package org.ndts.optalgj.problems.rect;


public class PositionedRectangle {
	private final Rectangle rectangle;
	private final int area;
	private int x;
	private int y;
	private boolean rotated; // without this we'd have to rotate the rectangle properties

	PositionedRectangle(final Rectangle rectangle) {
		this.rectangle = rectangle;
		this.x = 0;
		this.y = 0;
		this.rotated = false;
		this.area = rectangle.area();
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
		return !(l1x > r2x || l2x > r1x || l1y > r2y || l2y > r1y);
	}

	public double overlapArea(final PositionedRectangle other) {
		// TODO calculate overlap area between this and other
		throw new UnsupportedOperationException();
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
		if (rotated) {
			return rectangle.height();
		} else {
			return rectangle.width();
		}
	}

	public int height() {
		if (rotated) {
			return rectangle.width();
		} else {
			return rectangle.height();
		}
	}

	public boolean rotated() {
		return rotated;
	}

	public int area() {
		return area;
	}
}
