package mypackage.view.util.listviewObjects;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TupleStringLongBoolean {
    private int id;
    private StringProperty name;
    private StringProperty capital;
    private BooleanProperty selectionne;
    private StringProperty date; // Nouvelle propriété pour la date

    public TupleStringLongBoolean(int id, StringProperty name, StringProperty capital, BooleanProperty selectionne) {
        this.id = id;
        this.name = name;
        this.capital = capital;
        this.selectionne = selectionne;
        this.date = new SimpleStringProperty(""); // Valeur par défaut vide
    }

    public TupleStringLongBoolean(int id, StringProperty name, StringProperty capital, BooleanProperty selectionne, StringProperty date) {
        this.id = id;
        this.name = name;
        this.capital = capital;
        this.selectionne = selectionne;
        this.date = date;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name.get();
    }
    public StringProperty nameProperty() {
        return name;
    }

    public String getCapital() {
        return capital.get();
    }
    public StringProperty capitalProperty() {
        return capital;
    }

    public Boolean getSelectionne() {
        return selectionne.get();
    }
    public BooleanProperty selectionneProperty() {
        return selectionne;
    }

    public String getDate() {
        return date.get();
    }
    public StringProperty dateProperty() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name.set(name);
    }
    public void setCapital(String capital) {
        this.capital.set(capital);
    }
    public void setSelectionne(Boolean selectionne) {
        this.selectionne.set(selectionne);
    }
    public void setDate(String date) {
        this.date.set(date);
    }

    public TupleStringLongBoolean clone() {
        return new TupleStringLongBoolean(
            id,
            new SimpleStringProperty(name.get()),
            new SimpleStringProperty(capital.get()),
            new SimpleBooleanProperty(false),
            new SimpleStringProperty(date.get())
        );
    }
}
