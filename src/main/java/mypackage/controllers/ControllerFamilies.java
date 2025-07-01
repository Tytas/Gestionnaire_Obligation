package mypackage.controllers;

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
import mypackage.model.Family;
import mypackage.model.DataBaseInteractor.*;
import mypackage.view.add.AddObject.AddFamilyController;
import mypackage.view.edit.EditObject.EditFamilyController;
import mypackage.view.util.ConfirmWindow;



public class ControllerFamilies {

    @FXML
    private Label NameFamily;
    @FXML
    private Label capitalFamily;
    @FXML
    private TableView<Family> tableFamily;
    @FXML
    private TableColumn<Family, String> listFamName;
    @FXML
    private TableColumn<Family, String> listFamId;

    @FXML
    private Button deleteButton;

    private MainApp mainApp;

    private Stage stage;
    
    private ObservableList<Family> listFamily = FXCollections.observableArrayList();

    private ObservableList<Family> listFam = FXCollections.observableArrayList();

    private ArrayList<Integer> ids = new ArrayList<>();

    private Family selectedFamily;

    public ControllerFamilies() {
    }

    @FXML
    private void initialize() {
        // Add some sample data
        ids = FamilyInteractor.GetAllFamiliesId();
        for (Integer id : ids) {
            listFam.add(FamilyInteractor.GetFamily(id));
        }
        if (!listFam.isEmpty()) {
            System.out.println("Families loaded: " + listFam.size());
            tableFamily.setItems(listFam);
        }
        this.listFamName.setCellValueFactory(new PropertyValueFactory<Family, String>("name"));
        this.listFamId.setCellValueFactory(new PropertyValueFactory<Family, String>("id"));

        displayFamily(null);
    
        tableFamily.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> displayFamily(newValue));
    }

    public ObservableList<Family> getFamily() {
        return listFam;
    }

    public ArrayList<Integer> GetIds() {
        return ids;
    }   

    private void displayFamily(Family fam) {
        this.selectedFamily = fam;
        if(fam != null) {
            // Update the person details in the label
            NameFamily.setText(fam.getName());
        } else {
            // Clear the details if no person is selected
            NameFamily.setText("");
        }
    }

    @FXML
    private void deleteFamily(){
        if (selectedFamily != null) {
            if (ConfirmWindow.confirmWindow()) {
                System.out.println("Family deleted: " + selectedFamily.getName());
                selectedFamily.getInvestors().forEach(investorId -> {
                    InvestorInteractor.DeleteInvestor(investorId);
                });
                FamilyInteractor.DeleteFamily(selectedFamily.getId());
                listFam.remove(selectedFamily);
            } else {
                System.out.println("Deletion cancelled.");
                return;
            }
        } else {
            System.out.println("No Family selected to delete.");
        }
    }

    @FXML
    private void addFamily() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mypackage/view/add/AddFamily.fxml"));
            Parent root = loader.load();
            
            // Récupère le contrôleur lié au FXML (instancié automatiquement)
            AddFamilyController addFamilyWindow = loader.getController();

            stage = new Stage();
            stage.setTitle("Ajouter une Family");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // bloque la fenêtre principale
            stage.showAndWait(); // attend que la fenêtre se ferme

            if (addFamilyWindow.getResult()) {
                // Refresh the list of families
                ids = FamilyInteractor.GetAllFamiliesId();
                listFamily.clear();
                for (Integer id : ids) {
                    listFamily.add(FamilyInteractor.GetFamily(id));
                }
            }
            tableFamily.setItems(listFamily);
            selectedFamily = null; // Reset selected family
            displayFamily(null); // Clear displayed family details
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editFamily() {
        if (selectedFamily != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/mypackage/view/edit/EditFamily.fxml"));
                Parent root = loader.load();
                
                // Récupère le contrôleur lié au FXML (instancié automatiquement)
                EditFamilyController editFamilyWindow = loader.getController();
                System.out.println("Editing family: " + selectedFamily.getName());
                editFamilyWindow.initData(selectedFamily);

                stage = new Stage();
                stage.setTitle("Modifier une famille");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL); // bloque la fenêtre principale
                stage.showAndWait(); // attend que la fenêtre se ferme

                if (editFamilyWindow.getResult()) {
                    // Refresh the list of families
                    ids = FamilyInteractor.GetAllFamiliesId();
                    listFamily.clear();
                    for (Integer id : ids) {
                        listFamily.add(FamilyInteractor.GetFamily(id));
                    }
                }
                tableFamily.setItems(listFamily);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No obligation selected to edit.");
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
