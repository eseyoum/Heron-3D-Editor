package heron.gameboardeditor;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;

import java.net.URL;
import java.util.ResourceBundle;

import heron.gameboardeditor.GridBoard.Cell;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class OpeningScreenController {

    @FXML private Button copyButton;
    @FXML private Button deleteButton;
    @FXML private Button editButton;
    @FXML private Button exitButton;
    @FXML private Button exportButton;
    @FXML private Button minimizeButton;
    @FXML private Font x1;
    @FXML private Color x2;
    @FXML private Color x21;
    @FXML private Font x3;
    @FXML private Color x4;
    
    private static Scene scene;
    private boolean running = false;
    private boolean userTurn = false;
    private GridBoard myBoard;
    //private AnchorPane MapDisplay;
    private ImageView MapDisplay;
    
    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("welcomeScreen");
    }
    
    @FXML
    private void exitTheSceen() {
    	Platform.exit();
    }
    
    @FXML
    void Open2DMap(MouseEvent event) {
    	MapDisplay.add(createContent());
    }

    private Parent createContent() {
        BorderPane root = new BorderPane();
        root.setPrefSize(600, 800);
        myBoard = new GridBoard(false, event -> {
            if (running)
                return;

            Cell cell = (Cell) event.getSource();
            cell = myBoard.getCell(cell.x, cell.y);
            //if (cell.wasClicked) -edited this out so that you can click on cells that are already clicked -Corey
                //return;
            userTurn = cell.click();
        });

        VBox vbox = new VBox(50, myBoard);
        vbox.setAlignment(Pos.CENTER);

        root.setCenter(vbox);

        return root;
    }
}

