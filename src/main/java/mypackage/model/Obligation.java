package mypackage.model;

import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;

import mypackage.model.util.Replacement;

public class Obligation {
    private int id;
    private SimpleStringProperty name;
    private Boolean convertible; 
    private long capital;
    private Integer valeurNominale;
    private String startDate;   
    private int durationMonths;
    private int[] rate = {0, 0}; // [In Fine, mensuelle]
    private String interestBase;
    private String periodicity;
    private String[] prorogation; // [Durée de prorogation, nouveau taux, nouveau taux In Fine]
    private Boolean prorogationActivated = false;
    private String isin; //[numero ISIN]
    private ArrayList<String> safeties = new ArrayList<>();
    private int ApplicantId;
    private Map<String, Integer> depreciations = new HashMap<>(); // Date -> Pourcentage
    private Map<Integer, Long> investors = new HashMap<>();
    private ArrayList<Replacement> replacements = new ArrayList<>();

    public Obligation() {
        this.name = new SimpleStringProperty("");
        this.convertible = false;
        this.capital = 0;
        this.valeurNominale = 0;
        this.startDate = "";
        this.durationMonths = 0;
        this.rate = new int[]{0, 0};
        this.interestBase = "";
        this.periodicity = "";
        this.prorogation = new String[]{"", "", ""}; // [Durée de prorogation, nouveau taux, nouveau taux In Fine]
        this.prorogationActivated = false;
        this.isin = "";
        this.ApplicantId = -1;
    }

    public Obligation(int id, SimpleStringProperty name, Boolean convertible, long capital, Integer valeurNominale,
                      String startDate, int durationMonths, int[] rate, String interestBase, String periodicity, String[] prorogation, 
                      Boolean prorogationActivated, String isin, ArrayList<String> safeties, Map<String, Integer> depreciations, int applicantId) {
        this.id = id;
        this.name = name;
        this.convertible = convertible;
        this.capital = capital;
        this.valeurNominale = valeurNominale;
        this.startDate = startDate;
        this.durationMonths = durationMonths;
        this.rate = rate;
        this.interestBase = interestBase;
        this.periodicity = periodicity;
        this.prorogation = prorogation;
        this.prorogationActivated = prorogationActivated;
        this.isin = isin;
        this.safeties = safeties;
        this.depreciations = depreciations;
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
    public Integer getValeurNominale() {
        return valeurNominale;
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
    public String getInterestBase() {
        return interestBase;
    }
    public String getPeriodicity() {
        return periodicity;
    }
    public String[] getProrogation() {
        return prorogation;
    }
    public Boolean getProrogationActivated() {
        return prorogationActivated;
    }
    public String getIsin() {
        return isin;
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
    public Long getInvestorCapital(int investorId) {
        return investors.getOrDefault(investorId, 0L);
    }
    public ArrayList<Replacement> getReplacements() {
        return replacements;
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
    public void setValeurNominale(Integer valeurNominale) {
        this.valeurNominale = valeurNominale;
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
    public void setInterestBase(String interestBase) {
        this.interestBase = interestBase;
    }
    public void setPeriodicity(String periodicity) {
        this.periodicity = periodicity;
    }
    public void setProrogation(String[] prorogation) {
        this.prorogation = prorogation;
    }
    public void setProrogationActivated(Boolean prorogationActivated) {
        this.prorogationActivated = prorogationActivated;
    }
    public void setIsin(String isin) {
        this.isin = isin;
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
    public void setReplacements(ArrayList<Replacement> replacements) {
        this.replacements = replacements;
    }
    public void addReplacement(Replacement replacement) {
        this.replacements.add(replacement);
    }
    public void removeReplacement(Replacement replacement) {
        this.replacements.remove(replacement);
    }

    public ArrayList<String[]> getListCoupon() {
        ArrayList<String[]> listCoupon = new ArrayList<>();
        int period = 0;
        if(this.getPeriodicity() != null) {
            if(this.getPeriodicity().equals("Mensuelle")) {
                period = 1;
            } else if(this.getPeriodicity().equals("Trimestrielle")) {
                period = 3;
            } else if(this.getPeriodicity().equals("Semestrielle")) {
                period = 6;
            } else if(this.getPeriodicity().equals("Annuelle")) {
                period = 12;
            } else {
                System.err.println("Unknown periodicity: " + this.getPeriodicity());
            }
        } else {
            System.err.println("Periodicity not set for obligation ID: " + this.getId());
            return null;
        }
        if(this.getRate()[0] != 0){
            for(int i = 0; i < this.getDurationMonths(); i += period) {
                LocalDate couponDate = LocalDate.parse(this.getStartDate()).plusMonths(i);
                String[] coupon = new String[3];
                coupon[0] = couponDate.toString();
                coupon[1] = String.valueOf(this.getRate()[0] * this.getCapital() / 100);
                coupon[2] = this.getName();
                listCoupon.add(coupon);
            }
        }
        if(this.getRate()[1] != 0) {
            LocalDate couponDate = LocalDate.parse(this.getStartDate()).plusMonths(this.getDurationMonths());
            String[] coupon = new String[3];
            coupon[0] = couponDate.toString();
            coupon[1] = String.valueOf(this.getRate()[1] * this.getCapital() / 100);
            coupon[2] = this.getName();
            listCoupon.add(coupon);
        }
        return listCoupon;
    }
    
}
