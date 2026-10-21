package io.vestige.exceptions;

/**
 * Base exception for all Vestige SDK errors.
 */
public class VestigeException extends RuntimeException {

    private final int statusCode;
    private final String errorType;

    public VestigeException(String message) {
        this(message, 0, null, null);
    }

    public VestigeException(String message, Throwable cause) {
        this(message, 0, null, cause);
    }

    public VestigeException(String message, int statusCode, String errorType) {
        this(message, statusCode, errorType, null);
    }

    public VestigeException(String message, int statusCode, String errorType, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.errorType = errorType;
    }

    public int getStatusCode() { return statusCode; }
    public String getErrorType() { return errorType; }
}
