package mypackage.view.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Locale;
import java.awt.Desktop;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.element.Cell;


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
import mypackage.model.util.InvestorInfo;
import mypackage.model.util.Replacement;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mypackage.model.Applicant;
import mypackage.model.Investor;
import mypackage.model.InvestorLP;
import mypackage.model.InvestorNP;
import mypackage.model.Obligation;
import mypackage.model.DataBaseInteractor.InvestorInteractor;
import mypackage.model.DataBaseInteractor.ObligationInteractor;
import mypackage.model.DataBaseInteractor.ApplicantInteractor;

public class CouponWindow {
    private static ListView<String[]> souscripteurListView = new ListView<>();
    private static TextField souscripteurSearchField = new TextField();
    private static ObservableList<String[]> allSouscripteurs = FXCollections.observableArrayList();
    private static FilteredList<String[]> filteredSouscripteurs = new FilteredList<>(allSouscripteurs, p -> true);

    private static Double Taux = 0.0;
    private static Double TauxInFine = 0.0;
    private static Double montantCapitalise = 0.0;

    public static boolean show(String[] obligation) {
        Stage stage = new Stage();
        stage.setTitle("Listes des souscripteurs pour le coupon");

        Obligation obligationData = ObligationInteractor.GetObligationByName(obligation[2]);

        allSouscripteurs.clear();
        for(InvestorInfo triplet : obligationData.getInvestors()) {
            Integer souscripteurId = triplet.getInvestorId();
            Long amount = triplet.getCapital();
            allSouscripteurs.add(new String[]{InvestorInteractor.GetInvestor(souscripteurId).getName(), amount.toString(), });
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
                    Taux = 0.0;
                    TauxInFine = 0.0;
                    montantCapitalise = 0.0;
                    int period = 0;
                    if(obligationData.getPeriodicity().equals("Mensuelle")) {
                        period = 1;
                    } else if(obligationData.getPeriodicity().equals("Trimestrielle")) {
                        period = 3;
                    } else if(obligationData.getPeriodicity().equals("Semestrielle")) {
                        period = 6;
                    } else if(obligationData.getPeriodicity().equals("Annuelle")) {
                        period = 12;
                    }
                    if(obligationData.getProrogationActivated() && LocalDate.parse(obligationData.getEndDate()).isBefore(LocalDate.parse(obligation[0]))) {
                        Taux = Double.parseDouble(obligationData.getProrogation()[1]) / 100.0;
                        if(LocalDate.parse(obligationData.getProrogation()[0]).isEqual(LocalDate.parse(obligation[0]))) {
                            TauxInFine = Double.parseDouble(obligationData.getProrogation()[2]) / 100.0;
                            montantCapitalise = (double) MontantInvestiCapitalise(Integer.parseInt(item[1]) * obligationData.getValeurNominale(), obligationData, obligation[0]);
                                period = 12;
                            for(int i = 0; i < Integer.parseInt(obligationData.getProrogation()[0])/12-1; i++) {
                                montantCapitalise = montantCapitalise * (1 + Double.parseDouble(obligationData.getProrogation()[1]) / 100.0);
                            }
                        }
                    } else {
                        Taux = obligationData.getRate()[1] / 100.0;
                        if(LocalDate.parse(obligationData.getEndDate()).isEqual(LocalDate.parse(obligation[0]))) {
                            TauxInFine = obligationData.getRate()[0] / 100.0;
                            montantCapitalise = (double) MontantInvestiCapitalise(Integer.parseInt(item[1]) * obligationData.getValeurNominale(), obligationData, obligation[0]);
                        }
                    }
                    int investorId = InvestorInteractor.GetInvestorByName(item[0]);
                    long nbPart = obligationData.getInvestorCapital(investorId);
                    ArrayList<Replacement> replacements = obligationData.getReplacements();
                    if (replacements != null && !replacements.isEmpty()) {
                        for (Replacement replacement : replacements) {
                            if(LocalDate.parse(replacement.getDate()).isBefore(LocalDate.parse(obligation[0]))) {
                                if (replacement.getInvestorsBuyersId().containsKey(investorId)) {
                                    nbPart += replacement.getInvestorsBuyersId().get(investorId);
                                }
                                if (replacement.getInvestorsSalersId().containsKey(investorId)) {
                                    nbPart -= replacement.getInvestorsSalersId().get(investorId);
                                }
                            }
                        }
                    }
                    double partBrut = nbPart * obligationData.getValeurNominale() * Taux + montantCapitalise * TauxInFine;
                    if(obligationData.getInvestorDate(investorId).trim() != "" && LocalDate.parse(obligation[0]).isAfter(LocalDate.parse(obligationData.getInvestorDate(investorId)))){
                        LocalDate startDateInvestor = LocalDate.parse(obligationData.getInvestorDate(investorId));
                        long daysBetween = ChronoUnit.DAYS.between(startDateInvestor, LocalDate.parse(obligation[0]));
                        if(daysBetween < period * 31) {
                            partBrut = (partBrut / 365) * daysBetween;
                        } 
                    }
                    Label nameLabel = new Label(item[0]);
                    Label amountLabel = new Label();
                    setNumberLabel(amountLabel, String.valueOf(partBrut));
                    Label nameObligLabel = new Label(obligation[2]);
                    Label dateLabel = new Label(obligation[0]);
                    Button buttonAvisdOpere = new Button("A. d'Op");
                    double finalPartBrut = partBrut;
                    buttonAvisdOpere.setOnAction(event -> {
                        ShowAvisdOperePDF(item[0], obligationData, obligation[0], finalPartBrut);
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

    private static void ShowAvisdOperePDF(String investorName, Obligation obligation, String dateCoupon, double partBrut) {
        Stage stage = new Stage();
        Investor investor = InvestorInteractor.GetInvestor(InvestorInteractor.GetInvestorByName(investorName));
        Applicant applicant = ApplicantInteractor.GetApplicant(obligation.getApplicantId());
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

            Table table = new Table(2);
            table.useAllAvailableWidth();

            // En-tête à gauche
            Cell headerCell = new Cell().add(new Paragraph(applicant.getName()));
            headerCell.setTextAlignment(com.itextpdf.layout.properties.TextAlignment.LEFT);
            headerCell.setBorder(null);
            table.addCell(headerCell);

            // Titre surligné à droite
            Cell titleCell = new Cell().add(new Paragraph("AVIS D'OPERATION").setBold().setUnderline());
            titleCell.setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER);
            titleCell.setBorder(null);
            table.addCell(titleCell);

            Cell subHeaderCell = new Cell();
            subHeaderCell.add(new Paragraph(applicant.getLegalStatus()));
            subHeaderCell.add(new Paragraph("au capital de " + applicant.getsocialCapital() + " €"));
            subHeaderCell.add(new Paragraph(applicant.getAddress()[0] + " " + applicant.getAddress()[1] + " " + applicant.getAddress()[2]));
            subHeaderCell.add(new Paragraph(applicant.getAddress()[3] + " " + applicant.getAddress()[4]));       
            subHeaderCell.add(new Paragraph(applicant.getRegisterNumber() + " " + applicant.getcityRCS()));
            subHeaderCell.setTextAlignment(com.itextpdf.layout.properties.TextAlignment.LEFT);
            subHeaderCell.setBorder(null);
            table.addCell(subHeaderCell);

            // Ajout de la table au document
            document.add(table);
            
            if(investor instanceof InvestorLP) {
                InvestorLP investorLP = (InvestorLP) investor;
                Paragraph line1 = new Paragraph(investor.getName());
                line1.setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT);
                document.add(line1);
                Paragraph line2 = new Paragraph(investorLP.getAddress()[0] + " " + investorLP.getAddress()[1] + " " + investorLP.getAddress()[2]);
                line2.setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT);
                document.add(line2);
                Paragraph line3 = new Paragraph(investorLP.getAddress()[3] + " " + investorLP.getAddress()[4]);
                line3.setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT);
                document.add(line3);
            }
            if(investor instanceof InvestorNP) {
                InvestorNP investorNP = (InvestorNP) investor;
                Paragraph line1 = new Paragraph("M./Mme. " + investor.getName());
                line1.setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT);
                document.add(line1);
                Paragraph line2 = new Paragraph(investorNP.getAddress()[0] + " " + investorNP.getAddress()[1] + " " + investorNP.getAddress()[2]);
                line2.setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT);
                document.add(line2);
                Paragraph line3 = new Paragraph(investorNP.getAddress()[3] + " " + investorNP.getAddress()[4]);
                line3.setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT);
                document.add(line3);
            }

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));

            Paragraph place = new Paragraph(applicant.getAddress()[4] + " " + LocalDate.now());
            place.setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT);
            document.add(place);

            document.add(new Paragraph("\n"));

            Paragraph Objet = new Paragraph("Objet : Avis d'opéré " + obligation.getName());
            Objet.setBold().setUnderline();
            document.add(Objet);

            Paragraph l1 = new Paragraph("Chère Madame, cher Monsieur,");
            document.add(l1);
            String convertibleString = obligation.getConvertible() ? " convertible(s)" : " non convertible(s)";
            long nbPart = obligation.getInvestorCapital(investor.getId());
            int investorId = investor.getId();
            ArrayList<Replacement> replacements = obligation.getReplacements();
            if (replacements != null && !replacements.isEmpty()) {
                for (Replacement replacement : replacements) {
                    if(LocalDate.parse(replacement.getDate()).isBefore(LocalDate.parse(dateCoupon))) {
                        if (replacement.getInvestorsBuyersId().containsKey(investorId)) {
                            nbPart += replacement.getInvestorsBuyersId().get(investorId);
                        }
                        if (replacement.getInvestorsSalersId().containsKey(investorId)) {
                            nbPart -= replacement.getInvestorsSalersId().get(investorId);
                        }
                    }
                }
            }
            Paragraph l2 = new Paragraph("Vous êtes titulaire de " + nbPart + " obligation(s) " + convertibleString + " " + obligation.getName() + " en actions.");
            document.add(l2);
            Paragraph l3 = new Paragraph("Nous vous prions de trouver ci-après le détail des opérations réalisées sur vos titres.");
            document.add(l3);
            Paragraph l4 = new Paragraph("Nous vous prions de croire, chère Madame, cher Monsieur, en l'assurance de notre respectueuse considération.");
            document.add(l4);

            document.add(new Paragraph("\n\n\n"));

            Table table2 = new Table(2);
            table2.setWidth(UnitValue.createPercentValue(60));
            table2.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER);

            Cell cell1_1 = new Cell().add(new Paragraph("Détail de l'opération").setBold());
            cell1_1.setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER);
            Cell cell1_2 = new Cell().add(new Paragraph("Montant").setBold());
            cell1_2.setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER);
            Cell cell2_1 = new Cell().add(new Paragraph("Versement coupon du " + dateCoupon + "."));
            cell2_1.add(new Paragraph("    Montant Brut"));
            cell2_1.add(new Paragraph("    Prélèvement libératoire forfaitaire (30%)"));
            cell2_1.add(new Paragraph("    Net en euros"));
            cell2_1.setTextAlignment(com.itextpdf.layout.properties.TextAlignment.LEFT);
            Cell cell2_2 = new Cell().add(new Paragraph("\n"));
            NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
            format.setGroupingUsed(true);
            format.setMinimumFractionDigits(2);
            format.setMaximumFractionDigits(2);

            String formatted = format.format(1234567.89);
            // Affiche chaque caractère et son code unicode pour vérifier
            for (char c : formatted.toCharArray()) {
                System.out.println("'" + c + "' : " + (int)c);
            }

            if(investor instanceof InvestorNP && ((InvestorNP)investor).getAddress()[4].trim().toUpperCase().equals("FRANCE")) {
                cell2_2.add(new Paragraph(format.format(partBrut).replace('\u202F', ' ') + " €"));
                cell2_2.add(new Paragraph("- " + format.format(partBrut * 0.3).replace('\u202F', ' ') + " €"));
                cell2_2.add(new Paragraph(format.format(partBrut * 0.7).replace('\u202F', ' ') + " €"));
            } else {
                cell2_2.add(new Paragraph(format.format(partBrut).replace('\u202F', ' ') + " €"));
                cell2_2.add(new Paragraph(String.valueOf(" - €")));
                cell2_2.add(new Paragraph(format.format(partBrut).replace('\u202F', ' ') + " €"));
            }
            cell2_2.setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT);

            table2.addCell(cell1_1);
            table2.addCell(cell1_2);
            table2.addCell(cell2_1);
            table2.addCell(cell2_2);

            document.add(table2);

            document.add(new Paragraph("\n\n\n\n"));

            Paragraph line1 = new Paragraph(applicant.getName() + " au capital social de " + applicant.getsocialCapital() + "\n" +
                                            applicant.getAddress()[0] + " " + applicant.getAddress()[1] +
                                            " " + applicant.getAddress()[2] + " " + applicant.getAddress()[3] + "\n" +
                                            applicant.getRegisterNumber() + " RCS " + applicant.getcityRCS() + " " + applicant.getAddress()[4]);
            line1.setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER);
            document.add(line1);
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

    static private long MontantInvestiCapitalise(long montantInvesti, Obligation obligation, String CouponDate) {
        LocalDate couponDate = LocalDate.parse(CouponDate);
        LocalDate startDate = LocalDate.parse(obligation.getStartDate());
        long nombreDeMois = ChronoUnit.MONTHS.between(startDate, couponDate);
        for (int i = 1; i < nombreDeMois/12; i++) {
            montantInvesti = (long) (montantInvesti * (1 + obligation.getRate()[0] / 100.0));
        }
        return montantInvesti;
    }

    static public void setNumberLabel(Label label, String numberAsString) {
        try {
            // Nettoyer la chaîne d'entrée (enlever espaces existants, virgules, etc.)
            String cleanString = numberAsString.replaceAll("[\\s,]", "");
            
            // Convertir en nombre
            double value = Double.parseDouble(cleanString);
            
            // Formater avec espaces
            NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
            format.setGroupingUsed(true);
            label.setText(format.format(value) + " €");
        } catch (NumberFormatException e) {
            // En cas d'erreur de format, afficher la chaîne originale ou gérer l'erreur
            label.setText(numberAsString);
            // Ou bien : 
            // label.setText("Format invalide");
        }
    }
}
