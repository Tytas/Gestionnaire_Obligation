package mypackage.model.DataBaseInteractor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.beans.property.SimpleStringProperty;
import mypackage.model.Obligation;

public class ObligationInteractor {

    public static Obligation GetObligation(int id){
        ObjectMapper objectMapper = new ObjectMapper();
        String baseDir = "dataTEST/obligations/";
        SimpleStringProperty name = new SimpleStringProperty(Integer.toString(id));
        try {
            // Vérifier si le dossier existe
            if (Files.exists(Path.of(baseDir, name.get()))) {
                Obligation oblig = objectMapper.readValue(Path.of(baseDir, name.get()).resolve("data.json").toFile(),
                                                        Obligation.class);
                return oblig;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static Obligation GetObligationByName(String name){
        ArrayList<Integer> ids = GetAllObligationsId();
        for (Integer id : ids) {
            Obligation oblig = GetObligation(id);
            if (oblig != null && oblig.getName().equals(name)) {
                return oblig;
            }
        }
        return null; // Retourne null si aucune obligation avec ce nom n'est trouvée
    }

    public static ArrayList<Integer> GetAllObligationsId(){
        File[] ListObligFiles = new File("dataTEST/obligations/").listFiles(File::isDirectory);
        ArrayList<Integer> ListObligId = new ArrayList<>();
        if (ListObligFiles == null) {
            return ListObligId; // Retourner une liste vide si aucun dossier n'est trouvé
        }
        for (File file : ListObligFiles) {
            try {
                ListObligId.add(Integer.parseInt(file.getName()));
            } catch (NumberFormatException e) {
                // ignorer les dossiers qui ne sont pas des nombres
            }
        }
        return ListObligId;
    }

    public static Boolean SaveObligation(Obligation oblig){
        String baseDir = "dataTEST/obligations/";
        String obligationName = Integer.toString(oblig.getId());
        Path obligationFolder = Path.of(baseDir, obligationName);

        try {
            // Créer le dossier s'il n'existe pas déjà
            if (!Files.exists(obligationFolder)) {
                Files.createDirectories(obligationFolder);
            }

            // Créer le fichier JSON
            File jsonFile = obligationFolder.resolve("data.json").toFile();

            // Sérialiser avec Jackson
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, oblig);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean DeleteObligation(int id){
        String baseDir = "dataTEST/obligations/";
        String obligationName = Integer.toString(id);
        Path obligationFolder = Path.of(baseDir, obligationName);

        try {
            // Supprimer le dossier et son contenu
            if (Files.exists(obligationFolder)) {
                Files.walk(obligationFolder)
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

    public static int generateNewId() {
        ArrayList<Integer> ids = GetAllObligationsId();
        if (ids.isEmpty()) {
            return 1; // Si aucune obligation n'existe, commencer à 1
        } else {
            return ids.stream().max(Integer::compareTo).orElse(0) + 1; // Retourner le plus grand ID + 1
        }
    }
}
