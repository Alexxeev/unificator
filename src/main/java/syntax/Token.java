package syntax;

import org.jetbrains.annotations.NotNull;

/**
 * A record class to store token data.
 *
 * @param tokenType type of the token
 * @param optionalIndex index of token. Note: only constants,
 *                      variables, and functional symbols can have index.
 */
record Token(
        @NotNull Token.Type tokenType,
        String optionalIndex
) {

    /**
     * Returns a string representation of the token.
     */
    @Override
    public String toString() {
        return String.format("%c%s", tokenType.relatedCharacter, optionalIndex);
    }

    /**
     * Defines token type.
     */
    public enum Type {
        /**
         * A variable token
         */
        VARIABLE('x'),
        /**
         * A constant token
         */
        CONSTANT('c'),
        /**
         * A functional symbol token
         */
        FUNCTIONAL_SYMBOL('f'),
        /**
         * A left parenthesis token
         */
        LEFT_PARENTHESIS('('),
        /**
         * A right parenthesis token
         */
        RIGHT_PARENTHESIS(')'),
        /**
         * A comma token
         */
        COMMA(',');

        /**
         * A character (or prefix) associated with this token type.
         */
        private final char relatedCharacter;

        Type(final char relatedCharacter) {
            this.relatedCharacter = relatedCharacter;
        }

    }
}
