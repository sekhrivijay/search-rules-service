package com.ftd.services.rules.search.exception;

import java.lang.reflect.UndeclaredThrowableException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class SearchExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchExceptionHandler.class);

    @Value("${exceptionHandler.logForHttpStatusAndAbove:400}")
    private int                 loggingForHttpStatus;

    public static class TranslatedExceptionMessage {

        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String msg) {
            this.message = msg;
        }
    }

    /**
     * This method handles all checked exceptions; subclasses of Exception.
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(value = { UndeclaredThrowableException.class })
    protected ResponseEntity<Object> handleConflict(UndeclaredThrowableException ex, WebRequest request) {
        TranslatedExceptionMessage bd = new TranslatedExceptionMessage();

        bd.setMessage(ex.getUndeclaredThrowable().getLocalizedMessage());

        LOGGER.warn("UndeclaredThrowableException {} {}",
                ex.getUndeclaredThrowable().getClass().getSimpleName(),
                bd.getMessage());

        return handleExceptionInternal(ex, bd, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    /**
     * This handles all non-checked exceptions; subclasses of RuntimeException.
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(value = { AbstractException.class })
    protected ResponseEntity<Object> handleConflict(AbstractException ex, WebRequest request) {
        TranslatedExceptionMessage translatedMessage = new TranslatedExceptionMessage();
        translatedMessage.setMessage(ex.getLocalizedMessage());

        if (ex.getStatusCode().value() >= loggingForHttpStatus) {
            LOGGER.warn("{} {} @ {}.{} [{}]",
                    ex.getClass().getSimpleName(),
                    translatedMessage.getMessage(),
                    ex.getStackTraceElement().getClassName(),
                    ex.getStackTraceElement().getMethodName(),
                    ex.getStackTraceElement().getLineNumber());
        }

        return handleExceptionInternal(ex, translatedMessage, new HttpHeaders(), ex.getStatusCode(), request);
    }
}