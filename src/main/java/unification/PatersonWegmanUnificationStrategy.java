package unification;

import org.jetbrains.annotations.NotNull;
import syntax.ConstantTerm;
import syntax.FunctionalSymbolFirstTermIterator;
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

/**
 * An implementation of a linear unification algorithm by Paterson and Wegman.
 */
public final class PatersonWegmanUnificationStrategy implements UnificationStrategy {
    /**
     * A flag that is set to false if terms are not unifiable.
     */
    private boolean isUnifiable = true;
    /**
     * A list of pointers
     */
    private final Map<Term, Term> pointers = new IdentityHashMap<>();
    /**
     * A set of already processed terms
     */
    private final Set<Term> finished = Collections.newSetFromMap(new IdentityHashMap<>());
    /**
     * A list of undirected edges between nodes of two graphs
     */
    private final Map<Term, List<Term>> links = new IdentityHashMap<>();
    /**
     * A unifier in the triangular form.
     */
    private final Map<Term, Term> bindingList = new HashMap<>();
    @Override
    public @NotNull UnificationResult findUnifier(@NotNull final TermPair termPair) {
        //TermPair termPairCopy = TermPair.copyOf(Objects.requireNonNull(termPair));
        createLink(termPair.term1(), termPair.term2());

        Iterator<Term> funcFirstIterator = new FunctionalSymbolFirstTermIterator(
                termPair);
        funcFirstIterator.forEachRemaining(this::finish);

        if (isUnifiable) {
            return UnificationResult.unifiable(Substitution.fromTriangularForm(bindingList));
        }
        return UnificationResult.notUnifiable();
    }

    /**
     * A core method of term unification.
     *
     * @param term a term
     */
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
            for (Term parent : currentTerm.getParents()) {
                finish(parent);
            }
            for (Term link : links.getOrDefault(currentTerm, List.of())) {
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

    /**
     * Checks whether the provided terms are either functional symbol terms
     * or constant terms.
     *
     * @param term1 first term
     * @param term2 second term
     * @return true if the provided terms are either functional symbol terms
     *         or constant terms.
     */
    private boolean areFuncsOrConstants(Term term1, Term term2) {
        return (term1 instanceof FunctionalSymbolTerm &&
                term2 instanceof FunctionalSymbolTerm) ||
                (term1 instanceof ConstantTerm &&
                 term2 instanceof ConstantTerm);
    }

    /**
     * Creates an undirected edge between two nodes
     *
     * @param term1 first node
     * @param term2 second node
     */
    private void createLink(Term term1, Term term2) {
        links.computeIfAbsent(term1, t -> new ArrayList<>()).add(term2);
        links.computeIfAbsent(term2, t -> new ArrayList<>()).add(term1);
    }
}
