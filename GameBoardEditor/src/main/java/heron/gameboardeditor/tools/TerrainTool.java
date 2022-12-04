package heron.gameboardeditor.tools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import heron.gameboardeditor.CellUI;
import heron.gameboardeditor.GridBoardUI;
import heron.gameboardeditor.UndoRedoHandler;
import heron.gameboardeditor.datamodel.Block;
import heron.gameboardeditor.datamodel.Grid;
import javafx.scene.input.MouseEvent;

public class TerrainTool extends Tool {
	private GridBoardUI gridBoard;
	private TerrainObject terrainObject;
	private Grid gridData;
	
	private TerrainObject defaultMountain;
	private TerrainObject defaultVolcano;
	
	private ArrayList<TerrainObject> terrainObjects; //list of available terrain objects
	
	public TerrainTool(GridBoardUI gridBoard, UndoRedoHandler handler) {
		super(handler);
		this.gridBoard = gridBoard;
		this.gridData = gridBoard.getGridData();
		
		defaultMountain = createMountain();
		defaultVolcano = createVolcano();
		
		terrainObjects = new ArrayList<TerrainObject>();
		terrainObjects.add(defaultMountain);
		terrainObjects.add(defaultVolcano);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		CellUI cellClicked = gridBoard.getCell((int) e.getX() / CellUI.TILE_SIZE, (int) e.getY() / CellUI.TILE_SIZE);
		Block initialBlock = cellClicked.getBlock();
		drawTerrainObject(terrainObject, initialBlock);
		gridBoard.updateVisual();
	}
	
	public void setCurrentTerrainObject(String terrainObjectString) {
		for (TerrainObject terrainObject : terrainObjects) {
			if (terrainObject.name.equals(terrainObjectString)) {
				this.terrainObject = terrainObject;
			}
		}
	}
	
	private void drawTerrainObject(TerrainObject terrainObject, Block initialBlock) {
		initialBlock.setZ(terrainObject.initialTerrainData.level);
		initialBlock.setVisible(gridData.isVisibleLevel(terrainObject.initialTerrainData.level));
		for (TerrainData terrainData : terrainObject.terrainList) {
			 int x = initialBlock.getX() + terrainData.distanceX;
			 int y = initialBlock.getY() + terrainData.distanceY;
			 int z = terrainData.level;
			 if (gridData.isCoordinateInGrid(x, y)) {
				 Block block = gridData.getBlockAt(x, y);
				 block.setZ(z);
				 block.setVisible(gridData.isVisibleLevel(z));
			 }
		}
	}
	
	private TerrainObject createMountain() {
		ArrayList<TerrainData> mountainList = new ArrayList<TerrainData>();
		TerrainData initialTerrainData = new TerrainData(0, 0, 5); //top of the mountian
		mountainList = buildMountain();
		
		TerrainObject mountain = new TerrainObject("Mountain", mountainList, initialTerrainData);
		return mountain;
	}
	
	private ArrayList<TerrainData> buildMountain() {
		ArrayList<TerrainData> mountain = new ArrayList<TerrainData>();
		//first part of mountain
		mountain.add(new TerrainData(0, 1, 4));
		mountain.add(new TerrainData(1, 1, 4));
		mountain.add(new TerrainData(1, 0, 4));
		mountain.add(new TerrainData(1, -1, 4));
		mountain.add(new TerrainData(0, -1, 4));
		mountain.add(new TerrainData(-1, -1, 4));
		mountain.add(new TerrainData(-1, 0, 4));
		mountain.add(new TerrainData(-1, 1, 4));
		
		//second part of mountain
		mountain.add(new TerrainData(0, 2, 3));
		mountain.add(new TerrainData(1, 2, 3));
		mountain.add(new TerrainData(2, 2, 3));
		mountain.add(new TerrainData(2, 1, 3));
		mountain.add(new TerrainData(2, 0, 3));
		mountain.add(new TerrainData(2, -1, 3));
		mountain.add(new TerrainData(2, -2, 3));
		mountain.add(new TerrainData(1, -2, 3));
		mountain.add(new TerrainData(0, -2, 3));
		mountain.add(new TerrainData(-1, -2, 3));
		mountain.add(new TerrainData(-2, -2, 3));
		mountain.add(new TerrainData(-2, -1, 3));
		mountain.add(new TerrainData(-2, 0, 3));
		mountain.add(new TerrainData(-2, 1, 3));
		mountain.add(new TerrainData(-2, 2, 3));
		mountain.add(new TerrainData(-1, 2, 3));
		
		return mountain;
	}
	
	private TerrainObject createVolcano() {
		ArrayList<TerrainData> volcanoList = new ArrayList<TerrainData>();
		TerrainData initialTerrainData = new TerrainData(0, 0, 1); //top of the volcano
		volcanoList = buildMountain();
		TerrainObject volcano = new TerrainObject("Volcano", volcanoList, initialTerrainData);
		return volcano;
	}
	
	private class TerrainObject {
		private ArrayList<TerrainData> terrainList;
		private TerrainData initialTerrainData;
		private String name;
		
		private TerrainObject(String name, ArrayList<TerrainData> terrainList, TerrainData initialTerrainData) {
			this.name = name;
			this.terrainList = terrainList;
			this.initialTerrainData = initialTerrainData;
		}
	}
	
	private class TerrainData {
		int distanceX;
		int distanceY;
		int level;
		
		private TerrainData(int distanceX, int distanceY, int level) {
			this.distanceX = distanceX;
			this.distanceY = distanceY;
			this.level = level;
		}
	}
}
