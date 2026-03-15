package com.github.tavi.lilbird.api.exception;


/**
 * This exception should be used to back-propagate the most common
 * server exceptions (usually they indicate HTTP 400).
 * <p>
 * If some exception is not back-propagated, it is considered unhandled,
 * and the code must be adjusted to handle it accordingly.
 * <p>
 * Should not be used to wrap ambiguous exceptions 
 * (such as {@code RuntimeException} or {@code Exception});
 * they are considered HTTP 500.
 */
public class HandledServerException extends RuntimeException {

    /**
     * Creates a new handled exception with the provided
     * details message.
     * 
     * @param details       This message must specify the cause
     *                      of the exception.
     */
    public HandledServerException(final String details) {
        super(details);
    }

}
