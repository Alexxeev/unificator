package syntax;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * This class provides an ability to iterate over nodes
 * in the syntax tree.
 * This class uses pre-order depth-first traversal method to
 * iterate over nodes of the syntax tree
 */
final class PreOrderTermIterator implements Iterator<Term> {
    /**
     * Contains nodes to be traversed
     */
    private final Deque<Term> nodeStack = new ArrayDeque<>();
    /**
     * A current term
     */
    private Term currentTerm;
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
            final @NotNull Term root) {
        currentTerm = Objects.requireNonNull(root);
    }

    @Override
    public boolean hasNext() {
        return firstTime || !currentTerm.isLeafNode() || !nodeStack.isEmpty();
    }

    @Override
    public Term next() {
        if (firstTime) {
            firstTime = false;
            return currentTerm;
        }
        if (currentTerm instanceof FunctionalSymbolTerm) {
            List<Term> children = currentTerm.getChildren();
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
        currentTerm = nodeStack.pop();
        return currentTerm;
    }
}
