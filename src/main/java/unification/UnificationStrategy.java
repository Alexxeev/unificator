package unification;

import org.jetbrains.annotations.NotNull;
import syntax.TermNode;

/**
 * This interface describes a unification strategy
 * i.e. an algorithm for unification of two first-order
 * terms
 */
public interface UnificationStrategy {
    /**
     * Finds a unifier of set of two terms.
     * It uses Robinson unification algorithm to find the unifier
     * of two terms.
     *
     * @param term1 first term
     * @param term2 second term
     * @return Result of unification of two terms.
     */
    UnificationResult findUnifier(@NotNull final TermNode term1, @NotNull final TermNode term2);
}
