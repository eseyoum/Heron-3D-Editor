package heron.gameboardeditor.datamodel;

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
	
	

}
