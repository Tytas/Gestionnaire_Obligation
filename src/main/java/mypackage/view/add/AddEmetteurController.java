package mypackage.view.add;


import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import mypackage.view.util.GroupeWindow;
import mypackage.model.DataBaseInteractor.ApplicantInteractor;
import mypackage.model.Applicant;
import mypackage.model.Group;
import mypackage.model.DataBaseInteractor.GroupInteractor;
import mypackage.model.util.NationalityUtils;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;

public class AddEmetteurController {
    private boolean result = false;

    @FXML
    private TextField nomField;
    @FXML
    private TextField numRegistreField;
    @FXML
    private DatePicker dateCreationField;
    @FXML
    private TextField capitalSocialField;
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
    private TextField villeRCSField;
    @FXML
    private TextField bicField;
    @FXML
    private TextField ibanField;
    @FXML
    private TextField banqueField;

    @FXML private Label nomErreurField;
    @FXML private Label numRegistreErreurField;
    @FXML private Label dateCreationErreurField;
    @FXML private Label capitalSocialErreurField;
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
    @FXML private Label emailDirigeantErreurField;
    @FXML private Label telephoneDirigeantErreurField;

    @FXML private Label numeroAdresseDirigeantErreurField;
    @FXML private Label rueAdresseDirigeantErreurField;
    @FXML private Label codePostalAdresseDirigeantErreurField;
    @FXML private Label villeAdresseDirigeantErreurField;
    @FXML private Label paysAdresseDirigeantErreurField;
    @FXML private Label complementAdresseDirigeantErreurField;

    @FXML private Label fonctionDirigeantErreurField;
    @FXML private Label villeRCSErreurField;
    @FXML private Label numeroIdentificationDirigeantErreurField;
    @FXML private Label bicErreurField;
    @FXML private Label ibanErreurField;
    @FXML private Label banqueErreurField;
    
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
    public AddEmetteurController() {
        // Constructor logic if needed
    }
    public void initialize() {
        // Initialize the ComboBoxes and other UI elements if needed
        formeJuridiqueComboBox.setItems(NationalityUtils.getLegalForms());
        nationaliteDirigeantComboBox.setItems(NationalityUtils.getNationalities());

        nouveauGroupeButton.setOnAction(event -> {
            // Open a new window to create a new group
            GroupeWindow groupeWindow = new GroupeWindow();
            groupeWindow.show();
        });
        // Initialize the groupeComboBox with existing groups
        for (int id : GroupInteractor.GetAllGroupsId()) {
            Group group = GroupInteractor.GetGroup(id);
            System.out.println("Group : " + group);
            if (group != null) {
                groupeComboBox.getItems().add(group.getName());
            }
        }


        validerButton.setOnAction(event -> {
            boolean nomOK = validateField(nomField, nomErreurField, "text");
            // boolean numRegistreOK = validateField(numRegistreField, numRegistreErreurField, "text");
            // boolean capitalSocialOK = validateField(capitalSocialField, capitalSocialErreurField, "text");
            // boolean formeJuridiqueOK = validateField(formeJuridiqueComboBox, formeJuridiqueErreurComboBox, "");

            // boolean numeroAdresseOK = validateField(numeroAdresseField, numeroAdresseErreurField, "text");
            // boolean rueAdresseOK = validateField(rueAdresseField, rueAdresseErreurField, "text");
            // boolean codePostalAdresseOK = validateField(codePostalAdresseField, codePostalAdresseErreurField, "text");
            // boolean villeAdresseOK = validateField(villeAdresseField, villeAdresseErreurField, "text");
            // boolean paysAdresseOK = validateField(paysAdresseField, paysAdresseErreurField, "text");
            // Le complément est facultatif ? sinon :
            // boolean complementAdresseOK = validateField(complementAdresseField, complementAdresseErreurField, "text");

            // boolean sexeDirigeantOK = sexeDirigeantToggleGroup.getSelectedToggle() != null;
            // if (!sexeDirigeantOK) sexeDirigeantErreurLabel.setText("Veuillez sélectionner un sexe.");
            // sexeDirigeantErreurLabel.setVisible(!sexeDirigeantOK);

            // boolean nomDirigeantOK = validateField(nomDirigeantField, nomDirigeantErreurField, "text");
            // boolean prenomDirigeantOK = validateField(prenomDirigeantField, prenomDirigeantErreurField, "text");
            // boolean nationaliteDirigeantOK = validateField(nationaliteDirigeantComboBox, nationaliteDirigeantErreurComboBox, "");
            // boolean dateNaissanceDirigeantOK = validateField(dateNaissanceDirigeantField, dateNaissanceDirigeantErreurField, "");
            // boolean lieuNaissanceDirigeantOK = validateField(lieuNaissanceDirigeantField, lieuNaissanceDirigeantErreurField, "text");
            // boolean emailDirigeantOK = validateField(emailDirigeantField, emailDirigeantErreurField, "email");
            // boolean telephoneDirigeantOK = validateField(telephoneDirigeantField, telephoneDirigeantErreurField, "text");

            // boolean numeroAdresseDirigeantOK = validateField(numeroAdresseDirigeantField, numeroAdresseDirigeantErreurField, "text");
            // boolean rueAdresseDirigeantOK = validateField(rueAdresseDirigeantField, rueAdresseDirigeantErreurField, "text");
            // boolean codePostalAdresseDirigeantOK = validateField(codePostalAdresseDirigeantField, codePostalAdresseDirigeantErreurField, "text");
            // boolean villeAdresseDirigeantOK = validateField(villeAdresseDirigeantField, villeAdresseDirigeantErreurField, "text");
            // boolean paysAdresseDirigeantOK = validateField(paysAdresseDirigeantField, paysAdresseDirigeantErreurField, "text");
            
            // boolean fonctionDirigeantOK = validateField(fonctionDirigeantField, fonctionDirigeantErreurField, "text");
            // boolean villeRCSOK = validateField(villeRCSField, villeRCSErreurField, "text");
            
            // boolean bicOK = validateField(bicField, bicErreurField, "text");
            // boolean ibanOK = validateField(ibanField, ibanErreurField, "text");
            // boolean banqueOK = validateField(banqueField, banqueErreurField, "text");

            // Vérification globale - seul le nom est obligatoire
            boolean formulaireValide = nomOK;

            if (formulaireValide) {
                String sexeDirigeant = (sexeDirigeantToggleGroup.getSelectedToggle() != null) ? 
                    ((RadioButton) sexeDirigeantToggleGroup.getSelectedToggle()).getText() : "";
                CreateApplicant(
                    new SimpleStringProperty(nomField.getText().trim()),
                    (numRegistreField.getText() != null && !numRegistreField.getText().trim().isEmpty()) ? 
                        numRegistreField.getText().trim() : "",
                    (dateCreationField.getValue() != null) ? dateCreationField.getValue().toString() : "",
                    capitalSocialField.getText() != null ? capitalSocialField.getText().trim() : "",
                    formeJuridiqueComboBox.getValue() != null ? formeJuridiqueComboBox.getValue() : "",
                    new String[]{
                        numeroAdresseField.getText() != null ? numeroAdresseField.getText().trim() : "",
                        rueAdresseField.getText() != null ? rueAdresseField.getText().trim() : "",
                        codePostalAdresseField.getText() != null ? codePostalAdresseField.getText().trim() : "",
                        villeAdresseField.getText() != null ? villeAdresseField.getText().trim() : "",
                        paysAdresseField.getText() != null ? paysAdresseField.getText().trim() : "",
                        complementAdresseField.getText() != null ? complementAdresseField.getText().trim() : ""
                    },
                    sexeDirigeant,
                    new SimpleStringProperty(nomDirigeantField.getText() != null ? nomDirigeantField.getText().trim() : ""),
                    prenomDirigeantField.getText() != null ? prenomDirigeantField.getText().trim() : "",
                    nationaliteDirigeantComboBox.getValue() != null ? nationaliteDirigeantComboBox.getValue() : "",
                    (dateNaissanceDirigeantField.getValue() != null) ? dateNaissanceDirigeantField.getValue().toString() : "",
                    lieuNaissanceDirigeantField.getText() != null ? lieuNaissanceDirigeantField.getText().trim() : "",
                    emailDirigeantField.getText() != null ? emailDirigeantField.getText().trim() : "",
                    telephoneDirigeantField.getText() != null ? telephoneDirigeantField.getText().trim() : "",
                    new String[]{
                        numeroAdresseDirigeantField.getText() != null ? numeroAdresseDirigeantField.getText().trim() : "",
                        rueAdresseDirigeantField.getText() != null ? rueAdresseDirigeantField.getText().trim() : "",
                        codePostalAdresseDirigeantField.getText() != null ? codePostalAdresseDirigeantField.getText().trim() : "",
                        villeAdresseDirigeantField.getText() != null ? villeAdresseDirigeantField.getText().trim() : "",
                        paysAdresseDirigeantField.getText() != null ? paysAdresseDirigeantField.getText().trim() : "",
                        complementAdresseDirigeantField.getText() != null ? complementAdresseDirigeantField.getText().trim() : ""
                    },
                    villeRCSField.getText() != null ? villeRCSField.getText().trim() : "",
                    fonctionDirigeantField.getText() != null ? fonctionDirigeantField.getText().trim() : "",
                    ibanField.getText() != null ? ibanField.getText().trim() : "", 
                    bicField.getText() != null ? bicField.getText().trim() : "", 
                    banqueField.getText() != null ? banqueField.getText().trim() : "", 
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

    private void CreateApplicant(SimpleStringProperty name, String registerNumber, String dateOfCreation,
                    String socialCapital, String legalStatus, String[] address,
                    String civilityBoss, SimpleStringProperty nameBoss, String firstNameBoss, String nationalityBoss,
                    String dateOfBirthBoss, String placeOfBirthBoss,
                    String emailBoss, String phoneNumberBoss, String[] addressBoss, String cityRCS,
                    String roleBoss, String IBAN, String BIC, String BankName, int groupId) {
        int newId = ApplicantInteractor.generateNewId();
        Applicant applicant = new Applicant(newId, name, registerNumber, dateOfCreation,
                socialCapital, legalStatus, address,
                civilityBoss, nameBoss, firstNameBoss, nationalityBoss,
                dateOfBirthBoss, placeOfBirthBoss,
                emailBoss, phoneNumberBoss, addressBoss, cityRCS,
                roleBoss, IBAN, BIC, BankName, groupId);
        System.out.println("Creating applicant with the following details:");
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
