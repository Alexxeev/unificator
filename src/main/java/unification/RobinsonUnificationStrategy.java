package unification;

import org.jetbrains.annotations.NotNull;
import syntax.FunctionalSymbolTerm;
import syntax.Term;
import syntax.VariableTerm;

import java.util.List;
import java.util.Objects;

public class RobinsonUnificationStrategy implements UnificationStrategy {
    private Substitution substitution;
    private boolean isUnifiable = true;

    @Override
    public UnificationResult findUnifier(@NotNull Term term1, @NotNull Term term2) {
        substitution = Substitution.identity();
        findUnifierRecursive(
                Objects.requireNonNull(term1),
                Objects.requireNonNull(term2));
        return new UnificationResult(
                isUnifiable,
                isUnifiable ? substitution : Substitution.identity());
    }

    private void findUnifierRecursive(@NotNull Term term1, @NotNull Term term2) {
        Term term1Modified = term1, term2Modified = term2;
        if (term1Modified instanceof VariableTerm) {
            term1Modified = substitution.instantiateVariables(term1);
        }
        if (term1Modified instanceof VariableTerm) {
            term2Modified = substitution.instantiateVariables(term2);
        }
        if (term1Modified instanceof VariableTerm
                && Objects.equals(term1Modified.getName(), term2Modified.getName())) {
        } else if (term1Modified instanceof FunctionalSymbolTerm functionalSymbolTermNode1
                && term2Modified instanceof FunctionalSymbolTerm functionalSymbolTermNode2) {
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
        } else if (!(term1Modified instanceof VariableTerm)) {
            findUnifierRecursive(term2Modified, term1Modified);
        } else if (term2Modified.getDomain().contains(term1Modified.toString())) {
            isUnifiable = false;
        } else {
            substitution = substitution.composition(term1Modified.getName(), term2Modified);
        }
    }
}
