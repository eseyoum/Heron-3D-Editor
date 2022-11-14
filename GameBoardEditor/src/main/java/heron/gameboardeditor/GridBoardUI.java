package heron.gameboardeditor;

import java.util.HashSet;

import heron.gameboardeditor.datamodel.Block;
import heron.gameboardeditor.datamodel.Grid;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GridBoardUI extends Parent {
    private VBox rows = new VBox();
    private boolean user = false;
    public int blocks = 100;
    
    public int level; //the level of the depth map the user is currently working on
    public boolean eraserOn; //shows if the eraser tool is selected
    public boolean fillTool = false;
    public FillTool fillToolButton;
    
    HashSet<Block> blockSet;
    private Rectangle selectionRectangle;
    private double mouseX;
    private double mouseY;
    
    public Grid gridData;
    

    public GridBoardUI(boolean user, EventHandler<? super MouseEvent> handler) {
        this.user = user;
        this.gridData = new Grid(100,100);
        for (int y = 0; y < 100; y++) {
            HBox row = new HBox();
            for (int x = 0; x < 100; x++) {
                CellUI c = new CellUI(this, gridData.getBlockAt(x, y));
                c.setOnMouseClicked(handler);
                row.getChildren().add(c);
            }

            rows.getChildren().add(row);
        }

        getChildren().add(rows);
        
        blockSet = new HashSet<Block>(); //set for every block created on the board
        
        level = 1; //level begins with 1
        this.fillToolButton = new FillTool(this, this.gridData);
    }
    
    /**
     * Changes the current level of the Gridboard
     * 
     * @param level- the level the user wants to work on
     */
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

//    private Rectangle selectionRectangle() {
//    	selectionRectangle = new Rectangle();
//    	selectionRectangle.setStroke(Color.BLACK);
//    	selectionRectangle.setFill(Color.TRANSPARENT);
//    	selectionRectangle.getStrokeDashArray().addAll(5.0, 5.0);
//    	
//    	GridBoardUI pane = new GridBoardUI(user, null);
//    	pane.setOnMousePressed(event -> {
//    		mouseX = event.getX();
//    		mouseY = event.getY();
//    		selectionRectangle.setX(mouseX);
//    		selectionRectangle.setY(mouseY);
//    		selectionRectangle.setWidth(0);
//    		selectionRectangle.setHeight(0);
//    	});
//    	
//    	mapDisplay.setOnMouseDragged(event -> {
//    		selectionRectangle.setX(Math.min(event.getX(), mouseX));
//    		selectionRectangle.setWidth(Math.abs(event.getX() - mouseX));
//    		selectionRectangle.setY(Math.min(event.getY(), mouseY));
//    		selectionRectangle.setHeight(Math.abs(event.getY() - mouseY));
//    		
//    	});
//    	
//    	return selectionRectangle;
//    }
    
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

    public CellUI getCell(int x, int y) {
        return (CellUI)((HBox)rows.getChildren().get(y)).getChildren().get(x);
    }

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

    private boolean isValidPoint(Point2D point) {
        return isValidPoint(point.getX(), point.getY());
    }

    private boolean isValidPoint(double x, double y) {
        return x >= 0 && x < 100 && y >= 0 && y < 100;
    }
}