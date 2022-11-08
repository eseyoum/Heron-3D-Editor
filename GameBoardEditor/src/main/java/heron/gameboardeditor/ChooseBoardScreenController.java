package heron.gameboardeditor;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ChooseBoardScreenController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    
	@FXML
    void switchToWelcomeScreen(ActionEvent event) throws IOException {
		App.setRoot("welcomeScreen");
    }
	
	@FXML
    void switchToEmptyBoard(ActionEvent event) throws IOException {
		App.setRoot("newProjectScreen");
    }
    
    @FXML
    void initialize() {

    }

}