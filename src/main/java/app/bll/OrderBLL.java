package app.bll;

import app.dao.OrderDAO;
import app.model.Order;
import app.model.OrderDetails;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Business Logic Layer for managing orders.
 * <p>
 * This class encapsulates database operations for {@link Order} and {@link OrderDetails} entities.
 */
public class OrderBLL {

    /**
     * Logger instance for capturing runtime events or errors.
     */
    private static final Logger LOGGER = Logger.getLogger(OrderBLL.class.getName());

    /**
     * Data Access Object used to perform database operations related to orders.
     */
    private final OrderDAO orderDAO;

    /**
     * Constructs a new {@code OrderBLL} instance and initializes its DAO.
     */
    public OrderBLL() {
        this.orderDAO = new OrderDAO();
    }

    /**
     * Creates the orders table in the database if it does not exist.
     */
    public void createTable() {
        try{
            orderDAO.createTableIfNotExists();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating orders table", e);
        }
    }

    /**
     * Inserts an order into the database.
     *
     * @param order the order to insert
     */
    public void insertOrder(Order order) {
        try {
            orderDAO.insert(order);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inserting order: " + order, e);
        }
    }

    /**
     * Finds an order by their ID.
     *
     * @param id the order's ID
     * @return the found order or {@code null} if not found or an error occurs
     */
    public Order findOrderById(int id) {
        try {
            return orderDAO.findById(id);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding order with ID: " + id, e);
            return null;
        }
    }

    /**
     * Deletes an order from the database by ID.
     *
     * @param id the ID of the order to delete
     */
    public void deleteOrder(int id) {
        try {
            orderDAO.delete(id);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting order with ID: " + id, e);
        }
    }

    /**
     * Retrieves all detailed orders.
     *
     * @return a list of all detailed orders, or an empty list if an error occurs
     */
    public List<OrderDetails> getDetailedOrders() {
        try {
            return orderDAO.getDetailedOrders();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving order details", e);
            return null;
        }
    }

    /**
     * Retrieves the ID of the last inserted order.
     *
     * @return the ID of the last inserted order, or 0 if an error occurs
     */
    public int getLastId() {
        try{
            return orderDAO.getLastId();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error receiving order details", e);
            return 0;
        }
    }
}