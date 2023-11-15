package syntax;

import org.jetbrains.annotations.NotNull;

import java.text.CharacterIterator;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * This iterator allows to tokenize provided
 * character sequence and traverse through tokens.
 */
class TokenIterator implements Iterator<Token> {
    /**
     * A variable prefix
     */
    private static final char VARIABLE_SYMBOL = 'x';
    /**
     * A constant prefix
     */
    private static final char CONSTANT_SYMBOL = 'c';
    /**
     * A functional symbol prefix
     */
    private static final char FUNCTION_SYMBOL = 'f';
    /**
     * A symbol that represents left parenthesis
     */
    private static final char LEFT_PARENTHESIS = '(';
    /**
     * A symbol that represents right parenthesis
     */
    private static final char RIGHT_PARENTHESIS = ')';
    /**
     * A symbol that represents comma
     */
    private static final char COMMA = ',';
    /**
     * Maps symbols to the corresponding token types
     */
    private static final Map<Character, Token.Type> TOKEN_TYPE =
            Map.of(
                    LEFT_PARENTHESIS,  Token.Type.LEFT_PARENTHESIS,
                    RIGHT_PARENTHESIS, Token.Type.RIGHT_PARENTHESIS,
                    COMMA,             Token.Type.COMMA,
                    VARIABLE_SYMBOL,   Token.Type.VARIABLE,
                    CONSTANT_SYMBOL,   Token.Type.CONSTANT,
                    FUNCTION_SYMBOL,   Token.Type.FUNCTIONAL_SYMBOL
            );

    /**
     * True if this iterator is accessed for the first time.
     */
    private boolean firstTime = true;

    /**
     * An iterator over characters of the input term
     */
    private final CharacterIterator characterIterator;

    /**
     * Creates a new token iterator with provided character
     * iterator of the input formula
     *
     * @param characterIterator character iterator of the input
     *                          formula
     */
    public TokenIterator(final @NotNull CharacterIterator characterIterator) {
        this.characterIterator = Objects.requireNonNull(characterIterator);
    }

    /**
     * Returns true if argument is constant, variable of functional symbol
     * prefix
     *
     * @param character a character
     * @return true if argument is constant, variable of functional symbol
     *         prefix
     */
    private boolean isNamedTokenPrefix(final char character) {
        return character == CONSTANT_SYMBOL || character == VARIABLE_SYMBOL || character == FUNCTION_SYMBOL;
    }

    /**
     * Returns true if argument is punctuation symbol (left parenthesis,
     * right parenthesis or comma)
     *
     * @param character a character to be tested
     * @return true if argument is punctuation symbol
     */
    private boolean isPunctuationToken(final char character) {
        return character == LEFT_PARENTHESIS
                || character == RIGHT_PARENTHESIS || character == COMMA;
    }

    /**
     * Returns true if argument is letter or digit
     *
     * @param character a character to be tested
     * @return true if argument is digit
     */
    private boolean isLetterOrDigit(final char character) {
        return Character.isLetterOrDigit(character);
    }

    /**
     * Returns true if argument is Unicode space character
     *
     * @param character a character to be tested
     * @return true if argument is digit
     */
    private boolean isSpace(final char character) {
        return Character.isSpaceChar(character);
    }

    /**
     * Returns the character at the next position of the
     * {@code characterIterator}
     *
     * @return the character at the next position of the
     * {@code characterIterator}
     */
    private char readSymbol() {
        if (firstTime) {
            firstTime = false;
            return characterIterator.first();
        }
        return characterIterator.next();
    }

    /**
     * Reads the characters until the first occurrence of
     * non-whitespace character
     *
     * @return the first occurrence of non-whitespace
     * character
     */
    private char readSymbolUntilNonWhitespace() {
        do {
            readSymbol();
        } while (isSpace(characterIterator.current()));
        return characterIterator.current();
    }

    /**
     * Decreases the current index of {@code characterIterator}
     * if it has not already reached the last character
     * <p>
     * Note: this method does nothing if {@code characterIterator}
     * reached the last symbol to prevent endless iteration over
     * characters.
     */
    private void unreadSymbol() {
        if (characterIterator.getIndex() != characterIterator.getEndIndex()) {
            characterIterator.previous();
        }
    }

    /**
     * Reads letters and digits from
     * {@code characterIterator}
     *
     * @return string of digits
     */
    private String readLettersAndDigits() {
        StringBuilder sb = new StringBuilder();
        while (characterIterator.current() != CharacterIterator.DONE) {
            char currentChar = readSymbol();
            if (!isLetterOrDigit(currentChar)) {
                unreadSymbol();
                break;
            }
            sb.append(currentChar);
        }
        return sb.toString();
    }

    @Override
    public boolean hasNext() {
        return firstTime || characterIterator.getIndex() < characterIterator.getEndIndex() - 1;
    }

    @Override
    public Token next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        char currentChar = readSymbolUntilNonWhitespace();
        if (isNamedTokenPrefix(currentChar)) {
            String index = readLettersAndDigits();
            return new Token(TOKEN_TYPE.get(currentChar),  index);

        } else if (isPunctuationToken(currentChar)) {
            return new Token(TOKEN_TYPE.get(currentChar),  "");
        } else {
            throw new IllegalArgumentException(
                    String.format("invalid character %c (code point %d)", currentChar, (int) currentChar));
        }
    }
}
