package com.github.aqiu202.util.retry;

public class OutNumberOfTimesException extends RuntimeException {
    public OutNumberOfTimesException(String message, Exception e) {
        super(message, e);
    }
}
