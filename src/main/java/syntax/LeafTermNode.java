package syntax;

import java.util.List;

/**
 * Base class for constants and variables
 */
abstract class LeafTermNode extends AbstractTermNode implements TermNode {
    /**
     * Creates a leaf term node with provided name.
     * @param name name of the leaf term node
     */
    public LeafTermNode(final String name) {
        super(name);
    }

    @Override
    public final void addChildren(List<TermNode> nodes) {
        throw new UnsupportedOperationException(
                "cannot add children to the leaf node");
    }

    @Override
    public final List<TermNode> getChildren() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void prependChild(final TermNode term) {
        throw new UnsupportedOperationException(
                "cannot add children to the leaf node");
    }

    @Override
    public final void setChild(final int index, final TermNode term) {
        throw new UnsupportedOperationException(
                "leaf node does not have children");
    }

    @Override
    public final boolean isLeafNode() {
        return true;
    }

}
