package syntax;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class TermTest {
    @Test
    public void toString_ValidFunctionalSymbolTerm() {
        String termString = "f1(x1,c1)";
        Term term = Term.fromToken(
                new Token(Token.Type.FUNCTIONAL_SYMBOL,  "1")
        );
        FunctionalSymbolTerm functionalSymbolTermNode = (FunctionalSymbolTerm) term;
        functionalSymbolTermNode.prependChild(Term.fromToken(
                new Token(Token.Type.CONSTANT,  "1")
        ));
        functionalSymbolTermNode.prependChild(Term.fromToken(
                new Token(Token.Type.VARIABLE,  "1")
        ));

        assertEquals(termString, term.toString());
    }

    @Test
    public void toString_ValidFunctionalSymbolTerm2() {
        String termString = "f1(x1,f2(c1))";
        Term innerTerm = Term.fromToken(
                new Token(Token.Type.FUNCTIONAL_SYMBOL,  "2")
        );
        FunctionalSymbolTerm functionalSymbolInnerTermNode = (FunctionalSymbolTerm) innerTerm;
        functionalSymbolInnerTermNode.prependChild(Term.fromToken(
                new Token(Token.Type.CONSTANT,  "1")
        ));
        Term term = Term.fromToken(
                new Token(Token.Type.FUNCTIONAL_SYMBOL,  "1")
        );
        FunctionalSymbolTerm functionalSymbolTermNode = (FunctionalSymbolTerm) term;
        functionalSymbolTermNode.prependChild(innerTerm);
        functionalSymbolTermNode.prependChild(Term.fromToken(
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
        Term originalTerm = Term.fromString(termString);

        Term copyTerm = originalTerm.deepCopy();

        assertEquals(originalTerm.toString(), copyTerm.toString());
        assertNotSame(originalTerm, copyTerm);
    }

    @Test
    public void toString_SubtermModified_ShouldModifyParentTerm() {
        Term subterm = Term.fromString("f1(c,x)");
        Term term = Term.fromToken(new Token(Token.Type.FUNCTIONAL_SYMBOL, ""));
        FunctionalSymbolTerm functionalSymbolTerm = (FunctionalSymbolTerm) term;
        functionalSymbolTerm.addChild(subterm);
        functionalSymbolTerm.addChild(subterm);

        FunctionalSymbolTerm functionalSymbolSubterm = (FunctionalSymbolTerm) subterm;
        functionalSymbolSubterm.setChild(1, Term.fromString("c1"));

        assertEquals("f(f1(c,c1),f1(c,c1))", functionalSymbolTerm.toString());
    }
}