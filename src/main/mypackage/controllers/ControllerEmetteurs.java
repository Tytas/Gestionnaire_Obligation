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
import mypackage.model.Applicant;
import mypackage.model.DataBaseInteractor.ApplicantInteractor;
import mypackage.view.add.AddObject.AddEmetteurController;
import mypackage.view.edit.EditObject.EditEmetteurController;
import mypackage.view.util.ConfirmWindow;



public class ControllerEmetteurs {

    @FXML
    private Label NameApplicant;
    @FXML
    private Label capitalApplicant;
    @FXML
    private TableView<Applicant> tableApplicants;
    @FXML
    private TableColumn<Applicant, String> listApplicantName;
    @FXML
    private TableColumn<Applicant, String> listApplicantId;

    @FXML
    private Button deleteButton;

    private MainApp mainApp;

    private Stage stage;

    private ObservableList<Applicant> listApplicant = FXCollections.observableArrayList();

    private ArrayList<Integer> ids = new ArrayList<>();

    private Applicant selectedApplicant;

    public ControllerEmetteurs() {
    }

    @FXML
    private void initialize() {
        // Add some sample data
        ids = ApplicantInteractor.GetAllApplicantsId();
        for (Integer id : ids) {
            listApplicant.add(ApplicantInteractor.GetApplicant(id));
        }
        if (!listApplicant.isEmpty()) {
            System.out.println("Emetteurs loaded: " + listApplicant.size());
            tableApplicants.setItems(listApplicant);
        }
        this.listApplicantName.setCellValueFactory(new PropertyValueFactory<Applicant, String>("name"));
        this.listApplicantId.setCellValueFactory(new PropertyValueFactory<Applicant, String>("id"));

        displayApplicant(null);
    
        tableApplicants.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> displayApplicant(newValue));
    }

    public ObservableList<Applicant> getApplicants() {
        return listApplicant;
    }

    public ArrayList<Integer> GetIds() {
        return ids;
    }   

    private void displayApplicant(Applicant Applicant) {
        this.selectedApplicant = Applicant;
        if(Applicant != null) {
            // Update the person details in the label
            NameApplicant.setText(Applicant.getName());
        } else {
            // Clear the details if no person is selected
            NameApplicant.setText("");
        }
    }

    @FXML
    private void addApplicant() {
        try {
            File fxmlFile = new File("src/main/mypackage/view/add/AddEmetteur.fxml");
            if (!fxmlFile.exists()) {
                System.err.println("FXML file not found: " + fxmlFile.getAbsolutePath());
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
            Parent root = loader.load();
            
            // Récupère le contrôleur lié au FXML (instancié automatiquement)
            AddEmetteurController addEmetteurWindow = loader.getController();

            stage = new Stage();
            stage.setTitle("Ajouter un émetteur");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // bloque la fenêtre principale
            stage.showAndWait(); // attend que la fenêtre se ferme

            if (addEmetteurWindow.getResult()) {
                // Refresh the list of emetteurs
                ids = ApplicantInteractor.GetAllApplicantsId();
                listApplicant.clear();
                for (Integer id : ids) {
                    listApplicant.add(ApplicantInteractor.GetApplicant(id));
                }
            }
            tableApplicants.setItems(listApplicant);
            selectedApplicant = null; // Reset selected applicant
            displayApplicant(null); // Clear displayed applicant details
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editApplicant() {
        if (selectedApplicant != null) {
            try {
                File fxmlFile = new File("src/main/mypackage/view/edit/EditEmetteur.fxml");
                if (!fxmlFile.exists()) {
                    System.err.println("FXML file not found: " + fxmlFile.getAbsolutePath());
                    return;
                }
                FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
                Parent root = loader.load();
                
                // Récupère le contrôleur lié au FXML (instancié automatiquement)
                EditEmetteurController editEmetteurController = loader.getController();
                System.out.println("Editing emetteur: " + selectedApplicant.getName());
                editEmetteurController.initData(selectedApplicant);

                stage = new Stage();
                stage.setTitle("Modifier un émetteur");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL); // bloque la fenêtre principale
                stage.showAndWait(); // attend que la fenêtre se ferme

                if (editEmetteurController.getResult()) {
                    // Refresh the list of emetteurs
                    ids = ApplicantInteractor.GetAllApplicantsId();
                    listApplicant.clear();
                    for (Integer id : ids) {
                        listApplicant.add(ApplicantInteractor.GetApplicant(id));
                    }
                }
                tableApplicants.setItems(listApplicant);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No obligation selected to edit.");
        }
    }

    @FXML
    private void deleteApplicant(){
        if (selectedApplicant != null) {
            if (ConfirmWindow.confirmWindow()) {
                System.out.println("Applicant deleted: " + selectedApplicant.getName());
                ApplicantInteractor.DeleteApplicant(selectedApplicant.getId());
                listApplicant.remove(selectedApplicant);
            } else {
                System.out.println("Deletion cancelled.");
                return;
            }
        } else {
            System.out.println("No Applicant selected to delete.");
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
