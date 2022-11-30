package heron.gameboardeditor;

import java.util.ArrayList;
import java.util.List;
import heron.gameboardeditor.datamodel.Block;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * This class represents one cell (or tile) of the GridBoardUI
 */
public class CellUI extends Rectangle {
    public static final int TILE_SIZE = 30; //size of the cells
    public static final int MAX_LEVEL = 5; //number of possible levels
    private static final Color DEFAULT_COLOR = Color.CORNFLOWERBLUE; //default color of the cells
    private static List<Color> colorList = generateColors(); //list of colors for each level of the depth map
    
    private final GridBoardUI gridBoard;
    private Block block;
    private boolean isClicked;
    
    public CellUI(GridBoardUI gridBoard, Block block) {
        super(TILE_SIZE - 1, TILE_SIZE - 1); //a CellUI object is a rectangle
		this.gridBoard = gridBoard;
		this.block = block;
		updateVisualBasedOnBlock();
		this.deselect();
    }
    
    /**
     * Adds the possible colors for differentiating between levels on the depth map
     */
    private static List<Color> generateColors() {
    	List<Color> colors = new ArrayList<>();
    	Color color = Color.DARKGREY.darker().darker().darker(); //color for the first level
    	colors.add(color);
        for (int i = 0; i < MAX_LEVEL - 1; i++) {
        	color = color.brighter(); //higher levels are brighter colors 
        	colors.add(color);
        }
        return colors;
    }
    
    /**
     * Updates the cell color to reflect the level of the block
     */
    public void updateVisualBasedOnBlock() {
    	if (block.isVisible()) {
    		setFill(colorList.get(block.getZ() - 1));
    	} else {
      		setFill(DEFAULT_COLOR); //if the cell is not visible, the level is zero
    	}
	}
	
	public void setLevel(int level) {
		block.setZ(level);
		
		if (level != 0) {
			block.setVisible(true);
		} else {
			block.setVisible(false); //if cell level is zero it should not be visible
		}
		
		updateVisualBasedOnBlock();
	}

	public void select() {
		this.setStroke(Color.RED);
	}
	
	public void deselect() {
		this.setStroke(Color.BLACK);
	}
	
    public Block getBlock() {
    	return block;
    }
    
    public void setSelected(boolean status) {
    	isClicked = status;
    }
    public boolean isSelected() {
    	return isClicked;
    }
    
//    public boolean isEdgeCell() {
//    	return (block.getX() == gridBoard.getGridData().getWidth() - 1 || block.getX() == 0 || block.getY() == gridBoard.getGridData().getHeight() - 1 || block.getY() == 0);
//    }
//    
//    public boolean isCornerCell() {
//    	return ((block.getX() == 0 && block.getY() == 0) || (block.getX() == 0 && block.getY() == gridBoard.getGridData().getHeight() - 1) || (block.getX() == gridBoard.getGridData().getWidth() - 1 && block.getY() == 0) || (block.getX() == gridBoard.getGridData().getWidth() - 1 && block.getY() == gridBoard.getGridData().getHeight() - 1));
//    }

}