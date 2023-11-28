package unification;

import org.jetbrains.annotations.NotNull;
import syntax.ConstantTerm;
import syntax.FunctionalSymbolTerm;
import syntax.Term;
import syntax.VariableTerm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

class TriangularFormConverter {
    @NotNull
    final Map<Term, Term> bindingList;

    private final Map<Term, Term> ready = new IdentityHashMap<>();
    private final Map<Term, Term> domain = new HashMap<>();

    TriangularFormConverter(@NotNull Map<Term, Term> bindingList) {
        this.bindingList = bindingList;
    }

    public Substitution convert() {
        for (Map.Entry<Term, Term> entry : bindingList.entrySet()) {
            domain.put(
                    entry.getKey(),
                    exploreVariable(entry.getKey()));
        }
        return Substitution.of(domain);
    }

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

    private Term descend(Term term) {
        if (term == null) {
            return null;
        }
        if (term instanceof VariableTerm) {
            return exploreVariable(term);
        }
        if (term instanceof ConstantTerm) {
            return term;
        }
        if (ready.containsKey(term)) {
            return ready.get(term);
        }
        List<Term> result = exploreArgs(term.getChildren());
        if (result.equals(term.getChildren())) {
            ready.put(term, term);
        } else {
            ready.put(term, new FunctionalSymbolTerm(term.getName(), result));
        }
        return ready.get(term);

    }

    private List<Term> exploreArgs(List<Term> args) {
        List<Term> newArgs = new ArrayList<>();
        for (Term arg : args) {
            newArgs.add(descend(arg));
        }
        return newArgs;
    }
}
