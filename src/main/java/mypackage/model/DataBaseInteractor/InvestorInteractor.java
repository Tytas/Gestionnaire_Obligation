package mypackage.model.DataBaseInteractor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.beans.property.SimpleStringProperty;
import mypackage.model.InvestorLP;
import mypackage.model.InvestorNP;
import mypackage.model.Investor;

public class InvestorInteractor {

    public static InvestorNP GetInvestorNP(int id){
        if (id <= 0) {
            System.err.println("ID d'investisseur NP invalide : " + id);
            return null;
        }
        
        ObjectMapper objectMapper = new ObjectMapper();
        String baseDir = "data/investors/NP/";
        SimpleStringProperty name = new SimpleStringProperty(Integer.toString(id));
        try {
            // Vérifier si le dossier existe
            Path investorPath = Path.of(baseDir, name.get());
            Path dataPath = investorPath.resolve("data.json");
            
            if (Files.exists(investorPath) && Files.exists(dataPath)) {
                InvestorNP investor = objectMapper.readValue(dataPath.toFile(), InvestorNP.class);
                if (investor != null) {
                    return investor;
                } else {
                    System.err.println("Erreur : investisseur NP null après désérialisation pour ID " + id);
                }
            } 
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture de l'investisseur NP ID " + id + " : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static Boolean SaveInvestorNP(InvestorNP investor){
        String baseDir = "data/investors/NP/";
        String investorName = Integer.toString(investor.getId());
        Path investorFolder = Path.of(baseDir, investorName);

        try {
            // Créer le dossier s'il n'existe pas déjà
            if (!Files.exists(investorFolder)) {
                Files.createDirectories(investorFolder);
            }

            // Créer le fichier JSON
            File jsonFile = investorFolder.resolve("data.json").toFile();

            // Sérialiser avec Jackson
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, investor);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static InvestorLP GetInvestorLP(int id){
        if (id <= 0) {
            System.err.println("ID d'investisseur LP invalide : " + id);
            return null;
        }
        
        ObjectMapper objectMapper = new ObjectMapper();
        String baseDir = "data/investors/LP/";
        SimpleStringProperty name = new SimpleStringProperty(Integer.toString(id));
        try {
            // Vérifier si le dossier existe
            Path investorPath = Path.of(baseDir, name.get());
            Path dataPath = investorPath.resolve("data.json");
            
            if (Files.exists(investorPath) && Files.exists(dataPath)) {
                InvestorLP investor = objectMapper.readValue(dataPath.toFile(), InvestorLP.class);
                if (investor != null) {
                    return investor;
                } else {
                    System.err.println("Erreur : investisseur LP null après désérialisation pour ID " + id);
                }
            } 
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture de l'investisseur LP ID " + id + " : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static Boolean SaveInvestorLP(InvestorLP investor){
        String baseDir = "data/investors/LP/";
        String investorName = Integer.toString(investor.getId());
        Path investorFolder = Path.of(baseDir, investorName);

        try {
            // Créer le dossier s'il n'existe pas déjà
            if (!Files.exists(investorFolder)) {
                Files.createDirectories(investorFolder);
            }

            // Créer le fichier JSON
            File jsonFile = investorFolder.resolve("data.json").toFile();

            // Sérialiser avec Jackson
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, investor);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean SaveInvestor(Investor investor){
        if (investor instanceof InvestorNP) {
            return SaveInvestorNP((InvestorNP) investor);
        } else if (investor instanceof InvestorLP) {
            return SaveInvestorLP((InvestorLP) investor);
        }
        return false; // Si l'investisseur n'est ni NP ni LP
    }
    
    public static Boolean DeleteInvestorNP(int id){
        String baseDir = "data/investors/NP/";
        String InvestorNPName = Integer.toString(id);
        Path InvestorNPFolder = Path.of(baseDir, InvestorNPName);

        try {
            // Supprimer le dossier et son contenu
            if (Files.exists(InvestorNPFolder)) {
                Files.walk(InvestorNPFolder)
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


    public static Boolean DeleteInvestorLP(int id){
        String baseDir = "data/investors/LP/";
        String InvestorLPName = Integer.toString(id);
        Path InvestorLPFolder = Path.of(baseDir, InvestorLPName);

        try {
            // Supprimer le dossier et son contenu
            if (Files.exists(InvestorLPFolder)) {
                Files.walk(InvestorLPFolder)
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

    public static Boolean DeleteInvestor(int id){
        InvestorNP investorNP = GetInvestorNP(id);
        if (investorNP != null) {
            return DeleteInvestorNP(id);
        }
        InvestorLP investorLP = GetInvestorLP(id);
        if (investorLP != null) {
            return DeleteInvestorLP(id);
        }
        return false; // Si l'investisseur n'est ni NP ni LP
    }

    public static ArrayList<Integer> GetAllInvestorId(){
        File investorLPDir = new File("data/investors/LP/");
        File investorNPDir = new File("data/investors/NP/");
        ArrayList<Integer> ListInvestorId = new ArrayList<>();
        
        // Vérifier les répertoires LP
        if (investorLPDir.exists() && investorLPDir.isDirectory()) {
            File[] ListInvestorLPFiles = investorLPDir.listFiles(File::isDirectory);
            if (ListInvestorLPFiles != null) {
                for (File file : ListInvestorLPFiles) {
                    try {
                        String fileName = file.getName().trim();
                        if (!fileName.isEmpty()) {
                            ListInvestorId.add(Integer.parseInt(fileName));
                        }
                    } catch (NumberFormatException e) {
                        // ignorer les dossiers qui ne sont pas des nombres
                        System.err.println("Nom de dossier invalide pour investisseur LP : " + file.getName());
                    }
                }
            } else {
                System.err.println("Erreur lors de la lecture du répertoire des investisseurs LP");
            }
        } else {
            System.err.println("Répertoire des investisseurs LP non trouvé : data/investors/LP/");
        }
        
        // Vérifier les répertoires NP
        if (investorNPDir.exists() && investorNPDir.isDirectory()) {
            File[] ListInvestorNPFiles = investorNPDir.listFiles(File::isDirectory);
            if (ListInvestorNPFiles != null) {
                for (File file : ListInvestorNPFiles) {
                    try {
                        String fileName = file.getName().trim();
                        if (!fileName.isEmpty()) {
                            ListInvestorId.add(Integer.parseInt(fileName));
                        }
                    } catch (NumberFormatException e) {
                        // ignorer les dossiers qui ne sont pas des nombres
                        System.err.println("Nom de dossier invalide pour investisseur NP : " + file.getName());
                    }
                }
            } else {
                System.err.println("Erreur lors de la lecture du répertoire des investisseurs NP");
            }
        } else {
            System.err.println("Répertoire des investisseurs NP non trouvé : data/investors/NP/");
        }
        return ListInvestorId;
    }

    public static Investor GetInvestor(int id) {
        InvestorNP investorNP = GetInvestorNP(id);
        if (investorNP != null) {
            return investorNP;
        }
        InvestorLP investorLP = GetInvestorLP(id);
        if (investorLP != null) {
            return investorLP;
        }
        System.out.println("Investor with ID " + id + " not found.");
        return null; // Si l'investisseur n'est ni NP ni LP
    }

    public static int GetInvestorByName(String name) {
        ArrayList<Integer> ids = GetAllInvestorId();
        for (Integer id : ids) {
            Investor investor = GetInvestor(id);
            if (investor != null && investor.getName().equals(name)) {
                return id;
            }
        }
        return -1; // Si l'investisseur n'est pas trouvé
    }

    public static int generateNewId() {
        ArrayList<Integer> ids = GetAllInvestorId();
        if (ids.isEmpty()) {
            return 1; // Si aucun investisseur n'existe, commencer à 1
        } else {
            return ids.stream().max(Integer::compareTo).orElse(0) + 1; // Retourner le plus grand ID + 1
        }
    }
}
