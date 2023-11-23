package unification;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import syntax.Term;

import static org.junit.jupiter.api.Assertions.*;

class UnificationTest {
    @ParameterizedTest
    @CsvSource(value = {
            "f(x,f1(c));f(f1(c),f1(x1))",
            "f3(f2(x1),x1,f2(x2));f3(x3,c1,x3)",
            "f3(f2(x1),x1,f1(f2(x2)));f3(x3,c1,f1(x3))"

    }, delimiter = ';')
    public void testRobinsonUnification_termsAreUnifiable(String termString1, String termString2) {
        Term term1 = Term.dagFromString(termString1);
        Term term2 = Term.dagFromString(termString2);

        UnificationResult unificationResult = UnificationStrategy.ROBINSON.findUnifier(term1, term2);

        assertTrue(unificationResult.isUnifiable());
        Substitution unifier = unificationResult.unifier();
        assertFalse(unifier.domain().isEmpty());
        assertEquals(unifier.instantiateVariables(term1).toString(), unifier.instantiateVariables(term2).toString());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "f(x,f1(c));f(f1(c),f1(x1))",
            "f3(f2(x1),x1,f2(x2));f3(x3,c1,x3)",
            "f3(f2(x1),x1,f1(f2(x2)));f3(x3,c1,f1(x3))"

    }, delimiter = ';')
    public void testPolynomialRobinsonUnification_termsAreUnifiable(String termString1, String termString2) {
        Term term1 = Term.dagFromString(termString1);
        Term term2 = Term.dagFromString(termString2);

        UnificationResult unificationResult = UnificationStrategy.ROBINSON_POLYNOMIAL.findUnifier(term1, term2);

        assertTrue(unificationResult.isUnifiable());
        Substitution unifier = unificationResult.unifier();
        assertFalse(unifier.domain().isEmpty());
        assertEquals(unifier.instantiateVariables(term1).toString(), unifier.instantiateVariables(term2).toString());
    }
}