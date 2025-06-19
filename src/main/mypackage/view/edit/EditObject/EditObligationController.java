package mypackage.view.edit.EditObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import mypackage.model.Applicant;
import mypackage.model.Investor;
import mypackage.model.Obligation;
import mypackage.model.DataBaseInteractor.ApplicantInteractor;
import mypackage.model.DataBaseInteractor.InvestorInteractor;
import mypackage.model.DataBaseInteractor.ObligationInteractor;
import mypackage.model.util.Replacement;
import mypackage.view.util.AmortissementWindow;
import mypackage.view.util.SureteWindow;
import mypackage.view.util.cessionWindow;
import mypackage.view.util.listviewObjects.TupleStringLongBoolean;
import mypackage.view.util.listviewObjects.TupleStringMapMap;

public class EditObligationController {

    private boolean result = false;
    private Obligation currentObligation;
    @FXML
    private TextField nomField;
    @FXML
    private TextField capitalField;
    @FXML
    private ToggleGroup convertible;
    @FXML
    private TextField taux_INFINE;
    @FXML
    private TextField taux_TEMP;
    @FXML
    private ComboBox<String> baseCalculComboBox;
    @FXML
    private ComboBox<String> periodiciteComboBox;
    @FXML
    private DatePicker dateDebutField;
    @FXML
    private TextField dureeField; 
    @FXML
    private ToggleGroup prorogation;
    @FXML
    private RadioButton prorogationOui;
    @FXML
    private RadioButton prorogationNon;
    @FXML
    private TextField TauxProrogationField; 
    @FXML
    private TextField DureeProrogationField; 

    @FXML
    private ListView<TupleStringLongBoolean> souscripteurListView;
    @FXML
    private TextField souscripteurSearchField;
    private ObservableList<TupleStringLongBoolean> allSouscripteurs = FXCollections.observableArrayList();
    private FilteredList<TupleStringLongBoolean> filteredSouscripteurs;
    private ArrayList<TupleStringLongBoolean> selectedSouscripteurs = new ArrayList<>();

    private ChangeListener<Boolean> selectionListener;

    @FXML
    private ListView<String> emetteurListView;
    @FXML
    private TextField emetteurSearchField;
    private ObservableList<String> allEmetteurs = FXCollections.observableArrayList();
    private FilteredList<String> filteredEmetteurs;
    public String selectedEmetteur;

    @FXML
    private Button addAmortissementButton;
    @FXML
    private ListView<String[]> amortissementsListView;
    private ObservableList<String[]> amortissements = FXCollections.observableArrayList();

    @FXML
    private Button addSureteButton;
    @FXML
    private ListView<String> suretesListView;
    private ObservableList<String> suretes = FXCollections.observableArrayList();

    @FXML
    private Button addcessionButton;
    @FXML
    private ListView<TupleStringMapMap> cessionListView;
    private ObservableList<TupleStringMapMap> cessions = FXCollections.observableArrayList();

    @FXML
    private Button validerButton;
    @FXML
    private Button annulerButton;

    public EditObligationController() {
        // Constructor can be used for initialization if needed
    }

    public Obligation getCurrentObligation() {
        return currentObligation;
    }
    public void setCurrentObligation(Obligation newObligation) {
        this.currentObligation = newObligation;
    }

    public ListView<TupleStringLongBoolean> getSouscripteursListView() {
        return souscripteurListView;
    }

    public ListView<String> getEmetteursListView() {
        return emetteurListView;
    }

    public void initData(Obligation obligation) {
        this.currentObligation = obligation;
        // Set default values for the fields if currentObligation is not null
        System.out.println("Initializing EditObligationController with current obligation: " + currentObligation);
        if (currentObligation != null) {
            nomField.setText(currentObligation.getName());
            capitalField.setText(String.valueOf(currentObligation.getCapital()));
            dureeField.setText(String.valueOf(currentObligation.getDurationMonths()));
            baseCalculComboBox.setValue(currentObligation.getInterestBase());
            periodiciteComboBox.setValue(currentObligation.getPeriodicity());
            dateDebutField.setValue(LocalDate.parse(currentObligation.getStartDate()));
        }
    }

    @FXML
    private void initialize() { 
        baseCalculComboBox.getItems().addAll("30/360", "Jour Réel/365");
        periodiciteComboBox.getItems().addAll("Mensuelle", "Trimestrielle", "Semestrielle", "Annuelle");
        ArrayList<Integer> investorsId = InvestorInteractor.GetAllInvestorId();
        for (Integer id : investorsId) {
            Investor investor = InvestorInteractor.GetInvestorNP(id);
            if (investor != null) {
                TupleStringLongBoolean tuple = new TupleStringLongBoolean(new SimpleStringProperty(investor.getName()), 
                                                                          new SimpleStringProperty("0"), 
                                                                          new SimpleBooleanProperty(false));
                allSouscripteurs.add(tuple);
            }
        }
        prorogation.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == prorogationOui) {
                TauxProrogationField.setDisable(false);
                DureeProrogationField.setDisable(false);
            } else if (newToggle == prorogationNon) {
                TauxProrogationField.setDisable(true);
                DureeProrogationField.setDisable(true);
            }
        });
        filteredSouscripteurs = new FilteredList<>(allSouscripteurs, s -> true);
        souscripteurListView.setItems(filteredSouscripteurs);
        souscripteurSearchField.textProperty().addListener((obs, oldValue, newValue) -> {
            filteredSouscripteurs.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                return item.getName().toLowerCase().contains(lowerCaseFilter);
            });
        });
        souscripteurListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(TupleStringLongBoolean item, boolean empty) {
                super.updateItem(item, empty);
                CheckBox checkBox = new CheckBox();
                TextField montantField = new TextField();
                HBox content = new HBox(10, checkBox, montantField);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    checkBox.setText(item.getName());
                    checkBox.selectedProperty().unbind();
                    montantField.textProperty().unbindBidirectional(item.getCapital());

                    checkBox.selectedProperty().bindBidirectional(item.selectionneProperty());
                    montantField.textProperty().bindBidirectional(item.capitalProperty());

                    montantField.setDisable(!item.getSelectionne());
                    if (selectionListener != null) {
                        item.selectionneProperty().removeListener(selectionListener);
                    }

                    selectionListener = (obs, oldVal, newVal) -> {
                        montantField.setDisable(!newVal);
                        if (newVal) {
                            if (!selectedSouscripteurs.contains(item)) {
                                selectedSouscripteurs.add(item);
                            }
                        } else {
                            selectedSouscripteurs.remove(item);
                        }
                    };
                    item.selectionneProperty().addListener(selectionListener);
                    setGraphic(content);
                }
            }
        });

        ArrayList<Integer> applicantId = ApplicantInteractor.GetAllApplicantsId();
        for (Integer id : applicantId) {
            Applicant applicant = ApplicantInteractor.GetApplicant(id);
            if (applicant != null) {
                allEmetteurs.add(applicant.getName());
            }
        }
        filteredEmetteurs = new FilteredList<>(allEmetteurs, s -> true);
        emetteurListView.setItems(filteredEmetteurs);
        emetteurSearchField.textProperty().addListener((obs, oldValue, newValue) -> {
            filteredEmetteurs.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return item.toLowerCase().contains(lowerCaseFilter);
            });
        });
        emetteurListView.setOnMouseClicked(event -> {
            // Récupérer l'élément sélectionné dans la ListView
            selectedEmetteur = emetteurListView.getSelectionModel().getSelectedItem();
            
            // Si un élément est sélectionné
            if (selectedEmetteur != null) {
                System.out.println("Emetteur sélectionné : " + selectedEmetteur);
                // Faites ce que vous voulez avec l'élément sélectionné, par exemple :
                // Vous pouvez également effectuer une autre action basée sur cet élément, comme afficher des détails supplémentaires.
            }
        });

        amortissementsListView.setItems(amortissements);
        amortissementsListView.setCellFactory(lv -> new ListCell<String[]>() {
            @Override
            protected void updateItem(String[] item, boolean empty) {
                super.updateItem(item, empty);
                // Si item est nul ou si c'est une cellule vide, on ne fait rien
                if (empty || item == null || item.length < 2) {
                    setGraphic(null);
                    return;
                }
                // Crée le bouton de suppression
                Button deleteButton = new Button("-");
                deleteButton.setOnAction(e -> {
                    amortissements.remove(item);
                    amortissementsListView.refresh();
                });
                Label montantField = new Label("Année: " + item[0] + ", Taux: " + item[1] + " %");
                HBox content = new HBox(10, montantField, deleteButton);
                setGraphic(content);
            }
        });
        addAmortissementButton.setOnAction(e -> {
            // Ouvre la fenêtre pour ajouter un amortissement
            boolean success = AmortissementWindow.showDialog(amortissements);
            if (success) {
                // Le nouveau amortissement a été ajouté à la liste
                amortissementsListView.refresh();
            }
        });

        suretesListView.setItems(suretes);
        suretesListView.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                // Si item est nul ou si c'est une cellule vide, on ne fait rien
                if (empty || item == null || item.isEmpty()) {
                    setGraphic(null);
                    return;
                }
                // Crée le bouton de suppression
                Button deleteButton = new Button("-");
                deleteButton.setOnAction(e -> {
                    suretes.remove(item);
                    suretesListView.refresh();
                });
                Label montantField = new Label(item);
                HBox content = new HBox(10, montantField, deleteButton);
                setGraphic(content);
            }
        });
        addSureteButton.setOnAction(e -> {
            // Ouvre la fenêtre pour ajouter une sureté
            boolean success = SureteWindow.showDialog(suretes);
            if (success) {
                // Le nouveau sureté a été ajouté à la liste
                suretesListView.refresh();
            }
        });

        cessionListView.setItems(cessions);
        cessionListView.setCellFactory(lv -> new ListCell<TupleStringMapMap>() {
            @Override
            protected void updateItem(TupleStringMapMap item, boolean empty) {
                super.updateItem(item, empty);
                // Si item est nul ou si c'est une cellule vide, on ne fait rien
                if (empty || item == null) {
                    setGraphic(null);
                    return;
                }
                // Crée le bouton de suppression
                Button deleteButton = new Button("-");
                deleteButton.setOnAction(e -> {
                    cessions.remove(item);
                    cessionListView.refresh();
                });
                Label cessionField = new Label("Date: " + item.getDate());
                for(int i = 0; i < item.getVendeurs().size(); i++) {
                    cessionField.setText(cessionField.getText() + ", Vendeur: " + item.getVendeurs().keySet().toArray()[i] + " (" + item.getVendeurs().values().toArray()[i] + ")");
                }
                for(int i = 0; i < item.getAcheteurs().size(); i++) {
                    cessionField.setText(cessionField.getText() + ", Acheteur: " + item.getAcheteurs().keySet().toArray()[i] + " (" + item.getAcheteurs().values().toArray()[i] + ")");
                }
                HBox content = new HBox(10, cessionField, deleteButton);
                setGraphic(content);
            }
        });
        addcessionButton.setOnAction(e -> {
            // Ouvre la fenêtre pour ajouter une cession
            boolean success = cessionWindow.showDialog(cessions, selectedSouscripteurs);
            if (success) {
                // La nouvelle cession a été ajoutée à la liste
                cessionListView.refresh();
            }
        });

        validerButton.setOnAction(event -> {
            String nom = nomField.getText();
            String capitalString = capitalField.getText();
            String dureeString = dureeField.getText();
            Boolean isConvertible = false;
            int[] taux = {0, 0}; // [In Fine, mensuelle]
            Long capital = null;
            Integer duree = null;
            String baseCalcul = baseCalculComboBox.getValue();
            String periodicite = periodiciteComboBox.getValue();
            Boolean isProrogation = false;
            String dateDebutString = null;
            if(dateDebutField.getValue() != null){
                dateDebutString = dateDebutField.getValue().toString();
            }
            String emetteur = this.getEmetteursListView().getSelectionModel().getSelectedItem();

            RadioButton selectedType = (RadioButton) convertible.getSelectedToggle();
            if (selectedType != null && "OCA".equals(selectedType.getText())) {
                isConvertible = true;
                System.out.println("OCA selected");
            }
            RadioButton selectedProrogation = (RadioButton) prorogation.getSelectedToggle();
            if (selectedProrogation != null && selectedProrogation == prorogationOui) {
                isProrogation = true;
                System.out.println("prorogationOui selected");
            }

            if (capitalString != null && !capitalString.isEmpty()) {
                try {
                    capital = Long.parseLong(capitalString);
                } catch (NumberFormatException e) {
                    System.out.println("Capital must be a number");
                    return;
                }
            }
            if (dureeString != null && !dureeString.isEmpty()) {
                try {
                    duree = Integer.parseInt(dureeString);
                } catch (NumberFormatException e) {
                    System.out.println("Duration must be a number");
                    return;
                }
            }
            

            if (!nom.isEmpty() && capital != null && taux != null && isConvertible != null && selectedType != null
                && !baseCalcul.isEmpty() && !periodicite.isEmpty() && isProrogation != null && duree != null) {
                if((isProrogation == true && !TauxProrogationField.getText().trim().isEmpty() && !DureeProrogationField.getText().trim().isEmpty()) 
                    || isProrogation == false) {
                    CreateObligation(nom, capital, taux, isConvertible, baseCalcul, periodicite, isProrogation,
                                     TauxProrogationField.getText(), DureeProrogationField.getText(), duree, dateDebutString,
                                     selectedSouscripteurs, emetteur, suretes, amortissements);
                    result = true;
                    ((Stage) validerButton.getScene().getWindow()).close();
                }
            }
        });

        annulerButton.setOnAction(event -> {
            result = false;
            ((Stage) annulerButton.getScene().getWindow()).close();
        });
    }

    public boolean getResult() {
        return result;
    }

    private void CreateObligation(String nom, Long capital, int[] taux, Boolean isConvertible, 
                                  String baseCalcul, String periodicite, Boolean isProrogation, 
                                  String tauxProrogation, String dureeProrogation, Integer duree,
                                  String dateDebut, ArrayList<TupleStringLongBoolean> souscripteursList, String emetteurName,
                                  ObservableList<String> suretes, ObservableList<String[]> amortissements) {
        int newId = ObligationInteractor.generateNewId(); // Generate a new ID for the obligation
        int idApplicant = ApplicantInteractor.GetApplicantByName(selectedEmetteur);
        if (idApplicant == -1) {
            System.out.println("Emetteur not found: " + selectedEmetteur);
            return;
        }
        Obligation obligation = new Obligation(newId, new SimpleStringProperty(nom), isConvertible, capital, dateDebut, duree, taux, baseCalcul, periodicite,
                                                new String[]{tauxProrogation, dureeProrogation}, new ArrayList<>(suretes), idApplicant);
        for (TupleStringLongBoolean souscripteur : souscripteursList) {  
            if (souscripteur.getName() != null && !souscripteur.getName().isEmpty()) {
                int idInvestor = InvestorInteractor.GetInvestorByName(souscripteur.getName());
                if (idInvestor == -1) {
                    System.out.println("Investor not found: " + souscripteur.getName());
                    continue; // Skip this investor if not found
                }
                obligation.addInvestor(idInvestor, Long.valueOf(souscripteur.getCapital()));
            }
        }
        for (TupleStringMapMap cession : cessions) {
            if (cession.getDate() != null && !cession.getVendeurs().isEmpty() && !cession.getAcheteurs().isEmpty()) {
                Map<Integer, Long> vendeurs = new HashMap<>();
                Map<Integer, Long> acheteurs = new HashMap<>();
                for (Map.Entry<String, Long> entry : cession.getVendeurs().entrySet()) {
                    int idVendeur = InvestorInteractor.GetInvestorByName(entry.getKey());
                    if (idVendeur != -1) {
                        vendeurs.put(idVendeur, entry.getValue());
                    } else {
                        System.out.println("Vendeur not found: " + entry.getKey());
                    }
                }
                for (Map.Entry<String, Long> entry : cession.getAcheteurs().entrySet()) {
                    int idAcheteur = InvestorInteractor.GetInvestorByName(entry.getKey());
                    if (idAcheteur != -1) {
                        acheteurs.put(idAcheteur, entry.getValue());
                    } else {
                        System.out.println("Acheteur not found: " + entry.getKey());
                    }
                }
                Replacement replacement = new Replacement(cession.getDate(), vendeurs, acheteurs);
                obligation.addReplacement(replacement);
            }
        }

        ObligationInteractor.SaveObligation(obligation);
    }

}