package mypackage.model;

import javafx.beans.property.SimpleStringProperty;

public class InvestorLP extends Investor {
    private int registerNumber;
    private String dateOfCreation;
    private String capitalSocial;
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
    private String villeRCS;
    private String roleBoss;
    private String IBAN;
    private String BIC;
    private String bankName;

    public InvestorLP() {
        super();
        this.registerNumber = 0;
        this.dateOfCreation = "";
        this.capitalSocial = "";
        this.legalStatus = "";
        for (int i = 0; i < address.length; i++) {
            address[i] = "";
        }
        
        this.civilityBoss = "";
        this.nameBoss = new SimpleStringProperty("");;
        this.firstNameBoss = "";
        this.nationalityBoss = "";
        this.dateOfBirthBoss = "";
        this.placeOfBirthBoss = "";
        this.emailBoss = "";
        this.phoneNumberBoss = "";
        for (int i = 0; i < addressBoss.length; i++) {
            addressBoss[i] = "";
        }
        this.villeRCS = "";
        this.roleBoss = "";
        this.IBAN = "";
        this.BIC = "";
        this.bankName = "";
    }

    public InvestorLP(int id, SimpleStringProperty name, int registerNumber, String dateOfCreation,
                      String capitalSocial, String legalStatus, String[] address,
                      String civilityBoss, SimpleStringProperty nameBoss, String firstNameBoss, String nationalityBoss,
                      String dateOfBirthBoss, String placeOfBirthBoss,
                      String emailBoss, String phoneNumberBoss, String[] addressBoss, String villeRCS,
                      String roleBoss, String IBAN, String BIC, String BankName, int familyId) {
        super(id, name, familyId);
        this.registerNumber = registerNumber;
        this.dateOfCreation = dateOfCreation;
        this.capitalSocial = capitalSocial;
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
        this.villeRCS = villeRCS;
        this.roleBoss = roleBoss;
        this.IBAN = IBAN;
        this.BIC = BIC;
        this.bankName = BankName;
    }

    public int getRegisterNumber() {
        return registerNumber;
    }
    public String getDateOfCreation() {
        return dateOfCreation;
    }
    public String getcapitalSocial() {
        return capitalSocial;
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
    public String getEmailBoss() {
        return emailBoss;
    }
    public String getPhoneNumberBoss() {
        return phoneNumberBoss;
    }
    public String[] getAddressBoss() {
        return addressBoss;
    }
    public String getvilleRCS() {
        return villeRCS;
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
    public void setRegisterNumber(int registerNumber) {
        this.registerNumber = registerNumber;
    }
    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }
    public void setcapitalSocial(String capitalSocial) {
        this.capitalSocial = capitalSocial;
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
    public void setvilleRCS(String villeRCS) {
        this.villeRCS = villeRCS;
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
}
