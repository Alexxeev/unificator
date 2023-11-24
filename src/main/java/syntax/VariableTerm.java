package syntax;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

/**
 * A representation of a variable term
 */
public final class VariableTerm extends Term {
    /**
     * Creates a constant term node with provided name.
     * @param name name of the constant
     */
    VariableTerm(final @NotNull String name) {
        super(name);
    }

    @Override
    protected @NotNull StringBuilder constructTermString(@NotNull StringBuilder sb) {
        return sb.append(getName());
    }

    @Override
    protected @NotNull Term deepCopy(@NotNull Map<Term, Term> isomorphism) {
        Term copy = isomorphism.get(this);
        if (copy == null) {
            copy = new VariableTerm(getName());
            isomorphism.put(this, copy);
            for (Term parent : getParents()) {
                copy.addParent(parent.deepCopy(isomorphism));
            }
        }
        return copy;
    }

    @Override
    public @NotNull Set<String> getDomain() {
        return Set.of(getName());
    }

    @Override
    public boolean isLeafNode() {
        return true;
    }


    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof VariableTerm variableTerm) {
            return getName().equals(variableTerm.getName());
        }
        return false;
    }
}
