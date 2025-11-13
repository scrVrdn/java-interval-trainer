package io.github.scrvrdn;

public enum PitchClass {
    C, C_SHARP, D, D_SHARP, E, F, F_SHARP, G, G_SHARP, A, A_SHARP, B;

    private static final PitchClass[] PITCH_CLASSES = values();

    public static PitchClass getPitchClass(int i) {
        if (i < 0 || i >= PITCH_CLASSES.length) throw new IllegalArgumentException(String.format("Input has to be in the range [0, %d).", PITCH_CLASSES.length));
        return PITCH_CLASSES[i];
    }

    public int getMidiValue() {
        return this.ordinal();
    }

}
