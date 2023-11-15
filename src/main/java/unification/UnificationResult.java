package unification;

/**
 * Represents a result of unification of input set.
 * It contains status of unification (successful unification
 * or term set is not unifiable) and unifier of the input set.
 *
 * @param isUnifiable true if input set is unifiable.
 * @param unifier     unifier of the input set if unifier is found.
 *                    It returns identity substitution
 *                    if input set is not unifiable.
 */
public record UnificationResult(
        boolean isUnifiable,
        Substitution unifier
) {
}
