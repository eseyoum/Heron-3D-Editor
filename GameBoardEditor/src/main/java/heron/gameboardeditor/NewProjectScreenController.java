package heron.gameboardeditor;

import java.io.File; 
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import heron.gameboardeditor.datamodel.Grid;
import heron.gameboardeditor.datamodel.ProjectIO;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class NewProjectScreenController {
	
	@FXML 
    private AnchorPane mapDisplay;
	
    @FXML
    private TextField numRow;
    
    @FXML
    private TextField numColumn;
    
    @FXML
    private Slider levelSlider;
    
    @FXML
    private MenuItem mountainTerrainObject;
    
    @FXML
    private MenuItem volcanoTerrainObject;
    
    private static int rows;
    private static int columns;
    private BorderPane gridMapPane;
    private VBox boardParentVBox;
    private GridBoardUI gridBoard;
    
    /**
     * Creates the grid
     * @return the root, which is a BorderPane containing a VBox with a grid in it
     */
    private BorderPane createContent() {
        BorderPane root = new BorderPane();
        root.setPrefSize(600, 800);
        gridBoard = new GridBoardUI(App.getGrid()); //creates a GridBoardUI, which is the grid the user can see

        boardParentVBox = new VBox(50, gridBoard); //creates a vbox with myBoard for children
        boardParentVBox.setAlignment(Pos.CENTER);

        root.setCenter(boardParentVBox);

        return root;
    }
    
    //Tools
    @FXML
    /**
     * When the user clicks on Set Size button, this method get the number of rows and columns from the user's input 
     * and change the size of the board based them.
     * 
     */
    void setSizeAction(ActionEvent event) {
    	if (!numRow.getText().isBlank()) {
    		rows = Integer.parseInt(numRow.getText());
    	} 
    	
    	if (!numColumn.getText().isBlank()) {
    		columns = Integer.parseInt(numColumn.getText());
    	} 
    	
    	mapDisplay.getChildren().clear(); // clear the old gird
    	App.resizeGrid(columns, rows);
    	initialize();
    }
    
    @FXML
    void changeLevel(MouseEvent event) {
    	gridBoard.setLevel((int)levelSlider.getValue());
    }
    
    @FXML
    void pencilButtonOn(ActionEvent event) {
    	gridBoard.gridEditor.setCurrentTool(gridBoard.pencilTool);
    }
    
    @FXML
    void eraserButtonOn(ActionEvent event) {
    	gridBoard.gridEditor.setCurrentTool(gridBoard.eraserTool);
    }
    
    @FXML
    void digButtonOn(ActionEvent event) {
    	gridBoard.gridEditor.setCurrentTool(gridBoard.digTool);
    }
    
    @FXML
    void fillToolOn(ActionEvent event) {
    	gridBoard.gridEditor.setCurrentTool(gridBoard.fillTool);
    }
    
    @FXML
    void selectToolOn(ActionEvent event) {
    	gridBoard.gridEditor.setCurrentTool(gridBoard.selectionTool);
    }
    
    @FXML
    void terrainToolOn(ActionEvent event) {
    	gridBoard.terrainTool.setCurrentTerrainObject(null);
    	gridBoard.gridEditor.setCurrentTool(gridBoard.terrainTool);
    }
    
    @FXML
    void terrainToolMountain(ActionEvent event) {
    	gridBoard.terrainTool.setCurrentTerrainObject("Mountain");
    	gridBoard.gridEditor.setCurrentTool(gridBoard.terrainTool);
    }
    
    @FXML
    void terrainToolVolcano(ActionEvent event) {
    	gridBoard.terrainTool.setCurrentTerrainObject("Volcano");
    	gridBoard.gridEditor.setCurrentTool(gridBoard.terrainTool);
    }
    
    @FXML
    void generateMaze(ActionEvent event) {
    	gridBoard.generateMaze();
    }
    
    
    //File menu bar
    @FXML
    void newFile(ActionEvent event) throws IOException {
    	mapDisplay.getChildren().clear();
    	App.setRoot("newProjectScreen");
    }
    
    @FXML
    void openProject(ActionEvent event) {
    	File file = saveLoadHelper("open");
		if (file != null) {
			try {
				Grid grid = ProjectIO.load(file);
				App.setGrid(grid);
				gridBoard = new GridBoardUI(grid);
				boardParentVBox.getChildren().clear();
				boardParentVBox.getChildren().addAll(gridBoard);
			} catch (FileNotFoundException ex) {
				new Alert(AlertType.ERROR, "The file you tried to open could not be found.").showAndWait();
			} catch (IOException ex) {
				new Alert(AlertType.ERROR, "Error opening file.  Did you choose a valid .heron file (which uses JSON format?)").show();
			}
		}
    }
    
    @FXML
    void saveProject(ActionEvent event) {
    	File file = saveLoadHelper("save");
    	if (file != null) {
    		Grid grid = App.getGrid();
    		try {
				ProjectIO.save(grid, file);
			} catch (IOException ex) {
	    		new Alert(AlertType.ERROR, "An I/O error occurred while trying to save this file.").showAndWait();			
			}
    	}
    }
    
    private File saveLoadHelper(String dialog) {
    	FileChooser chooser = new FileChooser();
    	FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Heron game (*.heron)", "*.heron");
    	chooser.getExtensionFilters().add(extFilter);
    	File file;
    	if(dialog == "open") {
    		file = chooser.showOpenDialog(App.getMainWindow());
    	} else {
    		file = chooser.showSaveDialog(App.getMainWindow());
    	}
    	return file;
    }
    
    @FXML
    void exitTheSceen(ActionEvent event) {
    	Alert alert = new Alert(AlertType.WARNING, "Are you sure you want to quit?", ButtonType.YES, ButtonType.NO);
    	
    	Optional<ButtonType> result = alert.showAndWait();
    	if (result.get() == ButtonType.YES) {
    		Platform.exit();
    	}
    }
    
    //3D menu bar
    @FXML
    void show3DPreview(ActionEvent event) {
    	Board3DViewController preview3D = new Board3DViewController(gridBoard.getGridData());
    	preview3D.show();
    }
    
    @FXML
    private void initialize() {
    	gridMapPane = createContent(); //creates the 2d grid
    	mapDisplay.getChildren().addAll(gridMapPane);
    }
    
}
