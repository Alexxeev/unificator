package syntax;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TermTest {
    @Test
    public void toString_ValidFunctionalSymbolTerm() {
        String termString = "f1(x1,c1)";
        Term term = new TermWithArgs(
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
        Term term = new TermWithArgs(
                "f1",
                Arrays.asList(
                        new Constant("x1"),
                        new TermWithArgs(
                                "f2",
                                Arrays.asList(
                                        new Constant("c1")
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