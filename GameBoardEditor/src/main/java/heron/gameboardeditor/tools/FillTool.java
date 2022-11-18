package heron.gameboardeditor.tools;

import heron.gameboardeditor.GridBoardUI;
import heron.gameboardeditor.datamodel.Block;
import heron.gameboardeditor.datamodel.Grid;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class FillTool extends Tool {
	private GridBoardUI gridBoard;
	private Grid gridData;
	private boolean fillToolOn;
	
	public FillTool(GridBoardUI gridBoard, Grid gridData) {
		this.gridBoard = gridBoard;
		this.gridData = gridData;
	}
	
	/**
	 * Turns the fill tool on. This occurs when the button is pressed
	 */
	public void fillToolOn() {
    	fillToolOn = true;
    }
    
	/**
	 * Turns the fill tool off. This occurs after the area is filled
	 */
    public void fillToolOff() {
    	fillToolOn = false;
    }
    
    /**
     * 
     * @return whether the fill tool is on or off
     */
    public boolean isFillToolOn() {
    	return fillToolOn;
    }
    
    /**
     * 
     * @param block - the block the user clicks when the fill tool is active
     * @param startingLevel - the level of the cell the user first clicks
     * @param turnToLevel - the level which the area should turn to
     * @param gridData - the data of the grid. This is used to find surrounding cells
     */
	public void fill(Block block, int startingLevel, int turnToLevel) {
    	gridBoard.getCell(block.getX(), block.getY()).setFillTo(turnToLevel);
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
    			gridBoard.getCell(block.getX(), block.getY()).setFillTo(turnToLevel);
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
    			gridBoard.getCell(block.getX(), block.getY()).setFillTo(turnToLevel);
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
