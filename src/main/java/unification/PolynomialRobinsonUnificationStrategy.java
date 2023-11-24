package unification;

import org.jetbrains.annotations.NotNull;
import syntax.ConstantTerm;
import syntax.FunctionalSymbolTerm;
import syntax.Term;
import syntax.VariableTerm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PolynomialRobinsonUnificationStrategy implements UnificationStrategy {
    @Override
    public @NotNull UnificationResult findUnifier(
            @NotNull final Term term1, @NotNull final Term term2) {
        Term termCopy1 = Term.copyOf(term1);
        Term termCopy2 = Term.copyOf(term2);
        Map<String, Term> bindingList = new HashMap<>();
        try {
            findUnifierRecursive(termCopy1, termCopy2, bindingList);
        } catch (IllegalStateException e) {
            return UnificationResult.notUnifiable();
        }
        return UnificationResult.unifiable(Substitution.of(bindingList));
    }

    private void findUnifierRecursive(
            Term term1, Term term2, Map<String, Term> bindingList) {
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
            bindingList.put(term1.getName(), term2);
            replace(term1, term2);
        }
    }

    private void replace(Term term, Term replacement) {
        for (Term parent : term.getParents()) {
            parent.replaceChild(term, replacement);
        }
        replacement.addParents(term.getParents());
        term.removeParents();
    }
}
