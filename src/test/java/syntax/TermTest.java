package syntax;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TermTest {
    @Test
    public void toString_ValidFunctionalSymbolTerm() {
        String termString = "f1(x1,c1)";
        Term term = new FunctionalSymbolTerm(
                "f1",
                Arrays.asList(
                        Term.fromToken(
                                new Token(Token.Type.VARIABLE,  "1")),
                        Term.fromToken(
                                new Token(Token.Type.CONSTANT,  "1")
                        )
                )
        );

        assertEquals(termString, term.toString());
    }

    @Test
    public void toString_ValidFunctionalSymbolTerm2() {
        String termString = "f1(x1,f2(c1))";
        Term term = new FunctionalSymbolTerm(
                "f1",
                Arrays.asList(
                        new ConstantTerm("x1"),
                        new FunctionalSymbolTerm(
                                "f2",
                                Arrays.asList(
                                        new ConstantTerm("c1")
                                )
                        )
                )
        );

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

        Term copyTerm = Term.copyOf(originalTerm);

        assertEquals(originalTerm, copyTerm);
        assertNotSame(originalTerm, copyTerm);
    }

    @Test
    public void deepCopy_ShouldPreserveIdentities() {
        TermPair originalPair = TermPair.fromStrings("f1(x1,c1)", "f1(x1,f2(c1))");

        TermPair copyPair = TermPair.copyOf(originalPair);

        assertEquals(originalPair.term1(), copyPair.term1());
        assertEquals(originalPair.term2(), copyPair.term2());
        assertNotSame(originalPair.term1(), copyPair.term1());
        assertNotSame(originalPair.term1(), copyPair.term2());
        Term child1_1 = copyPair.term1().getChildren().get(0);
        Term child2_1 = copyPair.term2().getChildren().get(0);
        assertSame(child1_1, child2_1);
        assertEquals(2, child1_1.getParents().size());
        assertSame(copyPair.term1(), child1_1.getParents().get(0));
        assertSame(copyPair.term2(), child2_1.getParents().get(1));
        Term child1_2 = copyPair.term1().getChildren().get(1);
        Term subChild2_2 = copyPair.term2().getChildren().get(1).getChildren().get(0);
        assertSame(child1_2, subChild2_2);
    }

    @Test
    public void toString_SubtermModified_ShouldModifyParentTerm() {
        Term subterm = Term.fromString("f1(c,x)");
        Term term = new FunctionalSymbolTerm(
                "f",
                Arrays.asList(subterm, subterm)
        );

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

    @Test
    public void ctor_nullArgs_shouldThrow() {
        assertThrows(NullPointerException.class, () -> new TermPair(null, null));
    }
}