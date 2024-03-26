package org.ndts.optalgj.gui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.RangeSlider;
import org.ndts.optalgj.AlgorithmRunner;
import org.ndts.optalgj.algs.AlgorithmVariant;
import org.ndts.optalgj.algs.GreedyNeighborhoodVariant;
import org.ndts.optalgj.algs.LocalNeighborhoodVariant;
import org.ndts.optalgj.problems.rect.Input;
import org.ndts.optalgj.problems.rect.Rectangle;
import org.ndts.optalgj.problems.rect.SimpleSolutionConstructor;

import static org.ndts.optalgj.utils.CanvasDrawer.drawOutput;


public class MainApplicationController {
	// region Constants
	private static final String MIN_PREFIX = "Min: ";
	private static final String MAX_PREFIX = "Max: ";
	// region @FXML Attributes
	@FXML
	public Canvas canvas;
	// endregion
	@FXML
	public Button generateInstances;
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
	public TableView<Rectangle> instanceTable;
	@FXML
	public TableColumn<Rectangle, Integer> widthTableColumn;
	@FXML
	public TableColumn<Rectangle, Integer> heightTableColumn;
	@FXML
	public ChoiceBox<AlgorithmVariant> algorithmVariant;
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
	@FXML
	public VBox drawer;
	@FXML
	public BorderPane rootElement;
	double CANVAS_SCALE = 10.0;
	// endregion

	public void onGenerateInstances() {
		instanceTable.setItems(FXCollections.observableArrayList(InstanceGenerator.generateInstances(rectangleCount.getValue(), (int) rectangleWidthRange.getLowValue(), (int) rectangleWidthRange.getHighValue(), (int) rectangleHeightRange.getLowValue(), (int) rectangleHeightRange.getHighValue())));
		drawTestCanvas();
	}

	private void drawTestCanvas() {
		Input input;
		try {
			input = new Input(instanceTable.getItems().stream().toList(), ((int) maxBoxLength.getValue()));
		} catch (IllegalArgumentException iae) {
			iae.printStackTrace();
			return;
		}
		var output = new SimpleSolutionConstructor().arbitrarySolution(input);
		drawOutput(output, canvas, maxBoxLength.getValue(), CANVAS_SCALE);
	}

	private void onStart() {
		if (instanceTable.getItems().isEmpty()) {
			onGenerateInstances();
		}
		var algoVariant = algorithmVariant.getValue();
		switch (algoVariant) {
			case Local ->
				AlgorithmRunner.startAlgorithm(algoVariant, localNeighborhoodVariant.getValue(), instanceTable.getItems());
			case Greedy ->
				AlgorithmRunner.startAlgorithm(algoVariant, greedyNeighborhoodVariant.getValue(), instanceTable.getItems());
		}
		// TODO start timer
		// TODO devise a way to track iterations
	}

	private void onStop() {
		AlgorithmRunner.stopAlgorithm();
	}

	// region Initialize
	@FXML
	public void initialize() {
		initializeInstanceManagement();
		initializeInstanceInspector();
		initializeAlgorithmNeighborhood();
		initializeRun();
		initializeCanvas();
	}

	private void initializeInstanceManagement() {
		maxBoxLengthInfo.textProperty().bind(Bindings.createStringBinding(() -> MAX_PREFIX + (int) maxBoxLength.getValue(), maxBoxLength.valueProperty()));
		minRectangleWidthInfo.textProperty().bind(Bindings.createStringBinding(() -> MIN_PREFIX + (int) rectangleWidthRange.getLowValue(), rectangleWidthRange.lowValueProperty()));
		maxRectangleWidthInfo.textProperty().bind(Bindings.createStringBinding(() -> MAX_PREFIX + (int) rectangleWidthRange.getHighValue(), rectangleWidthRange.highValueProperty()));
		minRectangleHeightInfo.textProperty().bind(Bindings.createStringBinding(() -> MIN_PREFIX + (int) rectangleHeightRange.getLowValue(), rectangleHeightRange.lowValueProperty()));
		maxRectangleHeightInfo.textProperty().bind(Bindings.createStringBinding(() -> MAX_PREFIX + (int) rectangleHeightRange.getHighValue(), rectangleHeightRange.highValueProperty()));
	}

	private void initializeInstanceInspector() {
		widthTableColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().width()).asObject());
		heightTableColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().height()).asObject());
	}

	private void initializeAlgorithmNeighborhood() {
		localNeighborhoodVariant.visibleProperty().bind(Bindings.createBooleanBinding(() -> algorithmVariant.getValue() == AlgorithmVariant.Local, algorithmVariant.valueProperty()));
		localNeighborhoodVariant.managedProperty().bind(localNeighborhoodVariant.visibleProperty());
		greedyNeighborhoodVariant.visibleProperty().bind(Bindings.createBooleanBinding(() -> algorithmVariant.getValue() == AlgorithmVariant.Greedy, algorithmVariant.valueProperty()));
		greedyNeighborhoodVariant.managedProperty().bind(greedyNeighborhoodVariant.visibleProperty());
		algorithmVariant.getSelectionModel().selectFirst();
		localNeighborhoodVariant.getSelectionModel().selectFirst();
		greedyNeighborhoodVariant.getSelectionModel().selectFirst();
	}

	private void initializeRun() {
		startStopButton.textProperty().bind(Bindings.when(startStopButton.selectedProperty()).then("Stop").otherwise("Start"));
		startStopButton.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
			if (!oldValue && newValue) {
				onStart();
			} else if (oldValue && !newValue) {
				onStop();
			}
		});
	}

	private void initializeCanvas() {
		canvas.heightProperty().bind(drawer.heightProperty());
		canvas.widthProperty().bind(rootElement.widthProperty().subtract(drawer.widthProperty()));
	}
	// endregion
}