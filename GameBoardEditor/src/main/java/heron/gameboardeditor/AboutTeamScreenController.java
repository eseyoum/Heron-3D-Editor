package heron.gameboardeditor;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class AboutTeamScreenController {
	
	@FXML
    private TextArea credits;

	@FXML
    void switchToWelcomeScreen(ActionEvent event) throws IOException {
		App.setRoot("welcomeScreen");
    }
	
	//Avoid users from editing the text
	@FXML
    void initialize() {
		credits.setEditable(false);
    }
	
}