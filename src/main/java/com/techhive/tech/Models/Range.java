package com.techhive.tech.Models;

public class Range<T extends Comparable<T>> {
    private T min;
    private T max;

    public Range(T min, T max) {
        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("Min value must be less than or equal to max value");
        }
        this.min = min;
        this.max = max;
    }

    public T getMin() { return min; }
    public void setMin(T min) { this.min = min; }
    public T getMax() { return max; }
    public void setMax(T max) { this.max = max; }

    public boolean contains(T value) {
        return value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
    }

    @Override
    public String toString() {
        return "[" + min + ", " + max + "]";
    }
}