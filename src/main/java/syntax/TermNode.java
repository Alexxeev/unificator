package syntax;

import org.jetbrains.annotations.NotNull;

import java.text.StringCharacterIterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A node of first-order term syntax tree.
 * It is identified by its name and type.
 * <p>
 * Internal nodes of the tree can have arbitrary number of
 * children which are also instances of {@code TermNode}.
 * <p>
 * Users of this interface can insert children nodes to
 * internal nodes of the tree and replace children nodes
 */
public interface TermNode {
    /**
     * Creates a new node with provided token.
     *
     * @param token A token
     * @return An instance of {@code TermNode}
     * @throws NullPointerException if {@code token} is null
     * @throws IllegalArgumentException if {@code token} is
     * an instance of punctuation token
     */
    static TermNode fromToken(@NotNull final Token token) {
        Objects.requireNonNull(token);
        switch (token.tokenType()) {
            case CONSTANT -> {
                return new ConstantTermNode(token.toString());
            }
            case VARIABLE -> {
                return new VariableTermNode(token.toString());
            }
            case FUNCTIONAL_SYMBOL -> {
                return new FunctionalSymbolTermNode(token.toString());
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
    static TermNode fromString(@NotNull final String termString) {
        TokenIterator iterator = new TokenIterator(new StringCharacterIterator(
                Objects.requireNonNull(termString)
        ));
        return new TermParser(iterator).parseTerm();
    }

    /**
     * Returns a leaf term node with empty name field.
     * @return instance of empty token.
     */
    static TermNode empty() {
        return new ConstantTermNode("");
    }

    /**
     * Adds a list of terms to the children list of this node
     * to the end of the children list (optional operation).
     *
     * @param nodes list of nodes
     * @throws UnsupportedOperationException if node is leaf
     */
    void addChildren(List<TermNode> nodes);

    /**
     * Returns a list that contains children nodes of this node
     * (optional operation).
     * Note that returned list is immutable.
     *
     * @return immutable list of children term nodes
     * @throws UnsupportedOperationException if node is leaf
     */
    List<TermNode> getChildren();

    /**
     * Returns a set of variables that are present in this term.
     * Every variable in the domain is represented as string.
     * Returned set is empty If term contains no variables.
     * Note that returned set is immutable.
     *
     * @return immutable set of variables
     */
    Set<String> getDomain();

    /**
     * Returns the name of this term node. Name has the following format:
     * <p>
     * <p>
     * &lt;prefixCharacter&gt;&lt;indexDigits&gt;
     *
     * @return string representation of node name
     */
    String getName();

    /**
     * Returns the type that node represents.
     *
     * @return the type that node represents.
     */
    Type getTermType();

    /**
     * Inserts the specified term node as a child node
     * in the beginning of the child node list (optional operation).
     *
     * @param term node to be inserted.
     * @throws UnsupportedOperationException if this term node is leaf node.
     */
    void prependChild(TermNode term);

    /**
     * Replaces the element at the specified position in the child node list
     * with the specified element (optional operation).
     *
     * @param index index of child node to replace
     * @param term child node to be stored at the specified position
     * @throws UnsupportedOperationException if this term node is leaf node.
     * @throws IndexOutOfBoundsException if the index is out of range
     *         ({@code index < 0 || index > size()})
     */
    void setChild(int index, TermNode term);

    /**
     * Returns true if this node does not have children nodes
     * (this node is leaf node).
     *
     * @return true if this node is leaf node
     */
    boolean isLeafNode();

    /**
     * Returns true if this node represents a variable.
     *
     * @return true if this node represents a variable
     */
    boolean isVariable();

    /**
     * Returns true if this node represents a constant.
     *
     * @return true if this node represents a constant
     */
    boolean isConstant();

    /**
     * Type of term node
     */
    enum Type {
        /**
         * A constant term
         */
        CONSTANT,
        /**
         * A variable term
         */
        VARIABLE,
        /**
         * A functional symbol
         */
        FUNCTIONAL_SYMBOL,
    }
}
