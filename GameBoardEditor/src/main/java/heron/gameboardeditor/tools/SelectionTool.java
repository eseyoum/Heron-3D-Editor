package heron.gameboardeditor.tools;

import java.util.HashSet;
import java.util.Set;

import heron.gameboardeditor.CellUI;
import heron.gameboardeditor.GridBoardUI;
import heron.gameboardeditor.UndoRedoHandler;
import heron.gameboardeditor.datamodel.Block;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SelectionTool extends Tool {
	private GridBoardUI gridBoard;
	Rectangle selectionRectangle;
	private double initialSelectX;
	private double initialSelectY;
	private Set<CellUI>selectedRegionOfCells = new HashSet<CellUI>();
	private boolean pressedInSelectedCell;
		
	private CellUI removeSelectedCell;
	
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
			pressedInSelectedCell = (cellClicked.isSelected() == true);
//			if (!pressedInSelectedCell ) //if the user clicks off of the selected cells
//				cellClicked.setSelected(false);
//				selectedRegionOfCells.remove(cellClicked);
//				System.out.println(cellClicked.getX() + " " + cellClicked.getY());
//				deselectAll();

//			if (pressedInSelectedCell && !cellClicked.isSelected()) //if user clicks on the selected cells (the modified ones) but the cell doesn't get selected somehow (red)
////				cellClicked.setSelected(false);
////				selectedRegionOfCells.remove(cellClicked);
//				deselectAll();

//			storedCell = cellClicked;
//			storedCell.setSelected(cellClicked.getStatus());
			
//			if(e.getClickCount() == 2 && pressedInSelectedCell){
//				cellClicked.setSelected(false);
//				selectedRegionOfCells.remove(cellClicked);
//				System.out.println(cellClicked.getBlock().getX() + " " + cellClicked.getBlock().getY());
//			}
			
			
			
			if (pressedInSelectedCell)
				cellClicked.setSelected(true);
				selectedRegionOfCells.add(cellClicked);
				
//				System.out.println(cellClicked.getBlock().getX() + " " + cellClicked.getBlock().getY());
		} catch (IndexOutOfBoundsException ex) {
			//ignore, because user just clicked outside of the grid
		}
		
//		if (e.getButton().equals(MouseButton.SECONDARY)) { //if the user right clicks
//			CellUI emptyCellClicked = gridBoard.getCellAtPixelCoordinates(initialSelectX, initialSelectY);
//			emptyCellClicked.setSelected(true);
//			selectedCells.add(emptyCellClicked);
//		} 
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		CellUI cellClicked = gridBoard.getCellAtPixelCoordinates(initialSelectX, initialSelectY);
//		pressedInSelectedCell = (cellClicked.isSelected());
		if (pressedInSelectedCell) {
//			cellClicked.setSelected(true);
//			selectedRegionOfCells.add(cellClicked);
//		if (cellClicked.equals(removeSelectedCell)) { 
//			remove(e, cellClicked);
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
			int xStartIndex = (int) (initialSelectX/ CellUI.TILE_SIZE); //original x index when the cell initially gets pressed 
			int yStartIndex = (int) (initialSelectY/ CellUI.TILE_SIZE); //original y index when the cell initially gets pressed 
			int xEndIndex = (int) (e.getX() / CellUI.TILE_SIZE);
			int yEndIndex = (int) (e.getY() / CellUI.TILE_SIZE);
			cutAndPaste(xStartIndex, yStartIndex, xEndIndex, yEndIndex); 
			deselectAll();
		} else {
			int xStartIndex = (int) selectionRectangle.getX() / CellUI.TILE_SIZE;
			int yStartIndex = (int) selectionRectangle.getY() / CellUI.TILE_SIZE;
			int xEndIndex = (int) (selectionRectangle.getX() + selectionRectangle.getWidth()) / CellUI.TILE_SIZE;
			int yEndIndex = (int) (selectionRectangle.getY() + selectionRectangle.getHeight()) / CellUI.TILE_SIZE;
			for (int xIndex = xStartIndex; xIndex <= xEndIndex; xIndex++) {
				for (int yIndex = yStartIndex; yIndex <= yEndIndex; yIndex++) {
					if (gridBoard.getCell(xIndex, yIndex).getBlock().isVisible()) {
						selectedRegionOfCells.add(gridBoard.getCell(xIndex, yIndex));
						gridBoard.getCell(xIndex, yIndex).setSelected(true);
					}
				}
			}
			selectionRectangle.setVisible(false);
			undoRedoHandler.saveState();
		}
	}
	
	public void deselectAll() {
		for (CellUI cell: selectedRegionOfCells) {
			cell.setSelected(false);
    	}
    	selectedRegionOfCells.clear();
    }
	
	public Set<CellUI> getSelectedCells() {
		return selectedRegionOfCells;
	}
	
	public void removeSelectedCell(int xIndex, int yIndex) {
		for (CellUI cell: selectedRegionOfCells) {
			if (cell.getBlock().getX() == xIndex && cell.getBlock().getY() == yIndex) {
				cell.setSelected(false);
				selectedRegionOfCells.remove(cell);
			}
		}
	}
	
    public void cutAndPaste(int startXIndex, int startYIndex, int endXIndex, int endYIndex) throws ArrayIndexOutOfBoundsException {
    	Set<Block> selectedBlocks = new HashSet<>();
    	
    	for (CellUI cell: selectedRegionOfCells) {
    		selectedBlocks.add(cell.getBlock());
    	} 
    	int changeInXIndex = endXIndex - startXIndex;
    	int changeInYIndex = endYIndex - startYIndex;
    	gridBoard.getGridData().cutAndPaste(selectedBlocks, changeInXIndex, changeInYIndex);
    	gridBoard.updateVisual();
    }
    
//    public void remove(MouseEvent e, CellUI cellClicked) {
//    	if (e.getButton().equals(MouseButton.SECONDARY)) {
//    		removeSelectedCell(cellClicked.getBlock().getX(), cellClicked.getBlock().getY());
//    	}
//    	removeSelectedCell = cellClicked;
//    }
}
