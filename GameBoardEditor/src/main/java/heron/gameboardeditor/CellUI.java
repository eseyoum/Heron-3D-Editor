package heron.gameboardeditor;

import java.util.ArrayList;
import java.util.List;

import heron.gameboardeditor.datamodel.Block;
import heron.gameboardeditor.datamodel.Grid;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CellUI extends Rectangle {
    /**
	 * 
	 */
	private final GridBoardUI gridBoard;
    public Block block;
    public boolean wasClicked = false;
    public boolean blockPlaced = false;
    public Grid gridData;
    
    private static List<Color> colorList = generateColors();

    
    public CellUI(GridBoardUI gridBoard, Block block) {
        super(30, 30);
		this.gridBoard = gridBoard;
		this.block = block;
		updateVisualBasedOnBlock();
    }
    

    private void updateVisualBasedOnBlock() {
    	if (block.isVisible()) {
    		setFill(colorList.get(block.getZ() - 1));
    	} else {
      		setFill(Color.AQUA);
      	    		
    	}
        setStroke(Color.BLACK);
		
	}


	public boolean click() {
    	wasClicked = true;
    	
//        if (block.isVisible()) { //this code could be used later for an eraser tool -Corey
//            setFill(Color.LIGHTGRAY);
//            if (block == null) {
//            	this.gridBoard.blockSet.remove(block);
//            }
//            block.setVisible(false);
//        } 
        //else {
        	//setFill(Color.GRAY);
    	
    	if (gridBoard.fillTool) {
    		int startingLevel = block.getZ();
    		int turnToLevel = gridBoard.getLevel();
        	//this.gridBoard.blockSet.add(block);
        	gridData = gridBoard.gridData;
        	fill(block, startingLevel, turnToLevel, gridData);
        	
        	gridBoard.fillToolOff();
    		return false;
    	} else {
    	
    	if (gridBoard.getEraser()) {
    		block.setVisible(false);
    		block.setZ(0);
            updateVisualBasedOnBlock();
    		this.gridBoard.blockSet.remove(block);
    	} else {
            block.setZ(gridBoard.getLevel());
            block.setVisible(true);
            
            updateVisualBasedOnBlock();
        	//block = new Block(x, y, 0); //The 0 is a placeholder. Eventually, a z coordinate will be added instead of 0.
        	this.gridBoard.blockSet.add(block);
        }
        
        return false;
    }}
    
    public void setFillTo(int turnToLevel) {
    	setFill(colorList.get(turnToLevel - 1));
    }
    
    private void fill(Block block, int startingLevel, int turnToLevel, Grid gridData) {
    	//setFill(colorList.get(turnToLevel - 1)); //unsure if this will work
    	gridBoard.getCell(block.getX(), block.getY()).setFillTo(turnToLevel);
        block.setVisible(true);
        block.setZ(turnToLevel);
    	//this.gridBoard.blockSet.add(block);
		fill(gridData.getBlockAt(block.getX() + 1, block.getY()), block, startingLevel, turnToLevel, gridData);
		fill(gridData.getBlockAt(block.getX() - 1, block.getY()), block, startingLevel, turnToLevel, gridData);
		fill(gridData.getBlockAt(block.getX(), block.getY() + 1), block, startingLevel, turnToLevel, gridData);
		fill(gridData.getBlockAt(block.getX(), block.getY() - 1), block, startingLevel, turnToLevel, gridData);
    }
    
    private void fill(Block block, Block prevBlock, int startingLevel, int turnToLevel, Grid gridData) {
    		if (block == null) {
    			return;
    		}
    		else if (block.getZ() != startingLevel) {
    			return;
    		}
    		
    		else {
    			//setFill(colorList.get(turnToLevel - 1)); //unsure if this will work
    			gridBoard.getCell(block.getX(), block.getY()).setFillTo(turnToLevel);
    	        block.setVisible(true);
    	        block.setZ(turnToLevel);
    	    	//this.gridBoard.blockSet.add(block);
    	    	
    	    	fill(gridData.getBlockAt(block.getX() + 1, block.getY()), block, startingLevel, turnToLevel, gridData);
    			fill(gridData.getBlockAt(block.getX() - 1, block.getY()), block, startingLevel, turnToLevel, gridData);
    			fill(gridData.getBlockAt(block.getX(), block.getY() + 1), block, startingLevel, turnToLevel, gridData);
    			fill(gridData.getBlockAt(block.getX(), block.getY() - 1), block, startingLevel, turnToLevel, gridData);
    		}
    }
    
    public Block getBlock() {
    	return block;
    }
    
    /**
     * Adds the possible colors for differentiating between levels on the depth map
     * 
     */
    public static List<Color> generateColors() {
    	List<Color> colors = new ArrayList<>();
    	Color color = Color.DARKGREY.darker().darker().darker(); //color for the first level
    	colors.add(color);
        for (int i = 0; i < 4; i++) {
        	color = color.brighter(); //higher levels are brighter colors 
        	colors.add(color);
        }
        return colors;
    }


}