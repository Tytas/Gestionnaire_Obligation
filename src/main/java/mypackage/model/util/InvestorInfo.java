package mypackage.model.util;

/**
 * Classe pour stocker les informations d'un investisseur dans une obligation.
 * Contient l'ID de l'investisseur, le capital investi et la date d'investissement.
 */
public class InvestorInfo {
    
    private Integer investorId;
    private Long capital;
    private String date;

    /**
     * Constructeur par défaut
     */
    public InvestorInfo() {
        this.investorId = -1;
        this.capital = 0L;
        this.date = "";
    }

    /**
     * Constructeur avec tous les champs
     * @param investorId ID de l'investisseur
     * @param capital Capital investi
     * @param date Date d'investissement
     */
    public InvestorInfo(Integer investorId, Long capital, String date) {
        this.investorId = investorId;
        this.capital = capital;
        this.date = date;
    }

    /**
     * @return L'ID de l'investisseur
     */
    public Integer getInvestorId() {
        return investorId;
    }

    /**
     * Définit l'ID de l'investisseur
     * @param investorId Nouvel ID
     */
    public void setInvestorId(Integer investorId) {
        this.investorId = investorId;
    }

    /**
     * @return Le capital investi
     */
    public Long getCapital() {
        return capital;
    }

    /**
     * Définit le capital investi
     * @param capital Nouveau capital
     */
    public void setCapital(Long capital) {
        this.capital = capital;
    }

    /**
     * @return La date d'investissement
     */
    public String getDate() {
        return date;
    }

    /**
     * Définit la date d'investissement
     * @param date Nouvelle date
     */
    public void setDate(String date) {
        this.date = date;
    }
}
