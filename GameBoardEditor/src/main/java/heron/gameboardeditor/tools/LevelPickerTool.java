package heron.gameboardeditor.tools;

import heron.gameboardeditor.CellUI; 
import heron.gameboardeditor.GridBoardUI;
import heron.gameboardeditor.UndoRedoHandler;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

public class LevelPickerTool extends Tool {
	private GridBoardUI gridBoard;
	private Slider levelSlider; 
	
	public LevelPickerTool(GridBoardUI gridBoard, UndoRedoHandler handler) {
		super(handler);
		this.gridBoard = gridBoard;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		pickLevel(e);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		pickLevel(e);
	}
	
	public void addSlider(Slider levelSlider) {
		this.levelSlider = levelSlider;
	}
	
	public void pickLevel(MouseEvent e) {
		int x = (int) e.getX() / CellUI.DEFAULT_TILE_SIZE;
		int y = (int) e.getY() / CellUI.DEFAULT_TILE_SIZE;
		if (gridBoard.getGridData().isCoordinateInGrid(x, y)) {
			CellUI cellClicked = gridBoard.getCell(x, y);
			if (cellClicked.getLevel() > 0) {
				gridBoard.setLevel(cellClicked.getLevel());
				levelSlider.setValue(gridBoard.getLevel());
			}
		}
	}
}
