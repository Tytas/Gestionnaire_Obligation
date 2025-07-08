package mypackage.view.edit;

import java.time.LocalDate;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import mypackage.model.Applicant;
import mypackage.model.Group;
import mypackage.model.DataBaseInteractor.ApplicantInteractor;
import mypackage.model.DataBaseInteractor.GroupInteractor;
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
            nomField.setText(currentEmetteur.getName());
            numRegistreField.setText(String.valueOf(currentEmetteur.getRegisterNumber()));
            dateCreationField.setValue(LocalDate.parse(currentEmetteur.getDateOfCreation()));
            capitalSocialField.setText(currentEmetteur.getsocialCapital());
            formeJuridiqueComboBox.setValue(currentEmetteur.getLegalStatus());
            numeroAdresseField.setText(currentEmetteur.getAddress()[0]);
            rueAdresseField.setText(currentEmetteur.getAddress()[1]);
            codePostalAdresseField.setText(currentEmetteur.getAddress()[2]);
            villeAdresseField.setText(currentEmetteur.getAddress()[3]);
            paysAdresseField.setText(currentEmetteur.getAddress()[4]);
            complementAdresseField.setText(currentEmetteur.getAddress()[5]);
            if (currentEmetteur.getCivilityBoss() != null) {
                if( currentEmetteur.getCivilityBoss().equals("M.")) {
                    sexeDirigeantToggleGroup.selectToggle(sexeDirigeantToggleGroup.getToggles().get(0)); // Monsieur
                } else if (currentEmetteur.getCivilityBoss().equals("Mme.")) {
                    sexeDirigeantToggleGroup.selectToggle(sexeDirigeantToggleGroup.getToggles().get(1)); // Madame
                } else {
                    sexeDirigeantToggleGroup.selectToggle(null); // Aucun
                }
            }
            nomDirigeantField.setText(currentEmetteur.getNameBoss());
            prenomDirigeantField.setText(currentEmetteur.getFirstNameBoss());
            nationaliteDirigeantComboBox.setValue(currentEmetteur.getNationalityBoss());
            dateNaissanceDirigeantField.setValue(LocalDate.parse(currentEmetteur.getDateOfBirthBoss()));
            lieuNaissanceDirigeantField.setText(currentEmetteur.getPlaceOfBirthBoss());
            emailDirigeantField.setText(currentEmetteur.getEmailBoss());
            telephoneDirigeantField.setText(currentEmetteur.getPhoneNumberBoss());
            numeroAdresseDirigeantField.setText(currentEmetteur.getAddressBoss()[0]);
            rueAdresseDirigeantField.setText(currentEmetteur.getAddressBoss()[1]);
            codePostalAdresseDirigeantField.setText(currentEmetteur.getAddressBoss()[2]);
            villeAdresseDirigeantField.setText(currentEmetteur.getAddressBoss()[3]);
            paysAdresseDirigeantField.setText(currentEmetteur.getAddressBoss()[4]);
            complementAdresseDirigeantField.setText(currentEmetteur.getAddressBoss()[5]);
            fonctionDirigeantField.setText(currentEmetteur.getRoleBoss());
            villeRCSField.setText(currentEmetteur.getcityRCS());
            bicField.setText(currentEmetteur.getBIC());
            ibanField.setText(currentEmetteur.getIBAN());
            banqueField.setText(currentEmetteur.getBankName());
            if(currentEmetteur.getGroupId() != 0)
                groupeComboBox.setValue(GroupInteractor.GetGroup(currentEmetteur.getGroupId()).getName());
            else
                groupeComboBox.setValue("Aucun Groupe");
        }
    }

    
    public void initialize() {
        // Initialize the ComboBoxes and other UI elements if needed
        formeJuridiqueComboBox.setItems(FXCollections.observableArrayList("SARL", "SA", "SAS"));
        nationaliteDirigeantComboBox.setItems(FXCollections.observableArrayList("Française", "Américaine", "Allemande", "Espagnole"));

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
            boolean numRegistreOK = validateField(numRegistreField, numRegistreErreurField, "text");
            boolean capitalSocialOK = validateField(capitalSocialField, capitalSocialErreurField, "text");
            boolean formeJuridiqueOK = validateField(formeJuridiqueComboBox, formeJuridiqueErreurComboBox, "");

            boolean numeroAdresseOK = validateField(numeroAdresseField, numeroAdresseErreurField, "text");
            boolean rueAdresseOK = validateField(rueAdresseField, rueAdresseErreurField, "text");
            boolean codePostalAdresseOK = validateField(codePostalAdresseField, codePostalAdresseErreurField, "text");
            boolean villeAdresseOK = validateField(villeAdresseField, villeAdresseErreurField, "text");
            boolean paysAdresseOK = validateField(paysAdresseField, paysAdresseErreurField, "text");

            boolean sexeDirigeantOK = sexeDirigeantToggleGroup.getSelectedToggle() != null;
            if (!sexeDirigeantOK) sexeDirigeantErreurLabel.setText("Veuillez sélectionner un sexe.");
            sexeDirigeantErreurLabel.setVisible(!sexeDirigeantOK);

            boolean nomDirigeantOK = validateField(nomDirigeantField, nomDirigeantErreurField, "text");
            boolean prenomDirigeantOK = validateField(prenomDirigeantField, prenomDirigeantErreurField, "text");
            boolean nationaliteDirigeantOK = validateField(nationaliteDirigeantComboBox, nationaliteDirigeantErreurComboBox, "");
            boolean dateNaissanceDirigeantOK = validateField(dateNaissanceDirigeantField, dateNaissanceDirigeantErreurField, "");
            boolean lieuNaissanceDirigeantOK = validateField(lieuNaissanceDirigeantField, lieuNaissanceDirigeantErreurField, "text");
            boolean emailDirigeantOK = validateField(emailDirigeantField, emailDirigeantErreurField, "email");
            boolean telephoneDirigeantOK = validateField(telephoneDirigeantField, telephoneDirigeantErreurField, "text");

            boolean numeroAdresseDirigeantOK = validateField(numeroAdresseDirigeantField, numeroAdresseDirigeantErreurField, "text");
            boolean rueAdresseDirigeantOK = validateField(rueAdresseDirigeantField, rueAdresseDirigeantErreurField, "text");
            boolean codePostalAdresseDirigeantOK = validateField(codePostalAdresseDirigeantField, codePostalAdresseDirigeantErreurField, "text");
            boolean villeAdresseDirigeantOK = validateField(villeAdresseDirigeantField, villeAdresseDirigeantErreurField, "text");
            boolean paysAdresseDirigeantOK = validateField(paysAdresseDirigeantField, paysAdresseDirigeantErreurField, "text");
            
            boolean fonctionDirigeantOK = validateField(fonctionDirigeantField, fonctionDirigeantErreurField, "text");
            boolean villeRCSOK = validateField(villeRCSField, villeRCSErreurField, "text");

            boolean bicOK = validateField(bicField, bicErreurField, "text");
            boolean ibanOK = validateField(ibanField, ibanErreurField, "text");
            boolean banqueOK = validateField(banqueField, banqueErreurField, "text");

            // Vérification globale
            boolean formulaireValide =
                nomOK && numRegistreOK && capitalSocialOK && formeJuridiqueOK &&
                numeroAdresseOK && rueAdresseOK && codePostalAdresseOK && villeAdresseOK && paysAdresseOK &&
                sexeDirigeantOK && nomDirigeantOK && prenomDirigeantOK && nationaliteDirigeantOK && dateNaissanceDirigeantOK &&
                lieuNaissanceDirigeantOK && emailDirigeantOK && telephoneDirigeantOK &&
                numeroAdresseDirigeantOK && rueAdresseDirigeantOK && codePostalAdresseDirigeantOK && villeAdresseDirigeantOK &&
                paysAdresseDirigeantOK && fonctionDirigeantOK && villeRCSOK && bicOK && ibanOK && banqueOK;

            if (formulaireValide) {
                String sexeDirigeant = "";

                sexeDirigeant = ((RadioButton) sexeDirigeantToggleGroup.getSelectedToggle()).getText();
                System.out.println("Selected sexeDirigeant: " + sexeDirigeant);

                EditApplicant(
                    new SimpleStringProperty(nomField.getText().trim()),
                    Integer.parseInt(numRegistreField.getText().trim()),
                    dateCreationField.getValue().toString(),
                    capitalSocialField.getText().trim(),
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
                    villeRCSField.getText().trim(),
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

    private void EditApplicant(SimpleStringProperty name, int registerNumber, String dateOfCreation,
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
