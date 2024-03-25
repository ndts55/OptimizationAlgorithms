package org.ndts.optalgj.problems.rect;


public class PositionedRectangle {
	private Rectangle rectangle;
	private int x, y;
	private boolean rotated; // without this we'd have to rotate the rectangle properties

	PositionedRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
		this.x = 0;
		this.y = 0;
		this.rotated = false;
	}

	public void rotate() {
		rotated = !rotated;
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
}
