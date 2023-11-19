package syntax;

import org.jetbrains.annotations.NotNull;

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
}
