package syntax;

import org.jetbrains.annotations.NotNull;
import util.Assertions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    private final TokenIterator tokenIterator;

    private final Map<Term, Term> uniqueTerms = new HashMap<>();

    public Term parseTerm() {
        Assertions.check(tokenIterator.hasNext(),
                "Unexpected EOF while reading tokens");
        Term term = Term.fromToken(tokenIterator.next());
        if (term instanceof FunctionalSymbolTerm functionalSymbolTerm) {
            parseArguments(functionalSymbolTerm);
        }
        if (uniqueTerms.containsKey(term)) {
            return uniqueTerms.get(term);
        }
        uniqueTerms.put(term, term);
        return term;
    }

    private void parseArguments(FunctionalSymbolTerm term) {
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
