package mypackage.model.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Classe utilitaire pour les listes de nationalités
 */
public class NationalityUtils {
    
    /**
     * Retourne une liste des 30 principales nationalités
     * avec Française, Suisse, Belge en premier
     * @return ObservableList des nationalités
     */
    public static ObservableList<String> getNationalities() {
        return FXCollections.observableArrayList(
            // Les 5 premières comme demandé
            "Française",
            "Suisse", 
            "Belge",
            "Luxembourgeoise",
            "Italienne",
            // Les autres principales nationalités par ordre alphabétique
            "Allemande",
            "Américaine",
            "Anglaise",
            "Argentine",
            "Australienne",
            "Autrichienne",
            "Bahreïnie",
            "Brésilienne",
            "Canadienne",
            "Chinoise",
            "Danoise",
            "Émirienne",
            "Espagnole",
            "Finlandaise",
            "Grecque",
            "Hong-Kongaise",
            "Indienne",
            "Irlandaise",
            "Japonaise",
            "Koweïtienne",
            "Mexicaine",
            "Néerlandaise",
            "Néo-Zélandaise",
            "Norvégienne",
            "Omanaise",
            "Polonaise",
            "Portugaise",
            "Qatarie",
            "Russe",
            "Saoudienne",
            "Singapourienne",
            "Suédoise",
            "Tchèque",
            "Turque",
            "Autre"
        );
    }
    
    /**
     * Retourne une liste des principales formes juridiques
     * @return ObservableList des formes juridiques
     */
    public static ObservableList<String> getLegalForms() {
        return FXCollections.observableArrayList(
            "SARL",
            "SAS", 
            "SA",
            "Autre"
        );
    }
}
