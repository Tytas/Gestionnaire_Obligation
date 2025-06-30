package mypackage.view.add.AddObject;

import javafx.collections.transformation.FilteredList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ListView;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mypackage.model.Investor;
import mypackage.model.Applicant;
import mypackage.model.Obligation;
import mypackage.model.DataBaseInteractor.ObligationInteractor;
import mypackage.model.DataBaseInteractor.InvestorInteractor;
import mypackage.model.DataBaseInteractor.ApplicantInteractor;
import mypackage.view.util.AmortissementWindow;
import mypackage.view.util.SureteWindow;
import mypackage.view.util.listviewObjects.*;


public class AddObligationController {

    private boolean result = false;

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
    private TextField valeurNominaleField;
    @FXML
    private ToggleGroup isinToggle;
    @FXML
    private RadioButton isinOui;
    @FXML
    private RadioButton isinNon;
    @FXML
    private TextField numeroIsinField;

    @FXML private Label nomErreurField;
    @FXML private Label capitalErreurField;
    @FXML private Label tauxInFineErreurField;
    @FXML private Label tauxTempErreurField;
    @FXML private Label baseCalculErreurComboBox;
    @FXML private Label periodiciteErreurComboBox;
    @FXML private Label dateDebutErreurField;
    @FXML private Label dureeErreurField;
    @FXML private Label tauxProrogationErreurField;
    @FXML private Label dureeProrogationErreurField;
    @FXML private Label emetteurErreurLabel;
    @FXML private Label valeurNominaleErreurField;
    @FXML private Label isinErreurToggle;
    @FXML private Label numeroIsinErreurField;

    @FXML
    private ListView<TupleStringLongBoolean> souscripteurListView;
    @FXML
    private TextField souscripteurSearchField;
    private ObservableList<TupleStringLongBoolean> allSouscripteurs = FXCollections.observableArrayList();
    private FilteredList<TupleStringLongBoolean> filteredSouscripteurs;

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
    private Button validerButton;
    @FXML
    private Button annulerButton;


    public AddObligationController() {
        // Constructeur vide
    }

    public ListView<TupleStringLongBoolean> getSouscripteursListView() {
        return souscripteurListView;
    }

    public ListView<String> getEmetteursListView() {
        return emetteurListView;
    }

    public void initialize() {
        // Initialisation des ComboBox
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
        isinToggle.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == isinOui) {
                numeroIsinField.setDisable(false);
            } else if (newToggle == isinNon) {
                numeroIsinField.setDisable(true);
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

        validerButton.setOnAction(event -> {
            boolean hasError = false;

            String nom = nomField.getText();
            if (nom.isEmpty()) {
                showError(nomErreurField, "Le nom est requis");
                hasError = true;
            } else {
                hideError(nomErreurField);
            }

            String capitalString = capitalField.getText();
            Long capital = null;
            if (capitalString == null || capitalString.isEmpty()) {
                showError(capitalErreurField, "Le capital est requis");
                hasError = true;
            } else {
                try {
                    capital = Long.parseLong(capitalString);
                    hideError(capitalErreurField);
                } catch (NumberFormatException e) {
                    showError(capitalErreurField, "Le capital doit être un nombre");
                    hasError = true;
                }
            }

            String valeurNominaleString = valeurNominaleField.getText();
            Integer valeurNominale = null;
            if (valeurNominaleString == null || valeurNominaleString.isEmpty()) {
                showError(valeurNominaleErreurField, "La valeur nominale est requise");
                hasError = true;
            } else {
                try {
                    valeurNominale = Integer.parseInt(valeurNominaleString);
                    hideError(valeurNominaleErreurField);
                } catch (NumberFormatException e) {
                    showError(valeurNominaleErreurField, "La valeur nominale doit être un nombre");
                    hasError = true;
                }
            }

            String tauxTempString = taux_TEMP.getText();
            Integer tauxTemp = null;
            if (tauxTempString != null && !tauxTempString.isEmpty()) {
                try {
                    tauxTemp = Integer.parseInt(tauxTempString);
                    if (tauxTemp < 0 || tauxTemp > 100) {
                        showError(tauxTempErreurField, "Le taux TEMP doit être un pourcentage entre 0 et 100");
                        hasError = true;
                    }
                    hideError(tauxInFineErreurField);
                } catch (NumberFormatException e) {
                    showError(tauxInFineErreurField, "Le taux IN FINE doit être un pourcentage");
                    hasError = true;
                }
            }

            String tauxInFineString = taux_INFINE.getText();
            int tauxInFine = 0;
            if (tauxInFineString != null && !tauxInFineString.isEmpty()) {
                try {
                    tauxInFine = Integer.parseInt(tauxInFineString);
                    if (tauxInFine < 0 || tauxInFine > 100) {
                        showError(tauxInFineErreurField, "Le taux IN FINE doit être un pourcentage entre 0 et 100");
                        hasError = true;
                    }
                    hideError(tauxInFineErreurField);
                } catch (NumberFormatException e) {
                    showError(tauxInFineErreurField, "Le taux IN FINE doit être un pourcentage");
                    hasError = true;
                }
            }

            String dureeString = dureeField.getText();
            Integer duree = null;
            if (dureeString == null || dureeString.isEmpty()) {
                showError(dureeErreurField, "Durée requise");
                hasError = true;
            } else {
                try {
                    duree = Integer.parseInt(dureeString);
                    hideError(dureeErreurField);
                } catch (NumberFormatException e) {
                    showError(dureeErreurField, "Durée invalide");
                    hasError = true;
                }
            }

            String baseCalcul = baseCalculComboBox.getValue();
            if (baseCalcul == null || baseCalcul.isEmpty()) {
                showError(baseCalculErreurComboBox, "Base requise");
                hasError = true;
            } else {
                hideError(baseCalculErreurComboBox);
            }

            String periodicite = periodiciteComboBox.getValue();
            if (periodicite == null || periodicite.isEmpty()) {
                showError(periodiciteErreurComboBox, "Périodicité requise");
                hasError = true;
            } else {
                hideError(periodiciteErreurComboBox);
            }

            String dateDebutString = null;
            if (dateDebutField.getValue() == null) {
                showError(dateDebutErreurField, "Date requise");
                hasError = true;
            } else {
                dateDebutString = dateDebutField.getValue().toString();
                hideError(dateDebutErreurField);
            }

            RadioButton selectedType = (RadioButton) convertible.getSelectedToggle();
            boolean isConvertible = selectedType != null && "OCA".equals(selectedType.getText());

            Toggle selectedProrogation = prorogation.getSelectedToggle();
            boolean isProrogation = selectedProrogation != null &&
                                ((RadioButton) selectedProrogation).getText().equalsIgnoreCase("Oui");
            System.out.println("isProrogation: " + isProrogation);
            if (isProrogation) {
                if (TauxProrogationField.getText().trim().isEmpty()) {
                    showError(tauxProrogationErreurField, "Taux requis");
                    hasError = true;
                } else {
                    hideError(tauxProrogationErreurField);
                }

                if (DureeProrogationField.getText().trim().isEmpty()) {
                    showError(dureeProrogationErreurField, "Durée requise");
                    hasError = true;
                } else {
                    hideError(dureeProrogationErreurField);
                }
            } else {
                hideError(tauxProrogationErreurField);
                hideError(dureeProrogationErreurField);
            }

            Toggle selectedIsin = isinToggle.getSelectedToggle();
            boolean isIsin = selectedIsin != null &&
                                ((RadioButton) selectedIsin).getText().equalsIgnoreCase("Oui");
            System.out.println("isIsin: " + isIsin);
            String numeroIsin = "";
            if (isIsin) {
                if (numeroIsinField.getText().trim().isEmpty()) {
                    showError(numeroIsinErreurField, "Numéro ISIN requis");
                    hasError = true;
                } else {
                    numeroIsin = numeroIsinField.getText().trim();
                    hideError(numeroIsinErreurField);
                }
            } else {
                hideError(numeroIsinErreurField);
            }

            String emetteur = this.getEmetteursListView().getSelectionModel().getSelectedItem();
            if (emetteur == null || emetteur.isEmpty()) {
                showError(emetteurErreurLabel, "Émetteur requis");
                hasError = true;
            } else {
                hideError(emetteurErreurLabel);
            }

            if (!hasError) {
                CreateObligation(nom, capital, valeurNominale, new int[]{tauxInFine, tauxTemp}, isConvertible,
                    baseCalcul, periodicite, isProrogation, TauxProrogationField.getText(), DureeProrogationField.getText(),
                    numeroIsin, duree, dateDebutString, allSouscripteurs, emetteur, suretes, amortissements);
                result = true;
                ((Stage) validerButton.getScene().getWindow()).close();
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

    private void showError(Label label, String message) {
        label.setText(message);
        label.setVisible(true);
        label.setStyle("-fx-text-fill: red; -fx-font-size: 10px;");
    }

    private void hideError(Label label) {
        label.setVisible(false);
    }


    private void CreateObligation(String nom, Long capital, Integer valeurNominale, int[] taux, Boolean isConvertible,
                                  String baseCalcul, String periodicite, Boolean isProrogation,
                                  String tauxProrogation, String dureeProrogation, String numeroIsin, Integer duree,
                                  String dateDebut, ObservableList<TupleStringLongBoolean> souscripteursList, String emetteurName,
                                  ObservableList<String> suretes, ObservableList<String[]> amortissements) {
        int newId = ObligationInteractor.generateNewId(); // Generate a new ID for the obligation
        int idApplicant = ApplicantInteractor.GetApplicantByName(selectedEmetteur);
        if (idApplicant == -1) {
            System.out.println("Emetteur not found: " + selectedEmetteur);
            return;
        }
        Map<String, Integer> amortissementsMap = new HashMap<>();
        for(String[] amortissement : amortissements) {
            if (amortissement.length == 2) {
                String dateAmortissement = amortissement[0];
                int montantAmortissement = Integer.parseInt(amortissement[1]);
                amortissementsMap.put(dateAmortissement, montantAmortissement);
            } else {
                System.out.println("Invalid amortissement format: " + amortissement);
            }
        }
        Obligation obligation = new Obligation(newId, new SimpleStringProperty(nom), isConvertible, capital, valeurNominale, dateDebut, duree, taux, baseCalcul, periodicite,
                                                new String[]{tauxProrogation, dureeProrogation}, numeroIsin, new ArrayList<>(suretes), amortissementsMap, idApplicant);
        for (TupleStringLongBoolean souscripteur : souscripteursList) {
            System.out.println("Adding investor: " + souscripteur.getName());
            if (souscripteur.getName() != null && !souscripteur.getName().isEmpty() && souscripteur.getSelectionne()) {
                System.out.println("Investor name: " + souscripteur.getName());
                int idInvestor = InvestorInteractor.GetInvestorByName(souscripteur.getName());
                if (idInvestor == -1) {
                    System.out.println("Investor not found: " + souscripteur.getName());
                    continue; // Skip this investor if not found
                }
                obligation.addInvestor(idInvestor, Long.valueOf(souscripteur.getCapital()));
            }
        }
        ObligationInteractor.SaveObligation(obligation);
        Applicant newApplicant = ApplicantInteractor.GetApplicant(idApplicant);
        newApplicant.addObligation(obligation.getId());
        ApplicantInteractor.DeleteApplicant(idApplicant);
        ApplicantInteractor.SaveApplicant(newApplicant);

        for (TupleStringLongBoolean souscripteur : souscripteursList) {
            if( souscripteur.getSelectionne()){
                int idInvestor = InvestorInteractor.GetInvestorByName(souscripteur.getName());
                if (idInvestor != -1) {
                    Investor newInvestor = InvestorInteractor.GetInvestor(idInvestor);
                    newInvestor.addObligation(obligation.getId());
                    InvestorInteractor.DeleteInvestor(idInvestor);
                    InvestorInteractor.SaveInvestor(newInvestor);
                }
            }
        }
    }
}
