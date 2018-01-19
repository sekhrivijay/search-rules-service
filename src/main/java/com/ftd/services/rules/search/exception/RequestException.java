package com.ftd.services.rules.search.exception;

import org.springframework.http.HttpStatus;

public class RequestException extends AbstractException {

    private static final long serialVersionUID = 1L;

    public RequestException(HttpStatus statusCode, String message, Object... substitutionValues) {
        super(statusCode, message, substitutionValues);
    }
}
