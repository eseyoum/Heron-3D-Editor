package heron.gameboardeditor.datamodel;
import java.util.Objects;

public class Block {
    private int x;
    private int y;
    private int z;
    private boolean visible  = false; //whether or not the CellUI should be visible. If the level is zero, it should not be visible
    
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
}
