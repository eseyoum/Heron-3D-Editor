package heron.gameboardeditor.tools;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import heron.gameboardeditor.CellUI;
import heron.gameboardeditor.GridBoardUI;
import heron.gameboardeditor.UndoRedoHandler;
import heron.gameboardeditor.datamodel.Block;
import heron.gameboardeditor.datamodel.Grid;
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
	private Set<Block> selectedBlocks = new HashSet<Block>();

	private boolean pressedInSelectedCell; //is the clicked cell selected
		
	public SelectionTool(GridBoardUI gridBoard, UndoRedoHandler handler) {
		super(handler);
		this.gridBoard = gridBoard;
		selectionRectangle = new Rectangle();
		selectionRectangle.setStroke(Color.BLACK);
		selectionRectangle.setFill(Color.TRANSPARENT);
		selectionRectangle.getStrokeDashArray().addAll(5.0, 5.0);
		pressedInSelectedCell = false;
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
		try {
			CellUI cellClicked = gridBoard.getCellAtPixelCoordinates(initialSelectX, initialSelectY);
			pressedInSelectedCell = (cellClicked.isSelected());
			System.out.println("pressed in selected: " + pressedInSelectedCell);
			if (!pressedInSelectedCell) //if the user clicks off of the selected cells
				deselectAll();
			if (pressedInSelectedCell && !cellClicked.isSelected()) 
				deselectAll();
			if (pressedInSelectedCell)
				cellClicked.setSelected(true);
		} catch (IndexOutOfBoundsException ex) {
			//ignore, because user just clicked outside of the grid
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if (pressedInSelectedCell) { //the user is dragging the selected cells
			int x = (int) (((e.getX() / CellUI.TILE_SIZE) % (gridBoard.getWidth() / CellUI.TILE_SIZE)) * CellUI.TILE_SIZE);
			int y = (int) (((e.getY() / CellUI.TILE_SIZE) % (gridBoard.getHeight() / CellUI.TILE_SIZE)) * CellUI.TILE_SIZE);		
			
			int startDragXIndex = (int) (e.getX());
			int startDragYIndex = (int) (e.getY());
			int endDragXIndex = (int) e.getSceneX() - startDragXIndex;
			int endDragYIndex = (int) e.getSceneY() - startDragYIndex;
//			for (CellUI cell : selectedCells) {
//				cell.setX(endDragXIndex);
//				cell.setY(endDragYIndex);
//			}
//			drag(endDragXIndex, endDragYIndex);
			
		} else {
			selectionRectangle.setX(Math.min(e.getX(), initialSelectX));
			selectionRectangle.setWidth(Math.abs(e.getX() - initialSelectX));
			selectionRectangle.setY(Math.min(e.getY(), initialSelectY));
			selectionRectangle.setHeight(Math.abs(e.getY() - initialSelectY));
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		
		if (pressedInSelectedCell) { //user releases after dragging the selected cells
			int xStartIndex = (int) (initialSelectX/ CellUI.TILE_SIZE);
			int yStartIndex = (int) (initialSelectY/ CellUI.TILE_SIZE);
			int xEndIndex = (int) (e.getX() / CellUI.TILE_SIZE);
			int yEndIndex = (int) (e.getY() / CellUI.TILE_SIZE);
			cutAndPaste(xStartIndex, yStartIndex, xEndIndex, yEndIndex); 
		} else {
			int xStartIndex = (int) selectionRectangle.getX() / CellUI.TILE_SIZE;
			int yStartIndex = (int) selectionRectangle.getY() / CellUI.TILE_SIZE;
			int xEndIndex = (int) (selectionRectangle.getX() + selectionRectangle.getWidth()) / CellUI.TILE_SIZE;
			int yEndIndex = (int) (selectionRectangle.getY() + selectionRectangle.getHeight()) / CellUI.TILE_SIZE;
			for (int xIndex = xStartIndex; xIndex <= xEndIndex; xIndex++) {
				for (int yIndex = yStartIndex; yIndex <= yEndIndex; yIndex++) {
					if (gridBoard.getCell(xIndex, yIndex).getBlock().isVisible()) {
						selectedCells.add(gridBoard.getCell(xIndex, yIndex));
						gridBoard.getCell(xIndex, yIndex).setSelected(true);
					}
				}
			}
			selectionRectangle.setVisible(false);
		}
	}
	
	public void deselectAll() {
    	for (CellUI cell: selectedCells) {
    		cell.setSelected(false);
    	}
    	selectedCells.clear();
    	//selectedBlocks.clear(); //Corey, unsure if this helps
    }
	
	public Set<CellUI> getSelectedCells() {
		return selectedCells;
	}
	
    public void cutAndPaste(int startXIndex, int startYIndex, int endXIndex, int endYIndex) {
//    	Set<Block> selectedBlocks = new HashSet<>();
    	
    	for (CellUI cell: selectedCells) {
    		selectedBlocks.add(cell.getBlock());
    	} 
    	int changeInXIndex = endXIndex - startXIndex;
    	int changeInYIndex = endYIndex - startYIndex;
    	System.out.println("BEFORE:");
    	gridBoard.getGridData().printGrid();
    	gridBoard.getGridData().cutAndPaste(selectedBlocks, changeInXIndex, changeInYIndex);
    	System.out.println("AFTER:");
    	gridBoard.getGridData().printGrid();
    	
    	gridBoard.updateVisual();
    }
    
    public void drag(int eventXIndex, int eventYIndex) {
    	Set<Block> selectedBlocks = new HashSet<>();
    	
    	for (CellUI cell: selectedCells) {
    		selectedBlocks.add(cell.getBlock());
    	} 
    	gridBoard.getGridData().drag(selectedBlocks, eventXIndex, eventYIndex);

    	gridBoard.updateVisual();	
    }
}
