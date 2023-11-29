package syntax;

import org.jetbrains.annotations.NotNull;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A pair of terms with shared variables.
 * Terms are stored as reduced directed acyclic graphs.
 * Variables are shared between two terms.
 * This representation is more space efficient
 * than regular syntax tree.
 *
 * @param term1 first term
 * @param term2 second term
 */
public record TermPair(
        Term term1,
        Term term2
) {
    /**
     * Constructs a pair of terms
     *
     * @param term1 first term
     * @param term2 second term
     */
    public TermPair {
        Objects.requireNonNull(term1);
        Objects.requireNonNull(term2);
    }

    /**
     * Creates a pair of term from provided string representations
     *
     * @param termString1 string representation of the first term
     * @param termString2 string representation of the second term
     * @return a {@code TermPair} instance that contains two terms
     *         converted from corresponding string representations.
     * @see Term#fromString(String)
     */
    public static TermPair fromStrings(
            @NotNull final String termString1,
            @NotNull final String termString2
    ) {
        return new TermDagParser(
                TokenIterator.of(termString1),
                TokenIterator.of(termString2)).parseTermPair();
    }

    /**
     * Creates a deep copy of the term pair while preserving
     * structure of the terms.
     *
     * @param pair pair of terms
     * @return deep copy of the pair of terms
     * @throws NullPointerException if {@code pair} is null
     */
    public static TermPair copyOf(@NotNull final TermPair pair) {
        Objects.requireNonNull(pair);
        return of(pair.term1(), pair.term2());
    }

    /**
     * Constructs a {@code TermPair} instance from a provided pair of
     * {@code Term} instances. This method does not modify provided terms.
     * Instead, it creates a copy of each term and modifies created copies.
     *
     * @param term1 first term
     * @param term2 second term
     * @return pair of terms
     * @throws NullPointerException if either {@code term1} or {@code term2} is null
     */
    public static TermPair of(@NotNull final Term term1, @NotNull final Term term2) {
        Objects.requireNonNull(term1);
        Objects.requireNonNull(term2);
        Map<Term, Term> isomorphism = new IdentityHashMap<>();
        return new TermPair(
                term1.deepCopy(isomorphism),
                term2.deepCopy(isomorphism)
        );
    }
}
