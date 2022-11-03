package heron.gameboardeditor;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

public class WelcomeScreenController {
	

	@FXML // fx:id="newProjectButton"
    private Button newProjectButton; // Value injected by FXMLLoader
	
	@FXML // fx:id="continueProjectButton"
    private Button continueProjectButton; // Value injected by FXMLLoader
	
	@FXML
	private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button NewProject;
    
    @FXML
    private void switchToNewProject() throws IOException {
    	App.setRoot("chooseBoard");
    }
	
	@FXML
    private void continueProject(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open");
		fileChooser.showOpenDialog(null);
    }
	
    @FXML
    void switchToAboutTeamScreen(ActionEvent event) throws IOException {
    	App.setRoot("aboutTeamScreen");
    }

    @FXML
    void initialize() {
        assert NewProject != null : "fx:id=\"NewProject\" was not injected: check your FXML file 'welcomeScreen.fxml'.";

    }
    

}

