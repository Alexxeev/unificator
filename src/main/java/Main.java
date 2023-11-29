import syntax.Term;
import syntax.TermPair;
import unification.RobinsonUnificationStrategy;
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
        TermPair termPair = TermPair.fromStrings(args[0], args[1]);

        UnificationResult unificationResult = new RobinsonUnificationStrategy().findUnifier(termPair);
        if (unificationResult.isUnifiable()) {
            System.out.println("Found unifier for the terms:");
            System.out.println("- " + args[0]);
            System.out.println("- " + args[1]);
            System.out.println("Resulting unifier is:");
            System.out.println(unificationResult.unifier().domain());
            System.out.println("Resulting term is:");
            System.out.println(unificationResult.unifier().instantiateVariables(termPair.term1()));
        } else {
            System.out.println("Terms");
            System.out.println("- " + args[0]);
            System.out.println("- " + args[1]);
            System.out.println("are not unifiable");
        }
        System.exit(0);
    }
}
