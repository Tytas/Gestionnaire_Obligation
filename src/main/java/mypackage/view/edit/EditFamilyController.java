package mypackage.view.edit;

import java.time.LocalDate;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import mypackage.model.Family;
import mypackage.model.DataBaseInteractor.FamilyInteractor;
import mypackage.view.util.ContactWindow;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class EditFamilyController {
    private boolean result = false;

    private Family currentFamily;

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
    private TextField fonctionDirigeantField;
    @FXML
    private TextField villeRCSField;
    @FXML
    private TextField bicField;
    @FXML
    private TextField ibanField;
    @FXML
    private TextField banqueField;
    @FXML
    private TextField nbApproovalField;
    @FXML
    private ComboBox<String> distributionTypeField;

    @FXML
    private Button addContactButton;
    @FXML
    private ListView<String[]> contactsListView;
    private ObservableList<String[]> contacts = FXCollections.observableArrayList();

    @FXML private Label nomErreurField;
    @FXML private Label numRegistreErreurField;
    @FXML private Label dateCreationErreurField;
    @FXML private Label capitalSocialErreurField;
    @FXML private Label formeJuridiqueErreurComboBox;
    @FXML private Label familyTypeErreurComboBox;
    @FXML private Label nbApproovalErreurField;
    @FXML private Label distributionTypeErreurField;

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

    @FXML private Label fonctionDirigeantErreurField;
    @FXML private Label villeRCSErreurField;
    @FXML private Label bicErreurField;
    @FXML private Label ibanErreurField;
    @FXML private Label banqueErreurField;
    
    @FXML
    private Button annulerButton;
    @FXML
    private Button validerButton;

    public boolean getResult() {
        return result;
    }

    public EditFamilyController() {
        // Constructor logic if needed
    }

    public void initData(Family family) {
        this.currentFamily = family;
        // Set default values for the fields if currentObligation is not null
        System.out.println("Initializing EditFamilyController with current family: " + currentFamily);
        if (currentFamily != null) {
            nomField.setText(currentFamily.getName());
            numRegistreField.setText(String.valueOf(currentFamily.getRegisterNumber()));
            dateCreationField.setValue(LocalDate.parse(currentFamily.getDateOfCreation()));
            capitalSocialField.setText(currentFamily.getSocialCapital());
            formeJuridiqueComboBox.setValue(currentFamily.getLegalStatus());
            nbApproovalField.setText(String.valueOf(currentFamily.getNbAprooval()));
            distributionTypeField.setValue(currentFamily.getdistributionType());
            numeroAdresseField.setText(currentFamily.getAddress()[0]);
            rueAdresseField.setText(currentFamily.getAddress()[1]);
            codePostalAdresseField.setText(currentFamily.getAddress()[2]);
            villeAdresseField.setText(currentFamily.getAddress()[3]);
            paysAdresseField.setText(currentFamily.getAddress()[4]);
            complementAdresseField.setText(currentFamily.getAddress()[5]);
            if (currentFamily.getCivilityBoss() != null) {
                if( currentFamily.getCivilityBoss().equals("M.")) {
                    sexeDirigeantToggleGroup.selectToggle(sexeDirigeantToggleGroup.getToggles().get(0)); // Monsieur
                } else if (currentFamily.getCivilityBoss().equals("Mme.")) {
                    sexeDirigeantToggleGroup.selectToggle(sexeDirigeantToggleGroup.getToggles().get(1)); // Madame
                } else {
                    sexeDirigeantToggleGroup.selectToggle(null); // Aucun
                }
            }
            nomDirigeantField.setText(currentFamily.getNameBoss());
            prenomDirigeantField.setText(currentFamily.getFirstNameBoss());
            nationaliteDirigeantComboBox.setValue(currentFamily.getNationalityBoss());
            dateNaissanceDirigeantField.setValue(LocalDate.parse(currentFamily.getDateOfBirthBoss()));
            lieuNaissanceDirigeantField.setText(currentFamily.getPlaceOfBirthBoss());
            emailDirigeantField.setText(currentFamily.getEmailBoss());
            telephoneDirigeantField.setText(currentFamily.getPhoneNumberBoss());
            fonctionDirigeantField.setText(currentFamily.getRoleBoss());
            villeRCSField.setText(currentFamily.getvilleRCS());
            bicField.setText(currentFamily.getBIC());
            ibanField.setText(currentFamily.getIBAN());
            banqueField.setText(currentFamily.getBankName());
            for (String[] contact : currentFamily.getContacts()) {
                contacts.add(contact);
            }
        }
    }

    
    public void initialize() {
        // Initialize the ComboBoxes and other UI elements if needed
        formeJuridiqueComboBox.setItems(FXCollections.observableArrayList("SARL", "SAS", "SA"));
        nationaliteDirigeantComboBox.setItems(FXCollections.observableArrayList("Française", "Américaine", "Allemande", "Espagnole"));
        distributionTypeField.setItems(FXCollections.observableArrayList("CIF", "PSI", "BANQUE"));

        contactsListView.setItems(contacts);
        contactsListView.setCellFactory(lv -> new ListCell<String[]>() {
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
                    contacts.remove(item);
                    contactsListView.refresh();
                });
                Label montantField = new Label("Nom: " + item[0] + ", Prénom: " + item[1] + ", Email: " + item[2] + ", Téléphone: " + item[3]);
                HBox content = new HBox(10, montantField, deleteButton);
                setGraphic(content);
            }
        });
        addContactButton.setOnAction(e -> {
            // Ouvre la fenêtre pour ajouter un contact
            boolean success = ContactWindow.showDialog(contacts);
            if (success) {
                // Le nouveau contact a été ajouté à la liste
                contactsListView.refresh();
            }
        });

        validerButton.setOnAction(event -> {
            boolean nomOK = validateField(nomField, nomErreurField, "text");
            boolean numRegistreOK = validateField(numRegistreField, numRegistreErreurField, "text");
            boolean capitalSocialOK = validateField(capitalSocialField, capitalSocialErreurField, "text");
            boolean formeJuridiqueOK = validateField(formeJuridiqueComboBox, formeJuridiqueErreurComboBox, "");
            boolean distributionTypeOK = validateField(distributionTypeField, distributionTypeErreurField, "");


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
            
            boolean fonctionDirigeantOK = validateField(fonctionDirigeantField, fonctionDirigeantErreurField, "text");
            boolean villeRCSOK = validateField(villeRCSField, villeRCSErreurField, "text");
            boolean banqueOK = validateField(banqueField, banqueErreurField, "text");

            // Vérification globale
            boolean formulaireValide =
                nomOK && numRegistreOK && capitalSocialOK && formeJuridiqueOK &&
                numeroAdresseOK && rueAdresseOK && codePostalAdresseOK && villeAdresseOK && paysAdresseOK &&
                sexeDirigeantOK && nomDirigeantOK && prenomDirigeantOK && nationaliteDirigeantOK && dateNaissanceDirigeantOK &&
                lieuNaissanceDirigeantOK && emailDirigeantOK && telephoneDirigeantOK &&
                distributionTypeOK && fonctionDirigeantOK && villeRCSOK && banqueOK;

            if (formulaireValide) {
                String sexeDirigeant = "";

                sexeDirigeant = ((RadioButton) sexeDirigeantToggleGroup.getSelectedToggle()).getText();
                System.out.println("Selected sexeDirigeant: " + sexeDirigeant);

                EditFamily(
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
                    villeRCSField.getText().trim(),
                    fonctionDirigeantField.getText().trim(),
                    ibanField.getText().trim(), 
                    bicField.getText().trim(), 
                    banqueField.getText().trim(), 
                    nbApproovalField.getText().trim(),
                    distributionTypeField.getValue(),
                    contacts
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

    private void EditFamily(SimpleStringProperty name, int registerNumber, String dateOfCreation,
                    String SocialCapital, String legalStatus, String[] address,
                    String civilityBoss, SimpleStringProperty nameBoss, String firstNameBoss, String nationalityBoss,
                    String dateOfBirthBoss, String placeOfBirthBoss,
                    String emailBoss, String phoneNumberBoss, String villeRCSBoss,
                    String roleBoss, String IBAN, String BIC, String BankName,
                    String nbAprooval, String distributionType, ObservableList<String[]> contacts) {
        int Id = currentFamily.getId();
        Family family = new Family(Id, name, registerNumber, dateOfCreation,
                SocialCapital, legalStatus, address,
                civilityBoss, nameBoss, firstNameBoss, nationalityBoss,
                dateOfBirthBoss, placeOfBirthBoss,
                emailBoss, phoneNumberBoss, villeRCSBoss,
                roleBoss, IBAN, BIC, BankName,
                nbAprooval, distributionType);
        System.out.println("Creating family with the following details:");
        if(contacts != null) {
            for (String[] contact : contacts) {
                if (contact != null && contact.length >= 4) {
                    // Vérification que le contact n'existe pas déjà
                    boolean contactExists = false;
                    for (String[] existingContact : family.getContacts()) {
                        if (existingContact.length >= 4 && 
                            existingContact[0].equals(contact[0]) && 
                            existingContact[1].equals(contact[1]) && 
                            existingContact[2].equals(contact[2]) && 
                            existingContact[3].equals(contact[3])) {
                            contactExists = true;
                            System.out.println("Contact " + contact[0] + " " + contact[1] + " already exists");
                            break;
                        }
                    }
                    
                    if (!contactExists) {
                        family.addContacts(contact);
                        System.out.println("Contact added: " + contact[0] + " " + contact[1] + ", Email: " + contact[2] + ", Phone: " + contact[3]);
                    }
                } else {
                    System.out.println("Invalid contact data: " + (contact != null ? String.join(", ", contact) : "null"));
                }
            }
        } else {
            System.out.println("No contacts provided.");
        }
        family.setInvestors(FamilyInteractor.GetFamily(Id).getInvestors());

        FamilyInteractor.DeleteFamily(Id);
        FamilyInteractor.SaveFamily(family);
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
