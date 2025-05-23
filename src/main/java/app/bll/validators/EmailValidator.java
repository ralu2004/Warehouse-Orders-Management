package app.bll.validators;

import app.bll.exceptions.InvalidEmailException;
import app.model.Client;
import java.util.regex.Pattern;

/**
 * Validator implementation for checking the validity of a {@link Client}'s email address.
 * <p>
 * Uses a regular expression to ensure the email conforms to standard formats.
 */
public class EmailValidator implements Validator<Client> {

    /**
     * Regular expression for validating email format.
     * Accepts letters, digits, dots, plus, underscores, and hyphens before the '@',
     * and a valid domain structure after it.
     */
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    /**
     * Default constructor for AddressValidator.
     */
    public EmailValidator() {
    }

    /**
     * Validates the {@code email} field of a {@link Client} instance.
     *
     * @param t the client whose email will be validated
     * @throws InvalidEmailException if the email does not match the required format
     */
    @Override
    public void validate(Client t) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        if (!pattern.matcher(t.getEmail()).matches()) {
            throw new InvalidEmailException("Email is not a valid email!");
        }
    }
}
