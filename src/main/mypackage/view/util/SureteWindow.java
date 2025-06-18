package mypackage.view.util;

import javafx.collections.ObservableList;
import javafx.scene.control.TextInputDialog;

public class SureteWindow {
    public static boolean showDialog(ObservableList<String> suretes) {
        // Fenêtre pour l'année
        TextInputDialog sureteDialog = new TextInputDialog();
        sureteDialog.setHeaderText("Ajouter une sureté");
        sureteDialog.setContentText("Sureté :");

        String sureteString = sureteDialog.showAndWait().orElse(null);
        if (sureteString == null || sureteString.isEmpty()) {
            return false;
        }
        suretes.add(sureteString);
        return true;
    }
}
