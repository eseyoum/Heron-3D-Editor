package heron.gameboardeditor;

import java.util.ArrayList;

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
    
    private ArrayList<Color> colorList = new ArrayList<Color>();

    public CellUI(GridBoardUI gridBoard, Block block) {
        super(30, 30);
		this.gridBoard = gridBoard;
		this.block = block;
        setFill(Color.LIGHTGRAY);
        setStroke(Color.BLACK);
        
        addColors();
    }
    
    /**
     * Adds the possible colors for differentiating between levels on the depth map
     * 
     */
    public void addColors() {
    	Color color = Color.DARKGREY.darker().darker().darker(); //color for the first level
    	colorList.add(color);
        for (int i = 0; i < 4; i++) {
        	color = color.brighter(); //higher levels are brighter colors 
        	colorList.add(color);
        }
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
    	
    	if (gridBoard.getEraser()) {
    		setFill(Color.LIGHTGREY);
    		block.setVisible(true);
    		block.setZ(0);
    		this.gridBoard.blockSet.remove(block);
    	} else {
        	setFill(colorList.get(gridBoard.getLevel() - 1));
            block.setVisible(true);
            block.setZ(gridBoard.getLevel());
        	//block = new Block(x, y, 0); //The 0 is a placeholder. Eventually, a z coordinate will be added instead of 0.
        	this.gridBoard.blockSet.add(block);
        }
        
        return false;
    }
    
    public Block getBlock() {
    	return block;
    }
}