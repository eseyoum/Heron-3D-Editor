package heron.gameboardeditor;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import java.net.URL;
import java.util.ResourceBundle;

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

public class NewProjectScreenController {

    private boolean running = false;
    private boolean userTurn = false;
    private GridBoardUI myBoard;
    //private AnchorPane MapDisplay;
    @FXML private AnchorPane mapDisplay;
	
	@FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("welcomeScreen");
    }

    @FXML // fx:id="copyButton"
    private Button copyButton; // Value injected by FXMLLoader

    @FXML // fx:id="deleteButton"
    private Button deleteButton; // Value injected by FXMLLoader

    @FXML // fx:id="editButton"
    private Button editButton; // Value injected by FXMLLoader

    @FXML // fx:id="exitButton"
    private Button exitButton; // Value injected by FXMLLoader

    @FXML // fx:id="exportButton"
    private Button exportButton; // Value injected by FXMLLoader

    @FXML // fx:id="minimizeButton"
    private Button minimizeButton; // Value injected by FXMLLoader

    @FXML // fx:id="x1"
    private Font x1; // Value injected by FXMLLoader

    @FXML // fx:id="x2"
    private Color x2; // Value injected by FXMLLoader

    @FXML // fx:id="x21"
    private Color x21; // Value injected by FXMLLoader

    @FXML // fx:id="x3"
    private Font x3; // Value injected by FXMLLoader

    @FXML // fx:id="x4"
    private Color x4; // Value injected by FXMLLoader

    @FXML
    void exitTheSceen(ActionEvent event) {
    	Platform.exit();
    }
    
    private Boolean quit;
    
    @FXML
    private void initialize() {
    	mapDisplay.getChildren().add(createContent());
    }

    private BorderPane createContent() {
        BorderPane root = new BorderPane();
        root.setPrefSize(600, 800);
        myBoard = new GridBoardUI(false, event -> {
            if (running)
                return;

            CellUI cell = (CellUI) event.getSource();
            cell = myBoard.getCell(cell.getBlock().getX(), cell.getBlock().getY());
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
