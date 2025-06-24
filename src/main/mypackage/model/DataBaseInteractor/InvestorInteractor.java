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
        ObjectMapper objectMapper = new ObjectMapper();
        String baseDir = "data/investors/NP/";
        SimpleStringProperty name = new SimpleStringProperty(Integer.toString(id));
        try {
            // Vérifier si le dossier existe
            if (Files.exists(Path.of(baseDir, name.get()))) {
                InvestorNP investor = objectMapper.readValue(Path.of(baseDir, name.get()).resolve("data.json").toFile(),
                                                            InvestorNP.class);
                return investor;
            }
        } catch (IOException e) {
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
        ObjectMapper objectMapper = new ObjectMapper();
        String baseDir = "data/investors/LP/";
        SimpleStringProperty name = new SimpleStringProperty(Integer.toString(id));
        try {
            // Vérifier si le dossier existe
            if (Files.exists(Path.of(baseDir, name.get()))) {
                InvestorLP investor = objectMapper.readValue(Path.of(baseDir, name.get()).resolve("data.json").toFile(),
                                                            InvestorLP.class);
                return investor;
            }
        } catch (IOException e) {
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
        File[] ListInvestorLPFiles = new File("data/investors/LP/").listFiles(File::isDirectory);
        File[] ListInvestorNPFiles = new File("data/investors/NP/").listFiles(File::isDirectory);
        ArrayList<Integer> ListInvestorId = new ArrayList<>();
        for (File file : ListInvestorLPFiles) {
            try {
                ListInvestorId.add(Integer.parseInt(file.getName()));
            } catch (NumberFormatException e) {
                // ignorer les dossiers qui ne sont pas des nombres
            }
        }
        for (File file : ListInvestorNPFiles) {
            try {
                ListInvestorId.add(Integer.parseInt(file.getName()));
            } catch (NumberFormatException e) {
                // ignorer les dossiers qui ne sont pas des nombres
            }
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
