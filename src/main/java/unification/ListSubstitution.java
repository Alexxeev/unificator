package unification;

import org.jetbrains.annotations.NotNull;
import syntax.ConstantTerm;
import syntax.FunctionalSymbolTerm;
import syntax.Term;
import syntax.VariableTerm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A substitution defined as list of mappings from
 * set of the variables to the term set
 *
 * @param domain mappings from set of the variables
 *               to the term set
 */
record ListSubstitution(
        Map<String, Term> domain
) implements Substitution {

    @NotNull
    @Override
    public Term instantiateVariables(@NotNull final Term term) {
        Objects.requireNonNull(term);
        Term termCopy = Term.copyOf(term);
        return instantiateVariablesInPlace(termCopy);
    }

    @NotNull
    @Override
    public Term instantiateVariablesInPlace(@NotNull final Term term) {
        if (term instanceof VariableTerm) {
            if (domain.containsKey(term.getName())) {
                return domain.get(term.getName());
            }
        }
        if (term instanceof FunctionalSymbolTerm) {
            List<Term> children = term.getChildren();
            for (int i = 0; i < children.size(); i++) {
                term.setChild(
                        i,
                        instantiateVariablesInPlace(children.get(i)));
            }
        }
        return term;
    }

    @NotNull
    @Override
    public Substitution composition(@NotNull final Substitution other) {
        Objects.requireNonNull(other);
        //create shallow copy of map to prevent
        // destruction of original substitution
        Map<String, Term> newDomain = new HashMap<>(domain);
        for (Map.Entry<String, Term> entry : newDomain.entrySet()) {
            Term modifiedTerm = other.instantiateVariables(entry.getValue());
            if (modifiedTerm.getName().equals(entry.getKey())) {
                newDomain.remove(entry.getKey());
            } else {
                newDomain.replace(entry.getKey(), modifiedTerm);
            }
        }
        for (Map.Entry<String, Term> entry : other.domain().entrySet()) {
            if (!domain.containsKey(entry.getKey())) {
                newDomain.put(entry.getKey(), entry.getValue());
            }
        }
        return new ListSubstitution(newDomain);
    }

    @NotNull
    @Override
    public Substitution composition(
            final @NotNull String variable,
            final @NotNull Term replacementTerm) {
        Objects.requireNonNull(variable);
        Objects.requireNonNull(replacementTerm);

        Substitution other = Substitution.of(variable, replacementTerm);
        return composition(other);
    }
}
