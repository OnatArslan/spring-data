package com.onatarslan.springdata.todo;

public record Priority(
        int value
) {

    public static final int MIN = 1;
    public static final int MAX = 5;


    public Priority {
        if (value < MIN || value > MAX) {
            throw new IllegalArgumentException("Priority must be between %d and %d: %d".formatted(MIN, MAX, value));
        }

    }

    public static Priority of(int value) {
        return new Priority(value);
    }

}
