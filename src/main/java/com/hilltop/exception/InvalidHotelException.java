package com.hilltop.exception;

/**
 * InvalidHotelException
 */
public class InvalidHotelException extends HotelServiceException{
    public InvalidHotelException(String errorMessage) {
        super(errorMessage);
    }
}
