package heron.gameboardeditor.tools;

import java.util.ArrayList;
import java.util.List;

import heron.gameboardeditor.CellUI;
import heron.gameboardeditor.GridBoardUI;
import heron.gameboardeditor.UndoRedoHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * Represents the DigTool. If the user left clicks, the cell goes down one level. If the user right clicks, the cell goes up one level
 */
public class DigTool extends Tool {
	private GridBoardUI gridBoard;
	
	private CellUI cellLastClicked;
	
	public DigTool(GridBoardUI gridBoard, UndoRedoHandler handler) {
		super(handler);
		this.gridBoard = gridBoard;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		CellUI cellClicked = gridBoard.getCell((int) e.getX() / gridBoard.getTileSize(), (int) e.getY() / gridBoard.getTileSize());
		handleDig(cellClicked, e);
		undoRedoHandler.saveState();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		CellUI cellClicked = gridBoard.getCell((int) e.getX() / gridBoard.getTileSize(), (int) e.getY() / gridBoard.getTileSize());
		
		if (!cellClicked.equals(cellLastClicked)) { 
			handleDig(cellClicked, e);
		} else { //if the mouse is still on the same cell, nothing should happen
			return;
		}
	}
	
	public void handleDig(CellUI cellClicked, MouseEvent e) {
		if (e.getButton().equals(MouseButton.SECONDARY)) { //if the user right clicks
			build(cellClicked, e);
		} else {
			dig(cellClicked, e);
		}
		cellLastClicked = cellClicked;
	}

	public void dig(CellUI cellClicked, MouseEvent e) {
		if (cellClicked.getBlock().getZ() < 1) {
			return;
		} else {
			cellClicked.getBlock().setZ(cellClicked.getBlock().getZ() - 1);
			cellClicked.updateVisualBasedOnBlock();
		}
	}
	
	public void build(CellUI cellClicked, MouseEvent e) {
		if (cellClicked.getBlock().getZ() >= CellUI.getMaxLevel()) {
			return;
		} else {
			cellClicked.getBlock().setZ(cellClicked.getBlock().getZ() + 1);
			cellClicked.updateVisualBasedOnBlock();
		}
	}
}
