package org.ndts.optalgj.problems.rect;


public class PositionedRectangle {
	public Rectangle rectangle;
	public int x, y;
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
}
