package heron.gameboardeditor;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

public class TemplatesScreenController {
	
	@FXML
    private ScrollPane scrollPane;

	@FXML
    void switchToWelcomeScreen(ActionEvent event) throws IOException {
		App.setRoot("welcomeScreen");
    }
	
	
	@FXML
    void initialize() {


    }
	
}