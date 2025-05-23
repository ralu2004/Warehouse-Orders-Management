package app.dao;

import javafx.collections.FXCollections;
import app.connection.DbConnection;
import app.model.Order;
import app.model.OrderDetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO class for handling database operations related to the {@link Order} entity.
 * Inherits CRUD operations from {@link AbstractDAO} and provides additional functionality
 * for fetching detailed order data.
 */
public class OrderDAO extends AbstractDAO<Order> {

    /**
     * Constructs a new {@code OrderDAO} for handling {@link Order} entities.
     */
    public OrderDAO() {
        super(Order.class);
    }

    /**
     * Retrieves a list of detailed order information by joining tables Orders, Clients and Products.
     *
     * @return a list of {@link OrderDetails} objects representing detailed order data
     * @throws SQLException if a database access error occurs
     */
    public List<OrderDetails> getDetailedOrders() throws SQLException {
        List<OrderDetails> orders = FXCollections.observableArrayList();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DbConnection.getConnection();
            String query = "SELECT o.id AS order_id, CONCAT(c.first_name, ' ', c.last_name) AS client_name, " +
                    "p.name AS product_name, o.quantity, o.total_price, o.order_date " +
                    "FROM Orders o " +
                    "JOIN Clients c ON o.client_id = c.id " +
                    "JOIN Products p ON o.product_id = p.id";

            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                orders.add(new OrderDetails(
                        rs.getInt("order_id"),
                        rs.getString("client_name"),
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("total_price"),
                        rs.getTimestamp("order_date")
                ));
            }
        } catch(SQLException e) {
            LOGGER.severe(String.format("Error generating order details: %s", e.getMessage()));
            throw e;
        } finally {
            DbConnection.closeStatement(ps);
            DbConnection.closeResultSet(rs);
            DbConnection.closeConnection(con);
        }

        return orders;
    }

    /**
     * Retrieves the ID of the last inserted order.
     *
     * @return the last inserted order ID, or -1 if it doesn't exist
     * @throws SQLException if a database access error occurs
     */
    public int getLastId() throws SQLException {
        String query = "SELECT id FROM orders ORDER BY id DESC LIMIT 1";

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("id");
            } else {
                return -1;
            }
        }
    }
}
