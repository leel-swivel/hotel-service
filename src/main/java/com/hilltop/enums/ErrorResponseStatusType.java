package com.hilltop.enums;

import lombok.Getter;

/**
 * ErrorResponseStatusType
 */
@Getter
public enum ErrorResponseStatusType {


    INTERNAL_SERVER_ERROR(5000, "Internal server error."),
    INVALID_HOTEL_ID(4000,"Invalid hotel id."),
    MISSING_REQUIRED_FIELDS(4001,"Required fields are missing.");
    private final int code;
    private final String message;

    ErrorResponseStatusType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Error code covert into string to read display message from error property file
     *
     * @param errorCode errorCode
     * @return errorCode as string
     */
    public static String getCodeString(int errorCode) {
        return Integer.toString(errorCode);
    }

}
