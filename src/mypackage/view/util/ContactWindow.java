package mypackage.view.util;

import javafx.collections.*;
import javafx.scene.control.*;

public class ContactWindow {

    public static boolean showDialog(ObservableList<String[]> contacts) {
        // Fenêtre pour le nom
        TextInputDialog nomDialog = new TextInputDialog();
        nomDialog.setHeaderText("Ajouter un contact");
        nomDialog.setContentText("Nom :");

        String nom = nomDialog.showAndWait().orElse(null);
        if (nom == null || nom.isEmpty()) {
            return false;
        }

        // Fenêtre pour le prénom
        TextInputDialog prenomDialog = new TextInputDialog();
        prenomDialog.setHeaderText("Nouveau Prénom pour " + nom);
        prenomDialog.setContentText("Prénom :");

        String prenom = prenomDialog.showAndWait().orElse(null);
        if (prenom == null || prenom.isEmpty()) {
            return false;
        }

        // Fenêtre pour l'email
        TextInputDialog emailDialog = new TextInputDialog();
        emailDialog.setHeaderText("Nouveau Email pour " + nom + " " + prenom);
        emailDialog.setContentText("Email :");

        String email = emailDialog.showAndWait().orElse(null);
        if (email == null || email.isEmpty()) {
            return false;
        }

        // Fenêtre pour le téléphone
        TextInputDialog telephoneDialog = new TextInputDialog();
        telephoneDialog.setHeaderText("Nouveau Téléphone pour " + nom + " " + prenom);
        telephoneDialog.setContentText("Téléphone :");

        String telephone = telephoneDialog.showAndWait().orElse(null);
        if (telephone == null || telephone.isEmpty()) {
            return false;
        }

        // Ajout du contact à la liste
        contacts.add(new String[]{
            nom,
            prenom,
            email,
            telephone
        });
        return true; // On a ajouté avec succès
    }
}
 