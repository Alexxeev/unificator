package unification;

import org.jetbrains.annotations.NotNull;
import syntax.ConstantTerm;
import syntax.FunctionalSymbolTerm;
import syntax.Term;
import syntax.VariableTerm;

import java.util.HashMap;
import java.util.Map;

public class PolynomialRobinsonUnificationStrategy implements UnificationStrategy {
    @Override
    public UnificationResult findUnifier(@NotNull Term term1, @NotNull Term term2) {
        Term termCopy1 = term1.deepCopy();
        Term termCopy2 = term2.deepCopy();
        Map<String, Term> bindingList = new HashMap<>();
        try {
            findUnifierRecursive(termCopy1, termCopy2, bindingList);
        } catch (IllegalStateException e) {
            return UnificationResult.notUnifiable();
        }
        return UnificationResult.unifiable(Substitution.of(bindingList));
    }

    private void findUnifierRecursive(Term term1, Term term2, Map<String, Term> bindingList) {
        if (term1.equals(term2)) {
            //Do nothing
        } else if (term1 instanceof FunctionalSymbolTerm functionalSymbolTerm1
                && term2 instanceof FunctionalSymbolTerm functionalSymbolTerm2) {
            if (!term1.getName().equals(term2.getName())) {
                throw new IllegalStateException("Symbol clash");
            }
            int size1 = functionalSymbolTerm1.getChildren().size();
            int size2 = functionalSymbolTerm2.getChildren().size();
            if (size1 != size2) {
                throw new IllegalStateException("Symbol clash");
            }
            for (int i = 0; i < size1; i++) {
                findUnifierRecursive(
                        functionalSymbolTerm1.getChild(i),
                        functionalSymbolTerm2.getChild(i),
                        bindingList);
            }
        } else if (term1 instanceof ConstantTerm && term2 instanceof ConstantTerm) {
            if (!term1.getName().equals(term2.getName())) {
                throw new IllegalStateException("Symbol clash");
            }
        } else if (!(term1 instanceof VariableTerm)) {
            findUnifierRecursive(term2, term1, bindingList);
        } else if (term2.contains(term1)) {
            throw new IllegalStateException("Occurs check");
        } else {
            bindingList.put(term1.getName(), term2);
            replace(term1, term2);
        }
    }

    private void replace(Term term, Term replacement) {
        for (Term parent : term.getParents()) {
            ((FunctionalSymbolTerm)parent).replaceChild(term, replacement);
        }
        replacement.addParents(term.getParents());
        term.removeParents();
    }
}
