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
     * @param tokenIterator1 a token iterator of the first term.
     * @param tokenIterator2 a token iterator of the second term.
     */
    TermDagParser(
            final @NotNull TokenIterator tokenIterator1,
            final @NotNull TokenIterator tokenIterator2) {
        this.tokenIterator1 = Objects.requireNonNull(tokenIterator1);
        this.tokenIterator2 = Objects.requireNonNull(tokenIterator2);
    }

    /**
     * A token iterator of the first term
     */
    @NotNull
    private final TokenIterator tokenIterator1;

    /**
     * A token iterator of the second term
     */
    @NotNull
    private final TokenIterator tokenIterator2;

    /**
     * A set of all subterms occurred in the term.
     * <br>
     * Note: We have to use Map interface rather than Set one
     * because Set interface does not provide a method
     * to search for an element.
     */
    @NotNull
    private final Map<Term, Term> uniqueTerms = new HashMap<>();

    /**
     * Constructs a pair of terms
     *
     * @return pair of terms
     */
    public TermPair parseTermPair() {
        return new TermPair(
                parseTerm(tokenIterator1),
                parseTerm(tokenIterator2)
        );
    }

    /**
     * Constructs a term.
     *
     * @param tokenIterator an iterator over term tokens
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
     *
     * @param tokenIterator an iterator over term tokens.
     * @return list of terms.
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
