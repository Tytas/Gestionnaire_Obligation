package mypackage.controllers;

import java.io.File;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mypackage.MainApp;
import mypackage.model.DataBaseInteractor.*;
import mypackage.model.Obligation;
import mypackage.view.add.AddObject.AddObligationController;
import mypackage.view.util.ConfirmWindow;



public class ControllerObligations {

    @FXML
    private Label NameObligation;
    @FXML
    private Label capitalObligation;
    @FXML
    private TableView<Obligation> tableObligations;
    @FXML
    private TableColumn<Obligation, String> listObligName;
    @FXML
    private TableColumn<Obligation, String> listObligId;

    @FXML
    private Button deleteButton;

    private MainApp mainApp;

    private Stage stage;

    private ObservableList<Obligation> listOblig = FXCollections.observableArrayList();

    private ArrayList<Integer> ids = new ArrayList<>();

    private Obligation selectedObligation;

    public ControllerObligations() {
    }

    @FXML
    private void initialize() {
        // Add some sample data
        ids = ObligationInteractor.GetAllObligationsId();
        for (Integer id : ids) {
            listOblig.add(ObligationInteractor.GetObligation(id));
        }
        if (!listOblig.isEmpty()) {
            System.out.println("Obligations loaded: " + listOblig.size());
            tableObligations.setItems(listOblig);
        }
        this.listObligName.setCellValueFactory(new PropertyValueFactory<Obligation, String>("name"));
        this.listObligId.setCellValueFactory(new PropertyValueFactory<Obligation, String>("id"));

        displayObligation(null);
    
        tableObligations.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> displayObligation(newValue));
    }

    public ObservableList<Obligation> getObligations() {
        return listOblig;
    }

    public ArrayList<Integer> GetIds() {
        return ids;
    }   

    private void displayObligation(Obligation oblig) {
        this.selectedObligation = oblig;
        if(oblig != null) {
            // Update the person details in the label
            NameObligation.setText(oblig.getName());
            capitalObligation.setText(String.valueOf(oblig.getCapital()));
        } else {
            // Clear the details if no person is selected
            NameObligation.setText("");
            capitalObligation.setText("");
        }
    }

    @FXML
    private void deleteObligation(){
        if (selectedObligation != null) {
            if (ConfirmWindow.confirmWindow()) {
                System.out.println("Obligation deleted: " + selectedObligation.getName());
                ObligationInteractor.DeleteObligation(selectedObligation.getId());
                listOblig.remove(selectedObligation);
            } else {
                System.out.println("Deletion cancelled.");
                return;
            }
        } else {
            System.out.println("No obligation selected to delete.");
        }
    }

    @FXML
    private void addObligation() {
        try {
            File fxmlFile = new File("src/main/mypackage/view/add/AddObligation.fxml");
            if (!fxmlFile.exists()) {
                System.err.println("FXML file not found: " + fxmlFile.getAbsolutePath());
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
            Parent root = loader.load();
            
            // Récupère le contrôleur lié au FXML (instancié automatiquement)
            AddObligationController addObligationWindow = loader.getController();

            stage = new Stage();
            stage.setTitle("Ajouter une obligation");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // bloque la fenêtre principale
            stage.showAndWait(); // attend que la fenêtre se ferme

            if (addObligationWindow.getResult()) {
                // Refresh the list of obligations
                ids = ObligationInteractor.GetAllObligationsId();
                listOblig.clear();
                for (Integer id : ids) {
                    listOblig.add(ObligationInteractor.GetObligation(id));
                }
            }
            tableObligations.setItems(listOblig);
            selectedObligation = null; // Reset selected obligation
            displayObligation(null); // Clear displayed obligation details
        } catch (Exception e) {
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
