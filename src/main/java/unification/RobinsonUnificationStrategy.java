package unification;

import org.jetbrains.annotations.NotNull;
import syntax.ConstantTerm;
import syntax.FunctionalSymbolTerm;
import syntax.Term;
import syntax.VariableTerm;

import java.util.List;
import java.util.Objects;

/**
 * An implementation of Robinson's unification algorithm
 */
public class RobinsonUnificationStrategy implements UnificationStrategy {
    private Substitution substitution;
    private boolean isUnifiable = true;

    @Override
    public UnificationResult findUnifier(@NotNull Term term1, @NotNull Term term2) {
        Objects.requireNonNull(term1);
        Objects.requireNonNull(term2);
        substitution = Substitution.identity();
        findUnifierRecursive(term1.deepCopy(), term2.deepCopy());
        return new UnificationResult(
                isUnifiable,
                isUnifiable ? substitution : Substitution.identity());
    }

    private void findUnifierRecursive(@NotNull Term term1, @NotNull Term term2) {
        if (term1 instanceof VariableTerm) {
            term1 = substitution.instantiateVariablesInPlace(term1);
        }
        if (term2 instanceof VariableTerm) {
            term2 = substitution.instantiateVariablesInPlace(term2);
        }
        if (term1 instanceof VariableTerm
                && Objects.equals(term1.getName(), term2.getName())) {
        } else if (term1 instanceof FunctionalSymbolTerm functionalSymbolTermNode1
                && term2 instanceof FunctionalSymbolTerm functionalSymbolTermNode2) {
            String name1 = functionalSymbolTermNode1.getName();
            String name2 = functionalSymbolTermNode2.getName();
            List<Term> children1 = functionalSymbolTermNode1.getChildren();
            List<Term> children2 = functionalSymbolTermNode2.getChildren();
            if (!name1.equals(name2) || children1.size() != children2.size()) {
                isUnifiable = false;
                return;
            }
            for (int i = 0; i < children1.size(); i++) {
                findUnifierRecursive(children1.get(i), children2.get(i));
            }
        } else if (term1 instanceof ConstantTerm && term2 instanceof ConstantTerm) {
            if (!term1.getName().equals(term2.getName())) {
                isUnifiable = false;
            }
        } else if (!(term1 instanceof VariableTerm)) {
            findUnifierRecursive(term2, term1);
        } else if (term2.getDomain().contains(term1.toString())) {
            isUnifiable = false;
        } else {
            substitution = substitution.composition(term1.getName(), term2);
        }
    }
}
