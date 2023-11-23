package unification;

import org.jetbrains.annotations.NotNull;
import syntax.Term;

/**
 * This interface describes a unification strategy
 * i.e. an algorithm for unification of two first-order
 * terms
 */
public interface UnificationStrategy {
    @NotNull
    UnificationStrategy ROBINSON = new RobinsonUnificationStrategy();
    @NotNull
    UnificationStrategy ROBINSON_POLYNOMIAL
            = new PolynomialRobinsonUnificationStrategy();

    /**
     * Finds a unifier of set of two terms.
     *
     * @param term1 first term
     * @param term2 second term
     * @return Result of unification of two terms.
     */
    @NotNull
    UnificationResult findUnifier(
            @NotNull final Term term1, @NotNull final Term term2);
}
