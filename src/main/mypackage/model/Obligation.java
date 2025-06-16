package mypackage.model;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;

public class Obligation {
    private int id;
    private SimpleStringProperty name;
    private Boolean convertible; 
    private long capital;
    private String startDate;   
    private int durationMonths;
    private int[] rate = {0, 0}; // [In Fine, mensuelle]
    private int interestBase;
    private int periodicity;
    private String[] prorogation; // ["Oui" or "Non", Durée de prorogation, nouveau taux]
    private ArrayList<String> safeties = new ArrayList<>();
    private int ApplicantId;
    private Map<String, Integer> depreciations = new HashMap<>(); // Date -> Pourcentage
    private Map<Integer, Long> investors = new HashMap<>();

    public Obligation() {
        this.name = new SimpleStringProperty("");
        this.convertible = false;
        this.capital = 0;
        this.startDate = "";
        this.durationMonths = 0;
        this.rate = new int[]{0, 0};
        this.interestBase = 0;
        this.periodicity = 0;
        this.prorogation = new String[]{"", "", ""};
        this.ApplicantId = -1;
    }

    public Obligation(int id, SimpleStringProperty name, Boolean convertible, long capital, String startDate,
                      int durationMonths, int[] rate, int interestBase, int periodicity,
                      String[] prorogation, ArrayList<String> safeties, int applicantId) {
        this.id = id;
        this.name = name;
        this.convertible = convertible;
        this.capital = capital;
        this.startDate = startDate;
        this.durationMonths = durationMonths;
        this.rate = rate;
        this.interestBase = interestBase;
        this.periodicity = periodicity;
        this.prorogation = prorogation;
        this.safeties = safeties;
        this.ApplicantId = applicantId;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name.get();
    }
    public Boolean getConvertible() {
        return convertible;
    }
    public SimpleStringProperty nameProperty() {
        return name;
    }
    public long getCapital() {
        return capital;
    }
    public String getStartDate() {
        return startDate;
    }
    public int getDurationMonths() {
        return durationMonths;
    }
    public int[] getRate() {
        return rate;
    }
    public int getInterestBase() {
        return interestBase;
    }
    public int getPeriodicity() {
        return periodicity;
    }
    public String[] getProrogation() {
        return prorogation;
    }
    public ArrayList<String> getSafeties() {
        return safeties;
    }
    public int getApplicantId() {
        return ApplicantId;
    }
    public Map<String, Integer> getDepreciations() {
        return depreciations;
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
    public void setConvertible(Boolean convertible) {
        this.convertible = convertible;
    }
    public void setcapital(long capital) {
        this.capital = capital;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public void setDurationMonths(int durationMonths) {
        this.durationMonths = durationMonths;
    }
    public void setRate(int[] rate) {
        this.rate = rate;
    }
    public void setInterestBase(int interestBase) {
        this.interestBase = interestBase;
    }
    public void setPeriodicity(int periodicity) {
        this.periodicity = periodicity;
    }
    public void setProrogation(String[] prorogation) {
        this.prorogation = prorogation;
    }
    public void setSafeties(ArrayList<String> safeties) {
        this.safeties = safeties;
    }
    public void addSafety(String safety) {
        this.safeties.add(safety);
    }
    public void removeSafety(String safety) {
        this.safeties.remove(safety);
    }
    public void setApplicantId(int applicantId) {
        this.ApplicantId = applicantId;
    }
    public void setDepreciations(Map<String, Integer> depreciations) {
        this.depreciations = depreciations;
    }
    public void addDepreciation(String date, int percentage) {
        this.depreciations.put(date, percentage);
    }
    public void removeDepreciation(String date) {
        this.depreciations.remove(date);
    }
    public void setInvestors(Map<Integer, Long> investors) {
        this.investors = investors;
    }
    public void addInvestor(Integer investor, long capital) {
        this.investors.put(investor, capital);
    }
    public void removeInvestor(Integer investor) {
        this.investors.remove(investor);
    }
    
}
