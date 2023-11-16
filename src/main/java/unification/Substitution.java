package unification;

import org.jetbrains.annotations.NotNull;
import syntax.Term;

import java.util.Map;
import java.util.Objects;

/**
 * A representation of a substitution.
 * Substitution is represented as set of mappings from variables to
 * terms. Variables in the domain of the substitution are stored as
 * strings and terms in the range of the substitution are stored as
 * node trees.
 */
public interface Substitution {
    /**
     * Returns an identity substitution. This substitution has
     * empty domain.
     * @return an identity substitution
     */
    static Substitution identity() {
        return new ListSubstitution(Map.of());
    }

    /**
     * Creates a new substitution with provided variable and
     * replacement term.
     * @param variable variable
     * @param replacementTerm replacement term
     * @return A substitution with domain consisting of
     * provided variable.
     */
    static Substitution of(
            @NotNull final String variable,
            @NotNull final Term replacementTerm) {
        return new ListSubstitution(Map.of(
                Objects.requireNonNull(variable),
                Objects.requireNonNull(replacementTerm)));
    }

    static Substitution of(final Map<String, Term> domain) {
        return new ListSubstitution(domain);
    }

    /**
     * Applies substitution to the provided term.
     * Returned term will have all instances of variables
     * contained in the domain of substitution replaced by
     * corresponding terms
     *
     * @param term A term to substitute variables in
     * @return Term with variables in the domain of this substitution
     *         replaced by corresponding terms.
     */
    Term instantiateVariables(Term term);

    /**
     * Performs a composition operation on this substitution
     * with provided substitution.
     * @param other A substitution to compose this substitution with.
     * @return Result of composition of this substitution with
     *         other substitution.
     */
    Substitution composition(Substitution other);

    /**
     * Performs a composition operation on this substitution
     * with other substitution defined by provided variable-term
     * pair.
     * @param variable a variable
     * @param replacementTerm a term
     * @return Result of composition of this substitution with
     *         other substitution.
     */
    Substitution composition(String variable, Term replacementTerm);

    /**
     * Returns a map which entries are pairs of variables and
     * corresponding replacement term.
     *
     * @return A map which entries are pairs of variables and
     *         corresponding replacement term.
     */
    Map<String, Term> domain();
}
