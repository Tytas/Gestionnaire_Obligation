package mypackage.model;

import javafx.beans.property.SimpleStringProperty;

public class InvestorNP extends Investor {
    private String civility;
    private String firstName;
    private String nationality;
    private String dateOfBirth;
    private String placeOfBirth;
    private String email;
    private String phoneNumber;
    private String[] address = new String[6]; // Numéro, rue, code postal, ville, pays, Complement d'adresse
    private String IBAN;
    private String BIC;
    private String bankName;

    public InvestorNP() {
        super();
        this.civility = "";
        this.firstName = "";
        this.nationality = "";
        this.dateOfBirth = "";
        this.placeOfBirth = "";
        this.email = "";
        this.phoneNumber = "";
        for (int i = 0; i < address.length; i++) {
            address[i] = "";
        }
        this.IBAN = "";
        this.BIC = "";
        this.bankName = "";
    }
    public InvestorNP(int id, String civility, SimpleStringProperty name, String firstName,String nationality,
                    String dateOfBirth, String placeOfBirth, String email, String phoneNumber,
                    String[] address, String IBAN, String BIC, String BankName, int familyId) {
        super(id, name, familyId);
        this.civility = civility;
        this.firstName = firstName;
        this.nationality = nationality;
        this.dateOfBirth = dateOfBirth;
        this.placeOfBirth = placeOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.IBAN = IBAN;
        this.BIC = BIC;
        this.bankName = BankName;
    }

    public String getCivility() {
        return civility;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getNationality() {
        return nationality;
    }
    public String getDateOfBirth() {
        return dateOfBirth;
    }
    public String getPlaceOfBirth() {
        return placeOfBirth;
    }
    public String getEmail() {
        return email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String[] getAddress() {
        return address;
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

    public void setCivility(String civility) {
        this.civility = civility;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setNationality(String nationality){
        this.nationality = nationality;
    }
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setAddress(String[] address) {
        this.address = address;
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
