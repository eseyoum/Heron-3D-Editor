package heron.gameboardeditor;

import java.util.HashSet;
import java.util.Set;

import heron.gameboardeditor.datamodel.Block;
import heron.gameboardeditor.datamodel.Grid;
import heron.gameboardeditor.tools.FillTool;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GridBoardUI extends AnchorPane {
    private int level; //the level of the depth map the user is currently working on
    private boolean eraserOn; //shows if the eraser tool is selected
    private boolean fillTool = false; //shows if filltool is selected
    public FillTool fillToolButton;

    private double initialSelectX;
    private double initialSelectY;
    
    private Grid gridData;
    
    private CellUI[][] cellArray;
    
    private Set<CellUI>selectedCells = new HashSet<CellUI>();

    public GridBoardUI(Grid grid) {
        this.gridData = grid;
        cellArray = new CellUI[grid.getWidth()][grid.getHeight()]; //array of CellUIs, which are the tiles
        for (int y = 0; y < grid.getHeight(); y++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                cellArray[x][y] = new CellUI(this, gridData.getBlockAt(x, y)); //creates a CellUI at the x and y position
                cellArray[x][y].setOnMousePressed(event -> { //puts a listener for mouse presses on the CellUI
                	CellUI cell = (CellUI) event.getSource();
                    cell.click(); //calls click() if the CellUI is clicked
                });
                cellArray[x][y].setLayoutX(x * CellUI.TILE_SIZE); //spaces out the tiles based on TILE_SIZE
                cellArray[x][y].setLayoutY(y * CellUI.TILE_SIZE);
                this.getChildren().add(cellArray[x][y]);
            }
        }
        
        selectionRectangle(); //creates the selectionRectangle
        
        level = 1; //level begins with 1
        this.fillToolButton = new FillTool(this, this.gridData);
    }
    
    /**
     * Creates the selectionRectangle. It creates a rectangle you can drag. CellUI objects
     * in the rectangle are selected, which shows an outline
     */
    private void selectionRectangle() {
    	Rectangle selectionRectangle = new Rectangle();
    	selectionRectangle.setStroke(Color.BLACK);
    	selectionRectangle.setFill(Color.TRANSPARENT);
    	selectionRectangle.getStrokeDashArray().addAll(5.0, 5.0);
    	
    	
    	this.setOnMousePressed(event -> {
    		selectionRectangle.setVisible(true);
    		initialSelectX = event.getX();
    		initialSelectY = event.getY();
    		selectionRectangle.setX(initialSelectX);
    		selectionRectangle.setY(initialSelectY);
    		selectionRectangle.setWidth(0);
    		selectionRectangle.setHeight(0);
    		deselectAll();
    	});
    	
    	this.setOnMouseDragged(event -> {
    		selectionRectangle.setX(Math.min(event.getX(), initialSelectX));
    		selectionRectangle.setWidth(Math.abs(event.getX() - initialSelectX));
    		selectionRectangle.setY(Math.min(event.getY(), initialSelectY));
    		selectionRectangle.setHeight(Math.abs(event.getY() - initialSelectY));
    		
    	});
    	
    	this.setOnMouseReleased(event -> {
    		int xStartIndex = (int) selectionRectangle.getX() / CellUI.TILE_SIZE;
    		int yStartIndex = (int) selectionRectangle.getY() / CellUI.TILE_SIZE;
    		int xEndIndex = (int) (selectionRectangle.getX() + selectionRectangle.getWidth()) / CellUI.TILE_SIZE;
    		int yEndIndex = (int) (selectionRectangle.getY() + selectionRectangle.getHeight()) / CellUI.TILE_SIZE;
    		for (int xIndex = xStartIndex; xIndex <= xEndIndex; xIndex++) {
    			for (int yIndex = yStartIndex; yIndex <= yEndIndex; yIndex++) {
    				selectedCells.add(cellArray[xIndex][yIndex]);
    				cellArray[xIndex][yIndex].select();
    			}
    		}
    		selectionRectangle.setVisible(false);
    	});
    	
    	this.getChildren().add(selectionRectangle);
    }
    
    public void deselectAll() {
    	for (CellUI cell: selectedCells) {
    		cell.deselect();
    	}
    	selectedCells.clear();
    }
    
    public CellUI getCell(int x, int y) {
        return cellArray[x][y];
    }
    
    public void changeLevel(int level) {
    	this.level = level;
    }
    
    public int getLevel() {
    	return this.level;
    }
    
    public void eraserOn() {
    	this.eraserOn = true;
    }
    
    public void eraserOff() {
    	this.eraserOn = false;
    }
    
    public boolean getEraser() {
    	return this.eraserOn;
    }
//    public boolean placeBlock(Block block, int x, int y) {
//        if (canPlaceBlock(block, x, y)) {
//            Cell cell = getCell(x, y);
//            cell.block = block;
//            cell.setFill(Color.WHITE);
//            cell.setStroke(Color.GREEN);
//            return true;
//        }
//
//        return false;
//    }
//    
//    private Cell[] getNeighbors(int x, int y) {
//        Point2D[] points = new Point2D[] {
//                new Point2D(x - 1, y),
//                new Point2D(x + 1, y),
//                new Point2D(x, y - 1),
//                new Point2D(x, y + 1)
//        };
//
//        List<Cell> neighbors = new ArrayList<Cell>();
//
//        for (Point2D p : points) {
//            if (isValidPoint(p)) {
//                neighbors.add(getCell((int)p.getX(), (int)p.getY()));
//            }
//        }
//
//        return neighbors.toArray(new Cell[0]);
//    }

//    private boolean canPlaceBlock(Block block, int x, int y) {
//            if (!isValidPoint(x, y))
//                return false;
//
//            Cell cell = getCell(x, y);
//            if (cell.block != null)
//                return false;
//
//            for (Cell neighbor : getNeighbors(x, y)) {
//                if (!isValidPoint(x, y))
//                    return false;
//
//                if (neighbor.block != null)
//                    return false;
//            }
//        
//        
//        return true;
//    }

//    private boolean isValidPoint(Point2D point) {
//        return isValidPoint(point.getX(), point.getY());
//    }

//    private boolean isValidPoint(double x, double y) {
//        return x >= 0 && x < 100 && y >= 0 && y < 100;
//    }
}