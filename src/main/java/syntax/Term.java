package syntax;

import org.jetbrains.annotations.NotNull;

import java.text.StringCharacterIterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A node of first-order term syntax tree.
 * It is identified by its name and type.
 */
public abstract class Term {
    /**
     * Creates a new node with provided token.
     *
     * @param token A token
     * @return An instance of {@code TermNode}
     * @throws NullPointerException if {@code token} is null
     * @throws IllegalArgumentException if {@code token} is
     * an instance of punctuation token
     */
    static Term fromToken(@NotNull final Token token) {
        Objects.requireNonNull(token);
        switch (token.tokenType()) {
            case CONSTANT -> {
                return new ConstantTerm(token.toString());
            }
            case VARIABLE -> {
                return new VariableTerm(token.toString());
            }
            case FUNCTIONAL_SYMBOL -> {
                return new FunctionalSymbolTerm(token.toString());
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
    public static Term fromString(@NotNull final String termString) {
        TokenIterator iterator = new TokenIterator(new StringCharacterIterator(
                Objects.requireNonNull(termString)
        ));
        return new TermParser(iterator).parseTerm();
    }

    /**
     * Returns a leaf term node with empty name field.
     * @return instance of empty token.
     */
    public static Term empty() {
        return new ConstantTerm("");
    }

    /**
     * Name of this node
     */
    private final String name;

    /**
     * Creates a new term node with provided name
     * @param name name of the node
     */
    protected Term(@NotNull final String name) {
        this.name = Objects.requireNonNull(name);
    }

    /**
     * Recursively builds string representation of syntax tree
     * rooted at the provided node
     * @param sb a string builder
     * @param term root node of the syntax tree
     * @return a {@code StringBuilder} instance that contains
     *          a string representation of the syntax tree
     */
    private StringBuilder constructTermString(StringBuilder sb, Term term) {
        sb.append(term.getName());
        if (!(term instanceof FunctionalSymbolTerm)) {
            return sb;
        }
        sb.append("(");
        int i = 0;
        List<Term> children = ((FunctionalSymbolTerm)term).getChildren();
        int indexOfLastItem = children.size() - 1;
        for (Term child : children) {
            constructTermString(sb, child);
            if (i < indexOfLastItem) {
                sb.append(",");
            }
            i++;
        }
        sb.append(")");
        return sb;
    }

    /**
     * Returns a deep copy of the tree.
     *
     * @return deep copy of the tree.
     */
    public abstract Term deepCopy();

    /**
     * Returns a set of variables that are present in this term.
     * Every variable in the domain is represented as string.
     * Returned set is empty If term contains no variables.
     * Note that returned set is immutable.
     *
     * @return immutable set of variables
     */
    public abstract Set<String> getDomain();

    /**
     * Returns the name of this term node. Name has the following format:
     * <br>
     * &lt;prefixCharacter&gt;&lt;indexDigits&gt;
     *
     * @return string representation of node name
     */
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
    @Override
    public String toString() {
        return constructTermString(
                new StringBuilder(), this).toString();
    }
}
