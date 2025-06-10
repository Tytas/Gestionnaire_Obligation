package mypackage.model;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;

public class Family {
    private int id;
    private SimpleStringProperty name;
    private ArrayList<Integer> investors = new ArrayList<>();

    public Family() {
        this.name = new SimpleStringProperty("");;
    }
    public Family(int id, SimpleStringProperty name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name.get();
    }
    public ArrayList<Integer> getInvestors() {
        return investors;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(SimpleStringProperty name) {
        this.name = name;
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
