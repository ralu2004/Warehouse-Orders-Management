package app.bll;

import app.dao.ProductDAO;
import app.model.Product;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Business Logic Layer for managing products.
 * <p>
 * This class encapsulates database operations for {@link Product} entities.
 */
public class ProductBLL {

    /**
     * Logger instance for capturing runtime events or errors.
     */
    private static final Logger LOGGER = Logger.getLogger(ProductBLL.class.getName());

    /**
     * Data Access Object used to perform database operations related to products.
     */
    private final ProductDAO productDAO;

    /**
     * Constructs a new {@code ProductBLL} instance and initializes its DAO.
     */
    public ProductBLL() {
        this.productDAO = new ProductDAO();
    }

    /**
     * Creates the products table in the database if it does not exist.
     */
    public void createTable() {
        try{
            productDAO.createTableIfNotExists();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating product table", e);
        }
    }

    /**
     * Inserts a product into the database.
     *
     * @param prod the product to insert
     */
    public void insertProduct(Product prod) {
        try {
            productDAO.insert(prod);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inserting product: " + prod, e);
        }
    }

    /**
     * Finds a product by their ID.
     *
     * @param id the product's ID
     * @return the found product or {@code null} if not found or an error occurs
     */
    public Product findProductById(int id) {
        try {
            return productDAO.findById(id);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding product with ID: " + id, e);
            return null;
        }
    }

    /**
     * Retrieves all products from the database.
     *
     * @return a list of all products, or an empty list if an error occurs
     */
    public List<Product> findAllProducts() {
        try {
            return productDAO.findAll();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all products", e);
            return List.of();
        }
    }

    /**
     * Updates an existing product.
     *
     * @param prod the client to update
     */
    public void updateProduct(Product prod) {
        try {
            productDAO.update(prod);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating product: " + prod, e);
        }
    }

    /**
     * Deletes a product from the database by ID.
     *
     * @param id the ID of the product to delete
     * @throws Exception if deletion fails
     */
    public void deleteProduct(int id) throws Exception {
        try {
            productDAO.delete(id);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting product with ID: " + id, e);
            throw e;
        }
    }

    /**
     * Decreases the stock value of a product given by ID.
     *
     * @param productId the id of the product
     * @param quantity the quantity by which the stock will be decreased
     */
    public void decreaseStock(int productId, int quantity) {
        try {
            productDAO.decreaseStock(productId, quantity);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error decreasing product stock for product with ID: " + productId, e);
        } catch (IllegalStateException e) {
            LOGGER.log(Level.SEVERE, "Not enough stock available!");
        }
    }
}