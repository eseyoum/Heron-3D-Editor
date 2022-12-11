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
		setPointy(e);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		setPointy(e);
	}
	
	
	
	public void handlePointy(CellUI cellClicked, MouseEvent e) {
		if (e.getButton().equals(MouseButton.SECONDARY)) { //if the user right clicks
			setPointy(e);
		} else {
			setNotPointy(e);
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
			
			undoRedoHandler.saveState();
		}
	}
}