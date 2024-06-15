package syntax;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A representation of a constant term
 */
public final class Constant extends Term {
    /**
     * Creates a constant term node with provided name.
     * @param name name of the constant
     */
    Constant(final @NotNull String name) {
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
        if (obj instanceof Constant constant) {
            return Objects.equals(this.getName(), constant.getName());
        }
        return false;
    }
}
