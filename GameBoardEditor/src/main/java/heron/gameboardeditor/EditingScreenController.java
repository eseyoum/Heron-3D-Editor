package heron.gameboardeditor;

import java.io.File; 
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import heron.gameboardeditor.datamodel.Block;
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

public class EditingScreenController {
	
	@FXML 
    private AnchorPane mapDisplay;
	
    @FXML
    private TextField numRow;
    
    @FXML
    private TextField numColumn;
    
    @FXML
    private Slider levelSlider;
    
    private static int rows;
    private static int columns;
    private BorderPane gridMapPane;
    private VBox boardParentVBox;
    private GridBoardUI gridBoard;
    
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

    /**
     * Creates the grid
     * @return the root, which is a BorderPane containing a VBox with a grid in it
     */
    private BorderPane createContent() {
        BorderPane root = new BorderPane();
        root.setPrefSize(600, 800);
        gridBoard = new GridBoardUI(App.getGrid()); //creates a GridBoardUI, which is the grid the user can see
        boardParentVBox = new VBox(50, gridBoard); //creates a vbox with myBoard for children
        boardParentVBox.setAlignment(Pos.TOP_RIGHT);

        root.setCenter(boardParentVBox);

        return root;
    }
    
    @FXML
    void changeLevel(MouseEvent event) {
    	gridBoard.setLevel((int)levelSlider.getValue());
    	gridBoard.setAllSelectedCellsToLevel((int) levelSlider.getValue());
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
    void selectLevel(ActionEvent event) {
    	gridBoard.selectLevel(true);
    }
    
    @FXML
    void deselectLevel(ActionEvent event) {
    	gridBoard.selectLevel(false);
    }
    
    @FXML
    void generateMaze(ActionEvent event) {
    	gridBoard.generateMaze();
    }
    
    
    //File menu bar
    @FXML
    void newFile(ActionEvent event) throws IOException {
    	
    }
    
    @FXML
    void loadProject(ActionEvent event) {
    	File file = saveLoadHelper("open", "heron");
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
    	File file = saveLoadHelper("save", "heron");
    	if(file != null) {
    		Grid grid = App.getGrid();
    		try {
				ProjectIO.save(grid, file);
			} catch (IOException ex) {
	    		new Alert(AlertType.ERROR, "An I/O error occurred while trying to save this file.").showAndWait();			
			}
    	}
    }
    
    private File saveLoadHelper(String dialog, String fileType) {
    	FileChooser chooser = new FileChooser();
    	FileChooser.ExtensionFilter extention;
    	if(fileType == "heron" ) {
    		extention = new FileChooser.ExtensionFilter("Heron game (*.heron)", "*.heron");
    	} else {
    		extention = new FileChooser.ExtensionFilter("OBJ File (*.OBJ)", "*.OBJ");
    	}
    	chooser.getExtensionFilters().add(extention);
    	File file;
    	if(dialog == "open") {
    		file = chooser.showOpenDialog(App.getMainWindow());
    	} else {
    		file = chooser.showSaveDialog(App.getMainWindow());
    	}
    	return file;
    }
    
    @FXML
    void clear(ActionEvent event) {
    	gridBoard.clear();
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
    void exportToObj() throws IOException {
    	File file = saveLoadHelper("save", "OBJ");
    	if(file != null) {
    		Grid grid = App.getGrid();
    		FileWriter writer = new FileWriter(file);
    		for (int x = 0; x < grid.getWidth(); x++) {
    			for (int y = 0; y < grid.getHeight(); y++) {
    				Block blocks = grid.getBlockAt(x, y);
    				int r = blocks.getY();
    				int c = blocks.getX();
    				int e = blocks.getZ();
    				writer.write("v " + c + " " + r + " " + e + "\n");
    				writer.write("v " + c + " " + r + " " + 0 + "\n");
    				writer.write("v " + c + " " + (r + 1) + " " + 0 + "\n");
    				writer.write("v " + c + " " + (r + 1) + " " + e + "\n");
    				writer.write("v " + (c + 1) + " " + r + " " + e + "\n");
    				writer.write("v " + (c + 1) + " " + r + " " + 0 + "\n");
    				writer.write("v " + (c + 1) + " " + (r + 1) + " " + 0 + "\n");
    				writer.write("v " + (c + 1) + " " + (r + 1) + " " + e + "\n");
    			}
    		}
    		for(int i = 0; i < (grid.getWidth() * grid.getHeight()); i++) {
    			writer.write("f " + (8 * i + 4) + " " + (8 * i + 3) + " " + (8 * i + 2) + " " + (8 * i + 1) + "\n");
    			writer.write("f " + (8 * i + 2) + " " + (8 * i + 6) + " " + (8 * i + 5) + " " + (8 * i + 1) + "\n");
    			writer.write("f " + (8 * i + 3) + " " + (8 * i + 7) + " " + (8 * i + 6) + " " + (8 * i + 2) + "\n");
    			writer.write("f " + (8 * i + 8) + " " + (8 * i + 7) + " " + (8 * i + 3) + " " + (8 * i + 4) + "\n");
    			writer.write("f " + (8 * i + 5) + " " + (8 * i + 8) + " " + (8 * i + 4) + " " + (8 * i + 1) + "\n");
    			writer.write("f " + (8 * i + 6) + " " + (8 * i + 7) + " " + (8 * i + 8) + " " + (8 * i + 5) + "\n");
    		}
    		writer.close();
    	}
    }
    
    @FXML
    private void initialize() {
    	gridMapPane = createContent(); //creates the 2d grid
    	mapDisplay.getChildren().addAll(gridMapPane);
    }
    
}
