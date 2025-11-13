package io.github.scrvrdn;
public class AppConstants {
    // bit representation of directions
    public static final byte ASCENDING = 0b001;
    public static final byte DESCENDING = 0b010;
    public static final byte SIMULTANEOUS = 0b100;

    // bit representation of intervals
    public static final int MAX_INTERVALS = 0xFFFFF;
    public static final int MIN_INTERVALS = 1;
}