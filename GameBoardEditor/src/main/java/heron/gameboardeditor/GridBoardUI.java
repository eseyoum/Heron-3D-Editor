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
    private boolean fillTool = false;
    public FillTool fillToolButton;
    
    private Rectangle selectionRectangle;
    private double initialSelectX;
    private double initialSelectY;
    private UndoRedoHandler undoRedoHandler;
    
    private Grid gridData;

	private CellUI[][] cellArray;
	private CellUI cell;
    
    private Set<CellUI>selectedCells = new HashSet<CellUI>();
    private Set<CellUI>clickedCells = new HashSet<CellUI>();

    public GridBoardUI(Grid grid) {
        this.gridData = new Grid(100,100);
        this.gridData = grid;
        this.fillToolButton = new FillTool(this, this.gridData, undoRedoHandler);
        cellArray = new CellUI[grid.getWidth()][grid.getHeight()];
        for (int y = 0; y < grid.getHeight(); y++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                cellArray[x][y] = new CellUI(this, gridData.getBlockAt(x, y));
                cellArray[x][y].setOnMousePressed(event -> {
                    CellUI cell = (CellUI) event.getSource();
                    //cell = this.getCell(cell.getBlock().getX(), cell.getBlock().getY());
                    //if (cell.wasClicked) -edited this out so that you can click on cells that are already clicked -Corey
                        //return;
                    cell.click();
                    clickedCells.add(cell);
                });
                //row.getChildren().add(cellArray[x][y]);
                cellArray[x][y].setLayoutX(x * CellUI.TILE_SIZE);
                cellArray[x][y].setLayoutY(y * CellUI.TILE_SIZE);
                this.getChildren().add(cellArray[x][y]);
            }
        }
        
        this.setOnMouseClicked(event -> {
        	System.out.println("test");
        });
        
        selectionRectangleTestMethod();
        level = 1; //level begins with 1
        
    }
    
    public Grid getGridData() {
		return gridData;
	}
   
    public void fillTool() {
    	for (CellUI cell: clickedCells) {
    		cell.fillTool();
    	}
    	fillToolButton.fillToolOff();
    }
    
    private void selectionRectangleTestMethod() {
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

    public CellUI getCell(int x, int y) {
        return cellArray[x][y];
    }
    
    public class State {
    	private Grid grid;
    	
    	public State() {
    		grid = (Grid) GridBoardUI.this.gridData.clone();
    	}
    	
    	public void restore() {
    		GridBoardUI.this.gridData = (Grid) grid.clone();
    	}
    }

	public State createMemento() {
		return new State();
	}

	public void restoreState(State gridBoardState) {
		gridBoardState.restore();
		repaint();
	}

	private void repaint() {
		// TODO Auto-generated method stub
		
	}

}