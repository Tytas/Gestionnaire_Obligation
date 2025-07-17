package mypackage.controllers;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mypackage.MainApp;
import mypackage.model.Applicant;
import mypackage.model.Group;
import mypackage.model.Obligation;
import mypackage.model.DataBaseInteractor.ApplicantInteractor;
import mypackage.model.DataBaseInteractor.GroupInteractor;
import mypackage.model.DataBaseInteractor.ObligationInteractor;
import mypackage.view.add.AddEmetteurController;
import mypackage.view.edit.EditEmetteurController;
import mypackage.view.util.ConfirmWindow;



public class ControllerEmetteurs {

    @FXML
    private Label NameApplicant;
    @FXML
    private Label GroupApplicant;
    @FXML
    private Label DirigeantApplicant;
    @FXML
    private Label ValeurObligationsApplicant;
    @FXML
    private ListView<Obligation> listViewObligation;
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

    private void displayApplicant(Applicant applicant) {
        this.selectedApplicant = applicant;

        ObservableList<Obligation> obligations = FXCollections.observableArrayList();
        if (applicant != null) {
            for (Integer obligationId : applicant.getObligations()) {
                Obligation obligation = ObligationInteractor.GetObligation(obligationId);
                if (obligation != null) {
                    obligations.add(obligation);
                }
            }
        }
        listViewObligation.getItems().clear();
        listViewObligation.setItems(obligations);
        listViewObligation.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Obligation item, boolean empty) {
                super.updateItem(item, empty);
                Label nameField = new Label();
                HBox content = new HBox(10, nameField);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    nameField.setText(item.getName());
                    setGraphic(content);
                }
            }
        });

        if(applicant != null) {
            // Update the person details in the label
            NameApplicant.setText(applicant.getName());
            Group group = GroupInteractor.GetGroup(applicant.getGroupId());
            if (group != null) {
                GroupApplicant.setText(group.getName());
            } else {
                GroupApplicant.setText("Aucun groupe");
            }
            DirigeantApplicant.setText(applicant.getFirstNameBoss() + " " + applicant.getNameBoss());
            int totalObligationValue = 0;
            for(Integer obligationId : applicant.getObligations()) {
                Obligation obligation = ObligationInteractor.GetObligation(obligationId);
                if (obligation != null) {
                    totalObligationValue += obligation.getCapital();
                } else {
                    System.out.println("Obligation not found for ID: " + obligationId);
                }
            }
            setNumberLabel(ValeurObligationsApplicant, String.valueOf(totalObligationValue));
        } else {
            // Clear the details if no person is selected
            NameApplicant.setText("");
            GroupApplicant.setText("");
            DirigeantApplicant.setText("");
            ValeurObligationsApplicant.setText("");
        }
    }

    public void setNumberLabel(Label label, String numberAsString) {
        try {
            // Nettoyer la chaîne d'entrée (enlever espaces existants, virgules, etc.)
            String cleanString = numberAsString.replaceAll("[\\s,]", "");
            
            // Convertir en nombre
            double value = Double.parseDouble(cleanString);
            
            // Formater avec espaces
            NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
            format.setGroupingUsed(true);
            label.setText(format.format(value) + " €");
        } catch (NumberFormatException e) {
            // En cas d'erreur de format, afficher la chaîne originale ou gérer l'erreur
            label.setText(numberAsString);
            // Ou bien : 
            // label.setText("Format invalide");
        }
    }

    @FXML
    private void addApplicant() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mypackage/view/add/AddEmetteur.fxml"));
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/mypackage/view/edit/EditEmetteur.fxml"));
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
                Group group = GroupInteractor.GetGroup(selectedApplicant.getGroupId());
                group.removeMember(selectedApplicant.getId());
                GroupInteractor.DeleteGroup(selectedApplicant.getGroupId());
                GroupInteractor.SaveGroup(group);
                selectedApplicant.getObligations().forEach(obligationId -> {
                    ObligationInteractor.DeleteObligation(obligationId);
                });
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
