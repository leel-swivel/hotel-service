package com.hilltop.exception;

/**
 * HotelServiceException
 */
public class HotelServiceException extends RuntimeException {

    /**
     * BaseComponentException Exception with error message.
     *
     * @param errorMessage error message
     */
    public HotelServiceException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * Authentication Exception with error message and throwable error
     *
     * @param errorMessage error message
     * @param error        error
     */
    public HotelServiceException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }

}