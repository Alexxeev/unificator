package syntax;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * A representation of a variable term
 */
public final class VariableTermNode extends TermNode {
    /**
     * Creates a constant term node with provided name.
     * @param name name of the constant
     */
    VariableTermNode(final @NotNull String name) {
        super(name);
    }

    @Override
    public Set<String> getDomain() {
        return Set.of(getName());
    }

    @Override
    public boolean isLeafNode() {
        return true;
    }


}
