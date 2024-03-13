package unification;

import org.jetbrains.annotations.NotNull;
import syntax.Term;
import syntax.VariableTerm;

import java.util.Map;

record SingleSubstitution (
        Term variable,
        Term replacement
) implements Substitution {
    @Override
    public Term getBinding(Term variable) {
        if (    variable instanceof VariableTerm &&
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
