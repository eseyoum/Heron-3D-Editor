module heron.gameboardeditor {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;

    opens heron.gameboardeditor to javafx.fxml;
    exports heron.gameboardeditor;
}
