package heron.gameboardeditor;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;


public class WelcomeScreenController {
	
	@FXML
    private void switchToOpeningScreen() throws IOException {
    	App.setRoot("newProjectScreen");
    }

	@FXML // fx:id="newProjectButton"
    private Button newProjectButton; // Value injected by FXMLLoader
	
	@FXML // fx:id="continueProjectButton"
    private Button continueProjectButton; // Value injected by FXMLLoader
	
	@FXML
    void continueProjectButton(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open");
		fileChooser.showOpenDialog(null);
    }
	
	@FXML
    void aboutButton(ActionEvent event) {
		
    }
    
}

