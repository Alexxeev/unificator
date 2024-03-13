package syntax;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

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

    @Test
    public void testFunctionFirstTermIterator() {
        String termString = "f3(f2(x1),x1,f1(f2(x2)))";
        Term term = Term.fromString(termString);
        String[] expectedTermNodes = {
                "f3",
                "f2",
                "f1",
                "f2",
                "x1",
                "x1",
                "x2"
        };


        Iterator<Term> iterator = new FunctionalSymbolFirstTermIterator(term);
        List<String> termNoddeList = new ArrayList<>();
        iterator.forEachRemaining(t -> termNoddeList.add(t.getName()));
        String[] actualTermNodes = termNoddeList.toArray(String[]::new);

        Assertions.assertArrayEquals(expectedTermNodes, actualTermNodes);
    }
}