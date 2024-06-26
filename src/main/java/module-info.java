module org.ndts.optalgj {
	requires javafx.controls;
	requires javafx.fxml;

	requires org.controlsfx.controls;
	requires java.desktop;

	opens org.ndts.optalgj to javafx.fxml;
	exports org.ndts.optalgj;
	exports org.ndts.optalgj.algs;
	opens org.ndts.optalgj.algs to javafx.fxml;
	exports org.ndts.optalgj.gui;
	opens org.ndts.optalgj.gui to javafx.fxml;
	exports org.ndts.optalgj.problems.rect.neighborhood;
	opens org.ndts.optalgj.problems.rect.neighborhood to javafx.fxml;
	exports org.ndts.optalgj.problems.rect.domain;
	opens org.ndts.optalgj.problems.rect.domain to javafx.fxml;
	exports org.ndts.optalgj.problems.rect.utils;
	opens org.ndts.optalgj.problems.rect.utils to javafx.fxml;
	exports org.ndts.optalgj.problems.rect.node;
	opens org.ndts.optalgj.problems.rect.node to javafx.fxml;
}