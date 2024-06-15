package syntax;

import org.jetbrains.annotations.NotNull;

import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * A node of first-order term syntax tree.
 * It is identified by its name and type.
 */
public abstract class Term implements Iterable<Term> {
    /**
     * Creates a new node with provided token.
     *
     * @param token A token
     * @return An instance of {@code TermNode}
     * @throws NullPointerException if {@code token} is null
     * @throws IllegalArgumentException if {@code token} is
     * an instance of punctuation token
     */
    @NotNull
    static Term fromToken(@NotNull final Token token) {
        Objects.requireNonNull(token);
        switch (token.tokenType()) {
            case CONSTANT -> {
                return new Constant(token.toString());
            }
            case VARIABLE -> {
                return new Variable(token.toString());
            }
            case FUNCTIONAL_SYMBOL -> {
                return new TermWithArgs(token.toString());
            }
            default -> throw new IllegalArgumentException("illegal token type");
        }
    }

    /**
     * Creates a syntax tree of first-order term from string input.
     * <p>
     * Valid terms may have one of the following forms:<br>
     * c&lt;<i>index</i>&gt; - constant<br>
     * x&lt;<i>index</i>&gt; - variable<br>
     * f&lt;<i>index</i>&gt;<i>(</i>
     * &lt;<i>term</i>&gt;<i>,</i>&lt;<i>term</i>&gt;<i>...</i>&lt;<i>term</i>&gt;
     * <i>)</i> - term with functional symbol of arity &gt; 0
     * <p>
     * Parser uses recursive descent algorithm to process tokens.
     *
     * @param termString sequence of tokens representing first-order term
     * @return syntax tree of provided first-order term
     * @throws IllegalArgumentException if term is of invalid form
     * @throws NullPointerException if {@code termString} is {@code null}
     */
    @NotNull
    public static Term fromString(@NotNull final String termString) {
        TokenIterator iterator = new TokenIterator(new StringCharacterIterator(
                Objects.requireNonNull(termString)
        ));
        return new TermParser(iterator).parseTerm();
    }

    /**
     * Name of this node
     */
    @NotNull
    private final String name;

    /**
     * Parent nodes
     */
    @NotNull
    private final List<Term> parents = new ArrayList<>();

    /**
     * Creates a new term node with provided name
     * @param name name of the node
     */
    protected Term(@NotNull final String name) {
        this.name = Objects.requireNonNull(name);
    }

    /**
     * Inserts the specified term node as a parent node
     * in the end of the parent node list.
     *
     * @param parent node to be inserted.
     */
    protected void addParent(@NotNull final Term parent) {
        parents.add(Objects.requireNonNull(parent));
    }

    /**
     * Returns a list that contains parent nodes of this node.
     * Note that returned list is immutable.
     *
     * @return immutable list of children term nodes
     */
    @NotNull
    public List<Term> getParents() {
        return Collections.unmodifiableList(parents);
    }


    /**
     * Recursively builds string representation of syntax tree
     * rooted at the provided node
     * @param sb a string builder
     * @return a {@code StringBuilder} instance that contains
     *          a string representation of the syntax tree
     */
    @NotNull
    protected abstract StringBuilder constructTermString(@NotNull final StringBuilder sb);

    /**
     * Returns true if the specified term is subterm of this term
     *
     * @param other term to be checked
     * @return true if the specified term is subterm of this term
     */
    public boolean contains(@NotNull final Term other) {
        Objects.requireNonNull(other);
        for (Term term : this) {
            if (term == other)
                return true;
        }
        return false;
    }

    /**
     * Returns the name of this term node. Name has the following format:
     * <br>
     * &lt;prefixCharacter&gt;&lt;index&gt;
     *
     * @return string representation of node name
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Returns true if this node does not have children nodes
     * (this node is leaf node).
     *
     * @return true if this node is leaf node
     */
    public abstract boolean isLeafNode();

    /**
     * Returns a string representation of the syntax tree
     * rooted at this node
     * <br>
     * String representation is of the following formats:<br>
     * c&lt;<i>index</i>&gt; - constant<br>
     * x&lt;<i>index</i>&gt; - variable<br>
     * <i>f</i>&lt;<i>index</i>&gt;<i>(</i>
     * &lt;<i>term</i>&gt;<i>,</i>&lt;<i>term</i>&gt;<i>...</i>&lt;<i>term</i>&gt;
     * <i>)</i> - term with functional symbol of arity &gt; 0
     * <br>
     *
     * @return a string representation of the syntax tree
     *      rooted at this node
     */
    @NotNull
    @Override
    public String toString() {
        return constructTermString(
                new StringBuilder()).toString();
    }

    /**
     * Returns an iterator over the nodes of this term.
     * Elements are traversed using depth-first traversal
     * method.
     *
     * @return iterator over the nodes of this term.
     */
    @NotNull
    @Override
    public Iterator<Term> iterator() {
        return new PreOrderTermIterator(this);
    }

    /**
     * Compares the names of this term and provided term.
     *
     * @param other other term
     * @return true if {@code other} is not null and its name
     *         equals to the name of this term.
     */
    public boolean nameEquals(final Term other) {
        return other != null && getName().equals(other.getName());
    }
}
