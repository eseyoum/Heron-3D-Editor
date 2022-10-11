package heron.gameboardeditor;

import javafx.application.Application; 
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;

import java.util.Random;
import javafx.geometry.Pos;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import heron.gameboardeditor.GridBoard.Cell;
/**
 * JavaFX App
 */
public class MainApp extends Application {

    private static Scene scene;
    private boolean running = false;
    private boolean userTurn = false;
    private GridBoard myBoard;
    private int blocksToPlace = 100;
    

//    private Parent createContent() {
//        BorderPane root = new BorderPane();
//        root.setPrefSize(600, 800);
//        myBoard = new GridBoard(false, event -> {
//            if (running)
//                return;
//
//            Cell cell = (Cell) event.getSource();
//            cell = myBoard.getCell(cell.x, cell.y);
//            //if (cell.wasClicked) -edited this out so that you can click on cells that are already clicked -Corey
//                //return;
//            userTurn = cell.click();
//        });
//
//        VBox vbox = new VBox(50, myBoard);
//        vbox.setAlignment(Pos.CENTER);
//
//        root.setCenter(vbox);
//
//        return root;
//    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("openingScreen"), 640, 480);
        //Scene Scene = new Scene(createContent());
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