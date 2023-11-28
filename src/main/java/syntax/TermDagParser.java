package syntax;

import org.jetbrains.annotations.NotNull;
import util.Assertions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class provides methods to create
 * directed acyclic graph representation
 * of the provided first-order term.
 * This class uses recursive descent algorithm
 * to construct syntax tree.
 */
final class TermDagParser {
    /**
     * Creates a new parser with provided {@code TokenIterator} instance.
     *
     * @param tokenIterator a token iterator
     */
    TermDagParser(
            final @NotNull TokenIterator tokenIterator,
            final @NotNull TokenIterator tokenIterator2) {
        this.tokenIterator1 = Objects.requireNonNull(tokenIterator);
        this.tokenIterator2 = Objects.requireNonNull(tokenIterator2);
    }

    /**
     * A token iterator
     */
    @NotNull
    private final TokenIterator tokenIterator1;

    @NotNull
    private final TokenIterator tokenIterator2;

    /**
     * A set of all subterms occurred in the term.
     */
    @NotNull
    private final Map<Term, Term> uniqueTerms = new HashMap<>();

    public TermPair parseTermPair() {
        return new TermPair(
                parseTerm(tokenIterator1),
                parseTerm(tokenIterator2)
        );
    }

    /**
     * Constructs a term.
     *
     * @return a root of the syntax tree
     */
    private Term parseTerm(TokenIterator tokenIterator) {
        Assertions.require(tokenIterator.hasNext(),
                "Unexpected EOF while reading tokens");
        Token token = tokenIterator.next();
        Term term;
        if (token.tokenType() == Token.Type.FUNCTIONAL_SYMBOL) {
            term = new FunctionalSymbolTerm(token.toString(), parseArgs(tokenIterator));
        } else {
            term = Term.fromToken(token);
        }
        return uniqueTerms.computeIfAbsent(term, t -> t);
    }

    /**
     * Constructs a list of the arguments of the functional symbol term.
     */
    private List<Term> parseArgs(TokenIterator tokenIterator) {
        Assertions.require(tokenIterator.next().tokenType() == Token.Type.LEFT_PARENTHESIS,
                "expected a left parenthesis after functional symbol");
        List<Term> args = new ArrayList<>();
        while (tokenIterator.hasNext()) {
            Term argument = parseTerm(tokenIterator);
            args.add(argument);
            Token.Type tokenType = tokenIterator.next().tokenType();
            if (tokenType == Token.Type.RIGHT_PARENTHESIS) {
                return args;
            }
            Assertions.require(tokenType == Token.Type.COMMA,
                    "expected a comma or right parenthesis after an argument");
        }
        throw new IllegalArgumentException("Unexpected EOF while reading arguments");
    }
}
