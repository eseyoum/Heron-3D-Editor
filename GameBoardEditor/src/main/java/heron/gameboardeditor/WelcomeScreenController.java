package heron.gameboardeditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import heron.gameboardeditor.datamodel.Grid;
import heron.gameboardeditor.datamodel.ProjectIO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

public class WelcomeScreenController {
	

	@FXML // fx:id="newProjectButton"
    private Button newProjectButton; // Value injected by FXMLLoader
	
	@FXML // fx:id="continueProjectButton"
    private Button continueProjectButton; // Value injected by FXMLLoader
    
    @FXML
    private void switchToNewProject() throws IOException {
    	App.setRoot("chooseBoardScreen");
    }
	
	@FXML
    private void continueProject(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter fileExtension = new FileChooser.ExtensionFilter("Heron game (*.heron)", "*.heron");
		fileChooser.getExtensionFilters().add(fileExtension);
		File input = fileChooser.showOpenDialog(App.getMainWindow());
		if (input != null) {
			try {
				Grid grid = ProjectIO.load(input);
				App.setRoot("newProjectScreen");
				App.setGrid(grid);
				
				
				//myBoard = grid;
				
				///movieListView.getItems().clear();
		    	///movieListView.getItems().addAll(grid.getMovieList());
		    	///collectionNameTextField.setText(mc.getName());
		    	
			} catch (FileNotFoundException ex) {
				new Alert(AlertType.ERROR, "The file you tried to open could not be found.").showAndWait();
			} catch (IOException ex) {
				new Alert(AlertType.ERROR, "Error opening file.  Did you choose a valid .heron file (which uses JSON format?)").show();
			}
		}
		
    }
	
    @FXML
    void switchToAboutTeamScreen(ActionEvent event) throws IOException {
    	App.setRoot("aboutTeamScreen");
    }

    @FXML
    void initialize() {
    	Grid grid = App.getGrid();
        //assert NewProject != null : "fx:id=\"NewProject\" was not injected: check your FXML file 'welcomeScreen.fxml'.";
    }
    

}

