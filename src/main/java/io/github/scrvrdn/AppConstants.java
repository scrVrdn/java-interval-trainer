package io.github.scrvrdn;
public class AppConstants {
    // MIDI pitch range
    public static final int MAX_MIDI_VALUE = 108;
    public static final int MIN_MIDI_VALUE = 21;

    // bit representation of directions
    public static final byte ASCENDING = 0b001;
    public static final byte DESCENDING = 0b010;
    public static final byte SIMULTANEOUS = 0b100;

    // bit representation of intervals
    public static final int MAX_INTERVALS = 0xFFFFF;
    public static final int MIN_INTERVALS = 1;

    // tempo
    public static final int MAX_TEMPO = 240;
    public static final int MIN_TEMPO = 60;
    public static final int DEFAULT_TEMPO = 120;
}