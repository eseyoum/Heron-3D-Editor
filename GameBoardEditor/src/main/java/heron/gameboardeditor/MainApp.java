package edu.augustana.HeronTeamProject;

import javafx.application.Application; 
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.geom.Rectangle2D;
import java.io.IOException;

import java.util.Random;
import javafx.geometry.Pos;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import edu.augustana.HeronTeamProject.GridBoard.Cell;
/**
 * JavaFX App
 */
public class MainApp extends Application {

    private static Scene scene;
    private boolean running = false;

    private GridBoard myBoard;
    private int blocksToPlace = 5;

    private Parent createContent() {
        BorderPane root = new BorderPane();
        root.setPrefSize(600, 800);

        myBoard = new GridBoard(false, event -> {
            if (running)
                return;

            Cell cell = (Cell) event.getSource();
            if (myBoard.placeBlock(new Block(blocksToPlace, event.getButton() == MouseButton.PRIMARY), cell.x, cell.y)) {
            	if (--blocksToPlace == 0) {
                    startGame();
                }
            }
        });

        VBox vbox = new VBox(50, myBoard);
        vbox.setAlignment(Pos.CENTER);

        root.setCenter(vbox);

        return root;
    }


    private void startGame() {
    	//place default blocks
//    	myBoard.setOnMouseClicked(mouseEvent -> {
//            int x = (int) mouseEvent.getSceneX();
//            int y = (int) mouseEvent.getSceneY();
//            int type = 10;
//            if (myBoard.placeBlock(new Block(type, Math.random() < 0.5), x, y)) {
//                type--;
//            }
//    	});
    	int type = 10;
    	
    	myBoard.placeBlock(new Block(type, x, y)

        running = true;
    }
    @Override
    public void start(Stage stage) throws IOException {
        //scene = new Scene(loadFXML("primary"), 640, 480);
    	Scene scene = new Scene(createContent());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        
        javafx.geometry.Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds(); 
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);  
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 4);
        
  
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
