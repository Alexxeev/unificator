package unification;

import org.jetbrains.annotations.NotNull;
import syntax.Term;
import syntax.Variable;

import java.util.Map;

record SingleSubstitution (
        Term variable,
        Term replacement
) implements Substitution {
    @Override
    public Term getBinding(Term variable) {
        if (    variable instanceof Variable &&
                this.variable.nameEquals(variable)) {
            return replacement;
        }
        return variable;
    }

    @Override
    public @NotNull Map<Term, Term> domain() {
        return Map.of(variable, replacement);
    }
}
