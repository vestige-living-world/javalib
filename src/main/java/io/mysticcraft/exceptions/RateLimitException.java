package io.mysticcraft.exceptions;

/** @deprecated Use {@link io.vestige.exceptions.RateLimitException} */
@Deprecated
public class RateLimitException extends ClientException {
    public RateLimitException(String message) { super(message); }
}
