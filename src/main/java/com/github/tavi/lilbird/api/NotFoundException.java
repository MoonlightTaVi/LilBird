package com.github.tavi.lilbird.api;


/**
 * This class extends {@link HandledServerException} and is used
 * specifically for HTTP 404 responses.
 */
public class NotFoundException extends HandledServerException {

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
