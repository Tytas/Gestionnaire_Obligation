package mypackage.model;

import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;

import mypackage.model.util.Replacement;
import mypackage.model.util.InvestorInfo;

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
    private ArrayList<InvestorInfo> investors = new ArrayList<>(); // Liste des informations d'investisseurs
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
    public ArrayList<InvestorInfo> getInvestors() {
        return investors;
    }
    
    public Long getInvestorCapital(int investorId) {
        for (InvestorInfo info : investors) {
            if (info.getInvestorId() == investorId) {
                return info.getCapital();
            }
        }
        return 0L;
    }
    
    public String getInvestorDate(int investorId) {
        for (InvestorInfo info : investors) {
            if (info.getInvestorId() == investorId) {
                return info.getDate();
            }
        }
        return "";
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
    public void setInvestors(ArrayList<InvestorInfo> investors) {
        this.investors = investors;
    }
    
    /**
     * Convertit l'ancienne structure Map<Integer, Long> en nouvelle structure avec InvestorInfo
     * @param oldInvestors La map contenant les anciennes données
     */
    public void setInvestorsLegacy(Map<Integer, Long> oldInvestors) {
        this.investors.clear();
        for (Map.Entry<Integer, Long> entry : oldInvestors.entrySet()) {
            this.investors.add(new InvestorInfo(entry.getKey(), entry.getValue(), ""));
        }
    }
    
    public void addInvestor(Integer investor, long capital) {
        this.investors.add(new InvestorInfo(investor, capital, ""));
    }
    
    public void addInvestor(Integer investor, long capital, String date) {
        this.investors.add(new InvestorInfo(investor, capital, date));
    }
    
    public void removeInvestor(Integer investor) {
        this.investors.removeIf(info -> info.getInvestorId().equals(investor));
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

    public ArrayList<String[]> listCouponGetter() {
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
        if(this.getRate()[1] != 0){
            for(int i = period; i <= this.getDurationMonths(); i += period) {
                LocalDate couponDate = LocalDate.parse(this.getStartDate()).plusMonths(i);
                if (this.getRate()[0] != 0 && couponDate.isEqual(LocalDate.parse(this.getStartDate()).plusMonths(this.getDurationMonths())) && !this.getProrogationActivated()) {
                    String[] coupon = new String[3];
                    coupon[0] = couponDate.toString();
                    coupon[1] = String.valueOf((this.getRate()[1]+this.getRate()[0]) * this.getCapital() / 100);
                    coupon[2] = this.getName();
                    listCoupon.add(coupon);
                } else {
                    String[] coupon = new String[3];
                    coupon[0] = couponDate.toString();
                    coupon[1] = String.valueOf(this.getRate()[1] * this.getCapital() / 100);
                    coupon[2] = this.getName();
                    listCoupon.add(coupon);
                }
            }
        } else if(this.getRate()[0] != 0 && !this.getProrogationActivated()) {
            LocalDate couponDate = LocalDate.parse(this.getStartDate()).plusMonths(this.getDurationMonths());
            String[] coupon = new String[3];
            coupon[0] = couponDate.toString();
            coupon[1] = String.valueOf(this.getRate()[0] * this.getCapital() / 100);
            coupon[2] = this.getName();
            listCoupon.add(coupon);
        }
        if (this.getProrogationActivated() && (this.getProrogation()[1] != "0" || this.getProrogation()[1].trim() != "")) {
            for(int i = period; i <= Integer.parseInt(this.getProrogation()[0]); i+= period) {
                LocalDate prorogationCouponDate = LocalDate.parse(this.getStartDate()).plusMonths(this.getDurationMonths() + i);
                if ((this.getProrogation()[2] != "0" || this.getProrogation()[2] != "") && this.getProrogation()[1].trim() != "" && prorogationCouponDate.isEqual(LocalDate.parse(this.getStartDate()).plusMonths(this.getDurationMonths() + Integer.parseInt(this.getProrogation()[0])))) {
                    String[] coupon = new String[3];
                    coupon[0] = prorogationCouponDate.toString();
                    coupon[1] = String.valueOf((Long.parseLong(this.getProrogation()[1]) + Long.parseLong(this.getProrogation()[2])) * this.getCapital() / 100);
                    coupon[2] = this.getName();
                    listCoupon.add(coupon);
                } else {
                    String[] coupon = new String[3];
                    coupon[0] = prorogationCouponDate.toString();
                    coupon[1] = String.valueOf(Long.parseLong(this.getProrogation()[1]) * this.getCapital() / 100);
                    coupon[2] = this.getName();
                    listCoupon.add(coupon);
                }
            }
        } else if(this.getProrogationActivated() && (this.getProrogation()[2] != "0" || this.getProrogation()[2] != "")) {
            LocalDate prorogationCouponDate = LocalDate.parse(this.getStartDate()).plusMonths(this.getDurationMonths() + Integer.parseInt(this.getProrogation()[0]));
            String[] coupon = new String[3];
            coupon[0] = prorogationCouponDate.toString();
            coupon[1] = String.valueOf((Long.parseLong(this.getProrogation()[2])) * this.getCapital() / 100);
            coupon[2] = this.getName();
            listCoupon.add(coupon);
        }
        return listCoupon;
    }
    
}
