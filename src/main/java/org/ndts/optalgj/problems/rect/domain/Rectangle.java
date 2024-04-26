package org.ndts.optalgj.problems.rect.domain;

public record Rectangle(int id, int height, int width) {
	public int area() {
		return height * width;
	}
}
