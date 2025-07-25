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
        if (id <= 0) {
            System.err.println("ID de groupe invalide : " + id);
            return null;
        }
        
        ObjectMapper objectMapper = new ObjectMapper();
        String baseDir = "data/groups/";
        SimpleStringProperty name = new SimpleStringProperty(Integer.toString(id));
        try {
            // Vérifier si le dossier existe
            Path groupPath = Path.of(baseDir, name.get());
            Path dataPath = groupPath.resolve("data.json");
            
            if (Files.exists(groupPath) && Files.exists(dataPath)) {
                Group group = objectMapper.readValue(dataPath.toFile(), Group.class);
                if (group != null) {
                    return group;
                } else {
                    System.err.println("Erreur : groupe null après désérialisation pour ID " + id);
                }
            } else {
                System.err.println("Fichier data.json non trouvé pour le groupe ID " + id);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du groupe ID " + id + " : " + e.getMessage());
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
        File groupsDir = new File("data/groups/");
        if (!groupsDir.exists() || !groupsDir.isDirectory()) {
            System.err.println("Répertoire des groupes non trouvé : data/groups/");
            return new ArrayList<>();
        }
        
        File[] ListGroupFiles = groupsDir.listFiles(File::isDirectory);
        ArrayList<Integer> ListGroupId = new ArrayList<>();
        
        if (ListGroupFiles == null) {
            System.err.println("Erreur lors de la lecture du répertoire des groupes");
            return ListGroupId;
        }
        
        for (File file : ListGroupFiles) {
            try {
                String fileName = file.getName().trim();
                if (!fileName.isEmpty()) {
                    ListGroupId.add(Integer.parseInt(fileName));
                }
            } catch (NumberFormatException e) {
                // ignorer les dossiers qui ne sont pas des nombres
                System.err.println("Nom de dossier invalide pour groupe : " + file.getName());
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
