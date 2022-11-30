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
	private boolean fillToolOn;

	public FillTool(GridBoardUI gridBoard, Grid gridData, UndoRedoHandler handler) {
		super(handler);
		this.gridBoard = gridBoard;
		this.gridData = gridData;
		fillToolOn = false;
	}
	
	public void fillToolOn() {
    	fillToolOn = true;
    }
    
    public void fillToolOff() {
    	fillToolOn = false;
    }
 
    public boolean isFillToolOn() {
    	return fillToolOn;
    }
    
    public void mousePressed(MouseEvent e) {
    	CellUI cellClicked = gridBoard.getCell((int) e.getX() / CellUI.TILE_SIZE, (int) e.getY() / CellUI.TILE_SIZE); //the initial cell which is clicked
    	fill(cellClicked.getBlock(), cellClicked.getBlock().getZ(), gridBoard.getLevel());
    	gridBoard.getAllClickedCells().add(cellClicked);
    }
    
	public void fill(Block block, int startingLevel, int turnToLevel) {
    	gridBoard.getCell(block.getX(), block.getY()).setLevel(turnToLevel);
        block.setVisible(true);
        block.setZ(turnToLevel);
        
        fill(gridData.getBlockAt(block.getX() + 1, block.getY()), block, startingLevel, turnToLevel);
		fill(gridData.getBlockAt(block.getX() - 1, block.getY()), block, startingLevel, turnToLevel);
		fill(gridData.getBlockAt(block.getX(), block.getY() + 1), block, startingLevel, turnToLevel);
		fill(gridData.getBlockAt(block.getX(), block.getY() - 1), block, startingLevel, turnToLevel);
    	return;
    }
    
    private void fill(Block block, Block prevBlock, int startingLevel, int turnToLevel) {
    		if (block == null) {
    			return;
    		}
    		if (block.getX() + 1 > gridData.getWidth() - 1 || block.getY() + 1 > gridData.getHeight() - 1 || block.getX() - 1 < 0 || block.getY() - 1 < 0) {
    			gridBoard.getCell(block.getX(), block.getY()).setLevel(turnToLevel);
    	        block.setVisible(true);
    	        block.setZ(turnToLevel);
    			return;
    		}
    		
    		if (block.getZ() != startingLevel) {
    			return;
    		}
    		
    		if (block.getZ() == prevBlock.getZ()) {
    			return;
    		}
    		
    		else {
    			gridBoard.getCell(block.getX(), block.getY()).setLevel(turnToLevel);
    	        block.setVisible(true);
    	        block.setZ(turnToLevel);
    	    	
    	        try {
    	        	fill(gridData.getBlockAt(block.getX() + 1, block.getY()), block, startingLevel, turnToLevel);
    	        	fill(gridData.getBlockAt(block.getX() - 1, block.getY()), block, startingLevel, turnToLevel);
    	        	fill(gridData.getBlockAt(block.getX(), block.getY() + 1), block, startingLevel, turnToLevel);
    	        	fill(gridData.getBlockAt(block.getX(), block.getY() - 1), block, startingLevel, turnToLevel);
    			return;
    	        } catch (StackOverflowError e) {
    	        	Alert a = new Alert(AlertType.ERROR);
    	        	a.show();
    	        	System.exit(1);
    	        	return;
    	        }
    		}
    }
}
