package heron.gameboardeditor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import heron.gameboardeditor.datamodel.Block;
import heron.gameboardeditor.datamodel.Grid;
import heron.gameboardeditor.tools.DigTool;
import heron.gameboardeditor.tools.EraserTool;
import heron.gameboardeditor.tools.FillTool;
import heron.gameboardeditor.tools.PencilTool;
import heron.gameboardeditor.tools.SelectionTool;
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

    private UndoRedoHandler undoRedoHandler;
    
	private ArrayList<CellUI> edgeCells = new ArrayList<CellUI>(); //stores all cells on the edge of the gridBoard. Used for generating the maze
	private Set<CellUI>solutionPathCells = new HashSet<CellUI>(); //represents the cells in the solution path of the maze
	
    public GridBoardUI(Grid grid) {
        this.gridData = grid;
        cellArray = new CellUI[grid.getWidth()][grid.getHeight()];
        
        for (int y = 0; y < grid.getHeight(); y++) { //creates the grid
            for (int x = 0; x < grid.getWidth(); x++) {
                cellArray[x][y] = new CellUI(this, gridData.getBlockAt(x, y));
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
        
        this.gridEditor = new GridEditor(pencilTool); //pencilTool is the default tool
        
		this.setOnMousePressed(e -> gridEditor.mousePressed(e));
		this.setOnMouseReleased(e -> gridEditor.mouseReleased(e));
		this.setOnMouseDragged(e -> gridEditor.mouseDragged(e));
    }
    
    public Grid getGridData() { //grid data represents the data of the GridUI
		return gridData;
	}
    
    public CellUI getCell(int x, int y) {
        return cellArray[x][y];
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
    
    public void generateMaze() {
    	for (int y = 0; y < gridData.getHeight(); y++) { //may be a better way to go through the cells
            for (int x = 0; x < gridData.getWidth(); x++) {
            	cellArray[x][y].setLevel(2); //sets every cell to level 2
            }
    	}
    	
    	int edgeCellCount = 0;
    	
    	for (int y = 0; y < gridData.getHeight(); y++) { //may be a better way to go through the cells
            for (int x = 0; x < gridData.getWidth(); x++) {
            	if (cellArray[x][y].isEdgeCell()) {
            		edgeCells.add(cellArray[x][y]); //adds all edge cells to an array
            		edgeCellCount = edgeCellCount + 1;
            	}
            }
    	}
    	
    	Random rand = new Random();
    	CellUI cell = edgeCells.get(rand.nextInt(edgeCellCount)); //randomly chooses an edge cell for the start of the maze
    	
    	createSolutionPath(cell);
    	
    	int direction = 0; //1-up, 2-right, 3-down, 4-left
    	cell.setLevel(1);
    	solutionPathCells.add(cell);

    	Block block = cell.getBlock();
    	if (block.getX() == 0) { //cell is on left edge of grid
    		direction = 2;
    	}
    	if (block.getX() == gridData.getWidth() - 1) { //cell is on right edge of grid
    		direction = 4;
    	}
    	if (block.getY() == gridData.getHeight() - 1) { //cell is on bottom edge of grid
    		direction = 1;
    	}
    	if (block.getY() == 0) { //cell is on top of grid
    		direction = 3;
    	}
    	
    	if (direction == 1) { //up
    		createSolutionPath(cellArray[cell.getBlock().getX()][cell.getBlock().getY() - 1]);
    	} else if (direction == 2) { //right
    		createSolutionPath(cellArray[cell.getBlock().getX() + 1][cell.getBlock().getY()]);
    	} else if (direction == 3) { //down
    		createSolutionPath(cellArray[cell.getBlock().getX()][cell.getBlock().getY() + 1]);
    	} else if (direction == 4) { //left
    		createSolutionPath(cellArray[cell.getBlock().getX() - 1][cell.getBlock().getY()]);
    	}
    }
    
    private void createSolutionPath(CellUI cell) {
    	Random rand = new Random();
    	int direction = rand.nextInt(3) + 1;
    	cell.setLevel(1);
    	solutionPathCells.add(cell);
    	
    	if (cell.isEdgeCell()) {
    		return;
    	}
    	
    	if (cell.isThreeAdjacentTilesSame(2)) {
	    	if (direction == 1) { //up
	    		createSolutionPath(cellArray[cell.getBlock().getX()][cell.getBlock().getY() - 1]);
	    	} else if (direction == 2) { //right
	    		createSolutionPath(cellArray[cell.getBlock().getX() + 1][cell.getBlock().getY()]);
	    	} else if (direction == 3) { //down
	    		createSolutionPath(cellArray[cell.getBlock().getX()][cell.getBlock().getY() + 1]);
	    	} else if (direction == 4) { //left
	    		createSolutionPath(cellArray[cell.getBlock().getX() - 1][cell.getBlock().getY()]);
	    	}
    	}
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