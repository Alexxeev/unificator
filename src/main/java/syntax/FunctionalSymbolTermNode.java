package syntax;

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
final class FunctionalSymbolTermNode extends AbstractTermNode implements TermNode {
    private final List<TermNode> children = new ArrayList<>();

    /**
     * Creates a functional symbol term node with provided name.
     * @param name name of the functional symbol
     */
    public FunctionalSymbolTermNode(final String name) {
        super(name);
    }

    @Override
    public Set<String> getDomain() {
        Iterator<TermNode> termNodeIterator = new PreOrderTermIterator(
                this
        );
        Set<String> domain = new HashSet<>();
        while (termNodeIterator.hasNext()) {
            TermNode termNode = termNodeIterator.next();
            if (termNode.getTermType() == TermNode.Type.VARIABLE) {
                domain.add(termNode.getName());
            }
        }
        return domain;
    }

    @Override
    public TermNode.Type getTermType() {
        return TermNode.Type.FUNCTIONAL_SYMBOL;
    }

    @Override
    public void addChildren(List<TermNode> nodes) {
        children.addAll(nodes);
    }

    @Override
    public List<TermNode> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public void prependChild(final TermNode term) {
        children.add(0, Objects.requireNonNull(term));
    }

    @Override
    public void setChild(final int index, final TermNode term) {
        children.set(index, Objects.requireNonNull(term));
    }

    @Override
    public boolean isLeafNode() {
        return false;
    }

}
