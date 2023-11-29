package util;

/**
 * This utility class contains methods for checking some conditions
 * during execution of the code and throwing exceptions whether
 * violation is found
 */
public final class Assertions {
    /**
     * Do not instantiate this class
     */
    private Assertions() {
        throw new AssertionError("Do not instantiate this class");
    }

    /**
     * Throws {@link IllegalStateException} with provided message
     * if {@code condition} is false.
     *
     * @param condition condition to check
     * @param message message to show if exception is thrown.
     * @throws IllegalStateException if {@code condition} is false.
     */
    public static void check(boolean condition, final String message) {
        if (!condition)
            throw new IllegalStateException(message);
    }

    /**
     * Throws {@link IllegalArgumentException} with provided message
     * if {@code condition} is false.
     *
     * @param condition condition to check
     * @param message message to show if exception is thrown.
     * @throws IllegalArgumentException if {@code condition} is false.
     */
    public static void require(boolean condition, final String message) {
        if  (!condition)
            throw new IllegalArgumentException(message);
    }
}
