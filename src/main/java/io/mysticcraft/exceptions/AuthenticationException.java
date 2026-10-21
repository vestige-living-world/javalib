package io.mysticcraft.exceptions;

/** @deprecated Use {@link io.vestige.exceptions.AuthenticationException} */
@Deprecated
public class AuthenticationException extends ClientException {
    public AuthenticationException(String message) { super(message); }
}
