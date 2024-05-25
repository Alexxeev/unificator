package syntax;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A representation of a term consisting of
 * a functional symbol of non-zero arity
 * and a list of child terms.
 */
public final class TermWithArgs extends Term {
    /**
     * A list that contains children nodes of this term
     */
    private final List<Term> children;

    /**
     * Creates a functional symbol term node with provided name.
     * @param name name of the functional symbol
     */
    TermWithArgs(final @NotNull String name) {
        this(name, new ArrayList<>());
    }

    /**
     * Constructs a new term with provided name and list of arguments
     *
     * @param name name of this term
     * @param arguments list of arguments of this term
     */
    public TermWithArgs(
            final @NotNull String name,
            final @NotNull List<Term> arguments) {
        super(name);
        arguments.forEach(arg -> arg.addParent(this));
        this.children = arguments;
    }

    @Override
    protected @NotNull StringBuilder constructTermString(@NotNull StringBuilder sb) {
        sb.append(getName());
        sb.append("(");
        int childrenCount = children.size();
        for (int i = 0; i < childrenCount; i++) {
            children.get(i).constructTermString(sb);
            if (i < childrenCount - 1) {
                sb.append(",");
            }
        }
        sb.append(")");
        return sb;
    }

    public @NotNull List<Term> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public boolean isLeafNode() {
        return getChildren().isEmpty();
    }

    @Override
    public int hashCode() {
        return 31 * getName().hashCode() + getChildren().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof TermWithArgs termWithArgs) {
            return getName().equals(termWithArgs.getName())
                    && getChildren().equals(termWithArgs.getChildren());
        }
        return false;
    }
}
