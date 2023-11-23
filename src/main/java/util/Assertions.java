package util;

public final class Assertions {
    private Assertions() {}

    public static void check(boolean condition, final String message) {
        if (!condition)
            throw new IllegalStateException(message);
    }
}
