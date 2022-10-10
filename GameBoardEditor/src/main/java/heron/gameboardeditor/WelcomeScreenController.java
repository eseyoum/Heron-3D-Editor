package heron.gameboardeditor;

import java.io.IOException;

import heron.gameboardeditor.GridBoard.Cell;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class WelcomeScreenController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("openingScreen");
    }
}