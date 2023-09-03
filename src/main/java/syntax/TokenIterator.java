package syntax;

import java.text.CharacterIterator;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

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
            Map.ofEntries(
                    new AbstractMap.SimpleImmutableEntry<>(
                            LEFT_PARENTHESIS,
                            Token.Type.LEFT_PARENTHESIS),
                    new AbstractMap.SimpleImmutableEntry<>(
                            RIGHT_PARENTHESIS,
                            Token.Type.RIGHT_PARENTHESIS),
                    new AbstractMap.SimpleImmutableEntry<>(
                            COMMA,
                            Token.Type.COMMA),
                    new AbstractMap.SimpleImmutableEntry<>(
                            VARIABLE_SYMBOL,
                            Token.Type.VARIABLE),
                    new AbstractMap.SimpleImmutableEntry<>(
                            CONSTANT_SYMBOL,
                            Token.Type.CONSTANT),
                    new AbstractMap.SimpleImmutableEntry<>(
                            FUNCTION_SYMBOL,
                            Token.Type.FUNCTIONAL_SYMBOL)
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
    public TokenIterator(CharacterIterator characterIterator) {
        this.characterIterator = characterIterator;
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
     * @param character a character
     * @return true if argument is punctuation symbol
     */
    private boolean isPunctuationToken(final char character) {
        return character == LEFT_PARENTHESIS
                || character == RIGHT_PARENTHESIS || character == COMMA;
    }

    /**
     * Returns true if argument is digit
     *
     * @param character a digit
     * @return true if argument is digit
     */
    private boolean isDigit(final char character) {
        return Character.isDigit(character);
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
     * Reads digit symbols from character
     * {@code characterIterator}
     *
     * @return string of digits
     */
    private String readDigits() {
        StringBuilder sb = new StringBuilder();
        while (characterIterator.current() != CharacterIterator.DONE) {
            char currentChar = readSymbol();
            if (!isDigit(currentChar)) {
                if (characterIterator.getIndex() != characterIterator.getEndIndex()) {
                    characterIterator.previous();
                }
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
        char currentChar = readSymbol();
        if (isNamedTokenPrefix(currentChar)) {
            String index = readDigits();
            return new Token(TOKEN_TYPE.get(currentChar),  index);

        } else if (isPunctuationToken(currentChar)) {
            return new Token(TOKEN_TYPE.get(currentChar),  "");
        } else {
            throw new IllegalArgumentException(
                    String.format("invalid character %c (code point %d)", currentChar, (int) currentChar));
        }
    }
}
