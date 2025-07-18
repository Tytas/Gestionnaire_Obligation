package mypackage.view.consult;

import mypackage.model.Family;
import mypackage.model.Obligation;
import mypackage.model.InvestorLP;
import mypackage.model.InvestorNP;
import mypackage.model.util.InvestorInfo;
import mypackage.model.DataBaseInteractor.ObligationInteractor;
import mypackage.model.DataBaseInteractor.InvestorInteractor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.awt.Desktop;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ConsultFamilyController {
    private boolean result = false;

    private Family family;
    @FXML
    private Button validerButton;
    @FXML
    private Button annulerButton;


    public ConsultFamilyController() {
    }
    public void initialize() {
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public void init() {
        
        validerButton.setOnAction(event -> {
            boolean hasError = false;
            ShowExcel();
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

    private void ShowExcel() {
        System.out.println("🔄 Début de création du fichier Excel pour le family : " + family.getName());

        Stage stage = new Stage();
        try {
            // Configuration du FileChooser pour Excel
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer la liste des obligations du family");
            fileChooser.setInitialFileName(family.getName() + "_Obligations.xlsx");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers Excel", "*.xlsx"));

            File selectedFile = fileChooser.showSaveDialog(stage);

            if (selectedFile == null) {
                System.out.println("❌ Annulation de l'enregistrement.");
                return;
            }

            String filename = selectedFile.getAbsolutePath();

            // Récupérer tous les souscripteurs du family et leurs obligations
            System.out.println("🔍 Recherche des souscripteurs du family : " + family.getName());
            ArrayList<Integer> memberIds = family.getInvestors();
            if (memberIds == null || memberIds.isEmpty()) {
                System.err.println("⚠️ Aucun souscripteur trouvé dans le family : " + family.getName());
                return;
            }

            // Collecter toutes les obligations auxquelles les souscripteurs du family ont souscrit
            ArrayList<ObligationSubscriptionData> subscriptionsList = new ArrayList<>();
            
            // Récupérer toutes les obligations existantes
            ArrayList<Integer> obligationIds = ObligationInteractor.GetAllObligationsId();
            
            for (Integer obligationId : obligationIds) {
                Obligation obligation = ObligationInteractor.GetObligation(obligationId);
                if (obligation != null) {
                    // Vérifier si des membres du family ont souscrit à cette obligation
                    ArrayList<InvestorInfo> investors = obligation.getInvestors();
                    if (investors != null) {
                        for (InvestorInfo investorInfo : investors) {
                            if (memberIds.contains(investorInfo.getInvestorId())) {
                                // Un membre du family a souscrit à cette obligation
                                ObligationSubscriptionData data = new ObligationSubscriptionData();
                                data.obligationName = obligation.getName();
                                data.montantParticipation = investorInfo.getCapital() * obligation.getValeurNominale();
                                data.taux = obligation.getRate()[1] + "% / " + obligation.getRate()[0] + "%"; // taux mensuel / taux in fine
                                data.dateDebut = obligation.getStartDate();
                                data.dateFin = LocalDate.parse(obligation.getStartDate()).plusMonths(obligation.getDurationMonths()).toString();
                                
                                // Récupérer le nom et type du souscripteur
                                Integer investorId = investorInfo.getInvestorId();
                                
                                // Vérifier s'il s'agit d'un investisseur LP ou NP
                                InvestorLP investorLP = InvestorInteractor.GetInvestorLP(investorId);
                                if (investorLP != null) {
                                    data.nomSouscripteur = investorLP.getName();
                                    data.typeSouscripteur = "PM";
                                } else {
                                    InvestorNP investorNP = InvestorInteractor.GetInvestorNP(investorId);
                                    if (investorNP != null) {
                                        data.nomSouscripteur = investorNP.getName() + " " + investorNP.getFirstName();
                                        data.typeSouscripteur = "PP";
                                    } else {
                                        data.nomSouscripteur = "Souscripteur inconnu (ID: " + investorId + ")";
                                        data.typeSouscripteur = "Inconnu";
                                    }
                                }
                                
                                subscriptionsList.add(data);
                            }
                        }
                    }
                }
            }
            
            if (subscriptionsList.isEmpty()) {
                System.err.println("⚠️ Aucune souscription trouvée pour les membres du family : " + family.getName());
                return;
            }

            // Créer le classeur Excel
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Souscriptions du Family");

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

            // Créer l'en-tête d'information du family
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("FAMILY");
            titleCell.setCellStyle(headerStyle);

            Cell titleCell2 = titleRow.createCell(1);
            titleCell2.setCellValue(family.getName());
            titleCell2.setCellStyle(headerStyle);

            // Ligne vide
            sheet.createRow(1);

            // Créer l'en-tête du tableau
            Row headerRow = sheet.createRow(2);

            Cell headerCell1 = headerRow.createCell(0);
            headerCell1.setCellValue("Nom de l'Obligation");
            headerCell1.setCellStyle(headerStyle);

            Cell headerCell2 = headerRow.createCell(1);
            headerCell2.setCellValue("Nom du Souscripteur");
            headerCell2.setCellStyle(headerStyle);

            Cell headerCell3 = headerRow.createCell(2);
            headerCell3.setCellValue("Type de Souscripteur");
            headerCell3.setCellStyle(headerStyle);

            Cell headerCell4 = headerRow.createCell(3);
            headerCell4.setCellValue("Montant de Participation");
            headerCell4.setCellStyle(headerStyle);

            Cell headerCell5 = headerRow.createCell(4);
            headerCell5.setCellValue("Taux (Mensuel / In Fine)");
            headerCell5.setCellStyle(headerStyle);

            Cell headerCell6 = headerRow.createCell(5);
            headerCell6.setCellValue("Date de Début");
            headerCell6.setCellStyle(headerStyle);

            Cell headerCell7 = headerRow.createCell(6);
            headerCell7.setCellValue("Date de Fin");
            headerCell7.setCellStyle(headerStyle);

            // Remplir les données des souscriptions
            System.out.println("📊 Nombre de souscriptions trouvées: " + subscriptionsList.size());
            
            int rowIndex = 3;
            for (ObligationSubscriptionData data : subscriptionsList) {
                Row dataRow = sheet.createRow(rowIndex++);
                
                Cell cell1 = dataRow.createCell(0);
                cell1.setCellValue(data.obligationName);
                
                Cell cell2 = dataRow.createCell(1);
                cell2.setCellValue(data.nomSouscripteur);
                
                Cell cell3 = dataRow.createCell(2);
                cell3.setCellValue(data.typeSouscripteur);
                
                Cell cell4 = dataRow.createCell(3);
                cell4.setCellValue(data.montantParticipation);
                cell4.setCellStyle(currencyStyle);
                
                Cell cell5 = dataRow.createCell(4);
                cell5.setCellValue(data.taux);
                
                Cell cell6 = dataRow.createCell(5);
                cell6.setCellValue(data.dateDebut);
                
                Cell cell7 = dataRow.createCell(6);
                cell7.setCellValue(data.dateFin);
            }

            // Ajuster la largeur des colonnes
            for (int i = 0; i < 7; i++) {
                sheet.autoSizeColumn(i);
            }

            // Sauvegarder le fichier
            try (FileOutputStream fileOut = new FileOutputStream(filename)) {
                workbook.write(fileOut);
                System.out.println("✅ Fichier Excel créé avec succès : " + filename);
            }

            workbook.close();

            // Ouvrir le fichier automatiquement
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(selectedFile);
            }

        } catch (IOException e) {
            System.err.println("❌ Erreur lors de la création du fichier Excel : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Classe interne pour organiser les données des souscriptions
    private static class ObligationSubscriptionData {
        String obligationName;
        String nomSouscripteur;
        String typeSouscripteur;
        long montantParticipation;
        String taux;
        String dateDebut;
        String dateFin;
    }
}
