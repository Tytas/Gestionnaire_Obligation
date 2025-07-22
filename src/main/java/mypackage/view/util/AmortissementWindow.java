package mypackage.view.util;

import java.time.LocalDate;

import javafx.collections.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AmortissementWindow {

    public static boolean showDialog(ObservableList<String[]> amortissements) {
        // Créer une fenêtre modale personnalisée pour le DatePicker
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Ajouter un amortissement");
        
        // DatePicker pour la date
        DatePicker datePicker = new DatePicker(LocalDate.now());
        Label dateLabel = new Label("Date :");
        
        // Boutons
        Button okButton = new Button("OK");
        Button cancelButton = new Button("Annuler");
        
        // Layout
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(dateLabel, datePicker, okButton, cancelButton);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");
        
        // Variables pour stocker le résultat
        final boolean[] result = {false};
        final LocalDate[] selectedDate = {null};
        
        // Actions des boutons
        okButton.setOnAction(e -> {
            selectedDate[0] = datePicker.getValue();
            if (selectedDate[0] != null) {
                result[0] = true;
                dialog.close();
            }
        });
        
        cancelButton.setOnAction(e -> {
            result[0] = false;
            dialog.close();
        });

        // Afficher la fenêtre
        Scene scene = new Scene(vbox, 250, 150);
        dialog.setScene(scene);
        dialog.showAndWait();

        // Fenêtre pour le montant
        TextInputDialog montantDialog = new TextInputDialog();
        montantDialog.setHeaderText("Pourcentage de remboursement à partir de la date " + String.valueOf(selectedDate[0]));
        montantDialog.setContentText("Remboursement (%) :");
        
        String montantStr = montantDialog.showAndWait().orElse(null);
        if (montantStr == null || montantStr.isEmpty()) {
            return false;
        }

        try {
            int montant = Integer.parseInt(montantStr);
            amortissements.add(new String[]{
                String.valueOf(selectedDate[0]),
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
