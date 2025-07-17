package mypackage.view.consult;

import mypackage.model.Obligation;
import mypackage.model.DataBaseInteractor.InvestorInteractor;
import mypackage.model.DataBaseInteractor.ApplicantInteractor;
import mypackage.model.DataBaseInteractor.FamilyInteractor;
import mypackage.model.Family;
import mypackage.model.Investor;
import mypackage.model.InvestorLP;
import mypackage.model.InvestorNP;
import mypackage.model.util.InvestorInfo;
import mypackage.model.util.Replacement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.awt.Desktop;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;


public class ConsultObligationController {

    private boolean result = false;

    private Obligation obligation;

    private Family selectedFamily;

    @FXML
    private ComboBox<String> familyComboBox;
    @FXML
    private Button validerButton;
    @FXML
    private Button annulerButton;


    public ConsultObligationController() {
    }
    public void initialize() {
    }

    public void setObligation(Obligation obligation) {
        this.obligation = obligation;
    }

    public void init() {
        // Initialisation des composants
        ArrayList<Integer> investorIdList = new ArrayList<>();
        for (InvestorInfo investor : obligation.getInvestors()) {
            investorIdList.add(investor.getInvestorId());
        }
        ArrayList<Integer> familyIdList = new ArrayList<>();

        for (Integer investorId : investorIdList) {
            if(!familyIdList.contains(InvestorInteractor.GetInvestor(investorId).getFamilyId())){
                familyIdList.add(InvestorInteractor.GetInvestor(investorId).getFamilyId());
            }
        }

        for (Integer familyId : familyIdList) {
            selectedFamily = FamilyInteractor.GetFamily(familyId);
            if (selectedFamily != null) {
                familyComboBox.getItems().add(selectedFamily.getName());
            }
        }
        familyComboBox.getItems().add("Toutes");
        familyComboBox.getSelectionModel().select("Toutes");

        validerButton.setOnAction(event -> {
            boolean hasError = false;

            String selectedFamilyName = familyComboBox.getSelectionModel().getSelectedItem();
            ShowExcel(selectedFamilyName);
            if (!hasError) {
                result = true;
                ((Stage) validerButton.getScene().getWindow()).close();
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

    private void ShowExcel(String selectedFamilyName) {
        System.out.println("🔄 Début de création du fichier Excel pour le coupon:");
        System.out.println("   - Date: " + obligation.getStartDate());
        System.out.println("   - Montant: " + obligation.getValeurNominale());
        System.out.println("   - Obligation: " + obligation.getName());

        Stage stage = new Stage();
        try {
            // Configuration du FileChooser pour Excel
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer la liste des souscripteurs");
            fileChooser.setInitialFileName( obligation.getName() + "_consultation.xlsx");
            if(selectedFamilyName != null && !selectedFamilyName.equals("Toutes")) {
                fileChooser.setInitialFileName(obligation.getName() + "_consultation_" + selectedFamilyName + ".xlsx");
            }
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers Excel", "*.xlsx"));

            File selectedFile = fileChooser.showSaveDialog(stage);

            if (selectedFile == null) {
                System.out.println("❌ Annulation de l'enregistrement.");
                return;
            }

            String filename = selectedFile.getAbsolutePath();
            
            System.out.println("✅ Obligation trouvée: " + obligation.getName() + " (ID: " + obligation.getId() + ")");

            // Créer le classeur Excel
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Obligation - " + obligation.getName());

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
            obligationRateCell2.setCellValue(obligation.getRate()[1] + "% / " + obligation.getRate()[0] + "%");
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
                obligationProrogationDurationCell.setCellStyle(headerStyle);
            }
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

            ArrayList<Cell> headerListCoupon = new ArrayList<>();

            ArrayList<String[]> listCoupon = obligation.listCouponGetter();

            Row headerCouponRow = sheet.createRow(8);
            for (int i = 0; i < 3*listCoupon.size(); i+=3) {
                Cell headerCouponCell = headerCouponRow.createCell(12 + i);
                sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(8, 8, 12 + i, 14 + i));
                headerCouponCell.setCellValue("COUPON " + listCoupon.get(i/3)[0]);
                headerCouponCell.setCellStyle(headerStyle);

                headerListCoupon.add(headerCouponCell);

                Cell titleCouponBrutCell = headerRow.createCell(12 + i);
                titleCouponBrutCell.setCellValue("BRUT");
                Cell titleCouponPLFCell = headerRow.createCell(13 + i);
                titleCouponPLFCell.setCellValue("PLF");
                Cell titleCouponNetCell = headerRow.createCell(14 + i);
                titleCouponNetCell.setCellValue("NET");
            }

            if(LocalDate.parse(obligation.getStartDate()).plusMonths(obligation.getDurationMonths()).isEqual(LocalDate.parse(listCoupon.get(listCoupon.size() - 1)[0])) ||
               (obligation.getProrogationActivated() && LocalDate.parse(obligation.getStartDate()).plusMonths(obligation.getDurationMonths() + Integer.parseInt(obligation.getProrogation()[0])).isEqual(LocalDate.parse(listCoupon.get(listCoupon.size() - 1)[0])))) {
                Cell headerProrogationCell = headerCouponRow.createCell(12 + 3*listCoupon.size());
                sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(8, 8, 12 + 3*listCoupon.size(), 14 + 3*listCoupon.size()));
                headerProrogationCell.setCellValue("COUPON IN FINE");
                headerProrogationCell.setCellStyle(headerStyle);

                Cell titleCouponInFineBRUTCell = headerRow.createCell(12 + 3*listCoupon.size());
                titleCouponInFineBRUTCell.setCellValue("BRUT");
                Cell titleCouponInFinePLFCell = headerRow.createCell(13 + 3*listCoupon.size());
                titleCouponInFinePLFCell.setCellValue("PLF");
                Cell titleCouponInFineNETCell = headerRow.createCell(14 + 3*listCoupon.size());
                titleCouponInFineNETCell.setCellValue("NET");
            }

            // Remplir les données des souscripteurs
            ArrayList<InvestorInfo> investors = obligation.getInvestors();
            System.out.println("👥 Nombre de souscripteurs trouvés: " + (investors != null ? investors.size() : 0));
            if(!selectedFamilyName.equals("Toutes")) {
                investors.removeIf(investor -> !FamilyInteractor.GetFamily(InvestorInteractor.GetInvestor(investor.getInvestorId()).getFamilyId()).getName().equals(selectedFamilyName));
            }
            
            if (investors == null || investors.isEmpty()) {
                System.err.println("⚠️ Aucun souscripteur trouvé pour l'obligation : " + obligation.getName());
                
                // Créer une ligne indiquant qu'il n'y a pas de souscripteurs
                Row noDataRow = sheet.createRow(9);
                Cell noDataCell = noDataRow.createCell(0);
                noDataCell.setCellValue("Aucun souscripteur trouvé pour cette obligation");
                sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(9, 9, 0, 4 + headerListCoupon.size()));
            } else {
                System.out.println("💰 Valeur nominale: " + obligation.getValeurNominale());
                System.out.println("📊 Taux [In Fine, Mensuel]: [" + obligation.getRate()[0] + "%, " + obligation.getRate()[1] + "%]");
                
                int rowIndex = 10; // Commencer après l'en-tête

                for (InvestorInfo info : investors) {
                    int investorId = info.getInvestorId();
                    long nombreParts = info.getCapital();
                    ArrayList<Replacement> replacements = obligation.getReplacements();
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
                    long montantInvesti = nombreParts * obligation.getValeurNominale();
                    long montantInvestiInFine = 0L;
                    double partBrut = montantInvesti * obligation.getRate()[1] / 100.0;
                    partBrut /= 12.0 / period;
                    double partPLF = 0.0;
                    double partNet = partBrut;
                    double partBrutInFine = 0.0;
                    double partPLFInFine = 0.0;
                    double partNetInFine = 0.0;
                    LocalDate obligationEndDate = LocalDate.parse(obligation.getStartDate()).plusMonths(obligation.getDurationMonths());
                    LocalDate lastCouponDate = LocalDate.parse(listCoupon.get(listCoupon.size() - 1)[0]);
                    if(obligationEndDate.isEqual(lastCouponDate) && !obligation.getProrogationActivated()) {
                        if(obligation.getRate()[0] != 0) {
                            montantInvestiInFine = MontantInvestiCapitalise(montantInvesti, obligation, listCoupon.get(listCoupon.size() - 1)[0]);
                            partBrutInFine = montantInvestiInFine * obligation.getRate()[0] / 100.0;
                            partNetInFine = partBrutInFine;
                        }
                    }
                    if(obligation.getProrogationActivated()) {
                        obligationEndDate = LocalDate.parse(obligation.getStartDate()).plusMonths(obligation.getDurationMonths() + Integer.parseInt(obligation.getProrogation()[0]));
                        if(obligationEndDate.isEqual(lastCouponDate)) {
                            if(obligation.getProrogation()[2] != "0" && obligation.getProrogation()[2] != "") {
                                montantInvestiInFine = MontantInvestiCapitalise(montantInvesti, obligation, listCoupon.get(listCoupon.size() - 1)[0]);
                                for(int i = 0; i < Integer.parseInt(obligation.getProrogation()[0])/12-1; i++) {
                                    montantInvestiInFine = (long) (montantInvestiInFine * (1 + Double.parseDouble(obligation.getProrogation()[1]) / 100.0));
                                }
                                partBrutInFine = montantInvestiInFine * Double.parseDouble(obligation.getProrogation()[2]) / 100.0;
                                partNetInFine = partBrutInFine;
                                System.out.println("📅 Date de coupon égale à la date de fin de l'obligation, ajout du taux In Fine.");
                            }
                        }
                    }
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
                        if (investorLP.getAddress()[4].equals("France")) {
                            investorResidence = "R";
                        } else {
                            investorResidence = "NR";
                        }
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

                    // Calculer la part du coupon
                    for (int i = 0; i < 3*listCoupon.size(); i+=3) {
                        partBrut = partBrut / nombreParts;
                        if (replacements != null && !replacements.isEmpty()) { 
                            for (Replacement replacement : replacements) {
                                if(LocalDate.parse(replacement.getDate()).isBefore(LocalDate.parse(listCoupon.get(i/3)[0]))) {
                                    if (replacement.getInvestorsBuyersId().containsKey(investorId)) {
                                        nombreParts += replacement.getInvestorsBuyersId().get(investorId);
                                    }
                                    if (replacement.getInvestorsSalersId().containsKey(investorId)) {
                                        nombreParts -= replacement.getInvestorsSalersId().get(investorId);
                                    }
                                }
                            }
                        }
                        partBrut = partBrut * nombreParts;
                        if(LocalDate.parse(listCoupon.get(i/3)[0]).isAfter(LocalDate.parse(obligation.getStartDate()).plusMonths(obligation.getDurationMonths()))) {
                            partBrut = montantInvesti * Double.parseDouble(obligation.getProrogation()[1]) / 100.0;
                            if(partPLF != 0.0){
                                partPLF = partBrut * 0.3;
                                partNet = partBrut - partPLF;
                            } else {
                                partPLF = 0.0;
                                partNet = partBrut;
                            }
                        }
                        if(!info.getDate().isEmpty() && LocalDate.parse(info.getDate()).isAfter(LocalDate.parse(listCoupon.get(i/3)[0]))) {
                            continue;
                        } 
                        if(!info.getDate().isEmpty() && LocalDate.parse(listCoupon.get(i/3)[0]).isAfter(LocalDate.parse(info.getDate()))) {
                            long daysBetween = ChronoUnit.DAYS.between(LocalDate.parse(info.getDate()), LocalDate.parse(listCoupon.get(i/3)[0]));
                            if(daysBetween < period * 31) {
                                Cell cellCouponBrut = dataRow.createCell(12 + i);
                                cellCouponBrut.setCellValue((partBrut / 365) * daysBetween);
                                cellCouponBrut.setCellStyle(currencyStyle);

                                Cell cellCouponPLF = dataRow.createCell(13 + i);
                                cellCouponPLF.setCellValue((partPLF / 365) * daysBetween);
                                cellCouponPLF.setCellStyle(currencyStyle);

                                Cell cellCouponNet = dataRow.createCell(14 + i);
                                cellCouponNet.setCellValue((partNet / 365) * daysBetween);
                                cellCouponNet.setCellStyle(currencyStyle);
                            }else {
                                Cell cellCouponBrut = dataRow.createCell(12 + i);
                                cellCouponBrut.setCellValue(partBrut);
                                cellCouponBrut.setCellStyle(currencyStyle);

                                Cell cellCouponPLF = dataRow.createCell(13 + i);
                                cellCouponPLF.setCellValue(partPLF);
                                cellCouponPLF.setCellStyle(currencyStyle);

                                Cell cellCouponNet = dataRow.createCell(14 + i);
                                cellCouponNet.setCellValue(partNet);
                                cellCouponNet.setCellStyle(currencyStyle);
                            }
                        } else {
                            Cell cellCouponBrut = dataRow.createCell(12 + i);
                            cellCouponBrut.setCellValue(partBrut);
                            cellCouponBrut.setCellStyle(currencyStyle);

                            Cell cellCouponPLF = dataRow.createCell(13 + i);
                            cellCouponPLF.setCellValue(partPLF);
                            cellCouponPLF.setCellStyle(currencyStyle);

                            Cell cellCouponNet = dataRow.createCell(14 + i);
                            cellCouponNet.setCellValue(partNet);
                            cellCouponNet.setCellStyle(currencyStyle);
                        }
                    }

                    if(montantInvestiInFine != 0) {
                        Cell cellCouponInfineBrut = dataRow.createCell(12 + 3*listCoupon.size());
                        cellCouponInfineBrut.setCellValue(partBrutInFine);
                        cellCouponInfineBrut.setCellStyle(currencyStyle);
                        Cell cellCouponInfinePLF = dataRow.createCell(13 + 3*listCoupon.size());
                        cellCouponInfinePLF.setCellValue(partPLFInFine);
                        cellCouponInfinePLF.setCellStyle(currencyStyle);
                        Cell cellCouponInfineNet = dataRow.createCell(14 + 3*listCoupon.size());
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
                totalNombrePartsCell.setCellFormula("SUM(I11:I" + (rowIndex) + ")");

                Cell totalMontantInvestiCell = totalRow.createCell(9);
                totalMontantInvestiCell.setCellFormula("SUM(J11:J" + (rowIndex) + ")");
                totalMontantInvestiCell.setCellStyle(currencyStyle);

                for (int i = 0; i < 3*headerListCoupon.size(); i+=3) {

                    // Calculez les colonnes de base pour chaque itération
                    String brutCol = getColumnReference(12 + i);
                    String plfCol = getColumnReference(13 + i);
                    String netCol = getColumnReference(14 + i);
    
                    Cell totalBrutCell = totalRow.createCell(12 + i);
                    totalBrutCell.setCellFormula("SUM(" + brutCol + "11:" + brutCol + (rowIndex) + ")");
                    totalBrutCell.setCellStyle(currencyStyle);

                    Cell totalPLFCell = totalRow.createCell(13 + i);
                    totalPLFCell.setCellFormula("SUM(" + plfCol + "11:" + plfCol + (rowIndex) + ")");
                    totalPLFCell.setCellStyle(currencyStyle);

                    Cell totalNetCell = totalRow.createCell(14 + i);
                    totalNetCell.setCellFormula("SUM(" + netCol + "11:" + netCol + (rowIndex) + ")");
                    totalNetCell.setCellStyle(currencyStyle);
                }
                if(obligation.getRate()[0]!=0) {
                    Cell totalInfineBrutCell = totalRow.createCell(12 + 3*headerListCoupon.size());
                    totalInfineBrutCell.setCellFormula("SUM(" + getColumnReference(12 + 3*headerListCoupon.size()) + "11:" + getColumnReference(12 + 3*headerListCoupon.size()) + rowIndex + ")");
                    totalInfineBrutCell.setCellStyle(currencyStyle);
                    Cell totalInfinePLFCell = totalRow.createCell(13 + 3*headerListCoupon.size());
                    totalInfinePLFCell.setCellFormula("SUM(" + getColumnReference(13 + 3*headerListCoupon.size()) + "11:" + getColumnReference(13 + 3*headerListCoupon.size()) + rowIndex + ")");
                    totalInfinePLFCell.setCellStyle(currencyStyle);
                    Cell totalInfineNetCell = totalRow.createCell(14 + 3*headerListCoupon.size());
                    totalInfineNetCell.setCellFormula("SUM(" + getColumnReference(14 + 3*headerListCoupon.size()) + "11:" + getColumnReference(14 + 3*headerListCoupon.size()) + rowIndex + ")");
                    totalInfineNetCell.setCellStyle(currencyStyle);
                }
            
            } // Fin du bloc else (si des investisseurs existent)

            // Ajuster la largeur des colonnes
            for (int i = 0; i < 16 + 3*headerListCoupon.size(); i++) {
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
        LocalDate couponDate = LocalDate.parse(CouponDate);
        LocalDate startDate = LocalDate.parse(obligation.getStartDate());
        long nombreDeMois = ChronoUnit.MONTHS.between(startDate, couponDate);
        for (int i = 1; i < nombreDeMois / 12; i++) {
            montantInvesti = (long) (montantInvesti * (1 + obligation.getRate()[0] / 100.0));
        }
        return montantInvesti;
    }

    // Méthode pour convertir un index de colonne en référence de colonne Excel (A, B, ..., Z, AA, AB, etc.)
    private String getColumnReference(int columnIndex) {
        StringBuilder sb = new StringBuilder();
        while (columnIndex >= 0) {
            sb.insert(0, (char)('A' + columnIndex % 26));
            columnIndex = columnIndex / 26 - 1;
        }
        return sb.toString();
    }
}


