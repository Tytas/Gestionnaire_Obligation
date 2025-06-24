package mypackage.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mypackage.MainApp;
import mypackage.model.DataBaseInteractor.*;
import mypackage.model.Investor;
import mypackage.model.InvestorNP;
import mypackage.model.InvestorLP;
import mypackage.view.util.ConfirmWindow;
import mypackage.view.add.AddObject.AddSouscripteurMoralController;
import mypackage.view.add.AddObject.AddSouscripteurPhysiqueController;
import mypackage.view.edit.EditObject.EditSouscripteurMoralController;
import mypackage.view.edit.EditObject.EditSouscripteurPhysiqueController;


public class ControllerSouscripteurs {

    @FXML
    private Label NameInvestor;
    @FXML
    private TableView<Investor> tableInvestors;
    @FXML
    private TableColumn<Investor, String> listInvestorName;
    @FXML
    private TableColumn<Investor, String> listInvestorId;

    @FXML
    private Button deleteButton;

    private MainApp mainApp;

    private Stage stage;

    private ObservableList<Investor> listInvestor = FXCollections.observableArrayList();

    private ArrayList<Integer> ids = new ArrayList<>();

    private Investor selectedInvestor;

    public ControllerSouscripteurs() {
    }

    @FXML
    private void initialize() {
        // Add some sample data
        ids = InvestorInteractor.GetAllInvestorId();
        for (Integer id : ids) {
            if (InvestorInteractor.GetInvestorLP(id) != null){
                listInvestor.add(InvestorInteractor.GetInvestorLP(id));
            }
            if (InvestorInteractor.GetInvestorNP(id) != null){
                listInvestor.add(InvestorInteractor.GetInvestorNP(id));
            }
        }
        if (!listInvestor.isEmpty()) {
            System.out.println("Investors loaded: " + listInvestor.size());
            tableInvestors.setItems(listInvestor);
        }
        this.listInvestorName.setCellValueFactory(new PropertyValueFactory<Investor, String>("name"));
        this.listInvestorId.setCellValueFactory(new PropertyValueFactory<Investor, String>("id"));

        displayInvestor(null);
    
        tableInvestors.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> displayInvestor(newValue));
    }

    public ObservableList<Investor> getInvestors() {
        return listInvestor;
    }

    public ArrayList<Integer> GetIds() {
        return ids;
    }   

    private void displayInvestor(Investor investor) {
        this.selectedInvestor = investor;
        if (investor instanceof InvestorNP) {
            String name = ((InvestorNP) investor).getName();
            NameInvestor.setText(name);
        } else if (investor instanceof InvestorLP) {
            String name = ((InvestorLP) investor).getName();
            NameInvestor.setText(name);
        } else {
            // Clear the details if no person is selected
            NameInvestor.setText("");
        }
    }

    @FXML
    private void deleteInvestor(){
        if(selectedInvestor == null) {
            System.out.println("No Investor selected to delete.");
            return;
        }
        if (ConfirmWindow.confirmWindow()) {
            if (selectedInvestor instanceof InvestorLP) {
                System.out.println("Investor deleted: " + ((InvestorLP)selectedInvestor).getName());
                InvestorInteractor.DeleteInvestorLP(selectedInvestor.getId());
                listInvestor.remove(selectedInvestor);
            } else if (selectedInvestor instanceof InvestorNP) {
                System.out.println("Investor deleted: " + ((InvestorNP)selectedInvestor).getName());
                InvestorInteractor.DeleteInvestorNP(selectedInvestor.getId());
                listInvestor.remove(selectedInvestor);
            }
        } else {
            System.out.println("Deletion cancelled.");
            return;
        }
    }

    private void addSouscripteurMoral() {
        try {
            File fxmlFile = new File("src/main/mypackage/view/add/AddSouscripteurMoral.fxml");
            if (!fxmlFile.exists()) {
                System.err.println("FXML file not found: " + fxmlFile.getAbsolutePath());
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
            Parent root = loader.load();
            
            // Récupère le contrôleur lié au FXML (instancié automatiquement)
            AddSouscripteurMoralController addSouscripteurMoralWindow = loader.getController();

            stage = new Stage();
            stage.setTitle("Ajouter une obligation");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // bloque la fenêtre principale
            stage.showAndWait(); // attend que la fenêtre se ferme

            if (addSouscripteurMoralWindow.getResult()) {
                // Refresh the list of investors
                ids = InvestorInteractor.GetAllInvestorId();
                listInvestor.clear();
                for (Integer id : ids) {
                    listInvestor.add(InvestorInteractor.GetInvestor(id));
                }
            }
            tableInvestors.setItems(listInvestor);
            selectedInvestor = null; // Reset selected investor
            displayInvestor(null); // Clear displayed investor details
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addSouscripteurPhysique() {
        try {
            File fxmlFile = new File("src/main/mypackage/view/add/AddSouscripteurPhysique.fxml");
            if (!fxmlFile.exists()) {
                System.err.println("FXML file not found: " + fxmlFile.getAbsolutePath());
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
            Parent root = loader.load();
            
            // Récupère le contrôleur lié au FXML (instancié automatiquement)
            AddSouscripteurPhysiqueController addSouscripteurPhysiqueWindow = loader.getController();

            stage = new Stage();
            stage.setTitle("Ajouter un souscripteur physique");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // bloque la fenêtre principale
            stage.showAndWait(); // attend que la fenêtre se ferme

            if (addSouscripteurPhysiqueWindow.getResult()) {
                // Refresh the list of souscripteurs
                ids = InvestorInteractor.GetAllInvestorId();
                listInvestor.clear();
                for (Integer id : ids) {
                    listInvestor.add(InvestorInteractor.GetInvestor(id));
                }
            }
            tableInvestors.setItems(listInvestor);
            selectedInvestor = null; // Reset selected investor
            displayInvestor(null); // Clear displayed investor details
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void choiceNewInvestorType() {
        // Open a dialog to choose the type of investor to add
        Alert choiceAlert = new Alert(AlertType.CONFIRMATION);
        choiceAlert.setTitle("Choix du type de souscripteur");
        choiceAlert.setHeaderText("Veuillez choisir le type de souscripteur à ajouter :");
        ButtonType buttonMoral = new ButtonType("Souscripteur Moral");
        ButtonType buttonPhysique = new ButtonType("Souscripteur Physique");
        choiceAlert.getButtonTypes().setAll(buttonMoral, buttonPhysique);

        Optional<ButtonType> choiceResult = choiceAlert.showAndWait();
        if (choiceResult.isPresent()) {
            if (choiceResult.get() == buttonMoral) {
                addSouscripteurMoral();
            } else if (choiceResult.get() == buttonPhysique) {
                addSouscripteurPhysique();
            }
        }
    }

    @FXML
    private void editObligation() {
        if (selectedInvestor != null) {
            try {
                File fxmlFile = null;
                Boolean isInvestorLP = null;
                if( selectedInvestor instanceof InvestorLP) {       
                    fxmlFile = new File("src/main/mypackage/view/edit/EditSouscripteurMoral.fxml");
                    isInvestorLP = true;
                } else if (selectedInvestor instanceof InvestorNP) {
                    fxmlFile = new File("src/main/mypackage/view/edit/EditSouscripteurPhysique.fxml");
                    isInvestorLP = false;
                }
                if (!fxmlFile.exists()) {
                    System.err.println("FXML file not found: " + fxmlFile.getAbsolutePath());
                    return;
                }
                FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
                Parent root = loader.load();
                
                // Récupère le contrôleur lié au FXML (instancié automatiquement)
                if( isInvestorLP == true) {
                    EditSouscripteurMoralController editSouscripteurWindow = loader.getController();
                    InvestorLP selectedInvestorLP = (InvestorLP) selectedInvestor;
                    editSouscripteurWindow.initData(selectedInvestorLP);
                    stage = new Stage();
                    stage.setTitle("Modifier un souscripteur");
                    stage.setScene(new Scene(root));
                    stage.initModality(Modality.APPLICATION_MODAL); // bloque la fenêtre principale
                    stage.showAndWait(); // attend que la fenêtre se ferme

                    if (editSouscripteurWindow.getResult()) {
                        // Refresh the list of obligations
                        ids = ObligationInteractor.GetAllObligationsId();
                        listInvestor.clear();
                        for (Integer id : ids) {
                            listInvestor.add(InvestorInteractor.GetInvestor(id));
                        }
                    }
                    tableInvestors.setItems(listInvestor);
                } else {
                    EditSouscripteurPhysiqueController editSouscripteurWindow = loader.getController();
                    InvestorNP selectedInvestorNP = (InvestorNP) selectedInvestor;
                    editSouscripteurWindow.initData(selectedInvestorNP);
                    stage = new Stage();
                    stage.setTitle("Modifier un souscripteur");
                    stage.setScene(new Scene(root));
                    stage.initModality(Modality.APPLICATION_MODAL); // bloque la fenêtre principale
                    stage.showAndWait(); // attend que la fenêtre se ferme

                    if (editSouscripteurWindow.getResult()) {
                        // Refresh the list of obligations
                        ids = InvestorInteractor.GetAllInvestorId();
                        listInvestor.clear();
                        for (Integer id : ids) {
                            listInvestor.add(InvestorInteractor.GetInvestor(id));
                        }
                    }
                    tableInvestors.setItems(listInvestor);
                }
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
