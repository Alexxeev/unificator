package syntax;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class TermParserTest {
    @ParameterizedTest
    @ValueSource(strings = {
            "f1(x1,c1)",
            "f1(x1,f2(c1))",
            "fNamed(xVar)",
            " f1( xNamed1, f2(cNamed1) ) "
    })
    public void testTermParsingCase(String termString) {
        Term term = Term.fromString(termString);

        assertEquals(termString.replaceAll("\\s", ""), term.toString());
    }

    @Test
    public void testTermDagParsing() {
        String termString1 = "f(x)";
        String termString2 = "x";

        TermPair actualPair = TermPair.fromStrings(termString1, termString2);

        assertEquals(termString1, actualPair.term1().toString());
        assertEquals(termString2, actualPair.term2().toString());
        Term child = ((TermWithArgs) actualPair.term1()).getChildren().get(0);
        assertSame(child.getParents().get(0), actualPair.term1());
        assertSame(child, actualPair.term2());
    }
}