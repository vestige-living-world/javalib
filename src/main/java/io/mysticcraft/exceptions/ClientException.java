package io.mysticcraft.exceptions;

/** @deprecated Use {@link io.vestige.exceptions.ClientException} */
@Deprecated
public class ClientException extends MysticCraftException {
    public ClientException(String message) { super(message); }
}
