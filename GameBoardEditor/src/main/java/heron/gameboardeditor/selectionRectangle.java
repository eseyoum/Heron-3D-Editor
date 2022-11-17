package heron.gameboardeditor;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class selectionRectangle extends Rectangle {
	private Rectangle selectionRectangle;
	public selectionRectangle() {
		selectionRectangle = new Rectangle();
		selectionRectangle.setStroke(Color.BLACK);
		selectionRectangle.setFill(Color.TRANSPARENT);
		selectionRectangle.getStrokeDashArray().addAll(5.0, 5.0);
	}
	
	public Rectangle getSelectionRectangle() {
		return selectionRectangle;
	}
	
}
