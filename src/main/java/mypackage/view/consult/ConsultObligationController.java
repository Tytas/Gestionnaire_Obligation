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
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
            int familyId = InvestorInteractor.GetInvestor(investorId).getFamilyId();
            if(familyId != 0 && !familyIdList.contains(familyId)){
                familyIdList.add(familyId);
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
        System.out.println("Début de création du fichier Excel pour l'obligation :");
        System.out.println("   - Date: " + obligation.getStartDate());
        System.out.println("   - Montant: " + obligation.getValeurNominale());
        System.out.println("   - Obligation: " + obligation.getName());

        Stage stage = new Stage();
        try {
            // Configuration du FileChooser pour Excel
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer la liste des souscripteurs");
            fileChooser.setInitialFileName( obligation.getName() + "_" + obligation.getStartDate() + "_consultation.xlsx");
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
            obligationDateCell2.setCellValue(obligation.getEndDate());
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
            // Calculer la durée en mois pour l'affichage
            LocalDate startDate = LocalDate.parse(obligation.getStartDate());
            LocalDate endDate = LocalDate.parse(obligation.getEndDate());
            long durationInMonths = ChronoUnit.MONTHS.between(startDate, endDate);
            obligationDurationCell2.setCellValue(durationInMonths + " mois");
            obligationDurationCell2.setCellStyle(headerStyle);

            if (obligation.getProrogation()[0] != "") {
                Cell obligationProrogationDateCell = obligationDateRow.createCell(2);
                obligationProrogationDateCell.setCellValue("PROROGATION : " + obligation.getProrogation()[0]);
                obligationProrogationDateCell.setCellStyle(headerStyle);

                Cell obligationProrogationRateCell = obligationRateRow.createCell(2);
                obligationProrogationRateCell.setCellValue("Taux Prorogation : " + obligation.getProrogation()[1] + "%" + " + " + obligation.getProrogation()[2] + "% INFINE");
                obligationProrogationRateCell.setCellStyle(headerStyle);
            
                Cell obligationProrogationDurationCell = obligationDurationRow.createCell(2);
                // Calculer la durée en mois entre la fin normale et la fin de prorogation pour l'affichage
                LocalDate endDateNormale = LocalDate.parse(obligation.getEndDate());
                LocalDate endDateProrogation = LocalDate.parse(obligation.getProrogation()[0]);
                long durationProrogationMonths = ChronoUnit.MONTHS.between(endDateNormale, endDateProrogation);
                obligationProrogationDurationCell.setCellValue("Durée Prorogation : " + durationProrogationMonths + " mois (jusqu'au " + obligation.getProrogation()[0] + ")");
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
            headerCell3.setCellValue("Date de Souscription");
            headerCell3.setCellStyle(headerStyle);

            Cell headerCell4 = headerRow.createCell(3);
            headerCell4.setCellValue("PP / PM");
            headerCell4.setCellStyle(headerStyle);

            Cell headerCell5 = headerRow.createCell(4);
            headerCell5.setCellValue("R / NR");
            headerCell5.setCellStyle(headerStyle);

            Cell headerCell6 = headerRow.createCell(5);
            headerCell6.setCellValue("PLF");
            headerCell6.setCellStyle(headerStyle);

            Cell headerCell7 = headerRow.createCell(6);
            headerCell7.setCellValue("Adresse");
            headerCell7.setCellStyle(headerStyle);

            Cell headerCell8 = headerRow.createCell(7);
            headerCell8.setCellValue("Ville");
            headerCell8.setCellStyle(headerStyle);

            Cell headerCell9 = headerRow.createCell(8);
            headerCell9.setCellValue("Pays");
            headerCell9.setCellStyle(headerStyle);

            Cell headerCell10 = headerRow.createCell(9);
            headerCell10.setCellValue("Nombre D'Obligations");
            headerCell10.setCellStyle(headerStyle);

            Cell headerCell11 = headerRow.createCell(10);
            headerCell11.setCellValue("Montant de Souscritption");
            headerCell11.setCellStyle(headerStyle);

            Cell headerCell12 = headerRow.createCell(11);
            headerCell12.setCellValue("IBAN");
            headerCell12.setCellStyle(headerStyle);

            Cell headerCell13 = headerRow.createCell(12);
            headerCell13.setCellValue("BIC");
            headerCell13.setCellStyle(headerStyle);

            ArrayList<Cell> headerListCoupon = new ArrayList<>();

            ArrayList<String[]> listCoupon = obligation.listCouponGetter();

            Row headerCouponRow = sheet.createRow(8);
            for (int i = 0; i < 3*listCoupon.size(); i+=3) {
                Cell headerCouponCell = headerCouponRow.createCell(13 + i);
                sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(8, 8, 13 + i, 15 + i));
                headerCouponCell.setCellValue("COUPON " + listCoupon.get(i/3)[0]);
                headerCouponCell.setCellStyle(headerStyle);

                headerListCoupon.add(headerCouponCell);

                Cell titleCouponBrutCell = headerRow.createCell(13 + i);
                titleCouponBrutCell.setCellValue("BRUT");
                Cell titleCouponPLFCell = headerRow.createCell(14 + i);
                titleCouponPLFCell.setCellValue("PLF");
                Cell titleCouponNetCell = headerRow.createCell(15 + i);
                titleCouponNetCell.setCellValue("NET");
            }

            if(LocalDate.parse(obligation.getEndDate()).isEqual(LocalDate.parse(listCoupon.get(listCoupon.size() - 1)[0])) ||
               (obligation.getProrogationActivated() && LocalDate.parse(obligation.getProrogation()[0]).isEqual(LocalDate.parse(listCoupon.get(listCoupon.size() - 1)[0])))) {
                Cell headerProrogationCell = headerCouponRow.createCell(13 + 3*listCoupon.size());
                sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(8, 8, 13 + 3*listCoupon.size(), 15 + 3*listCoupon.size()));
                headerProrogationCell.setCellValue("COUPON IN FINE");
                headerProrogationCell.setCellStyle(headerStyle);

                Cell titleCouponInFineBRUTCell = headerRow.createCell(13 + 3*listCoupon.size());
                titleCouponInFineBRUTCell.setCellValue("BRUT");
                Cell titleCouponInFinePLFCell = headerRow.createCell(14 + 3*listCoupon.size());
                titleCouponInFinePLFCell.setCellValue("PLF");
                Cell titleCouponInFineNETCell = headerRow.createCell(15 + 3*listCoupon.size());
                titleCouponInFineNETCell.setCellValue("NET");
            }

            // Remplir les données des souscripteurs
            ArrayList<InvestorInfo> investors = obligation.getInvestors();
            System.out.println("👥 Nombre de souscripteurs trouvés: " + (investors != null ? investors.size() : 0));
            if(!selectedFamilyName.equals("Toutes")) {
                investors.removeIf(investor -> {
                    int investorFamilyId = InvestorInteractor.GetInvestor(investor.getInvestorId()).getFamilyId();
                    if (investorFamilyId == 0) {
                        return true; // Exclure les investisseurs sans famille
                    }
                    return !FamilyInteractor.GetFamily(investorFamilyId).getName().equals(selectedFamilyName);
                });
            }
            
            if (investors == null || investors.isEmpty()) {
                System.err.println("⚠️ Aucun souscripteur trouvé pour l'obligation : " + obligation.getName());
                
                // Créer une ligne indiquant qu'il n'y a pas de souscripteurs
                Row noDataRow = sheet.createRow(9);
                Cell noDataCell = noDataRow.createCell(0);
                noDataCell.setCellValue("Aucun souscripteur trouvé pour cette obligation");
                sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(9, 9, 0, 4 + headerListCoupon.size()));
            } else {
                Map<String, Integer> AmortissementsMap = new HashMap<>(obligation.getDepreciations());
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
                    LocalDate obligationEndDate = LocalDate.parse(obligation.getEndDate());
                    LocalDate lastCouponDate = LocalDate.parse(listCoupon.get(listCoupon.size() - 1)[0]);
                    if(obligationEndDate.isEqual(lastCouponDate) && !obligation.getProrogationActivated()) {
                        if(obligation.getRate()[0] != 0.0) {
                            montantInvestiInFine = MontantInvestiCapitalise(montantInvesti, obligation, listCoupon.get(listCoupon.size() - 1)[0]);
                            partBrutInFine = montantInvestiInFine;
                            partNetInFine = partBrutInFine;
                        }
                    }
                    if(obligation.getProrogationActivated()) {
                        obligationEndDate = LocalDate.parse(obligation.getProrogation()[0]); // date de fin de prorogation
                        if(obligationEndDate.isEqual(lastCouponDate)) {
                            if(Double.parseDouble(obligation.getProrogation()[2]) != 0.0) {
                                montantInvestiInFine = MontantInvestiCapitalise(montantInvesti, obligation, listCoupon.get(listCoupon.size() - 1)[0]);
                                // Calculer la durée en années entre la fin normale et la fin de prorogation
                                LocalDate endDateNormale = LocalDate.parse(obligation.getEndDate());
                                LocalDate endDateProrogation = LocalDate.parse(obligation.getProrogation()[0]);
                                long durationProrogationYears = ChronoUnit.YEARS.between(endDateNormale, endDateProrogation);
                                for(int i = 0; i <= durationProrogationYears; i++) {
                                    montantInvestiInFine = (long) (montantInvestiInFine * (1 + Double.parseDouble(obligation.getProrogation()[2]) / 100.0) + (montantInvesti * Double.parseDouble(obligation.getProrogation()[2]) / 100.0));
                                }
                                partBrutInFine = montantInvestiInFine;
                                System.out.println("Date de coupon égale à la date de fin de l'obligation, ajout du taux In Fine.");
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
                    String investorDate = obligation.getStartDate();
                    if(!obligation.getInvestorDate(investorId).equals("")) {
                        investorDate = obligation.getInvestorDate(investorId);
                    }
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
                        if (investorNP.getAddress()[4].toUpperCase(Locale.FRANCE).equals("FRANCE") ||
                            investorNP.getAddress()[4].toUpperCase(Locale.FRANCE).equals("FR")) {
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
                    if (investor.getFamilyId() != 0) {
                        cellFamily.setCellValue(FamilyInteractor.GetFamily(investor.getFamilyId()).getName());
                    } else {
                        cellFamily.setCellValue("Sans famille");
                    }

                    // Nom du Souscripteur
                    Cell cellName = dataRow.createCell(1);
                    cellName.setCellValue(investorName);

                    Cell cellDate = dataRow.createCell(2);
                    cellDate.setCellValue(investorDate);

                    Cell cellPPorPM = dataRow.createCell(3);
                    cellPPorPM.setCellValue(investorPMorPP);

                    Cell cellResident = dataRow.createCell(4);
                    cellResident.setCellValue(investorResidence);

                    Cell cellPLF = dataRow.createCell(5);
                    cellPLF.setCellValue(PLF);

                    Cell cellAddress = dataRow.createCell(6);
                    cellAddress.setCellValue(address);

                    Cell cellVille = dataRow.createCell(7);
                    cellVille.setCellValue(ville);

                    Cell cellPays = dataRow.createCell(8);
                    cellPays.setCellValue(pays);

                    Cell cellNombreParts = dataRow.createCell(9);
                    cellNombreParts.setCellValue(nombreParts);

                    Cell cellMontantInvesti = dataRow.createCell(10);
                    cellMontantInvesti.setCellValue(montantInvesti);
                    cellMontantInvesti.setCellStyle(currencyStyle);

                    Cell cellIBAN = dataRow.createCell(11);
                    cellIBAN.setCellValue(investorIBAN);

                    Cell cellBIC = dataRow.createCell(12);
                    cellBIC.setCellValue(investorBIC);

                    // Calculer la part du coupon
                    int index = 0;
                    boolean[] amortissementApplique = new boolean[AmortissementsMap.size()];
                    for (int j = 0; j < amortissementApplique.length; j++) {
                        amortissementApplique[j] = false;
                    }
                    for (int i = 0; i < 3*listCoupon.size(); i+=3) {
                        if(AmortissementsMap.isEmpty() || i == 0) {
                            System.out.println("Aucun amortissement trouvé pour l'obligation : " + obligation.getName());
                        } else {
                            System.out.println("Amortissements trouvés : " + AmortissementsMap.size());
                            index = 0;
                            for (Map.Entry<String, Integer> entry : AmortissementsMap.entrySet()) {
                                DateTimeFormatter frenchFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                                if(LocalDate.parse(entry.getKey(), frenchFormatter).isBefore(LocalDate.parse(listCoupon.get(i/3)[0])) && !amortissementApplique[index]) {
                                    amortissementApplique[index] = true;
                                    partBrut *= (1 - (entry.getValue() / 100.0));
                                    partPLF *= (1 - (entry.getValue() / 100.0));
                                    partNet *= (1 - (entry.getValue() / 100.0));
                                    partBrutInFine *= (1 - (entry.getValue() / 100.0));
                                    partPLFInFine *= (1 - (entry.getValue() / 100.0));
                                    partNetInFine *= (1 - (entry.getValue() / 100.0));
                                    System.out.println("   - Date: " + entry.getKey() + ", Montant: " + entry.getValue());
                                }
                                index++;
                            }
                        }
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
                        if(LocalDate.parse(listCoupon.get(i/3)[0]).isAfter(LocalDate.parse(obligation.getEndDate()))) {
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
                                Cell cellCouponBrut = dataRow.createCell(13 + i);
                                cellCouponBrut.setCellValue((partBrut / 365) * daysBetween);
                                cellCouponBrut.setCellStyle(currencyStyle);

                                Cell cellCouponPLF = dataRow.createCell(14 + i);
                                cellCouponPLF.setCellValue((partPLF / 365) * daysBetween);
                                cellCouponPLF.setCellStyle(currencyStyle);

                                Cell cellCouponNet = dataRow.createCell(15 + i);
                                cellCouponNet.setCellValue((partNet / 365) * daysBetween);
                                cellCouponNet.setCellStyle(currencyStyle);
                            }else {
                                Cell cellCouponBrut = dataRow.createCell(13 + i);
                                cellCouponBrut.setCellValue(partBrut);
                                cellCouponBrut.setCellStyle(currencyStyle);

                                Cell cellCouponPLF = dataRow.createCell(14 + i);
                                cellCouponPLF.setCellValue(partPLF);
                                cellCouponPLF.setCellStyle(currencyStyle);

                                Cell cellCouponNet = dataRow.createCell(15 + i);
                                cellCouponNet.setCellValue(partNet);
                                cellCouponNet.setCellStyle(currencyStyle);
                            }
                        } else {
                            Cell cellCouponBrut = dataRow.createCell(13 + i);
                            cellCouponBrut.setCellValue(partBrut);
                            cellCouponBrut.setCellStyle(currencyStyle);

                            Cell cellCouponPLF = dataRow.createCell(14 + i);
                            cellCouponPLF.setCellValue(partPLF);
                            cellCouponPLF.setCellStyle(currencyStyle);

                            Cell cellCouponNet = dataRow.createCell(15 + i);
                            cellCouponNet.setCellValue(partNet);
                            cellCouponNet.setCellStyle(currencyStyle);
                        }
                    }

                    if(montantInvestiInFine != 0) {
                        if(!info.getDate().isEmpty()) {
                            // Recalculer partBrutInFine pour une arrivée tardive
                            LocalDate investorLateDate = LocalDate.parse(info.getDate());
                            LocalDate lateStartDate = LocalDate.parse(obligation.getStartDate());
                            while( investorLateDate.isAfter(lateStartDate)) {
                                lateStartDate = lateStartDate.plusMonths(period);
                            }
                            double daysBetween = ChronoUnit.DAYS.between(investorLateDate, lateStartDate);
                            double montantInvestiCapitalise = obligation.getValeurNominale() * info.getCapital()
                                                            * (daysBetween / 365) * (obligation.getRate()[0] / 100.0);
                            LocalDate couponDate = LocalDate.parse(listCoupon.get(listCoupon.size() - 1)[0]);
                            long nombreDeMois = ChronoUnit.MONTHS.between(lateStartDate, couponDate);
                            for (int i = 1; i <= nombreDeMois / 12; i++) {
                                montantInvestiCapitalise = (long) (montantInvestiCapitalise * (1 + obligation.getRate()[0] / 100.0) + 
                                                                   obligation.getValeurNominale() * info.getCapital() * obligation.getRate()[0] / 100.0);
                            }
                            double partBrutInFineArriveeTardive = montantInvestiCapitalise;
                            partBrutInFine = partBrutInFineArriveeTardive;
                            if(partPLFInFine != 0.0) {
                                partPLFInFine = partBrutInFine * 0.3;
                                partNetInFine = partBrutInFine - partPLFInFine;
                            } else {
                                partPLFInFine = 0.0;
                                partNetInFine = partBrutInFine;
                            }
                        }
                        Cell cellCouponInfineBrut = dataRow.createCell(13 + 3*listCoupon.size());
                        cellCouponInfineBrut.setCellValue(partBrutInFine);
                        cellCouponInfineBrut.setCellStyle(currencyStyle);
                        Cell cellCouponInfinePLF = dataRow.createCell(14 + 3*listCoupon.size());
                        cellCouponInfinePLF.setCellValue(partPLFInFine);
                        cellCouponInfinePLF.setCellStyle(currencyStyle);
                        Cell cellCouponInfineNet = dataRow.createCell(15 + 3*listCoupon.size());
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

                Cell totalNombrePartsCell = totalRow.createCell(9);
                totalNombrePartsCell.setCellFormula("SUM(J11:J" + (rowIndex) + ")");

                Cell totalMontantInvestiCell = totalRow.createCell(10);
                totalMontantInvestiCell.setCellFormula("SUM(K11:K" + (rowIndex) + ")");
                totalMontantInvestiCell.setCellStyle(currencyStyle);

                for (int i = 0; i < 3*headerListCoupon.size(); i+=3) {

                    // Calculez les colonnes de base pour chaque itération
                    String brutCol = getColumnReference(13 + i);
                    String plfCol = getColumnReference(14 + i);
                    String netCol = getColumnReference(15 + i);

                    Cell totalBrutCell = totalRow.createCell(13 + i);
                    totalBrutCell.setCellFormula("SUM(" + brutCol + "11:" + brutCol + (rowIndex) + ")");
                    totalBrutCell.setCellStyle(currencyStyle);

                    Cell totalPLFCell = totalRow.createCell(14 + i);
                    totalPLFCell.setCellFormula("SUM(" + plfCol + "11:" + plfCol + (rowIndex) + ")");
                    totalPLFCell.setCellStyle(currencyStyle);

                    Cell totalNetCell = totalRow.createCell(15 + i);
                    totalNetCell.setCellFormula("SUM(" + netCol + "11:" + netCol + (rowIndex) + ")");
                    totalNetCell.setCellStyle(currencyStyle);
                }
                if(obligation.getRate()[0]!=0) {
                    Cell totalInfineBrutCell = totalRow.createCell(13 + 3*headerListCoupon.size());
                    totalInfineBrutCell.setCellFormula("SUM(" + getColumnReference(13 + 3*headerListCoupon.size()) + "11:" + getColumnReference(13 + 3*headerListCoupon.size()) + rowIndex + ")");
                    totalInfineBrutCell.setCellStyle(currencyStyle);
                    Cell totalInfinePLFCell = totalRow.createCell(14 + 3*headerListCoupon.size());
                    totalInfinePLFCell.setCellFormula("SUM(" + getColumnReference(14 + 3*headerListCoupon.size()) + "11:" + getColumnReference(14 + 3*headerListCoupon.size()) + rowIndex + ")");
                    totalInfinePLFCell.setCellStyle(currencyStyle);
                    Cell totalInfineNetCell = totalRow.createCell(15 + 3*headerListCoupon.size());
                    totalInfineNetCell.setCellFormula("SUM(" + getColumnReference(15 + 3*headerListCoupon.size()) + "11:" + getColumnReference(15 + 3*headerListCoupon.size()) + rowIndex + ")");
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
        long res = (long) (montantInvesti * (obligation.getRate()[0] / 100.0));
        for (int i = 1; i <= nombreDeMois / 12; i++) {
            res = (long) (res * ((100 + obligation.getRate()[0]) / 100.0) + (montantInvesti * (obligation.getRate()[0] / 100.0)));
        }
        return res;
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


