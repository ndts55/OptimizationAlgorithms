package org.ndts.optalgj;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import org.controlsfx.control.RangeSlider;

public class MainApplicationController {
	private static final String MIN_PREFIX = "Min: ";
	private static final String MAX_PREFIX = "Max: ";
	@FXML
	public Canvas canvas;
	@FXML
	public Button generateInstancesButton;
	@FXML
	public ToggleButton startStopButton;
	@FXML
	public Slider maxBoxLength;
	@FXML
	public RangeSlider rectangleHeightRange;
	@FXML
	public RangeSlider rectangleWidthRange;
	@FXML
	public Label minRectangleHeightInfo;
	@FXML
	public Label maxRectangleHeightInfo;
	@FXML
	public Label minRectangleWidthInfo;
	@FXML
	public Label maxRectangleWidthInfo;
	@FXML
	public Label maxBoxLengthInfo;
	@FXML
	public Spinner<Integer> rectangleCount;
	@FXML
	public ChoiceBox<AlgorithmVariant> algorithmVariantChoice;
	@FXML
	public ChoiceBox<LocalNeighborhoodVariant> localNeighborhoodVariant;
	@FXML
	public ChoiceBox<GreedyNeighborhoodVariant> greedyNeighborhoodVariant;
	@FXML
	public Label iterationCountInfo;
	@FXML
	public Label elapsedTimeInfo;
	@FXML
	public Label boxCountInfo;
	@FXML
	public Label spaceInfo;

	public void onGenerateInstances(ActionEvent actionEvent) {
		// TODO put together state
		// TODO kick off instance generation
	}

	@FXML
	public void initialize() {
		initializeInstanceManagement();
		initializeAlgorithmNeighborhood();
		initializeRun();
	}

	private void initializeInstanceManagement() {
		maxBoxLengthInfo.textProperty().bind(
				Bindings.createStringBinding(
						() -> MAX_PREFIX + (int) maxBoxLength.getValue(),
						maxBoxLength.valueProperty()
				)
		);
		minRectangleWidthInfo.textProperty().bind(
				Bindings.createStringBinding(
						() -> MIN_PREFIX + (int) rectangleWidthRange.getLowValue(),
						rectangleWidthRange.lowValueProperty()
				)
		);
		maxRectangleWidthInfo.textProperty().bind(
				Bindings.createStringBinding(
						() -> MAX_PREFIX + (int) rectangleWidthRange.getHighValue(),
						rectangleWidthRange.highValueProperty()
				)
		);
		minRectangleHeightInfo.textProperty().bind(
				Bindings.createStringBinding(
						() -> MIN_PREFIX + (int) rectangleHeightRange.getLowValue(),
						rectangleHeightRange.lowValueProperty()
				)
		);
		maxRectangleHeightInfo.textProperty().bind(
				Bindings.createStringBinding(
						() -> MAX_PREFIX + (int) rectangleHeightRange.getHighValue(),
						rectangleHeightRange.highValueProperty()
				)
		);
	}

	private void initializeAlgorithmNeighborhood() {
		localNeighborhoodVariant.visibleProperty().bind(
				Bindings.createBooleanBinding(
						() -> algorithmVariantChoice.getValue() == AlgorithmVariant.Local,
						algorithmVariantChoice.valueProperty()
				)
		);
		localNeighborhoodVariant.managedProperty().bind(localNeighborhoodVariant.visibleProperty());
		greedyNeighborhoodVariant.visibleProperty().bind(
				Bindings.createBooleanBinding(
						() -> algorithmVariantChoice.getValue() == AlgorithmVariant.Greedy,
						algorithmVariantChoice.valueProperty()
				)
		);
		greedyNeighborhoodVariant.managedProperty().bind(greedyNeighborhoodVariant.visibleProperty());
		algorithmVariantChoice.getSelectionModel().selectFirst();
		localNeighborhoodVariant.getSelectionModel().selectFirst();
		greedyNeighborhoodVariant.getSelectionModel().selectFirst();
	}

	private void initializeRun() {
		startStopButton.textProperty().bind(
				Bindings.when(startStopButton.selectedProperty()).then("Stop").otherwise("Start")
		);
	}
}