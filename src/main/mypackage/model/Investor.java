package mypackage.model;

abstract public class Investor {
    private int id;
    
    public Investor() {
        this.id = 0;
    }
    public Investor(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
