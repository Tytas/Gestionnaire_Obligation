package mypackage.view.util.AddObject;

import javafx.beans.property.BooleanProperty;
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
}
