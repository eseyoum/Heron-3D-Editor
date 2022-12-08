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
	private UndoRedoHandler UndoRedoHandler;
	private EditingScreenController controller;
	
	@FXML
    void switchToChooseScreen(ActionEvent event) throws IOException {
		App.setRoot("chooseBoardScreen");
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
				UndoRedoHandler = new UndoRedoHandler(controller);
				gridBoard = new GridBoardUI(grid, UndoRedoHandler);
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
    	
    }
	
}