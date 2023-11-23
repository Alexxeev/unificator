package syntax;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.List;

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
        String termString = "f(f1(c,x),f1(c,x))";
        CharacterIterator characterIterator = new StringCharacterIterator(termString);
        TokenIterator iterator = new TokenIterator(characterIterator);
        TermDagParser parser = new TermDagParser(iterator);

        Term actualTerm = parser.parseTerm();

        assertEquals(termString, actualTerm.toString());
        assertEquals(0, actualTerm.getParents().size());
        assertInstanceOf(FunctionalSymbolTerm.class, actualTerm);
        List<Term> children = actualTerm.getChildren();
        assertEquals(2, children.size());
        Term child1 = children.get(0);
        Term child2 = children.get(1);
        assertSame(child1, child2);
        assertEquals(child1.getParents().size(), child2.getParents().size());
        assertSame(child1.getParents().get(0), child2.getParents().get(0));
    }
}