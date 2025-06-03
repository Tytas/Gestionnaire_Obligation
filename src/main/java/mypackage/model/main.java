package mypackage.model;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class Main {
    // This is the main method that serves as the entry point for the program.
    public static void main(String[] args) {
        // Print a greeting message to the console.
        Obligation oblig = new Obligation("Oblig 2",
                                        "YYYYYYEEEEEEE",
                                        10000000000l,
                                        LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE),
                                        24);
        DatabaseInteractor.SaveObligation(oblig);
        Obligation loadedOblig = DatabaseInteractor.GenerateObligation("Oblig 2");
        if (loadedOblig != null) {
            System.out.println("Obligation loaded successfully:");
            System.out.println("Value: " + loadedOblig.getAmount());
        } else {
            System.out.println("Failed to load obligation.");
        }
    }
}
