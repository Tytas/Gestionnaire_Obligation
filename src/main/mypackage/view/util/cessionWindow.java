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

public class cessionWindow {
    private static ListView<TupleStringLongBoolean> souscripteurVendeurListView = new ListView<>();
    private static TextField souscripteurVendeurSearchField = new TextField();
    private static ObservableList<TupleStringLongBoolean> allSouscripteursVendeurs = FXCollections.observableArrayList();
    private static FilteredList<TupleStringLongBoolean> filteredSouscripteursVendeurs = new FilteredList<>(allSouscripteursVendeurs, s -> true);

    private static ListView<TupleStringLongBoolean> souscripteurAcheteurListView = new ListView<>();
    private static TextField souscripteurAcheteurSearchField = new TextField();
    private static ObservableList<TupleStringLongBoolean> allSouscripteursAcheteurs = FXCollections.observableArrayList();
    private static FilteredList<TupleStringLongBoolean> filteredSouscripteursAcheteurs = new FilteredList<>(allSouscripteursAcheteurs, s -> true);
    private static ArrayList<TupleStringLongBoolean> selectedSouscripteursVendeurs = new ArrayList<>();
    private static ArrayList<TupleStringLongBoolean> selectedSouscripteursAcheteurs = new ArrayList<>();

    public static boolean showDialog(ObservableList<TupleStringMapMap> cessions,
                                    ArrayList<TupleStringLongBoolean> souscripteursList) {
        Stage stage = new Stage();
        stage.setTitle("Ajouter une cession");

        // Sélection de la date avec DatePicker
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Sélectionnez une date");

        allSouscripteursAcheteurs.clear();
        allSouscripteursVendeurs.clear();

        for (TupleStringLongBoolean souscripteur : souscripteursList) {
            allSouscripteursVendeurs.add(souscripteur.clone());
            allSouscripteursAcheteurs.add(souscripteur.clone());
        }

        filteredSouscripteursVendeurs = new FilteredList<>(allSouscripteursVendeurs, s -> true);
        souscripteurVendeurListView.setItems(filteredSouscripteursVendeurs);
        souscripteurVendeurSearchField.textProperty().addListener((obs, oldValue, newValue) -> {
            filteredSouscripteursVendeurs.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                return item.getName().toLowerCase().contains(lowerCaseFilter);
            });
        });
        souscripteurVendeurListView.setCellFactory(lv -> new ListCell<TupleStringLongBoolean>() {
            private final CheckBox checkBoxVendeur = new CheckBox();
            private final TextField montantFieldVendeur = new TextField();
            private final HBox contentVendeur = new HBox(10, checkBoxVendeur, montantFieldVendeur);

            private TupleStringLongBoolean currentItem;

            {
                checkBoxVendeur.selectedProperty().addListener((obs, oldVal, newVal) -> {
                    if (currentItem != null) {
                        montantFieldVendeur.setDisable(!newVal);
                        currentItem.setSelectionne(newVal);
                        if (newVal) {
                            if (!selectedSouscripteursVendeurs.contains(currentItem)) {
                                selectedSouscripteursVendeurs.add(currentItem);
                            }
                        } else {
                            selectedSouscripteursVendeurs.remove(currentItem);
                        }
                    }
                });

                montantFieldVendeur.textProperty().addListener((obs, oldVal, newVal) -> {
                    if (currentItem != null) {
                        currentItem.setCapital(newVal);
                    }
                });
            }

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

        filteredSouscripteursAcheteurs = new FilteredList<>(allSouscripteursAcheteurs, s -> true);
        souscripteurAcheteurListView.setItems(filteredSouscripteursAcheteurs);
        souscripteurAcheteurSearchField.textProperty().addListener((obs, oldValue, newValue) -> {
            filteredSouscripteursAcheteurs.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                return item.getName().toLowerCase().contains(lowerCaseFilter);
            });
        });
        souscripteurAcheteurListView.setCellFactory(lv -> new ListCell<TupleStringLongBoolean>() {
            private final CheckBox checkBoxAcheteur = new CheckBox();
            private final TextField montantFieldAcheteur = new TextField();
            private final HBox contentAcheteur = new HBox(10, checkBoxAcheteur, montantFieldAcheteur);

            private TupleStringLongBoolean currentItem;

            {
                checkBoxAcheteur.selectedProperty().addListener((obs, oldVal, newVal) -> {
                    if (currentItem != null) {
                        montantFieldAcheteur.setDisable(!newVal);
                        currentItem.setSelectionne(newVal);
                        if (newVal) {
                            if (!selectedSouscripteursAcheteurs.contains(currentItem)) {
                                selectedSouscripteursAcheteurs.add(currentItem);
                            }
                        } else {
                            selectedSouscripteursAcheteurs.remove(currentItem);
                        }
                    }
                });

                montantFieldAcheteur.textProperty().addListener((obs, oldVal, newVal) -> {
                    if (currentItem != null) {
                        currentItem.setCapital(newVal);
                    }
                });
            }

            @Override
            protected void updateItem(TupleStringLongBoolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    currentItem = null;
                } else {
                    currentItem = item;
                    checkBoxAcheteur.setText(item.getName());
                    checkBoxAcheteur.setSelected(item.getSelectionne());
                    montantFieldAcheteur.setText(item.getCapital());
                    montantFieldAcheteur.setDisable(!item.getSelectionne());
                    setGraphic(contentAcheteur);
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

            if (selectedSouscripteursAcheteurs == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un élément dans la liste.");
                alert.showAndWait();
                return;
            }

            if (selectedSouscripteursVendeurs == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un élément dans la liste.");
                alert.showAndWait();
                return;
            }

            String dateStr = datePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            Map<String, Long> selectedVendeursMap = new HashMap<>();
            System.out.println("Selected:"+ selectedSouscripteursAcheteurs.get(0).getName());
            for (int i = 0; i < selectedSouscripteursVendeurs.size(); i++) {
                TupleStringLongBoolean vendeur = selectedSouscripteursVendeurs.get(i);
                selectedVendeursMap.put(vendeur.getName(), Long.valueOf(vendeur.getCapital()));
            }
            Map<String, Long> selectedAcheteursMap = new HashMap<>();
            for (int i = 0; i < selectedSouscripteursAcheteurs.size(); i++) {
                TupleStringLongBoolean acheteur = selectedSouscripteursAcheteurs.get(i);
                selectedAcheteursMap.put(acheteur.getName(), Long.valueOf(acheteur.getCapital()));
            }
            cessions.add(new TupleStringMapMap(dateStr, selectedVendeursMap, selectedAcheteursMap));
            result[0] = true;
            stage.close();
        });

        cancelButton.setOnAction(e -> stage.close());

        VBox inputBoxVendeur = new VBox(5, new Label("Rechercher :"), souscripteurVendeurSearchField, souscripteurVendeurListView);
        VBox inputBoxAcheteur = new VBox(5, new Label("Rechercher :"), souscripteurAcheteurSearchField, souscripteurAcheteurListView);
        HBox buttonBox = new HBox(10, okButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        HBox ListviewBox = new HBox(10, inputBoxVendeur, inputBoxAcheteur);
        VBox layout = new VBox(10,
                new Label("Date de cession :"), datePicker, ListviewBox, buttonBox);
        layout.setPadding(new Insets(15));

        stage.setScene(new Scene(layout));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        return result[0];
    }
}
