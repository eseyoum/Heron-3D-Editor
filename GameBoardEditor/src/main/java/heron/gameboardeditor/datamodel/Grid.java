package heron.gameboardeditor.datamodel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * This class represents the data of the GridBoardUI class
 */
public class Grid implements Cloneable {

	private Block[][] blockGrid;
	private int width;
	private int height;
	
	private ArrayList<Block> edgeBlocks = new ArrayList<Block>(); //stores all blocks on the edge of the gridBoard. Used for generating the maze
	private ArrayList<Block>solutionPathBlocks = new ArrayList<Block>(); //represents the blocks in the solution path of the maze
	private Block failedMovementMazeBlock = new Block (0, 0, 0); //when creating the maze, this represents a block which cannot move in a certain direction
	private int failedDirectionCount; //count of failed directions. If a failedMovementMazeBlock has 3 failed directions, it cannot move
	private int maxAttempts = 1000;
	private int attempts = 0;
	private boolean mazeFailed;
	private ArrayList<Block>mazeBranchBlocks = new ArrayList<Block>();
	
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
    
    public void generateMaze() { //for maze	
    	for (int y = 0; y < this.getHeight(); y++) { //may be a better way to go through the blocks
            for (int x = 0; x < this.getWidth(); x++) {
            	blockGrid[x][y].setZ(2);
            	blockGrid[x][y].setVisible(true);
            }
    	}
    	
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
    	
    	int direction = 0; //1-up, 2-right, 3-down, 4-left
    	block.setZ(1);
    	block.setVisible(true);
    	solutionPathBlocks.add(block);
    	
    	if (block.getX() == 0) { //block is on left edge of grid
    		direction = 2;
    	}
    	if (block.getX() == width - 1) { //block is on right edge of grid
    		direction = 4;
    	}
    	if (block.getY() == height - 1) { //block is on bottom edge of grid
    		direction = 1;
    	}
    	if (block.getY() == 0) { //block is on top of grid
    		direction = 3;
    	}
    	try {
	    	if (direction == 1) { //up
	    		createSolutionPath(blockGrid[block.getX()][block.getY() - 1], direction, block);
	    	} else if (direction == 2) { //right
	    		createSolutionPath(blockGrid[block.getX() + 1][block.getY()], direction, block);
	    	} else if (direction == 3) { //down
	    		createSolutionPath(blockGrid[block.getX()][block.getY() + 1], direction, block);
	    	} else if (direction == 4) { //left
	    		createSolutionPath(blockGrid[block.getX() - 1][block.getY()], direction, block);
	    	}
    	} catch (Exception e){
    		System.out.println("failed");
    	}
    	
    	if (mazeFailed) { //if the solution path was unable to be made, try again
    		mazeFailed = false;
    		
    		//reset
        	edgeBlocks.clear();
        	solutionPathBlocks.clear();
        	failedDirectionCount = 0;
        	failedMovementMazeBlock = new Block(0, 0, 0);
        	attempts = 0;
    		generateMaze();
    		
    		return;
    	}
    	
    	failedDirectionCount = 0;
    	failedMovementMazeBlock = new Block(0, 0, 0);
    	attempts = 0;
    	
    	createMazeBranches(solutionPathBlocks); //for making paths which aren't part of the solution path
    	int numBranches = 5; //the number of times branches should be made off of each other
    	int count = 0;
    	while (count < numBranches) {
    		createMazeBranches(mazeBranchBlocks); //for making paths which aren't part of the solution path
    		mazeBranchBlocks.clear();
    		count = count + 1;
    	}
    	edgeBlocks.clear();
    	
    	solutionPathBlocks.clear();
    }
    
    private void createSolutionPath(Block possiblePathBlock, int direction, Block previousBlock) { //for maze
		attempts = attempts + 1;
		
		if (attempts > maxAttempts) {
			System.out.println("maxAttempted");
			mazeFailed = true;
			return;
		}
		
    	if (isEdgeBlock(possiblePathBlock)) { //once the path reaches the edge, the path is finished
		    	if ((solutionPathBlocks.size() < width / 2 ) || (solutionPathBlocks.size() < height / 2)) {
	    			handleInvalidPath(previousBlock, direction); //the path should not end if the path is not long enough
	    		} else {
	    			possiblePathBlock.setZ(1);
		        	solutionPathBlocks.add(possiblePathBlock);
		        	return;
	    		}
	    		
    	} else {
	    	if (isValidPath(possiblePathBlock, 2, direction)) {
	        	addNewPathBlock(possiblePathBlock);
	        	int newDirection = generateNewDirection(direction);
	        	attemptMovement(possiblePathBlock, newDirection);
	    	} else {
	    		handleInvalidPath(previousBlock, direction); //the possiblePathBlock is not possible. Must go back to previous block and try a new direction
	    	}
    	}
    }
    
    private int generateNewDirection(int lastDirection) {
    	Random rand = new Random();
    	int newDirection = rand.nextInt(4) + 1;
    	while (isOppositeDirection(newDirection, lastDirection)) { //the path should not go backwards
    		newDirection = rand.nextInt(4) + 1;
    	}
    	return newDirection;
    }
    
    private void addNewPathBlock(Block newPathBlock) {
    	newPathBlock.setZ(1);
		newPathBlock.setVisible(true);
    	solutionPathBlocks.add(newPathBlock);
    }
    
    private void attemptMovement(Block block, int newDirection) {
		if (newDirection == 1) { //up
    		createSolutionPath(blockGrid[block.getX()][block.getY() - 1], newDirection, block);
    	} else if (newDirection == 2) { //right
    		createSolutionPath(blockGrid[block.getX() + 1][block.getY()], newDirection, block);
    	} else if (newDirection == 3) { //down
    		createSolutionPath(blockGrid[block.getX()][block.getY() + 1], newDirection, block);
    	} else if (newDirection == 4) { //left
    		createSolutionPath(blockGrid[block.getX() - 1][block.getY()], newDirection, block);
    	}
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
	private void handleInvalidPath(Block block, int failedDirection) {
		if (failedMovementMazeBlock.equals(block)) { //used for deciding if the last block in the path cannot move in any direction
			failedDirectionCount = failedDirectionCount + 1;
		} else {
			failedMovementMazeBlock = block;
			failedDirectionCount = 1;
		}
		
		if (failedDirectionCount == 4) { //failed to move in every direction
			handleUnableToMove(block);
		} else {
			int newDirection = 0;
			if (failedDirection == 4) {
				newDirection = 1;
			} else {
				newDirection = failedDirection + 1;
			}
	    	attemptMovement(block, newDirection);
		}
	}
	
	private void handleUnableToMove(Block failedBlock) { //failedBlock is the last block in the path that can't move in any direciton
		failedMovementMazeBlock = new Block(0, 0, 0); //reset the failedMovementMazeBlock
		
		Block lastBlock = solutionPathBlocks.get(solutionPathBlocks.size() - 2); //the last block in the path (the block before the failedBlock)
		int directionOfFailedBlock = getDirectionOfPathBlock(failedBlock, lastBlock);
		
		solutionPathBlocks.remove(failedBlock); //failedBlock is no longer in the path
		failedBlock.setZ(2); //change 2
		
		handleInvalidPath(lastBlock, directionOfFailedBlock);
	}
	
	private int getDirectionOfPathBlock(Block block, Block previousBlock) { //gets the direction the block moved in
		int direction = 1;
		if (getBlockAbove(block).equals(previousBlock)) { //block moved down //move to a  method
			direction = 3;
		} else if (getBlockRight(block).equals(previousBlock)) { //block moved left
			direction = 4;
		} else if (getBlockBelow(block).equals(previousBlock)) { //block moved up
			direction = 1;
		} else if (getBlockLeft(block).equals(previousBlock)) { //block moved right
			direction = 2;
		}
		return direction;
	}
	
	private void createMazeBranches(ArrayList<Block> blocks) {
		Random rand = new Random();
		//int branchNum = (blocks.size() - 1) * 2; //CAN CHANGE
		int branchNum = (blocks.size() - 1); //CAN CHANGE
		for (int i = 0; i < branchNum; i++) { //creating every branch
			int randomNum = rand.nextInt(blocks.size() - 1);
			Block randomInitialBlock = blocks.get(randomNum);
			while (isEdgeBlock(randomInitialBlock)) { //the starting point can't be an edge block
				randomInitialBlock = blocks.get(rand.nextInt(solutionPathBlocks.size() - 1));
			}
			int initalDirection = 1; ////first try to move up
			Block possiblePathBlock = blockGrid[randomInitialBlock.getX()][randomInitialBlock.getY() - 1]; //first try to move up
//        	mazeBranchBlocks.clear();
			createMazeBranch(possiblePathBlock, initalDirection, randomInitialBlock);
//			int branchCount = 0; //number of branches to make
////			while (branchCount < 1) {
////				createMazeBranches(mazeBranchBlocks);
////				branchCount = branchCount + 1;
////			}
		}
	}
	
	private void createMazeBranch(Block possiblePathBlock, int direction, Block previousBlock) {
		if (isEdgeBlock(possiblePathBlock)) {
			//return; //once the branch reaches the edge, it should end
			handleInvalidMazeBranch(previousBlock, direction);
		} else {
			if (isValidPath(possiblePathBlock, 2, direction)) {
				possiblePathBlock.setZ(1);
				mazeBranchBlocks.add(possiblePathBlock);
				int newDirection = generateNewDirection(direction);
				attemptMazeMovement(possiblePathBlock, newDirection);
			} else {
				handleInvalidMazeBranch(previousBlock, direction);
			}
		}
	}
	
    private void attemptMazeMovement(Block block, int newDirection) { //shouldn't have 2
		if (newDirection == 1) { //up
    		createMazeBranch(blockGrid[block.getX()][block.getY() - 1], newDirection, block);
    	}
		else if (newDirection == 2) { //right
    		createMazeBranch(blockGrid[block.getX() + 1][block.getY()], newDirection, block);
    	} else if (newDirection == 3) { //down
    		createMazeBranch(blockGrid[block.getX()][block.getY() + 1], newDirection, block);
    	} else if (newDirection == 4) { //left
    		createMazeBranch(blockGrid[block.getX() - 1][block.getY()], newDirection, block);
    	}
    }
    
	private void handleInvalidMazeBranch(Block block, int failedDirection) {
		if (failedMovementMazeBlock.equals(block)) { //used for deciding if the last block in the path cannot move in any direction
			failedDirectionCount = failedDirectionCount + 1;
		} else {
			failedMovementMazeBlock = block;
			failedDirectionCount = 1;
		}
		
		if (failedDirectionCount == 4) { //failed to move in every direction
			failedDirectionCount = 0;
			failedMovementMazeBlock = new Block(0, 0, 0);
			return; //if can't move, the branch should end
		} else {
			int newDirection = 0;
			if (failedDirection == 4) {
				newDirection = 1;
			} else {
				newDirection = failedDirection + 1;
			}
	    	attemptMazeMovement(block, newDirection);
		}
	}
	
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

	public void cutAndPaste(Set<Block> selectedBlocks, int changeInXIndex, int changeInYIndex) throws ArrayIndexOutOfBoundsException {
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
    		System.out.println(srcX + " " + srcY + " to " + destX + " " + destY );
    		blockGrid[destX][destY] = originalData.blockGrid[srcX][srcY];
    	}
	}

	public void drag(Set<Block> selectedBlocks, int changeInXIndex, int changeInYIndex) {
		// TODO Auto-generated method stub
		
	}


	

}
