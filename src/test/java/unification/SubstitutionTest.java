package unification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import syntax.Term;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SubstitutionTest {
    @ParameterizedTest
    @CsvSource(value = {
            "f1(x5);x5;c1;f1(c1)",
            "x5;x5;c1;c1"
    }, delimiter = ';')
    public void instantiateVariablesInPlace_ShouldModifyArgument(
            String termString,
            String variable,
            String substitutionTerm,
            String expectedTermString
    ) {
        Term term = Term.fromString(termString);
        Substitution substitution = Substitution.of(Term.fromString(variable), Term.fromString(substitutionTerm));

        term = substitution.instantiateVariablesInPlace(term);

        assertEquals(expectedTermString, term.toString());
    }

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

    @Test
    public void testSubstitutionComposition_firstSubstitutionIsIdentity() {
        Substitution substitution1 = Substitution.identity();
        Substitution substitution2 = Substitution.of(
                Map.of(
                        Term.fromString("x1"), Term.fromString("x2")
                )
        );

        Substitution composition = substitution1.composition(substitution2);

        assertEquals(1, composition.domain().size());
        assertTrue(composition.domain().containsKey(Term.fromString("x1")));
        assertEquals("x2", composition.domain().get(Term.fromString("x1")).toString());
    }

    @Test
    public void testSubstitutionComposition() {
        Substitution substitution1 = Substitution.of(
                Map.of(
                Term.fromString("x1"), Term.fromString("f1(x2)"),
                Term.fromString("x2"), Term.fromString("x3")
                )
        );
        Substitution substitution2 = Substitution.of(
                Map.of(
                        Term.fromString("x1"), Term.fromString("c1"),
                        Term.fromString("x2"), Term.fromString("c2"),
                        Term.fromString("x3"), Term.fromString("x2")
                )
        );

        Substitution composition = substitution1.composition(substitution2);
        Map<Term, Term> actualDomain = composition.domain();


        assertEquals(2, actualDomain.size());
        assertFalse(actualDomain.containsKey(Term.fromString("x2")));
        assertTrue(actualDomain.containsKey(Term.fromString("x1")));
        assertTrue(actualDomain.containsKey(Term.fromString("x3")));
        assertEquals("f1(c2)", actualDomain.get(Term.fromString("x1")).toString());
        assertEquals("x2", actualDomain.get(Term.fromString("x3")).toString());
    }
}