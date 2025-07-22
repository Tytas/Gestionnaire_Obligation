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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mypackage.MainApp;
import mypackage.model.Family;
import mypackage.model.Investor;
import mypackage.model.DataBaseInteractor.*;
import mypackage.view.add.AddFamilyController;
import mypackage.view.edit.EditFamilyController;
import mypackage.view.consult.ConsultFamilyController;
import mypackage.view.util.ConfirmWindow;



public class ControllerFamilies {

    @FXML
    private Label NameFamily;
    @FXML
    private Label ValueFamily;
    @FXML
    private Label NbSouscripteursFamily;
    @FXML
    private ListView<String[]> listViewContact;
    @FXML
    private TableView<Family> tableFamily;
    @FXML
    private ListView<Investor> listViewInvestor;
    @FXML
    private TableColumn<Family, String> listFamName;
    @FXML
    private TableColumn<Family, String> listFamId;
    @FXML
    private TextField searchFieldFamily;

    @FXML
    private Button deleteButton;

    private MainApp mainApp;

    private Stage stage;

    private ObservableList<Family> listFam = FXCollections.observableArrayList();
    private FilteredList<Family> filteredFamilies;

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
        
        // Setup filtered list
        filteredFamilies = new FilteredList<>(listFam, p -> true);
        
        // Setup search functionality
        if (searchFieldFamily != null) {
            searchFieldFamily.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredFamilies.setPredicate(family -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    
                    String lowerCaseFilter = newValue.toLowerCase();
                    
                    if (family.getName() != null && family.getName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    if (String.valueOf(family.getId()).contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });
        }
        
        // Wrap the FilteredList in a SortedList
        SortedList<Family> sortedFamilies = new SortedList<>(filteredFamilies);
        sortedFamilies.comparatorProperty().bind(tableFamily.comparatorProperty());
        
        if (!listFam.isEmpty()) {
            System.out.println("Families loaded: " + listFam.size());
            tableFamily.setItems(sortedFamilies);
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

        ObservableList<Investor> investors = FXCollections.observableArrayList();
        ObservableList<String[]> contacts = FXCollections.observableArrayList();
        int totalSouscriptions = 0;
        if (fam != null) {
            contacts.addAll(fam.getContacts());
            for (Integer investorId : fam.getInvestors()) {
                Investor investor = InvestorInteractor.GetInvestor(investorId);
                if (investor != null) {
                    investors.add(investor);
                    for (int idObligation : investor.getObligations()) {
                        totalSouscriptions += ObligationInteractor.GetObligation(idObligation).getInvestorCapital(investorId) 
                                            * ObligationInteractor.GetObligation(idObligation).getValeurNominale();
                    }   
                }
            }
        }
        listViewInvestor.getItems().clear();
        listViewInvestor.setItems(investors);
        listViewInvestor.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Investor item, boolean empty) {
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

        listViewContact.getItems().clear();
        listViewContact.setItems(contacts);
        listViewContact.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String[] item, boolean empty) {
                super.updateItem(item, empty);
                Label nameField = new Label();
                Label emailField = new Label();
                Label phoneField = new Label();
                AnchorPane content = new AnchorPane(nameField, emailField, phoneField);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    nameField.setText(item[1] + " " + item[0]); 
                    emailField.setText(item[2]); 
                    phoneField.setText(item[3]); 
                    AnchorPane.setLeftAnchor(nameField, 1.0);
                    AnchorPane.setLeftAnchor(emailField, 150.0);
                    AnchorPane.setLeftAnchor(phoneField, 400.0);
                    setGraphic(content);
                }
            }
        });

        if(fam != null) {
            // Update the person details in the label
            NameFamily.setText(fam.getName());
            setNumberLabel(ValueFamily, String.valueOf(totalSouscriptions));
            NbSouscripteursFamily.setText(String.valueOf(fam.getInvestors().size()));
        } else {
            // Clear the details if no person is selected
            NameFamily.setText("");
            ValueFamily.setText("");
            NbSouscripteursFamily.setText("");
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
                listFam.clear();
                for (Integer id : ids) {
                    listFam.add(FamilyInteractor.GetFamily(id));
                }
            }
            tableFamily.setItems(listFam);
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
                    listFam.clear();
                    for (Integer id : ids) {
                        listFam.add(FamilyInteractor.GetFamily(id));
                    }
                }
                tableFamily.setItems(listFam);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No obligation selected to edit.");
        }
    }

    @FXML
    private void consultFamily() {
        if (selectedFamily != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/mypackage/view/consult/ConsultFamily.fxml"));
                Parent root = loader.load();
                
                // Récupère le contrôleur lié au FXML (instancié automatiquement)
                ConsultFamilyController consultFamilyWindow = loader.getController();
                consultFamilyWindow.setFamily(selectedFamily);
                consultFamilyWindow.init(); // Initialise les événements des boutons

                stage = new Stage();
                stage.setTitle("Consulter une famille");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL); // bloque la fenêtre principale
                stage.showAndWait(); // attend que la fenêtre se ferme

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No family selected to consult.");
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
