package app.bll.validators;

import app.model.Client;
import java.util.regex.Pattern;

/**
 * Validator implementation for validating the {@code address} field of a {@link Client}.
 * <p>
 * Ensures that the address contains only allowed characters such as letters,
 * numbers, spaces, periods, commas, dashes, hash symbols, and apostrophes.
 */
public class AddressValidator implements Validator<Client> {

    /**
     * Regular expression pattern used to validate addresses.
     * Allows letters, digits, spaces, '.', ',', '-', '#', and '\'' characters.
     */
    private static final String ADDRESS_PATTERN = "^[A-Za-z0-9#.,'\\s-]+$";

    /**
     * Default constructor for AddressValidator.
     */
    public AddressValidator() {
    }

    /**
     * Validates the {@code address} field of a {@link Client} instance.
     *
     * @param t the client whose address will be validated
     * @throws IllegalArgumentException if the address does not match the allowed pattern
     */
    @Override
    public void validate(Client t) {
        Pattern pattern = Pattern.compile(ADDRESS_PATTERN);
        if (!pattern.matcher(t.getAddress()).matches()) {
            throw new IllegalArgumentException("Address is not valid!\n Allowed characters: letters, number '.', ',', '#', '-', and spaces.");
        }
    }
}
