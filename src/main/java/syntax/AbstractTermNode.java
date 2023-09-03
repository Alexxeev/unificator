package syntax;

import java.util.List;

/**
 * Base class that represents first-order term as tree-like structure.
 */
abstract class AbstractTermNode implements TermNode {
    /**
     * Name of this node
     */
    private final String name;

    /**
     * Creates a new term node with provided name
     * @param name name of the node
     */
    public AbstractTermNode(final String name) {
        this.name = name;
    }

    public final boolean isVariable() {
        return getTermType() == TermNode.Type.VARIABLE;
    }
    public final boolean isConstant() {
        return getTermType() == Type.CONSTANT;
    }

    /**
     * Recursively builds string representation of syntax tree
     * rooted at the provided node
     * @param sb a string builder
     * @param term root node of the syntax tree
     * @return a {@code StringBuilder} instance that contains
     *          a string representation of the syntax tree
     */
    private StringBuilder constructTermString(StringBuilder sb, TermNode term) {
        sb.append(term.getName());
        if (term.isLeafNode()) {
            return sb;
        }
        sb.append("(");
        int i = 0;
        List<TermNode> children = term.getChildren();
        int indexOfLastItem = children.size() - 1;
        for (TermNode child : children) {
            constructTermString(sb, child);
            if (i < indexOfLastItem) {
                sb.append(",");
            }
            i++;
        }
        sb.append(")");
        return sb;
    }

    public final String getName() {
        return name;
    }

    /**
     * Returns a string representation of the syntax tree
     * rooted at this node
     * <p>
     * String representation is of the following formats:<br>
     * c&lt;<i>index</i>&gt; - constant<br>
     * x&lt;<i>index</i>&gt; - variable<br>
     * f</i>&lt;<i>index</i>&gt;<i>(</i>
     * &lt;<i>term</i>&gt;<i>,</i>&lt;<i>term</i>&gt;<i>...</i>&lt;<i>term</i>&gt;
     * <i>)</i> - term with functional symbol of arity &gt; 0
     * <p>
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
