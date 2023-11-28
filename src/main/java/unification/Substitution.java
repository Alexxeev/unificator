package unification;

import org.jetbrains.annotations.NotNull;
import syntax.ConstantTerm;
import syntax.FunctionalSymbolTerm;
import syntax.Term;
import syntax.VariableTerm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
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
    @NotNull
    static Substitution fromTriangularForm(final @NotNull Map<Term, Term> bindingList) {
        final Map<Term, Term> domain = new HashMap<>();
        final Map<Term, Term> ready = new HashMap<>();
        for (Map.Entry<Term, Term> entry : bindingList.entrySet()) {
            domain.put(
                    entry.getKey(),
                    exploreVariable(entry.getKey(), bindingList, ready));
        }
        return Substitution.of(domain);
    }

    private static Term exploreVariable(
            Term variable,
            Map<Term, Term> bindingList,
            Map<Term, Term> ready) {
        if (ready.containsKey(variable)) {
            return ready.get(variable);
        }
        Term result = descend(bindingList.get(variable), bindingList, ready);
        if (result == null) {
            result = variable;
        }
        ready.put(variable, result);
        return result;
    }

    private static Term descend(
            Term term,
            Map<Term, Term> bindingList,
            Map<Term, Term> ready
    ) {
        if (term == null) {
            return null;
        }
        if (term instanceof VariableTerm) {
            return exploreVariable(term, bindingList, ready);
        }
        if (term instanceof ConstantTerm) {
            return term;
        }
        if (ready.containsKey(term)) {
            return ready.get(term);
        }
        List<Term> result = exploreArgs(term.getChildren(), bindingList, ready);
        if (result.equals(term.getChildren())) {
            ready.put(term, term);
        } else {
            ready.put(term, new FunctionalSymbolTerm(term.getName(), result));
        }
        return ready.get(term);

    }

    private static List<Term> exploreArgs(
            List<Term> args,
            Map<Term, Term> bindingList,
            Map<Term, Term> ready) {
        List<Term> newArgs = new ArrayList<>();
        for (Term arg : args) {
            newArgs.add(descend(arg, bindingList, ready));
        }
        return newArgs;
    }
    /**
     * Returns an identity substitution. This substitution has
     * empty domain.
     * @return an identity substitution
     */
    @NotNull
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
    @NotNull
    static Substitution of(
            @NotNull final Term variable,
            @NotNull final Term replacementTerm) {
        return new ListSubstitution(Map.of(
                Objects.requireNonNull(variable),
                Objects.requireNonNull(replacementTerm)));
    }

    /**
     * Creates a new substitution with provided domain
     * @param domain set of variable-term mappings
     * @return A substitution with domain consisting of
     * provided variable.
     */
    @NotNull
    static Substitution of(final @NotNull Map<Term, Term> domain) {
        return new ListSubstitution(Objects.requireNonNull(domain));
    }

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
    Term instantiateVariables(Term term);

    /**
     * Applies substitution to the provided term.
     * Returned term will have all instances of variables
     * contained in the domain of substitution replaced by
     * corresponding terms
     * <p>
     * Note: this operation is destructive and modifies the
     * term if the term contains functional symbols
     *
     * @param term A term to substitute variables in
     * @return Term with variables in the domain of this substitution
     *         replaced by corresponding terms.
     */
    @NotNull
    Term instantiateVariablesInPlace(Term term);

    /**
     * Performs a composition operation on this substitution
     * with provided substitution.
     * @param other A substitution to compose this substitution with.
     * @return Result of composition of this substitution with
     *         other substitution.
     */
    @NotNull
    Substitution composition(@NotNull final Substitution other);

    /**
     * Performs a composition operation on this substitution
     * with other substitution defined by provided variable-term
     * pair.
     * @param variable a variable
     * @param replacementTerm a term
     * @return Result of composition of this substitution with
     *         other substitution.
     */
    @NotNull
    Substitution composition(
            @NotNull final Term variable,
            @NotNull final Term replacementTerm);

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
