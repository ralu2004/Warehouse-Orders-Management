package app.bll;

import app.bll.exceptions.InvalidAddressException;
import app.bll.exceptions.InvalidEmailException;
import app.bll.validators.AddressValidator;
import app.bll.validators.EmailValidator;
import app.bll.validators.Validator;
import app.dao.ClientDAO;
import app.model.Client;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Business Logic Layer for managing clients.
 * <p>
 * This class encapsulates validation and database operations for {@link Client} entities.
 * It integrates data validation using a list of {@link Validator} implementations.
 */
public class ClientBLL {

    /**
     * Logger instance for capturing runtime events or errors.
     */
    private static final Logger LOGGER = Logger.getLogger(ClientBLL.class.getName());

    /**
     * Data Access Object used to perform database operations related to clients.
     */
    private ClientDAO clientDAO;

    /**
     * A list of validators applied to {@link Client} objects before insert or update operations.
     */
    private List<Validator<Client>> validators;

    /**
     * Constructs a new {@code ClientBLL} instance and initializes its DAO and validators.
     */
    public ClientBLL() {
        validators = new ArrayList<>();
        validators.add(new EmailValidator());
        validators.add(new AddressValidator());
        this.clientDAO = new ClientDAO();
    }

    /**
     * Creates the clients table in the database if it does not exist.
     */
    public void createTable() {
        try{
            clientDAO.createTableIfNotExists();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating clients table", e);
        }
    }

    /**
     * Inserts a validated client into the database.
     *
     * @param client the client to insert
     * @throws IllegalArgumentException if validation fails
     */
    public void insertClient(Client client) throws IllegalArgumentException {
        try {
            validators.forEach(validator -> validator.validate(client));
            clientDAO.insert(client);
        } catch (SQLException | IllegalAccessException e) {
            LOGGER.log(Level.SEVERE, "Error inserting client: " + client, e);
        } catch (InvalidEmailException | InvalidAddressException e) {
            throw e;
        }
    }

    /**
     * Finds a client by their ID.
     *
     * @param id the client's ID
     * @return the found client or {@code null} if not found or an error occurs
     */
    public Client findClientById(int id) {
        try {
            return clientDAO.findById(id);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding client with ID: " + id, e);
            return null;
        }
    }

    /**
     * Retrieves all clients from the database.
     *
     * @return a list of all clients, or an empty list if an error occurs
     */
    public List<Client> findAllClients() {
        try {
            return clientDAO.findAll();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all clients", e);
            return List.of();
        }
    }

    /**
     * Updates an existing client after validation.
     *
     * @param client the client to update
     * @throws IllegalArgumentException if validation fails
     */
    public void updateClient(Client client) {
        try {
            validators.forEach(validator -> validator.validate(client));
            clientDAO.update(client);
        } catch (SQLException | IllegalAccessException e) {
            LOGGER.log(Level.SEVERE, "Error updating client: " + client, e);
        } catch (InvalidEmailException | InvalidAddressException e) {
            throw e;
        }
    }

    /**
     * Deletes a client from the database by ID.
     *
     * @param id the ID of the client to delete
     * @throws Exception if delete fails
     */
    public void deleteClient(int id) throws Exception {
        try {
            clientDAO.delete(id);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting client with ID: " + id, e);
            throw e;
        }
    }
}