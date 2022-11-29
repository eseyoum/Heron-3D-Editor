package heron.gameboardeditor;


import heron.gameboardeditor.tools.Tool;
import javafx.scene.input.MouseEvent;

/**
 * This class uses the State pattern. It passes all the listening responsibilities to the currentTool
 */
public class GridEditor {

	private Tool currentTool;

	public GridEditor(Tool initialTool) {
		this.currentTool = initialTool;
	}

	public void setCurrentTool(Tool newTool) {
		currentTool = newTool;
	}

	public void mousePressed(MouseEvent e) {
		currentTool.mousePressed(e);
	}

	public void mouseReleased(MouseEvent e) {
		currentTool.mouseReleased(e);
	}

	public void mouseDragged(MouseEvent e) {
		currentTool.mouseDragged(e);
	}

}
