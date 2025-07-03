package mypackage.view.consult;

import mypackage.model.Obligation;
import mypackage.model.DataBaseInteractor.InvestorInteractor;
import mypackage.model.DataBaseInteractor.FamilyInteractor;
import mypackage.model.Family;
import mypackage.model.Investor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
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
        Integer[] investorIdList = obligation.getInvestors().keySet().toArray(new Integer[0]);
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
            if (selectedFamilyName == "Toutes") {
                ShowExcel(true);
            } else {
                ShowExcel(false);
            }

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

    private void ShowExcel(boolean isAllFamilies) {
        System.out.println("🔄 Début de création du fichier Excel pour le coupon:");
        System.out.println("   - Date: " + obligation.getStartDate());
        System.out.println("   - Montant: " + obligation.getValeurNominale());
        System.out.println("   - Obligation: " + obligation.getName());

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
            titleCell.setCellValue("Liste des Souscripteurs");
            titleCell.setCellStyle(headerStyle);

            Row obligationRow = sheet.createRow(1);
            Cell obligationCell = obligationRow.createCell(0);
            obligationCell.setCellValue("Obligation : " + obligation.getName());

            Row montantRow = sheet.createRow(2);
            Cell montantCell = montantRow.createCell(0);
            montantCell.setCellValue("Montant total de l'obligation : " + obligation.getCapital() + " €");

            // Ligne vide
            sheet.createRow(3);

            // Créer l'en-tête du tableau
            Row headerRow = sheet.createRow(25);

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

            ArrayList<Cell> headerListCoupon = new ArrayList<>();

            ArrayList<String[]> listCoupon = obligation.getListCoupon();

            for (int i = 0; i < listCoupon.size(); i++) {
                Cell headerCellCoupon = headerRow.createCell(4 + i);
                headerCellCoupon.setCellValue("Coupon du "+ listCoupon.get(i)[0]);
                headerCellCoupon.setCellStyle(headerStyle);
                headerListCoupon.add(headerCellCoupon);
            }

            // Remplir les données des souscripteurs
            Map<Integer, Long> investors = obligation.getInvestors();
            System.out.println("👥 Nombre de souscripteurs trouvés: " + (investors != null ? investors.size() : 0));
            
            if (investors == null || investors.isEmpty()) {
                System.err.println("⚠️ Aucun souscripteur trouvé pour l'obligation : " + obligation.getName());
                
                // Créer une ligne indiquant qu'il n'y a pas de souscripteurs
                Row noDataRow = sheet.createRow(5);
                Cell noDataCell = noDataRow.createCell(0);
                noDataCell.setCellValue("Aucun souscripteur trouvé pour cette obligation");
                sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(5, 5, 0, 4 + headerListCoupon.size()));
            } else {
                System.out.println("💰 Valeur nominale: " + obligation.getValeurNominale());
                System.out.println("📊 Taux [In Fine, Mensuel]: [" + obligation.getRate()[0] + "%, " + obligation.getRate()[1] + "%]");
                
                int rowIndex = 4; // Commencer après l'en-tête
                double[] totalPartsCoupon = new double[headerListCoupon.size()];
                long totalPartsCount = 0;
                long totalCapitalInvested = 0;

                for (Map.Entry<Integer, Long> entry : investors.entrySet()) {
                    int investorId = entry.getKey();
                    long nombreParts = entry.getValue();
                    long montantInvesti = nombreParts * obligation.getValeurNominale();
                    
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

                    // Calculer la part du coupon
                    for (int i = 0; i < listCoupon.size(); i++) {
                        double partCoupon = montantInvesti * obligation.getRate()[1] / 100.0;
                        // Ajouter aux totaux
                        totalPartsCoupon[i] += partCoupon;
                        try {
                            LocalDate obligationEndDate = LocalDate.parse(obligation.getStartDate()).plusMonths(obligation.getDurationMonths());
                            LocalDate couponDate = LocalDate.parse(listCoupon.get(i)[0]);
                            
                            if(obligationEndDate.isEqual(couponDate)) {
                                if(obligation.getRate()[0] != 0) {
                                    partCoupon += montantInvesti * obligation.getRate()[0] / 100.0;
                                    headerListCoupon.get(i).setCellValue("Coupon du :"+ listCoupon.get(i)[0] +  " ATTENTION IN FINE");
                                    System.out.println("📅 Date de coupon égale à la date de fin de l'obligation, ajout du taux In Fine.");
                                }
                            }
                        } catch (Exception dateEx) {
                            System.err.println("⚠️ Erreur de parsing des dates pour l'obligation " + obligation.getName() + ": " + dateEx.getMessage());
                        }
                        // Part du Coupon
                        Cell cellCoupon = dataRow.createCell(4 + i);
                        cellCoupon.setCellValue(partCoupon);
                        cellCoupon.setCellStyle(currencyStyle);
                    }
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

                for (int i = 0; i < headerListCoupon.size(); i++) {
                    Cell totalCouponCell = totalRow.createCell(4 + i);
                    totalCouponCell.setCellValue(totalPartsCoupon[i]);
                    totalCouponCell.setCellStyle(currencyStyle);
                }
            
            } // Fin du bloc else (si des investisseurs existent)

            // Ajuster la largeur des colonnes
            for (int i = 0; i < 5 + headerListCoupon.size(); i++) {
                sheet.autoSizeColumn(i);
            }

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
}
