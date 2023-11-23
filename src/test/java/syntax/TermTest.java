package syntax;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.text.StringCharacterIterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TermTest {
    @Test
    public void toString_ValidFunctionalSymbolTerm() {
        String termString = "f1(x1,c1)";
        Term term = Term.fromToken(
                new Token(Token.Type.FUNCTIONAL_SYMBOL,  "1")
        );
        term.addChild(Term.fromToken(
                new Token(Token.Type.VARIABLE,  "1")
        ));
        term.addChild(Term.fromToken(
                new Token(Token.Type.CONSTANT,  "1")
        ));

        assertEquals(termString, term.toString());
    }

    @Test
    public void toString_ValidFunctionalSymbolTerm2() {
        String termString = "f1(x1,f2(c1))";
        Term innerTerm = Term.fromToken(
                new Token(Token.Type.FUNCTIONAL_SYMBOL,  "2")
        );
        innerTerm.addChild(Term.fromToken(
                new Token(Token.Type.CONSTANT,  "1")
        ));
        Term term = Term.fromToken(
                new Token(Token.Type.FUNCTIONAL_SYMBOL,  "1")
        );
        term.addChild(Term.fromToken(
                new Token(Token.Type.VARIABLE,  "1")
        ));
        term.addChild(innerTerm);

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

        assertEquals(originalTerm, copyTerm);
        assertNotSame(originalTerm, copyTerm);
    }

    @Test
    public void deepCopy_ShouldPreserveIdentities() {
        Term originalTerm = new TermDagParser(
                new TokenIterator(
                        new StringCharacterIterator("f(f1(x,c),f1(x,c))")
                )
        ).parseTerm();

        Term copyTerm = originalTerm.deepCopy();

        assertInstanceOf(FunctionalSymbolTerm.class, copyTerm);
        List<Term> children = copyTerm.getChildren();
        assertEquals(2, children.size());
        Term child1 = children.get(0);
        Term child2 = children.get(1);
        assertSame(child1, child2);
        assertSame(child1.getParents().get(0), child2.getParents().get(0));
    }

    @Test
    public void toString_SubtermModified_ShouldModifyParentTerm() {
        Term subterm = Term.fromString("f1(c,x)");
        Term term = Term.fromToken(
                new Token(Token.Type.FUNCTIONAL_SYMBOL, ""));
        term.addChild(subterm);
        term.addChild(subterm);

        subterm.setChild(1, Term.fromString("c1"));

        assertEquals("f(f1(c,c1),f1(c,c1))", term.toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "c1",
            "x1",
            "f1(x1,c1)",
            "f1(x1,f2(c1))",
            "f3(f2(x1),x1,f1(f2(x2)))"
    })
    public void equals_ShouldBeReflective(String termString) {
        Term term = Term.fromString(termString);

        assertEquals(term, term);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "c1",
            "x1",
            "f1(x1,c1)",
            "f1(x1,f2(c1))",
            "f3(f2(x1),x1,f1(f2(x2)))"
    })
    public void equals_ShouldBeSymmetric(String termString) {
        Term term1 = Term.fromString(termString);
        Term term2 = Term.fromString(termString);

        assertEquals(term1, term2);
        assertEquals(term2, term1);
    }

    @Test
    public void equals_shouldBeFalseForNull() {
        Term term = Term.fromString("f1(c,x)");

        assertNotEquals(null, term);
    }
}