package syntax;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A representation of a constant term
 */
public final class ConstantTerm extends Term {
    /**
     * Creates a constant term node with provided name.
     * @param name name of the constant
     */
    ConstantTerm(final @NotNull String name) {
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
            copy = new ConstantTerm(getName());
            isomorphism.put(this, copy);
        }
        return copy;
    }

    @Override
    public @NotNull Set<String> getDomain() {
        return Set.of();
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
        if (obj instanceof ConstantTerm constantTerm) {
            return Objects.equals(this.getName(), constantTerm.getName());
        }
        return false;
    }
}
