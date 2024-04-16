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

	/**
	 * Copy constructor
	 *
	 * @param positionedRectangle Data to copy
	 */
	PositionedRectangle(final PositionedRectangle positionedRectangle) {
		this(positionedRectangle.rectangle, positionedRectangle.x, positionedRectangle.y,
			positionedRectangle.rotated);
	}

	/**
	 * @param other The other PositionedRectangle.
	 * @return Whether this and other overlap.
	 */
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

	public int overlapArea(final PositionedRectangle other) {
		if (!overlapsWith(other)) return 0;
		final var width = Math.min(x + width(), other.x + other.width()) - Math.max(x,
			other.x);
		final var height = Math.min(y + height(), other.y + other.height()) - Math.max(y,
			other.y);
		return width * height;
	}

	/**
	 * @param other The other PositionedRectangle
	 * @return Percentage of the area of this PositionedRectangle that overlaps with the other
	 * PositionedRectangle.
	 */
	public double overlapPercentage(final PositionedRectangle other) {
		return (double) overlapArea(other) / area;
	}

	/**
	 * @param boxLength Length of the containing box.
	 * @return Whether this PositionedRectangle is outside the bounds of its box.
	 */
	public boolean outOfBounds(int boxLength) {
		return x < 0 || y < 0 || x + width() > boxLength - 1 || y + height() > boxLength - 1;
	}

	/**
	 * Rotate this PositionedRectangle.
	 */
	public void rotate() {
		rotated = !rotated;
	}

	/**
	 * Translate this PositionedRectangle.
	 *
	 * @param x New X-axis coordinate.
	 * @param y New Y-axis coordinate.
	 */
	public void transformTo(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Translate and rotate this PositionedRectangle.
	 *
	 * @param x       New X-axis coordinate.
	 * @param y       New Y-axis coordinate.
	 * @param rotated Whether the rectangle should be rotated.
	 */
	public void transformTo(int x, int y, boolean rotated) {
		this.x = x;
		this.y = y;
		this.rotated = rotated;
	}

	/**
	 * @return X-axis coordinate.
	 */
	public int x() {
		return x;
	}

	/**
	 * @return Y-axis coordinate.
	 */
	public int y() {
		return y;
	}

	/**
	 * @return Width of this PositionedRectangle. Length along the X-axis.
	 */
	public int width() {
		return rotated ? rectangle.height() : rectangle.width();
	}

	/**
	 * @return Height of this PositionedRectangle. Length along the Y-axis.
	 */
	public int height() {
		return rotated ? rectangle.width() : rectangle.height();
	}

	/**
	 * @return Whether this PositionedRectangle is rotated.
	 */
	public boolean rotated() {
		return rotated;
	}

	/**
	 * @return Area of this PositionedRectangle.
	 */
	public int area() {
		return area;
	}

	/**
	 * @return ID of this PositionedRectangle.
	 */
	public int id() {
		return rectangle.id();
	}
}
