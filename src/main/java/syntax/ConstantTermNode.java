package syntax;

import java.util.Set;

/**
 * A representation of a constant term
 */
final class ConstantTermNode extends LeafTermNode implements TermNode {
    /**
     * Creates a constant term node with provided name.
     * @param name name of the constant
     */
    public ConstantTermNode(final String name) {
        super(name);
    }

    @Override
    public Set<String> getDomain() {
        return Set.of();
    }

    @Override
    public TermNode.Type getTermType() {
        return TermNode.Type.CONSTANT;
    }
}
