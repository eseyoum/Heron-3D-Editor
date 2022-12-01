package heron.gameboardeditor.datamodel;
import java.util.Objects;

public class Block {
    private int x;
    private int y;
    private int z;
    private boolean visible  = false; // If the level is zero, it should not be visible
    
    public Block(int x, int y, int z) {
    	this(x,y,z,false);
    }
    private Block(int x, int y, int z, boolean visible) {
    	this.x = x;
    	this.y = y;
    	this.z = z;
    	this.visible = visible;
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

	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setZ(int z) {
		this.z = z;
	}

	public Block clone() {
		return new Block(x,y,z,visible);
		
	}

}
