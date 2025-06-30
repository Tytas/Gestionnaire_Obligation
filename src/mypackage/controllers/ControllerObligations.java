package mypackage.controllers;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mypackage.MainApp;
import mypackage.model.DataBaseInteractor.*;
import mypackage.model.Obligation;
import mypackage.model.Applicant;
import mypackage.model.Investor;
import mypackage.view.add.AddObject.AddObligationController;
import mypackage.view.edit.EditObject.EditObligationController;
import mypackage.view.util.ConfirmWindow;



public class ControllerObligations {

    @FXML
    private Label NameObligation;
    @FXML
    private Label capitalObligation;
    @FXML
    private Label PartObligation;
    @FXML
    private Label EmetteurObligation;
    @FXML
    private Label OS_OCAObligation;
    @FXML
    private Label ISINObligation;
    @FXML
    private Label DureeObligation;
    @FXML
    private Label DateDebutObligation;
    @FXML
    private Label DateFinObligation;
    @FXML 
    private Label TauxObligation;
    @FXML
    private Label NombreSouscripteurObligation;
    @FXML
    private Label ProrogationObligation;
    @FXML
    private Label DureeProrogationObligation;
    @FXML
    private Label TauxProrogationObligation;

    @FXML
    private TableView<Obligation> tableObligations;
    @FXML
    private TableColumn<Obligation, String> listObligName;
    @FXML
    private TableColumn<Obligation, String> listObligId;

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
        if (!ids.isEmpty()) {
            for (Integer id : ids) {
                listOblig.add(ObligationInteractor.GetObligation(id));
            }
            if (!listOblig.isEmpty()) {
                System.out.println("Obligations loaded: " + listOblig.size());
                tableObligations.setItems(listOblig);
            }
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
            PartObligation.setText(String.valueOf(oblig.getValeurNominale()));
            EmetteurObligation.setText(ApplicantInteractor.GetApplicant(oblig.getApplicantId()).getName());
            if(oblig.getConvertible()) {
                OS_OCAObligation.setText("OCA");
            } else {
                OS_OCAObligation.setText("Obligation Simple");
            }
            ISINObligation.setText(oblig.getIsin());
            DureeObligation.setText(String.valueOf(oblig.getDurationMonths()+" mois"));
            DateDebutObligation.setText(oblig.getStartDate());
            LocalDate dateFin = LocalDate.parse(oblig.getStartDate()).plusMonths(oblig.getDurationMonths());
            DateFinObligation.setText(dateFin.toString());
            if(oblig.getRate()[0] != 0 && oblig.getRate()[1] != 0) {
                TauxObligation.setText(String.valueOf(oblig.getRate()[1]) + "%" + " + " + String.valueOf(oblig.getRate()[0]) + "% INFINE");
            } else {
                TauxObligation.setText("Aucun Taux");
                if(oblig.getRate()[0] != 0){
                    TauxObligation.setText(String.valueOf(oblig.getRate()[0]) + "% INFINE");
                } if(oblig.getRate()[1] != 0){
                    TauxObligation.setText(String.valueOf(oblig.getRate()[1]) + "%");
                }
            }
            NombreSouscripteurObligation.setText(String.valueOf(oblig.getInvestors().size()));
            if(oblig.getProrogation()[0] == "") {
                ProrogationObligation.setText("Aucune");
                DureeProrogationObligation.setText("");
                TauxProrogationObligation.setText("");
            } else {
                ProrogationObligation.setText("Oui");
                DureeProrogationObligation.setText(oblig.getProrogation()[0]);
                TauxProrogationObligation.setText(oblig.getProrogation()[1] +  "%");
            }
        } else {
            // Clear the details if no person is selected
            NameObligation.setText("");
            capitalObligation.setText("");
            PartObligation.setText("");
            EmetteurObligation.setText("");
            OS_OCAObligation.setText("");  
            ISINObligation.setText("");
            DureeObligation.setText("");
            DateDebutObligation.setText("");
            DateFinObligation.setText("");
            TauxObligation.setText("");
            NombreSouscripteurObligation.setText("");
            ProrogationObligation.setText("");
            DureeProrogationObligation.setText("");
            TauxProrogationObligation.setText("");
        }
    }

    @FXML
    private void deleteObligation(){
        if (selectedObligation != null) {
            if (ConfirmWindow.confirmWindow()) {
                System.out.println("Obligation deleted: " + selectedObligation.getName());
                Applicant applicant = ApplicantInteractor.GetApplicant(selectedObligation.getApplicantId());
                applicant.removeObligation(selectedObligation.getId());
                ApplicantInteractor.DeleteApplicant(applicant.getId());
                ApplicantInteractor.SaveApplicant(applicant);
                selectedObligation.getInvestors().forEach((investorId, capital) -> {
                    Investor investor = InvestorInteractor.GetInvestor(investorId);
                    investor.removeObligation(selectedObligation.getId());
                    InvestorInteractor.DeleteInvestor(investorId);
                    InvestorInteractor.SaveInvestor(investor);
                });
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
            File fxmlFile = new File("src/mypackage/view/add/AddObligation.fxml");
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

    @FXML
    private void editObligation() {
        if (selectedObligation != null) {
            try {
                File fxmlFile = new File("src/mypackage/view/edit/EditObligation.fxml");
                if (!fxmlFile.exists()) {
                    System.err.println("FXML file not found: " + fxmlFile.getAbsolutePath());
                    return;
                }
                FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
                Parent root = loader.load();
                
                // Récupère le contrôleur lié au FXML (instancié automatiquement)
                EditObligationController editObligationWindow = loader.getController();
                System.out.println("Editing obligation: " + selectedObligation.getName());
                editObligationWindow.initData(selectedObligation);
                
                stage = new Stage();
                stage.setTitle("Modifier une obligation");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL); // bloque la fenêtre principale
                stage.showAndWait(); // attend que la fenêtre se ferme

                if (editObligationWindow.getResult()) {
                    // Refresh the list of obligations
                    ids = ObligationInteractor.GetAllObligationsId();
                    listOblig.clear();
                    for (Integer id : ids) {
                        listOblig.add(ObligationInteractor.GetObligation(id));
                    }
                }
                tableObligations.setItems(listOblig);
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
