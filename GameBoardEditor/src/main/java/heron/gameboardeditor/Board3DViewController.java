package heron.gameboardeditor;

import heron.gameboardeditor.datamodel.Block;
import heron.gameboardeditor.datamodel.Grid;

import heron.gameboardeditor.tools.DigTool;
import heron.gameboardeditor.tools.EraserTool;
import heron.gameboardeditor.tools.FillTool;
import heron.gameboardeditor.tools.PencilTool;
import heron.gameboardeditor.tools.SelectionTool;
import javafx.scene.Group;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Board3DViewController {
	private Grid gridData;
	private static final int WIDTH = 1400;
	private static final int HEIGHT = 800;
	  
	private Stage stage3D;
	
	//Tracks drag starting point for x and y
	private double anchorX, anchorY;
	//Keep track of current angle for x and y
	private double anchorAngleX = 0;
	private double anchorAngleY = 0;
	//We will update these after drag. Using JavaFX property to bind with object
	private final DoubleProperty angleX = new SimpleDoubleProperty(0);
	private final DoubleProperty angleY = new SimpleDoubleProperty(0);
	
    public Board3DViewController(Grid gridData) {
        this.gridData = gridData;
        
      //Create box
       Box box = new Box(100, 20, 50);
        
      //Prepare transformable Group container
       SmartGroup group = new SmartGroup();
       group.getChildren().add(box);
     
       Camera camera = new PerspectiveCamera();
       Scene scene = new Scene(group, WIDTH, HEIGHT);
       scene.setFill(Color.SILVER);
       scene.setCamera(camera);
     
       
     //Move to center of the screen
       group.translateXProperty().set(WIDTH / 2);
       group.translateYProperty().set(HEIGHT / 2);
       group.translateZProperty().set(-1200);
       
       initMouseControl(group, scene);
     
       
       stage3D = new Stage();  
       stage3D.setTitle("Genuine Coder");
       stage3D.setScene(scene);
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
    	   Rotate xRotate;
    	   Rotate yRotate;
    	   group.getTransforms().addAll(
    	       xRotate = new Rotate(0, Rotate.X_AXIS),
    	       yRotate = new Rotate(0, Rotate.Y_AXIS)
    	   );
    	   xRotate.angleProperty().bind(angleX);
    	   yRotate.angleProperty().bind(angleY);
    	 
    	   scene.setOnMousePressed(event -> {
    	     anchorX = event.getSceneX();
    	     anchorY = event.getSceneY();
    	     anchorAngleX = angleX.get();
    	     anchorAngleY = angleY.get();
    	   });
    	 
    	   scene.setOnMouseDragged(event -> {
    	     angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
    	     angleY.set(anchorAngleY + anchorX - event.getSceneX());
    	   });
    	 }
	
}

