package mypackage.model.DataBaseInteractor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

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

    public static Boolean DeleteGroup(int id){
        String baseDir = "data/groups/";
        String groupName = Integer.toString(id);
        Path groupFolder = Path.of(baseDir, groupName);

        try {
            // Supprimer le dossier et son contenu
            if (Files.exists(groupFolder)) {
                Files.walk(groupFolder)
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
        }
        return false;
    }

    public static ArrayList<Integer> GetAllGroupsId(){
        File[] ListGroupFiles = new File("data/groups/").listFiles(File::isDirectory);
        ArrayList<Integer> ListGroupId = new ArrayList<>();
        for (File file : ListGroupFiles) {
            try {
                ListGroupId.add(Integer.parseInt(file.getName()));
            } catch (NumberFormatException e) {
                // ignorer les dossiers qui ne sont pas des nombres
            }
        }
        return ListGroupId;
    }

    public static int GetGroupByName(String name) {
        ArrayList<Integer> ids = GetAllGroupsId();
        for (int id : ids) {
            Group group = GetGroup(id);
            if (group != null && group.getName().equals(name)) {
                return id;
            }
        }
        return -1; // Retourne -1 si aucun groupe trouvé avec ce nom
    }

    public static int generateNewId() {
        ArrayList<Integer> ids = GetAllGroupsId();
        if (ids.isEmpty()) {
            return 1; // Si aucun groupe n'existe, commencer à 1
        } else {
            return ids.stream().max(Integer::compareTo).orElse(0) + 1; // Retourner le plus grand ID + 1
        }
    }
}
