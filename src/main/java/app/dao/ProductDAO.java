package app.dao;

import app.connection.DbConnection;
import app.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO class for handling database operations related to the {@link Product} entity.
 * Extends the generic {@link AbstractDAO} to inherit basic CRUD functionality.
 */
public class ProductDAO extends AbstractDAO<Product>{

    /**
     * Constructs a new {@code ProductDAO} for performing operations on {@link Product} entities.
     */
    public ProductDAO() {
        super(Product.class);
    }

    /**
     * Decreases the stock of a product given by its id by a given quantity.
     *
     * @param productId the ID of the product whose stock is to be decreased
     * @param quantity the quantity by which the stock will be decreased
     * @throws SQLException if a database access error occurs
     */
    public void decreaseStock(int productId, int quantity) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DbConnection.getConnection();

            String checkStockQuery = "SELECT stock FROM products WHERE id = ?";
            ps = con.prepareStatement(checkStockQuery);
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int currentStock = rs.getInt("stock");
                if (currentStock < quantity) {
                    throw new IllegalStateException("Not enough stock available!");
                }
            }

            String updateQuery = "UPDATE products SET stock = stock - ? WHERE id = ?";
            ps = con.prepareStatement(updateQuery);
            ps.setInt(1, quantity);
            ps.setInt(2, productId);
            ps.executeUpdate();
        } finally {
            DbConnection.closeStatement(ps);
            DbConnection.closeConnection(con);
        }
    }
}
