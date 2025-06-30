package mypackage.controllers;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import mypackage.MainApp;



public class ControllerMenu {

    private MainApp mainApp;


    public ControllerMenu() {
    }

    @FXML
    private void initialize() {
    }

    @FXML
    private BorderPane mainPane;

    @FXML
    private void handleMenuObligations() {
        try {
            // Load root layout from fxml file.
            File fxmlFile = new File("src/mypackage/view/Obligations.fxml");
            if (!fxmlFile.exists()) {
                System.err.println("FXML file not found: " + fxmlFile.getAbsolutePath());
                return;
            }   
            FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
            mainPane.setCenter(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleMenuFamilies() {
        try {
            // Load root layout from fxml file.
            File fxmlFile = new File("src/mypackage/view/Families.fxml");
            if (!fxmlFile.exists()) {
                System.err.println("FXML file not found: " + fxmlFile.getAbsolutePath());
                return;
            }   
            FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
            mainPane.setCenter(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleMenuSouscripteurs() {
        try {
            // Load root layout from fxml file.
            File fxmlFile = new File("src/mypackage/view/Souscripteurs.fxml");
            if (!fxmlFile.exists()) {
                System.err.println("FXML file not found: " + fxmlFile.getAbsolutePath());
                return;
            }   
            FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
            mainPane.setCenter(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleMenuEmetteurs() {
        try {
            // Load root layout from fxml file.
            File fxmlFile = new File("src/mypackage/view/Emetteur.fxml");
            if (!fxmlFile.exists()) {
                System.err.println("FXML file not found: " + fxmlFile.getAbsolutePath());
                return;
            }   
            FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
            mainPane.setCenter(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMenuHome() {
        try {
            // Load root layout from fxml file.
            File fxmlFile = new File("src/mypackage/view/Home.fxml");
            if (!fxmlFile.exists()) {
                System.err.println("FXML file not found: " + fxmlFile.getAbsolutePath());
                return;
            }   
            FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
            mainPane.setCenter(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void getMainApp() {
        if (mainApp != null) {
            System.out.println("MainApp is set.");
        } else {
            System.out.println("MainApp is not set.");
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
