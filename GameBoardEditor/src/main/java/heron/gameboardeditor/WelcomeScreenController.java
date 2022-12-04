package heron.gameboardeditor;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class WelcomeScreenController {
	
	@FXML
    private Button newProjectButton;
	
	@FXML
    private Button continueProjectButton;
	
    @FXML
    private void switchToNewProject() throws IOException {
    	App.setRoot("chooseBoardScreen");
    }
	
    @FXML
    void switchToAboutTeamScreen(ActionEvent event) throws IOException {
    	App.setRoot("aboutTeamScreen");
    }
    
    @FXML
    void switchToAboutSoftwareScreen(ActionEvent event) throws IOException {
    	App.setRoot("aboutSoftwareScreen");
    }

    @FXML
    void initialize() {
    	App.useNewGrid(31, 31);
    }
    
}
