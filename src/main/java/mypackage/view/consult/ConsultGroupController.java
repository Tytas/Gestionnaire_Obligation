package mypackage.view.consult;

import mypackage.model.Group;
import mypackage.model.Applicant;
import mypackage.model.Obligation;
import mypackage.model.DataBaseInteractor.ApplicantInteractor;
import mypackage.model.DataBaseInteractor.ObligationInteractor;

import java.io.File;
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


public class ConsultGroupController {

    private boolean result = false;

    private Group group;
    @FXML
    private Button validerButton;
    @FXML
    private Button annulerButton;


    public ConsultGroupController() {
    }
    public void initialize() {
    }

    public void setGroup(Group group) {
        this.group = group;
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
        System.out.println("🔄 Début de création du fichier Excel pour le groupe: " + group.getName());
        
        Stage stage = new Stage();
        try {
            // Configuration du FileChooser pour Excel
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer la liste des obligations du groupe");
            fileChooser.setInitialFileName(group.getName() + "_" + LocalDate.now() + "_Group.xlsx");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers Excel", "*.xlsx"));

            File selectedFile = fileChooser.showSaveDialog(stage);

            if (selectedFile == null) {
                System.out.println("❌ Annulation de l'enregistrement.");
                return;
            }

            String filename = selectedFile.getAbsolutePath();
            
            // Récupérer tous les émetteurs du groupe et leurs obligations
            System.out.println("🔍 Recherche des émetteurs du groupe: " + group.getName());
            ArrayList<Integer> memberIds = group.getMembers();
            if (memberIds == null || memberIds.isEmpty()) {
                System.err.println("⚠️ Aucun émetteur trouvé dans le groupe : " + group.getName());
                return;
            }
            
            // Collecter toutes les obligations de tous les émetteurs du groupe
            ArrayList<ObligationData> obligationsList = new ArrayList<>();
            
            for (Integer memberId : memberIds) {
                Applicant applicant = ApplicantInteractor.GetApplicant(memberId);
                if (applicant != null) {
                    System.out.println("📋 Traitement de l'émetteur: " + applicant.getName());
                    
                    // Récupérer toutes les obligations de cet émetteur
                    ArrayList<Integer> obligationIds = ObligationInteractor.GetAllObligationsId();
                    for (Integer obligationId : obligationIds) {
                        Obligation obligation = ObligationInteractor.GetObligation(obligationId);
                        if (obligation != null && obligation.getApplicantId() == memberId) {
                            // Créer un objet contenant toutes les infos nécessaires
                            ObligationData data = new ObligationData();
                            data.emetteurName = applicant.getName();
                            data.obligationName = obligation.getName();
                            data.capital = obligation.getCapital();
                            data.taux = obligation.getRate()[1] + "% / " + obligation.getRate()[0] + "%"; // taux mensuel / taux in fine
                            
                            // Calculer la durée en mois pour l'affichage
                            LocalDate startDate = LocalDate.parse(obligation.getStartDate());
                            LocalDate endDate;
                            if(obligation.getProrogationActivated()) {
                                // Si la prorogation est activée, on utilise la date de fin de la prorogation
                                endDate = LocalDate.parse(obligation.getProrogation()[0]);
                            } else {
                                // Sinon, on utilise la date de fin normale
                                endDate = LocalDate.parse(obligation.getEndDate());
                            }
                            long durationInMonths = ChronoUnit.MONTHS.between(startDate, endDate);
                            data.duree = durationInMonths + " mois";
                            
                            data.dateDebut = obligation.getStartDate();
                            data.dateFin = obligation.getEndDate();

                            obligationsList.add(data);
                        }
                    }
                }
            }
            
            if (obligationsList.isEmpty()) {
                System.err.println("⚠️ Aucune obligation trouvée pour les émetteurs du groupe : " + group.getName());
                return;
            }

            // Créer le classeur Excel
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Obligations du Groupe");

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

            // Créer l'en-tête d'information du groupe
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("GROUPE");
            titleCell.setCellStyle(headerStyle);

            Cell titleCell2 = titleRow.createCell(1);
            titleCell2.setCellValue(group.getName());
            titleCell2.setCellStyle(headerStyle);

            // Ligne vide
            sheet.createRow(1);

            // Créer l'en-tête du tableau
            Row headerRow = sheet.createRow(2);

            Cell headerCell1 = headerRow.createCell(0);
            headerCell1.setCellValue("Nom de l'Émetteur");
            headerCell1.setCellStyle(headerStyle);

            Cell headerCell2 = headerRow.createCell(1);
            headerCell2.setCellValue("Nom de l'Obligation");
            headerCell2.setCellStyle(headerStyle);

            Cell headerCell3 = headerRow.createCell(2);
            headerCell3.setCellValue("Capital");
            headerCell3.setCellStyle(headerStyle);

            Cell headerCell4 = headerRow.createCell(3);
            headerCell4.setCellValue("Taux (Mensuel / In Fine)");
            headerCell4.setCellStyle(headerStyle);

            Cell headerCell5 = headerRow.createCell(4);
            headerCell5.setCellValue("Durée");
            headerCell5.setCellStyle(headerStyle);

            Cell headerCell6 = headerRow.createCell(5);
            headerCell6.setCellValue("Date de Début");
            headerCell6.setCellStyle(headerStyle);

            Cell headerCell7 = headerRow.createCell(6);
            headerCell7.setCellValue("Date de Fin");
            headerCell7.setCellStyle(headerStyle);

            // Remplir les données des obligations
            System.out.println("📊 Nombre d'obligations trouvées: " + obligationsList.size());
            
            int rowIndex = 3;
            for (ObligationData data : obligationsList) {
                Row dataRow = sheet.createRow(rowIndex++);
                
                Cell cell1 = dataRow.createCell(0);
                cell1.setCellValue(data.emetteurName);
                
                Cell cell2 = dataRow.createCell(1);
                cell2.setCellValue(data.obligationName);
                
                Cell cell3 = dataRow.createCell(2);
                cell3.setCellValue(data.capital);
                cell3.setCellStyle(currencyStyle);
                
                Cell cell4 = dataRow.createCell(3);
                cell4.setCellValue(data.taux);
                
                Cell cell5 = dataRow.createCell(4);
                cell5.setCellValue(data.duree);
                
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
    
    // Classe interne pour organiser les données des obligations
    private static class ObligationData {
        String emetteurName;
        String obligationName;
        long capital;
        String taux;
        String duree;
        String dateDebut;
        String dateFin;
    }
}


