package edu.augustana.HeronTeamProject;

import java.util.ArrayList;
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
    public int ships = 5;

    public GridBoard(boolean user, EventHandler<? super MouseEvent> handler) {
        this.user = user;
        for (int y = 0; y < 10; y++) {
            HBox row = new HBox();
            for (int x = 0; x < 10; x++) {
                Cell c = new Cell(x, y, this);
                c.setOnMouseClicked(handler);
                row.getChildren().add(c);
            }

            rows.getChildren().add(row);
        }

        getChildren().add(rows);
    }

    public boolean placeBlock(Block block, int x, int y) {
        if (canPlaceBlock(block, x, y)) {
            int length = block.type;

            Cell cell = getCell(x, length);
            cell.block = block;
            cell.setFill(Color.WHITE);
            cell.setStroke(Color.GREEN);
            return true;
        }

        return false;
    }

    public Cell getCell(int x, int y) {
        return (Cell)((HBox)rows.getChildren().get(y)).getChildren().get(x);
    }

    private Cell[] getNeighbors(int x, int y) {
        Point2D[] points = new Point2D[] {
                new Point2D(x - 1, y),
                new Point2D(x + 1, y),
                new Point2D(x, y - 1),
                new Point2D(x, y + 1)
        };

        List<Cell> neighbors = new ArrayList<Cell>();

        for (Point2D p : points) {
            if (isValidPoint(p)) {
                neighbors.add(getCell((int)p.getX(), (int)p.getY()));
            }
        }

        return neighbors.toArray(new Cell[0]);
    }

    private boolean canPlaceBlock(Block block, int x, int y) {
        int length = block.type;
        
        for (int i = y; i < y + length; i++) {
            if (!isValidPoint(x, i))
                return false;

            Cell cell = getCell(x, i);
            if (cell.block != null)
                return false;

            for (Cell neighbor : getNeighbors(x, i)) {
                if (!isValidPoint(x, i))
                    return false;

                if (neighbor.block != null)
                    return false;
            }
        }
        
        return true;
    }

    private boolean isValidPoint(Point2D point) {
        return isValidPoint(point.getX(), point.getY());
    }

    private boolean isValidPoint(double x, double y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }

    public class Cell extends Rectangle {
        public int x, y;
        public Block block = null;
        public boolean wasClicked = false;

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
            setFill(Color.GRAY);

            if (block != null) {
            	block.hit();
                setFill(Color.GRAY);
            }

            return false;
        }
    }
}