package unification;

import org.jetbrains.annotations.NotNull;
import syntax.ConstantTerm;
import syntax.FunctionalSymbolTerm;
import syntax.Term;
import syntax.VariableTerm;

import java.util.List;
import java.util.Objects;
import java.util.Stack;

/**
 * An implementation of Robinson's unification algorithm
 */
public class RobinsonUnificationStrategy implements UnificationStrategy {
    @Override
    public UnificationResult findUnifier(@NotNull Term term1, @NotNull Term term2) {
        Objects.requireNonNull(term1);
        Objects.requireNonNull(term2);
        Substitution substitution = Substitution.identity();
        Stack<Term> termStack = new Stack<>();
        termStack.push(term1);
        termStack.push(term2);
        while (!termStack.isEmpty()) {
            Term currentTerm2 = termStack.pop();
            Term currentTerm1 = termStack.pop();
            if (currentTerm1 instanceof VariableTerm) {
                currentTerm1 = substitution.instantiateVariablesInPlace(currentTerm1);
            }
            if (currentTerm2 instanceof VariableTerm) {
                currentTerm2 = substitution.instantiateVariablesInPlace(currentTerm2);
            }
            if (currentTerm1 instanceof VariableTerm
                    && Objects.equals(currentTerm1.getName(), currentTerm2.getName())) {
                // Do nothing
            } else if (currentTerm1 instanceof FunctionalSymbolTerm functionalSymbolTermNode1
                    && currentTerm2 instanceof FunctionalSymbolTerm functionalSymbolTermNode2) {
                String name1 = functionalSymbolTermNode1.getName();
                String name2 = functionalSymbolTermNode2.getName();
                List<Term> children1 = functionalSymbolTermNode1.getChildren();
                List<Term> children2 = functionalSymbolTermNode2.getChildren();
                if (!name1.equals(name2) || children1.size() != children2.size()) {
                    return UnificationResult.notUnifiable();
                }
                for (int i = children1.size() - 1; i >= 0; i--) {
                    termStack.push(children1.get(i));
                    termStack.push(children2.get(i));
                }
            } else if (currentTerm1 instanceof ConstantTerm && currentTerm2 instanceof ConstantTerm) {
                if (!currentTerm1.getName().equals(currentTerm2.getName())) {
                    return UnificationResult.notUnifiable();
                }
            } else if (!(currentTerm1 instanceof VariableTerm)) {
                termStack.push(currentTerm2);
                termStack.push(currentTerm1);
            } else if (currentTerm2.getDomain().contains(currentTerm1.toString())) {
                return UnificationResult.notUnifiable();
            } else {
                substitution = substitution.composition(currentTerm1.getName(), currentTerm2);
            }
        }
        return UnificationResult.unifiable(substitution);
    }
}
