package unification;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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
    /**
     * Creates a result of successful unification process
     * @param unifier a unifier
     * @return a new {@code UnificationResult} that contains result of
     *         successful unification process
     */
    public static UnificationResult unifiable(@NotNull Substitution unifier) {
        return new UnificationResult(true, Objects.requireNonNull(unifier));
    }

    /**
     * Creates a result of unsuccessful unification process
     * @return a new {@code UnificationResult} that contains result of
     *         unsuccessful unification process
     */
    public static UnificationResult notUnifiable() {
        return new UnificationResult(false, Substitution.identity());
    }
}
