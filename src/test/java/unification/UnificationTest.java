package unification;

import org.junit.jupiter.api.Test;
import syntax.TermNode;

import static org.junit.jupiter.api.Assertions.*;

class UnificationTest {
    @Test
    public void testUnification_termsAreUnifiable() {
        TermNode term1 = TermNode.fromString("f3(f2(x1),x1,f2(x2))");
        TermNode term2 = TermNode.fromString("f3(x3,c1,x3)");

        Unification.UnificationResult unificationResult = Unification.findUnifier(term1, term2);

        assertTrue(unificationResult.isUnifiable());
        Substitution unifier = unificationResult.unifier();
        assertFalse(unifier.domain().isEmpty());
        assertEquals(unifier.instantiateVariables(term1).toString(), unifier.instantiateVariables(term2).toString());
    }

    @Test
    public void testUnification_termsAreUnifiable2() {
        TermNode term1 = TermNode.fromString("f3(f2(x1),x1,f1(f2(x2)))");
        TermNode term2 = TermNode.fromString("f3(x3,c1,f1(x3))");

        Unification.UnificationResult unificationResult = Unification.findUnifier(term1, term2);

        assertTrue(unificationResult.isUnifiable());
        Substitution unifier = unificationResult.unifier();
        assertFalse(unifier.domain().isEmpty());
        assertEquals(unifier.instantiateVariables(term1).toString(), unifier.instantiateVariables(term2).toString());
    }
}