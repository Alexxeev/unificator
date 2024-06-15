package unification;

import org.jetbrains.annotations.NotNull;
import syntax.TermWithArgs;
import syntax.Term;
import syntax.TermPair;
import syntax.Variable;

import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A more efficient implementation of Robinson's unification algorithm
 * that takes advantage of directed acyclic graph representation of terms. It also
 * reduces excessive method calls by isolating variable terms that are already
 * substituted with term.
 */
public class PolynomialRobinsonUnificationStrategy implements UnificationStrategy {
    private final Map<Term, Term> instantiations = new IdentityHashMap<>();

    @Override
    public @NotNull UnificationResult findUnifier(
            @NotNull final TermPair termPair) {
        Map<Term, Term> bindingList = new HashMap<>();
        try {
            findUnifierRecursive(termPair.term1(), termPair.term2(), bindingList);
        } catch (IllegalStateException e) {
            return UnificationResult.notUnifiable();
        }
        return UnificationResult.unifiable(Substitution.fromTriangularForm(bindingList));
    }

    /**
     * Internal recursive method that finds unifier for two terms
     *
     * @param term1 first term
     * @param term2 second term
     * @param bindingList a list of bindings
     */
    private void findUnifierRecursive(
            Term term1, Term term2, Map<Term, Term> bindingList) {
        term1 = findInstantiation(term1);
        term2 = findInstantiation(term2);
        if (term1 instanceof Variable variable1)
            unifyVariable(variable1, term2, bindingList);
        else if (term2 instanceof Variable variable2)
            unifyVariable(variable2, term1, bindingList);
        else if (!term1.nameEquals(term2))
            throw new IllegalStateException("Symbol clash");
        else if (term1 instanceof TermWithArgs term1WithArgs &&
                 term2 instanceof TermWithArgs term2WithArgs) {
            List<Term> successorsOfTerm1 = term1WithArgs.getArgs();
            List<Term> successorsOfTerm2 = term2WithArgs.getArgs();
            int successorCount = successorsOfTerm1.size();
            for (int i = 0; i < successorCount; i++) {
                Term ithSuccessorOfTerm1 = successorsOfTerm1.get(i);
                Term ithSuccessorOfTerm2 = successorsOfTerm2.get(i);
                if (ithSuccessorOfTerm1 == ithSuccessorOfTerm2)
                    continue;
                findUnifierRecursive(ithSuccessorOfTerm1, ithSuccessorOfTerm2, bindingList);
            }
            instantiations.put(term1, term2);
        }
    }

    private void unifyVariable(Variable variable, Term term, Map<Term, Term> bindingList) {
        if (occurs(variable, term))
            throw new IllegalStateException("Occurs check");
        bindingList.put(variable, term);
        instantiations.put(variable, term);
    }

    private Term findInstantiation(Term term) {
        Term result = term;
        while (instantiations.containsKey(result)) {
            result = instantiations.get(result);
        }
        return result;
    }

    private boolean occurs(Term term1, Term term2) {
        Set<Term> visited = Collections.newSetFromMap(new IdentityHashMap<>());
        return occursRecursive(term1, term2, visited);
    }

    private boolean occursRecursive(Term term1, Term term2, Set<Term> visited) {
        if (!(term2 instanceof TermWithArgs term2WithArgs))
            return term1 == term2;
        if (visited.contains(term2))
            return false;
        visited.add(term2);
        for (Term child : term2WithArgs.getArgs()) {
            if (occursRecursive(term1, findInstantiation(child), visited))
                return true;
        }
        return false;
    }
}
