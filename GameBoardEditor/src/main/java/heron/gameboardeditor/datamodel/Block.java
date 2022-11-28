package heron.gameboardeditor.datamodel;
import java.util.Objects;

public class Block {
    public int type; //length of the block plane
    public boolean vertical = true; //orientation of the block plane
    
    private int x;
    private int y;
    private int z;
    private boolean visible  = false;

    public Block(int type) {
        this.type = type;
    }
    
    /**
     * Creates a block with the x, y, and z coordinates. The z coordinate is the level of the grid map the
     * block was placed on.
     * 
     * @param x
     * @param y
     * @param z
     */
    public Block(int x, int y, int z) {
    	this.x = x;
    	this.y = y;
    	this.z = z;
    }

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
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

	public void setZ(int z) {
		this.z = z;
	}
	
//	@Override
//	public int hashCode() {
//		return Objects.hash(x, y, z);
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		Block other = (Block) obj;
//		return x == other.x && y == other.y && z == other.z;
//	}

    
    
}
