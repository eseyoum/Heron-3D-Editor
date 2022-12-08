package heron.gameboardeditor;

import java.util.ArrayList;
import java.util.List;
import heron.gameboardeditor.datamodel.Block;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * This class represents one cell (or tile) of the GridBoardUI
 */
public class CellUI extends StackPane implements Cloneable {
	
    //public int tileSize = 30; //size of the cells
    public static final int TILE_SIZE = 30;
    public static final int MAX_LEVEL = 5; //number of possible levels
    private static final Color DEFAULT_COLOR = Color.CORNFLOWERBLUE; //default color of the cells
    private static List<Color> colorList = generateColors(); //list of colors for each level of the depth map

//    private final GridBoardUI gridBoard;
    private GridBoardUI gridBoard;
    private int xIndex;
    private int yIndex;
    private boolean isClicked;
    private int tileSize;
    private String displayLevel;
    private Rectangle colorRect;
    private Text levelText;
    
    
    public CellUI(GridBoardUI gridBoard, int xIndex, int yIndex) {//, int tileSize) {
        //super(TILE_SIZE - 1, TILE_SIZE - 1); //a CellUI object is a rectangle
		super();
		this.colorRect = new Rectangle(TILE_SIZE - 1, TILE_SIZE - 1);
		this.levelText = new Text(String.valueOf(1));
		this.getChildren().addAll(colorRect, levelText);
    	this.gridBoard = gridBoard;
		this.xIndex = xIndex;
		this.yIndex = yIndex;
		this.tileSize = TILE_SIZE;
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
    		colorRect.setFill(colorList.get(block.getZ() - 1));
    	} else {
      		colorRect.setFill(DEFAULT_COLOR); //if the cell is not visible, the level is zero
    	}
	}
    

    public void updateVisualDisplayLevel() {
    	//Block block = getBlock(); 
    	//int level = block.getZ();
    	//Label levelLabel = new Label(String.valueOf(level));
    	
    	Text text = new Text("Level");
    	StackPane stackPane = new StackPane();
    	stackPane.getChildren().addAll(text, this);
	}
	
    public void displayLevel(int level) {
    	Block block = getBlock();
    	Text levelText = new Text(String.valueOf(level));
    
    	
    }
    
	public void setLevel(int level) {
		
		Block block = getBlock();
		block.setZ(level);//if cell level is zero it should not be visible
		updateVisualBasedOnBlock();
	}
	public int getLevel() {
		return getBlock().getZ();	
	}

	
    public Block getBlock() { 
    	return gridBoard.getGridData().getBlockAt(xIndex, yIndex);
    }
    
    public void setSelected(boolean status) {
    	isClicked = status;
    	if (isClicked) {
    		colorRect.setStroke(Color.RED);
    		//this.setFill(Color.GREEN);
    	} else {
    		colorRect.setStroke(Color.BLACK);
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
    
    public void zoomIn() {
    	this.tileSize += 10;
	}
    
    public void zoomOut() {
    	this.tileSize -= 10;
	}
    
    public CellUI clone() {
    	try {
    		CellUI clone = (CellUI) super.clone();
    		return clone;
    	} catch (CloneNotSupportedException e) {
    		assert false;
    		return null;
    	}
    }
}