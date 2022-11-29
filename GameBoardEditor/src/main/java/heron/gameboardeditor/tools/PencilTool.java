package heron.gameboardeditor.tools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import heron.gameboardeditor.CellUI;
import heron.gameboardeditor.GridBoardUI;
import heron.gameboardeditor.UndoRedoHandler;
import javafx.scene.input.MouseEvent;

public class PencilTool extends Tool {
	private GridBoardUI gridBoard;
	private List<CellUI>clickedCells = new ArrayList<CellUI>();

	public PencilTool(GridBoardUI gridBoard, UndoRedoHandler handler) {
		super(handler);
		this.gridBoard = gridBoard;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		CellUI cellClicked = gridBoard.getCell((int) e.getX() / CellUI.TILE_SIZE, (int) e.getY() / CellUI.TILE_SIZE);
		cellClicked.setLevel(gridBoard.getLevel());
		clickedCells.add(cellClicked);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		CellUI cellClicked = gridBoard.getCell((int) e.getX() / CellUI.TILE_SIZE, (int) e.getY() / CellUI.TILE_SIZE);
		cellClicked.setLevel(gridBoard.getLevel());
		clickedCells.add(cellClicked);
	}
	
	public List<CellUI> getClickedCells() {
		return clickedCells;
	}
	
	public CellUI getCellContaining(double x, double y) {
		for(CellUI cell : clickedCells) {
			if (cell.contains(x,y)) {
				return cell;
			}
		}
		return null;
	}
}
