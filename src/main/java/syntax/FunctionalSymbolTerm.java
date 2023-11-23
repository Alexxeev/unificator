package syntax;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A representation of a term consisting of
 * a functional symbol of non-zero arity
 * and a list of child terms.
 */
public final class FunctionalSymbolTerm extends Term {
    /**
     * A list that contains children nodes of this term
     */
    private final List<Term> children = new ArrayList<>();

    /**
     * Creates a functional symbol term node with provided name.
     * @param name name of the functional symbol
     */
    FunctionalSymbolTerm(final @NotNull String name) {
        super(name);
    }

    @Override
    protected StringBuilder constructTermString(StringBuilder sb) {
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

    @Override
    protected Term deepCopy(Map<Term, Term> isomorphism) {
        Term copy = isomorphism.get(this);
        if (copy == null) {
            copy = new FunctionalSymbolTerm(
                    this.getName());
            isomorphism.put(this, copy);
            for (Term child : this.getChildren()) {
                copy.addChild(child.deepCopy(isomorphism));
            }
            for (Term parent : this.getParents()) {
                copy.addParent(parent.deepCopy(isomorphism));
            }
        }
        return copy;
    }

    @Override
    public Set<String> getDomain() {
        Iterator<Term> termNodeIterator = new TermIterator(
                this
        );
        Set<String> domain = new HashSet<>();
        while (termNodeIterator.hasNext()) {
            Term term = termNodeIterator.next();
            if (term instanceof VariableTerm) {
                domain.add(term.getName());
            }
        }
        return domain;
    }

    @Override
    public void addChild(final @NotNull Term term) {
        children.add(term);
    }

    @Override
    public List<Term> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public void setChild(final int index, final @NotNull Term term) {
        children.set(index, Objects.requireNonNull(term));
    }

    @Override
    public void replaceChild(@NotNull final Term from, @NotNull final Term to) {
        Collections.replaceAll(children, from, to);
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
        if (obj instanceof FunctionalSymbolTerm functionalSymbolTerm) {
            return getName().equals(functionalSymbolTerm.getName())
                    && getChildren().equals(functionalSymbolTerm.getChildren());
        }
        return false;
    }
}
