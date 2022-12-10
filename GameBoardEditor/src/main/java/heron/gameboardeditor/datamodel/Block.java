package heron.gameboardeditor.datamodel;

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

	public boolean isVisible() {
		return z > 0;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public int getZ() {
		return z;
	}

	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	/**
	 * A z value of zero makes the block "invisible",
	 * otherwise it is visible.
	 * @param z
	 */
	public void setZ(int z) {
		this.z = z;
	}

	
	public void setPointy(boolean pointy) {
		this.isPointy = pointy;
	}
	
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
