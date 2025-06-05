package mypackage.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class MyController {

    @FXML
    private AnchorPane contentArea;

    @FXML
    private void handleMenu1() {
        // Changer le contenu de la zone droite pour Menu 1
        contentArea.getChildren().clear();
        // Ajoutez ici le contenu de la page Menu 1
    }

    @FXML
    private void handleMenu2() {
        // Changer le contenu de la zone droite pour Menu 2
        contentArea.getChildren().clear();
        // Ajoutez ici le contenu de la page Menu 2
    }

    @FXML
    private void handleMenu3() {
        // Changer le contenu de la zone droite pour Menu 3
        contentArea.getChildren().clear();
        // Ajoutez ici le contenu de la page Menu 3
    }

    @FXML
    private void handleMenu4() {
        // Changer le contenu de la zone droite pour Menu 4
        contentArea.getChildren().clear();
        // Ajoutez ici le contenu de la page Menu 4
    }
}
