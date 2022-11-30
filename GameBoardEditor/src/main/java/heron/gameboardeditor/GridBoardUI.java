package heron.gameboardeditor;

import java.util.ArrayList;
import java.util.List;

import heron.gameboardeditor.datamodel.Grid;
import heron.gameboardeditor.tools.DigTool;
import heron.gameboardeditor.tools.EraserTool;
import heron.gameboardeditor.tools.FillTool;
import heron.gameboardeditor.tools.PencilTool;
import heron.gameboardeditor.tools.SelectionTool;
import javafx.scene.layout.AnchorPane;

/**
 * This class represents the grid of cells
 */
public class GridBoardUI extends AnchorPane {

	private Grid gridData;
	private CellUI[][] cellArray;
	
	private int level = 1; //the level of the depth map the user is currently working on. The user starts on level 1
    
    public GridEditor gridEditor; //controls which tool the user is currently using
    
    public final PencilTool pencilTool;
    public final EraserTool eraserTool;
    public final DigTool digTool;
    public final FillTool fillTool;
    public final SelectionTool selectionTool;
    private List<CellUI> allClickedCells = new ArrayList<CellUI>();

    private UndoRedoHandler undoRedoHandler;

    public GridBoardUI(Grid grid) {
        this.gridData = grid;
        cellArray = new CellUI[grid.getWidth()][grid.getHeight()];
        
        for (int y = 0; y < grid.getHeight(); y++) { //creates the grid
            for (int x = 0; x < grid.getWidth(); x++) {
                cellArray[x][y] = new CellUI(this, gridData.getBlockAt(x, y));
                cellArray[x][y].setLayoutX(x * CellUI.TILE_SIZE); //spaces out the tiles based on TILE_SIZE
                cellArray[x][y].setLayoutY(y * CellUI.TILE_SIZE);
                this.getChildren().add(cellArray[x][y]);
            }
        }
        
        this.pencilTool = new PencilTool(this, undoRedoHandler);
        this.eraserTool = new EraserTool(this, undoRedoHandler);
        this.digTool = new DigTool(this, undoRedoHandler);
        this.fillTool = new FillTool(this, this.gridData, undoRedoHandler);
        this.selectionTool = new SelectionTool(this, undoRedoHandler);
        
        this.gridEditor = new GridEditor(pencilTool); //pencilTool is the default tool
        
		this.setOnMousePressed(e -> gridEditor.mousePressed(e));
		this.setOnMouseReleased(e -> gridEditor.mouseReleased(e));
		this.setOnMouseDragged(e -> gridEditor.mouseDragged(e));
    }
    
    public List<CellUI> getAllClickedCells(){
    	return allClickedCells;
    }
    
	public CellUI getCellContaining(int x, int y) {
		for (CellUI cell : allClickedCells) {
			if (cell.contains(x,y)) {
				return cell;
			}
		}
		return null;
	}
	
    public Grid getGridData() { //grid data represents the data of the GridUI
		return gridData;
	}
    
    public CellUI getCell(int x, int y) {
        return cellArray[x][y];
    }
    
    public int getLevel() {
    	return this.level;
    }
    
    public void setLevel(int level) {
    	this.level = level;
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