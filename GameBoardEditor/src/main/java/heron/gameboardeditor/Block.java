package heron.gameboardeditor;
import java.util.Objects;

import javafx.scene.Parent;

public class Block extends Parent {
    public int type; //length of the block plane
    public boolean vertical = true; //orientation of the block plane
    
    private int x;
    private int y;
    private int z;
    
    private int health;

    public Block(int type) {
        this.type = type;
        health = type;
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

    public void hit() {
        health--;
    }

    public boolean isAlive() {
        return health > 0;
    }

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Block other = (Block) obj;
		return x == other.x && y == other.y && z == other.z;
	}
    
    
}
