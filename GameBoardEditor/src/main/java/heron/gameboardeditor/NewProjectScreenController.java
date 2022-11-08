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
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
    private Button levelButton1;

    @FXML
    private Button levelButton2;

    @FXML
    private Button levelButton3;

    @FXML
    private Button levelButton4;

    @FXML
    private Button levelButton5;
    
    private TreeView<String> treeView;
    
    @FXML private StackPane editPanel;
    
    private double mouseX;
    private double mouseY;
    private Rectangle selectionRectangle;
    private BorderPane gridMap;
    
    
    @FXML
    void exitTheSceen(ActionEvent event) {
    	Platform.exit();
    }
    
    private Boolean quit;
    
    @FXML
    private void initialize() {
    	gridMap = createContent();
    	selectionRectangle = selectionRectangle();
    	mapDisplay.getChildren().addAll(gridMap, selectionRectangle);
    	treeView = checkBoxTreeView();
    	editPanel.getChildren().add(checkBoxTreeView());
//    	mapDisplay.getChildren().add(selectionRectangle());
    }

    private Rectangle selectionRectangle() {
    	selectionRectangle = new Rectangle();
    	selectionRectangle.setStroke(Color.BLACK);
    	selectionRectangle.setFill(Color.TRANSPARENT);
    	selectionRectangle.getStrokeDashArray().addAll(5.0, 5.0);
    	
    	
    	gridMap.setOnMousePressed(event -> {
    		mouseX = event.getX();
    		mouseY = event.getY();
    		selectionRectangle.setX(mouseX);
    		selectionRectangle.setY(mouseY);
    		selectionRectangle.setWidth(0);
    		selectionRectangle.setHeight(0);
    	});
    	
    	gridMap.setOnMouseDragged(event -> {
    		selectionRectangle.setX(Math.min(event.getX(), mouseX));
    		selectionRectangle.setWidth(Math.abs(event.getX() - mouseX));
    		selectionRectangle.setY(Math.min(event.getY(), mouseY));
    		selectionRectangle.setHeight(Math.abs(event.getY() - mouseY));
    		
    	});
    	
    	return selectionRectangle;
    }
    
    //Considering whether this checkBoxTreeView should be created in a new class named EditinPanelUI ?
    private TreeView checkBoxTreeView() { 
    	CheckBoxTreeItem<String> rootItem = new CheckBoxTreeItem<>("Level List");
        rootItem.setExpanded(true);                  
      
        final TreeView tree = new TreeView(rootItem);  
        tree.setEditable(true);
        
        tree.setCellFactory(CheckBoxTreeCell.<String>forTreeView());    
        for (int i = 0; i < 5; i++) {
            final CheckBoxTreeItem<String> checkBoxTreeItem = new CheckBoxTreeItem<>("Level " + (i+1));
            rootItem.getChildren().add(checkBoxTreeItem);   
        }
        
        tree.getSelectionModel().selectedItemProperty().isNull();
        
                       
        tree.setRoot(rootItem);
        tree.setShowRoot(true);
        return tree;
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
    
    @FXML
    void changeLevelTo1(ActionEvent event) {
    	myBoard.changeLevel(1);
    }

    @FXML
    void changeLevelTo2(ActionEvent event) {
    	myBoard.changeLevel(2);
    }

    @FXML
    void changeLevelTo3(ActionEvent event) {
    	myBoard.changeLevel(3);
    }

    @FXML
    void changeLevelTo4(ActionEvent event) {
    	myBoard.changeLevel(4);
    }

    @FXML
    void changeLevelTo5(ActionEvent event) {
    	myBoard.changeLevel(5);
    }
    
    @FXML
    void saveProject(ActionEvent event) {
    	
    }
    
}
