package syntax;

import org.jetbrains.annotations.NotNull;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;

public record TermPair(
        Term term1,
        Term term2
) {
    public TermPair {
        Objects.requireNonNull(term1);
        Objects.requireNonNull(term2);
    }

    public static TermPair fromStrings(
            @NotNull final String termString1,
            @NotNull final String termString2
    ) {
        return new TermDagParser(
                TokenIterator.of(termString1),
                TokenIterator.of(termString2)).parseTermPair();
    }

    public static TermPair copyOf(@NotNull final TermPair pair) {
        Objects.requireNonNull(pair);
        Map<Term, Term> isomorphism = new IdentityHashMap<>();
        return new TermPair(
                pair.term1.deepCopy(isomorphism),
                pair.term2.deepCopy(isomorphism)
        );
    }
}
