package heron.gameboardeditor;


import java.util.Set;

import heron.gameboardeditor.datamodel.Block;

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
    private int width; //number of cells of the width
    private int height; //number of cells of the height
  	
	private int level = 1; //the level of the depth map the user is currently working on. The user starts on level 1
    
    public GridEditor gridEditor; //controls which tool the user is currently using
    
    public final PencilTool pencilTool;
    public final EraserTool eraserTool;
    public final DigTool digTool;
    public final FillTool fillTool;
    public final SelectionTool selectionTool;
    public final TerrainTool terrainTool;
    private UndoRedoHandler undoRedoHandler;
    
    private double tileSize;
    
	public GridBoardUI(Grid grid, UndoRedoHandler undoRedoHandler) {
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
		
		this.width = gridData.getWidth();
		this.height = gridData.getHeight();
    }
    
    public void updateVisualBasedOnGrid() {
    	CellUI[][] newCellArray = new CellUI[gridData.getWidth()][gridData.getHeight()];
    	for (int x = 0; x < gridData.getWidth(); x++) {
        	for (int y = 0; y < gridData.getHeight(); y++) {
        		if (x >= width || y >= height) { //if grid board must add new cells
        			newCellArray[x][y] = new CellUI(this, x, y);
                    newCellArray[x][y].setLayoutX(x * CellUI.TILE_SIZE);
                    newCellArray[x][y].setLayoutY(y * CellUI.TILE_SIZE);
                    this.getChildren().add(newCellArray[x][y]);
        		} else { //if contains cell, copy the cell to the new array
        			newCellArray[x][y] = cellArray[x][y];
        		}
        	}
    	}
    	
    	for (int x = 0; x < width; x++) {
        	for (int y = 0; y < height; y++) {
        		if (x >= gridData.getWidth() || y >= gridData.getHeight()) { //remove the cells if they are not in the new gridBoard size
        			this.getChildren().remove(cellArray[x][y]);
        		}
        	}
    	}
    	
    	this.cellArray = newCellArray;
    	this.width = gridData.getWidth();
    	this.height = gridData.getHeight();
    }
    
    public double getTileSize() { 
		return tileSize;
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

    
    public void setTileSize(double size) { 
    	this.tileSize = size;
    	for (int y = 0; y < gridData.getHeight(); y++) { 
    		for (int x = 0; x < gridData.getWidth(); x++) {
               	cellArray[x][y].setWidth(size-1);
            	cellArray[x][y].setHeight(size-1);
 
            	cellArray[x][y].setLayoutX(x*size);
            	cellArray[x][y].setLayoutY(y*size);
            }
    	}
	}
    
  
    
    public void clear() {
    	for (int y = 0; y < gridData.getHeight(); y++) { //may be a better way to go through the cells
    		for (int x = 0; x < gridData.getWidth(); x++) {
    			if(gridData.getBlockAt(x, y).getZ() != 0) {
    				gridData.getBlockAt(x, y).setZ(0);
    				cellArray[x][y].updateVisualBasedOnBlock();
				}
            }
    	}
    }
    
    public void resize(int newWidth, int newHeight) {
    	gridData.resize(newWidth, newHeight);
    }
    
    public void setAllSelectedCellsToLevel(int level) {
    	Set<CellUI> selectedCells = selectionTool.getSelectedCells();
    	for(CellUI cell : selectedCells){
    		cell.setLevel(level);
    	}
    }
    
    public void selectLevel(boolean isSelected) {
    	for (int y = 0; y < gridData.getHeight(); y++) { //may be a better way to go through the cells
    		for (int x = 0; x < gridData.getWidth(); x++) {
            	if ((cellArray[x][y]).getBlock().getZ() == level) {
            		if (isSelected) {
            			selectionTool.addSelectedCell(cellArray[x][y]);
            		} else {
            			selectionTool.removeSelectedCell(cellArray[x][y]);
            		}
            	}
            }
    	}
    }
    
    public void generateMaze() {
    	Maze maze = new Maze(gridData);
    	maze.generateMaze();
    	//gridData.generateMaze();
    	updateVisual();
    }
    

}