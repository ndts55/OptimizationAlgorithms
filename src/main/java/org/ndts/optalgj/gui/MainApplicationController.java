package org.ndts.optalgj.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.controlsfx.control.RangeSlider;
import org.ndts.optalgj.algs.AlgorithmVariant;
import org.ndts.optalgj.algs.GreedyNeighborhoodVariant;
import org.ndts.optalgj.algs.LocalNeighborhoodVariant;
import org.ndts.optalgj.problems.rect.Box;
import org.ndts.optalgj.problems.rect.Input;
import org.ndts.optalgj.problems.rect.Rectangle;

import java.text.DecimalFormat;

import static org.ndts.optalgj.gui.CanvasDrawer.drawOutput;


public class MainApplicationController {
	// region Constants
	private static final String VALUE_PREFIX = "Value: ";
	private static final String MIN_PREFIX = "Min: ";
	private static final String MAX_PREFIX = "Max: ";
	private final DecimalFormat spaceInfoFormat = new DecimalFormat("#.##%");
	// endregion
	private final LongProperty runningSecondsProperty = new SimpleLongProperty(0L);
	private final Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1),
		event -> Platform.runLater(() -> runningSecondsProperty.set(runningSecondsProperty.get() + 1))));
	// region @FXML Attributes
	@FXML
	public Canvas canvas;
	@FXML
	public Button generateInstances;
	@FXML
	public Button startStopButton;
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
	// endregion
	// region Other attributes
	private SearchService service;

	// endregion
	private boolean isRunning() {
		return service != null && service.isRunning();
	}

	public void onGenerateInstances() {
		instanceTable.setItems(FXCollections.observableArrayList(InstanceGenerator.generateInstances(rectangleCount.getValue(), (int) rectangleWidthRange.getLowValue(), (int) rectangleWidthRange.getHighValue(), (int) rectangleHeightRange.getLowValue(), (int) rectangleHeightRange.getHighValue())));
	}

	// region Start Stop
	public void onStartStopClick() {
		Platform.runLater(() -> {
			if (isRunning()) onStop();
			else onStart();
		});
	}

	private void onStart() {
		disableStartStopButton();
		if (instanceTable.getItems().isEmpty()) onGenerateInstances();
		startService();
		setStopButton();
		enableStartStopButton();
	}

	private void startService() {
		assert service == null || !service.isRunning();
		Input input;
		try {
			input = new Input(instanceTable.getItems().stream().toList(),
				(int) maxBoxLength.getValue());
		} catch (IllegalArgumentException iae) {
			enableStartStopButton();
			return;
		}

		service = switch (algorithmVariant.getValue()) {
			case Local -> new SearchService(localNeighborhoodVariant.getValue(), input);
			case Greedy -> new SearchService(greedyNeighborhoodVariant.getValue(), input);
		};
		service.valueProperty().addListener((a, b, newValue) -> drawOutput(newValue, canvas));
		service.valueProperty().addListener((a, b, newValue) -> boxCountInfo.setText(String.valueOf(newValue.boxes().size())));
		service.valueProperty().addListener((a, b, newValue) -> {
			final var totalArea =
				(double) newValue.boxLength() * newValue.boxLength() * newValue.boxes().size();
			final var occupiedArea =
				(double) newValue.boxes().stream().mapToInt(Box::occupiedArea).sum();
			spaceInfo.setText(spaceInfoFormat.format(occupiedArea / totalArea));
		});
		service.iterationProperty().addListener((a, b, newValue) -> Platform.runLater(() -> iterationCountInfo.setText(String.valueOf(newValue))));
		service.setOnSucceeded(e -> Platform.runLater(() -> {
			drawOutput(service.getValue(), canvas);
			setStartButton();
			stopTimer();
		}));

		service.start();
		startTimer();
	}

	private void onStop() {
		disableStartStopButton();
		stopService();
		setStartButton();
		enableStartStopButton();
		stopTimer();
	}

	private void stopService() {
		assert service != null;
		service.cancel();
	}
	// endregion

	// region Initialize
	@FXML
	public void initialize() {
		initializeInstanceManagement();
		initializeInstanceInspector();
		initializeAlgorithmNeighborhood();
		initializeCanvas();
		initializeInfoLabels();
		initializeTimer();
	}

	private void initializeInstanceManagement() {
		maxBoxLengthInfo.textProperty().bind(Bindings.createStringBinding(() -> VALUE_PREFIX + (int) maxBoxLength.getValue(), maxBoxLength.valueProperty()));
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

	private void initializeCanvas() {
		canvas.widthProperty().bind(rootElement.widthProperty().subtract(drawer.widthProperty()));
	}

	private void initializeInfoLabels() {
		elapsedTimeInfo.textProperty().bind(runningSecondsProperty.asString("%ss"));
	}

	private void initializeTimer() {
		timer.setCycleCount(Timeline.INDEFINITE);
	}
	// endregion

	// region UI utils
	private void disableStartStopButton() {
		startStopButton.setDisable(true);
	}

	private void enableStartStopButton() {
		startStopButton.setDisable(false);
	}

	private void setStopButton() {
		startStopButton.setText("Stop");
	}

	private void setStartButton() {
		startStopButton.setText("Start");
	}

	private void startTimer() {
		runningSecondsProperty.set(0);
		timer.play();
	}

	private void stopTimer() {
		timer.stop();
	}
	// endregion
}