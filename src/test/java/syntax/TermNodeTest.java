package syntax;

import org.junit.jupiter.api.Test;

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
}