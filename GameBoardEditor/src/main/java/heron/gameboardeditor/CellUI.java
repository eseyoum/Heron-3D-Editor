package heron.gameboardeditor;

import java.util.ArrayList;
import java.util.List;

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
    private Block block;
    public static final int TILE_SIZE = 30;
    
    private static List<Color> colorList = generateColors();
    
    public CellUI(GridBoardUI gridBoard, Block block) {
        super(TILE_SIZE - 1, TILE_SIZE - 1);
		this.gridBoard = gridBoard;
		this.block = block;
		updateVisualBasedOnBlock();
		this.deselect();
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
    
    private void updateVisualBasedOnBlock() {
    	if (block.isVisible()) {
    		setFill(colorList.get(block.getZ() - 1));
    	} else {
      		setFill(Color.AQUA);
    	}
	}

	public boolean click() {
    	if (gridBoard.fillToolButton.isFillToolOn()) { //if the fill tool is selected
        	gridBoard.fillToolButton.fill(block, block.getZ(), gridBoard.getLevel());
        	gridBoard.fillToolButton.fillToolOff();
    	} if (gridBoard.getEraser()) { //if the eraser tool is selected
    		setFillTo(0);
    		block.setVisible(false);
    		block.setZ(0);
            updateVisualBasedOnBlock();
    	} else { //if the pencil tool is selected
        	setFillTo(gridBoard.getLevel());
            block.setVisible(true);
            block.setZ(gridBoard.getLevel());
        	updateVisualBasedOnBlock();
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

	public void select() {
		this.setStroke(Color.RED);
	}
	
	public void deselect() {
		this.setStroke(Color.BLACK);
	}

}