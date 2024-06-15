package syntax;

import org.jetbrains.annotations.NotNull;

/**
 * A representation of a variable term
 */
public final class Variable extends Term {
    /**
     * Creates a constant term node with provided name.
     * @param name name of the constant
     */
    Variable(final @NotNull String name) {
        super(name);
    }

    @Override
    protected @NotNull StringBuilder constructTermString(@NotNull StringBuilder sb) {
        return sb.append(getName());
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
        if (obj instanceof Variable variable) {
            return getName().equals(variable.getName());
        }
        return false;
    }
}
