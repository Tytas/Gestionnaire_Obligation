package mypackage.model.DataBaseInteractor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.beans.property.SimpleStringProperty;
import mypackage.model.Family;

public class FamilyInteractor {

    public static Family GetFamily(int id){
        ObjectMapper objectMapper = new ObjectMapper();
        String baseDir = "data/families/";
        SimpleStringProperty name = new SimpleStringProperty(Integer.toString(id));
        System.out.println("Dossier trouvé : " + name.get());
        try {
            // Vérifier si le dossier existe
            if (Files.exists(Path.of(baseDir, name.get()))) {
                Family family = objectMapper.readValue(Path.of(baseDir, name.get()).resolve("data.json").toFile(),
                                                        Family.class);
                System.out.println("Données de la famille chargées : " + family);
                return family;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static Boolean SaveFamily(Family family){
        String baseDir = "data/families/";
        String familyName = Integer.toString(family.getId());
        Path familyFolder = Path.of(baseDir, familyName);

        try {
            // Créer le dossier s'il n'existe pas déjà
            if (!Files.exists(familyFolder)) {
                Files.createDirectories(familyFolder);
            }

            // Créer le fichier JSON
            File jsonFile = familyFolder.resolve("data.json").toFile();

            // Sérialiser avec Jackson
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, family);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<Integer> GetAllFamiliesId(){
        File[] ListFamFiles = new File("data/families/").listFiles(File::isDirectory);
        ArrayList<Integer> ListFamId = new ArrayList<>();
        for (File file : ListFamFiles) {
            try {
                ListFamId.add(Integer.parseInt(file.getName()));
            } catch (NumberFormatException e) {
                // ignorer les dossiers qui ne sont pas des nombres
            }
        }
        return ListFamId;
    }

    public static Boolean DeleteFamily(int id){
        String baseDir = "data/families/";
        String familyName = Integer.toString(id);
        Path familyFolder = Path.of(baseDir, familyName);

        try {
            // Supprimer le dossier et son contenu
            if (Files.exists(familyFolder)) {
                Files.walk(familyFolder)
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

    public static int GetFamilyByName(String name) {
        ArrayList<Integer> ListFamId = GetAllFamiliesId();
        for (int id : ListFamId) {
            Family family = GetFamily(id);
            if (family != null && family.getName().trim().equals(name.trim())) {
                return id;
            }
        }
        return -1; // Retourne -1 si aucun dossier ne correspond au nom
    }

public static int generateNewId() {
        ArrayList<Integer> ids = GetAllFamiliesId();
        if (ids.isEmpty()) {
            return 1; // Si aucune famille n'existe, commencer à 1
        } else {
            return ids.stream().max(Integer::compareTo).orElse(0) + 1; // Retourner le plus grand ID + 1
        }
    }
}