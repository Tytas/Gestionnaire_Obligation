package mypackage.controllers;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
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
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mypackage.MainApp;
import mypackage.model.DataBaseInteractor.*;
import mypackage.model.Family;
import mypackage.model.Investor;
import mypackage.model.InvestorNP;
import mypackage.model.InvestorLP;
import mypackage.model.Obligation;
import mypackage.view.util.ConfirmWindow;
import mypackage.view.add.AddSouscripteurMoralController;
import mypackage.view.add.AddSouscripteurPhysiqueController;
import mypackage.view.edit.EditSouscripteurMoralController;
import mypackage.view.edit.EditSouscripteurPhysiqueController;


public class ControllerSouscripteurs {

    @FXML
    private Label NameInvestor;
    @FXML
    private Label FamilyInvestor;
    @FXML
    private Label NameIBAN;
    @FXML
    private Label NameBIC;
    @FXML
    private Label PhoneLabel;
    @FXML
    private Label EmailLabel;
    @FXML
    private ListView<Obligation> listViewObligation;
    @FXML
    private TableView<Investor> tableInvestors;
    @FXML
    private TableColumn<Investor, String> listInvestorName;
    @FXML
    private TableColumn<Investor, String> listInvestorStatut;
    @FXML
    private TableColumn<Investor, String> listInvestorCountry;
    @FXML
    private TextField searchFieldInvestors;

    @FXML
    private Button deleteButton;

    private MainApp mainApp;

    private Stage stage;

    private ObservableList<Investor> listInvestor = FXCollections.observableArrayList();
    private FilteredList<Investor> filteredInvestors;

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
        
        // Setup filtered list
        filteredInvestors = new FilteredList<>(listInvestor, p -> true);
        
        // Setup search functionality
        if (searchFieldInvestors != null) {
            searchFieldInvestors.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredInvestors.setPredicate(investor -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    
                    String lowerCaseFilter = newValue.toLowerCase();
                    
                    if (investor.getName() != null && investor.getName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    
                    // Search by country
                    String country = "";
                    if (investor instanceof InvestorNP) {
                        country = ((InvestorNP) investor).getAddress()[4];
                    } else if (investor instanceof InvestorLP) {
                        country = ((InvestorLP) investor).getAddress()[4];
                    }
                    if (country != null && country.toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    
                    if (String.valueOf(investor.getId()).contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });
        }
        
        // Wrap the FilteredList in a SortedList
        SortedList<Investor> sortedInvestors = new SortedList<>(filteredInvestors);
        sortedInvestors.comparatorProperty().bind(tableInvestors.comparatorProperty());
        
        if (!listInvestor.isEmpty()) {
            System.out.println("Investors loaded: " + listInvestor.size());
            tableInvestors.setItems(sortedInvestors);
        }
        this.listInvestorName.setCellValueFactory(new PropertyValueFactory<Investor, String>("name"));

        this.listInvestorStatut.setCellValueFactory(cellData -> {
            Investor investor = cellData.getValue();
            if (investor instanceof InvestorNP) {
                return new javafx.beans.property.SimpleStringProperty("PP");
            } else if (investor instanceof InvestorLP) {
                return new javafx.beans.property.SimpleStringProperty("PM");
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });

        this.listInvestorCountry.setCellValueFactory(cellData -> {
            Investor investor = cellData.getValue();
            if (investor instanceof InvestorNP) {
                return new javafx.beans.property.SimpleStringProperty(((InvestorNP) investor).getAddress()[4]); 
            } else if (investor instanceof InvestorLP) {
                return new javafx.beans.property.SimpleStringProperty(((InvestorLP) investor).getAddress()[4]);
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });

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
        ObservableList<Obligation> obligations = FXCollections.observableArrayList();
        if (investor != null) {
            for (Integer obligationId : investor.getObligations()) {
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
                Label montantField = new Label();
                Label montantEuroField = new Label();
                AnchorPane content = new AnchorPane(nameField, montantField, montantEuroField);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    nameField.setText(item.getName());
                    montantField.setText(String.valueOf(item.getInvestorCapital(investor.getId()) + " obligations"));
                    setNumberLabel(montantEuroField, String.valueOf(item.getInvestorCapital(investor.getId()) * item.getValeurNominale()));
                    AnchorPane.setLeftAnchor(nameField, 10.0);
                    AnchorPane.setLeftAnchor(montantField, 200.0);
                    AnchorPane.setLeftAnchor(montantEuroField, 400.0);
                    setGraphic(content);
                }
            }
        });

        if (investor instanceof InvestorNP) {
            String name = investor.getName() + " " + ((InvestorNP) investor).getFirstName();
            NameInvestor.setText(name);
            String familyName = FamilyInteractor.GetFamily(investor.getFamilyId()).getName();
            FamilyInvestor.setText(familyName);
            String iban = ((InvestorNP) investor).getIBAN();
            NameIBAN.setText(iban);
            String bic = ((InvestorNP) investor).getBIC();
            NameBIC.setText(bic);
            String phone = ((InvestorNP) investor).getPhoneNumber();
            PhoneLabel.setText(phone);
            String email = ((InvestorNP) investor).getEmail();
            EmailLabel.setText(email);
        } else if (investor instanceof InvestorLP) {
            String name = ((InvestorLP) investor).getName();
            NameInvestor.setText(name);
            String familyName = FamilyInteractor.GetFamily(investor.getFamilyId()).getName();
            FamilyInvestor.setText(familyName);
            String iban = ((InvestorLP) investor).getIBAN();
            NameIBAN.setText(iban);
            String bic = ((InvestorLP) investor).getBIC();
            NameBIC.setText(bic);
            String phone = ((InvestorLP) investor).getPhoneNumberBoss();
            PhoneLabel.setText(phone);
            String email = ((InvestorLP) investor).getEmailBoss();
            EmailLabel.setText(email);
        } else {
            // Clear the details if no person is selected
            NameInvestor.setText("");
            FamilyInvestor.setText("");
            NameIBAN.setText("");
            NameBIC.setText("");
            PhoneLabel.setText("");
            EmailLabel.setText("");
        }
    }

    @FXML
    private void deleteInvestor(){
        if(selectedInvestor == null) {
            System.out.println("No Investor selected to delete.");
            return;
        }
        if (ConfirmWindow.confirmWindow()) {
            Family family = FamilyInteractor.GetFamily(selectedInvestor.getFamilyId());
            family.removeInvestor(selectedInvestor.getId());
            FamilyInteractor.DeleteFamily(family.getId());
            FamilyInteractor.SaveFamily(family);
            selectedInvestor.getObligations().forEach(obligationId -> {
                Obligation obligation = ObligationInteractor.GetObligation(obligationId);
                obligation.removeInvestor(selectedInvestor.getId());
                ObligationInteractor.DeleteObligation(obligation.getId());
                ObligationInteractor.SaveObligation(obligation);
            });
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mypackage/view/add/AddSouscripteurMoral.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mypackage/view/add/AddSouscripteurPhysique.fxml"));
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
                FXMLLoader loader = null;
                Boolean isInvestorLP = null;
                if( selectedInvestor instanceof InvestorLP) {       
                    loader = new FXMLLoader(getClass().getResource("/mypackage/view/edit/EditSouscripteurMoral.fxml"));
                    isInvestorLP = true;
                } else if (selectedInvestor instanceof InvestorNP) {
                    loader = new FXMLLoader(getClass().getResource("/mypackage/view/edit/EditSouscripteurPhysique.fxml"));
                    isInvestorLP = false;
                }
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
