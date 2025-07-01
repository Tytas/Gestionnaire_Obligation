package mypackage.view.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Map;
import java.awt.Desktop;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

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
import javafx.stage.FileChooser;
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
                        ShowAvisdOperePDF(item, obligation);
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

    private static void ShowAvisdOperePDF(String[] item, String[] obligation) {
        System.out.println("Affichage de l'avis d'opéré pour : " + item[0]);
        Stage stage = new Stage();
        try {
            // Chemin du fichier PDF
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le rapport PDF");
            fileChooser.setInitialFileName("avis_d_opere_" + LocalDate.now() + ".pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));

            File selectedFile = fileChooser.showSaveDialog(stage);

            if (selectedFile == null) {
                System.out.println("❌ Annulation de l'enregistrement.");
                return;
            }

            String filename = selectedFile.getAbsolutePath();
            
            // Créer le document PDF
            PdfWriter writer = new PdfWriter(filename);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Titre du document
            document.add(new Paragraph("Avis d'Opérés de " + item[0])
                .setFontSize(20)
                .setBold());
            
            document.add(new Paragraph(" ")); // Ligne vide

            document.add(new Paragraph("Détails du Coupon")
                .setFontSize(16)
                .setBold());
            document.add(new Paragraph("Nom de L'Obligation : " + obligation[2])
                .setFontSize(12));
            document.add(new Paragraph("Montant du coupon : " + item[1] + " €")
                .setFontSize(12));

            // Fermer le document
            document.close();

            File pdfFile = new File(filename);
            if (pdfFile.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(pdfFile);
                } else {
                    System.err.println("❌ Ouverture automatique non supportée sur cette plateforme.");
                }
            }
            
            System.out.println("✅ PDF généré avec succès : " + filename);

        } catch (FileNotFoundException e) {
            System.err.println("❌ Erreur lors de la création du PDF : " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Erreur générale : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
