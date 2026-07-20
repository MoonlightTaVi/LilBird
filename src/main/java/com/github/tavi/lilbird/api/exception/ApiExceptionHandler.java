package com.github.tavi.lilbird.api.exception;

import org.springframework.dao.OptimisticLockingFailureException;
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


    /** {@link HandledServerException} (HTTP 400). */
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

    /** {@link NotFoundException} (HTTP 404). */
    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleServerException(
        final NotFoundException e, final WebRequest request
    ) 
    {
        return ProblemDetail
                .forStatusAndDetail(
                    HttpStatus.NOT_FOUND, 
                    "The requested resource is not found"
                );
    }

    /** Jakarta bean/DTO validation exception (HTTP 400). */
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

    /** Database optimistic lock on existing data update (HTTP 423). */
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ProblemDetail handleServerException(
        final OptimisticLockingFailureException e, final WebRequest request
    )
    {
        return ProblemDetail
                .forStatusAndDetail(
                    HttpStatus.LOCKED, 
                    "The resource is currently locked"
                );
    }


}
