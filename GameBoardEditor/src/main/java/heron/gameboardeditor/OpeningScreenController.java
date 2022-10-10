package heron.gameboardeditor;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class OpeningScreenController {

    @FXML private Button copyButton;
    @FXML private Button deleteButton;
    @FXML private Button editButton;
    @FXML private Button exitButton;
    @FXML private Button exportButton;
    @FXML private Button minimizeButton;
    @FXML private Font x1;
    @FXML private Color x2;
    @FXML private Color x21;
    @FXML private Font x3;
    @FXML private Color x4;
    
    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("welcomeScreen");
    }
    
    @FXML
    private void exitTheSceen() {
    	Platform.exit();
    }

}

