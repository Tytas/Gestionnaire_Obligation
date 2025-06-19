package mypackage.model.util;

import java.util.Map;
import java.util.HashMap;


public class Replacement {
    private Map<Integer, Long> investorsSalersId;
    private Map<Integer, Long> investorsBuyersId;
    private String date;

    public Replacement() {
        this.investorsSalersId = new HashMap<>();
        this.investorsBuyersId = new HashMap<>();
        this.date = "";
    }

    public Replacement(String date, Map<Integer, Long> investorsSalersId, Map<Integer, Long> investorsBuyersId) {
        this.investorsSalersId = investorsSalersId;
        this.investorsBuyersId = investorsBuyersId;
        this.date = date;
    }

    public Map<Integer, Long> getInvestorsSalersId() {
        return investorsSalersId;
    }
    public void setInvestorsSalersId(Map<Integer, Long> investorsSalersId) {
        this.investorsSalersId = investorsSalersId;
    }

    public Map<Integer, Long> getInvestorsBuyersId() {
        return investorsBuyersId;
    }
    public void setInvestorsBuyersId(Map<Integer, Long> investorsBuyersId) {
        this.investorsBuyersId = investorsBuyersId;
    }

    public void addInvestorSalersId(Integer investorId, Long amount) {
        this.investorsSalersId.put(investorId, amount);
    }
    public void addInvestorBuyersId(Integer investorId, Long amount) {
        this.investorsBuyersId.put(investorId, amount);
    }
    public void removeInvestorSalersId(Integer investorId) {
        this.investorsSalersId.remove(investorId);
    }
    public void removeInvestorBuyersId(Integer investorId) {
        this.investorsBuyersId.remove(investorId);
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}
