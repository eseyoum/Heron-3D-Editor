package heron.gameboardeditor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GridBoard extends Parent {
    private VBox rows = new VBox();
    private boolean user = false;
    public int blocks = 100;
    
    HashSet<Block> blockSet;
    

    public GridBoard(boolean user, EventHandler<? super MouseEvent> handler) {
        this.user = user;
        for (int y = 0; y < 30; y++) {
            HBox row = new HBox();
            for (int x = 0; x < 50; x++) {
                Cell c = new Cell(x, y, this);
                c.setOnMouseClicked(handler);
                row.getChildren().add(c);
            }

            rows.getChildren().add(row);
        }

        getChildren().add(rows);
        
        blockSet = new HashSet<Block>(); //set for every block created on the board
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

    public Cell getCell(int x, int y) {
        return (Cell)((HBox)rows.getChildren().get(y)).getChildren().get(x);
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

    public class Cell extends Rectangle {
        public int x, y;
        public Block block = null;
        public boolean wasClicked = false;
        public boolean blockPlaced = false;

        private GridBoard board;

        public Cell(int x, int y, GridBoard board) {
            super(30, 30);
            this.x = x;
            this.y = y;
            this.board = board;
            setFill(Color.LIGHTGRAY);
            setStroke(Color.BLACK);
        }

        public boolean click() {
        	wasClicked = true;
        	
            if (block != null) {
                setFill(Color.LIGHTGRAY);
                if (block == null) {
                	blockSet.remove(block);
                }
                block = null;
            } 
            else {
            	setFill(Color.GRAY);
    
            	block = new Block(x, y, 0); //The 0 is a placeholder. Eventually, a z coordinate will be added instead of 0.
            	blockSet.add(block);
            }
            
            return false;
        }
    }
}