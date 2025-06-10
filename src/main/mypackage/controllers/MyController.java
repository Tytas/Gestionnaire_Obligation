package mypackage.controllers;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import mypackage.MainAppTest;
import mypackage.model.DatabaseInteractor;
import mypackage.model.Obligation;
import mypackage.view.util.ConfirmWindow;



public class MyController {

    @FXML
    private Label NameObligation;
    @FXML
    private Label AmountObligation;
    @FXML
    private TableView<Obligation> tableObligations;
    @FXML
    private TableColumn<Obligation, String> listObligName;
    @FXML
    private TableColumn<Obligation, String> listObligId;

    @FXML
    private Button deleteButton;

    private MainAppTest mainApp;

    private ObservableList<Obligation> listOblig = FXCollections.observableArrayList();

    private ArrayList<Integer> ids = new ArrayList<>();

    private Obligation selectedObligation;

    public MyController() {
    }

    @FXML
    private void initialize() {
        // Add some sample data
        ids = DatabaseInteractor.GetAllObligationsId();
        for (Integer id : ids) {
            listOblig.add(DatabaseInteractor.GetObligation(id));
        }
        System.out.println("Obligations loaded: " + listOblig.size());
        tableObligations.setItems(listOblig);
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
            AmountObligation.setText(String.valueOf(oblig.getAmount()));
        } else {
            // Clear the details if no person is selected
            NameObligation.setText("");
            AmountObligation.setText("");
        }
    }

    @FXML
    private void deleteObligation(){
        if (selectedObligation != null) {
            if (ConfirmWindow.confirmWindow()) {
                System.out.println("Obligation deleted: " + selectedObligation.getName());
                DatabaseInteractor.DeleteObligation(selectedObligation.getId());
                listOblig.remove(selectedObligation);
            } else {
                System.out.println("Deletion cancelled.");
                return;
            }
        } else {
            System.out.println("No obligation selected to delete.");
        }
    }

    public void getMainApp() {
        if (mainApp != null) {
            System.out.println("MainApp is set.");
        } else {
            System.out.println("MainApp is not set.");
        }
    }

    public void setMainApp(MainAppTest mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        //personTable.setItems(mainApp.getPersonData());
    }
}
