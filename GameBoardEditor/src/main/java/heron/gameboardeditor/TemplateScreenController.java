package heron.gameboardeditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import heron.gameboardeditor.datamodel.Grid;
import heron.gameboardeditor.datamodel.ProjectIO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class TemplateScreenController {
	
	private VBox boardParentVBox;
	private GridBoardUI gridBoard;
	
	@FXML
    void switchToEmptyBoard(ActionEvent event) throws IOException {
		App.setRoot("editingScreen");
    }
	
	@FXML
	void Template1(ActionEvent event) {

		FileChooser chooser = new FileChooser();
    	FileChooser.ExtensionFilter extention = new FileChooser.ExtensionFilter("Heron game (*.heron)", "*.heron");
    	chooser.getExtensionFilters().add(extention);
    	File file = chooser.showOpenDialog(App.getMainWindow());
		if (file != null) {
			try {
				Grid grid = ProjectIO.load(file);
				App.setGrid(grid);
				gridBoard = new GridBoardUI(grid);
				boardParentVBox.getChildren().addAll(gridBoard);
			} catch (FileNotFoundException ex) {
				new Alert(AlertType.ERROR, "The file you tried to open could not be found.").showAndWait();
			} catch (IOException ex) {
				new Alert(AlertType.ERROR, "Error opening file.  Did you choose a valid .heron file (which uses JSON format?)").show();
			}
		}
	}
	
	@FXML
    void initialize() {
    	App.useNewGrid(10, 10);
    }
	
}