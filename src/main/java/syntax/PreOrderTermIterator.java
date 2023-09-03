package syntax;

import java.util.Iterator;
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
public final class PreOrderTermIterator implements Iterator<TermNode> {
    /**
     * Contains nodes to be traversed
     */
    private final Stack<TermNode> nodeStack = new Stack<>();
    /**
     * A current node
     */
    private TermNode currentNode;
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
    public PreOrderTermIterator(
            final TermNode root) {
        currentNode = Objects.requireNonNull(root);
    }

    @Override
    public boolean hasNext() {
        return firstTime || !currentNode.isLeafNode() || nodeStack.size() > 0;
    }

    @Override
    public TermNode next() {
        if (firstTime) {
            firstTime = false;
            return currentNode;
        }
        if (!currentNode.isLeafNode()) {
            ListIterator<TermNode> iterator =
                    currentNode.getChildren().listIterator(currentNode.getChildren().size());
            while (iterator.hasPrevious()) {
                TermNode childTerm = iterator.previous();
                nodeStack.push(childTerm);
            }
        }
        if (nodeStack.size() == 0) {
            throw new NoSuchElementException();
        }
        currentNode = nodeStack.pop();
        return currentNode;
    }
}
