package heron.gameboardeditor;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import heron.gameboardeditor.datamodel.Block;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * This class represents one cell (or tile) of the GridBoardUI
 */
public class CellUI extends Rectangle implements Cloneable {
    public static final int TILE_SIZE = 30; //size of the cells
    public static final int MAX_LEVEL = 5; //number of possible levels
    private static final Color DEFAULT_COLOR = Color.CORNFLOWERBLUE; //default color of the cells
    private static List<Color> colorList = generateColors(); //list of colors for each level of the depth map
    
//    private final GridBoardUI gridBoard;
    private GridBoardUI gridBoard;
    private int xIndex;
    private int yIndex;
    private boolean isClicked;
    
    public CellUI(GridBoardUI gridBoard, int xIndex, int yIndex) {
        super(TILE_SIZE - 1, TILE_SIZE - 1); //a CellUI object is a rectangle
		this.gridBoard = gridBoard;
		this.xIndex = xIndex;
		this.yIndex = yIndex;
		updateVisualBasedOnBlock();
		this.setSelected(false);
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
    	Block block = getBlock(); 
    	if (block.isVisible()) {
    		setFill(colorList.get(block.getZ() - 1));
    	} else {
      		setFill(DEFAULT_COLOR); //if the cell is not visible, the level is zero
    	}
	}
    
//    public void export() throws IOException {
//    	FileWriter writer = new FileWriter("hi");
//    	String file;
//    	for (int i = 0; i < xIndex; i++) {
//    		for(int y = 0; y < yIndex; y++) {
//    			if()
//    			file = "v " + ((TILE_SIZE - 1) / 2) + () + (if);
//    		}
//    	}
//    	writer.write("jldf");
//    	writer.close();
//    }
	
	public void setLevel(int level) {
		Block block = getBlock();
		block.setZ(level);//if cell level is zero it should not be visible
		updateVisualBasedOnBlock();
	}
		
    public Block getBlock() { 
    	return gridBoard.getGridData().getBlockAt(xIndex, yIndex);
    }
    
    public void setSelected(boolean status) {
    	isClicked = status;
    	if (isClicked) {
    		this.setStroke(Color.RED);
    		this.setFill(Color.GREEN);
    	} else {
    		this.setStroke(Color.BLACK);
    		this.setFill(Color.BEIGE);
    	}
    }
    public boolean isSelected() {
    	return isClicked;
    }
    
    public boolean isEdgeCell() {
    	return (xIndex == gridBoard.getGridData().getWidth() - 1 || xIndex == 0 || yIndex == gridBoard.getGridData().getHeight() - 1 || yIndex == 0);
    }
    
    public boolean isCornerCell() {
    	return ((xIndex == 0 && yIndex == 0) || (xIndex == 0 && yIndex == gridBoard.getGridData().getHeight() - 1) || (xIndex == gridBoard.getGridData().getWidth() - 1 && yIndex == 0) || (xIndex == gridBoard.getGridData().getWidth() - 1 && yIndex == gridBoard.getGridData().getHeight() - 1));
    }
    
    public CellUI clone() {
    	try {
    		CellUI clone = (CellUI) super.clone();
    		clone.gridBoard = gridBoard;
    		clone.xIndex = xIndex;
    		clone.yIndex = yIndex;
    		clone.isClicked = isClicked;
    		clone.updateVisualBasedOnBlock();
    		clone.setSelected(false);
    		return clone;
    	} catch (CloneNotSupportedException e) {
    		assert false;
    		return null;
    	}
    }
}