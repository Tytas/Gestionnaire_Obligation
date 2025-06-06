package mypackage;

import mypackage.controllers.MyController;
import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class MainAppTest extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    public MainAppTest() {
    }

   
    @Override
    public void start(Stage primaryStage) {

        
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Test");

        initRootLayout();

        showPersonOverview();
    }

    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            File fxmlFile = new File("src/main/resources/mypackage/test2.fxml");
            if (!fxmlFile.exists()) {
                System.err.println("FXML file not found: " + fxmlFile.getAbsolutePath());
                return;
            }   
            FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
            rootLayout = (BorderPane) loader.load();
            
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showPersonOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            File fxmlFile = new File("src/main/resources/mypackage/test1.fxml");
            if (!fxmlFile.exists()) {
                System.err.println("FXML file not found: " + fxmlFile.getAbsolutePath());
                return;
            }   
            loader.setLocation(fxmlFile.toURI().toURL());
            AnchorPane personOverview = (AnchorPane) loader.load();
            
            // Set person overview into the center of root layout.
            rootLayout.setCenter(personOverview);

            // Give the controller access to the main app.
            MyController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Stage getPrimaryStage() {
		return primaryStage;
	}
    public static void main(String[] args) {
        launch(args);
    }
}
