package mypackage.controllers;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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

import mypackage.model.util.InvestorInfo;

import mypackage.MainApp;
import mypackage.model.Obligation;
import mypackage.model.Investor;
import mypackage.model.InvestorNP;
import mypackage.model.InvestorLP;
import mypackage.model.DataBaseInteractor.ObligationInteractor;
import mypackage.model.DataBaseInteractor.ApplicantInteractor;
import mypackage.model.DataBaseInteractor.FamilyInteractor;
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
                    rateWeightedAverage += (obligation.getRate()[1] + obligation.getRate()[0]) * obligation.getCapital();
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
            if(obligation.getRate()[1] != 0){
                for(int i = period; i <= obligation.getDurationMonths(); i += period) {
                    LocalDate couponDate = LocalDate.parse(obligation.getStartDate()).plusMonths(i);
                    if (obligation.getRate()[0] != 0 && couponDate.isEqual(LocalDate.parse(obligation.getStartDate()).plusMonths(obligation.getDurationMonths())) && !obligation.getProrogationActivated()) {
                        String[] coupon = new String[3];
                        coupon[0] = couponDate.toString();
                        coupon[1] = String.valueOf((obligation.getRate()[1]+obligation.getRate()[0]) * obligation.getCapital() / 100);
                        coupon[2] = obligation.getName();
                        couponListViewItems.add(coupon);
                    } else {
                        String[] coupon = new String[3];
                        coupon[0] = couponDate.toString();
                        coupon[1] = String.valueOf(obligation.getRate()[1] * obligation.getCapital() / 100);
                        coupon[2] = obligation.getName();
                        couponListViewItems.add(coupon);
                    }
                }
            } else if(obligation.getRate()[0] != 0 && !obligation.getProrogationActivated()) {
                LocalDate couponDate = LocalDate.parse(obligation.getStartDate()).plusMonths(obligation.getDurationMonths());
                String[] coupon = new String[3];
                coupon[0] = couponDate.toString();
                coupon[1] = String.valueOf(obligation.getRate()[0] * obligation.getCapital() / 100);
                coupon[2] = obligation.getName();
                couponListViewItems.add(coupon);
            }
            if (obligation.getProrogationActivated() && (obligation.getProrogation()[1] != "0" || obligation.getProrogation()[1].trim() != "")) {
                for(int i = period; i <= Integer.parseInt(obligation.getProrogation()[0]); i+= period) {
                    LocalDate prorogationCouponDate = LocalDate.parse(obligation.getStartDate()).plusMonths(obligation.getDurationMonths() + i);
                    if ((obligation.getProrogation()[2] != "0" || obligation.getProrogation()[2] != "") && obligation.getProrogation()[1].trim() != "" && prorogationCouponDate.isEqual(LocalDate.parse(obligation.getStartDate()).plusMonths(obligation.getDurationMonths() + Integer.parseInt(obligation.getProrogation()[0])))) {
                        String[] coupon = new String[3];
                        coupon[0] = prorogationCouponDate.toString();
                        coupon[1] = String.valueOf((Long.parseLong(obligation.getProrogation()[1]) + Long.parseLong(obligation.getProrogation()[2])) * obligation.getCapital() / 100);
                        coupon[2] = obligation.getName();
                        couponListViewItems.add(coupon);
                    } else {
                        String[] coupon = new String[3];
                        coupon[0] = prorogationCouponDate.toString();
                        coupon[1] = String.valueOf(Long.parseLong(obligation.getProrogation()[1]) * obligation.getCapital() / 100);
                        coupon[2] = obligation.getName();
                        couponListViewItems.add(coupon);
                    }
                }
            } else if(obligation.getProrogationActivated() && (obligation.getProrogation()[2] != "0" || obligation.getProrogation()[2] != "")) {
                LocalDate prorogationCouponDate = LocalDate.parse(obligation.getStartDate()).plusMonths(obligation.getDurationMonths() + Integer.parseInt(obligation.getProrogation()[0]));
                String[] coupon = new String[3];
                coupon[0] = prorogationCouponDate.toString();
                coupon[1] = String.valueOf((Long.parseLong(obligation.getProrogation()[2])) * obligation.getCapital() / 100);
                coupon[2] = obligation.getName();
                couponListViewItems.add(coupon);
            }
        }
    }

    @FXML
    private void displayCoupon() {
        getCoupons();
        for(String[] item : couponListViewItems) {
            System.out.println("Coupon: " + item[0] + ", " + item[1] + " €, " + item[2]);
        }
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
                Button buttonExcel = new Button("Excel");
                buttonOuvrir.getStyleClass().add("coupon-list-button");
                buttonExcel.getStyleClass().addAll("coupon-list-button", "coupon-list-excel-button");
                AnchorPane content = new AnchorPane(dateLabel, amountLabel, obligationNameLabel, buttonExcel, buttonOuvrir);
                content.getStyleClass().add("coupon-list-cell");
                AnchorPane.setRightAnchor(buttonOuvrir, 5.0);
                AnchorPane.setRightAnchor(buttonExcel, 60.0);
                AnchorPane.setLeftAnchor(dateLabel, 0.0);
                AnchorPane.setLeftAnchor(amountLabel, 80.0);
                AnchorPane.setLeftAnchor(obligationNameLabel, 150.0);
                AnchorPane.setTopAnchor(dateLabel, 5.0);
                AnchorPane.setTopAnchor(amountLabel, 5.0);
                AnchorPane.setTopAnchor(obligationNameLabel, 5.0);
                AnchorPane.setTopAnchor(buttonExcel, 5.0);
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
                    buttonExcel.setOnAction(event -> {
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

            CellStyle yellowStyle = workbook.createCellStyle();
            yellowStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            yellowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            yellowStyle.setFont(headerFont);

            // Style pour les montants
            CellStyle currencyStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            currencyStyle.setDataFormat(format.getFormat("#,##0.00 €"));

            // Créer l'en-tête d'information
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("EMETTEUR");
            titleCell.setCellStyle(headerStyle);

            Cell titleCell2 = titleRow.createCell(1);
            titleCell2.setCellValue(ApplicantInteractor.GetApplicant(obligation.getApplicantId()).getName());
            titleCell2.setCellStyle(headerStyle);

            Row obligationTypeRow = sheet.createRow(1);
            Cell obligationTypeCell = obligationTypeRow.createCell(0);
            obligationTypeCell.setCellValue("TYPE");
            obligationTypeCell.setCellStyle(headerStyle);

            Cell obligationTypeCell2 = obligationTypeRow.createCell(1);
            obligationTypeCell2.setCellValue(obligation.getConvertible() ? "OCA" : "OS");
            obligationTypeCell2.setCellStyle(headerStyle);

            Row obligationNameRow = sheet.createRow(2);
            Cell obligationNameCell = obligationNameRow.createCell(0);
            obligationNameCell.setCellValue("NOM DE L'OBLIGATION");
            obligationNameCell.setCellStyle(headerStyle);

            Cell obligationNameCell2 = obligationNameRow.createCell(1);
            obligationNameCell2.setCellValue(obligation.getName());
            obligationNameCell2.setCellStyle(headerStyle);

            Row obligationCapitalRow = sheet.createRow(3);
            Cell obligationCapitalCell = obligationCapitalRow.createCell(0);
            obligationCapitalCell.setCellValue("CAPITAL");
            obligationCapitalCell.setCellStyle(headerStyle);

            Cell obligationCapitalCell2 = obligationCapitalRow.createCell(1);
            obligationCapitalCell2.setCellValue(obligation.getCapital());
            obligationCapitalCell2.setCellStyle(headerStyle);

            Row obligationDateRow = sheet.createRow(4);
            Cell obligationDateCell = obligationDateRow.createCell(0);
            obligationDateCell.setCellValue("ECHEANCE FINALE");
            obligationDateCell.setCellStyle(headerStyle);

            Cell obligationDateCell2 = obligationDateRow.createCell(1);
            obligationDateCell2.setCellValue(LocalDate.parse(obligation.getStartDate()).plusMonths(obligation.getDurationMonths()).toString());
            obligationDateCell2.setCellStyle(headerStyle);

            Row obligationRateRow = sheet.createRow(5);
            Cell obligationRateCell = obligationRateRow.createCell(0);
            obligationRateCell.setCellValue("TAUX / TAUX IN FINE");
            obligationRateCell.setCellStyle(headerStyle);

            Cell obligationRateCell2 = obligationRateRow.createCell(1);
            obligationRateCell2.setCellValue(obligation.getRate()[0] + "% / " + obligation.getRate()[1] + "%");
            obligationRateCell2.setCellStyle(headerStyle);

            Row obligationPeriodicityRow = sheet.createRow(6);
            Cell obligationPeriodicityCell = obligationPeriodicityRow.createCell(0);
            obligationPeriodicityCell.setCellValue("PERIODICITE COUPON");
            obligationPeriodicityCell.setCellStyle(headerStyle);

            Cell obligationPeriodicityCell2 = obligationPeriodicityRow.createCell(1);
            obligationPeriodicityCell2.setCellValue(obligation.getPeriodicity());
            obligationPeriodicityCell2.setCellStyle(headerStyle);

            Row obligationDurationRow = sheet.createRow(7);
            Cell obligationDurationCell = obligationDurationRow.createCell(0);
            obligationDurationCell.setCellValue("DUREE");
            obligationDurationCell.setCellStyle(headerStyle);

            Cell obligationDurationCell2 = obligationDurationRow.createCell(1);
            obligationDurationCell2.setCellValue(obligation.getDurationMonths() + " mois");
            obligationDurationCell2.setCellStyle(headerStyle);

            if (obligation.getProrogation()[0] != "") {
                Cell obligationProrogationDateCell = obligationDateRow.createCell(2);
                obligationProrogationDateCell.setCellValue("PROROGATION : " + LocalDate.parse(obligation.getStartDate()).plusMonths( obligation.getDurationMonths() + Integer.parseInt(obligation.getProrogation()[0])).toString());
                obligationProrogationDateCell.setCellStyle(headerStyle);

                Cell obligationProrogationRateCell = obligationRateRow.createCell(2);
                obligationProrogationRateCell.setCellValue("Taux Prorogation : " + obligation.getProrogation()[1] + "%" + " + " + obligation.getProrogation()[2] + "% INFINE");
                obligationProrogationRateCell.setCellStyle(headerStyle);
            
                Cell obligationProrogationDurationCell = obligationDurationRow.createCell(2);
                obligationProrogationDurationCell.setCellValue("Durée Prorogation : " + obligation.getProrogation()[0] + " mois");
                obligationProrogationDurationCell.setCellStyle(headerStyle);}

            // Ligne vide
            sheet.createRow(8);

            // Créer l'en-tête du tableau
            Row headerRow = sheet.createRow(9);

            Cell headerCell1 = headerRow.createCell(0);
            headerCell1.setCellValue("Family Office");
            headerCell1.setCellStyle(headerStyle);

            Cell headerCell2 = headerRow.createCell(1);
            headerCell2.setCellValue("Nom du Souscripteur");
            headerCell2.setCellStyle(headerStyle);

            Cell headerCell3 = headerRow.createCell(2);
            headerCell3.setCellValue("PP / PM");
            headerCell3.setCellStyle(headerStyle);

            Cell headerCell4 = headerRow.createCell(3);
            headerCell4.setCellValue("R / NR");
            headerCell4.setCellStyle(headerStyle);

            Cell headerCell5 = headerRow.createCell(4);
            headerCell5.setCellValue("PLF");
            headerCell5.setCellStyle(headerStyle);

            Cell headerCell6 = headerRow.createCell(5);
            headerCell6.setCellValue("Adresse");
            headerCell6.setCellStyle(headerStyle);

            Cell headerCell7 = headerRow.createCell(6);
            headerCell7.setCellValue("Ville");
            headerCell7.setCellStyle(headerStyle);

            Cell headerCell8 = headerRow.createCell(7);
            headerCell8.setCellValue("Pays");
            headerCell8.setCellStyle(headerStyle);

            Cell headerCell9 = headerRow.createCell(8);
            headerCell9.setCellValue("Nombre D'Obligations");
            headerCell9.setCellStyle(headerStyle);

            Cell headerCell10 = headerRow.createCell(9);
            headerCell10.setCellValue("Montant de Souscritption");
            headerCell10.setCellStyle(headerStyle);

            Cell headerCell11 = headerRow.createCell(10);
            headerCell11.setCellValue("IBAN");
            headerCell11.setCellStyle(headerStyle);

            Cell headerCell12 = headerRow.createCell(11);
            headerCell12.setCellValue("BIC");
            headerCell12.setCellStyle(headerStyle);

            Row headerCouponRow = sheet.createRow(8);
            Cell headerCouponCell = headerCouponRow.createCell(12);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(8, 8, 12, 14));
            headerCouponCell.setCellValue("COUPON " + item[0]);
            headerCouponCell.setCellStyle(headerStyle);

            Cell titleCouponBrutCell = headerRow.createCell(12);
            titleCouponBrutCell.setCellValue("BRUT");
            Cell titleCouponPLFCell = headerRow.createCell(13);
            titleCouponPLFCell.setCellValue("PLF");
            Cell titleCouponNetCell = headerRow.createCell(14);
            titleCouponNetCell.setCellValue("NET");

            if(LocalDate.parse(obligation.getStartDate()).plusMonths(obligation.getDurationMonths()).isEqual(LocalDate.parse(item[0])) ||
               (obligation.getProrogationActivated() && LocalDate.parse(obligation.getStartDate()).plusMonths(obligation.getDurationMonths() + Integer.parseInt(obligation.getProrogation()[0])).isEqual(LocalDate.parse(item[0])))) {
                Cell headerProrogationCell = headerCouponRow.createCell(15);
                sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(8, 8, 15, 17));
                headerProrogationCell.setCellValue("COUPON IN FINE");
                headerProrogationCell.setCellStyle(headerStyle);

                Cell titleCouponInFineBRUTCell = headerRow.createCell(15);
                titleCouponInFineBRUTCell.setCellValue("BRUT");
                Cell titleCouponInFinePLFCell = headerRow.createCell(16);
                titleCouponInFinePLFCell.setCellValue("PLF");
                Cell titleCouponInFineNETCell = headerRow.createCell(17);
                titleCouponInFineNETCell.setCellValue("NET");
            }

            // Remplir les données des souscripteurs
            ArrayList<InvestorInfo> investors = obligation.getInvestors();
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
                
                int rowIndex = 10;

                for (InvestorInfo info : investors) {
                    int investorId = info.getInvestorId();
                    long nombreParts = info.getCapital();
                    long montantInvesti = nombreParts * obligation.getValeurNominale();
                    long montantInvestiInFine = 0L;
                    double partBrut = montantInvesti * obligation.getRate()[1] / 100.0;
                    double partPLF = 0.0;
                    double partNet = partBrut;
                    double partBrutInFine = 0.0;
                    double partPLFInFine = 0.0;
                    double partNetInFine = 0.0;
                    LocalDate obligationEndDate = LocalDate.parse(obligation.getStartDate()).plusMonths(obligation.getDurationMonths());
                    LocalDate couponDate = LocalDate.parse(item[0]);
                    if(obligationEndDate.isEqual(couponDate) && !obligation.getProrogationActivated()) {
                        if(obligation.getRate()[0] != 0) {
                            montantInvestiInFine = MontantInvestiCapitalise(montantInvesti, obligation, item[0]);
                            partBrutInFine = montantInvestiInFine * obligation.getRate()[0] / 100.0;
                            partNetInFine = partBrutInFine;
                        }
                    }

                    if(obligation.getProrogationActivated()) {
                        obligationEndDate = LocalDate.parse(obligation.getStartDate()).plusMonths(obligation.getDurationMonths() + Integer.parseInt(obligation.getProrogation()[0]));
                        if(obligationEndDate.isEqual(couponDate)) {
                            if(obligation.getProrogation()[2] != "0" && obligation.getProrogation()[2] != "") {
                                montantInvestiInFine = MontantInvestiCapitalise(montantInvesti, obligation, item[0]);
                                int period = 0;
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
                                for(int i = 0; i < Integer.parseInt(obligation.getProrogation()[0])/period-1; i++) {
                                    montantInvestiInFine = (long) (montantInvestiInFine * (1 + Double.parseDouble(obligation.getProrogation()[1]) / 100.0));
                                }
                                partBrutInFine = montantInvestiInFine * Double.parseDouble(obligation.getProrogation()[2]) / 100.0;
                                partNetInFine = partBrutInFine;
                                System.out.println("📅 Date de coupon égale à la date de fin de l'obligation, ajout du taux In Fine.");
                            }
                        }
                    } 
                    

                    // Récupérer l'investisseur
                    Investor investor = InvestorInteractor.GetInvestor(investorId);
                    String investorName = investor != null ? investor.getName() : "Inconnu";
                    String investorPMorPP = investor != null ? (investor instanceof InvestorNP ? "PP" : "PM") : "Inconnu";
                    String investorResidence = "";
                    String address = "";
                    String ville = "";
                    String pays = "";
                    String investorIBAN = "";
                    String investorBIC = "";
                    if(investor != null && investor instanceof InvestorLP) {
                        InvestorLP investorLP = (InvestorLP) investor;
                        investorResidence = "";
                        address = investorLP.getAddress()[0] + " " +
                                  investorLP.getAddress()[1] + ", " +
                                  investorLP.getAddress()[5];
                        ville = investorLP.getAddress()[2] + ", " +
                                investorLP.getAddress()[3];
                        pays = investorLP.getAddress()[4];
                        investorIBAN = investorLP.getIBAN();
                        investorBIC = investorLP.getBIC();
                    } else if(investor != null && investor instanceof InvestorNP) {
                        InvestorNP investorNP = (InvestorNP) investor;
                        if (investorNP.getAddress()[4].equals("France")) {
                            investorResidence = "R";
                        } else {
                            investorResidence = "NR";
                        }
                        address = investorNP.getAddress()[0] + " " +
                                  investorNP.getAddress()[1] + ", " +
                                  investorNP.getAddress()[5];
                        ville = investorNP.getAddress()[2] + ", " +
                                investorNP.getAddress()[3];
                        pays = investorNP.getAddress()[4];
                        investorIBAN = investorNP.getIBAN();
                        investorBIC = investorNP.getBIC();
                    } else {
                        System.err.println("❌ Investisseur non trouvé ou de type inconnu pour l'ID: " + investorId);
                        continue; // Passer à l'investisseur suivant
                    }
                    String PLF = "0";
                    if (investorResidence.equals("R")) {
                        PLF = "0.3";
                        partPLF = partBrut * 0.3;
                        partNet = partBrut - partPLF;
                        if(montantInvestiInFine != 0) {
                            partPLFInFine = partBrutInFine * 0.3;
                            partNetInFine = partBrutInFine - partPLFInFine;
                        }
                    }

                    Row dataRow = sheet.createRow(rowIndex);
                    
                    // ID Souscripteur
                    Cell cellFamily = dataRow.createCell(0);
                    cellFamily.setCellValue(FamilyInteractor.GetFamily(investor.getFamilyId()).getName());

                    // Nom du Souscripteur
                    Cell cellName = dataRow.createCell(1);
                    cellName.setCellValue(investorName);
                    
                    Cell cellPPorPM = dataRow.createCell(2);
                    cellPPorPM.setCellValue(investorPMorPP);

                    Cell cellResident = dataRow.createCell(3);
                    cellResident.setCellValue(investorResidence);

                    Cell cellPLF = dataRow.createCell(4);
                    cellPLF.setCellValue(PLF);

                    Cell cellAddress = dataRow.createCell(5);
                    cellAddress.setCellValue(address);

                    Cell cellVille = dataRow.createCell(6);
                    cellVille.setCellValue(ville);

                    Cell cellPays = dataRow.createCell(7);
                    cellPays.setCellValue(pays);

                    Cell cellNombreParts = dataRow.createCell(8);
                    cellNombreParts.setCellValue(nombreParts);

                    Cell cellMontantInvesti = dataRow.createCell(9);
                    cellMontantInvesti.setCellValue(montantInvesti);
                    cellMontantInvesti.setCellStyle(currencyStyle);

                    Cell cellIBAN = dataRow.createCell(10);
                    cellIBAN.setCellValue(investorIBAN);

                    Cell cellBIC = dataRow.createCell(11);
                    cellBIC.setCellValue(investorBIC);

                    if(!(!info.getDate().isEmpty() && LocalDate.parse(info.getDate()).isAfter(LocalDate.parse(item[0])))) {
                        //TODO prendre en compte le temps entre la date d'arrivée et la date du coupon avec la formule adéquate
                        Cell cellCouponBrut = dataRow.createCell(12);
                        cellCouponBrut.setCellValue(partBrut);
                        cellCouponBrut.setCellStyle(currencyStyle);

                        Cell cellCouponPLF = dataRow.createCell(13);
                        cellCouponPLF.setCellValue(partPLF);
                        cellCouponPLF.setCellStyle(currencyStyle);

                        Cell cellCouponNet = dataRow.createCell(14);
                        cellCouponNet.setCellValue(partNet);
                        cellCouponNet.setCellStyle(currencyStyle);
                    }

                    if(montantInvestiInFine != 0) {
                        Cell cellCouponInfineBrut = dataRow.createCell(15);
                        cellCouponInfineBrut.setCellValue(partBrutInFine);
                        cellCouponInfineBrut.setCellStyle(currencyStyle);
                        Cell cellCouponInfinePLF = dataRow.createCell(16);
                        cellCouponInfinePLF.setCellValue(partPLFInFine);
                        cellCouponInfinePLF.setCellStyle(currencyStyle);
                        Cell cellCouponInfineNet = dataRow.createCell(17);
                        cellCouponInfineNet.setCellValue(partNetInFine);
                        cellCouponInfineNet.setCellStyle(currencyStyle);
                    }

                    rowIndex++;
                }

                // Ligne de total
                Row totalRow = sheet.createRow(rowIndex);
                Cell totalLabelCell = totalRow.createCell(1);
                totalLabelCell.setCellValue("TOTAL :");
                totalLabelCell.setCellStyle(headerStyle);

                Cell totalNombrePartsCell = totalRow.createCell(8);
                totalNombrePartsCell.setCellFormula("SUM(I11:I" + rowIndex + ")");

                Cell totalMontantInvestiCell = totalRow.createCell(9);
                totalMontantInvestiCell.setCellFormula("SUM(J11:J" + rowIndex + ")");
                totalMontantInvestiCell.setCellStyle(currencyStyle);

                Cell totalBrutCell = totalRow.createCell(12);
                totalBrutCell.setCellFormula("SUM(M11:M" + rowIndex + ")");
                totalBrutCell.setCellStyle(currencyStyle);

                Cell totalPLFCell = totalRow.createCell(13);
                totalPLFCell.setCellFormula("SUM(N11:N" + rowIndex + ")");
                totalPLFCell.setCellStyle(currencyStyle);

                Cell totalNetCell = totalRow.createCell(14);
                totalNetCell.setCellFormula("SUM(O11:O" + rowIndex + ")");
                totalNetCell.setCellStyle(currencyStyle);

                if(obligation.getRate()[0]!=0) {
                    Cell totalInfineBrutCell = totalRow.createCell(15);
                    totalInfineBrutCell.setCellFormula("SUM(P11:P" + rowIndex + ")");
                    totalInfineBrutCell.setCellStyle(currencyStyle);
                    Cell totalInfinePLFCell = totalRow.createCell(16);
                    totalInfinePLFCell.setCellFormula("SUM(Q11:Q" + rowIndex + ")");
                    totalInfinePLFCell.setCellStyle(currencyStyle);
                    Cell totalInfineNetCell = totalRow.createCell(17);
                    totalInfineNetCell.setCellFormula("SUM(R11:R" + rowIndex + ")");
                    totalInfineNetCell.setCellStyle(currencyStyle);
                }
            } 

            // Ajuster la largeur des colonnes
            for (int i = 0; i <= 17; i++) {
                sheet.autoSizeColumn(i);
            }

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

    private long MontantInvestiCapitalise(long montantInvesti, Obligation obligation, String CouponDate) {
        int nbCouponEcoules = 0;
        LocalDate couponDate = LocalDate.parse(CouponDate);
        LocalDate startDate = LocalDate.parse(obligation.getStartDate());
        long nombreDeMois = ChronoUnit.MONTHS.between(startDate, couponDate);
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
                return montantInvesti;
            }
        }
        nbCouponEcoules = (int) (nombreDeMois / period);
        for (int i = 1; i < nbCouponEcoules; i++) {
            montantInvesti = (long) (montantInvesti * (1 + obligation.getRate()[1] / 100.0));
        }
        return montantInvesti;
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
