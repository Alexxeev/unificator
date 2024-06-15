package unification;

import org.jetbrains.annotations.NotNull;
import syntax.Constant;
import syntax.TermWithArgs;
import syntax.Term;
import syntax.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * A class that converts triangular form substitution to
 * the non-ordered substitution.
 */
class TriangularFormConverter {
    /**
     * A binding list
     */
    @NotNull
    final Map<Term, Term> bindingList;

    /**
     * A list of terms that are already processed.
     */
    private final Map<Term, Term> ready = new IdentityHashMap<>();
    /**
     * A domain of the constructed non-ordered substitution.
     */
    private final Map<Term, Term> domain = new HashMap<>();

    /**
     * Constructs a new converter from provided binding list.
     *
     * @param bindingList a binding list.
     */
    TriangularFormConverter(@NotNull Map<Term, Term> bindingList) {
        this.bindingList = bindingList;
    }

    /**
     * Performs a conversion to the non-ordered form.
     *
     * @return non-ordered substitution
     */
    public Substitution convert() {
        for (Map.Entry<Term, Term> entry : bindingList.entrySet()) {
            domain.put(
                    entry.getKey(),
                    exploreVariable(entry.getKey()));
        }
        return Substitution.of(domain);
    }

    /**
     * Processes a term that is associated to the
     * provided variable.
     *
     * @param variable a variable
     * @return a new term that will be associated
     *         with this variable in a non-ordered
     *         substitution.
     */
    private Term exploreVariable(
            Term variable) {
        if (ready.containsKey(variable)) {
            return ready.get(variable);
        }
        Term result = descend(bindingList.get(variable));
        if (result == null) {
            result = variable;
        }
        ready.put(variable, result);
        return result;
    }

    /**
     * Recursively processes a provided term.
     *
     * @param term a term.
     * @return modified term.
     */
    private Term descend(Term term) {
        if (term == null) {
            return null;
        }
        if (term instanceof Variable) {
            return exploreVariable(term);
        }
        if (term instanceof Constant) {
            return term;
        }
        if (ready.containsKey(term)) {
            return ready.get(term);
        }
        if (term instanceof TermWithArgs termWithArgs) {
            List<Term> result = exploreArgs(termWithArgs.getArgs());
            if (result.equals(termWithArgs.getArgs())) {
                ready.put(termWithArgs, termWithArgs);
            } else {
                ready.put(termWithArgs, new TermWithArgs(termWithArgs.getName(), result));
            }
        }
        return ready.get(term);

    }

    /**
     * Processes a list of terms
     *
     * @param args list of terms
     * @return a new list that contains
     *         modified arguments.
     */
    private List<Term> exploreArgs(List<Term> args) {
        List<Term> newArgs = new ArrayList<>();
        for (Term arg : args) {
            newArgs.add(descend(arg));
        }
        return newArgs;
    }
}
