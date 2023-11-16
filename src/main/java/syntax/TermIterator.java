package syntax;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Stack;

/**
 * This class provides an ability to iterate over nodes
 * in the syntax tree.
 * This class uses pre-order depth-first traversal method to
 * iterate over nodes of the syntax tree
 */
public final class TermIterator implements Iterator<Term> {
    /**
     * Contains nodes to be traversed
     */
    private final Stack<Term> nodeStack = new Stack<>();
    /**
     * A current node
     */
    private Term currentNode;
    /**
     * A flag that determines if iterator is not yet accessed
     */
    private boolean firstTime = true;

    /**
     * Creates a new term iterator with provided root of the
     * syntax tree.
     *
     * @param root root node of the syntax tree
     */
    public TermIterator(
            final @NotNull Term root) {
        currentNode = Objects.requireNonNull(root);
    }

    @Override
    public boolean hasNext() {
        return firstTime || !currentNode.isLeafNode() || !nodeStack.isEmpty();
    }

    @Override
    public Term next() {
        if (firstTime) {
            firstTime = false;
            return currentNode;
        }
        if (currentNode instanceof FunctionalSymbolTerm functionalSymbolTermNode) {
            List<Term> children = functionalSymbolTermNode.getChildren();
            ListIterator<Term> iterator =
                    children.listIterator(children.size());
            while (iterator.hasPrevious()) {
                Term childTerm = iterator.previous();
                nodeStack.push(childTerm);
            }
        }
        if (nodeStack.isEmpty()) {
            throw new NoSuchElementException();
        }
        currentNode = nodeStack.pop();
        return currentNode;
    }
}
