package com.miracle.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

/**
 * Handles uncaught exceptions for MVC controllers that render JSP views.
 * Returns simple text so the browser doesn't render XML/JSON error maps.
 */
@Slf4j
@ControllerAdvice
public class JspExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleAllExceptions(Exception ex) {
        log.error("Unhandled exception caught by JspExceptionHandler", ex);
        return "An error occurred: " + ex.getMessage();
    }
}