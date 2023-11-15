package syntax;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TermParserTest {
    String[] termParsingTestCases = {
            "f1(x1,c1)",
            "f1(x1,f2(c1))",
            "fNamed(xVar)"
    };

    public void testTermParsingCase(String termString) {
        TermNode term = TermNode.fromString(termString);

        assertEquals(termString, term.toString());
    }

    @Test
    public void testTermParsing() {
        for (String termString : termParsingTestCases) {
            testTermParsingCase(termString);
        }
    }
}