package unification;

import org.jetbrains.annotations.NotNull;
import syntax.Term;
import syntax.TermPair;

/**
 * This interface describes a unification strategy
 * i.e. an algorithm for unification of two first-order
 * terms
 */
public interface UnificationStrategy {
    /**
     * Finds a unifier of set of two terms.
     *
     * @param termPair a term pair
     * @return Result of unification of two terms.
     */
    @NotNull
    UnificationResult findUnifier(
            @NotNull final TermPair termPair);
}
