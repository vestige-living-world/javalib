package io.mysticcraft.exceptions;

/** @deprecated Use {@link io.vestige.exceptions.VestigeException} */
@Deprecated
public class MysticCraftException extends RuntimeException {
    public MysticCraftException(String message) { super(message); }
    public MysticCraftException(String message, Throwable cause) { super(message, cause); }
}
