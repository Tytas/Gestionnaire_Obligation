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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mypackage.MainApp;
import mypackage.model.Group;
import mypackage.model.Applicant;
import mypackage.model.Obligation;
import mypackage.model.DataBaseInteractor.GroupInteractor;
import mypackage.model.DataBaseInteractor.ApplicantInteractor;
import mypackage.model.DataBaseInteractor.ObligationInteractor;
import mypackage.view.consult.ConsultGroupController;
import mypackage.view.util.ConfirmWindow;
import mypackage.view.util.GroupeWindow;



public class ControllerGroups {

    @FXML
    private Label NameGroup;
    @FXML
    private Label DirigeantGroup;
    @FXML
    private Label ValeurObligationsGroup;
    @FXML
    private ListView<Obligation> listviewObligation;
    @FXML
    private TableView<Group> tableGroups;
    @FXML
    private TableColumn<Group, String> listGroupName;
    @FXML
    private TableColumn<Group, String> listGroupId;

    @FXML
    private Button deleteButton;

    private MainApp mainApp;

    private Stage stage;

    private ObservableList<Group> listGroup = FXCollections.observableArrayList();

    private ArrayList<Integer> ids = new ArrayList<>();

    private Group selectedGroup;

    public ControllerGroups() {
    }

    @FXML
    private void initialize() {
        // Add some sample data
        ids = GroupInteractor.GetAllGroupsId();
        for (Integer id : ids) {
            listGroup.add(GroupInteractor.GetGroup(id));
        }
        if (!listGroup.isEmpty()) {
            System.out.println("Groups loaded: " + listGroup.size());
            tableGroups.setItems(listGroup);
        }
        this.listGroupName.setCellValueFactory(new PropertyValueFactory<Group, String>("name"));
        this.listGroupId.setCellValueFactory(new PropertyValueFactory<Group, String>("id"));

        // Vérifier que les composants FXML sont bien injectés avant de les utiliser
        if (listviewObligation != null) {
            displayGroup(null);
        } else {
            System.out.println("listviewObligation is not initialized.");
        }
    
        tableGroups.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> displayGroup(newValue));
    }

    private void displayGroup(Group group) {
        this.selectedGroup = group;

        ObservableList<Obligation> obligations = FXCollections.observableArrayList();
        if (group != null) {
            for (Integer memberId : group.getMembers()) {
                Applicant applicant = ApplicantInteractor.GetApplicant(memberId);
                if (applicant != null) {
                    for (Integer obligationId : applicant.getObligations()) {
                        Obligation obligation = ObligationInteractor.GetObligation(obligationId);
                        if (obligation != null) {
                            obligations.add(obligation);
                        }
                    }
                }
            }
        }
        listviewObligation.getItems().clear();
        listviewObligation.setItems(obligations);
        listviewObligation.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Obligation item, boolean empty) {
                super.updateItem(item, empty);
                Label nomField = new Label();
                Label tauxField = new Label();
                Label capitalField = new Label();
                Label dateField = new Label();
                Label nomEmetteurField = new Label();
                AnchorPane content = new AnchorPane(nomField, tauxField, capitalField, dateField, nomEmetteurField);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    nomField.setText(item.getName());
                    tauxField.setText(item.getRate()[1] + "%" + " + " + item.getRate()[0] + "% IN FINE");
                    setNumberLabel(capitalField, String.valueOf(item.getCapital()));
                    dateField.setText(item.getStartDate() + " - " + item.getEndDate());
                    nomEmetteurField.setText(ApplicantInteractor.GetApplicant(item.getApplicantId()).getName());
                    AnchorPane.setLeftAnchor(nomField, 1.0);
                    AnchorPane.setLeftAnchor(tauxField, 100.0);
                    AnchorPane.setLeftAnchor(capitalField, 225.0);
                    AnchorPane.setLeftAnchor(dateField, 350.0);
                    AnchorPane.setLeftAnchor(nomEmetteurField, 550.0);
                    setGraphic(content);
                }
            }
        });

        if(group != null) {
            // Update the person details in the label
            NameGroup.setText(group.getName());
            DirigeantGroup.setText(group.getBossFirstName() + " " + group.getBossName());
            int totalObligationValue = 0;
            for (Obligation obligation : obligations) {
                totalObligationValue += obligation.getCapital();
                System.out.println("Obligation not found for ID: " + obligation.getId());
            }
            setNumberLabel(ValeurObligationsGroup, String.valueOf(totalObligationValue));
        } else {
            // Clear the details if no person is selected
            NameGroup.setText("");
            DirigeantGroup.setText("");
            ValeurObligationsGroup.setText("");
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
    private void addGroup() {
        try {
            GroupeWindow groupeWindow = new GroupeWindow();
            groupeWindow.show();
            listGroup.clear();
            ids = GroupInteractor.GetAllGroupsId();
            for (Integer id : ids) {
                listGroup.add(GroupInteractor.GetGroup(id));
            }
            tableGroups.setItems(listGroup);
            selectedGroup = null; // Reset selected Group
            displayGroup(null); // Clear displayed Group details
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteGroup(){
        if (selectedGroup != null) {
            if (ConfirmWindow.confirmWindow()) {
                System.out.println("Group deleted: " + selectedGroup.getName());
                for (Integer idMember : selectedGroup.getMembers()) {
                    Applicant applicant = ApplicantInteractor.GetApplicant(idMember);
                    applicant.setGroupId(0);
                    ApplicantInteractor.DeleteApplicant(idMember);
                    ApplicantInteractor.SaveApplicant(applicant);
                }
                GroupInteractor.DeleteGroup(selectedGroup.getId());
                listGroup.remove(selectedGroup);
            } else {
                System.out.println("Deletion cancelled.");
                return;
            }
        } else {
            System.out.println("No Group selected to delete.");
        }
    }

    @FXML
    private void consultGroup() {
        if (selectedGroup != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/mypackage/view/consult/ConsultGroup.fxml"));
                Parent root = loader.load();
                
                // Récupère le contrôleur lié au FXML (instancié automatiquement)
                ConsultGroupController consultGroupWindow = loader.getController();
                System.out.println("Consulting group: " + selectedGroup.getName());
                consultGroupWindow.setGroup(selectedGroup);
                consultGroupWindow.init();

                stage = new Stage();
                stage.setTitle("Consulter un groupe");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL); // bloque la fenêtre principale
                stage.showAndWait(); // attend que la fenêtre se ferme

                if (consultGroupWindow.getResult()) {
                    // Refresh the list of groups
                    ids = GroupInteractor.GetAllGroupsId();
                    listGroup.clear();
                    for (Integer id : ids) {
                        listGroup.add(GroupInteractor.GetGroup(id));
                    }
                }
                tableGroups.setItems(listGroup);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No group selected to consult.");
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
