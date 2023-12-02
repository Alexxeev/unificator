package unification;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A class that creates unification strategies
 */
public final class UnificationStrategyFactory {
    /**
     * Do not instantiate this class
     */
    private UnificationStrategyFactory() {
        throw new AssertionError("Do not instantiate this class");
    }

    /**
     * A map that contains all the unification algorithms
     * used in this library
     */
    private static final Map<String, Supplier<UnificationStrategy>> algorithms = Map.of(
            "robinson", RobinsonUnificationStrategy::new,
            "robinson-poly", PolynomialRobinsonUnificationStrategy::new,
            "paterson-wegman", PatersonWegmanUnificationStrategy::new
    );

    /**
     * Creates a new instance of {@link UnificationStrategy} from
     * provided name of the unification algorithm.
     * Valid names are: {@code robinson}, {@code robinson-poly}
     * and {@code paterson-wegman}
     *
     * @param algorithm name of the unification algorithm
     * @return a new {@link UnificationStrategy} instance
     * @throws NullPointerException if {@code algorithm} is null
     * @throws NoSuchElementException if there is no such unification
     *                                strategy associated with provided
     *                                algorithm name.
     */
    public static UnificationStrategy createInstance(@NotNull final String algorithm) {
        Objects.requireNonNull(algorithm);
        if (!algorithms.containsKey(algorithm)) {
            throw new NoSuchElementException(String.format("Invalid key %s provided", algorithm));
        }
        return algorithms.get(algorithm).get();
    }
}
