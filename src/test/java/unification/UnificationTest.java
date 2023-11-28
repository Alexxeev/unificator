package unification;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import syntax.Term;
import syntax.TermPair;

import static org.junit.jupiter.api.Assertions.*;

class UnificationTest {
    @ParameterizedTest
    @CsvSource(value = {
            "f(x,f1(c));f(f1(c),f1(x1))",
            "f3(f2(x1),x1,f2(x2));f3(x3,c1,x3)",
            "f3(f2(x1),x1,f1(f2(x2)));f3(x3,c1,f1(x3))"

    }, delimiter = ';')
    public void testRobinsonUnification_termsAreUnifiable(String termString1, String termString2) {
        TermPair termPair = TermPair.fromStrings(termString1, termString2);

        UnificationResult unificationResult = UnificationStrategy.ROBINSON.findUnifier(termPair);

        assertTrue(unificationResult.isUnifiable());
        Substitution unifier = unificationResult.unifier();
        assertFalse(unifier.domain().isEmpty());
        assertEquals(
                unifier.instantiateVariables(termPair.term1()).toString(),
                unifier.instantiateVariables(termPair.term2()).toString());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "f(x,f1(c));f(f1(c),f1(x1))",
            "f3(f2(x1),x1,f2(x2));f3(x3,c1,x3)",
            "f3(f2(x1),x1,f1(f2(x2)));f3(x3,c1,f1(x3))"

    }, delimiter = ';')
    public void testPolynomialRobinsonUnification_termsAreUnifiable(String termString1, String termString2) {
        TermPair termPair = TermPair.fromStrings(termString1, termString2);

        UnificationResult unificationResult = UnificationStrategy.ROBINSON_POLYNOMIAL.findUnifier(termPair);

        assertTrue(unificationResult.isUnifiable());
        Substitution unifier = unificationResult.unifier();
        assertFalse(unifier.domain().isEmpty());
        assertEquals(unifier.instantiateVariables(termPair.term1()).toString(), unifier.instantiateVariables(termPair.term2()).toString());
    }
}