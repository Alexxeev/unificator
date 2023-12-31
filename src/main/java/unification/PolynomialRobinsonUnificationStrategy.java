package unification;

import org.jetbrains.annotations.NotNull;
import syntax.ConstantTerm;
import syntax.FunctionalSymbolTerm;
import syntax.Term;
import syntax.TermPair;
import syntax.VariableTerm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A more efficient implementation of Robinson's unification algorithm
 * that takes advantage of directed acyclic graph representation of terms. It also
 * reduces excessive method calls by isolating variable terms that are already
 * substituted with term.
 */
public class PolynomialRobinsonUnificationStrategy implements UnificationStrategy {
    @Override
    public @NotNull UnificationResult findUnifier(
            @NotNull final TermPair termPair) {
        TermPair termPairCopy = TermPair.copyOf(Objects.requireNonNull(termPair));
        Map<Term, Term> bindingList = new HashMap<>();
        try {
            findUnifierRecursive(termPairCopy.term1(), termPairCopy.term2(), bindingList);
        } catch (IllegalStateException e) {
            return UnificationResult.notUnifiable();
        }
        return UnificationResult.unifiable(Substitution.of(bindingList));
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
        if (term1.equals(term2)) {
            //Do nothing
        } else if (term1 instanceof FunctionalSymbolTerm
                && term2 instanceof FunctionalSymbolTerm) {
            if (!term1.getName().equals(term2.getName())) {
                throw new IllegalStateException("Symbol clash");
            }
            List<Term> children1 = term1.getChildren();
            List<Term> children2 = term2.getChildren();
            if (children1.size() != children2.size()) {
                throw new IllegalStateException("Symbol clash");
            }
            for (int i = 0; i < children1.size(); i++) {
                findUnifierRecursive(
                        children1.get(i),
                        children2.get(i),
                        bindingList);
            }
        } else if (term1 instanceof ConstantTerm
                && term2 instanceof ConstantTerm) {
            if (!term1.getName().equals(term2.getName())) {
                throw new IllegalStateException("Symbol clash");
            }
        } else if (!(term1 instanceof VariableTerm)) {
            findUnifierRecursive(term2, term1, bindingList);
        } else if (term2.contains(term1)) {
            throw new IllegalStateException("Occurs check");
        } else {
            bindingList.put(term1, term2);
            replace(term1, term2);
        }
    }

    /**
     * Moves parent nodes of the {@code term} to the {@code replacement}.
     * As a result of this operation {@code term} will be isolated.
     *
     * @param term term to isolate
     * @param replacement term to move parents to
     */
    private void replace(Term term, Term replacement) {
        for (Term parent : term.getParents()) {
            parent.replaceChild(term, replacement);
        }
        replacement.addParents(term.getParents());
        term.removeParents();
    }
}
