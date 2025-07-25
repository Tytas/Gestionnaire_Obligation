package mypackage.model.DataBaseInteractor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.beans.property.SimpleStringProperty;
import mypackage.model.Applicant;

public class ApplicantInteractor {


    public static Applicant GetApplicant(int id){
        if (id <= 0) {
            System.err.println("ID de candidat invalide : " + id);
            return null;
        }
        
        ObjectMapper objectMapper = new ObjectMapper();
        String baseDir = "data/applicants/";
        SimpleStringProperty name = new SimpleStringProperty(Integer.toString(id));
        try {
            // Vérifier si le dossier existe
            Path applicantPath = Path.of(baseDir, name.get());
            Path dataPath = applicantPath.resolve("data.json");
            
            if (Files.exists(applicantPath) && Files.exists(dataPath)) {
                Applicant applicant = objectMapper.readValue(dataPath.toFile(), Applicant.class);
                if (applicant != null) {
                    return applicant;
                } else {
                    System.err.println("Erreur : candidat null après désérialisation pour ID " + id);
                }
            } else {
                System.err.println("Fichier data.json non trouvé pour le candidat ID " + id);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du candidat ID " + id + " : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static Boolean SaveApplicant(Applicant applicant){
        String baseDir = "data/applicants/";
        String applicantName = Integer.toString(applicant.getId());
        Path applicantFolder = Path.of(baseDir, applicantName);

        try {
            // Créer le dossier s'il n'existe pas déjà
            if (!Files.exists(applicantFolder)) {
                Files.createDirectories(applicantFolder);
            }

            // Créer le fichier JSON
            File jsonFile = applicantFolder.resolve("data.json").toFile();

            // Sérialiser avec Jackson
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, applicant);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<Integer> GetAllApplicantsId(){
        File applicantsDir = new File("data/applicants/");
        if (!applicantsDir.exists() || !applicantsDir.isDirectory()) {
            System.err.println("Répertoire des candidats non trouvé : data/applicants/");
            return new ArrayList<>();
        }
        
        File[] ListApplicantFiles = applicantsDir.listFiles(File::isDirectory);
        ArrayList<Integer> ListApplicantId = new ArrayList<>();
        
        if (ListApplicantFiles == null) {
            System.err.println("Erreur lors de la lecture du répertoire des candidats");
            return ListApplicantId;
        }
        
        for (File file : ListApplicantFiles) {
            try {
                String fileName = file.getName().trim();
                if (!fileName.isEmpty()) {
                    ListApplicantId.add(Integer.parseInt(fileName));
                }
            } catch (NumberFormatException e) {
                // ignorer les dossiers qui ne sont pas des nombres
                System.err.println("Nom de dossier invalide pour candidat : " + file.getName());
            }
        }
        return ListApplicantId;
    }

    public static Boolean DeleteApplicant(int id){
        String baseDir = "data/applicants/";
        String ApplicantName = Integer.toString(id);
        Path ApplicantFolder = Path.of(baseDir, ApplicantName);

        try {
            // Supprimer le dossier et son contenu
            if (Files.exists(ApplicantFolder)) {
                Files.walk(ApplicantFolder)
                     .sorted((a, b) -> b.compareTo(a)) // Pour supprimer les fichiers avant les dossiers
                     .forEach(path -> {
                         try {
                             Files.delete(path);
                         } catch (IOException e) {
                             e.printStackTrace();
                         }
                     });
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    public static int GetApplicantByName(String name) {
        ArrayList<Integer> ids = GetAllApplicantsId();
        for (int id : ids) {
            Applicant applicant = GetApplicant(id);
            if (applicant != null && applicant.getName().equals(name)) {
                return id;
            }
        }
        return -1; // Retourne -1 si aucun applicant trouvé avec ce nom
    }

    public static int generateNewId() {
        ArrayList<Integer> ids = GetAllApplicantsId();
        if (ids.isEmpty()) {
            return 1; // Si aucun applicant n'existe, commencer à 1
        } else {
            return ids.stream().max(Integer::compareTo).orElse(0) + 1; // Retourner le plus grand ID + 1
        }
    }
}
