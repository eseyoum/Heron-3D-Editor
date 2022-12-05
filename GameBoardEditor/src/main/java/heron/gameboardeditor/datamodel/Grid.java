package heron.gameboardeditor.datamodel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * This class represents the data of the GridBoardUI class
 */
public class Grid implements Cloneable {

	private Block[][] blockGrid;
	private int width;
	private int height;
	
	private ArrayList<Block> edgeBlocks = new ArrayList<Block>(); //stores all blocks on the edge of the gridBoard. Used for generating the maze

	private Set<Block>solutionPathBlocks = new HashSet<Block>(); //represents the blocks in the solution path of the maze
	private Grid originalData;

	private Block failedMovementMazeBlock = new Block (0, 0, 0); //when creating the maze, this represents a block which cannot move in a certain direction
	private int failedDirectionCount; //count of failed directions. If a failedMovementMazeBlock has 3 failed directions, it cannot move
	private ArrayList<Block>mazeBranchBlocks = new ArrayList<Block>();
	private Block possibleEndBlock;
	private int numBranches = 4; //the number of times branches should be made off of each other
	private int mazeBorderLevel = 2; //the level of the borders of the maze
	private int mazePathLevel = 1; //the level of the path of the maze
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
    

    public Block getRandomEdgeBlock() {
    	int edgeBlockCount = 0;
    	
    	for (int y = 0; y < this.getHeight(); y++) { //may be a better way to go through the blocks
            for (int x = 0; x < this.getWidth(); x++) {
            	if (isEdgeBlock(blockGrid[x][y])) {
            		edgeBlocks.add(blockGrid[x][y]); //adds all edge blocks to an array
            		edgeBlockCount = edgeBlockCount + 1;
            	}
            }
    	}
    	
    	Random rand = new Random();
    	Block block = edgeBlocks.get(rand.nextInt(edgeBlockCount)); //randomly chooses an edge block for the start of the maze
    	
    	while (this.isCornerBlock(block)) { //the starting block should not be on a corner
    		block = edgeBlocks.get(rand.nextInt(edgeBlockCount));
    	}
    	
    	edgeBlocks.clear();
    	
    	return block;
    }
    
    /**
     * The generate maze methods creates a randomized maze. It works by first setting
     * all of the blocks in the grid to a certain level. The method then starts at the edge
     * of the grid and carves out a path. This method then creates more paths which branch off of that one.
     */
    public void generateMaze() {
    	if (height < 3 || width < 3) {
    		Alert errorAlert = new Alert(AlertType.ERROR); //taken help from James_D on Stack Overflow https://stackoverflow.com/questions/39149242/how-can-i-do-an-error-messages-in-javafx
    		errorAlert.setHeaderText("Error");
    		errorAlert.setContentText("The grid is too small!");
    		errorAlert.showAndWait();
    		return;
    	}
    	
    	allBlocksSetZ(mazeBorderLevel);
    	
    	Block block = getRandomEdgeBlock(); //gets the starting point for the maze
    	block.setZ(mazePathLevel);
    	
    	//direction is an integer which represents the direction the maze path is going in. 1 is up, 2 is right, 3 is down, 4 is left
    	int direction = getInitialDirection(block); //finds the initial direction the path of the maze should go in
    	
    	attemptMazeMovement(block, direction); //creates the first path of the maze
    	
    	createMazeBranches(mazeBranchBlocks); //for building off of the initial path of the maze with more paths
    	int count = 0;
    	while (count < numBranches) {
    		createMazeBranches(mazeBranchBlocks); //for branching off of branches and making more paths
    		mazeBranchBlocks.clear();
    		count = count + 1;
    	}
    	
    	//the possibleEndBlock is a block on the edge of the grid which is next to the end of a random path
    	possibleEndBlock.setZ(mazePathLevel); //places the last block to complete the maze
    }
    
    private int getInitialDirection(Block block) {
    	int direction;
    	
    	if (block.getX() == 0) { //block is on left edge of grid
    		direction = 2;
    	} else if (block.getX() == width - 1) { //block is on right edge of grid
    		direction = 4;
    	} else if (block.getY() == height - 1) { //block is on bottom edge of grid
    		direction = 1;
    	} else { //block is on top of grid
    		direction = 3;
    	}
    	
    	return direction;
    }
    
    private int generateNewDirection(int previousDireciton) {
    	Random rand = new Random();
    	int newDirection = rand.nextInt(4) + 1;
    	while (isOppositeDirection(newDirection, previousDireciton)) { //the path should not go backwards
    		newDirection = rand.nextInt(4) + 1;
    	}
    	return newDirection;
    }
    
    private boolean isOppositeDirection(int direction, int previousDirection) {
    	if ((direction == 1 && previousDirection == 3) || (direction == 3 && previousDirection == 1)) {
    		return true;
    	}
    	if ((direction == 2 && previousDirection == 4) || (direction == 4 && previousDirection == 2)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
	
    /**
     * Randomly branches off of a path to create new paths
     * 
     * @param path- the path of blocks which will have branches
     */
	private void createMazeBranches(ArrayList<Block> path) {
		Random rand = new Random();
		int branchNum = (path.size() - 1); //the number of branches which should be made
		
		for (int i = 0; i < branchNum; i++) { //creating every branch
			Block initialBlock = path.get(rand.nextInt(path.size() - 1)); //finds a random block in the path to start branching off of
			
			while (isEdgeBlock(initialBlock)) { //the starting point of the branch can't be an edge block
				initialBlock = path.get(rand.nextInt(mazeBranchBlocks.size() - 1));
			}
			
    		//int initialDirection = rand.nextInt(4) + 1; //the first direction the path should move in
    		int initialDirection = 1;
			Block possiblePathBlock = blockGrid[initialBlock.getX()][initialBlock.getY() - 1];
			createMazePath(possiblePathBlock, initialDirection, initialBlock);
		}
	}
	
	/**
	 * Creates a path in the maze
	 * 
	 * @param possibleBlock - a block in the grid which may or may not become part of the path
	 * @param direction - the direction from the previousBlock in the path to the possibleBlock
	 * @param previousBlock - the previousBlock in the path
	 */
	private void createMazePath(Block possibleBlock, int direction, Block previousBlock) {
		if (isEdgeBlock(possibleBlock)) {
			possibleEndBlock = possibleBlock;
			handleInvalidMovement(previousBlock, direction);
		} else {
			if (isValidPath(possibleBlock, mazeBorderLevel, direction)) {
				Block newBlock = possibleBlock;
				newBlock.setZ(mazePathLevel);
				mazeBranchBlocks.add(newBlock);
				int newDirection = generateNewDirection(direction);
				attemptMazeMovement(newBlock, newDirection);
			} else {
				handleInvalidMovement(previousBlock, direction);
			}
		}
	}
	
	/**
	 * Attempts to add a new block to a path in the maze
	 * 
	 * @param block - the previous block in the path
	 * @param newDirection - the direction the path will go in
	 */
    private void attemptMazeMovement(Block block, int newDirection) {
		if (newDirection == 1) { //up
    		createMazePath(blockGrid[block.getX()][block.getY() - 1], newDirection, block);
    	}
		else if (newDirection == 2) { //right
    		createMazePath(blockGrid[block.getX() + 1][block.getY()], newDirection, block);
    	} else if (newDirection == 3) { //down
    		createMazePath(blockGrid[block.getX()][block.getY() + 1], newDirection, block);
    	} else if (newDirection == 4) { //left
    		createMazePath(blockGrid[block.getX() - 1][block.getY()], newDirection, block);
    	}
    }
    
    /**
     * Handles if the path is unable to add new block in a certain direction
     * 
     * @param block - the block which failed to move (add a new block to the path) in a certain direction
     * @param failedDirection - the failed direction of the block
     */
	private void handleInvalidMovement(Block block, int failedDirection) {
		if (failedMovementMazeBlock.equals(block)) { //if the block has already failed to move (add a new block) in one direction
			failedDirectionCount = failedDirectionCount + 1;
		} else {
			failedMovementMazeBlock = block;
			failedDirectionCount = 1;
		}
		
		if (failedDirectionCount == 4) { //failed to move in every direction
			failedDirectionCount = 0;
			failedMovementMazeBlock = new Block(0, 0, 0);
			return; //if cannot move, the branch should end
		} else { //try moving in a new direction
			int newDirection;
			if (failedDirection == 4) {
				newDirection = 1;
			} else {
				newDirection = failedDirection + 1;
			}
	    	attemptMazeMovement(block, newDirection);
		}
	}
	
//  private void addNewPathBlock(Block newPathBlock) {
//	newPathBlock.setZ(1);
//	newPathBlock.setVisible(true);
//	solutionPathBlocks.add(newPathBlock);
//}
	
//	private int getDirectionOfPathBlock(Block block, Block previousBlock) { //gets the direction the block moved in
//	int direction = 1;
//	if (getBlockAbove(block).equals(previousBlock)) { //block moved down //move to a  method
//		direction = 3;
//	} else if (getBlockRight(block).equals(previousBlock)) { //block moved left
//		direction = 4;
//	} else if (getBlockBelow(block).equals(previousBlock)) { //block moved up
//		direction = 1;
//	} else if (getBlockLeft(block).equals(previousBlock)) { //block moved right
//		direction = 2;
//	}
//	return direction;
//}
	
    public boolean isThreeAdjacentBlocksSameLevel(Block block, int level) {
    	int count = countBlocksInFourDirections(block, level);
    	return (count == 3); //returns true if there are 3 adjacent blocks with the level
    }
     
    public boolean isValidPath(Block block, int level, int direction) {
    	return (isThreeAdjacentBlocksSameLevel(block, level) && (isValidPathCorners(block, level, direction)));
    }
    
    /**
     * Counts the blocks above, below, right, and left of a certain block if they have the specified level
     * @param block
     * @param level
     * @return
     */
    private int countBlocksInFourDirections(Block block, int level) {
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
    	return count;
    }
    
    private boolean isValidPathCorners(Block block, int level, int direction) {
    	if (direction == 1) { //up
    		if (getBlockAbove(getBlockRight(block)).getZ() == level) { //top right is blank
    	    	if (getBlockAbove(getBlockLeft(block)).getZ() == level) { //top left is blank
    	    		return true;
    	    	}
    		}
    	}
    	
    	if (direction == 2) { //right
    		if ((getBlockAbove(getBlockRight(block)).getZ() == level)) { //top right is blank
    	    	if (getBlockBelow(getBlockRight(block)).getZ() == level) { //bottom right is blank
    	    		return true;
    	    	}
    		}
    	}
    	
    	if (direction == 3) { //down
        	if (getBlockBelow(getBlockRight(block)).getZ() == level) { //bottom right is blank
            	if (getBlockBelow(getBlockLeft(block)).getZ() == level) { //bottom left is blank
            		return true;
            	}
        	}
    	}
    	
    	if (direction == 4) { //left
        	if (getBlockAbove(getBlockLeft(block)).getZ() == level) { //top left is blank
            	if (getBlockBelow(getBlockLeft(block)).getZ() == level) { //bottom left is blank
            		return true;
            	}
        	}
    	}
    	
    	return false;
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

//	public void cutAndPaste(Set<Block> selectedBlocks, int changeInXIndex, int changeInYIndex) throws ArrayIndexOutOfBoundsException {
//    	Grid originalData = this.clone();
//    	System.out.println(selectedBlocks);
//
//    	for (Block block : selectedBlocks) {
//    		int srcX = block.getX();
//    		int srcY = block.getY();
//    		int srcZ = block.getZ();
//    		int destX = srcX + changeInXIndex;
//    		int destY = srcY + changeInYIndex;
//    		blockGrid[destX][destY] = new Block(srcX,srcY,srcZ);
//    		
//    	}
	public void cutAndPaste(Set<Block> selectedBlocks, int changeInXIndex, int changeInYIndex) throws ArrayIndexOutOfBoundsException {
		originalData = this.clone();
    	System.out.println(selectedBlocks);
    	
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
    		System.out.println(srcX + " " + srcY + " to " + destX + " " + destY );
//    		blockGrid[srcX][srcY].moveTo(destX, destY);
    		
    		blockGrid[destX][destY] = originalData.blockGrid[srcX][srcY];
    		blockGrid[destX][destY].setZ(originalData.blockGrid[srcX][srcY].getZ());
    		}
	}
	
	
	public void printGrid() {
		System.out.println("Printing grid " + width + "x" +height);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				System.out.print(" " + blockGrid[x][y].getZ()) ;
			}			
			System.out.println();
		}
	
	}
}
