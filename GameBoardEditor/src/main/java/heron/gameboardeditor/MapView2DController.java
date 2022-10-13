package heron.gameboardeditor;

import java.net.URL;
import java.util.ResourceBundle;

import heron.gameboardeditor.GridBoard.Cell;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MapView2DController {
	@FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button copyButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button exportButton;

    @FXML
    private Button minimizeButton;

    @FXML
    private Font x1;

    @FXML
    private Color x2;

    @FXML
    private Color x21;

    @FXML
    private Font x3;

    @FXML
    private Color x4;
    
    private static Scene scene;
    private boolean running = false;
    private boolean userTurn = false;
    private GridBoard myBoard;

    @FXML
    void Open2DMap(MouseEvent event) {
    	
    }

    @FXML
    void exitTheSceen(ActionEvent event) {
    	
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
    
    @FXML
    void initialize() {
    	scene = new Scene(createContent());

    }
}
