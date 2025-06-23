package mypackage.view.add.AddObject;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import mypackage.model.Obligation;
import mypackage.model.DataBaseInteractor.ObligationInteractor;
import mypackage.model.DataBaseInteractor.ApplicantInteractor;
import mypackage.model.Applicant;
import mypackage.model.Group;
import mypackage.model.DataBaseInteractor.GroupInteractor;
import mypackage.view.util.GroupeWindow;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;

public class AddEmetteurController {
    private boolean result = false;

    @FXML
    private TextField nomField;
    @FXML
    private ComboBox<String> paysComboBox;
    @FXML
    private TextField numRegistreField;
    @FXML
    private DatePicker dateCreationField;
    @FXML
    private TextField typeEntrepriseField;
    @FXML
    private  ComboBox<String> formeJuridiqueComboBox;

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
    private ToggleGroup sexeDirigeantToggleGroup;
    @FXML
    private TextField nomDirigeantField;
    @FXML
    private TextField prenomDirigeantField;
    @FXML
    private ComboBox<String> nationaliteDirigeantComboBox;
    @FXML
    private DatePicker dateNaissanceDirigeantField;
    @FXML
    private TextField lieuNaissanceDirigeantField;
    @FXML
    private ComboBox<String> langueDirigeantComboBox;
    @FXML
    private TextField emailDirigeantField;
    @FXML
    private TextField telephoneDirigeantField;

    @FXML
    private TextField numeroAdresseDirigeantField;
    @FXML
    private TextField rueAdresseDirigeantField;
    @FXML
    private TextField codePostalAdresseDirigeantField;
    @FXML
    private TextField villeAdresseDirigeantField;
    @FXML
    private TextField paysAdresseDirigeantField;
    @FXML
    private TextField complementAdresseDirigeantField;

    @FXML
    private TextField fonctionDirigeantField;
    @FXML
    private TextField residenceFiscaleDirigeantField;
    @FXML
    private TextField numeroIdentificationDirigeantField;
    @FXML
    private TextField bicField;
    @FXML
    private TextField ibanField;
    @FXML
    private TextField banqueField;

    @FXML private Label nomErreurField;
    @FXML private Label paysErreurComboBox;
    @FXML private Label numRegistreErreurField;
    @FXML private Label dateCreationErreurField;
    @FXML private Label typeEntrepriseErreurField;
    @FXML private Label formeJuridiqueErreurComboBox;

    @FXML private Label numeroAdresseErreurField;
    @FXML private Label rueAdresseErreurField;
    @FXML private Label codePostalAdresseErreurField;
    @FXML private Label villeAdresseErreurField;
    @FXML private Label paysAdresseErreurField;
    @FXML private Label complementAdresseErreurField;

    @FXML private Label sexeDirigeantErreurLabel;
    @FXML private Label nomDirigeantErreurField;
    @FXML private Label prenomDirigeantErreurField;
    @FXML private Label nationaliteDirigeantErreurComboBox;
    @FXML private Label dateNaissanceDirigeantErreurField;
    @FXML private Label lieuNaissanceDirigeantErreurField;
    @FXML private Label langueDirigeantErreurComboBox;
    @FXML private Label emailDirigeantErreurField;
    @FXML private Label telephoneDirigeantErreurField;

    @FXML private Label numeroAdresseDirigeantErreurField;
    @FXML private Label rueAdresseDirigeantErreurField;
    @FXML private Label codePostalAdresseDirigeantErreurField;
    @FXML private Label villeAdresseDirigeantErreurField;
    @FXML private Label paysAdresseDirigeantErreurField;
    @FXML private Label complementAdresseDirigeantErreurField;

    @FXML private Label fonctionDirigeantErreurField;
    @FXML private Label residenceFiscaleDirigeantErreurField;
    @FXML private Label numeroIdentificationDirigeantErreurField;
    @FXML private Label bicErreurField;
    @FXML private Label ibanErreurField;
    @FXML private Label banqueErreurField;
    
    @FXML
    private ListView<String[]> obligationListView;
    @FXML
    private TextField obligationSearchField;
    private ObservableList<String[]> allObligations = FXCollections.observableArrayList();
    private FilteredList<String[]> filteredObligations;

    @FXML
    private ComboBox<String> groupeComboBox;
    @FXML
    private Button nouveauGroupeButton;


    @FXML
    private Button annulerButton;
    @FXML
    private Button validerButton;

    public boolean getResult() {
        return result;
    }
    public ListView<String[]> getObligationListView() {
        return obligationListView;
    }

    public AddEmetteurController() {
        // Constructor logic if needed
    }
    public void initialize() {
        // Initialize the ComboBoxes and other UI elements if needed
        paysComboBox.setItems(FXCollections.observableArrayList("France", "USA", "Germany", "Spain"));
        formeJuridiqueComboBox.setItems(FXCollections.observableArrayList("SARL", "SA", "SAS", "EURL"));
        nationaliteDirigeantComboBox.setItems(FXCollections.observableArrayList("Française", "Américaine", "Allemande", "Espagnole"));
        langueDirigeantComboBox.setItems(FXCollections.observableArrayList("Français", "Anglais", "Allemand", "Espagnol"));

        ArrayList<Integer> obligationId = ObligationInteractor.GetAllObligationsId();
        for (Integer id : obligationId) {
            Obligation obligation = ObligationInteractor.GetObligation(id);
            if (obligation != null) {
                String[] obligationData = new String[2];
                obligationData[0] = obligation.getName();
                obligationData[1] = String.valueOf(false);
                allObligations.add(obligationData);
            }
        }

        nouveauGroupeButton.setOnAction(event -> {
            // Open a new window to create a new group
            GroupeWindow groupeWindow = new GroupeWindow();
            groupeWindow.show();
        });
        // Initialize the groupeComboBox with existing groups
        for (int id : GroupInteractor.GetAllGroupsId()) {
            Group group = GroupInteractor.GetGroup(id);
            if (group != null) {
                groupeComboBox.getItems().add(group.getName());
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
                return obligation[0].toLowerCase().contains(lowerCaseFilter);
            });
        });
        obligationListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String[] item, boolean empty) {
                super.updateItem(item, empty);
                CheckBox checkBox = new CheckBox();
                HBox content = new HBox(10, checkBox);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    checkBox.setText(item[0]);
                    checkBox.setSelected(Boolean.parseBoolean(item[1])); 
                    checkBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                        item[1] = String.valueOf(isNowSelected);
                    });
                setGraphic(content);
                }
            }
        });

        validerButton.setOnAction(event -> {
            boolean nomOK = validateField(nomField, nomErreurField, "text");
            boolean paysOK = validateField(paysComboBox, paysErreurComboBox, "");
            boolean numRegistreOK = validateField(numRegistreField, numRegistreErreurField, "text");
            boolean typeEntrepriseOK = validateField(typeEntrepriseField, typeEntrepriseErreurField, "text");
            boolean formeJuridiqueOK = validateField(formeJuridiqueComboBox, formeJuridiqueErreurComboBox, "");

            boolean numeroAdresseOK = validateField(numeroAdresseField, numeroAdresseErreurField, "text");
            boolean rueAdresseOK = validateField(rueAdresseField, rueAdresseErreurField, "text");
            boolean codePostalAdresseOK = validateField(codePostalAdresseField, codePostalAdresseErreurField, "text");
            boolean villeAdresseOK = validateField(villeAdresseField, villeAdresseErreurField, "text");
            boolean paysAdresseOK = validateField(paysAdresseField, paysAdresseErreurField, "text");
            // Le complément est facultatif ? sinon :
            boolean complementAdresseOK = validateField(complementAdresseField, complementAdresseErreurField, "text");

            boolean sexeDirigeantOK = sexeDirigeantToggleGroup.getSelectedToggle() != null;
            if (!sexeDirigeantOK) sexeDirigeantErreurLabel.setText("Veuillez sélectionner un sexe.");
            sexeDirigeantErreurLabel.setVisible(!sexeDirigeantOK);

            boolean nomDirigeantOK = validateField(nomDirigeantField, nomDirigeantErreurField, "text");
            boolean prenomDirigeantOK = validateField(prenomDirigeantField, prenomDirigeantErreurField, "text");
            boolean nationaliteDirigeantOK = validateField(nationaliteDirigeantComboBox, nationaliteDirigeantErreurComboBox, "");
            boolean dateNaissanceDirigeantOK = validateField(dateNaissanceDirigeantField, dateNaissanceDirigeantErreurField, "");
            boolean lieuNaissanceDirigeantOK = validateField(lieuNaissanceDirigeantField, lieuNaissanceDirigeantErreurField, "text");
            boolean langueDirigeantOK = validateField(langueDirigeantComboBox, langueDirigeantErreurComboBox, "");
            boolean emailDirigeantOK = validateField(emailDirigeantField, emailDirigeantErreurField, "email");
            boolean telephoneDirigeantOK = validateField(telephoneDirigeantField, telephoneDirigeantErreurField, "text");

            boolean numeroAdresseDirigeantOK = validateField(numeroAdresseDirigeantField, numeroAdresseDirigeantErreurField, "text");
            boolean rueAdresseDirigeantOK = validateField(rueAdresseDirigeantField, rueAdresseDirigeantErreurField, "text");
            boolean codePostalAdresseDirigeantOK = validateField(codePostalAdresseDirigeantField, codePostalAdresseDirigeantErreurField, "text");
            boolean villeAdresseDirigeantOK = validateField(villeAdresseDirigeantField, villeAdresseDirigeantErreurField, "text");
            boolean paysAdresseDirigeantOK = validateField(paysAdresseDirigeantField, paysAdresseDirigeantErreurField, "text");
            boolean complementAdresseDirigeantOK = validateField(complementAdresseDirigeantField, complementAdresseDirigeantErreurField, "text");

            boolean fonctionDirigeantOK = validateField(fonctionDirigeantField, fonctionDirigeantErreurField, "text");
            boolean residenceFiscaleDirigeantOK = validateField(residenceFiscaleDirigeantField, residenceFiscaleDirigeantErreurField, "text");
            boolean numeroIdentificationDirigeantOK = validateField(numeroIdentificationDirigeantField, numeroIdentificationDirigeantErreurField, "text");

            boolean bicOK = validateField(bicField, bicErreurField, "text");
            boolean ibanOK = validateField(ibanField, ibanErreurField, "text");
            boolean banqueOK = validateField(banqueField, banqueErreurField, "text");

            // Vérification globale
            boolean formulaireValide =
                nomOK && paysOK && numRegistreOK && typeEntrepriseOK && formeJuridiqueOK &&
                numeroAdresseOK && rueAdresseOK && codePostalAdresseOK && villeAdresseOK && paysAdresseOK && complementAdresseOK &&
                sexeDirigeantOK && nomDirigeantOK && prenomDirigeantOK && nationaliteDirigeantOK && dateNaissanceDirigeantOK &&
                lieuNaissanceDirigeantOK && langueDirigeantOK && emailDirigeantOK && telephoneDirigeantOK &&
                numeroAdresseDirigeantOK && rueAdresseDirigeantOK && codePostalAdresseDirigeantOK && villeAdresseDirigeantOK &&
                paysAdresseDirigeantOK && complementAdresseDirigeantOK &&
                fonctionDirigeantOK && residenceFiscaleDirigeantOK && numeroIdentificationDirigeantOK &&
                bicOK && ibanOK && banqueOK;

            if (formulaireValide) {
                String sexeDirigeant = ((RadioButton) sexeDirigeantToggleGroup.getSelectedToggle()).getText();
                CreateApplicant(
                    new SimpleStringProperty(nomField.getText().trim()),
                    paysComboBox.getValue(),
                    Integer.parseInt(numRegistreField.getText().trim()),
                    dateCreationField.getValue().toString(),
                    typeEntrepriseField.getText().trim(),
                    formeJuridiqueComboBox.getValue(),
                    new String[]{
                        numeroAdresseField.getText().trim(),
                        rueAdresseField.getText().trim(),
                        codePostalAdresseField.getText().trim(),
                        villeAdresseField.getText().trim(),
                        paysAdresseField.getText().trim(),
                        complementAdresseField.getText().trim()
                    },
                    sexeDirigeant,
                    new SimpleStringProperty(nomDirigeantField.getText().trim()),
                    prenomDirigeantField.getText().trim(),
                    nationaliteDirigeantComboBox.getValue(),
                    dateNaissanceDirigeantField.getValue().toString(),
                    lieuNaissanceDirigeantField.getText().trim(),
                    langueDirigeantComboBox.getValue(),
                    emailDirigeantField.getText().trim(),
                    telephoneDirigeantField.getText().trim(),
                    new String[]{
                        numeroAdresseDirigeantField.getText().trim(),
                        rueAdresseDirigeantField.getText().trim(),
                        codePostalAdresseDirigeantField.getText().trim(),
                        villeAdresseDirigeantField.getText().trim(),
                        paysAdresseDirigeantField.getText().trim(),
                        complementAdresseDirigeantField.getText().trim()
                    },
                    residenceFiscaleDirigeantField.getText().trim(),
                    numeroIdentificationDirigeantField.getText().trim(),
                    fonctionDirigeantField.getText().trim(),
                    ibanField.getText().trim(), 
                    bicField.getText().trim(), 
                    banqueField.getText().trim(), 
                    0 // Assuming groupId is managed elsewhere
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

    private void CreateApplicant(SimpleStringProperty name, String country, int registerNumber, String dateOfCreation,
                    String typeOfBusiness, String legalStatus, String[] address,
                    String civilityBoss, SimpleStringProperty nameBoss, String firstNameBoss, String nationalityBoss,
                    String dateOfBirthBoss, String placeOfBirthBoss, String languageBoss,
                    String emailBoss, String phoneNumberBoss, String[] addressBoss, String fiscalcountryBoss,
                    String taxIdNumberBoss, String roleBoss, String IBAN, String BIC, String BankName, int groupId) {
        int newId = ApplicantInteractor.generateNewId();
        Applicant applicant = new Applicant(newId, name, country, registerNumber, dateOfCreation,
                typeOfBusiness, legalStatus, address,
                civilityBoss, nameBoss, firstNameBoss, nationalityBoss,
                dateOfBirthBoss, placeOfBirthBoss, languageBoss,
                emailBoss, phoneNumberBoss, addressBoss, fiscalcountryBoss,
                taxIdNumberBoss, roleBoss, IBAN, BIC, BankName, groupId);
        System.out.println("Creating applicant with the following details:");
        for (String[] obligation : allObligations) {
            if (Boolean.parseBoolean(obligation[1])) {
                System.out.println(ObligationInteractor.GetObligationByName(obligation[0]).getId());
                applicant.addObligation(ObligationInteractor.GetObligationByName(obligation[0]).getId());
                int idObligation = ObligationInteractor.GetObligationByName(obligation[0]).getId();
                if (idObligation != -1) {
                    Obligation newObligation = ObligationInteractor.GetObligation(idObligation);
                    newObligation.setApplicantId(newId);
                    ObligationInteractor.DeleteObligation(idObligation);
                    ObligationInteractor.SaveObligation(newObligation);
                }
            }
        }
        String selectedGroupName = groupeComboBox.getValue();
        int selectedGroupId = GroupInteractor.GetGroupByName(selectedGroupName);
        if (selectedGroupId != -1) {
            Group group = GroupInteractor.GetGroup(selectedGroupId);
            group.addMember(newId);
            GroupInteractor.SaveGroup(group);
            applicant.setGroupId(selectedGroupId);
        }
        ApplicantInteractor.SaveApplicant(applicant);
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
