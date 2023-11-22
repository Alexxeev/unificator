package syntax;

import org.jetbrains.annotations.NotNull;

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
    protected StringBuilder constructTermString(StringBuilder sb) {
        return sb.append(getName());
    }

    @Override
    public Term deepCopy() {
        return new ConstantTerm(this.getName());
    }

    @Override
    public Set<String> getDomain() {
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
