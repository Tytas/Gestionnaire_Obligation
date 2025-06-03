package mypackage.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DatabaseInteractor {

    static Obligation GenerateObligation(String name){
        ObjectMapper objectMapper = new ObjectMapper();
        String baseDir = "data/obligations/";
        try {
            // Vérifier si le dossier existe
            if (Files.exists(Path.of(baseDir, name))) {
                Obligation oblig = objectMapper.readValue(Path.of(baseDir, name).resolve("data.json").toFile(),
                                                        Obligation.class);
                return oblig;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    static Boolean SaveObligation(Obligation oblig){
        String baseDir = "data/obligations/";
        String obligationName = oblig.getName();
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
}
