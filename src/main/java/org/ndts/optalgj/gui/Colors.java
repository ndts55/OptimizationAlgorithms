package org.ndts.optalgj.gui;

import javafx.scene.paint.Color;

public class Colors {
	private static final Color[] colors = new Color[]{Color.RED, Color.ORANGE, Color.YELLOW,
		Color.GREEN, Color.BLUE, Color.PURPLE,};

	public static Color get(int rectangleId) {
		return colors[rectangleId % colors.length];
	}
}
