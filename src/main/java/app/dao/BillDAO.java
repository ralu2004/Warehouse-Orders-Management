package app.dao;

import app.connection.DbConnection;
import app.model.Bill;

import java.sql.*;

/**
 * DAO class for handling database operations specific to the {@link Bill} entity.
 * Extends the generic {@link AbstractDAO} to provide default behavior and custom operations.
 */
public class BillDAO extends AbstractDAO<Bill> {

    /**
     * Constructs a new {@code BillDAO} for handling {@link Bill} entities.
     */
    public BillDAO() {
        super(Bill.class);
    }

    /**
     * Overrides the update method to prevent modification of {@link Bill} records.
     * Bills are immutable once generated.
     *
     * @param bill the bill object to update
     * @throws UnsupportedOperationException always, since updates are not supported
     */
    @Override
    public void update(Bill bill) throws SQLException, IllegalAccessException {
        throw new UnsupportedOperationException("Updates are not allowed for Bill records.");
    }

    /**
     * Retrieves a {@link Bill} from the database using the associated order ID.
     *
     * @param orderId the ID of the order for which the bill is to be fetched
     * @return the {@link Bill} object if found, or {@code null} if no matching bill exists
     */
    public Bill getBillByOrderId(int orderId) {
        String query = "SELECT * FROM Log WHERE orderId = ?";

        try (
                Connection con = DbConnection.getConnection();
                PreparedStatement stmt = con.prepareStatement(query)
        ) {
            stmt.setInt(1, orderId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    double amount = rs.getDouble("amount");
                    Timestamp timestamp = rs.getTimestamp("timestamp");
                    return new Bill(id, orderId, amount, timestamp);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error fetching Bill by orderId: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

