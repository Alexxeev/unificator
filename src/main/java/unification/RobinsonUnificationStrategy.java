package unification;

import org.jetbrains.annotations.NotNull;
import syntax.Constant;
import syntax.TermWithArgs;
import syntax.Term;
import syntax.TermPair;
import syntax.Variable;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * An implementation of Robinson's unification algorithm
 */
public class RobinsonUnificationStrategy implements UnificationStrategy {
    @Override
    public @NotNull UnificationResult findUnifier(
            @NotNull final TermPair termPair) {
        Objects.requireNonNull(termPair);
        Map<Term, Term> substitutionDomain = new HashMap<>();
        Deque<Term> termStack = new ArrayDeque<>();
        termStack.push(termPair.term1());
        termStack.push(termPair.term2());
        while (!termStack.isEmpty()) {
            Term currentTerm2 = termStack.pop();
            Term currentTerm1 = termStack.pop();
            if (currentTerm1 instanceof Variable) {
                currentTerm1 = substitutionDomain
                        .getOrDefault(currentTerm1, currentTerm1);
            }
            if (currentTerm2 instanceof Variable) {
                currentTerm2 = substitutionDomain
                        .getOrDefault(currentTerm1, currentTerm2);
            }
            if (currentTerm1 instanceof Variable
                    && currentTerm1.getName().equals(currentTerm2.getName())) {
                // Do nothing
            } else if (currentTerm1 instanceof TermWithArgs currentTerm1WithArgs
                    && currentTerm2 instanceof TermWithArgs currentTerm2WithArgs) {
                String name1 = currentTerm1.getName();
                String name2 = currentTerm2.getName();
                List<Term> children1 = currentTerm1WithArgs.getChildren();
                List<Term> children2 = currentTerm2WithArgs.getChildren();
                if (!name1.equals(name2)
                        || children1.size() != children2.size()) {
                    return UnificationResult.notUnifiable();
                }
                for (int i = children1.size() - 1; i >= 0; i--) {
                    termStack.push(children1.get(i));
                    termStack.push(children2.get(i));
                }
            } else if (currentTerm1 instanceof Constant
                    && currentTerm2 instanceof Constant) {
                if (!currentTerm1.getName().equals(currentTerm2.getName())) {
                    return UnificationResult.notUnifiable();
                }
            } else if (!(currentTerm1 instanceof Variable)) {
                termStack.push(currentTerm2);
                termStack.push(currentTerm1);
            } else if (currentTerm2.contains(currentTerm1)) {
                return UnificationResult.notUnifiable();
            } else {
                composition(
                        substitutionDomain,
                        currentTerm1,
                        currentTerm2);
            }
        }
        return UnificationResult.unifiable(Substitution.of(substitutionDomain));
    }

    /**
     * Performs a composition operation on this substitution
     * with other substitution defined by provided variable-term
     * pair.
     * @param variable a variable
     * @param replacementTerm a term
     */
    private void composition(
            Map<Term, Term> substitutionDomain,
            Term variable,
            Term replacementTerm) {
        Substitution singleSubstitution = Substitution.of(variable, replacementTerm);

        for (var entry: substitutionDomain.entrySet()) {
            Term newReplacement = singleSubstitution.instantiateVariables(entry.getValue());
            substitutionDomain.put(entry.getKey(), newReplacement);
        }
        substitutionDomain.put(variable, replacementTerm);
    }
}
