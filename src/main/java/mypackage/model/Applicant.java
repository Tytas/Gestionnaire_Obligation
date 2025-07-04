package mypackage.model;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;

public class Applicant {
    private int id;
    private SimpleStringProperty name;
    private int registerNumber;
    private String dateOfCreation;
    private String socialCapital;
    private String legalStatus;
    private String[] address = new String[6]; // Numéro, rue, code postal, ville, pays, Complement d'adresse
    private String cityRCS;

    private String civilityBoss;
    private SimpleStringProperty nameBoss;
    private String firstNameBoss;
    private String nationalityBoss;
    private String dateOfBirthBoss;
    private String placeOfBirthBoss;
    private String emailBoss;
    private String phoneNumberBoss;
    private String[] addressBoss = new String[6]; // Numéro, rue, code postal, ville, pays, Complement d'adresse
    private String taxIdNumberBoss;
    private String roleBoss;
    
    private String IBAN;
    private String BIC;
    private String bankName;
    private int groupId;
    private ArrayList<Integer> obligations = new ArrayList<>();

    public Applicant() {
        this.name = new SimpleStringProperty("");
        this.registerNumber = 0;
        this.dateOfCreation = "";
        this.socialCapital = "";
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
        this.cityRCS = "";
        this.taxIdNumberBoss = "";
        this.roleBoss = "";
        this.IBAN = "";
        this.BIC = "";
        this.bankName = "";
        this.groupId = 0;
    }

    public Applicant(int id, SimpleStringProperty name, int registerNumber, String dateOfCreation,
                    String socialCapital, String legalStatus, String[] address,
                    String civilityBoss, SimpleStringProperty nameBoss, String firstNameBoss, String nationalityBoss,
                    String dateOfBirthBoss, String placeOfBirthBoss,
                    String emailBoss, String phoneNumberBoss, String[] addressBoss, String cityRCS,
                    String taxIdNumberBoss, String roleBoss, String IBAN, String BIC, String BankName, int groupId) {
        this.id = id;
        this.name = name;
        this.registerNumber = registerNumber;
        this.dateOfCreation = dateOfCreation;
        this.socialCapital = socialCapital;
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
        this.cityRCS = cityRCS;
        this.taxIdNumberBoss = taxIdNumberBoss;
        this.roleBoss = roleBoss;
        this.IBAN = IBAN;
        this.BIC = BIC;
        this.bankName = BankName;
        this.groupId = groupId;
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
    public String getsocialCapital() {
        return socialCapital;
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
    public String getcityRCS() {
        return cityRCS;
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
    public int getGroupId() {
        return groupId;
    }
    public ArrayList<Integer> getObligations() {
        return obligations;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
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
    public void setsocialCapital(String socialCapital) {
        this.socialCapital = socialCapital;
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
    public void setcityRCS(String cityRCS) {
        this.cityRCS = cityRCS;
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
    public void setObligations(ArrayList<Integer> obligations) {
        this.obligations = obligations;
    }
    public void addObligation(int obligationId) {
        this.obligations.add(obligationId);
    }
    public void removeObligation(int obligationId) {
        this.obligations.remove(Integer.valueOf(obligationId));
    }
}
