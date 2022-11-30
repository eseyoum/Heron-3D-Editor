package heron.gameboardeditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import heron.gameboardeditor.datamodel.Grid;
import heron.gameboardeditor.datamodel.ProjectIO;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
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

    @FXML // fx:id="x1"
    private Font x1; // Value injected by FXMLLoader
    @FXML // fx:id="x2"
    private Color x2; // Value injected by FXMLLoader
    @FXML // fx:id="x21"
    private Color x21; // Value injected by FXMLLoader
    @FXML // fx:id="x3"
    private Font x3; // Value injected by FXMLLoader
    @FXML // fx:id="x4"
    private Color x4; // Value injected by FXMLLoader
    
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
    private void switchToSecondary() throws IOException {
        App.setRoot("welcomeScreen");
    }
    
    @FXML
    void exitTheSceen(ActionEvent event) {
    	Platform.exit();
    }
    
    @FXML
    void setSizeAction(ActionEvent event) {
    	if (!numRow.getText().isBlank()) {
    		rows = Integer.parseInt(numRow.getText());
    	} else {
    		rows = 1;
    	}
    	
    	if (!numColumn.getText().isBlank()) {
    		columns = Integer.parseInt(numColumn.getText());
    	} else {
    		columns = 1;
    	}
    	
    	mapDisplay.getChildren().clear(); // clear the old gird
    	App.useNewGrid(rows, columns);
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
    
    @FXML
    void saveProject(ActionEvent event) {
    	FileChooser saveChooser = new FileChooser();
    	FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Heron game (*.heron)", "*.heron");
    	saveChooser.getExtensionFilters().add(extFilter);
    	File outputFile = saveChooser.showSaveDialog(App.getMainWindow());
    	if (outputFile != null) {
    		Grid grid = App.getGrid();
    		try {
				ProjectIO.save(grid, outputFile);
			} catch (IOException ex) {
	    		new Alert(AlertType.ERROR, "An I/O error occurred while trying to save this file.").showAndWait();			
			}
    	}
    	
    }
    
    @FXML
    void openProject(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter fileExtension = new FileChooser.ExtensionFilter("Heron game (*.heron)", "*.heron");
		fileChooser.getExtensionFilters().add(fileExtension);
		File input = fileChooser.showOpenDialog(App.getMainWindow());
		if (input != null) {
			try {
				Grid grid = ProjectIO.load(input);
				App.setGrid(grid);
				
				//createContent();
				
				gridBoard = new GridBoardUI(grid);
				
				boardParentVBox.getChildren().clear();
				boardParentVBox.getChildren().addAll(gridBoard);
				
				
				//App.setRoot("newProjectScreen");
				//myBoard = grid;
		    	
		    	
			} catch (FileNotFoundException ex) {
				new Alert(AlertType.ERROR, "The file you tried to open could not be found.").showAndWait();
			} catch (IOException ex) {
				new Alert(AlertType.ERROR, "Error opening file.  Did you choose a valid .heron file (which uses JSON format?)").show();
			}
		}
    }
    
}
