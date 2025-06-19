package mypackage.view.util.listviewObjects;

import java.util.HashMap;
import java.util.Map;

public class TupleStringMapMap {
    private String date;
    private Map<String, Long> vendeurs = new HashMap<>();
    private Map<String, Long> acheteurs = new HashMap<>();

    public TupleStringMapMap(String date, Map<String, Long> vendeurs, Map<String, Long> acheteurs) {
        this.date = date;
        this.vendeurs = vendeurs;
        this.acheteurs = acheteurs;
    }

    public String getDate() {
        return date;
    }

    public Map<String, Long> getVendeurs() {
        return vendeurs;
    }

    public Map<String, Long> getAcheteurs() {
        return acheteurs;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setVendeurs(Map<String, Long> vendeurs) {
        this.vendeurs = vendeurs;
    }
    public void setAcheteurs(Map<String, Long> acheteurs) {
        this.acheteurs = acheteurs;
    }
}
