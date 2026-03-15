package com.github.tavi.lilbird.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ValidationException;


/**
 * The exception handler that automatically produces HTTP responses
 * with standardized problem details messages when the server throws
 * exceptions.
 */
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {


    /**
     * {@link HandledServerException} indicates HTTP 400.
     */
    @ExceptionHandler(HandledServerException.class)
    public ProblemDetail handleServerException(
        final HandledServerException e, final WebRequest request
    ) 
    {
        return ProblemDetail
                .forStatusAndDetail(
                    HttpStatus.BAD_REQUEST, 
                    e.getLocalizedMessage()
                );
    }

    /**
     * {@link NotFoundException} indicates HTTP 404.
     */
    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleServerException(
        final NotFoundException e, final WebRequest request
    ) 
    {
        return ProblemDetail
                .forStatusAndDetail(
                    HttpStatus.NOT_FOUND, 
                    e.getLocalizedMessage()
                );
    }

    /**
     * Jakarta bean/DTO validation exception.
     */
    @ExceptionHandler(ValidationException.class)
    public ProblemDetail handleServerException(
        final ValidationException e, final WebRequest request
    ) 
    {
        return ProblemDetail
                .forStatusAndDetail(
                    HttpStatus.BAD_REQUEST, 
                    e.getLocalizedMessage()
                );
    }

}
