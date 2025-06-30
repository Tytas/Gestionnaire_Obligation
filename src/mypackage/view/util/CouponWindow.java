package mypackage.view.util;

import java.util.Map;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mypackage.model.Obligation;
import mypackage.model.DataBaseInteractor.InvestorInteractor;
import mypackage.model.DataBaseInteractor.ObligationInteractor;

public class CouponWindow {
    private static ListView<String[]> souscripteurListView = new ListView<>();
    private static TextField souscripteurSearchField = new TextField();
    private static ObservableList<String[]> allSouscripteurs = FXCollections.observableArrayList();
    private static FilteredList<String[]> filteredSouscripteurs = new FilteredList<>(allSouscripteurs, p -> true);

    public static boolean show(String[] obligation) {
        Stage stage = new Stage();
        stage.setTitle("Listes des souscripteurs pour le coupon");

        Obligation obligationData = ObligationInteractor.GetObligationByName(obligation[2]);

        allSouscripteurs.clear();
        for(Map.Entry<Integer, Long> entry : obligationData.getInvestors().entrySet()) {
            Integer souscripteurId = entry.getKey();
            Long amount = entry.getValue();
            allSouscripteurs.add(new String[]{InvestorInteractor.GetInvestor(souscripteurId).getName(), amount.toString()});
        }

        souscripteurListView.setItems(filteredSouscripteurs);
        souscripteurSearchField.textProperty().addListener((obs, oldValue, newValue) -> {
            filteredSouscripteurs.setPredicate(souscripteur -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return souscripteur[0].toLowerCase().contains(lowerCaseFilter);
            });
        });
        souscripteurListView.setCellFactory(lv -> new ListCell<String[]>() {
            @Override
            protected void updateItem(String[] item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Label nameLabel = new Label(item[0]);
                    Label amountLabel = new Label(Integer.parseInt(item[1]) * obligationData.getValeurNominale() + " €");
                    Label nameObligLabel = new Label(obligation[2]);
                    Label dateLabel = new Label(obligation[0]);
                    Button buttonAvisdOpere = new Button("A. d'Op");
                    buttonAvisdOpere.setOnAction(event -> {
                        ShowAvisdOperePDF(item);
                    });
                    HBox content = new HBox(10, nameLabel, amountLabel, nameObligLabel, dateLabel, buttonAvisdOpere);
                    setGraphic(content);
                }
            }
        });

        Button okButton = new Button("OK");

        final boolean[] result = {false};

        okButton.setOnAction(e -> {            
            stage.close();
        });

        VBox inputBox = new VBox(5, new Label("Souscripteurs :"), souscripteurSearchField, souscripteurListView);
        HBox buttonBox = new HBox(10, okButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox mainLayout = new VBox(10, inputBox, buttonBox);
        mainLayout.setPadding(new Insets(10));
        Scene scene = new Scene(mainLayout, 600, 400);
        stage.setScene(scene);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        return result[0];
    }

    private static void ShowAvisdOperePDF(String[] item) {
        System.out.println("Affichage de l'avis d'opéré pour : " + item[0]);
    }
}
