package mypackage.model;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;

public class Obligation {
    private int id;
    private final SimpleStringProperty name;
    private String description; 
    private long amount;
    private String startDate;   
    private int durationMonths;
    private int ApplicantId;
    private Map<Integer, Long> investors = new HashMap<>();

    public Obligation() {
        this.name = new SimpleStringProperty("");
        this.description = "";
        this.amount = 0;
        this.startDate = "";
        this.durationMonths = 0;
    }

    public Obligation(int id, SimpleStringProperty name, String description, long amount, String startDate, int durationMonths, int applicantId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.startDate = startDate;
        this.durationMonths = durationMonths;
        this.ApplicantId = applicantId;
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
    public String getDescription() {
        return description;
    }
    public long getAmount() {
        return amount;
    }
    public String getStartDate() {
        return startDate;
    }
    public int getDurationMonths() {
        return durationMonths;
    }
    public int getApplicantId() {
        return ApplicantId;
    }
    public Map<Integer, Long> getInvestors() {
        return investors;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name.set(name);
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setAmount(long amount) {
        this.amount = amount;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public void setDurationMonths(int durationMonths) {
        this.durationMonths = durationMonths;
    }
    public void setApplicantId(int applicantId) {
        this.ApplicantId = applicantId;
    }
    public void setInvestors(Map<Integer, Long> investors) {
        this.investors = investors;
    }
    public void addInvestor(Integer investor, long amount) {
        this.investors.put(investor, amount);
    }
    public void removeInvestor(Integer investor) {
        this.investors.remove(investor);
    }
    
}
