package app.bll;

import app.dao.BillDAO;
import app.model.Bill;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Business Logic Layer for managing bills.
 * <p>
 * Serves as an intermediary between the data access layer and presentation and
 * controller layers. It manipulates {@link Bill} entities using {@link BillDAO} methods.
 */
public class BillBLL {

    /**
     * Logger instance for capturing runtime events or errors.
     */
    private static final Logger LOGGER = Logger.getLogger(BillBLL.class.getName());

    /**
     * Data access object for handling bill-related database operations.
     */
    private final BillDAO billDAO;

    /**
     * Constructs a new {@code BillBLL} instance and initializes the associated DAO.
     */
    public BillBLL() {
        this.billDAO = new BillDAO();
    }

    /**
     * Creates the database table for bills if it does not already exist.
     */
    public void createTable() {
        try{
            billDAO.createTableIfNotExists();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating Bills Log table", e);
        }
    }

    /**
     * Generates and inserts a new bill into the database.
     *
     * @param bill the bill object to insert
     * @throws SQLException if a database access error occurs
     * @throws IllegalAccessException if the bill's fields are inaccessible via reflection
     */
    public void generateBill(Bill bill) throws SQLException, IllegalAccessException {
        try{
            billDAO.insert(bill);
        } catch(Exception e){
            LOGGER.log(Level.SEVERE, "Error generating bill", e);
            throw e;
        }
    }

    /**
     * Retrieves a bill by its associated order ID.
     *
     * @param id the order ID
     * @return the matching bill, or {@code null} if not found or an error occurs
     */
    public Bill getBillByOrderId(int id) {
        try {
            return billDAO.getBillByOrderId(id);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting bill by order id", e);
        }
        return null;
    }
}
