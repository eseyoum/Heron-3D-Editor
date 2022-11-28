package heron.gameboardeditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import java.net.URL;
import java.util.ResourceBundle;

import heron.gameboardeditor.datamodel.Grid;
import heron.gameboardeditor.datamodel.ProjectIO;
import heron.gameboardeditor.tools.FillTool;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;


public class NewProjectScreenController {

    
    
    //private AnchorPane MapDisplay;
    @FXML private AnchorPane mapDisplay;
	
	@FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("welcomeScreen");
    }
	
	@FXML 
	private ToggleGroup toolButtonToggleGroup;	
	@FXML
    private ToggleButton eraserToggle;
    @FXML
    private ToggleButton fillToolToggle;
    @FXML
    private ToggleButton pencilToggle;
    @FXML
    private ToggleButton selectToggle;
	
	
	
	@FXML
    private Button eraserButton;
	
	@FXML
	private Button fillButton;
	
    @FXML // fx:id="copyButton"
    private Button copyButton; // Value injected by FXMLLoader

    @FXML // fx:id="deleteButton"
    private Button deleteButton; // Value injected by FXMLLoader

    @FXML // fx:id="editButton"
    private Button editButton; // Value injected by FXMLLoader

    @FXML // fx:id="exitButton"
    private Button selectButton; // Value injected by FXMLLoader

    @FXML // fx:id="exportButton"
    private Button exportButton; // Value injected by FXMLLoader

    @FXML // fx:id="minimizeButton"
    private Button minimizeButton; // Value injected by FXMLLoader

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
    
    @FXML
    private Button pencilButton;
    
    private TreeView<String> treeView;
    
    @FXML private StackPane editPanel;
    
    private double mouseX;
    private double mouseY;
    //private selectionRectangle selectionRectangle; Not used as of now. May make the selectionRectangle an object
    private Rectangle selectionRectangleTest;
    private BorderPane gridMapPane;
    private VBox boardParentVBox;
    private GridBoardUI myBoard;
    private UndoRedoHandler undoRedoHandler;
    
    @FXML
    void exitTheSceen(ActionEvent event) {
    	Platform.exit();
    }
    
    private Boolean quit;
    
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
        myBoard = new GridBoardUI(App.getGrid()); //creates a GridBoardUI, which is the grid the user can see

        boardParentVBox = new VBox(50, myBoard); //creates a vbox with myBoard for children
        boardParentVBox.setAlignment(Pos.CENTER);

        root.setCenter(boardParentVBox);

        return root;
    }
    
    @FXML
    void changeLevelTo1(ActionEvent event) {
    	myBoard.changeLevel(1);
    }

    @FXML
    void changeLevelTo2(ActionEvent event) {
    	myBoard.changeLevel(2);
    }

    @FXML
    void changeLevelTo3(ActionEvent event) {
    	myBoard.changeLevel(3);
    }

    @FXML
    void changeLevelTo4(ActionEvent event) {
    	myBoard.changeLevel(4);
    }

    @FXML
    void changeLevelTo5(ActionEvent event) {
    	myBoard.changeLevel(5);
    }
    
    @FXML
    void pencilButtonOn(ActionEvent event) {
    	myBoard.gridEditor.setCurrentTool(myBoard.pencilTool);
    }
    
    @FXML
    void eraserButtonOn(ActionEvent event) {
    	myBoard.gridEditor.setCurrentTool(myBoard.eraserTool);
    }
    
    @FXML
    void fillToolOn(ActionEvent event) {
    	myBoard.gridEditor.setCurrentTool(myBoard.fillTool);
    }
    
    @FXML
    void selectToolOn(ActionEvent event) {
    	myBoard.gridEditor.setCurrentTool(myBoard.selectionTool);
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
				
				myBoard = new GridBoardUI(App.getGrid());
				
				boardParentVBox.getChildren().clear();
				boardParentVBox.getChildren().addAll(myBoard);
				
				
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
