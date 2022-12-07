package heron.gameboardeditor;


import javafx.scene.image.Image;

import heron.gameboardeditor.datamodel.Block;

import heron.gameboardeditor.datamodel.Grid;
import javafx.scene.Group;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.stage.Stage;

public class Board3DViewController {
	private Grid gridData;
	private static final int WIDTH = 1400;
	private static final int HEIGHT = 800;
	private static final int BLOCK_SIZE = 10;
	private static final int BLOCK_Z_HEIGHT = 10;
	

	private Stage stage3D;

	// Tracks drag starting point for x and y
	private double anchorX, anchorY;
	
	// Keep track of current angle for x and y
	private double anchorAngleX = 0;
	private double anchorAngleY = 0;
	
	// We will update these after drag. Using JavaFX property to bind with object
	private final DoubleProperty angleX = new SimpleDoubleProperty(0);
	private final DoubleProperty angleY = new SimpleDoubleProperty(0);

	public Board3DViewController(Grid gridData) {
		this.gridData = gridData;
		int width = gridData.getWidth();
		int height = gridData.getHeight();
		
		// Create box
//		Box box1 = new Box(10, 10, 50);
//		Box box2 = new Box(10, 10, 20);
//		
//		
//		box2.translateXProperty().set(20);
//
//		box1.translateZProperty().set(25);
//		box2.translateZProperty().set(10);
	
		//Create Material
		PhongMaterial material = new PhongMaterial();
		//Applying JavaFX Diffuse Map with Color Royal Blue[/caption] Royal blue color
		material.setDiffuseColor(Color.GREEN);
		//Apply material for the box
		
		
		// Prepare transformable Group container
		SmartGroup group = new SmartGroup();
//		group.getChildren().add(box1);
//		group.getChildren().add(box2);
		
		int level = 0;
		int maxLevel = gridData.getMaxLevel();
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Block block = gridData.getBlockAt(x, y);
				level = block.getZ();
				
				Box box = new Box( BLOCK_SIZE, BLOCK_SIZE, level*BLOCK_Z_HEIGHT);
				

				box.translateXProperty().set( BLOCK_SIZE*(x - width/2));
				box.translateYProperty().set( BLOCK_SIZE*(y - height/2));
				
				box.translateZProperty().set(-(level*BLOCK_Z_HEIGHT)/2 + (maxLevel*BLOCK_Z_HEIGHT)/2);
				
				group.getChildren().add(box);
				box.setMaterial(material);
			}
		}
		
	
		Camera camera = new PerspectiveCamera();
		camera.translateZProperty().set(-500);

		Scene scene = new Scene(group, WIDTH, HEIGHT,true);
		scene.setFill(Color.SILVER);
		scene.setCamera(camera);
		// Movoe to center of the screen
		group.translateXProperty().set(WIDTH / 2);
		group.translateYProperty().set(HEIGHT / 2);
		group.translateZProperty().set(-1200);

		initMouseControl(group, scene);

		stage3D = new Stage();
		stage3D.setTitle("Genuine Coder");
		stage3D.setScene(scene);

		// Add keyboard control.
		stage3D.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			switch (event.getCode()) {
			case W:
				group.translateZProperty().set(group.getTranslateZ() + 100);
				break;
			case S:
				group.translateZProperty().set(group.getTranslateZ() - 100);
				break;
			case Q:
				group.rotateByX(10);
				break;
			case E:
				group.rotateByX(-10);
				break;
			case NUMPAD6:
				group.rotateByY(10);
				break;
			case NUMPAD4:
				group.rotateByY(-10);
				break;
			}
		});

	}

	public void show() {
		stage3D.show();
	}

	class SmartGroup extends Group {

		Rotate r;
		Transform t = new Rotate();

		void rotateByX(int ang) {
			r = new Rotate(ang, Rotate.X_AXIS);
			t = t.createConcatenation(r);
			this.getTransforms().clear();
			this.getTransforms().addAll(t);
		}

		void rotateByY(int ang) {
			r = new Rotate(ang, Rotate.Y_AXIS);
			t = t.createConcatenation(r);
			this.getTransforms().clear();
			this.getTransforms().addAll(t);
		}
	}

	private void initMouseControl(SmartGroup group, Scene scene) {
		// Prepare X and Y axis rotation transformation objects
		Rotate xRotate;
		Rotate yRotate;
		
		//Add both transformation to the container
		group.getTransforms().addAll(xRotate = new Rotate(0, Rotate.X_AXIS), yRotate = new Rotate(0, Rotate.Y_AXIS));
		xRotate.angleProperty().bind(angleX);
		yRotate.angleProperty().bind(angleY);
		
		//Listen for mouse press -- Drag start with a click
		scene.setOnMousePressed(event -> {
			//Save start points
			anchorX = event.getSceneX();
			anchorY = event.getSceneY();
			
			//Save current rotation angle
			anchorAngleX = angleX.get();
			anchorAngleY = angleY.get();
		});
		
		//Listen for drag
		scene.setOnMouseDragged(event -> {
			angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
			angleY.set(anchorAngleY + anchorX - event.getSceneX());
		});
	}

}
