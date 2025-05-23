package app.bll.exceptions;

/**
 * Exception thrown to indicate that a provided address is invalid.
 */
public class InvalidAddressException extends IllegalArgumentException {

    /**
     * Constructs a new {@code InvalidAddressException} with the specified message.
     *
     * @param message the message explaining why the address is considered invalid
     */
    public InvalidAddressException(String message) {
        super(message);
    }
}
