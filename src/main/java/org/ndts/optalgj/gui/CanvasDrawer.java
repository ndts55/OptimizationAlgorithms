package org.ndts.optalgj.gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import org.ndts.optalgj.problems.rect.Output;

public class CanvasDrawer {
	private static final double scale = 5.0;

	public static void drawOutput(Output output, Canvas canvas) {
		if (output == null || output.boxes().isEmpty()) {
			System.out.println("EMPTY");
		}
		// Clear output
		final var gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		// Check if there's anything to do
		var boxes = output.boxes();
		if (boxes.isEmpty()) {
			canvas.setHeight(0);
			return;
		}
		// Set canvas height
		final var boxLength = (double) output.boxLength() * scale;
		canvas.setHeight(Math.ceil((output.boxes().size() * boxLength) / canvas.getWidth()) * boxLength);
		// Prepare loop
		final int maxBoxesPerRow = (int) (canvas.getWidth() / boxLength);
		gc.setLineWidth(1);
		for (var i = 0; i < boxes.size(); i++) {
			var boxStartX = (i % maxBoxesPerRow) * boxLength;
			var boxStartY = (i / maxBoxesPerRow) * boxLength;
			gc.setStroke(Color.BLACK);
			gc.strokeRect(boxStartX, boxStartY, boxLength, boxLength);

			var box = boxes.get(i);
			for (var rectangle : box) {
				gc.setFill(Colors.get(rectangle.id()));
				final var x = boxStartX + (rectangle.x() * scale);
				final var y = boxStartY + (rectangle.y() * scale);
				final var w = rectangle.width() * scale;
				final var h = rectangle.height() * scale;
				gc.fillRect(x, y, w, h);
				gc.strokeRect(x, y, w, h);
			}
		}
	}
}
