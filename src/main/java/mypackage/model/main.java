package mypackage.model;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;


class Main {
    // This is the main method that serves as the entry point for the program.
    public static void main(String[] args) {
        // Print a greeting message to the console.
        Obligation oblig = new Obligation(1,
                                        "Oblig 2",
                                        "YYYYYYEEEEEEE",
                                        10000000000l,
                                        LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE),
                                        24,
                                        1);
        Applicant applicant = new Applicant(1,
                                        "Dupuis",
                                        "Emily",
                                        "dupuis.emily@gmail.com",
                                        "0123456789",
                                        "2 rue de la Paix, Paris",
                                        "FR7612345678901234567890123");
        Family family = new Family(1, "Famille Dupont");
        Investor jean = new Investor(1,
                                    "Dupont",
                                    "Jean", 
                                    "dupont.jean@gmail.com",
                                    "0123456789", 
                                    "1 rue de la Paix, Paris", 
                                    "FR7612345678901234567890123",
                                    1);
        Investor louis = new Investor(2,
                                    "Dupont",
                                    "Louis", 
                                    "dupont.louis@gmail.com",
                                    "0123456789", 
                                    "1 rue de la Paix, Paris", 
                                    "FR7612345678901234567890123",
                                    1);
        oblig.addInvestor(jean.getId(), 10000000000l);
        oblig.addInvestor(louis.getId(), 5000000000l);
        applicant.addObligation(oblig.getId());
        family.addInvestor(jean.getId());
        family.addInvestor(louis.getId());
        louis.addObligation(1);
        jean.addObligation(1);
        DatabaseInteractor.SaveApplicant(applicant);
        DatabaseInteractor.SaveFamily(family);
        DatabaseInteractor.SaveInvestor(jean);
        DatabaseInteractor.SaveInvestor(louis);
        DatabaseInteractor.SaveObligation(oblig);
        Obligation loadedOblig = DatabaseInteractor.GenerateObligation(1);
        if (loadedOblig != null) {
            System.out.println("Obligation loaded successfully:");
            System.out.println("Value: " + loadedOblig.getAmount());
        } else {
            System.out.println("Failed to load obligation.");
        }
        Applicant loadedApplicant = DatabaseInteractor.GenerateApplicant(1);
        if (loadedApplicant != null) {
            System.out.println("Applicant loaded successfully:");
            System.out.println("Name: " + loadedApplicant.getFirstName());
        } else {
            System.out.println("Failed to load applicant.");
        }
        for (Map.Entry<Integer, Long> entry : loadedOblig.getInvestors().entrySet()) {
            Investor loadedInvestor = DatabaseInteractor.GenerateInvestor(entry.getKey());
            Family loadedFamily = DatabaseInteractor.GenerateFamily(loadedInvestor.getFamilyId());
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
