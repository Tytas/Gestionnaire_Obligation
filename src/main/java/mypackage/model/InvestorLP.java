package mypackage.model;

import java.util.ArrayList;

public class InvestorLP extends Investor {
    private String name;
    private String country;
    private int registerNumber;
    private String dateOfCreation;
    private String typeOfBuisness;
    private String legalStatus;
    private String[] address = new String[6]; // Numéro, rue, code postal, ville, pays, Complement d'adresse

    private String civilityBoss;
    private String nameBoss;
    private String firstNameBoss;
    private String nationalityBoss;
    private String dateOfBirthBoss;
    private String placeOfBirthBoss;
    private String LanguageBoss;
    private String emailBoss;
    private String phoneNumberBoss;
    private String[] addressBoss = new String[6]; // Numéro, rue, code postal, ville, pays, Complement d'adresse
    private String fiscalcountryBoss;
    private String taxIdNumberBoss;
    private String roleBoss;
    private String IBAN;
    private String BIC;
    private String bankName;
    private int familyId;
    private ArrayList<Integer> obligations = new ArrayList<>();

    public InvestorLP() {
        super();
        this.name = "";
        this.country = "";
        this.registerNumber = 0;
        this.dateOfCreation = "";
        this.typeOfBuisness = "";
        this.legalStatus = "";
        for (int i = 0; i < address.length; i++) {
            address[i] = "";
        }
        
        this.civilityBoss = "";
        this.nameBoss = "";
        this.firstNameBoss = "";
        this.nationalityBoss = "";
        this.dateOfBirthBoss = "";
        this.placeOfBirthBoss = "";
        this.LanguageBoss = "";
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
        this.familyId = 0;
    }

    public InvestorLP(int id, String name, String country, int registerNumber, String dateOfCreation,
                      String typeOfBuisness, String legalStatus, String[] address,
                      String civilityBoss, String nameBoss, String firstNameBoss, String nationalityBoss,
                      String dateOfBirthBoss, String placeOfBirthBoss, String languageBoss,
                      String emailBoss, String phoneNumberBoss, String[] addressBoss, String fiscalcountryBoss,
                      String taxIdNumberBoss, String roleBoss, String IBAN, String BIC, String BankName, int familyId) {
        super(id);
        this.name = name;
        this.country = country;
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
        this.LanguageBoss = languageBoss;
        this.emailBoss = emailBoss;
        this.phoneNumberBoss = phoneNumberBoss;
        this.addressBoss = addressBoss;
        this.fiscalcountryBoss = fiscalcountryBoss;
        this.taxIdNumberBoss = taxIdNumberBoss;
        this.roleBoss = roleBoss;
        this.IBAN = IBAN;
        this.BIC = BIC;
        this.bankName = BankName;
        this.familyId = familyId;
    }

    public String getName() {
        return name;
    }
    public String getcountry() {
        return country;
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
    public String getLanguageBoss() {
        return LanguageBoss;
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
    public String getFiscalcountryBoss() {
        return fiscalcountryBoss;
    }
    public String getTaxIdNumberBoss() {
        return taxIdNumberBoss;
    }
    public String getRoleBoss() {
        return roleBoss;
    }
    public int getFamilyId() {
        return familyId;
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
    public ArrayList<Integer> getObligations() {
        return obligations;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setcountry(String country) {
        this.country = country;
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
        this.nameBoss = nameBoss;
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
    public void setLanguageBoss(String languageBoss) {
        this.LanguageBoss = languageBoss;
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
    public void setFamilyId(int familyId) {
        this.familyId = familyId;
    }
    public void setObligations(ArrayList<Integer> obligations) {
        this.obligations = obligations;
    }
    public void addObligation(int obligationId) {
        if (!obligations.contains(obligationId)) {
            obligations.add(obligationId);
        }
    }
    public void removeObligation(int obligationId) {
        obligations.remove(Integer.valueOf(obligationId));
    }
}
