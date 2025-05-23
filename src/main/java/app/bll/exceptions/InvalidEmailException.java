package app.bll.exceptions;

/**
 * Exception thrown to indicate that a provided email address is invalid.
 */
public class InvalidEmailException extends IllegalArgumentException {

    /**
     * Constructs a new {@code InvalidEmailException} with the specified message.
     *
     * @param message the message explaining why the email is considered invalid
     */
    public InvalidEmailException(String message) {
        super(message);
    }
}
