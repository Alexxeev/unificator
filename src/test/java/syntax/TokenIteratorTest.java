package syntax;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.StringCharacterIterator;
import java.util.Iterator;

class TokenIteratorTest {
    @Test
    public void testValidConstant() {
        String constant = "c1";
        Iterator<Token> tokenIterator = new TokenIterator(
                new StringCharacterIterator(constant)
        );

        Token token = Assertions.assertDoesNotThrow(tokenIterator::next);
        Assertions.assertFalse(tokenIterator.hasNext());
        Assertions.assertEquals(constant, token.toString());
        Assertions.assertEquals("1", token.optionalIndex());
    }

    @Test
    public void testValidFunctionalSymbolTerm() {
        String termString = "f1(x1,c1)";
        Iterator<Token> tokenSequence = new TokenIterator(
                new StringCharacterIterator(termString)
        );
        Token[] expectedTokens = {
                new Token(Token.Type.FUNCTIONAL_SYMBOL, "1"),
                new Token(Token.Type.LEFT_PARENTHESIS,  ""),
                new Token(Token.Type.VARIABLE,  "1"),
                new Token(Token.Type.COMMA,  ""),
                new Token(Token.Type.CONSTANT,  "1"),
                new Token(Token.Type.RIGHT_PARENTHESIS,  "")
        };
        int i = 0;
        while (tokenSequence.hasNext()){
            Token actualToken = tokenSequence.next();
            Assertions.assertEquals(expectedTokens[i].toString(), actualToken.toString());
            i++;
        }
        Assertions.assertEquals(6, i);
    }
}