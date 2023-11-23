package syntax;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

class TermIteratorTest {
    @Test
    public void testTermIterator() {
        String termString = "f3(f2(x1),x1,f1(f2(x2)))";
        Term term = Term.fromString(termString);
        String[] expectedTermNodes = {
                "f3",
                "f2",
                "x1",
                "x1",
                "f1",
                "f2",
                "x2"
        };

        Iterator<Term> iterator = new PreOrderTermIterator(term);
        int count = 0;

        while (iterator.hasNext()) {
            Assertions.assertEquals(expectedTermNodes[count], iterator.next().getName());
            count++;
        }
        Assertions.assertEquals(expectedTermNodes.length, count);
    }
}