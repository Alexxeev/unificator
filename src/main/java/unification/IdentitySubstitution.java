package unification;

import org.jetbrains.annotations.NotNull;
import syntax.Term;

import java.util.Map;

enum IdentitySubstitution implements Substitution {
    INSTANCE {
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
    };
}
