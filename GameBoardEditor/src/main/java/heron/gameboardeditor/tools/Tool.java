package heron.gameboardeditor.tools;

import heron.gameboardeditor.UndoRedoHandler;
import javafx.scene.input.MouseEvent;

/**
 * This abstract class is the superclass of all tools
 *
 */
public abstract class Tool {
	private UndoRedoHandler undoRedoHandler;
	
	public Tool(UndoRedoHandler handler) {
		undoRedoHandler = handler;
	}
	
	public void saveState() {
		undoRedoHandler.saveState();
	}
	
	public void mousePressed(MouseEvent e) {
		
	}
	
	public void mouseReleased(MouseEvent e) {
		
	}
	
	public void mouseDragged(MouseEvent e) {
		
	}
	
}
