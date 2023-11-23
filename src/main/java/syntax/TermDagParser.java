package syntax;

import org.jetbrains.annotations.NotNull;
import util.Assertions;

import java.util.HashMap;
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
    TermDagParser(final @NotNull TokenIterator tokenIterator) {
        this.tokenIterator = Objects.requireNonNull(tokenIterator);
    }

    /**
     * A token iterator
     */
    @NotNull
    private final TokenIterator tokenIterator;

    /**
     * A set of all subterms occurred in the term.
     */
    @NotNull
    private final Map<Term, Term> uniqueTerms = new HashMap<>();

    /**
     * Constructs a term.
     *
     * @return a root of the syntax tree
     */
    @NotNull
    public Term parseTerm() {
        Assertions.check(tokenIterator.hasNext(),
                "Unexpected EOF while reading tokens");
        Term term = Term.fromToken(tokenIterator.next());
        if (term instanceof FunctionalSymbolTerm) {
            parseArguments(term);
        }
        if (uniqueTerms.containsKey(term)) {
            return uniqueTerms.get(term);
        }
        uniqueTerms.put(term, term);
        return term;
    }

    /**
     * Constructs a list of the arguments of the functional symbol term.
     */
    private void parseArguments(@NotNull final Term term) {
        Assertions.check(tokenIterator.next().tokenType() == Token.Type.LEFT_PARENTHESIS,
                "expected a left parenthesis after functional symbol");
        while (tokenIterator.hasNext()) {
            Term argument = parseTerm();
            argument.addParent(term);
            term.addChild(argument);
            Token.Type tokenType = tokenIterator.next().tokenType();
            if (tokenType == Token.Type.RIGHT_PARENTHESIS) {
                return;
            }
            Assertions.check(tokenType == Token.Type.COMMA,
                    "expected a comma or right parenthesis after an argument");
        }
        throw new IllegalArgumentException("Unexpected EOF while reading arguments");
    }
}
