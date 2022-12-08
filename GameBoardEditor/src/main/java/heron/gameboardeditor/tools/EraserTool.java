package heron.gameboardeditor.tools;

import java.util.ArrayList;
import java.util.List;

import heron.gameboardeditor.CellUI;
import heron.gameboardeditor.GridBoardUI;
import heron.gameboardeditor.UndoRedoHandler;
import javafx.scene.input.MouseEvent;

public class EraserTool extends Tool {
	private GridBoardUI gridBoard;
	
	public EraserTool(GridBoardUI gridBoard, UndoRedoHandler handler) {
		super(handler);
		this.gridBoard = gridBoard;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		erase(e);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		erase(e);
	}
	
	private void erase(MouseEvent e) {
//		CellUI cellClicked = gridBoard.getCell((int) e.getX() / CellUI.TILE_SIZE, (int) e.getY() / CellUI.TILE_SIZE);
		int x = (int) e.getX() / (int) gridBoard.getTileSize();
		int y = (int) e.getY() / (int) gridBoard.getTileSize();
		if (gridBoard.getGridData().isCoordinateInGrid(x, y)) {
			CellUI cellClicked = gridBoard.getCell(x, y);
			if (cellClicked.getLevel() > 0) {
				cellClicked.setLevel(0);
				cellClicked.setSelected(false);
				gridBoard.selectionTool.getSelectedCells().remove(cellClicked);
				undoRedoHandler.saveState();
			}
		}

	}
}
