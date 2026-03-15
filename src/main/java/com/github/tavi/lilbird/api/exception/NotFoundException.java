package com.github.tavi.lilbird.api.exception;


/**
 * A simple class designed specifically for HTTP 404 responses.
 */
public class NotFoundException extends RuntimeException {

    /**
     * Creates a new HTTP 404 exception with the provided
     * details message.
     * 
     * @param details       This message must specify the details
     *                      about the exception.
     */
    public NotFoundException(final String details) {
        super(details);
    }

}
