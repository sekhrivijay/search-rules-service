package com.ftd.services.rules.search.exception;

import java.text.MessageFormat;

import org.springframework.http.HttpStatus;

public abstract class AbstractException extends RuntimeException {

    private static final long   serialVersionUID           = 1L;
    private static final int    STACK_LOCATION_FOR_THROWER = 3;

    private HttpStatus          statusCode;
    private String              message;
    private StackTraceElement   stackTraceElement;

    public AbstractException(HttpStatus statusCode, String message, Object... substitutionValues) {
        super(message);

        this.message = new MessageFormat(message).format(substitutionValues);
        this.statusCode = statusCode;

        this.stackTraceElement = Thread.currentThread().getStackTrace()[STACK_LOCATION_FOR_THROWER];
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public StackTraceElement getStackTraceElement() {
        return stackTraceElement;
    }
}
