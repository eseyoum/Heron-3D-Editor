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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

public class NewProjectScreenController {
    @FXML 
    private AnchorPane mapDisplay;
	
	@FXML 
	private ToggleGroup toolButtonToggleGroup;	
	
	@FXML
    private ToggleButton eraserToggle;
	
	@FXML
	private ToggleButton digToggle;
    
	@FXML
    private ToggleButton fillToolToggle;
    
	@FXML
    private ToggleButton pencilToggle;
    
	@FXML
    private ToggleButton selectToggle;
    
	@FXML
    private MenuItem generateMazeButton;
    
	@FXML
    private Font x1;
    
	@FXML
    private Color x2;
    
	@FXML
    private Color x21;
    
	@FXML
    private Font x3;
    
	@FXML
    private Color x4;
    
	@FXML
    private Button levelButton1;
    
	@FXML
    private Button levelButton2;
    
	@FXML
    private Button levelButton3;
    
	@FXML
    private Button levelButton4;
    
	@FXML
    private Button levelButton5;
    
    @FXML private StackPane editPanel;
    
    @FXML
    private Button setSizeButton;
    
    @FXML
    private TextField numRow;
    
    @FXML
    private TextField numColumn;
    
    private static int rows = 10;
    private static int columns = 10;
    private BorderPane gridMapPane;
    private VBox boardParentVBox;
    private GridBoardUI gridBoard;
    
    @FXML
    void exitTheSceen(ActionEvent event) {
    	Alert alert = new Alert(AlertType.WARNING, "Are you sure you want to quit?", ButtonType.YES, ButtonType.NO);
    	
    	Optional<ButtonType> result = alert.showAndWait();
    	if (result.get() == ButtonType.YES) {
    		Platform.exit();
    	}
    	
    }
    
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
    private void initialize() {
    	gridMapPane = createContent(); //creates the 2d grid
    	mapDisplay.getChildren().addAll(gridMapPane);
    }

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
    
    @FXML
    void changeLevelTo1(ActionEvent event) {
    	gridBoard.setLevel(1);
    }
    @FXML
    void changeLevelTo2(ActionEvent event) {
    	gridBoard.setLevel(2);
    }
    @FXML
    void changeLevelTo3(ActionEvent event) {
    	gridBoard.setLevel(3);
    }
    @FXML
    void changeLevelTo4(ActionEvent event) {
    	gridBoard.setLevel(4);
    }
    @FXML
    void changeLevelTo5(ActionEvent event) {
    	gridBoard.setLevel(5);
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
    void generateMaze(ActionEvent event) {
    	gridBoard.generateMaze();
    }
    
//    @FXML
//    void newFile(ActionEvent event) throws IOException {
//    	mapDisplay.getChildren().clear();
//    	App.setRoot("newProjectScreen");
//    }

    void show3DPreview(ActionEvent event) {
    	Board3DViewController preview3D = new Board3DViewController(gridBoard.getGridData());
    	preview3D.show();
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
    
    
}
