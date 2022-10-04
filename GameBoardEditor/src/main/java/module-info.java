module heron.gameboardeditor {
    requires javafx.controls;
    requires javafx.fxml;

    opens heron.gameboardeditor to javafx.fxml;
    exports heron.gameboardeditor;
}
