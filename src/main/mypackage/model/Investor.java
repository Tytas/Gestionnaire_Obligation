package mypackage.model;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;

abstract public class Investor {
    private int id;
    private SimpleStringProperty name;
    private int familyId;
    private ArrayList<Integer> obligations = new ArrayList<>();
    
    public Investor() {
        this.name = new SimpleStringProperty("");
        this.familyId = 0;
    }
    public Investor(int id, SimpleStringProperty name, int familyId) {
        this.name = name;
        this.id = id;
        this.familyId = familyId;
    }

    public String getName() {
        return name.get();
    }
    public SimpleStringProperty nameProperty() {
        return name;
    }
    public int getId() {
        return id;
    }
    public int getFamilyId() {
        return familyId;
    }
    public ArrayList<Integer> getObligations() {
        return obligations;
    }
    public void setName(String name) {
        this.name.set(name);
    }
    public void setId(int id) {
        this.id = id;
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
/* Todo: pour la partie edition des obligations
    public Long getCapital(int obligationId) {
        return ObligationInteractor.GetObligation(obligationId);
    }*/
}