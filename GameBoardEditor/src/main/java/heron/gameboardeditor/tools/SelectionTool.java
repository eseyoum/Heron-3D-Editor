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
	private boolean pressedInCell;
	private Point2D origin;
		
	public SelectionTool(GridBoardUI gridBoard, UndoRedoHandler handler) {
		super(handler);
		this.gridBoard = gridBoard;
		selectionRectangle = new Rectangle();
		selectionRectangle.setStroke(Color.BLACK);
		selectionRectangle.setFill(Color.TRANSPARENT);
		selectionRectangle.getStrokeDashArray().addAll(5.0, 5.0);
		pressedInCell = false;
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
		deselectAll();
		
		CellUI clickedCell = getCellContaining((int) e.getX(), (int) e.getY());
		
		System.out.println("press cell test");
		pressedInCell = (clickedCell != null);
		if (!pressedInCell)
			deselectAll();
		if (pressedInCell && !clickedCell.isSelected())
			deselectAll();
		if (pressedInCell)
			clickedCell.setSelected(true);
	
		origin = new Point2D(e.getX(), e.getY());
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if (pressedInCell) {
			initialSelectX = e.getSceneX();
			initialSelectY = e.getSceneY();
			int x = (int) ((initialSelectX / CellUI.TILE_SIZE) % (gridBoard.getWidth() / CellUI.TILE_SIZE) * CellUI.TILE_SIZE);
			int y = (int) ((initialSelectY / CellUI.TILE_SIZE) % (gridBoard.getHeight() / CellUI.TILE_SIZE) * CellUI.TILE_SIZE);
			moveSelectedCells(x - origin.getX(), y - origin.getY());
//			for (CellUI cell: selectedCells) {
//				cell.setLayoutX(x - origin.getX());
//				cell.setLayoutY(y - origin.getY());
//			}
			origin = new Point2D(x,y);
		} 

			selectionRectangle.setX(Math.min(e.getX(), initialSelectX));
			selectionRectangle.setWidth(Math.abs(e.getX() - initialSelectX));
			selectionRectangle.setY(Math.min(e.getY(), initialSelectY));
			selectionRectangle.setHeight(Math.abs(e.getY() - initialSelectY));
		
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
				cell.setX(cell.getX() + dx);
				cell.setY(cell.getY() + dy);
			}
		}
	}
	
}
