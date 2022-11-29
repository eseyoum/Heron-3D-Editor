package heron.gameboardeditor.datamodel;

import heron.gameboardeditor.GridEditor;

public class Grid {
	private Block[][] blockGrid;
	private int width;
	private int height;
	
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
	
	public Block getBlockAt(int x, int y) {
		return blockGrid[x][y];
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public Grid clone() {
		try {
			Grid clone = (Grid) super.clone();
			clone.blockGrid = new Block[width][height];
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					clone.blockGrid[x][y] = new Block(x,y,0);
				}			
			}
			return clone;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	

}
