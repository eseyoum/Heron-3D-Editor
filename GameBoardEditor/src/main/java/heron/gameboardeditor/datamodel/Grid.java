package heron.gameboardeditor.datamodel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import heron.gameboardeditor.CellUI;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * This class represents the data of the GridBoardUI class
 */
public class Grid implements Cloneable {

	private Block[][] blockGrid;
	private int width;
	private int height;
	
//	private ArrayList<Block> edgeBlocks = new ArrayList<Block>(); //stores all blocks on the edge of the gridBoard. Used for generating the maze
//
//	private Set<Block>solutionPathBlocks = new HashSet<Block>(); //represents the blocks in the solution path of the maze
//
//	private Block failedMovementMazeBlock = new Block (0, 0, 0); //when creating the maze, this represents a block which cannot move in a certain direction
//	private int failedDirectionCount; //count of failed directions. If a failedMovementMazeBlock has 3 failed directions, it cannot move
//	private ArrayList<Block>mazeBranchBlocks = new ArrayList<Block>();
//	private Block possibleEndBlock;
//	private int numBranches = 4; //the number of times branches should be made off of each other
//	private int mazeBorderLevel = 2; //the level of the borders of the maze
//	private int mazePathLevel = 1; //the level of the path of the maze
	/**
	 * Constructs a grid 
	 * 
	 * @param width - the number of columns
	 * @param height - the number of rows
	 * 
	 */
	public Grid(int width, int height) {
		this.width = width;
		this.height = height;
		blockGrid = new Block[width][height];
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				blockGrid[x][y] = new Block(x,y,0);
			}			
		}
	}

	public void repaint() {
		
	}
	
	/**
	 * This method returns the block at position (x,y)
	 * 
	 * @return blockGrid[x][y] - a block at position (x,y)
	 */
	public Block getBlockAt(int x, int y) {
		return blockGrid[x][y];
	}

	
	/**
	 * This method return the width of the grid
	 * 
	 * @return width - the width of the grid    
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * This method return the height of the grid
	 * 
	 * @return height - the height of the grid    
	 */
	public int getHeight() {
		return height;
	}
	
	
	/**
	 * This method return biggest level of the grid
	 * 
	 * @return max - the biggest level of the grid
	 */
	public int getMaxLevel() {
		int max = -1;
		for (int y = 0; y < width; y++) {
			for (int x = 0; x < height; x++) {
				Block block = getBlockAt(x, y);
				int level = block.getZ();
				if (level > max) {
					max = level;
				}
			}			
		}
		return max;
	}
	
	/**
	 * This method return blockGrid
	 * 
	 * @return blockGrid - the array array of blocks
	 */
	public Block[][] getBlockGrid() {
		return blockGrid;
	}
	
	/**
	 * This method checks if a coordinate is inside the grid
	 * 
	 * @param x - the x coordinate
	 * @param y - the y coordinate
	 * 
	 * @return true if the coordinate is inside the grid
	 * @return false if the coordinate is not inside the grid
	 * 
	 */
	public boolean isCoordinateInGrid(int x, int y) {
		if ((x < 0) || (x > width - 1)) {
			return false;
		} else if ((y < 0) || (y > height - 1)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * This method allows us to resize the grid by creating a new gird (with a new width and a new height) 
	 * and set the data field blockGrid to this new grid. 
	 * The new grid is created based on the old grid, hence, the information of the cells (from the old grid)
	 * which fit in the new grid are kept and moved to the new grid.
	 * 
	 * @param newWidth - the number of columns of the new grid
	 * @param newHeight - the number of rows of the new grid
	 *           
	 */
	public void resize(int newWidth, int newHeight) {
		Block[][] newBlockGrid = new Block[newWidth][newHeight];
		for (int y = 0; y < newHeight; y++) {
			for (int x = 0; x < newWidth; x++) {
				if (x >= width || y >= height) {  // if the old grid does not contain the cell, create a new cell
					newBlockGrid[x][y] = new Block(x,y,0);
				} else { // if the old grid contains the cell, copy the cell to the new grid
					newBlockGrid[x][y] = blockGrid[x][y];
				}
			}			
		}
		this.blockGrid = newBlockGrid;
		this.width = newWidth;
		this.height = newHeight;
	}
	
	public boolean isVisibleLevel(int level) { //if the level should be visible
		boolean isVisible;
		if (level == 0) {
    		isVisible = false;
    	} else {
    		isVisible = true;
    	}
		return isVisible;
	}
	
	public void allBlocksSetZ(int level) {
		boolean isVisible = isVisibleLevel(level);
		
		for (int y = 0; y < this.getHeight(); y++) {
            for (int x = 0; x < this.getWidth(); x++) {
            	blockGrid[x][y].setZ(level);
            }
    	}
	}
	
	public Set<Block> getSelectedBlocks(Set<CellUI> selectedCells) { //gets the blocks associated with the selected cells
		Set<Block> selectedBlocks = new HashSet<Block>();
		for (CellUI cell : selectedCells) {
			selectedBlocks.add(cell.getBlock());
		}
		
		return selectedBlocks;
	}
	
	public Block getBlockRight(Block block) {
		return this.getBlockAt(block.getX() + 1, block.getY());
	}
	
	public Block getBlockLeft(Block block) {
		return this.getBlockAt(block.getX() - 1, block.getY());
	}
	
	public Block getBlockAbove(Block block) {
		return this.getBlockAt(block.getX(), block.getY() - 1);
	}
	
	public Block getBlockBelow(Block block) {
		return this.getBlockAt(block.getX(), block.getY() + 1);
	}
	
    public boolean isEdgeBlock(Block block) {
    	return (block.getX() == this.width - 1 || block.getX() == 0 || block.getY() == this.height - 1 || block.getY() == 0);
    }
    
    public boolean isCornerBlock(Block block) {
    	return ((block.getX() == 0 && block.getY() == 0) || (block.getX() == 0 && block.getY() == this.height - 1) || (block.getX() == this.width - 1 && block.getY() == 0) || (block.getX() == this.width - 1 && block.getY() == this.height - 1));
    }
    
    public ArrayList<Block> getEdgeBlocks() {
    	ArrayList<Block> edgeBlocks = new ArrayList<>();
    	
    	int edgeBlockCount = 0;
    	
    	for (int y = 0; y < this.getHeight(); y++) { //may be a better way to go through the blocks
            for (int x = 0; x < this.getWidth(); x++) {
            	if (isEdgeBlock(blockGrid[x][y])) {
            		edgeBlocks.add(blockGrid[x][y]); //adds all edge blocks to an array
            		edgeBlockCount = edgeBlockCount + 1;
            	}
            }
    	}
    	return edgeBlocks;
    }
    
    public Block getRandomEdgeBlock(ArrayList<Block> edgeBlocks) {
    	Random rand = new Random();
    	Block block = edgeBlocks.get(rand.nextInt(edgeBlocks.size())); //randomly chooses an edge block for the start of the maze
    	
    	while (this.isCornerBlock(block)) { //the starting block should not be on a corner
    		block = edgeBlocks.get(rand.nextInt(edgeBlocks.size()));
    	}
    	
    	edgeBlocks.clear();
    	
    	return block;
    }

	public Grid clone() {
		try {
			Grid clone = (Grid) super.clone();
			clone.blockGrid = new Block[width][height];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					clone.blockGrid[x][y] = this.blockGrid[x][y].clone();
				}			
			} 
			return clone;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void cutAndPaste(Set<Block> selectedBlocks, int changeInXIndex, int changeInYIndex) throws ArrayIndexOutOfBoundsException {
		Grid originalData = this.clone();
    	
		for (Block block : selectedBlocks) {
    		int srcX = block.getX();
    		int srcY = block.getY();
    		blockGrid[srcX][srcY].setZ(0);
    	}

    	for (Block block : selectedBlocks) {
    		int srcX = block.getX();
    		int srcY = block.getY();
    		int destX = srcX + changeInXIndex;
    		int destY = srcY + changeInYIndex;
    		blockGrid[destX][destY].setZ(originalData.blockGrid[srcX][srcY].getZ());
    		}
	}
	
	
	public void printGrid() {
		System.out.println(this.toString());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Grid " + width + "x" +height+"\n");
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				sb.append(" ");
				sb.append(blockGrid[x][y].getZ()) ;
			}
			sb.append("\n");
		}
		return sb.toString();
	}

}
