package io.mysticcraft.exceptions;

/** @deprecated Use {@link io.vestige.exceptions.NetworkException} */
@Deprecated
public class NetworkException extends MysticCraftException {
    public NetworkException(String message) { super(message); }
    public NetworkException(String message, Throwable cause) { super(message, cause); }
}
