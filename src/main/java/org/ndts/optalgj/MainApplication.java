package org.ndts.optalgj;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage stage) throws IOException {
		var fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
		var scene = new Scene(fxmlLoader.load());
		var stylesheet = MainApplication.class.getResource("style.css");
		if (stylesheet != null) scene.getStylesheets().add(stylesheet.toExternalForm());
		stage.setTitle("Optimization Algorithms");
		stage.setScene(scene);
		stage.show();
	}
}