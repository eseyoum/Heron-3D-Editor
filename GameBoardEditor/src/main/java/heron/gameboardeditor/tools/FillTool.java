package heron.gameboardeditor.tools;

import java.util.ArrayList;
import java.util.List;

import heron.gameboardeditor.CellUI;
import heron.gameboardeditor.GridBoardUI;
import heron.gameboardeditor.UndoRedoHandler;
import heron.gameboardeditor.datamodel.Block;
import heron.gameboardeditor.datamodel.Grid;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

public class FillTool extends Tool {
	private GridBoardUI gridBoard;
	private Grid gridData;
	
	public FillTool(GridBoardUI gridBoard, Grid gridData, UndoRedoHandler handler) {
		super(handler);
		this.gridBoard = gridBoard;
		this.gridData = gridData;
	}
    
    public void mousePressed(MouseEvent e) {
    	CellUI cellClicked = gridBoard.getCell((int) e.getX() / CellUI.TILE_SIZE, (int) e.getY() / CellUI.TILE_SIZE); //the initial cell which is clicked
    	fill(cellClicked.getBlock(), cellClicked.getBlock().getZ(), gridBoard.getLevel());
    	gridBoard.updateVisual();
    }
    
	public void fill(Block block, int startingLevel, int turnToLevel) {
//    	CellUI initialCellClicked = gridBoard.getCell(block.getX(), block.getY());
//    	initialCellClicked.setLevel(turnToLevel);
    	block.setZ(turnToLevel);
    	block.setVisible(true);
    	
    	if (gridData.isEdgeBlock(block)) {
    		//initialCellClicked.setLevel(turnToLevel);
    		block.setZ(turnToLevel);
    		block.setVisible(true);
    		handleEdgeBlock(block, startingLevel, turnToLevel);
    	} else {
    		fillSurroundingCells(block, startingLevel, turnToLevel);
    	}
    	return;
    }
    
    private void fill(Block block, Block prevBlock, int startingLevel, int turnToLevel) {
    		//CellUI cellClicked = gridBoard.getCell(block.getX(), block.getY());
    		if (block.getZ() != startingLevel) {
    			return;
    		}
    		if (gridData.isEdgeBlock(block)) {
    			//cellClicked.setLevel(turnToLevel);
    			block.setZ(turnToLevel);
    			block.setVisible(true);
    			handleEdgeBlock(block, startingLevel, turnToLevel);
    		} else {
    			//gridBoard.getCell(block.getX(), block.getY()).setLevel(turnToLevel);
    			block.setZ(turnToLevel);
    			block.setVisible(true);
    			fillSurroundingCells(block, startingLevel, turnToLevel);
    		}
    		return;
    }
    
    private void fillSurroundingCells(Block block, int startingLevel, int turnToLevel) {
    	fill(gridData.getBlockAbove(block), block, startingLevel, turnToLevel); //move up
    	fill(gridData.getBlockRight(block), block, startingLevel, turnToLevel); //move right
    	fill(gridData.getBlockBelow(block), block, startingLevel, turnToLevel); //move down
    	fill(gridData.getBlockLeft(block), block, startingLevel, turnToLevel); //move left
    }
    
    private void handleEdgeBlock(Block block, int startingLevel, int turnToLevel) {
    	if (gridData.isCornerBlock(block)) {
	    	handleCornerBlock(block, startingLevel, turnToLevel);
    	} else {
	    	if (block.getX() == 0) { //block is on left edge of grid
	        	fill(gridData.getBlockAbove(block), block, startingLevel, turnToLevel); //move up
	        	fill(gridData.getBlockRight(block), block, startingLevel, turnToLevel); //move right
	        	fill(gridData.getBlockBelow(block), block, startingLevel, turnToLevel); //move down
	    	}
	    	if (block.getX() == gridData.getWidth() - 1) { //block is on right edge of grid
	        	fill(gridData.getBlockAbove(block), block, startingLevel, turnToLevel); //move up
	        	fill(gridData.getBlockBelow(block), block, startingLevel, turnToLevel); //move down
	        	fill(gridData.getBlockLeft(block), block, startingLevel, turnToLevel); //move left
	    	}
	    	if (block.getY() == gridData.getHeight() - 1) { //block is on bottom edge of grid
	        	fill(gridData.getBlockAbove(block), block, startingLevel, turnToLevel); //move up
	        	fill(gridData.getBlockRight(block), block, startingLevel, turnToLevel); //move right
	        	fill(gridData.getBlockLeft(block), block, startingLevel, turnToLevel); //move left
	    	}
	    	if (block.getY() == 0) { //block is on top of grid
	        	fill(gridData.getBlockRight(block), block, startingLevel, turnToLevel); //move right
	        	fill(gridData.getBlockBelow(block), block, startingLevel, turnToLevel); //move down
	        	fill(gridData.getBlockLeft(block), block, startingLevel, turnToLevel); //move left
	    	}
    	}
    }
    
    private void handleCornerBlock(Block block, int startingLevel, int turnToLevel) {
    	if (block.getX() == 0 && block.getY() == 0) {//block is on top left of grid
        	fill(gridData.getBlockRight(block), block, startingLevel, turnToLevel); //move right
        	fill(gridData.getBlockBelow(block), block, startingLevel, turnToLevel); //move down
    	}
    	if (block.getX() == 0 && block.getY() == gridData.getHeight() - 1) {//block is on bottom left of grid
        	fill(gridData.getBlockAbove(block), block, startingLevel, turnToLevel); //move up
        	fill(gridData.getBlockRight(block), block, startingLevel, turnToLevel); //move right
    	}
    	if (block.getX() == gridData.getWidth() - 1 && block.getY() == 0) {//block is on top right of grid
        	fill(gridData.getBlockBelow(block), block, startingLevel, turnToLevel); //move down
        	fill(gridData.getBlockLeft(block), block, startingLevel, turnToLevel); //move left
    	}
    	if (block.getX() == gridData.getWidth() - 1 && block.getY() == gridData.getHeight() - 1) {//block is on bottom right of grid
        	fill(gridData.getBlockAbove(block), block, startingLevel, turnToLevel); //move up
        	fill(gridData.getBlockLeft(block), block, startingLevel, turnToLevel); //move left
    	}
    }
}
