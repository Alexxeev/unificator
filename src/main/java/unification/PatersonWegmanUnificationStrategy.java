package unification;

import org.jetbrains.annotations.NotNull;
import syntax.ConstantTerm;
import syntax.FunctionalSymbolTerm;
import syntax.Term;
import syntax.TermPair;
import syntax.VariableTerm;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

public final class PatersonWegmanUnificationStrategy implements UnificationStrategy {
    private boolean isUnifiable = true;
    private final Map<Term, Term> pointers = new IdentityHashMap<>();
    private final Set<Term> finished = Collections.newSetFromMap(new IdentityHashMap<>());
    private final Map<Term, List<Term>> links = new IdentityHashMap<>();
    private final Map<Term, Term> bindingList = new HashMap<>();
    @Override
    public @NotNull UnificationResult findUnifier(@NotNull final TermPair termPair) {
        TermPair termPairCopy = TermPair.copyOf(Objects.requireNonNull(termPair));
        createLink(termPairCopy.term1(), termPairCopy.term2());

        finish(termPairCopy.term1(), termPairCopy.term2(), term ->
                (term instanceof FunctionalSymbolTerm) || (term instanceof ConstantTerm));
        finish(termPairCopy.term1(), termPairCopy.term2(), term -> term instanceof VariableTerm);

        if (isUnifiable) {
            return UnificationResult.unifiable(Substitution.fromTriangularForm(bindingList));
        }
        return UnificationResult.notUnifiable();
    }



    private void finish(Term term1, Term term2, Predicate<Term> instancePredicate) {
        Iterator<Term> iterator1 = term1.iterator();
        Iterator<Term> iterator2 = term2.iterator();
        while (iterator1.hasNext() && iterator2.hasNext()) {
            Term currentTerm1 = iterator1.next();
            if (instancePredicate.test(currentTerm1)) {
                finish(currentTerm1);
            }
            Term currentTerm2 = iterator2.next();
            if (instancePredicate.test(currentTerm2)) {
                finish(currentTerm2);
            }
        }
    }

    private void finish(Term term) {
        if (finished.contains(term)) {
            return;
        }
        if (pointers.containsKey(term)) {
            isUnifiable = false;
            return;
        }
        pointers.put(term, term);
        Deque<Term> termStack = new ArrayDeque<>();
        termStack.push(term);
        while (!termStack.isEmpty()) {
            Term currentTerm = termStack.pop();
            if (
                    areFuncsOrConstants(currentTerm, term) &&
                    !currentTerm.nameEquals(term)
            ) {
                isUnifiable = false;
                return;
            }
            for (Term parent : term.getParents()) {
                finish(parent);
            }
            for (Term link : links.get(currentTerm)) {
                if (finished.contains(link) || link.equals(term)) {
                    continue;
                }
                if (!pointers.containsKey(link)) {
                    pointers.put(link, term);
                    termStack.push(link);
                } else if (!pointers.get(link).equals(term)) {
                    isUnifiable = false;
                    return;
                }
            }
            if (!currentTerm.equals(term)) {
                if (currentTerm instanceof VariableTerm) {
                    bindingList.put(currentTerm, term);
                } else if (currentTerm instanceof FunctionalSymbolTerm) {
                    Iterator<Term> currentTermChildren = currentTerm.getChildren().listIterator();
                    Iterator<Term> termChildren = term.getChildren().listIterator();
                    while (currentTermChildren.hasNext() && termChildren.hasNext()) {
                        createLink(currentTermChildren.next(), termChildren.next());
                    }
                }
                finished.add(currentTerm);
            }
        }
        finished.add(term);
    }

    private boolean areFuncsOrConstants(Term term1, Term term2) {
        return (term1 instanceof FunctionalSymbolTerm &&
                term2 instanceof FunctionalSymbolTerm) ||
                (term1 instanceof ConstantTerm &&
                 term2 instanceof ConstantTerm);
    }

    private void createLink(Term term1, Term term2) {
        links.computeIfAbsent(term1, t -> new ArrayList<>()).add(term2);
        links.computeIfAbsent(term2, t -> new ArrayList<>()).add(term1);
    }
}
