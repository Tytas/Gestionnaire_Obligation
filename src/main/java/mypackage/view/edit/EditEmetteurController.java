package mypackage.view.edit;

import java.time.LocalDate;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import mypackage.model.Applicant;
import mypackage.model.Group;
import mypackage.model.DataBaseInteractor.ApplicantInteractor;
import mypackage.model.DataBaseInteractor.GroupInteractor;
import mypackage.model.util.NationalityUtils;
import mypackage.view.util.GroupeWindow;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;

public class EditEmetteurController {
    private boolean result = false;

    private Applicant currentEmetteur;

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
    public EditEmetteurController() {
        // Constructor logic if needed
    }

    public void initData(Applicant emetteur) {
        this.currentEmetteur = emetteur;
        // Set default values for the fields if currentObligation is not null
        System.out.println("Initializing EditEmetteurController with current emetteur: " + currentEmetteur);
        if (currentEmetteur != null) {
            try {
                // Initialisation sécurisée des champs
                if (currentEmetteur.getName() != null) {
                    nomField.setText(currentEmetteur.getName());
                }
                
                numRegistreField.setText(currentEmetteur.getRegisterNumber());
                
                if (currentEmetteur.getDateOfCreation() != null && !currentEmetteur.getDateOfCreation().isEmpty()) {
                    try {
                        dateCreationField.setValue(LocalDate.parse(currentEmetteur.getDateOfCreation()));
                    } catch (Exception e) {
                        System.err.println("Erreur lors du parsing de la date de création: " + e.getMessage());
                    }
                }
                
                if (currentEmetteur.getsocialCapital() != null) {
                    capitalSocialField.setText(currentEmetteur.getsocialCapital());
                }
                
                if (currentEmetteur.getLegalStatus() != null) {
                    formeJuridiqueComboBox.setValue(currentEmetteur.getLegalStatus());
                }
                
                // Gestion sécurisée de l'adresse
                String[] address = currentEmetteur.getAddress();
                if (address != null) {
                    if (address.length > 0 && address[0] != null) numeroAdresseField.setText(address[0]);
                    if (address.length > 1 && address[1] != null) rueAdresseField.setText(address[1]);
                    if (address.length > 2 && address[2] != null) codePostalAdresseField.setText(address[2]);
                    if (address.length > 3 && address[3] != null) villeAdresseField.setText(address[3]);
                    if (address.length > 4 && address[4] != null) paysAdresseField.setText(address[4]);
                    if (address.length > 5 && address[5] != null) complementAdresseField.setText(address[5]);
                }
                
                if (currentEmetteur.getCivilityBoss() != null) {
                    if( currentEmetteur.getCivilityBoss().equals("M.")) {
                        sexeDirigeantToggleGroup.selectToggle(sexeDirigeantToggleGroup.getToggles().get(0)); // Monsieur
                    } else if (currentEmetteur.getCivilityBoss().equals("Mme.")) {
                        sexeDirigeantToggleGroup.selectToggle(sexeDirigeantToggleGroup.getToggles().get(1)); // Madame
                    } else {
                        sexeDirigeantToggleGroup.selectToggle(null); // Aucun
                    }
                }
                
                if (currentEmetteur.getNameBoss() != null) {
                    nomDirigeantField.setText(currentEmetteur.getNameBoss());
                }
                
                if (currentEmetteur.getFirstNameBoss() != null) {
                    prenomDirigeantField.setText(currentEmetteur.getFirstNameBoss());
                }
                
                if (currentEmetteur.getNationalityBoss() != null) {
                    nationaliteDirigeantComboBox.setValue(currentEmetteur.getNationalityBoss());
                }
                
                if (currentEmetteur.getDateOfBirthBoss() != null && !currentEmetteur.getDateOfBirthBoss().isEmpty()) {
                    try {
                        dateNaissanceDirigeantField.setValue(LocalDate.parse(currentEmetteur.getDateOfBirthBoss()));
                    } catch (Exception e) {
                        System.err.println("Erreur lors du parsing de la date de naissance du dirigeant: " + e.getMessage());
                    }
                }
                
                if (currentEmetteur.getPlaceOfBirthBoss() != null) {
                    lieuNaissanceDirigeantField.setText(currentEmetteur.getPlaceOfBirthBoss());
                }
                
                if (currentEmetteur.getEmailBoss() != null) {
                    emailDirigeantField.setText(currentEmetteur.getEmailBoss());
                }
                
                if (currentEmetteur.getPhoneNumberBoss() != null) {
                    telephoneDirigeantField.setText(currentEmetteur.getPhoneNumberBoss());
                }
                
                // Gestion sécurisée de l'adresse du dirigeant
                String[] addressBoss = currentEmetteur.getAddressBoss();
                if (addressBoss != null) {
                    if (addressBoss.length > 0 && addressBoss[0] != null) numeroAdresseDirigeantField.setText(addressBoss[0]);
                    if (addressBoss.length > 1 && addressBoss[1] != null) rueAdresseDirigeantField.setText(addressBoss[1]);
                    if (addressBoss.length > 2 && addressBoss[2] != null) codePostalAdresseDirigeantField.setText(addressBoss[2]);
                    if (addressBoss.length > 3 && addressBoss[3] != null) villeAdresseDirigeantField.setText(addressBoss[3]);
                    if (addressBoss.length > 4 && addressBoss[4] != null) paysAdresseDirigeantField.setText(addressBoss[4]);
                    if (addressBoss.length > 5 && addressBoss[5] != null) complementAdresseDirigeantField.setText(addressBoss[5]);
                }
                
                if (currentEmetteur.getRoleBoss() != null) {
                    fonctionDirigeantField.setText(currentEmetteur.getRoleBoss());
                }
                
                if (currentEmetteur.getcityRCS() != null) {
                    villeRCSField.setText(currentEmetteur.getcityRCS());
                }
                
                if (currentEmetteur.getBIC() != null) {
                    bicField.setText(currentEmetteur.getBIC());
                }
                
                if (currentEmetteur.getIBAN() != null) {
                    ibanField.setText(currentEmetteur.getIBAN());
                }
                
                if (currentEmetteur.getBankName() != null) {
                    banqueField.setText(currentEmetteur.getBankName());
                }
                
                // Gestion sécurisée du groupe
                try {
                    if(currentEmetteur.getGroupId() != 0) {
                        Group group = GroupInteractor.GetGroup(currentEmetteur.getGroupId());
                        if (group != null && group.getName() != null) {
                            groupeComboBox.setValue(group.getName());
                        } else {
                            groupeComboBox.setValue("Aucun Groupe");
                        }
                    } else {
                        groupeComboBox.setValue("Aucun Groupe");
                    }
                } catch (Exception e) {
                    System.err.println("Erreur lors de la récupération du groupe: " + e.getMessage());
                    groupeComboBox.setValue("Aucun Groupe");
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de l'initialisation des données de l'émetteur: " + e.getMessage());
                e.printStackTrace();
            }
        }
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

            // Vérification globale - seule la raison sociale est obligatoire
            boolean formulaireValide = nomOK;

            if (formulaireValide) {
                String sexeDirigeant = (sexeDirigeantToggleGroup.getSelectedToggle() != null) ? 
                    ((RadioButton) sexeDirigeantToggleGroup.getSelectedToggle()).getText() : "";
                System.out.println("Selected sexeDirigeant: " + sexeDirigeant);

                EditApplicant(
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

    private void EditApplicant(SimpleStringProperty name, String registerNumber, String dateOfCreation,
                    String socialCapital, String legalStatus, String[] address,
                    String civilityBoss, SimpleStringProperty nameBoss, String firstNameBoss, String nationalityBoss,
                    String dateOfBirthBoss, String placeOfBirthBoss,
                    String emailBoss, String phoneNumberBoss, String[] addressBoss, String cityRCS,
                    String roleBoss, String IBAN, String BIC, String BankName, int groupId) {
        int Id = currentEmetteur.getId();
        Applicant applicant = new Applicant(Id, name, registerNumber, dateOfCreation,
                socialCapital, legalStatus, address,
                civilityBoss, nameBoss, firstNameBoss, nationalityBoss,
                dateOfBirthBoss, placeOfBirthBoss,
                emailBoss, phoneNumberBoss, addressBoss, cityRCS,
                roleBoss, IBAN, BIC, BankName, groupId);
        
        String selectedGroupName = groupeComboBox.getValue();
        int selectedGroupId = GroupInteractor.GetGroupByName(selectedGroupName);
        if (selectedGroupId != -1) {
            Group group = GroupInteractor.GetGroup(selectedGroupId);
            group.addMember(Id);
            GroupInteractor.DeleteGroup(selectedGroupId);
            GroupInteractor.SaveGroup(group);
            applicant.setGroupId(selectedGroupId);
        }
        ApplicantInteractor.DeleteApplicant(Id);
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
