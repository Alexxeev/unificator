package unification;

import org.jetbrains.annotations.NotNull;
import syntax.ConstantTermNode;
import syntax.FunctionalSymbolTermNode;
import syntax.TermNode;
import syntax.VariableTermNode;

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
        Map<String, TermNode> domain
) implements Substitution {

    @Override
    public TermNode instantiateVariables(@NotNull final TermNode term) {
        Objects.requireNonNull(term);
        if (term instanceof ConstantTermNode) {
            return term;
        }
        if (term instanceof VariableTermNode) {
            if (!domain.containsKey(term.getName())) {
                return term;
            }
            return domain.get(term.getName());
        }
        if (term instanceof FunctionalSymbolTermNode functionalSymbolTerm) {
            List<TermNode> children = functionalSymbolTerm.getChildren();
            for (int i = 0; i < children.size(); i++) {
                functionalSymbolTerm.setChild(i, instantiateVariables(children.get(i)));
            }
        }
//        Iterator<TermNode> termIterator =
//                new PreOrderTermIterator(Objects.requireNonNull(term), true);
//        termIterator.forEachRemaining(currentTerm -> {
//            if (currentTerm.isLeafNode()) {
//                return;
//            }
//            List<TermNode> children = currentTerm.getChildren();
//            for (int i = 0, size = children.size(); i < size; i++) {
//                String childName = children.get(i).getName();
//                if (!domain.containsKey(childName)) {
//                    continue;
//                }
//                TermNode replacementTerm = domain.get(childName);
//                currentTerm.setChild(i, replacementTerm);
//            }
//        });
        return term;
    }

    @Override
    public Substitution composition(@NotNull final Substitution other) {
        Objects.requireNonNull(other);
        //create shallow copy of map to prevent
        // destruction of original substitution
        Map<String, TermNode> newDomain = new HashMap<>(domain);
        for (Map.Entry<String, TermNode> entry : newDomain.entrySet()) {
            TermNode modifiedTerm = other.instantiateVariables(entry.getValue());
            if (modifiedTerm.getName().equals(entry.getKey())) {
                newDomain.remove(entry.getKey());
            } else {
                newDomain.replace(entry.getKey(), modifiedTerm);
            }
        }
        for (Map.Entry<String, TermNode> entry : other.domain().entrySet()) {
            if (!domain.containsKey(entry.getKey())) {
                newDomain.put(entry.getKey(), entry.getValue());
            }
        }
        return new ListSubstitution(newDomain);
    }

    @Override
    public Substitution composition(final @NotNull String variable, final @NotNull TermNode replacementTerm) {
        Objects.requireNonNull(variable);
        Objects.requireNonNull(replacementTerm);

        Substitution other = Substitution.of(variable, replacementTerm);
        return composition(other);
    }
}
