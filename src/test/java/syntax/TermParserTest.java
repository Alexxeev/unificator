package syntax;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TermParserTest {
    @ParameterizedTest
    @ValueSource(strings = {
            "f1(x1,c1)",
            "f1(x1,f2(c1))",
            "fNamed(xVar)",
            " f1( xNamed1, f2(cNamed1) ) "
    })
    public void testTermParsingCase(String termString) {
        TermNode term = TermNode.fromString(termString);

        assertEquals(termString.replaceAll("\\s", ""), term.toString());
    }
}