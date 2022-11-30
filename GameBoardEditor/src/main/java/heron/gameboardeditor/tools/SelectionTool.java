package heron.gameboardeditor.tools;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import heron.gameboardeditor.CellUI;
import heron.gameboardeditor.GridBoardUI;
import heron.gameboardeditor.UndoRedoHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SelectionTool extends Tool {
	private GridBoardUI gridBoard;
	Rectangle selectionRectangle;
	private double initialSelectX;
	private double initialSelectY;
	private Set<CellUI>selectedCells = new HashSet<CellUI>();
	

	private boolean pressedInSelectedCell;
	private Point2D origin;
		
	public SelectionTool(GridBoardUI gridBoard, UndoRedoHandler handler) {
		super(handler);
		this.gridBoard = gridBoard;
		selectionRectangle = new Rectangle();
		selectionRectangle.setStroke(Color.BLACK);
		selectionRectangle.setFill(Color.TRANSPARENT);
		selectionRectangle.getStrokeDashArray().addAll(5.0, 5.0);
		pressedInSelectedCell = false;
		origin = new Point2D(0, 0);
		gridBoard.getChildren().add(selectionRectangle);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		initialSelectX = e.getX();
		initialSelectY = e.getY();
		
		selectionRectangle.setX(initialSelectX);
		selectionRectangle.setY(initialSelectY);
		selectionRectangle.setWidth(0);
		selectionRectangle.setHeight(0);
		selectionRectangle.setVisible(true);
		CellUI cellClicked = getCellContaining((int) e.getX() / CellUI.TILE_SIZE, (int) e.getY() / CellUI.TILE_SIZE);
		pressedInSelectedCell = (cellClicked.isSelected());
		if (!pressedInSelectedCell)
			deselectAll();
		if (pressedInSelectedCell && !cellClicked.isSelected()) 
			deselectAll();
		if (pressedInSelectedCell)
			cellClicked.setSelected(true);
		origin = new Point2D(e.getX(), e.getY());
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if (pressedInSelectedCell) {
			System.out.println("Drag test - cell is pressed or not");
			int x = (int) (((e.getX() / CellUI.TILE_SIZE) % (gridBoard.getWidth() / CellUI.TILE_SIZE)) * CellUI.TILE_SIZE);
			int y = (int) (((e.getY() / CellUI.TILE_SIZE) % (gridBoard.getHeight() / CellUI.TILE_SIZE)) * CellUI.TILE_SIZE);
			moveSelectedCells(x - origin.getX(), y - origin.getY());
			origin = new Point2D(x,y);
		} else {
			selectionRectangle.setX(Math.min(e.getX(), initialSelectX));
			selectionRectangle.setWidth(Math.abs(e.getX() - initialSelectX));
			selectionRectangle.setY(Math.min(e.getY(), initialSelectY));
			selectionRectangle.setHeight(Math.abs(e.getY() - initialSelectY));
	
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		int xStartIndex = (int) selectionRectangle.getX() / CellUI.TILE_SIZE;
		int yStartIndex = (int) selectionRectangle.getY() / CellUI.TILE_SIZE;
		int xEndIndex = (int) (selectionRectangle.getX() + selectionRectangle.getWidth()) / CellUI.TILE_SIZE;
		int yEndIndex = (int) (selectionRectangle.getY() + selectionRectangle.getHeight()) / CellUI.TILE_SIZE;
		for (int xIndex = xStartIndex; xIndex <= xEndIndex; xIndex++) {
			for (int yIndex = yStartIndex; yIndex <= yEndIndex; yIndex++) {
				if (gridBoard.getAllClickedCells().contains(gridBoard.getCell(xIndex, yIndex))) {
					selectedCells.add(gridBoard.getCell(xIndex, yIndex));
					gridBoard.getCell(xIndex, yIndex).select();
				}
			}
		}
		selectionRectangle.setVisible(false);
	}
	
	public void deselectAll() {
    	for (CellUI cell: selectedCells) {
    		cell.deselect();
    		cell.setSelected(false);
    	}
    	selectedCells.clear();
    }
	
	public Set<CellUI> getSelectedCells() {
		return selectedCells;
	}
	
	public CellUI getCellContaining(int x, int y) {
		for (CellUI cell : selectedCells) {
			if (cell.contains(x,y)) {
				return cell;
			}
		}
		return null;
	}
	
	public void moveSelectedCells(double dx, double dy) {
		for (CellUI cell : selectedCells) {
			if (cell.isSelected()) {
				cell = new CellUI(gridBoard, cell.getBlock());
				cell.setLayoutX(cell.getX() + dx);
				cell.setLayoutY(cell.getY() + dy);
			}
		}
	}
	
}
