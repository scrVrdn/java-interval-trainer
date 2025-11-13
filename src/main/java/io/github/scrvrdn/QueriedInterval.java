package io.github.scrvrdn;

public final class QueriedInterval {
    // the stored interval enum
    private final Interval interval;

    // lower note of the interval as MIDI value
    private final int lo;

    // higher note of the interval as MIDI value
    private final int hi;
    
    // the direction of the interval, represented as a bit vector with 0b001 ascending, 0b010, descending and 0b100 simultaneous
    private final byte direction;

    public QueriedInterval(Interval interval, int lo, byte direction) {
        this.interval = interval;
        this.lo = lo;
        this.hi = lo + interval.getSemitones();
        this.direction = direction;

    }

    public Interval getInterval() {
        return this.interval;
    }

    public int getLo() {
        return this.lo;
    }

    public int getHi() {
        return this.hi;
    }

    public byte getDirection() {
        return this.direction;
    }

    public int getSemitones() {
        return this.interval.getSemitones();
    }
}
