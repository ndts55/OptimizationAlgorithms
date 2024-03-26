package org.ndts.optalgj.problems.rect;

public record Rectangle(int height, int width) {
	public int area() {
		return height * width;
	}
}
