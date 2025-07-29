package mypackage.controllers;

import java.text.NumberFormat;
import java.util.Locale;
import java.time.LocalDate;
import java.util.ArrayList;
import java.time.temporal.ChronoUnit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mypackage.MainApp;
import mypackage.model.DataBaseInteractor.*;
import mypackage.model.Obligation;
import mypackage.model.Applicant;
import mypackage.model.Investor;
import mypackage.view.consult.ConsultObligationController;
import mypackage.view.add.AddObligationController;
import mypackage.view.edit.EditObligationController;
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
    private Label ProrogationEnCoursObligation;

    
    @FXML
    private TextField searchFieldObligations;
    @FXML
    private TableView<Obligation> tableObligations;
    @FXML
    private TableColumn<Obligation, String> listObligName;
    @FXML
    private TableColumn<Obligation, String> listObligDate;
    @FXML
    private TableColumn<Obligation, Long> listObligCapital;
    @FXML
    private TableColumn<Obligation, String> listObligEtat;

    private MainApp mainApp;

    private Stage stage;

    private ObservableList<Obligation> listOblig = FXCollections.observableArrayList();
    private FilteredList<Obligation> filteredOblig;

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
        }

        // Setup filtered list
        filteredOblig = new FilteredList<>(listOblig, p -> true);

        // Setup search functionality
        if (searchFieldObligations != null) {
            searchFieldObligations.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredOblig.setPredicate(obligation -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    
                    String lowerCaseFilter = newValue.toLowerCase();

                    if (obligation.getName() != null && obligation.getName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    if (String.valueOf(obligation.getId()).contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });
        }
        
        // Wrap the FilteredList in a SortedList
        SortedList<Obligation> sortedObligations = new SortedList<>(filteredOblig);
        sortedObligations.comparatorProperty().bind(tableObligations.comparatorProperty());
        if (!listOblig.isEmpty()) {
            System.out.println("Obligations loaded: " + listOblig.size());
            tableObligations.setItems(sortedObligations);
        }

        this.listObligName.setCellValueFactory(new PropertyValueFactory<Obligation, String>("name"));
        this.listObligDate.setCellValueFactory(new PropertyValueFactory<Obligation, String>("startDate"));
        this.listObligCapital.setCellValueFactory(new PropertyValueFactory<Obligation, Long>("capital"));
        
        this.listObligCapital.setCellFactory(column -> new javafx.scene.control.TableCell<Obligation, Long>() {
            @Override
            protected void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    try {
                        double value = item.doubleValue();
                        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
                        format.setGroupingUsed(true);
                        String formatted = format.format(value);
                        setText(formatted + " €");
                    } catch (Exception e) {
                        setText(item.toString());
                    }
                }
            }
        });

        // Nouvelle colonne avec valeur calculée/personnalisée
        this.listObligEtat.setCellValueFactory(cellData -> {
            Obligation obligation = cellData.getValue();
            // Exemple : calculer le statut basé sur plusieurs propriétés
            String status = calculateStatus(obligation);
            return new javafx.beans.property.SimpleStringProperty(status);
        });
        tableObligations.setRowFactory(tv -> {
            javafx.scene.control.TableRow<Obligation> row = new javafx.scene.control.TableRow<>();
            row.itemProperty().addListener((obs, oldObligation, newObligation) -> {
                row.getStyleClass().removeAll("percentage-low", "percentage-medium", "percentage-high", "percentage-full", "percentage-error");
                if (newObligation != null) {
                    String status = calculateStatus(newObligation);
                    if (status.contains("%")) {
                        try {
                            String percentageStr = status.replace("%", "").replace(",", ".");
                            double percentage = Double.parseDouble(percentageStr);
                            
                            if (percentage < 50.0) {
                                row.getStyleClass().add("percentage-low");
                            } else if (percentage < 80.0) {
                                row.getStyleClass().add("percentage-medium");
                            } else if (percentage < 100.0) {
                                row.getStyleClass().add("percentage-high");
                            } else if (percentage >= 100.0) {
                                row.getStyleClass().add("percentage-full");
                            }
                            
                        } catch (NumberFormatException e) {
                            row.getStyleClass().add("percentage-error");
                        }
                    }
                }
            });
            return row;
        });

        displayObligation(null);
    
        tableObligations.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> displayObligation(newValue));
    }

    private String calculateStatus(Obligation obligation) {
        // Exemple : déterminer le statut de l'obligation
        Long capital = obligation.getCapital();
        Long capitalSouscripteur = obligation.getInvestors().stream()
            .mapToLong(investorInfo -> investorInfo.getCapital())
            .sum() * obligation.getValeurNominale();
        Double pourcentRemplissage = (double) capitalSouscripteur / capital * 100;
        if(pourcentRemplissage != 100){
            return String.format("%.2f%%", pourcentRemplissage);
        }
        LocalDate now = LocalDate.now();
        LocalDate startDate = LocalDate.parse(obligation.getStartDate());
        LocalDate endDate = LocalDate.parse(obligation.getEndDate());
        
        if (now.isBefore(startDate)) {
            return "À venir";
        } else if (now.isAfter(endDate)) {
            return "Terminée";
        } else {
            return "En cours";
        }
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
            setNumberLabel(NameObligation, oblig.getName());
            setNumberLabel(capitalObligation, String.valueOf(oblig.getCapital()));
            setNumberLabel(PartObligation, String.valueOf(oblig.getValeurNominale()));
            EmetteurObligation.setText(ApplicantInteractor.GetApplicant(oblig.getApplicantId()).getName());
            if(oblig.getConvertible()) {
                OS_OCAObligation.setText("OCA");
            } else {
                OS_OCAObligation.setText("Obligation Simple");
            }
            ISINObligation.setText(oblig.getIsin());
            DureeObligation.setText(String.valueOf(ChronoUnit.MONTHS.between(LocalDate.parse(oblig.getStartDate()), LocalDate.parse(oblig.getEndDate()))) + " mois");
            DateDebutObligation.setText(oblig.getStartDate());
            DateFinObligation.setText(oblig.getEndDate());
            if(oblig.getRate()[0] != 0.0 && oblig.getRate()[1] != 0.0) {
                TauxObligation.setText(String.valueOf(oblig.getRate()[1]) + "%" + " + " + String.valueOf(oblig.getRate()[0]) + "% INFINE");
            } else {
                TauxObligation.setText("Aucun Taux");
                if(oblig.getRate()[0] != 0.0){
                    TauxObligation.setText(String.valueOf(oblig.getRate()[0]) + "% INFINE");
                } if(oblig.getRate()[1] != 0.0){
                    TauxObligation.setText(String.valueOf(oblig.getRate()[1]) + "%");
                }
            }
            NombreSouscripteurObligation.setText(String.valueOf(oblig.getInvestors().size()));
            if(oblig.getProrogation()[0] == "") {
                ProrogationObligation.setText("Aucune");
                DureeProrogationObligation.setText("");
                TauxProrogationObligation.setText("");
                ProrogationEnCoursObligation.setText("");
            } else {
                ProrogationObligation.setText("Oui");
                DureeProrogationObligation.setText(oblig.getProrogation()[0]);
                TauxProrogationObligation.setText(oblig.getProrogation()[1] +  "%" + " + " + oblig.getProrogation()[2] + "% INFINE");
                ProrogationEnCoursObligation.setText(oblig.getProrogationActivated() ? "Oui" : "Non");
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
            ProrogationEnCoursObligation.setText("");
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
                selectedObligation.getInvestors().forEach((investorInfo) -> {
                    Investor investor = InvestorInteractor.GetInvestor(investorInfo.getInvestorId());
                    investor.removeObligation(selectedObligation.getId());
                    InvestorInteractor.DeleteInvestor(investorInfo.getInvestorId());
                    InvestorInteractor.SaveInvestor(investor);
                });
                ObligationInteractor.DeleteObligation(selectedObligation.getId());
                listOblig.remove(selectedObligation);
                selectedObligation = null;
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mypackage/view/add/AddObligation.fxml"));
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editObligation() {
        if (selectedObligation != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/mypackage/view/edit/EditObligation.fxml"));
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

    @FXML
    private void consultObligation() {
        if (selectedObligation != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/mypackage/view/consult/ConsultObligation.fxml"));
                Parent root = loader.load();
                
                // Récupère le contrôleur lié au FXML (instancié automatiquement)
                ConsultObligationController consultObligationWindow = loader.getController();
                System.out.println("Consulting obligation: " + selectedObligation.getName());
                consultObligationWindow.setObligation(selectedObligation);
                consultObligationWindow.init();

                stage = new Stage();
                stage.setTitle("Consulter une obligation");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL); // bloque la fenêtre principale
                stage.showAndWait(); // attend que la fenêtre se ferme

                if (consultObligationWindow.getResult()) {
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
            System.out.println("No obligation selected to consult.");
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
