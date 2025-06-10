package mypackage.model;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import javafx.beans.property.SimpleStringProperty;


class Main {
    // This is the main method that serves as the entry point for the program.
    public static void main(String[] args) {
        // Print a greeting message to the console.
        Obligation oblig = new Obligation(1,
                                        new SimpleStringProperty("Oblig 2"),
                                        "YYYYYYEEEEEEE",
                                        10000000000l,
                                        LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE),
                                        24,
                                        1);
        String[] address = {"1", "rue de la Paix", "95000", "Paris", "France", ""};
        Applicant applicant = new Applicant(1,
                                        new SimpleStringProperty("Applicant 1"),
                                        "France",
                                        123456789,
                                        "2023-10-01",
                                        "Entreprise",
                                        "SARL",
                                        address,
                                        "M",
                                        new SimpleStringProperty("Dupont"),
                                        "Jean",
                                        "FR",
                                        "1980-01-01",
                                        "Paris",
                                        "Français",
                                        "dupont.jean@gmail.com",
                                        "0123456789",
                                        address,
                                        "France",
                                        "123456789",
                                        "Directeur",
                                        "FR7612345678901234567890123",
                                        "CRLIFRPP",
                                        "LCL",
                                        1);
        Group group = new Group(1, new SimpleStringProperty("Big Group"), "Arnaut", "Bernard");
        Family family = new Family(1, new SimpleStringProperty("Famille Dupont"));
        InvestorNP jean = new InvestorNP(1,
                                    "M",
                                    new SimpleStringProperty("Dupont"),
                                    "Jean",
                                    "FR",
                                    "1980-01-01",
                                    "Paris",
                                    "Français",
                                    "dupont.jean@gmail.com",
                                    "0123456789", 
                                    address, 
                                    "FR7612345678901234567890123",
                                    "CRLIFRPP",
                                    "LCL",
                                    1);
        InvestorNP louis = new InvestorNP(2,
                                    "M",
                                    new SimpleStringProperty("Dupont"),
                                    "Louis", 
                                    "FR",
                                    "1985-01-01",
                                    "Paris",
                                    "Français",
                                    "dupont.louis@gmail.com",
                                    "0123456789", 
                                    address, 
                                    "FR7612345678901234567890123",
                                    "CRLIFRPP",
                                    "LCL",
                                    1);
        oblig.addInvestor(jean.getId(), 10000000000l);
        oblig.addInvestor(louis.getId(), 5000000000l);
        applicant.addObligation(oblig.getId());
        family.addInvestor(jean.getId());
        family.addInvestor(louis.getId());
        louis.addObligation(1);
        jean.addObligation(1);
        group.addMember(applicant.getId());
        DatabaseInteractor.SaveApplicant(applicant);
        DatabaseInteractor.SaveFamily(family);
        DatabaseInteractor.SaveInvestorNP(jean);
        DatabaseInteractor.SaveInvestorNP(louis);
        DatabaseInteractor.SaveObligation(oblig);
        DatabaseInteractor.SaveGroup(group);
        Obligation loadedOblig = DatabaseInteractor.GetObligation(1);
        if (loadedOblig != null) {
            System.out.println("Obligation loaded successfully:");
            System.out.println("Value: " + loadedOblig.getAmount());
        } else {
            System.out.println("Failed to load obligation.");
        }
        Applicant loadedApplicant = DatabaseInteractor.GetApplicant(1);
        if (loadedApplicant != null) {
            System.out.println("Applicant loaded successfully:");
            System.out.println("Name: " + loadedApplicant.getName());
        } else {
            System.out.println("Failed to load applicant.");
        }
        for (Map.Entry<Integer, Long> entry : loadedOblig.getInvestors().entrySet()) {
            InvestorNP loadedInvestor = DatabaseInteractor.GetInvestorNP(entry.getKey());
            Family loadedFamily = DatabaseInteractor.GetFamily(loadedInvestor.getFamilyId());
            if (loadedInvestor != null && loadedFamily != null ) {
                System.out.println("Investor and family loaded successfully:");
                System.out.println("Name: " + loadedInvestor.getFirstName());
                System.out.println("Family Name : " + loadedFamily.getName());
            } else {
                System.out.println("Failed to load investor and family.");
            }
        }
    }
}
