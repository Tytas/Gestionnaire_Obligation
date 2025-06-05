package mypackage;

import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Chargement du fichier FXML
        File fxmlFile = new File("src/main/resources/mypackage/view.fxml");
        if (!fxmlFile.exists()) {
            System.err.println("FXML file not found: " + fxmlFile.getAbsolutePath());
            return;
        }   
        FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
        Scene scene = new Scene(loader.load(), 800, 600);


        // Configuration de la fenêtre principale
        primaryStage.setTitle("FXML Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
