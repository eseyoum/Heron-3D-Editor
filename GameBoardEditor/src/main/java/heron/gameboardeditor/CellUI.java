package heron.gameboardeditor;

import heron.gameboardeditor.datamodel.Block;
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


    public CellUI(GridBoardUI gridBoard, Block block) {
        super(30, 30);
		this.gridBoard = gridBoard;
		this.block = block;
        setFill(Color.LIGHTGRAY);
        setStroke(Color.BLACK);
    }

    public boolean click() {
    	wasClicked = true;
    	
        if (block.isVisible()) {
            setFill(Color.LIGHTGRAY);
            if (block == null) {
            	this.gridBoard.blockSet.remove(block);
            }
            block.setVisible(false);
        } 
        else {
        	setFill(Color.GRAY);
            block.setVisible(true);
            block.setZ(0);

        	//block = new Block(x, y, 0); //The 0 is a placeholder. Eventually, a z coordinate will be added instead of 0.
        	this.gridBoard.blockSet.add(block);
        }
        
        return false;
    }
    
    public Block getBlock() {
    	return block;
    }
}