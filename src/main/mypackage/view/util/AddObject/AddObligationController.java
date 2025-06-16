package mypackage.view.util.AddObject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

public class AddObligationController {

    private boolean result = false;

    @FXML
    private TextField nomField;
    @FXML
    private TextField capitalField;
    @FXML
    private ToggleGroup convertible;
    @FXML
    private TextField taux_INFINE;
    @FXML
    private TextField taux_TEMP;
    @FXML
    private Button validerButton;
    @FXML
    private Button annulerButton;


    public AddObligationController() {
        // Constructeur vide
    }

    public void initialize() {
        validerButton.setOnAction(event -> {
            String nom = nomField.getText();
            String capitalString = capitalField.getText();
            Boolean isConvertible = false;
            int[] taux = {0, 0}; // [In Fine, mensuelle]
            Long capital = null;
            if(!capitalString.isEmpty()) {
                try {
                    capital = Long.parseLong(capitalString);
                } catch (NumberFormatException e) {
                    // Gérer l'erreur de format de nombre
                    capitalField.setText("Capital invalide");
                    return;
                }
            } else {
                capitalField.setText("Capital requis");
                return;
            }
            if (!taux_INFINE.getText().isEmpty()) {
                try {
                    taux[0] = Integer.parseInt(taux_INFINE.getText());
                } catch (NumberFormatException e) {
                    taux_INFINE.setText("invalide");
                }
            }
            if (!taux_INFINE.getText().isEmpty()) {
                try {
                    taux[0] = Integer.parseInt(taux_INFINE.getText());
                } catch (NumberFormatException e) {
                    taux_INFINE.setText("invalide");
                }
            }
            if (!taux_TEMP.getText().isEmpty()) {
                try {
                    taux[1] = Integer.parseInt(taux_TEMP.getText());
                } catch (NumberFormatException e) {
                    taux_TEMP.setText("invalide");
                }
            }
            RadioButton selected = (RadioButton) convertible.getSelectedToggle();
            if (selected != null && "OCA".equals(selected.getText())) {
                isConvertible = true;
                System.out.println("OCA selected");
            }
            // ici tu peux appeler un interactor ou créer l'objet directement
            if (!nom.isEmpty() && capital != null && taux != null && isConvertible != null) {
                CreateObligation(nom, capital, taux, isConvertible);
                result = true;
                ((Stage) validerButton.getScene().getWindow()).close();
            }
            else {
                // Gérer le cas où les données sont invalides
                if( nom.isEmpty()) {
                    nomField.setText("Nom requis");
                }
                if( capital == null) {
                    capitalField.setText("Capital requis");
                }
                if( taux[0] == 0) {
                    taux_INFINE.setText("Taux In Fine requis");
                }
                if( taux[1] == 0) {
                    taux_TEMP.setText("Taux Mensuel requis");
                }
                if( selected == null) {
                    convertible.selectToggle(null);
                    // Afficher un message d'erreur ou une alerte
                    System.out.println("Veuillez sélectionner un type d'obligation.");
                }
            }
        });
        annulerButton.setOnAction(event -> {
            result = false;
            ((Stage) annulerButton.getScene().getWindow()).close();
        });
    }

    public boolean getResult() {
        return result;
    }

    private void CreateObligation(String nom, Long capital, int[] taux, Boolean isConvertible) {

    }
}
