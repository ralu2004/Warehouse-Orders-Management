package app.dao;

import app.model.Client;

/**
 * DAO class for handling database operations related to the {@link Client} entity.
 * Extends the generic {@link AbstractDAO} to inherit basic CRUD functionality.
 */
public class ClientDAO extends AbstractDAO<Client> {

    /**
     * Constructs a new {@code ClientDAO} for performing operations on {@link Client} entities.
     */
    public ClientDAO() {
        super(Client.class);
    }
}
