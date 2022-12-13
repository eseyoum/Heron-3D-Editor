package heron.gameboardeditor;

import java.util.ArrayList;
import java.util.List;
import heron.gameboardeditor.datamodel.Block;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.effect.ColorAdjust; 

/**
 * This class represents one cell (or tile) of the GridBoardUI
 */
public class CellUI extends StackPane implements Cloneable {
	
    //public int tileSize = 30; //size of the cells
    public static final int DEFAULT_TILE_SIZE = 30;
    private static final Color DEFAULT_COLOR = Color.CORNFLOWERBLUE; //default color of the cells
    
    private static List<Color> colorList; //list of colors for each level of the depth map
    private static Color firstLevelColor = Color.DARKGREY.darker().darker().darker(); //color for the first level

    private static GridBoardUI gridBoard;
    private int xIndex;
    private int yIndex;
    private boolean isClicked;

    private int tileSize;
    private String displayLevel;
    private Rectangle colorRect;
	private Text levelText;
	private Line pointyLine;
	private boolean showLevel = false;
	
    public CellUI(GridBoardUI gridBoard, int xIndex, int yIndex, int tileSize) {
		super();
		this.colorRect = new Rectangle(tileSize - 1, tileSize - 1);
		//this.levelText = new Text("");
		this.getChildren().addAll(colorRect);
    	this.gridBoard = gridBoard;
		this.xIndex = xIndex;
		this.yIndex = yIndex;
		this.levelText = new Text("");
		updateVisualBasedOnBlock();
		this.setSelected(false);
		generateColors();
    }

    /**
     * Adds the possible colors for differentiating between levels on the depth map
     */
    public static void generateColors() {
    	int maxLevel = gridBoard.getGridData().getMaxZ();
    	List<Color> colors = new ArrayList<>();
    	Color color = firstLevelColor;
    	colors.add(color);
    	double initialBrightness = color.getBrightness();
    	double brightnessIncrease = ((double) 1 - initialBrightness) / ((double) maxLevel - 1); //how much each level's color should be increased from the previous
        for (int i = 0; i < maxLevel - 1; i++) {
        	color = brightenColor(color, brightnessIncrease);
        	colors.add(color);
        }
        colorList = colors;
    }
    
    private static Color brightenColor(Color color, double brightnessIncrease) {
    	double newBrightness = color.getBrightness() + brightnessIncrease;
    	if (newBrightness > 1) { //1 is the highest brightness can be
    		newBrightness = 1;
    	}
    	return Color.hsb(color.getHue(), color.getSaturation(), newBrightness, color.getOpacity());
    }
    
    public void createPointyLine() {
    	Line stroke = new Line(0, 0, 10, 10);
    	stroke.setStrokeWidth(10);
    	stroke.setStroke(Color.YELLOW);
    	this.pointyLine = stroke;
    }
    
    public boolean isPointy() {
    	return getBlock().isPointy();
    }
    
	public void setPointy(boolean pointy) {
		Block block = getBlock(); 
		block.setPointy(pointy);
		updateVisualBasedOnBlock();
	}
    
	/**
     * Updates the cell color to reflect the level of the block
     */
    public void updateVisualBasedOnBlock() {
    	Block block = getBlock(); 
    	if (block.isVisible()) {
    		colorRect.setFill(colorList.get(getLevel() - 1));
    	} else {
      		colorRect.setFill(DEFAULT_COLOR); //if the cell is not visible, the level is zero
      		block.setPointy(false);
    	}
    	
    	if (showLevel) {
        	updateVisualDisplayLevel();
    	}
    	
    	createPointyLine();
    	if(block.isPointy()) {
    		this.getChildren().add(pointyLine);
    	} else {
    		this.getChildren().remove(pointyLine);
    	}
	}
    
//    public void updateVisualPointy() {
//    	Block block = getBlock();
//    	if(block.isPointy()) {
//    		this.getChildren().add(this.pointyLine);
//    	} else {
//    		this.getChildren().remove(this.pointyLine);
//    	}
//	}
    
   
    @FXML
    /**
     * This method shows the level text on each cell.
     */
    public void updateVisualDisplayLevel() {
    	updateVisualRemoveLevel();

    	Block block = getBlock(); 
    	int level = block.getZ();
    	Text text = new Text(String.valueOf(level));
    	this.levelText = text;
		this.getChildren().addAll(levelText);
		showLevel = true;
	}
    
    @FXML
    /**
     * This method removes the level text on each cell.
     */
    public void updateVisualRemoveLevel() {
    	Block block = getBlock(); 
    	int level = block.getZ();
		this.getChildren().remove(this.levelText);
		showLevel = false;
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
    
    public String getDisplayLevel() {
  		return displayLevel;
  	}

  	public Rectangle getColorRect() {
  		return colorRect;
  	}
  	
  	//SELECTION TOOL
    public void setSelected(boolean status) {
    	isClicked = status;
    	if (isClicked) {
    		colorRect.setStroke(Color.RED);
    	} else {
    		colorRect.setStroke(Color.BLACK);
    	}
    }
    public boolean isSelected() {
    	return isClicked;
    }
    
    //TERRAIN TOOL
    public boolean isEdgeCell() {
    	return (xIndex == gridBoard.getGridData().getWidth() - 1 || xIndex == 0 || yIndex == gridBoard.getGridData().getHeight() - 1 || yIndex == 0);
    }
    
    public boolean isCornerCell() {
    	return ((xIndex == 0 && yIndex == 0) || (xIndex == 0 && yIndex == gridBoard.getGridData().getHeight() - 1) || (xIndex == gridBoard.getGridData().getWidth() - 1 && yIndex == 0) || (xIndex == gridBoard.getGridData().getWidth() - 1 && yIndex == gridBoard.getGridData().getHeight() - 1));
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