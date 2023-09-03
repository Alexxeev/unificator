package syntax;

import java.util.Set;

/**
 * A representation of a variable term
 */
final class VariableTermNode extends LeafTermNode implements TermNode {
    /**
     * Creates a constant term node with provided name.
     * @param name name of the constant
     */
    public VariableTermNode(final String name) {
        super(name);
    }

    @Override
    public Set<String> getDomain() {
        return Set.of(getName());
    }

    @Override
    public TermNode.Type getTermType() {
        return TermNode.Type.VARIABLE;
    }
}
