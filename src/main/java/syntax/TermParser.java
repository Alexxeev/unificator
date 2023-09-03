package syntax;

import org.jetbrains.annotations.NotNull;

import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class provides methods to create syntax tree of first-order term.
 * This class uses recursive descent algorithm to construct syntax tree.
 */
final class TermParser {
    /**
     * Creates a new parser with provided {@code TokenIterator} instance.
     *
     * @param tokenIterator a token iterator
     */
    TermParser(final @NotNull TokenIterator tokenIterator) {
        this.tokenIterator = tokenIterator;
    }

    /**
     * A token iterator
     */
    private final TokenIterator tokenIterator;

    /**
     * Constructs a list of the arguments of the functional symbol term.
     *
     * @return list of term nodes
     */
    private List<TermNode> parseArguments() {
        if (tokenIterator.next().tokenType() != Token.Type.LEFT_PARENTHESIS) {
            throw new IllegalArgumentException(
                    "expected a left parenthesis after functional symbol");
        }
        List<TermNode> arguments = new ArrayList<>();
        while (tokenIterator.hasNext()) {
            arguments.add(parseTerm());
            Token.Type tokenType = tokenIterator.next().tokenType();
            if (tokenType == Token.Type.RIGHT_PARENTHESIS) {
                return arguments;
            }
            if (tokenType != Token.Type.COMMA) {
                throw new IllegalArgumentException(
                        "expected a comma or right parenthesis after an argument");
            }
        }
        throw new IllegalArgumentException("Unexpected EOF while reading arguments");
    }

    /**
     * Constructs a term.
     *
     * @return a root of the syntax tree
     */
    public TermNode parseTerm() {
        if (!tokenIterator.hasNext()) {
            throw new IllegalArgumentException("Unexpected EOF while reading tokens");
        }
        TermNode term = TermNode.fromToken(tokenIterator.next());
        if (term.getTermType() == TermNode.Type.FUNCTIONAL_SYMBOL) {
            term.addChildren(parseArguments());
        }
        return term;
    }
}
