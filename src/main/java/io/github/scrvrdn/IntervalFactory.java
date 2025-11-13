package io.github.scrvrdn;

import java.util.Random;

public class IntervalFactory {
    private static final int MAX_MIDI_VALUE = 108;
    private static final int MIN_MIDI_VALUE = 21;

    // random number generator
    private final Random rng = new Random();

    // bit vector to represent the available interval directions (see AppConstans)
    private byte intervalDirections;
    
    // number of available interval directions, is equivalent to the number of set bits in intervalDirections
    private int numOfDirections;

    // set of intervals to choose from represented as a bit victor
    private int intervalSet;

    // number of intervals in the set (equivalent to number of set bits in intervalSet)
    private int numOfIntervals;

    public IntervalFactory() {
        this.intervalDirections = AppConstants.ASCENDING | AppConstants.DESCENDING | AppConstants.SIMULTANEOUS;
        this.numOfDirections = 3;

        this.intervalSet = AppConstants.MAX_INTERVALS;
        this.numOfIntervals = 20;
    }

    // returns a random interval from the interval set
    public QueriedInterval buildInterval() {
        // choose an interval at random
        Interval interval = getRandomInterval();

        // choose randomly the midi value of the lower note
        int lo = MIN_MIDI_VALUE + this.rng.nextInt(MAX_MIDI_VALUE - MIN_MIDI_VALUE - interval.getSemitones() + 1);

        // choose direction
        byte direction = getRandomDirection();

        return new QueriedInterval(interval, lo, direction);
    }

    private Interval getRandomInterval() {
        int n = this.rng.nextInt(this.numOfIntervals);
        
        // find n(+ 1)th set bit
        int bitMask = this.intervalSet;
        int count = 0;
        int i = 0;
        while (bitMask != 0) {
            i = Integer.numberOfTrailingZeros(bitMask);
            if (count == n) break;
            bitMask &= bitMask - 1; // clear least significant set bit
            count++;
        }
        
        return Interval.getInterval(i);
    }

    // chooses randomly from the available directions; returns a direction encoded as bit victor
    private byte getRandomDirection() {
        if (this.numOfDirections == 1) return this.intervalDirections;

        int bits = this.rng.nextInt(this.numOfDirections);
        byte dir = (byte) (this.intervalDirections & -this.intervalDirections); // set dir as the right most bit of intervalDirections
        dir <<= bits;
        if ((this.intervalDirections & dir) != dir) dir <<= 1;
        return dir;
    }

    // takes as input a direction encoded as a bit vector and updates available interval directions
    public void setIntervalDirections(byte newDirections) {
        if (newDirections > (AppConstants.ASCENDING | AppConstants.DESCENDING | AppConstants.SIMULTANEOUS) || newDirections < AppConstants.ASCENDING) throw new IllegalArgumentException();
        this.intervalDirections = newDirections;
        this.numOfDirections = Integer.bitCount(newDirections);
    }

    // updates the intervals included in the query set, represented as a bit vector;
    public void setIntervals(int newIntervals) {
        if (newIntervals < AppConstants.MIN_INTERVALS ||newIntervals > AppConstants.MAX_INTERVALS) throw new IllegalArgumentException();

        this.intervalSet = newIntervals;
        this.numOfIntervals = Integer.bitCount(newIntervals);
    }

    // returns true if the interval passed as argument is included in the interval set
    public boolean contains(Interval interval) {
        if (interval == null) throw new IllegalArgumentException();

        int i = 1 << interval.getSemitones();
        return (this.intervalSet & i) == i;
    }
}
