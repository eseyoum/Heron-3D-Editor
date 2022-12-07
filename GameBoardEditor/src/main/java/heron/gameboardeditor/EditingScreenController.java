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
    
    @FXML
    private MenuItem mountainTerrainObject;
    
    @FXML
    private MenuItem volcanoTerrainObject;
    
    private static int rows;
    private static int columns;
    private BorderPane gridMapPane;
    private VBox boardParentVBox;
    private GridBoardUI gridBoard;
    private UndoRedoHandler undoRedoHandler;
    
    @FXML
    private void initialize() {
    	this.undoRedoHandler = new UndoRedoHandler(this);
    	refreshUIFromGrid();
    }
    
    private void refreshUIFromGrid() {
        gridMapPane = new BorderPane();
        gridMapPane.setPrefSize(600, 800);
        gridBoard = new GridBoardUI(App.getGrid(), undoRedoHandler); //creates a GridBoardUI, which is the grid the user can see
        boardParentVBox = new VBox(50, gridBoard); //creates a vbox with myBoard for children
        boardParentVBox.setAlignment(Pos.TOP_RIGHT);
        gridMapPane.setCenter(boardParentVBox);

        mapDisplay.getChildren().clear();
    	mapDisplay.getChildren().addAll(gridMapPane);
    	
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
    	
    	App.resizeGrid(columns, rows);
    	refreshUIFromGrid();
    	undoRedoHandler.saveState();
    }
    
    @FXML
    private void menuEditUndo() {
    	undoRedoHandler.undo();
    }
    
    @FXML
    private void menuEditRedo() {
    	undoRedoHandler.redo();
    }
    
    @FXML
    void changeLevel(MouseEvent event) {
    	gridBoard.setLevel((int)levelSlider.getValue());
    	undoRedoHandler.saveState();
    }
    
    @FXML
    void pencilButtonOn(ActionEvent event) {
    	gridBoard.gridEditor.setCurrentTool(gridBoard.pencilTool);
    	undoRedoHandler.saveState();
    }
    
    @FXML
    void eraserButtonOn(ActionEvent event) {
    	gridBoard.gridEditor.setCurrentTool(gridBoard.eraserTool);
    	undoRedoHandler.saveState();
    }
    
    @FXML
    void digButtonOn(ActionEvent event) {
    	gridBoard.gridEditor.setCurrentTool(gridBoard.digTool);
    	undoRedoHandler.saveState();
    }
    
    @FXML
    void fillToolOn(ActionEvent event) {
    	gridBoard.gridEditor.setCurrentTool(gridBoard.fillTool);
    	undoRedoHandler.saveState();
    }
    
    @FXML
    void selectToolOn(ActionEvent event) {
    	gridBoard.gridEditor.setCurrentTool(gridBoard.selectionTool);
    	undoRedoHandler.saveState();
    }
    
    @FXML
    void terrainToolOn(ActionEvent event) {
    	gridBoard.terrainTool.setCurrentTerrainObject(null);
    	gridBoard.gridEditor.setCurrentTool(gridBoard.terrainTool);
    	undoRedoHandler.saveState();
    }
    
    @FXML
    void terrainToolMountain(ActionEvent event) {
    	gridBoard.terrainTool.setCurrentTerrainObject("Mountain");
    	gridBoard.gridEditor.setCurrentTool(gridBoard.terrainTool);
    	undoRedoHandler.saveState();
    }
    
    @FXML
    void terrainToolVolcano(ActionEvent event) {
    	gridBoard.terrainTool.setCurrentTerrainObject("Volcano");
    	gridBoard.gridEditor.setCurrentTool(gridBoard.terrainTool);
    	undoRedoHandler.saveState();
    }
    
    @FXML
    void generateMaze(ActionEvent event) {
    	gridBoard.generateMaze();
    	undoRedoHandler.saveState();
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
				undoRedoHandler = new UndoRedoHandler(this);
				gridBoard = new GridBoardUI(grid, undoRedoHandler);
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
    	undoRedoHandler.saveState();
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
    
    
    public class State {
    	private Grid grid;
    	
    	public State() {
    		grid = (Grid) App.getGrid().clone();
    	}
    	
    	public void restore() {
    		App.setGrid(grid.clone());
    		refreshUIFromGrid();
    	}
    }

	public State createMemento() {
		return new State();
	}

	public void restoreState(State gridBoardState) {
		gridBoardState.restore();
		
	}

}
