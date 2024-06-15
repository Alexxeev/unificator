package syntax;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Queue;

/**
 * This class provides an ability to iterate over nodes
 * in the syntax tree.
 * This class uses a variant of pre-order breadth-first traversal method
 * that traverses nodes in a way that functional symbols and constants
 * are traversed first.
 */
public class FunctionalSymbolFirstTermIterator implements Iterator<Term> {
    /**
     * Contains nodes to be traversed
     */
    private final Queue<Term> nodeQueue = new ArrayDeque<>();

    private final Queue<Term> varQueue = new ArrayDeque<>();

    /**
     * Creates a new term iterator with provided root of the
     * syntax tree.
     *
     * @param root root node of the syntax tree
     */
    public FunctionalSymbolFirstTermIterator(
            final @NotNull Term root) {
        nodeQueue.add(Objects.requireNonNull(root));
    }

    public FunctionalSymbolFirstTermIterator(
            final @NotNull TermPair termPair) {
        nodeQueue.add(Objects.requireNonNull(termPair.term1()));
        nodeQueue.add(Objects.requireNonNull(termPair.term2()));
    }

    @Override
    public boolean hasNext() {
        return !nodeQueue.isEmpty() || !varQueue.isEmpty();
    }

    private Term nextInternal() {
        Term currentTerm = nodeQueue.remove();
        if (currentTerm instanceof TermWithArgs currentTermWithArgs) {
            nodeQueue.addAll(currentTermWithArgs.getArgs());
        }
        return currentTerm;
    }

    @Override
    public Term next() {
        if (nodeQueue.isEmpty()) {
            if (varQueue.isEmpty())
                throw new NoSuchElementException();
            return varQueue.remove();
        }
        Term currentTerm = nextInternal();
        while (currentTerm instanceof Variable) {
            varQueue.add(currentTerm);
            if (nodeQueue.isEmpty())
                return varQueue.remove();
            currentTerm = nextInternal();
        }
        return currentTerm;
    }
}
