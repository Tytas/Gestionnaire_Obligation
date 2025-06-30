package mypackage.view.util;

import javafx.collections.*;
import javafx.scene.control.*;

public class AmortissementWindow {

    public static boolean showDialog(ObservableList<String[]> amortissements) {
        // Fenêtre pour l'année
        TextInputDialog anneeDialog = new TextInputDialog();
        anneeDialog.setHeaderText("Ajouter un amortissement");
        anneeDialog.setContentText("Année :");
        
        String annee = anneeDialog.showAndWait().orElse(null);
        if (annee == null || annee.isEmpty()) {
            return false;
        }

        // Fenêtre pour le montant
        TextInputDialog montantDialog = new TextInputDialog();
        montantDialog.setHeaderText("Nouveau Taux à partir de l'année " + annee);
        montantDialog.setContentText("Taux (%) :");
        
        String montantStr = montantDialog.showAndWait().orElse(null);
        if (montantStr == null || montantStr.isEmpty()) {
            return false;
        }

        try {
            int montant = Integer.parseInt(montantStr);
            amortissements.add(new String[]{
                annee,
                String.valueOf(montant)
            });
            return true; // On a ajouté avec succès
        } catch (NumberFormatException ex) {
            // Gérer l'exception si le montant n'est pas un nombre valide
            Alert alert = new Alert(Alert.AlertType.ERROR, "Montant invalide.");
            alert.showAndWait();
            return false;
        }
    }
}
