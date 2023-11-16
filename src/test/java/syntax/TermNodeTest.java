package syntax;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class TermNodeTest {
    @Test
    public void toString_ValidFunctionalSymbolTerm() {
        String termString = "f1(x1,c1)";
        TermNode term = TermNode.fromToken(
                new Token(Token.Type.FUNCTIONAL_SYMBOL,  "1")
        );
        FunctionalSymbolTermNode functionalSymbolTermNode = (FunctionalSymbolTermNode) term;
        functionalSymbolTermNode.prependChild(TermNode.fromToken(
                new Token(Token.Type.CONSTANT,  "1")
        ));
        functionalSymbolTermNode.prependChild(TermNode.fromToken(
                new Token(Token.Type.VARIABLE,  "1")
        ));

        assertEquals(termString, term.toString());
    }

    @Test
    public void toString_ValidFunctionalSymbolTerm2() {
        String termString = "f1(x1,f2(c1))";
        TermNode innerTerm = TermNode.fromToken(
                new Token(Token.Type.FUNCTIONAL_SYMBOL,  "2")
        );
        FunctionalSymbolTermNode functionalSymbolInnerTermNode = (FunctionalSymbolTermNode) innerTerm;
        functionalSymbolInnerTermNode.prependChild(TermNode.fromToken(
                new Token(Token.Type.CONSTANT,  "1")
        ));
        TermNode term = TermNode.fromToken(
                new Token(Token.Type.FUNCTIONAL_SYMBOL,  "1")
        );
        FunctionalSymbolTermNode functionalSymbolTermNode = (FunctionalSymbolTermNode) term;
        functionalSymbolTermNode.prependChild(innerTerm);
        functionalSymbolTermNode.prependChild(TermNode.fromToken(
                new Token(Token.Type.VARIABLE,  "1")
        ));

        assertEquals(termString, term.toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "c1",
            "x1",
            "f1(x1,c1)",
            "f1(x1,f2(c1))",
            "f3(f2(x1),x1,f1(f2(x2)));f3(x3,c1,f1(x3))"
    })
    public void deepCopy_ValidTerm_ShouldBeEqual(final String termString) {
        TermNode originalTerm = TermNode.fromString(termString);

        TermNode copyTerm = originalTerm.deepCopy();

        assertEquals(originalTerm.toString(), copyTerm.toString());
        assertNotSame(originalTerm, copyTerm);
    }
}