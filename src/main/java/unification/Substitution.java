package unification;

import org.jetbrains.annotations.NotNull;
import syntax.TermWithArgs;
import syntax.Term;
import syntax.Variable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
     * Constructs a new substitution from provided substitution in
     * the triangular (ordered) form.
     *
     * @param bindingList list of bindings
     * @return a new substitution from provided binding list
     */
    @NotNull
    static Substitution fromTriangularForm(final @NotNull Map<Term, Term> bindingList) {
        return new TriangularFormConverter(bindingList).convert();
    }

    /**
     * Returns an identity substitution. This substitution has
     * empty domain.
     * @return an identity substitution
     */
    @NotNull
    static Substitution identity() {
        return IdentitySubstitution.INSTANCE;
    }

    /**
     * Creates a new substitution with provided variable and
     * replacement term.
     * @param variable variable
     * @param replacementTerm replacement term
     * @return A substitution with domain consisting of
     * provided variable.
     */
    @NotNull
    static Substitution of(
            @NotNull final Term variable,
            @NotNull final Term replacementTerm) {
        return new SingleSubstitution(
                Objects.requireNonNull(variable),
                Objects.requireNonNull(replacementTerm)
        );
    }

    /**
     * Creates a new substitution with provided domain
     * @param domain set of variable-term mappings
     * @return A substitution with domain consisting of
     * provided variable.
     */
    @NotNull
    static Substitution of(final @NotNull Map<Term, Term> domain) {
        return new ListSubstitution(
                Collections.unmodifiableMap(
                        Objects.requireNonNull(domain)));
    }

    Term getBinding(Term variable);

    /**
     * Applies substitution to the copy of the provided term.
     * Returned term will have all instances of variables
     * contained in the domain of substitution replaced by
     * corresponding terms
     *
     * @param term A term to substitute variables in
     * @return Term with variables in the domain of this substitution
     *         replaced by corresponding terms.
     */
    @NotNull
    default Term instantiateVariables(@NotNull final Term term) {
        Objects.requireNonNull(term);

        if (term instanceof TermWithArgs termWithArgs) {
            List<Term> newChildren = new ArrayList<>(termWithArgs.getArgs().size());
            for (var child : termWithArgs.getArgs())
                newChildren.add(instantiateVariables(child));

            return new TermWithArgs(term.getName(), newChildren);
        }
        if (term instanceof Variable) {
            return getBinding(term);
        }
        return term;
    }

    /**
     * Returns a map which entries are pairs of variables and
     * corresponding replacement term.
     *
     * @return A map which entries are pairs of variables and
     *         corresponding replacement term.
     */
    @NotNull
    Map<Term, Term> domain();
}
