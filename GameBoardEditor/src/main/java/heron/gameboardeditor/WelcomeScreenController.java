
package heron.gameboardeditor;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class WelcomeScreenController {
	@FXML
    private void switchToOpeningScreen() throws IOException {
    	App.setRoot("openingScreen");
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button NewProject;

    @FXML
    void switchtoAboutTeamScreen(ActionEvent event) throws IOException {
    	App.setRoot("aboutTeam");
    }

    @FXML
    void initialize() {
        assert NewProject != null : "fx:id=\"NewProject\" was not injected: check your FXML file 'welcomeScreen.fxml'.";

    }

}

