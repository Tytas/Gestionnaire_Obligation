package mypackage.view.edit;

import java.time.LocalDate;
import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
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
import mypackage.model.InvestorLP;
import mypackage.model.Obligation;
import mypackage.model.Family;
import mypackage.model.DataBaseInteractor.InvestorInteractor;
import mypackage.model.DataBaseInteractor.ObligationInteractor;
import mypackage.model.DataBaseInteractor.FamilyInteractor;

public class EditSouscripteurMoralController {
    private boolean result = false;
    private InvestorLP currentSouscripteur;

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
    @FXML private Label paysErreurComboBox;
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
    @FXML private Label bicErreurField;
    @FXML private Label ibanErreurField;
    @FXML private Label banqueErreurField;
    @FXML private Label familyErreurLabel;
    
    @FXML
    private ListView<String[]> obligationListView;
    @FXML
    private TextField obligationSearchField;
    private ObservableList<String[]> allObligations = FXCollections.observableArrayList();
    private FilteredList<String[]> filteredObligations;

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
    public ListView<String[]> getObligationListView() {
        return obligationListView;
    }
    public ListView<String> getFamilyListView() {
        return familyListView;
    }

    public EditSouscripteurMoralController() {
        // Constructor logic if needed
    }

    public void initData(InvestorLP souscripteur) {
        this.currentSouscripteur = souscripteur;
        // Set default values for the fields if currentObligation is not null
        System.out.println("Initializing EditEmetteurController with current emetteur: " + currentSouscripteur);
        if (currentSouscripteur != null) {
            try {
                // Initialisation sécurisée des champs
                if (currentSouscripteur.getName() != null) {
                    nomField.setText(currentSouscripteur.getName());
                }
                
                numRegistreField.setText(String.valueOf(currentSouscripteur.getRegisterNumber()));
                
                if (currentSouscripteur.getDateOfCreation() != null && !currentSouscripteur.getDateOfCreation().isEmpty()) {
                    try {
                        dateCreationField.setValue(LocalDate.parse(currentSouscripteur.getDateOfCreation()));
                    } catch (Exception e) {
                        System.err.println("Erreur lors du parsing de la date de création: " + e.getMessage());
                    }
                }
                
                if (currentSouscripteur.getcapitalSocial() != null) {
                    capitalSocialField.setText(currentSouscripteur.getcapitalSocial());
                }
                
                if (currentSouscripteur.getLegalStatus() != null) {
                    formeJuridiqueComboBox.setValue(currentSouscripteur.getLegalStatus());
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
                
                if (currentSouscripteur.getCivilityBoss() != null) {
                    if( currentSouscripteur.getCivilityBoss().equals("M.")) {
                        sexeDirigeantToggleGroup.selectToggle(sexeDirigeantToggleGroup.getToggles().get(0)); // Monsieur
                    } else if (currentSouscripteur.getCivilityBoss().equals("Mme.")) {
                        sexeDirigeantToggleGroup.selectToggle(sexeDirigeantToggleGroup.getToggles().get(1)); // Madame
                    } else {
                        sexeDirigeantToggleGroup.selectToggle(null); // Aucun
                    }
                }
                
                if (currentSouscripteur.getNameBoss() != null) {
                    nomDirigeantField.setText(currentSouscripteur.getNameBoss());
                }
                
                if (currentSouscripteur.getFirstNameBoss() != null) {
                    prenomDirigeantField.setText(currentSouscripteur.getFirstNameBoss());
                }
                
                if (currentSouscripteur.getNationalityBoss() != null) {
                    nationaliteDirigeantComboBox.setValue(currentSouscripteur.getNationalityBoss());
                }
                
                if (currentSouscripteur.getDateOfBirthBoss() != null && !currentSouscripteur.getDateOfBirthBoss().isEmpty()) {
                    try {
                        dateNaissanceDirigeantField.setValue(LocalDate.parse(currentSouscripteur.getDateOfBirthBoss()));
                    } catch (Exception e) {
                        System.err.println("Erreur lors du parsing de la date de naissance du dirigeant: " + e.getMessage());
                    }
                }
                
                if (currentSouscripteur.getPlaceOfBirthBoss() != null) {
                    lieuNaissanceDirigeantField.setText(currentSouscripteur.getPlaceOfBirthBoss());
                }
                
                if (currentSouscripteur.getEmailBoss() != null) {
                    emailDirigeantField.setText(currentSouscripteur.getEmailBoss());
                }
                
                if (currentSouscripteur.getPhoneNumberBoss() != null) {
                    telephoneDirigeantField.setText(currentSouscripteur.getPhoneNumberBoss());
                }
                
                // Gestion sécurisée de l'adresse du dirigeant
                String[] addressBoss = currentSouscripteur.getAddressBoss();
                if (addressBoss != null) {
                    if (addressBoss.length > 0 && addressBoss[0] != null) numeroAdresseDirigeantField.setText(addressBoss[0]);
                    if (addressBoss.length > 1 && addressBoss[1] != null) rueAdresseDirigeantField.setText(addressBoss[1]);
                    if (addressBoss.length > 2 && addressBoss[2] != null) codePostalAdresseDirigeantField.setText(addressBoss[2]);
                    if (addressBoss.length > 3 && addressBoss[3] != null) villeAdresseDirigeantField.setText(addressBoss[3]);
                    if (addressBoss.length > 4 && addressBoss[4] != null) paysAdresseDirigeantField.setText(addressBoss[4]);
                    if (addressBoss.length > 5 && addressBoss[5] != null) complementAdresseDirigeantField.setText(addressBoss[5]);
                }
                
                if (currentSouscripteur.getRoleBoss() != null) {
                    fonctionDirigeantField.setText(currentSouscripteur.getRoleBoss());
                }
                
                if (currentSouscripteur.getvilleRCS() != null) {
                    villeRCSField.setText(currentSouscripteur.getvilleRCS());
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
                    for(String[] obligation : filteredObligations) {
                        if (obligation != null) {
                            for(int id : souscripteur.getObligations()) {
                                try {
                                    Obligation obligationObj = ObligationInteractor.GetObligation(id);
                                    if (obligationObj != null && obligationObj.getName() != null && 
                                        obligation.length > 0 && obligation[0] != null &&
                                        obligation[0].equalsIgnoreCase(obligationObj.getName())) {
                                        if (obligation.length > 1) {
                                            obligation[1] = "true"; // Mark as selected
                                        }
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
                } catch (Exception e) {
                    System.err.println("Erreur lors de la récupération de la famille: " + e.getMessage());
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de l'initialisation des données du souscripteur moral: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    public void initialize() {
        // Initialize the ComboBoxes and other UI elements if needed
        formeJuridiqueComboBox.setItems(FXCollections.observableArrayList("SARL", "SA", "SAS"));
        nationaliteDirigeantComboBox.setItems(FXCollections.observableArrayList("Française", "Américaine", "Allemande", "Espagnole"));

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
                String[] obligationData = new String[2];
                obligationData[0] = obligation.getName();
                obligationData[1] = String.valueOf(false);
                allObligations.add(obligationData);
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
            boolean numRegistreOK = validateField(numRegistreField, numRegistreErreurField, "text");
            boolean capitalSocialOK = validateField(capitalSocialField, capitalSocialErreurField, "text");
            boolean formeJuridiqueOK = validateField(formeJuridiqueComboBox, formeJuridiqueErreurComboBox, "");

            boolean numeroAdresseOK = validateField(numeroAdresseField, numeroAdresseErreurField, "text");
            boolean rueAdresseOK = validateField(rueAdresseField, rueAdresseErreurField, "text");
            boolean codePostalAdresseOK = validateField(codePostalAdresseField, codePostalAdresseErreurField, "text");
            boolean villeAdresseOK = validateField(villeAdresseField, villeAdresseErreurField, "text");
            boolean paysAdresseOK = validateField(paysAdresseField, paysAdresseErreurField, "text");
            // Le complément est facultatif ? sinon :
            boolean complementAdresseOK = validateField(complementAdresseField, complementAdresseErreurField, "text");

            boolean sexeDirigeantOK = sexeDirigeantToggleGroup.getSelectedToggle() != null;
            if (!sexeDirigeantOK) {
                sexeDirigeantErreurLabel.setText("Veuillez sélectionner un sexe.");
                sexeDirigeantErreurLabel.setVisible(!sexeDirigeantOK);
            }
            else {
                sexeDirigeantErreurLabel.setVisible(false);
            }
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
            boolean complementAdresseDirigeantOK = validateField(complementAdresseDirigeantField, complementAdresseDirigeantErreurField, "text");

            boolean fonctionDirigeantOK = validateField(fonctionDirigeantField, fonctionDirigeantErreurField, "text");
            boolean villeRCSOK = validateField(villeRCSField, villeRCSErreurField, "text");
            
            boolean bicOK = validateField(bicField, bicErreurField, "text");
            boolean ibanOK = validateField(ibanField, ibanErreurField, "text");
            boolean banqueOK = validateField(banqueField, banqueErreurField, "text");

            
            boolean familyOK = selectedFamily != null && !selectedFamily.isEmpty();
            if (!familyOK){
                familyErreurLabel.setText("Veuillez sélectionner une family.");
                familyErreurLabel.setVisible(!familyOK);
                familyErreurLabel.setStyle("-fx-text-fill: red; -fx-font-size: 10px;");
            }
            else {
                familyErreurLabel.setVisible(false);
            }

            // Vérification globale
            boolean formulaireValide =
                nomOK && numRegistreOK && capitalSocialOK && formeJuridiqueOK &&
                numeroAdresseOK && rueAdresseOK && codePostalAdresseOK && villeAdresseOK && paysAdresseOK && complementAdresseOK &&
                sexeDirigeantOK && nomDirigeantOK && prenomDirigeantOK && nationaliteDirigeantOK && dateNaissanceDirigeantOK &&
                lieuNaissanceDirigeantOK && emailDirigeantOK && telephoneDirigeantOK &&
                numeroAdresseDirigeantOK && rueAdresseDirigeantOK && codePostalAdresseDirigeantOK && villeAdresseDirigeantOK &&
                paysAdresseDirigeantOK && complementAdresseDirigeantOK &&
                fonctionDirigeantOK && villeRCSOK &&
                bicOK && ibanOK && banqueOK && familyOK;

            if (formulaireValide) {
                String sexeDirigeant = ((RadioButton) sexeDirigeantToggleGroup.getSelectedToggle()).getText();
                EditInvestorLP(
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
                    FamilyInteractor.GetFamilyByName(selectedFamily)
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

    private void EditInvestorLP(SimpleStringProperty name, int registerNumber, String dateOfCreation,
                    String capitalSocial, String legalStatus, String[] address,
                    String civilityBoss, SimpleStringProperty nameBoss, String firstNameBoss, String nationalityBoss,
                    String dateOfBirthBoss, String placeOfBirthBoss,
                    String emailBoss, String phoneNumberBoss, String[] addressBoss, String villeRCS,
                    String roleBoss, String IBAN, String BIC, String BankName, int familyId) {
        int Id = currentSouscripteur.getId();
        InvestorLP investorlp = new InvestorLP(Id, name, registerNumber, dateOfCreation,
                capitalSocial, legalStatus, address,
                civilityBoss, nameBoss, firstNameBoss, nationalityBoss,
                dateOfBirthBoss, placeOfBirthBoss,
                emailBoss, phoneNumberBoss, addressBoss, villeRCS,
                roleBoss, IBAN, BIC, BankName, familyId);
        System.out.println("Creating investorlp with the following details:");
        for (String[] obligation : allObligations) {
            if (Boolean.parseBoolean(obligation[1])) {
                System.out.println(ObligationInteractor.GetObligationByName(obligation[0]).getId());
                int obligationId = ObligationInteractor.GetObligationByName(obligation[0]).getId();
                
                // Vérification que l'obligation n'existe pas déjà dans l'investor
                if (!investorlp.getObligations().contains(obligationId)) {
                    investorlp.addObligation(obligationId);
                } else {
                    System.out.println("Obligation " + obligationId + " already exists in investor " + Id);
                }
                
                int idObligation = ObligationInteractor.GetObligationByName(obligation[0]).getId();
                if (idObligation != -1) {
                    Obligation newObligation = ObligationInteractor.GetObligation(idObligation);
                    
                    // Vérification que l'investisseur avec ce montant n'existe pas déjà dans l'obligation
                    Long defaultAmount = 10L;
                    boolean investorExists = false;
                    for (mypackage.model.util.InvestorInfo info : newObligation.getInvestors()) {
                        if (info.getInvestorId().equals(Id) && 
                            info.getCapital().equals(defaultAmount)) {
                            investorExists = true;
                            System.out.println("Investor " + Id + " with amount " + defaultAmount + " already exists in obligation " + idObligation);
                            break;
                        }
                    }
                    if (!investorExists) {
                        newObligation.addInvestor(Id, defaultAmount);
                    }
                    
                    ObligationInteractor.DeleteObligation(idObligation);
                    ObligationInteractor.SaveObligation(newObligation);
                }
            }
        }
        int familyIdSelected = FamilyInteractor.GetFamilyByName(selectedFamily);
        investorlp.setFamilyId(familyIdSelected);
        Family family = FamilyInteractor.GetFamily(familyIdSelected);
        
        // Vérification que l'investisseur n'existe pas déjà dans la famille
        if (!family.getInvestors().contains(Id)) {
            family.addInvestor(Id);
        } else {
            System.out.println("Investor " + Id + " already exists in family " + familyIdSelected);
        }
        
        FamilyInteractor.DeleteFamily(familyIdSelected);
        FamilyInteractor.SaveFamily(family);
        
        InvestorInteractor.DeleteInvestorLP(currentSouscripteur.getId());
        InvestorInteractor.SaveInvestorLP(investorlp);
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
