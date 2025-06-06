package mypackage.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DatabaseInteractor {

    public static Obligation GetObligation(int id){
        ObjectMapper objectMapper = new ObjectMapper();
        String baseDir = "data/obligations/";
        try {
            // Vérifier si le dossier existe
            if (Files.exists(Path.of(baseDir, Integer.toString(id)))) {
                Obligation oblig = objectMapper.readValue(Path.of(baseDir, Integer.toString(id)).resolve("data.json").toFile(),
                                                        Obligation.class);
                return oblig;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static Boolean SaveObligation(Obligation oblig){
        String baseDir = "data/obligations/";
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



    public static InvestorNP GetInvestorNP(int id){
        ObjectMapper objectMapper = new ObjectMapper();
        String baseDir = "data/investors/NP/";
        String name = Integer.toString(id);
        try {
            // Vérifier si le dossier existe
            if (Files.exists(Path.of(baseDir, name))) {
                InvestorNP investor = objectMapper.readValue(Path.of(baseDir, name).resolve("data.json").toFile(),
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
        String name = Integer.toString(id);
        try {
            // Vérifier si le dossier existe
            if (Files.exists(Path.of(baseDir, name))) {
                InvestorLP investor = objectMapper.readValue(Path.of(baseDir, name).resolve("data.json").toFile(),
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



    public static Family GetFamily(int id){
        ObjectMapper objectMapper = new ObjectMapper();
        String baseDir = "data/families/";
        String name = Integer.toString(id);
        try {
            // Vérifier si le dossier existe
            if (Files.exists(Path.of(baseDir, name))) {
                Family family = objectMapper.readValue(Path.of(baseDir, name).resolve("data.json").toFile(),
                                                        Family.class);
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



    public static Applicant GetApplicant(int id){
        ObjectMapper objectMapper = new ObjectMapper();
        String baseDir = "data/applicants/";
        String name = Integer.toString(id);
        try {
            // Vérifier si le dossier existe
            if (Files.exists(Path.of(baseDir, name))) {
                Applicant applicant = objectMapper.readValue(Path.of(baseDir, name).resolve("data.json").toFile(),
                                                        Applicant.class);
                return applicant;
            }
        } catch (IOException e) {
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

    public static Group GetGroup(int id){
        ObjectMapper objectMapper = new ObjectMapper();
        String baseDir = "data/groups/";
        String name = Integer.toString(id);
        try {
            // Vérifier si le dossier existe
            if (Files.exists(Path.of(baseDir, name))) {
                Group group = objectMapper.readValue(Path.of(baseDir, name).resolve("data.json").toFile(),
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
