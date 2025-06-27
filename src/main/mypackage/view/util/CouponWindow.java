package mypackage.view.util;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mypackage.view.util.listviewObjects.TupleStringLongBoolean;
import mypackage.view.util.listviewObjects.TupleStringMapMap;

public class CouponWindow {
    private static ListView<TupleStringLongBoolean> souscripteurVendeurListView = new ListView<>();
    private static TextField souscripteurVendeurSearchField = new TextField();
    private static ObservableList<TupleStringLongBoolean> allSouscripteursVendeurs = FXCollections.observableArrayList();

    public static boolean showDialog(ObservableList<TupleStringMapMap> cessions,
                                    ArrayList<TupleStringLongBoolean> souscripteursList) {
        Stage stage = new Stage();
        stage.setTitle("Ajouter une cession");

        // Sélection de la date avec DatePicker
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Sélectionnez une date");

        allSouscripteursVendeurs.clear();

        for (TupleStringLongBoolean souscripteur : souscripteursList) {
            allSouscripteursVendeurs.add(souscripteur.clone());
        }

        souscripteurVendeurListView.setItems(allSouscripteursVendeurs);
        souscripteurVendeurSearchField.textProperty().addListener((obs, oldValue, newValue) -> {
            
        });
        souscripteurVendeurListView.setCellFactory(lv -> new ListCell<TupleStringLongBoolean>() {
            private final CheckBox checkBoxVendeur = new CheckBox();
            private final TextField montantFieldVendeur = new TextField();
            private final HBox contentVendeur = new HBox(10, checkBoxVendeur, montantFieldVendeur);

            private TupleStringLongBoolean currentItem;

            @Override
            protected void updateItem(TupleStringLongBoolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    currentItem = null;
                } else {
                    currentItem = item;
                    checkBoxVendeur.setText(item.getName());
                    checkBoxVendeur.setSelected(item.getSelectionne());
                    montantFieldVendeur.setText(item.getCapital());
                    montantFieldVendeur.setDisable(!item.getSelectionne());
                    setGraphic(contentVendeur);
                }
            }
        });

        Button okButton = new Button("OK");
        Button cancelButton = new Button("Annuler");

        final boolean[] result = {false};

        okButton.setOnAction(e -> {
            if (datePicker.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner une date.");
                alert.showAndWait();
                return;
            }

            

            
            stage.close();
        });

        cancelButton.setOnAction(e -> stage.close());

        VBox inputBoxVendeur = new VBox(5, new Label("Vendeur :"), souscripteurVendeurSearchField, souscripteurVendeurListView);
        HBox buttonBox = new HBox(10, okButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        return result[0];
    }
}
