package mypackage.model;
public class Obligation {

    private String name;
    private String description; 
    private long amount;
    private String startDate;
    private int durationMonths;

    public Obligation() {
        this.name = "";
        this.description = "";
        this.amount = 0;
        this.startDate = "";
        this.durationMonths = 0;
    }

    public Obligation(String name, String description, long amount, String startDate, int durationMonths) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.startDate = startDate;
        this.durationMonths = durationMonths;
    }

    public String getName() {
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

    public void setName(String name) {
        this.name = name;
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

    
}
