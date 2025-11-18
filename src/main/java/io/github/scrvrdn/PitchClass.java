package io.github.scrvrdn;

public enum PitchClass {
    C, C_SHARP, D, E_FLAT, E, F, F_SHARP, G, G_SHARP, A, B_FLAT, B;

    private static final PitchClass[] PITCH_CLASSES = values();

    public static PitchClass getPitchClass(int i) {
        if (i < 0 || i >= PITCH_CLASSES.length) throw new IllegalArgumentException(String.format("Input has to be in the range [0, %d).", PITCH_CLASSES.length));
        return PITCH_CLASSES[i];
    }

    public int getMidiValue() {
        return this.ordinal();
    }

    @Override
    public String toString() {
        switch(this.ordinal()) {
            case 0:
                return "C";
            case 1:
                return "C\u266F";
            case 2:
                return "D";
            case 3:
                return "E\u266D";
            case 4:
                return "E";
            case 5:
                return "F";
            case 6:
                return "F\u266F";
            case 7:
                return "G";
            case 8:
                return "G\u266F";
            case 9:
                return "A";
            case 10:
                return "B\u266D";
            case 11:
                return "B";
        }
        
        return "";
    }
}
