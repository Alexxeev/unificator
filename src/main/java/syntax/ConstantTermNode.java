package syntax;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * A representation of a constant term
 */
public final class ConstantTermNode extends TermNode {
    /**
     * Creates a constant term node with provided name.
     * @param name name of the constant
     */
    public ConstantTermNode(final @NotNull String name) {
        super(name);
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
