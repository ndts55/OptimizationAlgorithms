package org.ndts.optalgj.gui;

import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class Colors {
	// TODO pick colors that are more different to each other
	private static final Color[] colors = new Color[]{Color.RED, Color.ORANGE, Color.YELLOW,
		Color.GREEN, Color.BLUE, Color.INDIGO, Color.PURPLE,};
	private static final Map<Integer, Color> map = new HashMap<>(1000);
	private static int index = 0;

	public static Color get(int rectangleId) {
		if (!map.containsKey(rectangleId)) {
			map.put(rectangleId, colors[index]);
			index = (index + 1) % colors.length;
		}
		return map.get(rectangleId);
	}
}
