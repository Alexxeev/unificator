package unification;

import syntax.PreOrderTermIterator;
import syntax.TermNode;

import java.util.Iterator;
import java.util.Objects;

/**
 * This utility class provides methods to perform unification
 * operation on the pair of terms.
 * Note that unification of arbitrary set of terms isn't implemented yet.
 */
public final class Unification {
    /**
     * Represents a result of unification of input set.
     * It contains status of unification (successful unification
     * or term set is not unifiable) and unifier of the input set.
     *
     * @param isUnifiable true if input set is unifiable.
     * @param unifier unifier of the input set if unifier is found.
     *                It returns identity substitution
     *                if input set is not unifiable.
     */
    public record Result(
            boolean isUnifiable,
            Substitution unifier
    ) { }

    /**
     * A result of search for disagreement set for two terms
     *
     * @param offendingVariable a variable that is needed to be
     *                          replaced with corresponding term
     * @param offendingTerm a term to replace corresponding
     *                      variable
     * @param status a status of the search
     */
    private record DisagreementPairSearchResult(
            TermNode offendingVariable,
            TermNode offendingTerm,
            DisagreementStatus status
    ) { }

    /**
     * A status of the search for disagreement set for two terms
     */
    private enum DisagreementStatus {
        /**
         * Search is successful. It indicates that there is a variable
         * and the term that does not contain the variable
         */
        FOUND,
        /**
         * Symbol clash. It indicates one of the three states:
         * <p>
         * 1. there are two different functional
         * symbols
         * <p>
         * 2. there is a variable that is contained in
         * the other term
         * <p>
         * 3. there is no variable occurred
         */
        SYMBOL_CLASH,
        /**
         * Terms are identical.
         */
        NO_DISAGREEMENT
    }

    /**
     * Do not instantiate this class
     */
    private Unification() {
        throw new AssertionError("instantiating utility class");
    }

    /**
     * Finds disagreement set for the two provided terms.
     *
     * @param root1 root of the first term syntax tree
     * @param root2 root of the second term syntax tree
     * @return result of the search for disagreement set
     */
    private static DisagreementPairSearchResult findDisagreement(
            final TermNode root1,
            final TermNode root2) {
        Iterator<TermNode> term1NodeIterator = new PreOrderTermIterator(
                root1);
        Iterator<TermNode> term2NodeIterator = new PreOrderTermIterator(
                root2);
        TermNode term1 = TermNode.empty();
        TermNode term2 = TermNode.empty();
        while (term1NodeIterator.hasNext() && term2NodeIterator.hasNext()) {
            term1 = term1NodeIterator.next();
            term2 = term2NodeIterator.next();
            if (term1.getName().equals(term2.getName())) {
                //Node names are equal, continue scanning
                continue;
            } else if (!term1.isVariable() && !term2.isVariable()) {
                // If there is no variable then no substitution can occur
                // we can conclude that disagreement pair is not unifiable
                return new DisagreementPairSearchResult(
                        term1, term2, DisagreementStatus.SYMBOL_CLASH);
            } else if (term1.isVariable()
                    && term2.getDomain().contains(term1.getName())) {
                return new DisagreementPairSearchResult(
                        term1, term2, DisagreementStatus.SYMBOL_CLASH);
            } else if (term2.isVariable()
                    && term1.getDomain().contains(term2.getName())) {
                return new DisagreementPairSearchResult(
                        term1, term2, DisagreementStatus.SYMBOL_CLASH);
            }
            if (term1.isVariable()) {
                return new DisagreementPairSearchResult(
                        term1, term2, DisagreementStatus.FOUND);
            } else {
                return new DisagreementPairSearchResult(
                        term2, term1, DisagreementStatus.FOUND);
            }
        }
        return new DisagreementPairSearchResult(
                term1, term2, DisagreementStatus.NO_DISAGREEMENT);
    }

    /**
     * Finds a unifier of set of two terms.
     * It uses Robinson unification algorithm to find the unifier
     * of two terms.
     *
     * @param term1 first term
     * @param term2 second term
     * @return Result of unification of two terms.
     */
    public static Result findUnifier(final TermNode term1, final TermNode term2) {
        TermNode modifiedTerm1 = Objects.requireNonNull(term1);
        TermNode modifiedTerm2 = Objects.requireNonNull(term2);
        Substitution substitution = Substitution.identity();
        while (true) {
            DisagreementPairSearchResult result =
                    findDisagreement(modifiedTerm1, modifiedTerm2);
            switch (result.status()) {
                case NO_DISAGREEMENT -> {
                    return new Result(true, substitution);
                }
                case SYMBOL_CLASH -> {
                    return new Result(false, Substitution.identity());
                }
                case FOUND -> {
                    substitution = substitution.composition(
                            Substitution.of(
                                    result.offendingVariable.getName(),
                                    result.offendingTerm)
                    );
                    modifiedTerm1 = substitution.instantiateVariables(modifiedTerm1);
                    modifiedTerm2 = substitution.instantiateVariables(modifiedTerm2);
                }
                default -> throw new AssertionError(
                        "unexpected disagreement search result");
            }
        }
    }
}
