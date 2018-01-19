package com.ftd.services.rules.search.controller;

import java.lang.reflect.UndeclaredThrowableException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestfulExceptionHandler extends ResponseEntityExceptionHandler {

    public static class RestfulBaseException {
        private String msg;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

    }

    @ExceptionHandler(value = { UndeclaredThrowableException.class })
    protected ResponseEntity<Object> handleConflict(UndeclaredThrowableException ex, WebRequest request) {
        RestfulBaseException bd = new RestfulBaseException();
        bd.setMsg(ex.getUndeclaredThrowable().getLocalizedMessage());
        return handleExceptionInternal(ex, bd, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}