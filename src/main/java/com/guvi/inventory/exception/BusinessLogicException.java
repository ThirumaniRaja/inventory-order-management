package com.guvi.inventory.exception;

/**
 * Exception thrown when an operation violates business rules or constraints
 */
public class BusinessLogicException extends RuntimeException {
    private final String errorCode;

    public BusinessLogicException(String message) {
        super(message);
        this.errorCode = null;
    }

    public BusinessLogicException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessLogicException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = null;
    }

    public BusinessLogicException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

