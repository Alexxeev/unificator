package unification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import syntax.Term;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SubstitutionTest {
    @Test
    public void testVariableSubstitution() {
        Substitution substitution = Substitution.of(
                Map.of(
                        Term.fromString("x4"), Term.fromString("c1"),
                        Term.fromString("x1"), Term.fromString("f1(x3,x6)"),
                        Term.fromString("x2"), Term.fromString("x1"),
                        Term.fromString("x3"), Term.fromString("f2(c2,x2,x3)")
                )
        );
        Term term1 = Term.fromString("x5");
        Term term2 = Term.fromString("f3(x1,x3)");
        Term term3 = Term.fromString("x2");
        Term term4 = Term.fromString("x3");

        Term actualSubstitution1 = substitution.instantiateVariables(term1);
        Term actualSubstitution2 = substitution.instantiateVariables(term2);
        Term actualSubstitution3 = substitution.instantiateVariables(term3);
        Term actualSubstitution4 = substitution.instantiateVariables(term4);

        assertEquals("x5", actualSubstitution1.toString());
        assertEquals("f3(f1(x3,x6),f2(c2,x2,x3))", actualSubstitution2.toString());
        assertEquals("x1", actualSubstitution3.toString());
        assertEquals("f2(c2,x2,x3)", actualSubstitution4.toString());
    }
}