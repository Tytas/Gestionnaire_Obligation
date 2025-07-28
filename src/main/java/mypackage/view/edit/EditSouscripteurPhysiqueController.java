package mypackage.view.edit;

import java.time.LocalDate;
import java.util.ArrayList;

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
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import mypackage.model.InvestorNP;
import mypackage.model.Obligation;
import mypackage.model.Family;
import mypackage.model.DataBaseInteractor.InvestorInteractor;
import mypackage.model.DataBaseInteractor.ObligationInteractor;
import mypackage.model.util.NationalityUtils;
import mypackage.model.DataBaseInteractor.FamilyInteractor;
import mypackage.view.util.listviewObjects.TupleStringLongBoolean;

public class EditSouscripteurPhysiqueController {
    private boolean result = false;

    private InvestorNP currentSouscripteur;

    @FXML
    private ToggleGroup sexeToggleGroup;
    @FXML
    private RadioButton MRadioButton;
    @FXML
    private RadioButton MmeRadioButton;
    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private ComboBox<String> nationaliteField;
    @FXML
    private DatePicker dateNaissanceField;
    @FXML
    private TextField lieuNaissanceField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField telephoneField;

    @FXML
    private TextField numeroAdresseField;
    @FXML
    private TextField rueAdresseField;
    @FXML
    private TextField codePostalAdresseField;
    @FXML
    private TextField villeAdresseField;
    @FXML
    private TextField paysAdresseField;
    @FXML
    private TextField complementAdresseField;

    @FXML
    private TextField bicField;
    @FXML
    private TextField ibanField;
    @FXML
    private TextField banqueField;

    @FXML private Label sexeErreurLabel;
    @FXML private Label nomErreurField;
    @FXML private Label prenomErreurField;
    @FXML private Label nationaliteErreurComboBox;
    @FXML private Label dateNaissanceErreurField;
    @FXML private Label lieuNaissanceErreurField;
    @FXML private Label emailErreurField;
    @FXML private Label telephoneErreurField;

    @FXML private Label numeroAdresseErreurField;
    @FXML private Label rueAdresseErreurField;
    @FXML private Label codePostalAdresseErreurField;
    @FXML private Label villeAdresseErreurField;
    @FXML private Label paysAdresseErreurField;
    @FXML private Label complementAdresseErreurField;

    @FXML private Label bicErreurField;
    @FXML private Label ibanErreurField;
    @FXML private Label banqueErreurField;
    @FXML private Label familyErreurLabel;
    
    @FXML
    private ListView<TupleStringLongBoolean> obligationListView;
    @FXML
    private TextField obligationSearchField;
    private ObservableList<TupleStringLongBoolean> allObligations = FXCollections.observableArrayList();
    private FilteredList<TupleStringLongBoolean> filteredObligations;
    private ChangeListener<Boolean> selectionListener;

    @FXML
    private ListView<String> familyListView;
    @FXML
    private TextField familySearchField;
    private ObservableList<String> allFamilies = FXCollections.observableArrayList();
    private FilteredList<String> filteredFamilies;
    public String selectedFamily;

    @FXML
    private Button annulerButton;
    @FXML
    private Button validerButton;

    public boolean getResult() {
        return result;
    }
    public ListView<TupleStringLongBoolean> getObligationListView() {
        return obligationListView;
    }
    public ListView<String> getFamilyListView() {
        return familyListView;
    }

    public EditSouscripteurPhysiqueController() {
        // Constructor logic if needed
    }

    public void initData(InvestorNP souscripteur) {
        this.currentSouscripteur = souscripteur;
        // Set default values for the fields if currentObligation is not null
        System.out.println("Initializing EditEmetteurController with current emetteur: " + currentSouscripteur);
        if (currentSouscripteur != null) {
            try {
                // Initialisation sécurisée des champs
                if (currentSouscripteur.getName() != null) {
                    nomField.setText(currentSouscripteur.getName());
                }
                
                if (currentSouscripteur.getFirstName() != null) {
                    prenomField.setText(currentSouscripteur.getFirstName());
                }
                
                // Gestion sécurisée des boutons radio de sexe
                RadioButton selectedRadioButton = (RadioButton) sexeToggleGroup.getSelectedToggle();
                if (selectedRadioButton != null) {
                    selectedRadioButton.setSelected(false);
                }
                
                if (currentSouscripteur.getCivility() != null) {
                    if (currentSouscripteur.getCivility().equals("M.")) {
                        MRadioButton.setSelected(true);
                    } else if (currentSouscripteur.getCivility().equals("Mme.")) {
                        MmeRadioButton.setSelected(true);
                    }
                }
                
                // Gestion sécurisée de l'adresse
                String[] address = currentSouscripteur.getAddress();
                if (address != null) {
                    if (address.length > 0 && address[0] != null) numeroAdresseField.setText(address[0]);
                    if (address.length > 1 && address[1] != null) rueAdresseField.setText(address[1]);
                    if (address.length > 2 && address[2] != null) codePostalAdresseField.setText(address[2]);
                    if (address.length > 3 && address[3] != null) villeAdresseField.setText(address[3]);
                    if (address.length > 4 && address[4] != null) paysAdresseField.setText(address[4]);
                    if (address.length > 5 && address[5] != null) complementAdresseField.setText(address[5]);
                }
                
                if (currentSouscripteur.getNationality() != null) {
                    nationaliteField.setValue(currentSouscripteur.getNationality());
                }
                
                if (currentSouscripteur.getDateOfBirth() != null && !currentSouscripteur.getDateOfBirth().isEmpty()) {
                    try {
                        dateNaissanceField.setValue(LocalDate.parse(currentSouscripteur.getDateOfBirth()));
                    } catch (Exception e) {
                        System.err.println("Erreur lors du parsing de la date de naissance: " + e.getMessage());
                    }
                }
                
                if (currentSouscripteur.getPlaceOfBirth() != null) {
                    lieuNaissanceField.setText(currentSouscripteur.getPlaceOfBirth());
                }
                
                if (currentSouscripteur.getEmail() != null) {
                    emailField.setText(currentSouscripteur.getEmail());
                }
                
                if (currentSouscripteur.getPhoneNumber() != null) {
                    telephoneField.setText(currentSouscripteur.getPhoneNumber());
                }
                
                if (currentSouscripteur.getBIC() != null) {
                    bicField.setText(currentSouscripteur.getBIC());
                }
                
                if (currentSouscripteur.getIBAN() != null) {
                    ibanField.setText(currentSouscripteur.getIBAN());
                }
                
                if (currentSouscripteur.getBankName() != null) {
                    banqueField.setText(currentSouscripteur.getBankName());
                }
                
                // Gestion sécurisée des obligations
                if (filteredObligations != null && souscripteur.getObligations() != null) {
                    for(TupleStringLongBoolean obligation : filteredObligations) {
                        if (obligation != null) {
                            for(int id : souscripteur.getObligations()) {
                                try {
                                    Obligation obligationObj = ObligationInteractor.GetObligation(id);
                                    if (obligationObj != null && obligationObj.getName() != null && 
                                        obligation != null &&
                                        obligation.getName().equalsIgnoreCase(obligationObj.getName())) {
                                        obligation.setSelectionne(true);
                                        obligation.setCapital(String.valueOf(obligationObj.getInvestorCapital(currentSouscripteur.getId())));
                                        obligation.setDate(obligationObj.getInvestorDate(currentSouscripteur.getId()));
                                    }
                                } catch (Exception e) {
                                    System.err.println("Erreur lors de la récupération de l'obligation ID " + id + ": " + e.getMessage());
                                }
                            }
                        }
                    }
                }
                
                // Gestion sécurisée de la famille
                try {
                    if (currentSouscripteur.getFamilyId() != 0) {
                        Family family = FamilyInteractor.GetFamily(currentSouscripteur.getFamilyId());
                        if (family != null && family.getName() != null && familyListView != null) {
                            for (int i = 0; i < familyListView.getItems().size(); i++) {
                                if (familyListView.getItems().get(i) != null && 
                                    familyListView.getItems().get(i).equals(family.getName())) {
                                    familyListView.getSelectionModel().select(i);
                                    selectedFamily = familyListView.getItems().get(i);
                                    break;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Erreur lors de la récupération de la famille: " + e.getMessage());
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de l'initialisation des données du souscripteur physique: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void initialize() {
        // Initialize the ComboBoxes and other UI elements if needed
        nationaliteField.setItems(NationalityUtils.getNationalities());

        ArrayList<Integer> familyId = FamilyInteractor.GetAllFamiliesId();
        for (Integer id : familyId) {
            Family family = FamilyInteractor.GetFamily(id);
            if (family != null) {
                allFamilies.add(family.getName());
            }
        }
        filteredFamilies = new FilteredList<>(allFamilies, s -> true);
        familyListView.setItems(filteredFamilies);
        familySearchField.textProperty().addListener((obs, oldValue, newValue) -> {
            filteredFamilies.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return item.toLowerCase().contains(lowerCaseFilter);
            });
        });
        familyListView.setOnMouseClicked(event -> {
            // Récupérer l'élément sélectionné dans la ListView
            selectedFamily = familyListView.getSelectionModel().getSelectedItem();

            // Si un élément est sélectionné
            if (selectedFamily != null) {
                System.out.println("Famille sélectionnée : " + selectedFamily);
                // Faites ce que vous voulez avec l'élément sélectionné, par exemple :
                // Vous pouvez également effectuer une autre action basée sur cet élément, comme afficher des détails supplémentaires.
            }
        });

        ArrayList<Integer> obligationId = ObligationInteractor.GetAllObligationsId();
        for (Integer id : obligationId) {
            Obligation obligation = ObligationInteractor.GetObligation(id);
            if (obligation != null) {
                TupleStringLongBoolean tuple = new TupleStringLongBoolean(new SimpleStringProperty(obligation.getName()),
                                                                          new SimpleStringProperty("0"),
                                                                          new SimpleBooleanProperty(false));
                allObligations.add(tuple);
            }
        }
        // Initialize the obligation list view
        filteredObligations = new FilteredList<>(allObligations, p -> true);
        obligationListView.setItems(filteredObligations);

        // Add a listener to the search field to filter obligations
        obligationSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredObligations.setPredicate(obligation -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true; // Show all obligations if search is empty
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return obligation.getName().toLowerCase().contains(lowerCaseFilter);
            });
        });
        obligationListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(TupleStringLongBoolean item, boolean empty) {
                super.updateItem(item, empty);
                CheckBox checkBox = new CheckBox();
                TextField montantField = new TextField();
                DatePicker datePicker = new DatePicker();
                datePicker.setStyle("-fx-pref-width: 115px; -fx-pref-height: 25px; -fx-min-height: 25px;" +
                                     "-fx-max-height: 25px; -fx-font-size: 12px; -fx-padding: 0px;");
                montantField.setStyle("-fx-pref-width: 50px; -fx-pref-height: 25px; -fx-min-height: 25px;" +
                                      "-fx-max-height: 25px; -fx-font-size: 14px; -fx-padding: 0px;");

                // Liaison bidirectionnelle personnalisée pour le DatePicker
                datePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
                    if (newDate != null) {
                        item.setDate(newDate.toString());
                    } else {
                        item.setDate("");
                    }
                });
                
                HBox content = new HBox(10, checkBox, montantField, datePicker);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    checkBox.setText(item.getName());
                    
                    checkBox.selectedProperty().bindBidirectional(item.selectionneProperty());
                    montantField.textProperty().bindBidirectional(item.capitalProperty());

                    if (item.getDate() != null && !item.getDate().isEmpty()) {
                        try {
                            datePicker.setValue(LocalDate.parse(item.getDate()));
                        } catch (Exception e) {
                            datePicker.setValue(null);
                        }
                    } else {
                        datePicker.setValue(null);
                    }
                    // Désactiver les champs si non sélectionné
                    montantField.setDisable(!item.getSelectionne());
                    datePicker.setDisable(!item.getSelectionne());
                    
                    if (selectionListener != null) {
                        item.selectionneProperty().removeListener(selectionListener);
                    }

                    selectionListener = (obs, oldVal, newVal) -> {
                        montantField.setDisable(!newVal);
                        datePicker.setDisable(!newVal);
                    };
                    item.selectionneProperty().addListener(selectionListener);
                    setGraphic(content);
                }
            }
        });

        validerButton.setOnAction(event -> {
            // boolean sexeOK = sexeToggleGroup.getSelectedToggle() != null;
            // if (!sexeOK) {
            //     sexeErreurLabel.setText("Veuillez sélectionner un sexe.");
            //     sexeErreurLabel.setVisible(!sexeOK);
            //     sexeErreurLabel.setStyle("-fx-text-fill: red; -fx-font-size: 10px;");
            // }
            // else {
            //     sexeErreurLabel.setVisible(false);
            // }
            boolean nomOK = validateField(nomField, nomErreurField, "text");
            // boolean prenomOK = validateField(prenomField, prenomErreurField, "text");
            // boolean nationaliteOK = validateField(nationaliteField, nationaliteErreurComboBox, "");
            // boolean dateNaissanceOK = validateField(dateNaissanceField, dateNaissanceErreurField, "");
            // boolean lieuNaissanceOK = validateField(lieuNaissanceField, lieuNaissanceErreurField, "text");
            // boolean emailOK = validateField(emailField, emailErreurField, "email");
            // boolean telephoneOK = validateField(telephoneField, telephoneErreurField, "text");

            // boolean numeroAdresseOK = validateField(numeroAdresseField, numeroAdresseErreurField, "text");
            // boolean rueAdresseOK = validateField(rueAdresseField, rueAdresseErreurField, "text");
            // boolean codePostalOK = validateField(codePostalAdresseField, codePostalAdresseErreurField, "text");
            // boolean villeAdresseOK = validateField(villeAdresseField, villeAdresseErreurField, "text");
            // boolean paysAdresseOK = validateField(paysAdresseField, paysAdresseErreurField, "text");

            // boolean bicOK = validateField(bicField, bicErreurField, "text");
            // boolean ibanOK = validateField(ibanField, ibanErreurField, "text");
            // boolean banqueOK = validateField(banqueField, banqueErreurField, "text");

            boolean familyOK = selectedFamily != null && !selectedFamily.isEmpty();
            if (!familyOK){
                familyErreurLabel.setText("Veuillez sélectionner une family.");
                familyErreurLabel.setVisible(!familyOK);
                familyErreurLabel.setStyle("-fx-text-fill: red; -fx-font-size: 10px;");
            }
            else {
                familyErreurLabel.setVisible(false);
            }

            // Vérification globale - seul le nom est obligatoire
            boolean formulaireValide = nomOK;

            if (formulaireValide) {
                String sexe = (sexeToggleGroup.getSelectedToggle() != null) ? 
                    ((RadioButton) sexeToggleGroup.getSelectedToggle()).getText() : "";
                EditInvestorNP(
                    new SimpleStringProperty(nomField.getText().trim()),
                    sexe,
                    prenomField.getText() != null ? prenomField.getText().trim() : "",
                    nationaliteField.getValue() != null ? nationaliteField.getValue() : "",
                    (dateNaissanceField.getValue() != null) ? dateNaissanceField.getValue().toString() : "",
                    lieuNaissanceField.getText() != null ? lieuNaissanceField.getText().trim() : "",
                    emailField.getText() != null ? emailField.getText().trim() : "",
                    telephoneField.getText() != null ? telephoneField.getText().trim() : "",
                    new String[]{
                        numeroAdresseField.getText() != null ? numeroAdresseField.getText().trim() : "",
                        rueAdresseField.getText() != null ? rueAdresseField.getText().trim() : "",
                        codePostalAdresseField.getText() != null ? codePostalAdresseField.getText().trim() : "",
                        villeAdresseField.getText() != null ? villeAdresseField.getText().trim() : "",
                        paysAdresseField.getText() != null ? paysAdresseField.getText().trim() : "",
                        complementAdresseField.getText() != null ? complementAdresseField.getText().trim() : ""
                    },
                    ibanField.getText() != null ? ibanField.getText().trim() : "", 
                    bicField.getText() != null ? bicField.getText().trim() : "", 
                    banqueField.getText() != null ? banqueField.getText().trim() : "",
                    (selectedFamily != null) ? FamilyInteractor.GetFamilyByName(selectedFamily) : 0
                );
                result = true;
                ((Stage) validerButton.getScene().getWindow()).close();
            } else {
                // Show an error message or alert to the user
                System.out.println("Please fill in all required fields.");
            }
        });

        annulerButton.setOnAction(event -> {
            result = false;
            ((Stage) annulerButton.getScene().getWindow()).close();
        });
    }

    private void EditInvestorNP(SimpleStringProperty name,
                    String civilityBoss, String firstNameBoss, String nationalityBoss,
                    String dateOfBirthBoss, String placeOfBirthBoss, 
                    String emailBoss, String phoneNumberBoss, String[] addressBoss,String IBAN, String BIC, String BankName, int familyId) {
        int id = currentSouscripteur.getId();
        InvestorNP investorNP = new InvestorNP(
                id, civilityBoss, name, firstNameBoss, nationalityBoss,
                dateOfBirthBoss, placeOfBirthBoss,
                emailBoss, phoneNumberBoss, addressBoss, 
                IBAN, BIC, BankName, familyId);
        System.out.println("Creating investorNP with the following details:");
        for (TupleStringLongBoolean obligation : allObligations) {
            if (obligation.getSelectionne()) {
                System.out.println(ObligationInteractor.GetObligationByName(obligation.getName()).getId());
                int obligationId = ObligationInteractor.GetObligationByName(obligation.getName()).getId();
                
                // Vérification que l'obligation n'existe pas déjà dans l'investor
                if (!investorNP.getObligations().contains(obligationId)) {
                    investorNP.addObligation(obligationId);
                }
                
                int idObligation = ObligationInteractor.GetObligationByName(obligation.getName()).getId();
                if (idObligation != -1) {
                    Obligation newObligation = ObligationInteractor.GetObligation(idObligation);
                    
                    // Vérification que l'investisseur avec ce montant n'existe pas déjà dans l'obligation
                    Long investmentAmount = Long.valueOf(obligation.getCapital());
                    boolean investorExists = false;
                    for (mypackage.model.util.InvestorInfo info : newObligation.getInvestors()) {
                        if (info.getInvestorId().equals(id) && 
                            info.getCapital().equals(investmentAmount) && 
                            info.getDate().equals(obligation.getDate())) {
                            investorExists = true;
                            System.out.println("Investor " + id + " with amount " + investmentAmount + " already exists in obligation " + idObligation);
                            break;
                        }
                    }
                    if (!investorExists) {
                        newObligation.removeInvestor(id);
                        newObligation.addInvestor(id, investmentAmount, obligation.getDate());
                    }
                    
                    ObligationInteractor.DeleteObligation(idObligation);
                    ObligationInteractor.SaveObligation(newObligation);
                }
            }
        }
        
        // Gestion de la famille seulement si une famille est sélectionnée
        System.out.println("Selected family: " + selectedFamily);
        if (selectedFamily != null && !selectedFamily.isEmpty()) {
            int familyIdSelected = FamilyInteractor.GetFamilyByName(selectedFamily);
            System.out.println("Family ID selected: " + familyIdSelected);
            investorNP.setFamilyId(familyIdSelected);
            
            // Vérification que familyIdSelected n'est pas 0 avant d'accéder à la famille
            if (familyIdSelected != 0) {
                Family family = FamilyInteractor.GetFamily(familyIdSelected);
                
                // Vérification que l'investisseur n'existe pas déjà dans la famille
                if (family != null && !family.getInvestors().contains(id)) {
                    family.addInvestor(id);
                } else if (family != null) {
                    System.out.println("Investor " + id + " already exists in family " + familyIdSelected);
                }
                
                if (family != null) {
                    FamilyInteractor.DeleteFamily(familyIdSelected);
                    FamilyInteractor.SaveFamily(family);
                }
            }
        } else {
            // Aucune famille sélectionnée, familyId = 0
            investorNP.setFamilyId(0);
        }

        InvestorInteractor.DeleteInvestorNP(id);
        InvestorInteractor.SaveInvestorNP(investorNP);
    }

    private boolean validateField(Control field, Label errorLabel, String expectedType) {
        boolean isValid = true;
    
        if (field instanceof TextField textField) {
            String input = textField.getText().trim();
    
            if (input.isEmpty()) {
                isValid = false;
                errorLabel.setText("Ce champ est requis.");
            } else if (!isValidType(input, expectedType)) {
                isValid = false;
                errorLabel.setText("Format invalide : attendu " + expectedType + ".");
            }
    
        } else if (field instanceof ComboBox<?> comboBox) {
            isValid = comboBox.getValue() != null;
            if (!isValid) {
                errorLabel.setText("Veuillez sélectionner une valeur.");
            }
        } else if (field instanceof DatePicker datePicker) {
            isValid = datePicker.getValue() != null;
            if (!isValid) {
                errorLabel.setText("Veuillez choisir une date.");
            }
        }

        errorLabel.setVisible(!isValid);
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 10px;");
        return isValid;
    }
    private boolean isValidType(String input, String expectedType) {
        switch (expectedType) {
            case "text":
                return true; // Any text is valid
            case "number":
                return input.matches("\\d+"); // Only digits are valid
            case "email":
                return input.matches("^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,}$"); // Simple email validation
            default:
                return false; // Unknown type
        }
    }
    
}
