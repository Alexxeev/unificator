package syntax;

import org.jetbrains.annotations.NotNull;
import util.Assertions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class provides methods to create syntax
 * tree of the provided first-order term.
 * This class uses recursive descent algorithm
 * to construct syntax tree.
 */
final class TermParser {
    /**
     * Creates a new parser with provided {@code TokenIterator} instance.
     *
     * @param tokenIterator a token iterator
     */
    TermParser(final @NotNull TokenIterator tokenIterator) {
        this.tokenIterator = Objects.requireNonNull(tokenIterator);
    }

    /**
     * A token iterator
     */
    @NotNull
    private final TokenIterator tokenIterator;

    /**
     * Constructs a list of the arguments of the functional symbol term.
     *
     * @return list of term nodes
     */
    @NotNull
    private List<Term> parseArguments() {
        Assertions.require(tokenIterator.next().tokenType() == Token.Type.LEFT_PARENTHESIS,
                "expected a left parenthesis after functional symbol");
        List<Term> arguments = new ArrayList<>();
        while (tokenIterator.hasNext()) {
            arguments.add(parseTerm());
            Token.Type tokenType = tokenIterator.next().tokenType();
            if (tokenType == Token.Type.RIGHT_PARENTHESIS) {
                return arguments;
            }
            Assertions.require(tokenType == Token.Type.COMMA,
                    "expected a comma or right parenthesis after an argument");
        }
        throw new IllegalArgumentException("Unexpected EOF while reading arguments");
    }

    /**
     * Constructs a term.
     *
     * @return a root of the syntax tree
     */
    @NotNull
    public Term parseTerm() {
        if (!tokenIterator.hasNext()) {
            throw new IllegalArgumentException("Unexpected EOF while reading tokens");
        }
        Token token = tokenIterator.next();
        if (token.tokenType() == Token.Type.FUNCTIONAL_SYMBOL) {
            return new FunctionalSymbolTerm(token.toString(), parseArguments());
        }
        return Term.fromToken(token);
    }
}
