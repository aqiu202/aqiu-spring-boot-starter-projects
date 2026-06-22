package com.github.aqiu202.cron.exp;

public class InvalidCronException extends RuntimeException {

    public InvalidCronException(String message) {
        super(message);
    }

    public InvalidCronException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCronException(Throwable cause) {
        super(cause);
    }
}
