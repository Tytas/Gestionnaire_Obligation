package mypackage.controllers;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Map;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.awt.Desktop;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.collections.FXCollections;
import javafx.stage.Stage;

import mypackage.MainApp;
import mypackage.model.Obligation;
import mypackage.model.Investor;
import mypackage.model.DataBaseInteractor.ObligationInteractor;
import mypackage.model.DataBaseInteractor.InvestorInteractor;
import mypackage.view.util.CouponWindow;

public class ControllerHome {
    @FXML
    private Label totalCapitauxLabel;
    @FXML
    private Label totalObligationsRunningLabel;
    @FXML
    private Label totalInvestorsLabel;
    @FXML
    private Label rateWeightedAverageLabel;
    @FXML
    private Label remainDurationAverageLabel;

    @FXML
    private ListView<String[]> couponListView = new ListView<>();
    ObservableList<String[]> couponListViewItems = FXCollections.observableArrayList(); //String[] : "[0] = date, [1] = amount, [2] = obligationName"

    private MainApp mainApp;

    public ControllerHome() {
    }

    @FXML
    private void initialize() {
        
        displayObligationsResume();

        displayCoupon();

    }

    private void displayObligationsResume() {
        ArrayList<Integer> obligationsId = ObligationInteractor.GetAllObligationsId();

        Long totalCapitaux = 0l;
        Integer totalObligationsRunning = 0;
        Integer totalInvestors = 0;
        Long rateWeightedAverage = 0l;
        Long remainDurationDayAverage = 0l;

        for (Integer obligationId : obligationsId) {
            Obligation obligation = ObligationInteractor.GetObligation(obligationId);
            if (obligation != null) {
                if(LocalDate.now().isBefore(LocalDate.parse(obligation.getStartDate()).plusMonths(obligation.getDurationMonths()))) {
                    totalCapitaux += obligation.getCapital();
                    totalObligationsRunning += 1;
                    totalInvestors += obligation.getInvestors().size();
                    rateWeightedAverage += (obligation.getRate()[0] + obligation.getRate()[1]) * obligation.getCapital();
                    remainDurationDayAverage += obligation.getCapital() * ChronoUnit.DAYS.between(
                        LocalDate.now(),
                        LocalDate.parse(obligation.getStartDate()).plusMonths(obligation.getDurationMonths()));
                }
            } else {
                System.err.println("Obligation with ID " + obligationId + " not found.");
            }
        }
        if (totalObligationsRunning > 0) {
            rateWeightedAverage /= totalCapitaux;
            remainDurationDayAverage /= totalCapitaux;
        }
        Integer remainMonths = (int) (remainDurationDayAverage / 30);
        Integer remainDays = (int) (remainDurationDayAverage % 30);

        totalCapitauxLabel.setText(String.valueOf(totalCapitaux));
        totalObligationsRunningLabel.setText(String.valueOf(totalObligationsRunning));
        totalInvestorsLabel.setText(String.valueOf(totalInvestors));
        rateWeightedAverageLabel.setText(String.valueOf(rateWeightedAverage) + " %");
        remainDurationAverageLabel.setText(String.valueOf(remainMonths + " mois " + remainDays + " jours"));
    }

    private void getCoupons() {
        ArrayList<Integer> ObligIds = ObligationInteractor.GetAllObligationsId();
        for (Integer id : ObligIds) {
            Obligation obligation = ObligationInteractor.GetObligation(id);
            int period = 0;
            if(obligation.getPeriodicity() != null) {
                if(obligation.getPeriodicity().equals("Mensuelle")) {
                    period = 1;
                } else if(obligation.getPeriodicity().equals("Trimestrielle")) {
                    period = 3;
                } else if(obligation.getPeriodicity().equals("Semestrielle")) {
                    period = 6;
                } else if(obligation.getPeriodicity().equals("Annuelle")) {
                    period = 12;
                } else {
                    System.err.println("Unknown periodicity: " + obligation.getPeriodicity());
                    continue;
                }
            } else {
                System.err.println("Periodicity not set for obligation ID: " + id);
                continue;
            }
            if(obligation.getRate()[0] != 0){
                for(int i = 0; i < obligation.getDurationMonths(); i += period) {
                    LocalDate couponDate = LocalDate.parse(obligation.getStartDate()).plusMonths(i);
                    String[] coupon = new String[3];
                    coupon[0] = couponDate.toString();
                    coupon[1] = String.valueOf(obligation.getRate()[0] * obligation.getCapital() / 100);
                    coupon[2] = obligation.getName();
                    couponListViewItems.add(coupon);
                }
            }
            if(obligation.getRate()[1] != 0) {
                LocalDate couponDate = LocalDate.parse(obligation.getStartDate()).plusMonths(obligation.getDurationMonths());
                String[] coupon = new String[3];
                coupon[0] = couponDate.toString();
                coupon[1] = String.valueOf(obligation.getRate()[1] * obligation.getCapital() / 100);
                coupon[2] = obligation.getName();
                couponListViewItems.add(coupon);
            }
        }
    }

    @FXML
    private void displayCoupon() {
        getCoupons();
        couponListViewItems.sort((a, b) -> {
            LocalDate dateA = LocalDate.parse(a[0]);
            LocalDate dateB = LocalDate.parse(b[0]);
            return dateA.compareTo(dateB);
        });
        couponListView.setItems(couponListViewItems);
        couponListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String[] item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(null);
                Label dateLabel = new Label();
                Label amountLabel = new Label();
                Label obligationNameLabel = new Label();
                Button buttonOuvrir = new Button("Ouvrir");
                Button buttonPDF = new Button("Excel");
                buttonOuvrir.getStyleClass().add("coupon-list-button");
                buttonPDF.getStyleClass().addAll("coupon-list-button", "coupon-list-excel-button");
                AnchorPane content = new AnchorPane(dateLabel, amountLabel, obligationNameLabel, buttonPDF, buttonOuvrir);
                content.getStyleClass().add("coupon-list-cell");
                AnchorPane.setRightAnchor(buttonOuvrir, 5.0);
                AnchorPane.setRightAnchor(buttonPDF, 60.0);
                AnchorPane.setLeftAnchor(dateLabel, 0.0);
                AnchorPane.setLeftAnchor(amountLabel, 80.0);
                AnchorPane.setLeftAnchor(obligationNameLabel, 150.0);
                AnchorPane.setTopAnchor(dateLabel, 5.0);
                AnchorPane.setTopAnchor(amountLabel, 5.0);
                AnchorPane.setTopAnchor(obligationNameLabel, 5.0);
                AnchorPane.setTopAnchor(buttonPDF, 5.0);
                AnchorPane.setTopAnchor(buttonOuvrir, 5.0);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    dateLabel.setText(item[0]);
                    amountLabel.setText(" " + item[1] +  " €");
                    obligationNameLabel.setText(" " + item[2] +  " ");
                    buttonOuvrir.setOnAction(event -> {
                        CouponWindow.show(item);
                    });
                    buttonPDF.setOnAction(event -> {
                        ShowExcelCoupon(item);
                    });
                }
                setGraphic(content);
            }
        });
    }

    private void ShowExcelCoupon(String[] item) {
        System.out.println("🔄 Début de création du fichier Excel pour le coupon:");
        System.out.println("   - Date: " + item[0]);
        System.out.println("   - Montant: " + item[1]);
        System.out.println("   - Obligation: " + item[2]);
        
        Stage stage = new Stage();
        try {
            // Configuration du FileChooser pour Excel
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer la liste des souscripteurs");
            fileChooser.setInitialFileName("souscripteurs_coupon_" + LocalDate.now() + ".xlsx");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers Excel", "*.xlsx"));

            File selectedFile = fileChooser.showSaveDialog(stage);

            if (selectedFile == null) {
                System.out.println("❌ Annulation de l'enregistrement.");
                return;
            }

            String filename = selectedFile.getAbsolutePath();
            
            // Récupérer l'obligation par son nom
            System.out.println("🔍 Recherche de l'obligation: " + item[2]);
            Obligation obligation = ObligationInteractor.GetObligationByName(item[2]);
            if (obligation == null) {
                System.err.println("❌ Obligation non trouvée : " + item[2]);
                return;
            }
            System.out.println("✅ Obligation trouvée: " + obligation.getName() + " (ID: " + obligation.getId() + ")");

            // Créer le classeur Excel
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Souscripteurs Coupon");

            // Style pour l'en-tête
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Style pour les montants
            CellStyle currencyStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            currencyStyle.setDataFormat(format.getFormat("#,##0.00 €"));

            // Créer l'en-tête d'information
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Liste des Souscripteurs - Coupon du " + item[0]);
            titleCell.setCellStyle(headerStyle);

            Row obligationRow = sheet.createRow(1);
            Cell obligationCell = obligationRow.createCell(0);
            obligationCell.setCellValue("Obligation : " + item[2]);

            Row montantRow = sheet.createRow(2);
            Cell montantCell = montantRow.createCell(0);
            montantCell.setCellValue("Montant total du coupon : " + item[1] + " €");

            // Ligne vide
            sheet.createRow(3);

            // Créer l'en-tête du tableau
            Row headerRow = sheet.createRow(4);

            Cell headerCell1 = headerRow.createCell(0);
            headerCell1.setCellValue("ID Souscripteur");
            headerCell1.setCellStyle(headerStyle);

            Cell headerCell2 = headerRow.createCell(1);
            headerCell2.setCellValue("Nom du Souscripteur");
            headerCell2.setCellStyle(headerStyle);

            Cell headerCell3 = headerRow.createCell(2);
            headerCell3.setCellValue("Nombre de Parts");
            headerCell3.setCellStyle(headerStyle);

            Cell headerCell4 = headerRow.createCell(3);
            headerCell4.setCellValue("Montant Investi (€)");
            headerCell4.setCellStyle(headerStyle);

            Cell headerCell5 = headerRow.createCell(4);
            headerCell5.setCellValue("Part du Coupon (€)");
            headerCell5.setCellStyle(headerStyle);

            // Remplir les données des souscripteurs
            Map<Integer, Long> investors = obligation.getInvestors();
            System.out.println("👥 Nombre de souscripteurs trouvés: " + (investors != null ? investors.size() : 0));
            
            if (investors == null || investors.isEmpty()) {
                System.err.println("⚠️ Aucun souscripteur trouvé pour l'obligation : " + item[2]);
                
                // Créer une ligne indiquant qu'il n'y a pas de souscripteurs
                Row noDataRow = sheet.createRow(5);
                Cell noDataCell = noDataRow.createCell(0);
                noDataCell.setCellValue("Aucun souscripteur trouvé pour cette obligation");
                sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(5, 5, 0, 4));
                
            } else {
                System.out.println("💰 Valeur nominale: " + obligation.getValeurNominale());
                System.out.println("📊 Taux [In Fine, Mensuel]: [" + obligation.getRate()[0] + "%, " + obligation.getRate()[1] + "%]");
                
                int rowIndex = 5;
                double totalPartsCoupon = 0.0;
                long totalPartsCount = 0;
                long totalCapitalInvested = 0;

                for (Map.Entry<Integer, Long> entry : investors.entrySet()) {
                    int investorId = entry.getKey();
                    long nombreParts = entry.getValue();
                    long montantInvesti = nombreParts * obligation.getValeurNominale();
                    double partCoupon = montantInvesti * obligation.getRate()[1] / 100.0;
                
                    // Calculer la part du coupon
                    try {
                        LocalDate obligationEndDate = LocalDate.parse(obligation.getStartDate()).plusMonths(obligation.getDurationMonths());
                        LocalDate couponDate = LocalDate.parse(item[0]);
                        
                        if(obligationEndDate.isEqual(couponDate)) {
                            if(obligation.getRate()[0] != 0) {
                                partCoupon += montantInvesti * obligation.getRate()[0] / 100.0;
                                headerCell5.setCellValue("Part du Coupon (€) ATTENTION IN FINE");
                                System.out.println("📅 Date de coupon égale à la date de fin de l'obligation, ajout du taux In Fine.");
                            }
                        }
                    } catch (Exception dateEx) {
                        System.err.println("⚠️ Erreur de parsing des dates pour l'obligation " + obligation.getName() + ": " + dateEx.getMessage());
                    }
                
                    // Ajouter aux totaux
                    totalPartsCoupon += partCoupon;
                    totalPartsCount += nombreParts;
                    totalCapitalInvested += montantInvesti;

                    // Récupérer l'investisseur
                    Investor investor = InvestorInteractor.GetInvestor(investorId);
                    String investorName = investor != null ? investor.getName() : "Inconnu";

                    Row dataRow = sheet.createRow(rowIndex);
                    
                    // ID Souscripteur
                    Cell cellId = dataRow.createCell(0);
                    cellId.setCellValue(investorId);
                    
                    // Nom du Souscripteur
                    Cell cellName = dataRow.createCell(1);
                    cellName.setCellValue(investorName);
                    
                    // Nombre de parts
                    Cell cellParts = dataRow.createCell(2);
                    cellParts.setCellValue(nombreParts);

                    // Montant Investi
                    Cell cellMontant = dataRow.createCell(3);
                    cellMontant.setCellValue(montantInvesti);
                    cellMontant.setCellStyle(currencyStyle);

                    // Part du Coupon
                    Cell cellCoupon = dataRow.createCell(4);
                    cellCoupon.setCellValue(partCoupon);
                    cellCoupon.setCellStyle(currencyStyle);

                    rowIndex++;
                }

                // Ligne de total
                Row totalRow = sheet.createRow(rowIndex + 1);
                Cell totalLabelCell = totalRow.createCell(1);
                totalLabelCell.setCellValue("TOTAL :");
                totalLabelCell.setCellStyle(headerStyle);

                Cell totalPartsCell = totalRow.createCell(2);
                totalPartsCell.setCellValue(totalPartsCount);

                Cell totalCapitalCell = totalRow.createCell(3);
                totalCapitalCell.setCellValue(totalCapitalInvested);
                totalCapitalCell.setCellStyle(currencyStyle);

                Cell totalCouponCell = totalRow.createCell(4);
                totalCouponCell.setCellValue(totalPartsCoupon);
                totalCouponCell.setCellStyle(currencyStyle);
            
            } // Fin du bloc else (si des investisseurs existent)

            // Ajuster la largeur des colonnes
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);

            // Fusionner les cellules du titre
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 4));
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(1, 1, 0, 4));
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(2, 2, 0, 4));

            // Sauvegarder le fichier
            FileOutputStream fileOut = new FileOutputStream(filename);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();

            // Ouvrir le fichier automatiquement
            File excelFile = new File(filename);
            if (excelFile.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(excelFile);
                } else {
                    System.err.println("❌ Ouverture automatique non supportée sur cette plateforme.");
                }
            }
            
            System.out.println("✅ Fichier Excel généré avec succès : " + filename);

        } catch (FileNotFoundException e) {
            System.err.println("❌ Erreur lors de la création du fichier Excel : " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("❌ Erreur d'écriture du fichier Excel : " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("❌ Erreur de format du montant : " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Erreur générale : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void getMainApp() {
        if (mainApp != null) {
            System.out.println("MainApp is set.");
        } else {
            System.out.println("MainApp is not set.");
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    /**
     * Méthode simple pour générer un PDF avec iText
     * Génère un rapport des obligations en cours
     */
    public void generateSimplePDF() {
        Stage stage = new Stage();
        try {
            // Chemin du fichier PDF
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le rapport PDF");
            fileChooser.setInitialFileName("rapport_obligations_" + LocalDate.now() + ".pdf");
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
            document.add(new Paragraph("Rapport des Obligations")
                .setFontSize(20)
                .setBold());
            
            document.add(new Paragraph("Généré le : " + LocalDate.now().toString())
                .setFontSize(12));
            
            document.add(new Paragraph(" ")); // Ligne vide
            
            // Statistiques générales
            document.add(new Paragraph("Statistiques Générales")
                .setFontSize(16)
                .setBold());
            
            document.add(new Paragraph("Total des capitaux : " + totalCapitauxLabel.getText()));
            document.add(new Paragraph("Nombre d'obligations : " + totalObligationsRunningLabel.getText()));
            document.add(new Paragraph("Nombre d'investisseurs : " + totalInvestorsLabel.getText()));
            document.add(new Paragraph("Taux moyen pondéré : " + rateWeightedAverageLabel.getText()));
            
            document.add(new Paragraph(" ")); // Ligne vide
            
            // Liste des obligations (exemple simple)
            document.add(new Paragraph("Liste des Obligations")
                .setFontSize(16)
                .setBold());
            
            // Récupérer les obligations depuis la base de données
            ArrayList<Integer> obligationsId = ObligationInteractor.GetAllObligationsId();
            
            // Créer un tableau simple (3 colonnes)
            Table table = new Table(3);
            table.setWidth(500);
            
            // En-têtes du tableau
            table.addHeaderCell("ID");
            table.addHeaderCell("Nom");
            table.addHeaderCell("Capital");
            
            // Ajouter les données
            for (Integer id : obligationsId) {
                Obligation obligation = ObligationInteractor.GetObligation(id);
                if (obligation != null) {
                    table.addCell(String.valueOf(obligation.getId()));
                    table.addCell(obligation.getName());
                    table.addCell(String.valueOf(obligation.getCapital()) + " €");
                }
            }
            
            document.add(table);
            
            // Pied de page
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Fin du rapport - Gestionnaire d'Obligations")
                .setFontSize(10)
                .setItalic());
            
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
