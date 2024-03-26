package org.ndts.optalgj.utils;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import org.ndts.optalgj.problems.rect.Output;

public class CanvasDrawer {
	public static void drawOutput(Output output, Canvas canvas, double boxLength, double scale) {
		var boxes = output.boxes();
		if (boxes.isEmpty()) {
			return;
		}
		boxLength *= scale;
		final int maxBoxesPerRow = (int) (canvas.getWidth() / boxLength);
		var gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		gc.setLineWidth(1);
		// first, draw however, if there are performance problems, optimize
		for (var i = 0; i < boxes.size(); i++) {
			var boxStartX = (i % maxBoxesPerRow) * boxLength;
			var boxStartY = (i / maxBoxesPerRow) * boxLength;
			gc.setStroke(Color.BLACK);
			gc.strokeRect(boxStartX, boxStartY, boxLength, boxLength); // x y width height

			var box = boxes.get(i);
			gc.setStroke(Color.RED);
			for (var rectangle : box) {
				gc.strokeRect(boxStartX + rectangle.x(), boxStartY + rectangle.y(), rectangle.width() * scale, rectangle.height() * scale); // x y width height
			}
		}
	}
}
