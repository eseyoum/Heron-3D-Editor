package heron.gameboardeditor;

import java.util.Stack;

public class UndoRedoHandler {
	private Stack<GridBoardUI.State> undoStack, redoStack;
	// invariant: The top state of the undoStack always is a copy of the
	// current state of the canvas.
	private GridBoardUI gridBoard;

	/**
	 * constructor
	 * 
	 * @param canvas the DrawingCanvas whose changes are saved for later
	 *               restoration.
	 */
	public UndoRedoHandler(GridBoardUI gridBoard) {
		undoStack = new Stack<GridBoardUI.State>();
		redoStack = new Stack<GridBoardUI.State>();
		this.gridBoard = gridBoard;
		// store the initial state of the canvas on the undo stack
		undoStack.push(gridBoard.createMemento());
	}

	/**
	 * saves the current state of the drawing canvas for later restoration
	 */
	public void saveState() {
		GridBoardUI.State canvasState = gridBoard.createMemento();
		undoStack.push(canvasState);
		redoStack.clear();
	}

	/**
	 * restores the state of the drawing canvas to what it was before the last
	 * change. Nothing happens if there is no previous state (for example, when the
	 * application first starts up or when you've already undone all actions since
	 * the startup state).
	 */
	public void undo() {
		if (undoStack.size() == 1) // only the current state is on the stack
			return;

		GridBoardUI.State canvasState = undoStack.pop();
		redoStack.push(canvasState);
		gridBoard.restoreState(undoStack.peek());
	}

	/**
	 * restores the state of the drawing canvas to what it was before the last undo
	 * action was performed. If some change was made to the state of the canvas
	 * since the last undo, then this method does nothing.
	 */
	public void redo() {
		if (redoStack.isEmpty())
			return;

		GridBoardUI.State canvasState = redoStack.pop();
		undoStack.push(canvasState);
		gridBoard.restoreState(canvasState);
	}
}
