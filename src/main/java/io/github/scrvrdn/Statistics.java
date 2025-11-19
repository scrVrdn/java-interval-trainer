package io.github.scrvrdn;

public class Statistics {
    private int total;
    private int correct;

    private Statistics() {
        this.total = 0;
        this.correct = 0;
    }

    private static class StatsHolder {
        private static final Statistics INSTANCE = new Statistics();
    }

    public static Statistics getInstance() {
        return StatsHolder.INSTANCE;
    }

    public void incrementTotal() {
        this.total++;
    }

    public void incrementCorrect() {
        this.correct++;
    }

    public int getTotal() {
        return this.total;
    }

    public int getCorrect() {
        return this.correct;
    }

    // returns average of correct answers rounded to the nearest integer
    public int getAverage() {
        if (this.total == 0) return 0;
        return (int) (100.0 * this.correct / this.total + 0.5);
    }
}
