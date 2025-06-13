package mypackage.model;

import javafx.beans.property.SimpleStringProperty;

abstract public class Investor {
    private int id;
    private SimpleStringProperty name;

    
    public Investor() {
        this.name = new SimpleStringProperty("");
    }
    public Investor(int id, SimpleStringProperty name) {
        this.name = name;
        this.id = id;
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
    public void setName(String name) {
        this.name.set(name);
    }
    public void setId(int id) {
        this.id = id;
    }
}
