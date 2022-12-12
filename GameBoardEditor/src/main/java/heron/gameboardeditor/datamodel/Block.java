package heron.gameboardeditor.datamodel;

import javafx.fxml.FXML;

public class Block {
    private int x;
    private int y;
    private int z;  // If the level is zero, it should not be visible
    private boolean isPointy = false;
    
    
//    public Block(int x, int y, int z) {
//    	this(x,y,z,false);
//    }
    public Block(int x, int y, int z) {
    	this.x = x;
    	this.y = y;
    	this.z = z;
    	
    }
    
    @FXML
    /**
     * This method checks if a block is visible.
     * 
     * @return true if the height is bigger than 0.
     * @return false if the height is less than or equal to 0.
     */
	public boolean isVisible() {
		return z > 0;
	}

    @FXML
    /**
     * This method gets the x coordinate of the block
     * 
     * @return x - the x coordinate of the block
     */
	public int getX() {
		return x;
	}

    @FXML
    /**
     * This method gets the y coordinate of the block
     * 
     * @return y - the y coordinate of the block
     */
	public int getY() {
		return y;
	}
	
    @FXML
    /**
     * This method gets the height/ level of the block
     * 
     * @return z - the height 
     */
	public int getZ() {
		return z;
	}

    @FXML
    /**
     * This method sets the x coordinate of the block
     * 
     * @param x - the value that the x coordinate of the block will be set to
     */
	public void setX(int x) {
		this.x = x;
	}
	
    @FXML
    /**
     * This method sets the y coordinate of the block
     * 
     * @param y - the value that the y coordinate of the block will be set to
     */
	public void setY(int y) {
		this.y = y;
	}
    
	/**
	 * This method sets the height/level of the block.
	 * A z value of zero makes the block "invisible",
	 * otherwise it is visible.
	 * 
	 * @param z - the value that the height of the block will be set to
	 */
	public void setZ(int z) {
		this.z = z;
	}

	
	/**
	 * This method sets the isPointy data field of the block
	 * 
	 * @param pointy - true if the block is pointy and false if the block is not pointy
	 */
	public void setPointy(boolean pointy) {
		this.isPointy = pointy;
	}
	
	/**
	 * This method checks whether the block is pointy
	 * 
	 * @return true if the block is pointy
	 * @return false if the block is not pointy
	 */
	public boolean isPointy() {
		return(this.isPointy);
	}

	
	public Block clone() {
		return new Block(x,y,z);
	}

	public void moveTo(int dx, int dy) {
		Block blockToClone = this.clone();
		blockToClone = new Block(x + dx, y + dy,z);
	}

}
