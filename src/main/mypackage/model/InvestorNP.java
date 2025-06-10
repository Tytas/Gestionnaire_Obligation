package mypackage.model;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;

public class InvestorNP extends Investor {
    private String civility;
    private SimpleStringProperty name;
    private String firstName;
    private String nationality;
    private String dateOfBirth;
    private String placeOfBirth;
    private String Language;
    private String email;
    private String phoneNumber;
    private String[] address = new String[6]; // Numéro, rue, code postal, ville, pays, Complement d'adresse
    private String IBAN;
    private String BIC;
    private String bankName;
    private int familyId;
    private ArrayList<Integer> obligations = new ArrayList<>();

    public InvestorNP() {
        super();
        this.civility = "";
        this.name = new SimpleStringProperty("");;
        this.firstName = "";
        this.nationality = "";
        this.dateOfBirth = "";
        this.placeOfBirth = "";
        this.Language = "";
        this.email = "";
        this.phoneNumber = "";
        for (int i = 0; i < address.length; i++) {
            address[i] = "";
        }
        this.IBAN = "";
        this.BIC = "";
        this.bankName = "";
        this.familyId = 0;
    }
    public InvestorNP(int id, String civility, SimpleStringProperty name, String firstName,String nationality,
                    String dateOfBirth, String placeOfBirth, String language, String email, String phoneNumber,
                    String[] address, String IBAN, String BIC, String BankName, int familyId) {
        super(id);
        this.civility = civility;
        this.name = name;
        this.firstName = firstName;
        this.nationality = nationality;
        this.dateOfBirth = dateOfBirth;
        this.placeOfBirth = placeOfBirth;
        this.Language = language;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.IBAN = IBAN;
        this.BIC = BIC;
        this.bankName = BankName;
        this.familyId = familyId;
    }

    public String getCivility() {
        return civility;
    }
    public String getName() {
        return name.get();
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
    public String getLanguage() {
        return Language;
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
    public int getFamilyId() {
        return familyId;
    }
    public ArrayList<Integer> getObligations() {
        return obligations;
    }

    public void setCivility(String civility) {
        this.civility = civility;
    }
    public void setName(SimpleStringProperty name) {
        this.name = name;
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
    public void setLanguage(String language) {
        this.Language = language;
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
    public void setFamilyId(int familyId) {
        this.familyId = familyId;
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
