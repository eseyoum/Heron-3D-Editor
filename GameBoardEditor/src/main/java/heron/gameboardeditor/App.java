package heron.gameboardeditor;

import javafx.application.Application; 
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

import heron.gameboardeditor.datamodel.Grid;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage mainWindow;
    private static Grid gridData;// = new Grid(10, 10); //creates the data for the grid
    
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("welcomeScreen"), 900, 600);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
        mainWindow = stage;
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    
    static Scene getScene() throws IOException {
    	return scene;
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    public static void main(String[] args) {
        launch();
    }
    
    public static void useNewGrid(int rows, int columns) {
    	gridData = new Grid(rows,columns);
    }

    public static Grid getGrid() {
    	return gridData;
    }
        
    public static Stage getMainWindow() {
    	return mainWindow;
    }
    
    public static void setGrid(Grid newGrid) {
    	gridData = newGrid;
    }

}