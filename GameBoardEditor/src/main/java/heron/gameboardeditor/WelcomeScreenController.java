package heron.gameboardeditor;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class WelcomeScreenController {
	
	@FXML
    private void switchToOpeningScreen() throws IOException {
    	App.setRoot("openingScreen");
    }

	@FXML // fx:id="newProjectButton"
    private Button newProjectButton; // Value injected by FXMLLoader
	
	@FXML // fx:id="continueProjectButton"
    private Button continueProjectButton; // Value injected by FXMLLoader
	
	@FXML // fx:id="helpButton"
    private Button helpButton; // Value injected by FXMLLoader
	
    
}

