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

//		CellUI cellClicked = gridBoard.getCell((int) e.getX() / CellUI.DEFAULT_TILE_SIZE, (int) e.getY() / CellUI.DEFAULT_TILE_SIZE);
//		cellClicked.setLevel(0);
//		cellClicked.setSelected(false);
//		gridBoard.selectionTool.getSelectedCells().remove(cellClicked);

		int x = (int) e.getX() / CellUI.TILE_SIZE;
		int y = (int) e.getY() / CellUI.TILE_SIZE;
		if (gridBoard.getGridData().isCoordinateInGrid(x, y)) {
			CellUI cellClicked = gridBoard.getCell(x, y);
			cellClicked.setLevel(0);
			cellClicked.setSelected(false);
			gridBoard.selectionTool.getSelectedCells().remove(cellClicked);
		}

	}
	
	@Override
	public void mouseDragged(MouseEvent e) {

//		CellUI cellClicked = gridBoard.getCell((int) e.getX() / CellUI.DEFAULT_TILE_SIZE, (int) e.getY() / CellUI.DEFAULT_TILE_SIZE);
//		cellClicked.setLevel(0);
//		cellClicked.setSelected(false);
//		gridBoard.selectionTool.getSelectedCells().remove(cellClicked);

		int x = (int) e.getX() / CellUI.TILE_SIZE;
		int y = (int) e.getY() / CellUI.TILE_SIZE;
		if (gridBoard.getGridData().isCoordinateInGrid(x, y)) {
			CellUI cellClicked = gridBoard.getCell(x, y);
			cellClicked.setLevel(0);
			cellClicked.setSelected(false);
			gridBoard.selectionTool.getSelectedCells().remove(cellClicked);
		}

	}
}
