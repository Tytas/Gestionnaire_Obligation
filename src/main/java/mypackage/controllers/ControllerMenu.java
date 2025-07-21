package mypackage.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import mypackage.MainApp;



public class ControllerMenu {

    private MainApp mainApp;

    @FXML private Button obligationsButton;
    @FXML private Button investorsButton;
    @FXML private Button applicantsButton;
    @FXML private Button groupsButton;
    @FXML private Button familiesButton;
    @FXML private Button homeButton; 
    
    private Button currentActiveButton = null;

    public ControllerMenu() {
    }

    @FXML
    private void initialize() {
        // Associer les event handlers aux boutons directement dans le contrôleur
        obligationsButton.setOnAction(event -> handleMenuObligations());
        investorsButton.setOnAction(event -> handleMenuSouscripteurs());
        applicantsButton.setOnAction(event -> handleMenuEmetteurs());
        groupsButton.setOnAction(event -> handleMenuGroups());
        familiesButton.setOnAction(event -> handleMenuFamilies());
        homeButton.setOnAction(event -> handleMenuHome());

        handleMenuHome(); // Charger la page Home par défaut
    }

    @FXML
    private BorderPane mainPane;

    // Méthodes handle - plus besoin de @FXML car elles sont appelées directement
    private void handleMenuObligations() {
        try {
            // Load root layout from fxml file.
            setActiveButton(obligationsButton);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mypackage/view/Obligations.fxml"));
            mainPane.setCenter(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void handleMenuFamilies() {
        try {
            // Load root layout from fxml file.
            setActiveButton(familiesButton);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mypackage/view/Families.fxml"));
            mainPane.setCenter(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void handleMenuSouscripteurs() {
        try {
            // Load root layout from fxml file.
            setActiveButton(investorsButton);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mypackage/view/Souscripteurs.fxml"));
            mainPane.setCenter(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleMenuEmetteurs() {
        try {
            // Load root layout from fxml file.
            setActiveButton(applicantsButton);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mypackage/view/Emetteur.fxml"));
            mainPane.setCenter(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleMenuGroups() {
        try {
            // Load root layout from fxml file.
            setActiveButton(groupsButton);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mypackage/view/Group.fxml"));
            mainPane.setCenter(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleMenuHome() {
        try {
            // Load root layout from fxml file.
            setActiveButton(homeButton); // Pas de bouton actif pour Home
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mypackage/view/Home.fxml"));
            mainPane.setCenter(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setActiveButton(Button button) {
        // Retirer la classe active du bouton précédent
        if (currentActiveButton != null) {
            currentActiveButton.getStyleClass().remove("active");
        }
        
        // Ajouter la classe active au nouveau bouton
        if (button != null) {
            button.getStyleClass().add("active");
            currentActiveButton = button;
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
