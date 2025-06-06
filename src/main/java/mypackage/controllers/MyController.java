package mypackage.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import mypackage.MainAppTest;
import mypackage.model.DatabaseInteractor;
import mypackage.model.Obligation;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class MyController {

    @FXML
    private TableView<String> personTable;
    @FXML
    private TableColumn<String, String> firstNameColumn;
    @FXML
    private TableColumn<String, String> lastNameColumn;

    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label postalCodeLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label birthdayLabel;

    private MainAppTest mainApp;

    private ObservableList<Obligation> listOblig = FXCollections.observableArrayList();

    public MyController() {
    }

    @FXML
    private void initialize() {
        // Add some sample data
        Obligation oblig = DatabaseInteractor.GetObligation(1);
        listOblig.add(oblig);
    }

    public ObservableList<Obligation> getObligations() {
        return listOblig;
    }

    public void displayObligation() {
        this.firstNameLabel.setText(this.getObligations().get(0).getName());
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
