package app.model;
import app.annotations.Column;
import app.annotations.Table;

import java.sql.Timestamp;

/**
 * Represents a bill record in the system, storing details about a transaction.
 * @param id the unique identifier of the bill
 * @param orderId the associated order ID for this bill
 * @param amount the total amount of the transaction (to be paid by the customer)
 * @param timestamp the time when the bill was generated
 */
@Table(name = "Log")
public record Bill(
        @Column(name = "id", type = "INT", primaryKey = true, nullable = false, updatable = false)
        Integer id,

        @Column(name = "orderId", type = "INT", nullable = false, updatable = false)
        int orderId,

        @Column(name = "amount", type = "DOUBLE PRECISION", nullable = false, updatable = false)
        double amount,

        @Column(name = "timestamp", type = "TIMESTAMP", nullable = false, updatable = false)
        Timestamp timestamp
) {}
