package io.vestige.exceptions;

/**
 * Thrown when the server returns a 5xx server error.
 */
public class ServerException extends VestigeException {

    public ServerException(String message, int statusCode, String errorType) {
        super(message, statusCode, errorType);
    }

    public ServerException(String message, int statusCode, String errorType, Throwable cause) {
        super(message, statusCode, errorType, cause);
    }
}
