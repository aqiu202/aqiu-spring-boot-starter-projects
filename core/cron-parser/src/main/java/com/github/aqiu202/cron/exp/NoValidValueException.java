package com.github.aqiu202.cron.exp;

public class NoValidValueException extends RuntimeException {

    public NoValidValueException(String message) {
        super(message);
    }

    public NoValidValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoValidValueException(Throwable cause) {
        super(cause);
    }
}
