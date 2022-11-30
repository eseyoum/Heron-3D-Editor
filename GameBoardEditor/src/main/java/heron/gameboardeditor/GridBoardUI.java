package heron.gameboardeditor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import heron.gameboardeditor.datamodel.Block;
import java.util.List;

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
    
    public void generateMaze() { //for maze
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
    	
    	//createSolutionPath(cell);
    	
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
    		createSolutionPath(cellArray[cell.getBlock().getX()][cell.getBlock().getY() - 1], direction, cell);
    	} else if (direction == 2) { //right
    		createSolutionPath(cellArray[cell.getBlock().getX() + 1][cell.getBlock().getY()], direction, cell);
    	} else if (direction == 3) { //down
    		createSolutionPath(cellArray[cell.getBlock().getX()][cell.getBlock().getY() + 1], direction, cell);
    	} else if (direction == 4) { //left
    		createSolutionPath(cellArray[cell.getBlock().getX() - 1][cell.getBlock().getY()], direction, cell);
    	}
    }
    
    private void createSolutionPath(CellUI cell, int previousDirection, CellUI previousCell) { //for maze
    	Random rand = new Random();
    	int direction = rand.nextInt(4) + 1;
    	while (isOppositeDirection(direction, previousDirection)) { //the path should not go backwards
    		direction = rand.nextInt(4) + 1;
    	}
    	
    	if (cell.isEdgeCell()) { //once the path reaches the edge, the path is finished
        	cell.setLevel(1);
        	solutionPathCells.add(cell);
    		return;
    	}
    	
    	if (gridData.isThreeAdjacentBlocksSameLevel(cell.getBlock(), 2)) {
        	cell.setLevel(1);
        	solutionPathCells.add(cell);
    		if (direction == 1) { //up
	    		createSolutionPath(cellArray[cell.getBlock().getX()][cell.getBlock().getY() - 1], direction, cell);
	    	} else if (direction == 2) { //right
	    		createSolutionPath(cellArray[cell.getBlock().getX() + 1][cell.getBlock().getY()], direction, cell);
	    	} else if (direction == 3) { //down
	    		createSolutionPath(cellArray[cell.getBlock().getX()][cell.getBlock().getY() + 1], direction, cell);
	    	} else if (direction == 4) { //left
	    		createSolutionPath(cellArray[cell.getBlock().getX() - 1][cell.getBlock().getY()], direction, cell);
	    	}
    	} else {
    		//if the direction the path tried to go in is not valid
    		createSolutionPath(previousCell, previousDirection, previousCell); //alse need an if there is no direction to go
    	}
    	
//    	if (cell.isThreeAdjacentTilesSame(2)) {
//	    	if (direction == 1 && previousDirection != 3) { //up
//	    		createSolutionPath(cellArray[cell.getBlock().getX()][cell.getBlock().getY() - 1], direction);
//	    	} else if (direction == 2 && previousDirection != 4) { //right
//	    		createSolutionPath(cellArray[cell.getBlock().getX() + 1][cell.getBlock().getY()], direction);
//	    	} else if (direction == 3 && previousDirection != 1) { //down
//	    		createSolutionPath(cellArray[cell.getBlock().getX()][cell.getBlock().getY() + 1], direction);
//	    	} else if (direction == 4 && previousDirection!= 2) { //left
//	    		createSolutionPath(cellArray[cell.getBlock().getX() - 1][cell.getBlock().getY()], direction);
//	    	}
//    	}
    	
//    	if (cell.isThreeAdjacentTilesSame(2)) {
//	    	if (direction == 1) { //up
//	    		if (gridData.getBlockAbove(cell.getBlock()).getZ() == 1) {
//	    			createSolutionPath(cellArray[cell.getBlock().getX()][cell.getBlock().getY()], direction);
//	    		} else {
//	    			createSolutionPath(cellArray[cell.getBlock().getX()][cell.getBlock().getY() - 1], direction);
//	    		}
//	    	} else if (direction == 2) { //right
//	    		if (gridData.getBlockRight(cell.getBlock()).getZ() == 1) {
//	    			createSolutionPath(cellArray[cell.getBlock().getX()][cell.getBlock().getY()], direction);
//	    		} else {
//	    			createSolutionPath(cellArray[cell.getBlock().getX() + 1][cell.getBlock().getY()], direction);
//	    		}
//	    	} else if (direction == 3) { //down
//	    		if (gridData.getBlockBelow(cell.getBlock()).getZ() == 1) {
//	    			createSolutionPath(cellArray[cell.getBlock().getX()][cell.getBlock().getY()], direction);
//	    		} else {
//	    			createSolutionPath(cellArray[cell.getBlock().getX()][cell.getBlock().getY() + 1], direction);
//	    		}
//	    	} else if (direction == 4) { //left
//	    		if (gridData.getBlockLeft(cell.getBlock()).getZ() == 1) {
//	    			createSolutionPath(cellArray[cell.getBlock().getX()][cell.getBlock().getY()], direction);
//	    		} else {
//	    			createSolutionPath(cellArray[cell.getBlock().getX() - 1][cell.getBlock().getY()], direction);
//	    		}
//	    	}
//    	}
    }
    private boolean isOppositeDirection(int direction, int previousDirection) {
    	if ((direction == 1 && previousDirection == 3) || (direction == 3 && previousDirection == 1)) {
    		return true;
    	}
    	if ((direction == 2 && previousDirection == 4) || (direction == 4 && previousDirection == 2)) {
    		return true;
    	}
    	else {
    		return false;
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