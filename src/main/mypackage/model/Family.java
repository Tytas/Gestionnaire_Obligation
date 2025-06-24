package mypackage.model;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;

public class Family {
    private int id;
    private SimpleStringProperty name;
    private int registerNumber;
    private String dateOfCreation;
    private String typeOfBuisness;
    private String legalStatus;
    private String[] address = new String[6]; // Numéro, rue, code postal, ville, pays, Complement d'adresse

    private String civilityBoss;
    private SimpleStringProperty nameBoss;
    private String firstNameBoss;
    private String nationalityBoss;
    private String dateOfBirthBoss;
    private String placeOfBirthBoss;
    private String emailBoss;
    private String phoneNumberBoss;
    private String[] addressBoss = new String[6]; // Numéro, rue, code postal, ville, pays, Complement d'adresse
    private String fiscalcountryBoss;
    private String taxIdNumberBoss;
    private String roleBoss;
    private String IBAN;
    private String BIC;
    private String bankName;
    private String familyType;
    private String nbAprooval;
    private ArrayList<String[]> contacts = new ArrayList<>(); // [Nom, Prénom, Email, Téléphone]
    private String alertMail;

    private ArrayList<Integer> investors = new ArrayList<>();

    public Family() {
        this.name = new SimpleStringProperty("");
        this.registerNumber = 0;
        this.dateOfCreation = "";
        this.typeOfBuisness = "";
        this.legalStatus = "";
        for (int i = 0; i < address.length; i++) {
            address[i] = "";
        }
        this.civilityBoss = "";
        this.nameBoss = new SimpleStringProperty("");
        this.firstNameBoss = "";
        this.nationalityBoss = "";
        this.dateOfBirthBoss = "";
        this.placeOfBirthBoss = "";
        this.emailBoss = "";
        this.phoneNumberBoss = "";
        for (int i = 0; i < addressBoss.length; i++) {
            addressBoss[i] = "";
        }
        this.fiscalcountryBoss = "";
        this.taxIdNumberBoss = "";
        this.roleBoss = "";
        this.IBAN = "";
        this.BIC = "";
        this.bankName = "";
        this.familyType = "";
        this.nbAprooval = "";
        this.alertMail = "";
    }
    public Family(int id, SimpleStringProperty name, int registerNumber, String dateOfCreation,
                  String typeOfBuisness, String legalStatus, String[] address,
                  String civilityBoss, SimpleStringProperty nameBoss, String firstNameBoss,
                  String nationalityBoss, String dateOfBirthBoss, String placeOfBirthBoss,
                  String emailBoss, String phoneNumberBoss,
                  String[] addressBoss, String fiscalcountryBoss, String taxIdNumberBoss,
                  String roleBoss, String IBAN, String BIC, String bankName, String familyType,
                  String nbAprooval, String alertMail) {
        this.id = id;
        this.name = name;
        this.registerNumber = registerNumber;
        this.dateOfCreation = dateOfCreation;
        this.typeOfBuisness = typeOfBuisness;
        this.legalStatus = legalStatus;
        this.address = address;
        this.civilityBoss = civilityBoss;
        this.nameBoss = nameBoss;
        this.firstNameBoss = firstNameBoss;
        this.nationalityBoss = nationalityBoss;
        this.dateOfBirthBoss = dateOfBirthBoss;
        this.placeOfBirthBoss = placeOfBirthBoss;
        this.emailBoss = emailBoss;
        this.phoneNumberBoss = phoneNumberBoss;
        this.addressBoss = addressBoss;
        this.fiscalcountryBoss = fiscalcountryBoss;
        this.taxIdNumberBoss = taxIdNumberBoss;
        this.roleBoss = roleBoss;
        this.IBAN = IBAN;
        this.BIC = BIC;
        this.bankName = bankName;
        this.familyType = familyType;
        this.nbAprooval = nbAprooval;
        this.alertMail = alertMail;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name.get();
    }
    public SimpleStringProperty nameProperty() {
        return name;
    }
    public int getRegisterNumber() {
        return registerNumber;
    }
    public String getDateOfCreation() {
        return dateOfCreation;
    }
    public String getTypeOfBuisness() {
        return typeOfBuisness;
    }
    public String getLegalStatus() {
        return legalStatus;
    }
    public String[] getAddress() {
        return address;
    }
    public String getCivilityBoss() {
        return civilityBoss;
    }
    public String getNameBoss() {
        return nameBoss.get();
    }
    public SimpleStringProperty nameBossProperty() {
        return nameBoss;
    }
    public String getFirstNameBoss() {
        return firstNameBoss;
    }
    public String getNationalityBoss() {
        return nationalityBoss;
    }
    public String getDateOfBirthBoss() {
        return dateOfBirthBoss;
    }
    public String getPlaceOfBirthBoss() {
        return placeOfBirthBoss;
    }
    public String getEmailBoss(){
        return emailBoss;
    }
    public String getPhoneNumberBoss() {
        return phoneNumberBoss;
    }
    public String[] getAddressBoss() {
        return addressBoss;
    }
    public String getFiscalcountryBoss() {
        return fiscalcountryBoss;
    }
    public String getTaxIdNumberBoss() {
        return taxIdNumberBoss;
    }
    public String getRoleBoss() {
        return roleBoss;
    }
    public String getIBAN() {
        return IBAN;
    }
    public String getBIC() {
        return BIC;
    }
    public String getBankName() {
        return bankName;
    }
    public String getFamilyType() {
        return familyType;
    }
    public String getNbAprooval() {
        return nbAprooval;
    }
    public ArrayList<String[]> getContacts() {
        return contacts;
    }
    public String getAlertMail() {
        return alertMail;
    }
    public ArrayList<Integer> getInvestors() {
        return investors;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name.set(name);
    }
    public void setRegisterNumber(int registerNumber) {
        this.registerNumber = registerNumber;
    }
    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }
    public void setTypeOfBuisness(String typeOfBuisness) {
        this.typeOfBuisness = typeOfBuisness;
    }
    public void setLegalStatus(String legalStatus) {
        this.legalStatus = legalStatus;
    }
    public void setAddress(String[] address) {
        this.address = address;
    }
    public void setCivilityBoss(String civilityBoss) {
        this.civilityBoss = civilityBoss;
    }
    public void setNameBoss(String nameBoss) {
        this.nameBoss.set(nameBoss);
    }
    public void setFirstNameBoss(String firstNameBoss) {
        this.firstNameBoss = firstNameBoss;
    }
    public void setNationalityBoss(String nationalityBoss) {
        this.nationalityBoss = nationalityBoss;
    }
    public void setDateOfBirthBoss(String dateOfBirthBoss) {
        this.dateOfBirthBoss = dateOfBirthBoss;
    }
    public void setPlaceOfBirthBoss(String placeOfBirthBoss) {
        this.placeOfBirthBoss = placeOfBirthBoss;
    }
    public void setEmailBoss(String emailBoss) {
        this.emailBoss = emailBoss;
    }
    public void setPhoneNumberBoss(String phoneNumberBoss) {
        this.phoneNumberBoss = phoneNumberBoss;
    }
    public void setAddressBoss(String[] addressBoss) {
        this.addressBoss = addressBoss;
    }
    public void setFiscalcountryBoss(String fiscalcountryBoss) {
        this.fiscalcountryBoss = fiscalcountryBoss;
    }
    public void setTaxIdNumberBoss(String taxIdNumberBoss) {
        this.taxIdNumberBoss = taxIdNumberBoss;
    }
    public void setRoleBoss(String roleBoss) {
        this.roleBoss = roleBoss;
    }
    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }
    public void setBIC(String BIC) {
        this.BIC = BIC;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public void setFamilyType(String familyType) {
        this.familyType = familyType;
    }
    public void setNbAprooval(String nbAprooval) {
        this.nbAprooval = nbAprooval;
    }
    public void setContacts(ArrayList<String[]> contacts) {
        this.contacts = contacts;
    }
    public void addContacts(String[] contact){
        this.contacts.add(contact);
    }
    public void removeContacts(String[] contact){
        this.contacts.remove(contact);
    }
    public void setAlertMail(String alertMail) {
        this.alertMail = alertMail;
    }
    public void setInvestors(ArrayList<Integer> investors) {
        this.investors = investors;
    }
    public void addInvestor(int investorId) {
        this.investors.add(investorId);
    }
    public void removeInvestor(int investorId) {
        this.investors.remove(Integer.valueOf(investorId));
    }
}
