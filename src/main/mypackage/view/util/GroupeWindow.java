package mypackage.view.util;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mypackage.model.DataBaseInteractor.GroupInteractor;
import mypackage.model.Group;

public class GroupeWindow {

    public void show() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Ajouter un groupe");

        Label groupLabel = new Label("Nom du groupe :");
        TextField groupField = new TextField();

        Label leaderLabel = new Label("Nom du dirigeant :");
        TextField leaderField = new TextField();
        Label leaderPrenomLabel = new Label("Prenom du dirigeant :");
        TextField leaderPrenomField = new TextField();

        Button confirmButton = new Button("Confirmer");
        Button cancelButton = new Button("Annuler");

        confirmButton.setOnAction(e -> {
            String groupName = groupField.getText();
            String leaderName = leaderField.getText();
            String leaderFirstName = leaderPrenomField.getText();
            Group group = new Group(GroupInteractor.generateNewId(), new SimpleStringProperty(groupName), leaderName, leaderFirstName);
            GroupInteractor.SaveGroup(group);
            dialogStage.close();
        });

        cancelButton.setOnAction(e -> dialogStage.close());

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(15));
        grid.add(groupLabel, 0, 0);
        grid.add(groupField, 1, 0);
        grid.add(leaderLabel, 0, 1);
        grid.add(leaderField, 1, 1);
        grid.add(leaderPrenomLabel, 0, 2);
        grid.add(leaderPrenomField, 1, 2);

        HBox buttons = new HBox(10, confirmButton, cancelButton);
        buttons.setPadding(new Insets(10));
        buttons.setSpacing(10);

        VBox layout = new VBox(10, grid, buttons);
        layout.setPadding(new Insets(10));

        dialogStage.setScene(new Scene(layout));
        dialogStage.showAndWait();
    }
}