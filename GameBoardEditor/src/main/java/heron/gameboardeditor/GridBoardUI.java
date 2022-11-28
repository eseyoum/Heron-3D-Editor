package heron.gameboardeditor;

import heron.gameboardeditor.datamodel.Grid;
import heron.gameboardeditor.tools.FillTool;
import heron.gameboardeditor.tools.PencilTool;
import heron.gameboardeditor.tools.SelectionTool;
import javafx.scene.layout.AnchorPane;

public class GridBoardUI extends AnchorPane {
    private int level; //the level of the depth map the user is currently working on
    private boolean eraserOn; //shows if the eraser tool is selected
    public GridEditor gridEditor;
    public final PencilTool pencilTool;
    public final FillTool fillTool;
    public final SelectionTool selectionTool;

    private UndoRedoHandler undoRedoHandler;
    
    private Grid gridData;

	private CellUI[][] cellArray;
	private CellUI cell;

    public GridBoardUI(Grid grid) {
        this.gridData = new Grid(100,100);
        this.gridData = grid;
        this.fillTool = new FillTool(this, this.gridData, undoRedoHandler);
        this.pencilTool = new PencilTool(this, undoRedoHandler);
        cellArray = new CellUI[grid.getWidth()][grid.getHeight()];
        for (int y = 0; y < grid.getHeight(); y++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                cellArray[x][y] = new CellUI(this, gridData.getBlockAt(x, y));
                cellArray[x][y].setLayoutX(x * CellUI.TILE_SIZE); //spaces out the tiles based on TILE_SIZE
                cellArray[x][y].setLayoutY(y * CellUI.TILE_SIZE);
                this.getChildren().add(cellArray[x][y]);
            }
        }
        this.selectionTool = new SelectionTool(this, undoRedoHandler);
        level = 1; //level begins with 1
        
        this.gridEditor = new GridEditor(selectionTool);
		this.setOnMousePressed(e -> gridEditor.mousePressed(e));
		this.setOnMouseReleased(e -> gridEditor.mouseReleased(e));
		this.setOnMouseDragged(e -> gridEditor.mouseDragged(e));
        
    }
    
    public Grid getGridData() {
		return gridData;
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
	}

}