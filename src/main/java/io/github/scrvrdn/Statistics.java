package io.github.scrvrdn;

public class Statistics {
    private int total;
    private int correct;

    public Statistics() {
        this.total = 0;
        this.correct = 0;
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

    // returns average rounded to the nearest integer
    public int getAverage() {
        if (this.total == 0) return 0;
        return (this.correct + this.total / 2) / this.total;
    }
}
