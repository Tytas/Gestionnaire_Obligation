package mypackage.model;

import java.util.ArrayList;

public class Investor {
    private int id;
    private String name;
    private String firstName;
    private String email;
    private String phoneNumber;
    private String address;
    private String IBAN;
    private int familyId;
    private ArrayList<Integer> obligations = new ArrayList<>();

    public Investor() {
        this.name = "";
        this.firstName = "";
        this.email = "";
        this.phoneNumber = "";
        this.address = "";
        this.IBAN = "";
        this.familyId = 0;
    }
    public Investor(int id, String name, String firstName, String email, String phoneNumber, String address, String IBAN, int familyId) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.IBAN = IBAN;
        this.familyId = familyId;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }   
    public String getFirstName() {
        return firstName;
    }
    public String getEmail() {
        return email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getAddress() {
        return address;
    }
    public String getIBAN() {
        return IBAN;
    }
    public int getFamilyId() {
        return familyId;
    }
    public ArrayList<Integer> getObligations() {
        return obligations;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
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
