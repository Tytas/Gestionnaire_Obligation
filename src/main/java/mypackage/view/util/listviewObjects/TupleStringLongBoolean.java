package mypackage.view.util.listviewObjects;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TupleStringLongBoolean {
    private StringProperty name;
    private StringProperty capital;
    private BooleanProperty selectionne;

    public TupleStringLongBoolean(StringProperty name, StringProperty capital, BooleanProperty selectionne) {
        this.name = name;
        this.capital = capital;
        this.selectionne = selectionne;
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

    public void setName(String name) {
        this.name.set(name);
    }
    public void setCapital(String capital) {
        this.capital.set(capital);
    }
    public void setSelectionne(Boolean selectionne) {
        this.selectionne.set(selectionne);
    }

    public TupleStringLongBoolean clone() {
    return new TupleStringLongBoolean(
        new SimpleStringProperty(name.get()),
        new SimpleStringProperty(capital.get()),
        new SimpleBooleanProperty(false)
    );
}
}
