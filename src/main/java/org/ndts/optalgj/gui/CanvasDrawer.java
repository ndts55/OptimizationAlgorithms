package org.ndts.optalgj.gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import org.ndts.optalgj.problems.rect.domain.Output;

public class CanvasDrawer {
	private static final double scale = 5.0;
	private static final double scaledMargin = 1.5 * scale;

	public static void drawOutput(Output output, Canvas canvas) {
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
		final var boxLengthWithMargin = boxLength + scaledMargin * 2;
		canvas.setHeight(Math.ceil((output.boxes().size() * boxLengthWithMargin) / canvas.getWidth()) * boxLengthWithMargin);
		// Prepare loop
		final int maxBoxesPerRow = (int) (canvas.getWidth() / boxLengthWithMargin);
		gc.setLineWidth(1);
		gc.setStroke(Color.BLACK);
		for (var i = 0; i < boxes.size(); i++) {
			var boxStartX = (i % maxBoxesPerRow) * boxLengthWithMargin + scaledMargin;
			var boxStartY =
				Integer.divideUnsigned(i, maxBoxesPerRow) * boxLengthWithMargin + scaledMargin;
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
