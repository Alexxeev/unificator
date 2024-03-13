package unification;

import org.jetbrains.annotations.NotNull;
import syntax.Term;

import java.util.Map;

class IdentitySubstitution implements Substitution {
    static final Substitution INSTANCE = new IdentitySubstitution();

    private IdentitySubstitution() {}

    @Override
    public Term getBinding(Term variable) {
        return variable;
    }

    @Override
    public @NotNull Term instantiateVariables(@NotNull Term term) {
        return term;
    }

    @Override
    public @NotNull Map<Term, Term> domain() {
        return Map.of();
    }
}
