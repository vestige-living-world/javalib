package io.vestige.exceptions;

/**
 * Thrown when the server returns a 429 Too Many Requests response.
 */
public class RateLimitException extends ClientException {

    private final long retryAfterMs;

    public RateLimitException(String message, long retryAfterMs) {
        super(message, 429, "rate_limit_error");
        this.retryAfterMs = retryAfterMs;
    }

    public long getRetryAfterMs() { return retryAfterMs; }
}
