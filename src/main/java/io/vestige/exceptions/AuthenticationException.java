package io.vestige.exceptions;

/**
 * Thrown when the server returns a 401 Unauthorized or 403 Forbidden response.
 */
public class AuthenticationException extends ClientException {

    public AuthenticationException(String message, int statusCode) {
        super(message, statusCode, "authentication_error");
    }
}
