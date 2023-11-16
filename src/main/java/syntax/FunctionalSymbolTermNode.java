package syntax;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A representation of a term consisting of
 * a functional symbol of non-zero arity
 * and a list of child terms.
 */
public final class FunctionalSymbolTermNode extends TermNode {
    private final List<TermNode> children = new ArrayList<>();

    /**
     * Creates a functional symbol term node with provided name.
     * @param name name of the functional symbol
     */
    FunctionalSymbolTermNode(final @NotNull String name) {
        super(name);
    }

    @Override
    public TermNode deepCopy() {
        final FunctionalSymbolTermNode copy = new FunctionalSymbolTermNode(
                this.getName());
        for (TermNode child : this.getChildren()) {
            copy.addChild(child.deepCopy());
        }
        return copy;
    }

    @Override
    public Set<String> getDomain() {
        Iterator<TermNode> termNodeIterator = new TermIterator(
                this
        );
        Set<String> domain = new HashSet<>();
        while (termNodeIterator.hasNext()) {
            TermNode termNode = termNodeIterator.next();
            if (termNode instanceof VariableTermNode) {
                domain.add(termNode.getName());
            }
        }
        return domain;
    }

    public void addChild(final @NotNull TermNode term) {
        children.add(term);
    }

    /**
     * Adds a list of terms to the children list of this node
     * to the end of the children list.
     *
     * @param nodes list of nodes
     */
    public void addChildren(@NotNull List<TermNode> nodes) {
        children.addAll(Objects.requireNonNull(nodes));
    }

    /**
     * Returns a list that contains children nodes of this node.
     * Note that returned list is immutable.
     *
     * @return immutable list of children term nodes
     */
    public List<TermNode> getChildren() {
        return Collections.unmodifiableList(children);
    }

    /**
     * Inserts the specified term node as a child node
     * in the beginning of the child node list.
     *
     * @param term node to be inserted.
     */
    public void prependChild(final @NotNull TermNode term) {
        children.add(0, Objects.requireNonNull(term));
    }

    /**
     * Replaces the element at the specified position in the child node list
     * with the specified element.
     *
     * @param index index of child node to replace
     * @param term child node to be stored at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     *         ({@code index < 0 || index > size()})
     */
    public void setChild(final int index, final @NotNull TermNode term) {
        children.set(index, Objects.requireNonNull(term));
    }

    @Override
    public boolean isLeafNode() {
        return getChildren().isEmpty();
    }

}
