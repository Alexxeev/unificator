package unification;

import syntax.Term;

import java.util.Map;

/**
 * A substitution defined as list of mappings from
 * set of the variables to the term set
 *
 * @param domain mappings from set of the variables
 *               to the term set
 */
record ListSubstitution(
        Map<Term, Term> domain
) implements Substitution {

    @Override
    public Term getBinding(Term variable) {
        return domain.getOrDefault(variable, variable);
    }
}
