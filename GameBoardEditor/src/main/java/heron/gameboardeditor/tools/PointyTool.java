package heron.gameboardeditor.tools;

import heron.gameboardeditor.CellUI;
import heron.gameboardeditor.GridBoardUI;
import heron.gameboardeditor.UndoRedoHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * Sets a tile to "pointy" which means it has a pyramid top in 3D
 */
public class PointyTool extends Tool {
	private GridBoardUI gridBoard;
	private CellUI cellLastClicked;
	
	public PointyTool(GridBoardUI gridBoard, UndoRedoHandler handler) {
		super(handler);
		this.gridBoard = gridBoard;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		handlePointy(e);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		handlePointy(e);
	}
	
	/**
	 * Handle if the user left or right clicked
	 * @param e - the MouseEvent
	 */
	public void handlePointy(MouseEvent e) {
		CellUI cellClicked = gridBoard.getCell((int) e.getX() / gridBoard.getTileSize(), (int) e.getY() / gridBoard.getTileSize());
		if (e.getButton().equals(MouseButton.SECONDARY)) { //if the user right clicks
			if (cellClicked.getBlock().isPointy()) {
				setNotPointy(e);
			}
			
		} else {
			if (!cellClicked.getBlock().isPointy()) {
				setPointy(e);
			}
			
		}
	}
	
	private void setPointy(MouseEvent e) {
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
