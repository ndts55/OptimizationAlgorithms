module org.ndts.optalgj {
	requires javafx.controls;
	requires javafx.fxml;

	requires org.kordamp.bootstrapfx.core;
	requires org.controlsfx.controls;

	opens org.ndts.optalgj to javafx.fxml;
	exports org.ndts.optalgj;
	exports org.ndts.optalgj.algs;
	opens org.ndts.optalgj.algs to javafx.fxml;
	exports org.ndts.optalgj.problems.rect;
	opens org.ndts.optalgj.problems.rect to javafx.fxml;
}