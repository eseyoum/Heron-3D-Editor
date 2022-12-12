package heron.gameboardeditor.tools;

import java.util.ArrayList;
import java.util.List;

import heron.gameboardeditor.CellUI;
import heron.gameboardeditor.GridBoardUI;
import heron.gameboardeditor.UndoRedoHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class PointyTool extends Tool {
	private GridBoardUI gridBoard;
	private CellUI cellLastClicked;
	
	public PointyTool(GridBoardUI gridBoard, UndoRedoHandler handler) {
		super(handler);
		this.gridBoard = gridBoard;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		CellUI cellClicked = gridBoard.getCell((int) e.getX() / gridBoard.getTileSize(), (int) e.getY() / gridBoard.getTileSize());
		
		handlePointy(cellClicked, e);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		
		CellUI cellClicked = gridBoard.getCell((int) e.getX() / gridBoard.getTileSize(), (int) e.getY() / gridBoard.getTileSize());
		
		if (!cellClicked.equals(cellLastClicked)) { 
			handlePointy(cellClicked, e);
		} else { //if the mouse is still on the same cell, nothing should happen
			return;
		}
	}

	public void handlePointy(CellUI cellClicked, MouseEvent e) {
		if (e.getButton().equals(MouseButton.SECONDARY)) { //if the user right clicks
			if (cellClicked.getBlock().isPointy()) {
				setNotPointy(e);
			}
			
		} else {
			if (!cellClicked.getBlock().isPointy()) {
				setPointy(e);
			}
			
		}
		cellLastClicked = cellClicked;
	}
	
	
	private void setPointy(MouseEvent e) {
//		CellUI cellClicked = gridBoard.getCell((int) e.getX() / CellUI.TILE_SIZE, (int) e.getY() / CellUI.TILE_SIZE);
		int x = (int) e.getX() / gridBoard.getTileSize();
		int y = (int) e.getY() / gridBoard.getTileSize();
		if (gridBoard.getGridData().isCoordinateInGrid(x, y)) {
			CellUI cellClicked = gridBoard.getCell(x, y);
			cellClicked.setPointy(true);
			cellClicked.createPointyLine();
			cellClicked.updateVisualPointy();
			undoRedoHandler.saveState();
		}
	}
	
	private void setNotPointy(MouseEvent e) {
//		CellUI cellClicked = gridBoard.getCell((int) e.getX() / CellUI.TILE_SIZE, (int) e.getY() / CellUI.TILE_SIZE);
		int x = (int) e.getX() / gridBoard.getTileSize();
		int y = (int) e.getY() / gridBoard.getTileSize();
		
		if (gridBoard.getGridData().isCoordinateInGrid(x, y)) {
			CellUI cellClicked = gridBoard.getCell(x, y);
			cellClicked.setPointy(false);
			cellClicked.updateVisualPointy();
			undoRedoHandler.saveState();
		}
	}
}
