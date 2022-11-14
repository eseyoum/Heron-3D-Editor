package heron.gameboardeditor;

import java.util.ArrayList;

import heron.gameboardeditor.datamodel.Block;
import heron.gameboardeditor.datamodel.Grid;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
    	if (gridBoard.fillToolButton.isFillToolOn()) { //if the fill tool is selected
        	gridBoard.fillToolButton.fill(block, block.getZ(), gridBoard.getLevel());
        	gridBoard.fillToolButton.fillToolOff();
    	} if (gridBoard.getEraser()) { //if the eraser tool is selected
    		setFillTo(0);
    		block.setVisible(true);
    		block.setZ(0);
    		this.gridBoard.blockSet.remove(block);
    	} else { //if the pencil tool is selected
        	setFillTo(gridBoard.getLevel());
            block.setVisible(true);
            block.setZ(gridBoard.getLevel());
        	this.gridBoard.blockSet.add(block);
        }
    return false;	
    }
    
    public void setFillTo(int turnToLevel) {
    	if (turnToLevel == 0) {
    		setFill(Color.LIGHTGREY);
    	} else {
    		setFill(colorList.get(turnToLevel - 1));
    	}
    }
    
    public Block getBlock() {
    	return block;
    }
}