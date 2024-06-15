package unification;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import syntax.TermPair;

import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Fork(value = 1, warmups = 3)
@BenchmarkMode(Mode.AverageTime)
@Warmup(
        iterations = 3,
        time = 1,
        timeUnit = TimeUnit.MILLISECONDS)
@Measurement(
        iterations = 5,
        time = 1, timeUnit =
        TimeUnit.MILLISECONDS)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class UnificationComplexityTest {

    private TermPair termPair;

    @Param({"2", "3", "4", "5",
            "6", "7", "8", "9", "10",
            "20", "30", "40", "50",
            "60", "70", "80", "90", "100",
            "200", "300", "400", "500", "600",
            "700", "800", "900", "1000",
            "2000", "3000", "4000", "5000", "6000",
            "7000", "8000", "9000", "10000"})
    private int termLength;

    private String prepareTerm1(int termLength) {
        StringJoiner joiner =
                new StringJoiner(",", "f1(", ")");
        for (int i = 1; i <= termLength; i++)
            joiner.add(String.format("f(x%d,x%d)", i, i));
        return joiner.toString();
    }

    private String prepareTerm2(int termLength) {
        StringJoiner joiner =
                new StringJoiner(",", "f1(", ")");
        for (int i = 2; i <= termLength + 1; i++)
            joiner.add(String.format("x%d", i));
        return joiner.toString();
    }

    @Setup(Level.Iteration)
    public void setUp() {
        termPair =
                TermPair.fromStrings(
                        prepareTerm1(termLength),
                        prepareTerm2(termLength));
    }

    private UnificationResult benchmarkUnification(
            UnificationStrategy strategy) {
        return strategy.findUnifier(termPair);
    }

    @Benchmark
    public UnificationResult benchmarkRobinson() {
        return benchmarkUnification(
                new RobinsonUnificationStrategy());
    }

    @Benchmark
    public UnificationResult benchmarkPolyRobinson() {
        return benchmarkUnification(
                new PolynomialRobinsonUnificationStrategy());
    }

    @Benchmark
    public UnificationResult benchmarkPatersonWegman() {
        return benchmarkUnification(
                new PatersonWegmanUnificationStrategy());
    }
}
