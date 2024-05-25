package syntax;

import org.jetbrains.annotations.NotNull;

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

}
