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
import javafx.application.Application;
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
	
    public Board3DViewController(Grid gridData) {
        this.gridData = gridData;
        
        
        Sphere sphere = new Sphere(50);
        
        Group group = new Group();
        group.getChildren().add(sphere);
     
        Camera camera = new PerspectiveCamera();
        Scene scene = new Scene(group, WIDTH, HEIGHT);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);
     
        sphere.translateXProperty().set(WIDTH / 2);
        sphere.translateYProperty().set(HEIGHT / 2);
        
        stage3D = new Stage();
     
        stage3D.addEventHandler(KeyEvent.KEY_PRESSED, event ->{
          switch (event.getCode()) {
            case W:
              sphere.translateZProperty().set(sphere.getTranslateZ() + 100);
              break;
            case S:
              sphere.translateZProperty().set(sphere.getTranslateZ() - 100);
              break;
          }
        });
     
        stage3D.setTitle("Genuine Coder");
        stage3D.setScene(scene);

    }
    
   
    public void show() {
    	stage3D.show();
    }
    
  
	
}

