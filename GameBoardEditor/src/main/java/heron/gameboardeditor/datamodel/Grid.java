package heron.gameboardeditor.datamodel;

public class Grid {
	
	private Block[][] blockGrid;
	
	public Grid(int width, int height) {
		blockGrid = new Block[width][height];
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < width; y++) {
				blockGrid[x][y] = new Block(x,y,0);
			}			
		}
	}
	
	public Block getBlockAt(int x, int y) {
		return blockGrid[x][y];
	}
	

}
