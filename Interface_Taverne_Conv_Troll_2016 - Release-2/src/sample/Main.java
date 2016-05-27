package sample;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import model.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    static Stage primaryStage;
    private static Scene mainScene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            this.primaryStage=primaryStage;
            FXMLLoader fxmlLoaderMain = new FXMLLoader();
            VBox page = fxmlLoaderMain.load(getClass().getResource("Interface_V2.fxml").openStream());
            mainScene = new Scene(page);
            this.primaryStage.setScene(mainScene);
            this.primaryStage.setTitle("Taverne du Troll penché of the dead V2.1");
            this.primaryStage.show();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
