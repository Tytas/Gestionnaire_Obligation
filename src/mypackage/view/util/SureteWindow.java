package mypackage.view.util;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;

public class SureteWindow {
    public static boolean showDialog(ObservableList<String> suretes) {
        ObservableList<String> availableSuretes = javafx.collections.FXCollections.observableArrayList(
                "GAPD", "CAUTION", "FIDUCIE SUR TITRES",
            "FIDUCIE SUR IMMEUBLE", "HYPOTHEQUE 1ER RANG",
            "HYPOTHEQUE 1ER RANG","NANTISSEMENT", 
            "CONVERTIBLE", "GAGE"
        );
        Dialog<String> sureteDialog = new Dialog<>();
        sureteDialog.setHeaderText("Ajouter une sureté");
        sureteDialog.setContentText("Sureté :");
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(availableSuretes);
        comboBox.setPromptText("Sélectionnez une sureté");
        VBox vbox = new VBox(comboBox);
        sureteDialog.getDialogPane().setContent(vbox);
        sureteDialog.getDialogPane().getButtonTypes().addAll(javafx.scene.control.ButtonType.OK, javafx.scene.control.ButtonType.CANCEL);
        sureteDialog.showAndWait();
        String selectedSurete = comboBox.getValue();
        if (selectedSurete != null && !selectedSurete.isEmpty()) {
            suretes.add(selectedSurete);
            return true;
        }

        return false;
    }
}

