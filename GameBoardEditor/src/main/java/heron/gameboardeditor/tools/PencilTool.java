package heron.gameboardeditor.tools;

import heron.gameboardeditor.CellUI; 
import heron.gameboardeditor.GridBoardUI;
import heron.gameboardeditor.UndoRedoHandler;
import javafx.scene.input.MouseEvent;

public class PencilTool extends Tool {
	private GridBoardUI gridBoard;
	
	public PencilTool(GridBoardUI gridBoard, UndoRedoHandler handler) {
		super(handler);
		this.gridBoard = gridBoard;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		pencil(e);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		pencil(e);
	}
	
	private void pencil(MouseEvent e) {
//		CellUI cellClicked = gridBoard.getCell((int) e.getX() / CellUI.TILE_SIZE, (int) e.getY() / CellUI.TILE_SIZE);
//		int x = (int) e.getX() / CellUI.TILE_SIZE;
//		int y = (int) e.getY() / CellUI.TILE_SIZE;
		int x = (int) (e.getX() / gridBoard.getTileSize());
		int y = (int) (e.getY() / gridBoard.getTileSize());
		if (gridBoard.getGridData().isCoordinateInGrid(x, y)) {
			CellUI cellClicked = gridBoard.getCell(x, y);
			if (cellClicked.getLevel() != gridBoard.getLevel()) {
				cellClicked.setLevel(gridBoard.getLevel());
				undoRedoHandler.saveState();	
			}
		}
	}
}
