import syntax.Term;
import unification.UnificationResult;
import unification.UnificationStrategy;

/**
 * Console application main class
 */
public class Main {
    /**
     * Main point of entry to the console application
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Provide two terms enclosed in the double quotes");
            System.exit(1);
        }
        Term term1 = Term.fromString(args[0]);
        Term term2 = Term.fromString(args[1]);

        UnificationResult unificationResult = UnificationStrategy.ROBINSON.findUnifier(term1, term2);
        if (unificationResult.isUnifiable()) {
            System.out.println("Found unifier for the terms:");
            System.out.println("- " + args[0]);
            System.out.println("- " + args[1]);
            System.out.println("Resulting unifier is:");
            System.out.println(unificationResult.unifier().domain());
            System.out.println("Resulting term is:");
            System.out.println(unificationResult.unifier().instantiateVariables(term1));
        } else {
            System.out.println("Terms");
            System.out.println("- " + args[0]);
            System.out.println("- " + args[1]);
            System.out.println("are not unifiable");
        }
        System.exit(0);
    }
}
