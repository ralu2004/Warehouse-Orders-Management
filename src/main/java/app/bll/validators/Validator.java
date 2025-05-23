package app.bll.validators;

/**
 * Generic interface for implementing validation logic on objects of type {@code T}.
 */
public interface Validator<T> {

    /**
     * Validates the given object of type {@code T}.
     *
     * @param t the object to validate
     */
    void validate(T t);
}
