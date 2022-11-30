package heron.gameboardeditor.datamodel;

import java.util.Set;

/**
 * This class represents the data of the GridBoardUI class
 */
public class Grid implements Cloneable {

	private Block[][] blockGrid;
	private int width;
	private int height;
	
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
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				blockGrid[x][y] = new Block(x,y,0);
			}			
		}
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
		for (int x = 0; x < newWidth; x++) {
			for (int y = 0; y < newHeight; y++) {
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
    
    public boolean isThreeAdjacentBlocksSameLevel(Block block, int level) {
    	int count = 0;
    	if (getBlockRight(block).getZ() == level) {
    		count = count + 1;
    	}
    	if (getBlockLeft(block).getZ() == level) {
    		count = count + 1;
    	}
    	if (getBlockAbove(block).getZ() == level) {
    		count = count + 1;
    	}
    	if (getBlockBelow(block).getZ() == level) {
    		count = count + 1;
    	}
    	
    	return (count == 3); //returns true if there are 3 adjacent blocks with the level
    }
    

	public Grid clone() {
		try {
			Grid clone = (Grid) super.clone();
			clone.blockGrid = new Block[width][height];
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					clone.blockGrid[x][y] = this.blockGrid[x][y].clone();
				}			
			}
			return clone;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void cutAndPaste(Set<Block> selectedBlocks, int changeInXIndex, int changeInYIndex) {
    	Grid originalData = this.clone();
    	System.out.println(selectedBlocks);

    	for (Block block : selectedBlocks) {
    		int srcX = block.getX();
    		int srcY = block.getY();
    		blockGrid[srcX][srcY].setVisible(false);
    	}

    	for (Block block : selectedBlocks) {
    		int srcX = block.getX();
    		int srcY = block.getY();
    		int destX = srcX + changeInXIndex;
    		int destY = srcY + changeInYIndex;
    		System.out.println(srcX + " "+ srcY + " to " + destX + " " + destY );
    		blockGrid[destX][destY] = originalData.blockGrid[srcX][srcY];
    	}
	}
	

}
