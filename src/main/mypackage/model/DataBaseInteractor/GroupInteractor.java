package mypackage.model.DataBaseInteractor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.beans.property.SimpleStringProperty;
import mypackage.model.Group;

public class GroupInteractor {

    public static Group GetGroup(int id){
        ObjectMapper objectMapper = new ObjectMapper();
        String baseDir = "data/groups/";
        SimpleStringProperty name = new SimpleStringProperty(Integer.toString(id));
        try {
            // Vérifier si le dossier existe
            if (Files.exists(Path.of(baseDir, name.get()))) {
                Group group = objectMapper.readValue(Path.of(baseDir, name.get()).resolve("data.json").toFile(),
                                                        Group.class);
                return group;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static Boolean SaveGroup(Group group){
        String baseDir = "data/groups/";
        String groupName = Integer.toString(group.getId());
        Path groupFolder = Path.of(baseDir, groupName);

        try {
            // Créer le dossier s'il n'existe pas déjà
            if (!Files.exists(groupFolder)) {
                Files.createDirectories(groupFolder);
            }

            // Créer le fichier JSON
            File jsonFile = groupFolder.resolve("data.json").toFile();

            // Sérialiser avec Jackson
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, group);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
