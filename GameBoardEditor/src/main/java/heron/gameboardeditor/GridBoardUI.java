package heron.gameboardeditor;

import heron.gameboardeditor.datamodel.Grid;
import heron.gameboardeditor.generators.Maze;
import heron.gameboardeditor.tools.DigTool;
import heron.gameboardeditor.tools.EraserTool;
import heron.gameboardeditor.tools.FillTool;
import heron.gameboardeditor.tools.PencilTool;
import heron.gameboardeditor.tools.SelectionTool;
import heron.gameboardeditor.tools.TerrainTool;
import javafx.scene.layout.AnchorPane;

/**
 * This class represents the grid of cells
 */
public class GridBoardUI extends AnchorPane {

	private Grid gridData;
	private CellUI[][] cellArray;
	
	private int level = 1; //the level of the depth map the user is currently working on. The user starts on level 1
    
    public GridEditor gridEditor; //controls which tool the user is currently using
    
    public final PencilTool pencilTool;
    public final EraserTool eraserTool;
    public final DigTool digTool;
    public final FillTool fillTool;
    public final SelectionTool selectionTool;
    public final TerrainTool terrainTool;

    private UndoRedoHandler undoRedoHandler;
    
    public GridBoardUI(Grid grid) {
        this.gridData = grid;
        cellArray = new CellUI[grid.getWidth()][grid.getHeight()];
        
        for (int x = 0; x < grid.getWidth(); x++) {
        	for (int y = 0; y < grid.getHeight(); y++) { //creates the grid
                cellArray[x][y] = new CellUI(this, x, y);
                cellArray[x][y].setLayoutX(x * CellUI.TILE_SIZE); //spaces out the tiles based on TILE_SIZE
                cellArray[x][y].setLayoutY(y * CellUI.TILE_SIZE);
                this.getChildren().add(cellArray[x][y]);
            }
        }
       
        this.pencilTool = new PencilTool(this, undoRedoHandler);
        this.eraserTool = new EraserTool(this, undoRedoHandler);
        this.digTool = new DigTool(this, undoRedoHandler);
        this.fillTool = new FillTool(this, this.gridData, undoRedoHandler);
        this.selectionTool = new SelectionTool(this, undoRedoHandler);
        this.terrainTool = new TerrainTool(this, undoRedoHandler);
        
        this.gridEditor = new GridEditor(pencilTool); //pencilTool is the default tool
        
		this.setOnMousePressed(e -> gridEditor.mousePressed(e));
		this.setOnMouseReleased(e -> gridEditor.mouseReleased(e));
		this.setOnMouseDragged(e -> gridEditor.mouseDragged(e));
    }
	
    public Grid getGridData() { //grid data represents the data of the GridUI
		return gridData;
	}
    
    public CellUI getCell(int xIndex, int yIndex) throws IndexOutOfBoundsException {
        return cellArray[xIndex][yIndex];
    }
    
    public CellUI getCellAtPixelCoordinates(double x, double y) throws IndexOutOfBoundsException {
    	int xIndex = (int) (x / CellUI.TILE_SIZE);
    	int yIndex = (int) (y / CellUI.TILE_SIZE);
    	return cellArray[xIndex][yIndex];
    }
    
    public int getLevel() {
    	return this.level;
    }
    
    public void setLevel(int level) {
    	this.level = level;
    }
    
    public void updateVisual() {
    	for (int y = 0; y < gridData.getHeight(); y++) { //may be a better way to go through the cells
    		for (int x = 0; x < gridData.getWidth(); x++) {
            	cellArray[x][y].updateVisualBasedOnBlock();
            }
    	}
    }
    
    public void resize(int newWidth, int newHeight) {
    	gridData.resize(newWidth, newHeight);
    }
    
    public void generateMaze() {
    	Maze maze = new Maze(gridData);
    	maze.generateMaze();
    	//gridData.generateMaze();
    	updateVisual();
    }
    
    public class State {
    	private Grid grid;
    	
    	public State() {
    		grid = (Grid) GridBoardUI.this.gridData.clone();
    	}
    	
    	public void restore() {
    		GridBoardUI.this.gridData = (Grid) grid.clone();
    	}
    }

	public State createMemento() {
		return new State();
	}

	public void restoreState(State gridBoardState) {
		gridBoardState.restore();
	}

}